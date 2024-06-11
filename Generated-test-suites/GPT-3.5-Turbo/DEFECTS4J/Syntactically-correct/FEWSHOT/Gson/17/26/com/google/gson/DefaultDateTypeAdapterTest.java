package com.google.gson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

public class DefaultDateTypeAdapterTest {

    private DefaultDateTypeAdapter adapter;
    private DateFormat expectedDateFormat;
    
    @Before
    public void setUp() {
        adapter = new DefaultDateTypeAdapter(Date.class);
        expectedDateFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US);
    }
    
    @Test
    public void testWrite_NullValue() throws IOException {
        JsonWriter out = mock(JsonWriter.class);
        
        adapter.write(out, null);
        
        verify(out).nullValue();
    }
    
    @Test
    public void testWrite_NotNullValue() throws IOException, ParseException {
        JsonWriter out = mock(JsonWriter.class);
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2022-01-01");
        String expectedDateString = expectedDateFormat.format(date);
        
        adapter.write(out, date);
        
        verify(out).value(expectedDateString);
    }
    
    @Test(expected = JsonParseException.class)
    public void testRead_NonStringValue() throws IOException {
        JsonReader in = mock(JsonReader.class);
        when(in.peek()).thenReturn(JsonToken.NUMBER);
        
        adapter.read(in);
    }
    
    @Test
    public void testRead_DateType() throws IOException, ParseException {
        JsonReader in = mock(JsonReader.class);
        String dateString = "2022-01-01";
        Date expectedDate = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
        when(in.peek()).thenReturn(JsonToken.STRING);
        when(in.nextString()).thenReturn(dateString);
        
        Date actualDate = adapter.read(in);
        
        assertEquals(expectedDate, actualDate);
    }
    
    @Test
    public void testRead_TimestampType() throws IOException, ParseException {
        adapter = new DefaultDateTypeAdapter(Timestamp.class);
        JsonReader in = mock(JsonReader.class);
        String dateString = "2022-01-01";
        Date expectedDate = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
        when(in.peek()).thenReturn(JsonToken.STRING);
        when(in.nextString()).thenReturn(dateString);
        
        Date actualDate = adapter.read(in);
        
        assertEquals(new Timestamp(expectedDate.getTime()), actualDate);
    }
    
    @Test
    public void testRead_JavaSqlDateType() throws IOException, ParseException {
        adapter = new DefaultDateTypeAdapter(java.sql.Date.class);
        JsonReader in = mock(JsonReader.class);
        String dateString = "2022-01-01";
        Date expectedDate = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
        when(in.peek()).thenReturn(JsonToken.STRING);
        when(in.nextString()).thenReturn(dateString);
        
        Date actualDate = adapter.read(in);
        
        assertEquals(new java.sql.Date(expectedDate.getTime()), actualDate);
    }
    
    @Test
    public void testDeserializeToDate_LocalFormat() throws ParseException {
        String dateString = "2022-01-01";
        Date expectedDate = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
        
        Date actualDate = adapter.deserializeToDate(dateString);
        
        assertEquals(expectedDate, actualDate);
    }
    
    @Test
    public void testDeserializeToDate_EnUsFormat() throws ParseException {
        SimpleDateFormat enUsFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String dateString = "2022-01-01";
        Date expectedDate = enUsFormat.parse(dateString);
        
        Date actualDate = adapter.deserializeToDate(dateString);
        
        assertEquals(expectedDate, actualDate);
    }
    
    @Test(expected = JsonSyntaxException.class)
    public void testDeserializeToDate_InvalidFormat() {
        String dateString = "2022/01/01";
        
        adapter.deserializeToDate(dateString);
    }
    
    @Test
    public void testToString() {
        String expectedToString = "DefaultDateTypeAdapter(SimpleDateFormat)";
        
        String actualToString = adapter.toString();
        
        assertEquals(expectedToString, actualToString);
    }
}