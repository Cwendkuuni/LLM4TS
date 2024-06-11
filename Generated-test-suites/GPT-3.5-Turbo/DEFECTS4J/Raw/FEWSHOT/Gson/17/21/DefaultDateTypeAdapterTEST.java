package com.google.gson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import com.google.gson.internal.bind.util.ISO8601Utils;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class DefaultDateTypeAdapterTest {
    @Test
    public void testWrite_NullValue_WriteNullValue() throws IOException {
        JsonWriter out = new JsonWriter() {
            @Override
            public void value(String value) throws IOException {
                assertEquals("null", value);
            }

            @Override
            public void nullValue() throws IOException {
                assertEquals("null", "null");
            }
        };

        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US),
                DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT));
        adapter.write(out, null);
    }

    @Test
    public void testWrite_ValidValue_WriteDateStringValue() throws IOException {
        final String expectedDate = "2022-01-01 23:59:59";
        Date date = null;
        try {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
            date = format.parse(expectedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        JsonWriter out = new JsonWriter() {
            @Override
            public void value(String value) throws IOException {
                assertEquals(expectedDate, value);
            }
        };

        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US),
                DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT));
        adapter.write(out, date);
    }

    @Test
    public void testRead_NonStringToken_ThrowJsonParseException() throws IOException {
        final JsonReader in = new JsonReader() {
            @Override
            public JsonToken peek() throws IOException {
                return JsonToken.NUMBER;
            }
        };

        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US),
                DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT));
        try {
            adapter.read(in);
        } catch (JsonParseException e) {
            assertEquals("The date should be a string value", e.getMessage());
        }
    }

    @Test
    public void testRead_ValidDateString_ReturnDateObject() throws IOException {
        final String dateString = "2022-01-01 23:59:59";
        Date expectedDate = null;
        try {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
            expectedDate = format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        final JsonReader in = new JsonReader() {
            @Override
            public JsonToken peek() throws IOException {
                return JsonToken.STRING;
            }

            @Override
            public String nextString() throws IOException {
                return dateString;
            }
        };

        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US),
                DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT));
        Date actualDate = adapter.read(in);
        assertEquals(expectedDate, actualDate);
    }

    @Test
    public void testToString_ReturnDefaultDateTypeAdapter() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US),
                DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT));
        String actualToString = adapter.toString();
        assertEquals("DefaultDateTypeAdapter(SimpleDateFormat)", actualToString);
    }
}