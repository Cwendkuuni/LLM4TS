package com.google.gson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

import com.google.gson.internal.bind.util.ISO8601Utils;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class DefaultDateTypeAdapterTest {

    @Test
    public void testWriteNullDate() throws IOException {
        JsonWriter writer = new JsonWriter(new StringWriter());
        DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
        dateTypeAdapter.write(writer, null);
        writer.close();

        String expectedJson = "null";
        assertEquals(expectedJson, writer.toString());
    }

    @Test
    public void testWriteDate() throws IOException {
        JsonWriter writer = new JsonWriter(new StringWriter());
        DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(Date.class);

        Date date = new Date();
        String expectedJson = formatDate(date);

        dateTypeAdapter.write(writer, date);
        writer.close();

        assertEquals(expectedJson, writer.toString());
    }

    @Test
    public void testWriteTimestamp() throws IOException {
        JsonWriter writer = new JsonWriter(new StringWriter());
        DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(Timestamp.class);

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String expectedJson = formatDate(timestamp);

        dateTypeAdapter.write(writer, timestamp);
        writer.close();

        assertEquals(expectedJson, writer.toString());
    }

    @Test
    public void testWriteSqlDate() throws IOException {
        JsonWriter writer = new JsonWriter(new StringWriter());
        DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(java.sql.Date.class);

        java.sql.Date sqlDate = new java.sql.Date(System.currentTimeMillis());
        String expectedJson = formatDate(sqlDate);

        dateTypeAdapter.write(writer, sqlDate);
        writer.close();

        assertEquals(expectedJson, writer.toString());
    }

    @Test
    public void testReadNullValue() throws IOException {
        String json = "null";
        JsonReader reader = new JsonReader(new StringReader(json));
        DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
        Date date = dateTypeAdapter.read(reader);

        assertNull(date);
    }

    @Test
    public void testReadInvalidDate() throws IOException {
        String json = "\"not a date\"";
        JsonReader reader = new JsonReader(new StringReader(json));
        DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(Date.class);

        try {
            Date date = dateTypeAdapter.read(reader);
            fail("Expected JsonParseException to be thrown");
        } catch (JsonParseException e) {
            // expected exception
        }
    }

    @Test
    public void testReadValidDate() throws IOException {
        String json = "\"2021-09-01T12:00:00Z\"";
        JsonReader reader = new JsonReader(new StringReader(json));
        DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(Date.class);

        Date expectedDate = ISO8601Utils.parse("2021-09-01T12:00:00Z", new ParsePosition(0));
        Date date = dateTypeAdapter.read(reader);

        assertEquals(expectedDate, date);
    }

    @Test
    public void testReadValidTimestamp() throws IOException {
        String json = "\"2021-09-01T12:00:00Z\"";
        JsonReader reader = new JsonReader(new StringReader(json));
        DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(Timestamp.class);

        Timestamp expectedTimestamp = new Timestamp(ISO8601Utils.parse("2021-09-01T12:00:00Z", new ParsePosition(0)).getTime());
        Timestamp timestamp = dateTypeAdapter.read(reader);

        assertEquals(expectedTimestamp, timestamp);
    }

    @Test
    public void testReadValidSqlDate() throws IOException {
        String json = "\"2021-09-01\"";
        JsonReader reader = new JsonReader(new StringReader(json));
        DefaultDateTypeAdapter dateTypeAdapter = new DefaultDateTypeAdapter(java.sql.Date.class);

        java.sql.Date expectedSqlDate = new java.sql.Date(ISO8601Utils.parse("2021-09-01", new ParsePosition(0)).getTime());
        java.sql.Date sqlDate = dateTypeAdapter.read(reader);

        assertEquals(expectedSqlDate, sqlDate);
    }

    private String formatDate(Date date) {
        DateFormat format = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US);
        return format.format(date);
    }

    private String formatDate(Timestamp timestamp) {
        DateFormat format = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US);
        return format.format(new Date(timestamp.getTime()));
    }

    private String formatDate(java.sql.Date sqlDate) {
        DateFormat format = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US);
        return format.format(new Date(sqlDate.getTime()));
    }
}