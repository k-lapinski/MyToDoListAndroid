package com.example.todolist;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Button btnSubbmit;
    EditText editText;
    ListView listView;
    ArrayList<String> itemList = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    public Integer counter = itemList.size();
    public static void setLocale(Activity activity, String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Resources resources = activity.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id == R.id.delete_all_all){
            itemList.clear();
            arrayAdapter.notifyDataSetChanged();
            Toast toastAdd = Toast.makeText(getApplicationContext(), getString(R.string.item_removed_info), Toast.LENGTH_SHORT);
            toastAdd.setGravity(Gravity.TOP, 0, 250);
            toastAdd.show();
        }
        if(id == R.id.change_lan) {
                setLocale(MainActivity.this, "pl");
                finish();
                startActivity(getIntent());
        }
        if(id == R.id.change_lan2) {
            setLocale(MainActivity.this, "en");
            finish();
            startActivity(getIntent());
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    editText = findViewById(R.id.editText);
    btnSubbmit = findViewById(R.id.button);
    listView = findViewById(R.id.ListView);

    itemList = FileHelper.readData(this);

    arrayAdapter = new ArrayAdapter<>(this,R.layout.activity_listview,itemList);
    listView.setAdapter(arrayAdapter);


        counter = itemList.size();
    btnSubbmit.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            Log.d(TAG, "onClick: " + now);
            String itemName = editText.getText().toString().toUpperCase();
            Log.i("MyApp",itemName);
            if(itemName.length() > 0) {
                itemList.add(itemName);
                editText.setText("");
                arrayAdapter.notifyDataSetChanged();
                Snackbar.make(view, getString(R.string.item_added) , Snackbar.LENGTH_SHORT).show();
                counter++;
            }
            else {
                Toast toastAdd = Toast.makeText(getApplicationContext(), getString(R.string.task_cant_empty), Toast.LENGTH_SHORT);
                toastAdd.setGravity(Gravity.TOP, 0, 250);
                toastAdd.show();
            }

        }
    });

    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            Log.d(TAG, "onItemClick: " + now);
            AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
            alert.setTitle(getString(R.string.del_btn));
            alert.setMessage(getString(R.string.really_delete));
            alert.setCancelable(false);
            alert.setNegativeButton(getString(R.string.no_btn), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                    LocalDateTime now = LocalDateTime.now();
                    Log.i(TAG, "onClick: " + now);
                    dialogInterface.cancel();

                }
            });
            alert.setPositiveButton(getString(R.string.yes_btn), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                    LocalDateTime now = LocalDateTime.now();
                    Log.i(TAG, "onClick: " + now);
                    String itemTaked = itemList.get(position);
                    itemList.add(getString(R.string.done_24) + " " + itemTaked);
                    itemList.remove(position);
                    arrayAdapter.notifyDataSetChanged();
                    Toast toastAdd = Toast.makeText(getApplicationContext(), getString(R.string.item_removed_info), Toast.LENGTH_SHORT);
                    toastAdd.setGravity(Gravity.TOP, 0, 250);
                    toastAdd.show();
                    counter--;
                }
            });
            AlertDialog alertDialog = alert.create();
            alertDialog.show();
        }
    });


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long arg3) {
                TextView textView = (TextView) view;
                String titleYt = "Łasuch strajkuje • Epizod • Smerfy";
                if (textView.getText().toString().equalsIgnoreCase(titleYt.toUpperCase())) {
                    displayVideo();
                }
                return false;
            }
        });

    }

    @Override
    protected void onStop() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        Log.i(TAG, "onStop: " + now);
        super.onStop();
        FileHelper.writeData(itemList,getApplicationContext());
    }

    private void displayVideo() {
        String url = "https://www.youtube.com/watch?v=CFaPAK89g8k";
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(url));

        startActivity(webIntent);
    }


}