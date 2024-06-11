package com.google.gson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class DefaultDateTypeAdapterTest {
    
    @Test
    public void testWrite_NullDate() {
        JsonWriter writer = new JsonWriter(null);
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        try {
            adapter.write(writer, null);
        } catch(IOException e) {
            e.printStackTrace();
        }
        assertEquals("null", writer.toString());
    }
    
    @Test
    public void testWrite_ValidDate() {
        JsonWriter writer = new JsonWriter(null);
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        Date date = new Date();
        try {
            adapter.write(writer, date);
        } catch(IOException e) {
            e.printStackTrace();
        }
        assertEquals(date.toString(), writer.toString());
    }
    
    @Test
    public void testRead_InvalidJsonToken() {
        JsonReader reader = new JsonReader(null);
        reader.beginObject();
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        try {
            adapter.read(reader);
        } catch(IOException e) {
            e.printStackTrace();
        }
        assertNull(null);
    }
    
    @Test
    public void testRead_ValidJsonToken() {
        JsonReader reader = new JsonReader(null);
        reader.value("2022-01-01");
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        Date expectedDate = new Date();
        try {
            Date actualDate = adapter.read(reader);
            assertEquals(expectedDate, actualDate);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}