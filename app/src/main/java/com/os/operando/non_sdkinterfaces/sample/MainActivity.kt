package com.os.operando.non_sdkinterfaces.sample

import android.content.ContextWrapper
import android.databinding.DataBindingUtil
import android.gesture.Gesture
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.os.operando.non_sdkinterfaces.sample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder().detectNonSdkApiUsage().build())
        }

        binding.blacklist.setOnClickListener {
            // hiddenapi-blacklist.txt
            // https://android.googlesource.com/platform/prebuilts/runtime/+/master/appcompat/hiddenapi-blacklist.txt
            // Landroid/gesture/Gesture;->setID(J)V
            val g = Gesture()
            val method = g.javaClass.getDeclaredMethod("setID", Long::class.java)
            method.isAccessible = true
            method.invoke(g, 1L)
        }

        binding.darkGreylist.setOnClickListener {
            // https://android.googlesource.com/platform/prebuilts/runtime/+/master/appcompat/hiddenapi-dark-greylist.txt
            // Landroid/content/ContextWrapper;->getOpPackageName()Ljava/lang/String;
            val method = ContextWrapper(this).javaClass.getDeclaredMethod("getOpPackageName")
            method.isAccessible = true
            Log.d("test", method.invoke(applicationContext).toString())
        }

        binding.lightGreylist.setOnClickListener {
            // https://android.googlesource.com/platform/prebuilts/runtime/+/master/appcompat/hiddenapi-light-greylist.txt
            // Landroid/widget/Toast;->mDuration:I
            val t = Toast.makeText(this, "ダイナミックおじゃまします", Toast.LENGTH_LONG)
            val f = t.javaClass.getDeclaredField("mDuration")
            f.isAccessible = true
            Log.d("test", f.getInt(t).toString())
            t.show()
        }
    }
}
