package com.google.gson.internal.test; // Assuming a test package has been created

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DefaultDateTypeAdapterTest {

    @Test
    public void testDefaultDateTypeAdapter() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter();
        assertEquals(DefaultDateTypeAdapter.class.getSimpleName() + "(US English DateFormat)", adapter.toString());
    }

    @Test
    public void testDefaultDateTypeAdapterWithDateFormat() {
        SimpleDateFormat enUsFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.US);
        DateFormat localFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.ENGLISH);
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, enUsFormat, localFormat);
        assertEquals(DefaultDateTypeAdapter.class.getSimpleName() + "(Date.class, US English DateFormat, English DateFormat)", adapter.toString());
    }

    @Test
    public void testDefaultDateTypeAdapterWithCustomDateFormat() {
        SimpleDateFormat enUsFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSSSSS", Locale.US);
        DateFormat localFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSSSSS", Locale.ENGLISH);
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, enUsFormat, localFormat);
        assertEquals(DefaultDateTypeAdapter.class.getSimpleName() + "(Date.class, Custom DateFormat, Custom DateFormat)", adapter.toString());
    }

    @Test
    public void testDefaultDateTypeAdapterWithCustomDateStyles() {
        DateFormat enUsFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US);
        DateFormat localFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT);
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, enUsFormat, localFormat);
        assertEquals(DefaultDateTypeAdapter.class.getSimpleName() + "(Date.class, US English DateFormat, English DateFormat)", adapter.toString());

        adapter = new DefaultDateTypeAdapter(Date.class, Date.DATE, Date.HOUR_OF_DAY | Date.MINUTE | Date.SECOND | Date.MILLISECOND);
        assertEquals(DefaultDateTypeAdapter.class.getSimpleName() + "(Date.class, Date Style, Date Style)", adapter.toString());

        adapter = new DefaultDateTypeAdapter(Date.class, Date.DATE, Date.DATE | Date.MONTH | Date.YEAR);
        assertEquals(DefaultDateTypeAdapter.class.getSimpleName() + "(Date.class, Date Style, Date Style)", adapter.toString());
    }

    @Test
    public void testDefaultDateTypeAdapterWrite() throws IOException {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter();
        Date date = new Date();
        String json = "";
        try (JsonWriter writer = new JsonWriter(new StringWriter())) {
            adapter.write(writer, date);
            json = writer.toString();
        }
        Date deserializedDate = adapter.read(new JsonReader(new StringReader(json))).getAsDate();
        assertEquals(date.getTime(), deserializedDate.getTime());
    }

    @Test
    public void testDefaultDateTypeAdapterReadDate() throws IOException {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter();
        Date date = new Date();
        String json = "{\"date\":\"" + adapter.toString() + "\":\"" + adapter.enUsFormat.format(date) + "\"}";
        Date deserializedDate = adapter.read(new JsonReader(new StringReader(json))).getAsDate();
        assertEquals(date.getTime(), deserializedDate.getTime());
    }

    @Test
    public void testDefaultDateTypeAdapterReadTimestamp() throws IOException {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Timestamp.class);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String json = "{\"timestamp\":\"" + adapter.toString() + "\":\"" + adapter.enUsFormat.format(new Date(timestamp.getTime())) + "\"}";
        Timestamp deserializedTimestamp = adapter.read(new JsonReader(new StringReader(json))).getAsJson().getAsJsonObject().get("timestamp").getAsJsonPrimitive().getAsLong();
        assertEquals(timestamp.getTime(), deserializedTimestamp);
    }

    @Test
    public void testDefaultDateTypeAdapterReadSqlDate() throws IOException {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(java.sql.Date.class);
        java.sql.Date sqlDate = new java.sql.Date(System.currentTimeMillis());
        String json = "{\"sqlDate\":\"" + adapter.toString() + "\":\"" + adapter.enUsFormat.format(new Date(sqlDate.getTime())) + "\"}";
        java.sql.Date deserializedSqlDate = adapter.read(new JsonReader(new StringReader(json))).getAsJson().getAsJsonObject().get("sqlDate").getAsJsonPrimitive().getAsLong();
        assertEquals(sqlDate.getTime(), deserializedSqlDate.getTime());
    }
}