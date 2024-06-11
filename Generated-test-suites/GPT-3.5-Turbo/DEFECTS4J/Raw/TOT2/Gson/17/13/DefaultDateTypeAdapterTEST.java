package com.google.gson;

import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultDateTypeAdapterTest {

    @Test
    public void testWrite_NullValue() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        // Testing null value
        try {
            adapter.write(null, null);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }

    @Test
    public void testRead_InvalidDateValue() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        JsonReader reader = new JsonReader(new StringReader("{ \"date\": 123456 }"));
        // Testing invalid date value
        try {
            adapter.read(reader);
            fail("Expected JsonParseException");
        } catch (IOException | JsonParseException e) {
            // success
        }
    }

    @Test
    public void testDeserializeToDate_NullString() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        // Testing null date string
        Date result = adapter.deserializeToDate(null);
        assertNull(result);
    }

    @Test
    public void testToString() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        // Testing toString method
        String result = adapter.toString();
        assertNotNull(result);
    }

}