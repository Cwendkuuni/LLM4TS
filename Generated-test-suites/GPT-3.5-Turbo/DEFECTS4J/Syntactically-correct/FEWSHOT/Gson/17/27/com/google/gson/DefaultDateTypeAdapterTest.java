package com.google.gson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.junit.Test;

public class DefaultDateTypeAdapterTest {

    @Test
    public void testWrite_NullDate_WritesNullValue() throws IOException {
        JsonWriter out = mock(JsonWriter.class);
        
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        adapter.write(out, null);
        
        verify(out).nullValue();
    }
    
    @Test
    public void testWrite_Date_WritesFormattedDate() throws IOException {
        JsonWriter out = mock(JsonWriter.class);
        
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Date date = format.parse("2021-01-01");
        
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        adapter.write(out, date);
        
        verify(out).value("2021-01-01");
    }
    
    @Test
    public void testWrite_Timestamp_WritesFormattedTimestamp() throws IOException {
        JsonWriter out = mock(JsonWriter.class);
        
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        Date timestamp = format.parse("2021-01-01 12:00:00");
        Timestamp timestampObj = new Timestamp(timestamp.getTime());
        
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Timestamp.class);
        adapter.write(out, timestampObj);
        
        verify(out).value("2021-01-01 12:00:00");
    }
    
    @Test
    public void testWrite_SQLDate_WritesFormattedSQLDate() throws IOException {
        JsonWriter out = mock(JsonWriter.class);
        
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Date sqlDate = format.parse("2021-01-01");
        java.sql.Date sqlDateObj = new java.sql.Date(sqlDate.getTime());
        
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(java.sql.Date.class);
        adapter.write(out, sqlDateObj);
        
        verify(out).value("2021-01-01");
    }
    
    @Test
    public void testRead_NonStringValue_ThrowsException() throws IOException {
        JsonReader in = mock(JsonReader.class);
        when(in.peek()).thenReturn(JsonToken.NUMBER);
        
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        
        try {
            adapter.read(in);
            fail("Expected JsonParseException to be thrown");
        } catch (JsonParseException e) {
            assertEquals("The date should be a string value", e.getMessage());
        }
    }
    
    @Test
    public void testRead_InvalidDateString_ThrowsException() throws IOException {
        JsonReader in = mock(JsonReader.class);
        when(in.peek()).thenReturn(JsonToken.STRING);
        when(in.nextString()).thenReturn("2021-01-01");
        
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        
        try {
            adapter.read(in);
            fail("Expected JsonSyntaxException to be thrown");
        } catch (JsonSyntaxException e) {
            assertEquals("2021-01-01", e.getJson());
            assertNotNull(e.getCause());
        }
    }
    
    @Test
    public void testRead_Date_ReturnsDateObject() throws IOException {
        JsonReader in = mock(JsonReader.class);
        when(in.peek()).thenReturn(JsonToken.STRING);
        when(in.nextString()).thenReturn("2021-01-01");
        
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        Date result = adapter.read(in);
        
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Date expectedDate = format.parse("2021-01-01");
        
        assertEquals(expectedDate, result);
    }
    
    @Test
    public void testRead_Timestamp_ReturnsTimestampObject() throws IOException {
        JsonReader in = mock(JsonReader.class);
        when(in.peek()).thenReturn(JsonToken.STRING);
        when(in.nextString()).thenReturn("2021-01-01 12:00:00");
        
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Timestamp.class);
        Date result = adapter.read(in);
        
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        Date expectedTimestamp = format.parse("2021-01-01 12:00:00");
        Timestamp expectedTimestampObj = new Timestamp(expectedTimestamp.getTime());
        
        assertEquals(expectedTimestampObj, result);
    }
    
    @Test
    public void testRead_SQLDate_ReturnsSQLDateObject() throws IOException {
        JsonReader in = mock(JsonReader.class);
        when(in.peek()).thenReturn(JsonToken.STRING);
        when(in.nextString()).thenReturn("2021-01-01");
        
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(java.sql.Date.class);
        Date result = adapter.read(in);
        
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Date expectedSQLDate = format.parse("2021-01-01");
        java.sql.Date expectedSQLDateObj = new java.sql.Date(expectedSQLDate.getTime());
        
        assertEquals(expectedSQLDateObj, result);
    }
}