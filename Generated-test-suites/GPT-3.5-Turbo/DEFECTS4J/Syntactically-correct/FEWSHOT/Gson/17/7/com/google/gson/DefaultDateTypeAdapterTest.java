package com.google.gson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class DefaultDateTypeAdapterTest {
    
    @Test
    public void testWrite_NullDate_WritesNullValue() {
        JsonWriter writer = createJsonWriter();
        DefaultDateTypeAdapter adapter = createDefaultDateTypeAdapter(Date.class);
        Date value = null;
        
        adapter.write(writer, value);
        
        assertEquals("null", writer.toString());
    }
    
    @Test
    public void testWrite_ValidDate_WritesDateAsString() {
        JsonWriter writer = createJsonWriter();
        DefaultDateTypeAdapter adapter = createDefaultDateTypeAdapter(Date.class);
        Date value = new Date(1587472660000L);
        
        adapter.write(writer, value);
        
        assertEquals("\"21-Apr-2020 22:51:00\"", writer.toString());
    }
    
    @Test
    public void testRead_InvalidJson_ThrowsJsonParseException() {
        JsonReader reader = createJsonReader("12345");
        DefaultDateTypeAdapter adapter = createDefaultDateTypeAdapter(Date.class);
        
        try {
            adapter.read(reader);
        } catch (JsonParseException e) {
            assertEquals("The date should be a string value", e.getMessage());
        }
    }
    
    @Test
    public void testRead_ValidDate_ReturnsDateObject() {
        JsonReader reader = createJsonReader("\"21-Apr-2020 22:51:00\"");
        DefaultDateTypeAdapter adapter = createDefaultDateTypeAdapter(Date.class);
        
        Date date = adapter.read(reader);
        
        assertEquals(new Date(1587472660000L), date);
    }
    
    @Test
    public void testRead_NullDate_ReturnsNull() {
        JsonReader reader = createJsonReader("null");
        DefaultDateTypeAdapter adapter = createDefaultDateTypeAdapter(Date.class);
        
        Date date = adapter.read(reader);
        
        assertNull(date);
    }
    
    private JsonWriter createJsonWriter() {
        StringWriter stringWriter = new StringWriter();
        return new JsonWriter(stringWriter);
    }
    
    private JsonReader createJsonReader(String json) {
        StringReader stringReader = new StringReader(json);
        return new JsonReader(stringReader);
    }
    
    private DefaultDateTypeAdapter createDefaultDateTypeAdapter(Class<? extends Date> dateType) {
        return new DefaultDateTypeAdapter(dateType);
    }
    
    private DefaultDateTypeAdapter createDefaultDateTypeAdapter(Class<? extends Date> dateType, String datePattern) {
        return new DefaultDateTypeAdapter(dateType, datePattern);
    }
    
    private DefaultDateTypeAdapter createDefaultDateTypeAdapter(Class<? extends Date> dateType, int style) {
        return new DefaultDateTypeAdapter(dateType, style);
    }
    
    private DefaultDateTypeAdapter createDefaultDateTypeAdapter(Class<? extends Date> dateType, int dateStyle, int timeStyle) {
        return new DefaultDateTypeAdapter(dateType, dateStyle, timeStyle);
    }
}