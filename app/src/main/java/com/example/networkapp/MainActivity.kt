package com.example.networkapp

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import org.json.JSONException
import org.json.JSONObject

// TODO (1: Fix any bugs)
// TODO (2: Add function saveComic(...) to save and load comic info automatically when app starts)

class MainActivity : AppCompatActivity() {

    private lateinit var requestQueue: RequestQueue
    lateinit var titleTextView: TextView
    lateinit var descriptionTextView: TextView
    lateinit var numberEditText: EditText
    lateinit var showButton: Button
    lateinit var comicImageView: ImageView
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestQueue = Volley.newRequestQueue(this)

        titleTextView = findViewById<TextView>(R.id.comicTitleTextView)
        descriptionTextView = findViewById<TextView>(R.id.comicDescriptionTextView)
        numberEditText = findViewById<EditText>(R.id.comicNumberEditText)
        showButton = findViewById<Button>(R.id.showComicButton)
        comicImageView = findViewById<ImageView>(R.id.comicImageView)

        sharedPreferences = getPreferences(Context.MODE_PRIVATE)
        loadComic()

        showButton.setOnClickListener {
            downloadComic(numberEditText.text.toString())
        }

    }

    private fun downloadComic (comicId: String) {
        val url = "https://xkcd.com/$comicId/info.0.json"
        requestQueue.add (
            JsonObjectRequest(url, {showComic(it)}, {
            })
        )
    }

    private fun showComic (comicObject: JSONObject) {
        try {
            titleTextView.text = comicObject.getString("title")
            descriptionTextView.text = comicObject.getString("alt")
            Picasso.get().load(comicObject.getString("img")).into(comicImageView)
            saveComic(comicObject)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private fun saveComic(comicObject: JSONObject) {
        val editor = sharedPreferences.edit()
        editor.putString("title", comicObject.getString("title"))
        editor.putString("description", comicObject.getString("alt"))
        editor.putString("imageUrl", comicObject.getString("img"))
        editor.apply()
    }

    private fun loadComic() {
        val title = sharedPreferences.getString("title", "")
        val description = sharedPreferences.getString("description", "")
        val imageUrl = sharedPreferences.getString("imageUrl", "")

        titleTextView.text = title
        descriptionTextView.text = description
        Picasso.get().load(imageUrl).into(comicImageView)
    }


}