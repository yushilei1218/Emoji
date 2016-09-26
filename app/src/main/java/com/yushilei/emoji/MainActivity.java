package com.yushilei.emoji;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements TextWatcher {

    private TextView tv;
    private EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.tv);
        et = (EditText) findViewById(R.id.et);
        et.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        changeStrWithEmoji(s.toString());
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    String TAG = "TAG";

    public void changeStrWithEmoji(String input) {
        //1.匹配字符串input中“[xxx]”
        Pattern pattern = Pattern.compile("\\[[^\\[\\]]*\\]");
        //2.获取匹配器
        Matcher matcher = pattern.matcher(input);
        //3.创建SP
        SpannableString sp = new SpannableString(input);
        //4.依次找到字符串input匹配到的子字符串
        while (matcher.find()) {
            //5.获取匹配到的子字符串[emoxxx]
            String group = matcher.group();
            //6.在该字符串开始的位置 与结束的位置
            int start = matcher.start();
            int end = matcher.end();
            Log.d(TAG, group);
            //7.获取 R.mipmap.class 实例
            Class<R.mipmap> mipmapClass = R.mipmap.class;
            try {
                //8.去掉[emoxxx] 中括号查找 定义的变量
                Field field = mipmapClass.getDeclaredField(group.substring(1, group.length() - 1));
                //9 R.mipmap.class 定义的变量都是 static的 可直接获取
                int rid = field.getInt(null);

                Log.d(TAG, rid + "");
                //10.获取该[emoxxx]对应的emo Drawable
                Drawable drawable = getResources().getDrawable(rid);
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                //11.在指定的位置设置该 ImageSpan即可
                sp.setSpan(new ImageSpan(drawable), start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //12.显示出来
        tv.setText(sp);
    }
}
