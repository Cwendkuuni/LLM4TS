package com.google.gson.internal.test; // Assuming a test package has been created for this purpose

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import org.junit.Test;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DefaultDateTypeAdapterTest {

    @Test
    public void testDefaultDateTypeAdapter() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        assertEquals(adapter.getClass().getSimpleName(), "DefaultDateTypeAdapter(Date)");

        adapter = new DefaultDateTypeAdapter(Timestamp.class);
        assertEquals(adapter.getClass().getSimpleName(), "DefaultDateTypeAdapter(Timestamp)");

        adapter = new DefaultDateTypeAdapter(java.sql.Date.class);
        assertEquals(adapter.getClass().getSimpleName(), "DefaultDateTypeAdapter(java.sql.Date)");

        adapter = new DefaultDateTypeAdapter(Date.class, "dd-MM-yyyy HH:mm:ss");
        assertEquals(adapter.getClass().getSimpleName(), "DefaultDateTypeAdapter(Date, SimpleDateFormat)");

        adapter = new DefaultDateTypeAdapter(Date.class, DateFormat.LONG);
        assertEquals(adapter.getClass().getSimpleName(), "DefaultDateTypeAdapter(Date, DateFormat.LONG)");

        adapter = new DefaultDateTypeAdapter(Date.class, DateFormat.MEDIUM);
        assertEquals(adapter.getClass().getSimpleName(), "DefaultDateTypeAdapter(Date, DateFormat.MEDIUM)");

        adapter = new DefaultDateTypeAdapter(Date.class, DateFormat.SHORT);
        assertEquals(adapter.getClass().getSimpleName(), "DefaultDateTypeAdapter(Date, DateFormat.SHORT)");

        adapter = new DefaultDateTypeAdapter(Date.class, Locale.CHINA);
        assertEquals(adapter.getClass().getSimpleName(), "DefaultDateTypeAdapter(Date, Locale.CHINA)");
    }

    @Test
    public void testWrite() throws IOException {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        JsonWriter out = new JsonWriter(System.out);
        Date date = new Date();
        adapter.write(out, date);
        out.flush();
        assertEquals("Sun Jan 22 15:58:39 GMT+08:00 2023", System.out.getBuffer().toString().trim());
    }

    @Test
    public void testReadDate() throws IOException {
        JsonReader in = new JsonReader(new StringReader("\"2023-01-22T15:58:39Z\""));
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        Date date = adapter.read(in);
        assertEquals(new Date(1674375859000L), date);
    }

    @Test
    public void testReadTimestamp() throws IOException {
        JsonReader in = new JsonReader(new StringReader("\"2023-01-22T15:58:39.123Z\""));
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Timestamp.class);
        Timestamp timestamp = adapter.read(in);
        assertEquals(new Timestamp(1674375859123L), timestamp);
    }

    @Test
    public void testReadSqlDate() throws IOException {
        JsonReader in = new JsonReader(new StringReader("\"2023-01-22\""));
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(java.sql.Date.class);
        java.sql.Date date = adapter.read(in);
        assertEquals(new java.sql.Date(1674356800000L), date);
    }

    @Test(expected = JsonParseException.class)
    public void testReadInvalidDate() throws IOException {
        JsonReader in = new JsonReader(new StringReader("\"something\""));
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        adapter.read(in);
    }

    @Test(expected = IllegalStateException.class)
    public void testReadInvalidType() throws IOException {
        JsonReader in = new JsonReader(new StringReader("\"2023-01-22\""));
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Integer.class);
        adapter.read(in);
    }
}