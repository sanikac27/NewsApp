package com.example.project_newsapp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_newsapp.Models.NewsApiResponse;
import com.example.project_newsapp.Models.NewsHeadlines;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SelectListener, View.OnClickListener{
    RecyclerView recyclerView;
    CustomAdapter adapter;
    ProgressDialog dialog;
    Button b1,b2,b3,b4,b5,b6,b7;
    SearchView searchview;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchview=(SearchView)findViewById(R.id.search_view);

        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                dialog.setTitle("Fetching news article of "+query);
                dialog.show();
                RequestManager manager=new RequestManager(MainActivity.this);
                manager.getNewsHeadlines(listener,"general",query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        dialog=new ProgressDialog(this);
        dialog.setTitle("Fetching News Articles...");
        dialog.show();

        b1=(Button)findViewById(R.id.btn_1);
        b1.setOnClickListener(this);
        b2=(Button)findViewById(R.id.btn_2);
        b2.setOnClickListener(this);
        b3=(Button)findViewById(R.id.btn_3);
        b3.setOnClickListener(this);
        b4=(Button)findViewById(R.id.btn_4);
        b4.setOnClickListener(this);
        b5=(Button)findViewById(R.id.btn_5);
        b5.setOnClickListener(this);
        b6=(Button)findViewById(R.id.btn_6);
        b6.setOnClickListener(this);
        b7=(Button)findViewById(R.id.btn_7);
        b7.setOnClickListener(this);

        RequestManager manager=new RequestManager(this);
        manager.getNewsHeadlines(listener,"general",null);
    }

    private final OnFetchDataListener<NewsApiResponse> listener=new OnFetchDataListener<NewsApiResponse>() {
        @Override
        public void onFetchData(List<NewsHeadlines> list, String message) {
            if(list.isEmpty())
            {
                Toast.makeText(MainActivity.this,"No Data Found..!!!",Toast.LENGTH_SHORT).show();
            }
            else
            {
                showNews(list);
                dialog.dismiss();
            }

        }

        @Override
        public void onError(String message) {

            Toast.makeText(MainActivity.this,"An Error Occurred..!!!!",Toast.LENGTH_SHORT).show();
        }
    };

    private void showNews(List<NewsHeadlines> list) {
        recyclerView=findViewById(R.id.recycler_main);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,1));
        adapter=new CustomAdapter(this,list,this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void OnNewsClicked(NewsHeadlines headlines) {
        startActivity(new Intent(MainActivity.this,DetailsActivity.class)
               .putExtra("data",headlines));
        /*Intent intent = new Intent(MainActivity.this,DetailsActivity.class);
        intent.putExtra("data",headlines);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);*/

    }

    @Override
    public void onClick(View v) {
        Button button=(Button) v;
        String category=button.getText().toString();
        dialog.setTitle("Fetching news articles of "+category);
        dialog.show();
        RequestManager manager=new RequestManager(this);
        manager.getNewsHeadlines(listener,category,null);
    }
}