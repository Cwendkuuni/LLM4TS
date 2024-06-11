package com.google.gson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class DefaultDateTypeAdapterTest {
    @Test
    public void testWriteNullDate() {
        JsonWriter writer = new JsonWriter();
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        adapter.write(writer, null);
        assertNull(writer.getValue());
    }

    @Test
    public void testWriteDate() {
        JsonWriter writer = new JsonWriter();
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        Date date = new Date();
        adapter.write(writer, date);
        assertEquals(date.toString(), writer.getValue());
    }

    @Test
    public void testWriteNullTimestamp() {
        JsonWriter writer = new JsonWriter();
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Timestamp.class);
        adapter.write(writer, null);
        assertNull(writer.getValue());
    }

    @Test
    public void testWriteTimestamp() {
        JsonWriter writer = new JsonWriter();
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Timestamp.class);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        adapter.write(writer, timestamp);
        assertEquals(timestamp.toString(), writer.getValue());
    }

    @Test
    public void testWriteNullSqlDate() {
        JsonWriter writer = new JsonWriter();
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(java.sql.Date.class);
        adapter.write(writer, null);
        assertNull(writer.getValue());
    }

    @Test
    public void testWriteSqlDate() {
        JsonWriter writer = new JsonWriter();
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(java.sql.Date.class);
        java.sql.Date sqlDate = new java.sql.Date(System.currentTimeMillis());
        adapter.write(writer, sqlDate);
        assertEquals(sqlDate.toString(), writer.getValue());
    }

    @Test
    public void testReadInvalidJsonToken() throws IOException {
        JsonReader reader = new JsonReader();
        reader.setPeek(JsonToken.NUMBER);
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        try {
            adapter.read(reader);
            fail("Expected JsonParseException to be thrown");
        } catch (JsonParseException e) {
            // Expected
        }
    }

    @Test
    public void testReadDate() throws IOException {
        JsonReader reader = new JsonReader();
        String dateString = "2020-01-01";
        reader.setPeek(JsonToken.STRING);
        reader.setStringValue(dateString);
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        Date date = adapter.read(reader);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        assertEquals(dateFormat.parse(dateString), date);
    }

    @Test
    public void testReadTimestamp() throws IOException {
        JsonReader reader = new JsonReader();
        String dateString = "2020-01-01T00:00:00";
        reader.setPeek(JsonToken.STRING);
        reader.setStringValue(dateString);
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Timestamp.class);
        Timestamp timestamp = adapter.read(reader);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        assertEquals(dateFormat.parse(dateString), timestamp);
    }

    @Test
    public void testReadSqlDate() throws IOException {
        JsonReader reader = new JsonReader();
        String dateString = "2020-01-01";
        reader.setPeek(JsonToken.STRING);
        reader.setStringValue(dateString);
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(java.sql.Date.class);
        java.sql.Date sqlDate = adapter.read(reader);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        assertEquals(dateFormat.parse(dateString), sqlDate);
    }
}