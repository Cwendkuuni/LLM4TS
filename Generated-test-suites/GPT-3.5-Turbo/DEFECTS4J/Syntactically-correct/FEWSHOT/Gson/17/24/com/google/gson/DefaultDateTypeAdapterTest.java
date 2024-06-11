package com.google.gson;

import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DefaultDateTypeAdapterTest {
    
    private DefaultDateTypeAdapter defaultDateTypeAdapter;
    
    @Before
    public void setUp() {
        defaultDateTypeAdapter = new DefaultDateTypeAdapter(Date.class);
    }
    
    @Test
    public void testWrite() throws Exception {
        // test null value
        JsonWriter out = new JsonWriter();
        defaultDateTypeAdapter.write(out, null);
        assertEquals("null", out.toString());
        
        // test non-null value
        Date date = new Date();
        out = new JsonWriter();
        defaultDateTypeAdapter.write(out, date);
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US);
        assertEquals(dateFormat.format(date), out.toString());
    }
    
    @Test
    public void testRead() throws Exception {
        JsonReader in = new JsonReader();
        
        // test valid date string
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US);
        Date expectedDate = dateFormat.parse("January 1, 2020");
        in.setNextValueAsString(dateFormat.format(expectedDate));
        Date actualDate = defaultDateTypeAdapter.read(in);
        assertEquals(expectedDate, actualDate);
        
        // test invalid date string
        in.setNextValueAsString("invalid");
        try {
            defaultDateTypeAdapter.read(in);
            assertNull("Expected JsonParseException to be thrown");
        } catch (JsonParseException e) {
            // expected
        }
    }
    
    @Test
    public void testDeserializeToDate() throws ParseException {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        
        // test valid date string
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US);
        Date expectedDate = dateFormat.parse("January 1, 2020");
        Date actualDate = adapter.deserializeToDate(dateFormat.format(expectedDate));
        assertEquals(expectedDate, actualDate);
        
        // test invalid date string
        try {
            adapter.deserializeToDate("invalid");
            assertNull("Expected JsonSyntaxException to be thrown");
        } catch (JsonSyntaxException e) {
            // expected
        }
    }
    
    @Test
    public void testToString() {
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        assertEquals("DefaultDateTypeAdapter(SimpleDateFormat)", adapter.toString());
    }
}