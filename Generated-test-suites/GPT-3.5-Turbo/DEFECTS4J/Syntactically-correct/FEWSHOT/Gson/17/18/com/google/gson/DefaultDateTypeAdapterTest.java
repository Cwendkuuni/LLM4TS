package com.google.gson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class DefaultDateTypeAdapterTest {
    
    @Test
    public void testWrite_NullDate() {
        // Arrange
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        JsonWriter writer = new JsonWriter(null);
        
        // Act
        adapter.write(writer, null);
        
        // Assert
        assertEquals(JsonToken.NULL, writer.peek());
    }
    
    @Test
    public void testRead_InvalidJsonToken() {
        // Arrange
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        JsonReader reader = new JsonReader(null);
        reader.addJsonToken(JsonToken.NUMBER);

        // Act and Assert
        try {
            adapter.read(reader);
            fail("Expected JsonParseException to be thrown.");
        } catch (JsonParseException ex) {
            // Pass
        }
    }
    
    @Test
    public void testRead_DeserializeToDate() {
        // Arrange
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        JsonReader reader = new JsonReader(null);
        reader.addJsonToken(JsonToken.STRING);
        reader.setString("2021-01-01");

        // Act
        Date result = adapter.read(reader);

        // Assert
        assertEquals(new SimpleDateFormat("yyyy-MM-dd").parse("2021-01-01"), result);
    }
    
    @Test
    public void testRead_DateTypeIsDate() {
        // Arrange
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        JsonReader reader = new JsonReader(null);
        reader.addJsonToken(JsonToken.STRING);
        reader.setString("2021-01-01");

        // Act
        Date result = adapter.read(reader);

        // Assert
        assertEquals(Date.class, result.getClass());
    }
    
    @Test
    public void testRead_DateTypeIsTimestamp() {
        // Arrange
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Timestamp.class);
        JsonReader reader = new JsonReader(null);
        reader.addJsonToken(JsonToken.STRING);
        reader.setString("2021-01-01");

        // Act
        Date result = adapter.read(reader);

        // Assert
        assertEquals(Timestamp.class, result.getClass());
        assertEquals(new SimpleDateFormat("yyyy-MM-dd").parse("2021-01-01").getTime(), ((Timestamp)result).getTime());
    }
    
    @Test
    public void testRead_DateTypeIsSqlDate() {
        // Arrange
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(java.sql.Date.class);
        JsonReader reader = new JsonReader(null);
        reader.addJsonToken(JsonToken.STRING);
        reader.setString("2021-01-01");

        // Act
        Date result = adapter.read(reader);

        // Assert
        assertEquals(java.sql.Date.class, result.getClass());
        assertEquals(new SimpleDateFormat("yyyy-MM-dd").parse("2021-01-01").getTime(), ((java.sql.Date)result).getTime());
    }
    
    @Test
    public void testToString() {
        // Arrange
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);

        // Act
        String result = adapter.toString();

        // Assert
        assertEquals("DefaultDateTypeAdapter(SimpleDateFormat)", result);
    }

}