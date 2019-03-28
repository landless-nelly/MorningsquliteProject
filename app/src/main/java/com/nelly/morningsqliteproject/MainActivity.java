package com.nelly.morningsqliteproject;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
EditText mName, mEmail,mId;
Button mBtnSave,mBtnView,mBtnDelete;
SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mName=findViewById(R.id.edtName);
        mEmail=findViewById(R.id.edtMail);
        mId=findViewById(R.id.edtid);
        mBtnSave=findViewById(R.id.btnSave);
        mBtnDelete=findViewById(R.id.btnDelete);
        mBtnView=findViewById(R.id.btnView);
        db=openOrCreateDatabase("voterDb",MODE_PRIVATE,null);
        db.execSQL("CREATE TABLE IF NOT EXISTS voterreg(name Varchar,email VARCHAR, idNo VARCHAR)");
        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name =mName.getText().toString();
                String email =mEmail.getText().toString();
                String id =mId.getText().toString();
                if (name.isEmpty()) {
                    messageDisplay("NAME ERROR","kINDLY FILL IN YOUR NAME");
                } else if (email.isEmpty()) {
                    messageDisplay("EMAIL ERROR","KINDLY DISPLAY YOUR EMAIL");
                }else if (id.isEmpty()){
                    messageDisplay("ID ERROR","KINDLY FILL IN YOUR ID");
                }else {
                    //*/*/*/*/**/*/*/*/*/insert into db query
                    db.execSQL("INSERT INTO voterreg VALUES(' "+name+"','"+email+"','"+id+"')");
                    messageDisplay("QUERY SUCESS","DATA SAVED SUCESSFULLY");
                    clear();
                }
            }
        });
        mBtnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                QUERY TO VIEW DB RECORDS
                Cursor cursor =db.rawQuery("SELECT * FROM votterreg",null);
//                check if there any records
                if (cursor.getCount()==0){
                    messageDisplay("NO RECORDS","SORRY NO RECORDS WERE FOUND");
                }
//                USE BUFFER TO APPEND THE RECORDS
                StringBuffer buffer=new StringBuffer();
                while (cursor.moveToNext()){
                    buffer.append("\n"+cursor.getString(0));
                    buffer.append("\t "+cursor.getString(1));
                    buffer.append("\t "+cursor.getString(3));
                    buffer.append("\n");
                }
                messageDisplay("DATABASE",buffer.toString());
            }
        });
        mBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                CHECK IF SOMEONE HAS NOT ENTERED THE ID
                String id =mId.getText().toString().trim();
                if (id.isEmpty()) {
                    messageDisplay("ID ERROR","KINDLY FILL IN THE ID");
                }else {
                    Cursor cursor=db.rawQuery("SELECT *FROM voterreg WHERE idNo='"+id+"'",null);
                    if (cursor.moveToFirst()){
                       db.execSQL("DELETE FROM votterreg WHERE idNo ='"+id+"'");
                       messageDisplay("DELETED","Record deleted Sucessfully");
                       clear();
                    }
                }
            }
        });

    }
    private void  messageDisplay(String title,String message){
        AlertDialog.Builder builder =new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.create().show();
    }

      public void  clear(){
        mName.setText("");
        mEmail.setText("");
        mId.setText("");
      }
}
