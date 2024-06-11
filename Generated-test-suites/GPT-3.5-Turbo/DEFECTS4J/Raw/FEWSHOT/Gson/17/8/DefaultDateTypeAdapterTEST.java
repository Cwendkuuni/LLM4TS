package com.google.gson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DefaultDateTypeAdapterTest {
    private final String datePattern = "yyyy-MM-dd";

    @Test
    public void testWrite() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, datePattern);
        JsonWriter writer = null;
        Date date = new Date();
        // Test null value
        assertNull(adapter.write(writer, null));
        // Test valid value
        assertEquals(DateFormat.getDateInstance(DateFormat.DEFAULT).format(date), adapter.write(writer, date));
    }

    @Test
    public void testRead() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, datePattern);
        JsonReader reader = null;
        String dateAsString = "2021-01-01";
        Date date = null;
        // Test null value
        assertNull(adapter.read(reader));
        // Test valid value
        date = new SimpleDateFormat(datePattern, Locale.US).parse(dateAsString);
        assertEquals(date, adapter.read(reader));
    }

    @Test
    public void testToString() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class, datePattern);
        String expected = "DefaultDateTypeAdapter(SimpleDateFormat)";
        assertEquals(expected, adapter.toString());
    }
}