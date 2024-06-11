package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

public class DateTimeSerializerBaseTest {

    @Test
    public void testWithFormat() {
        // Create a DateTimeSerializerBase object with custom format
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    
        // Test the withFormat method
        DateTimeSerializerBase<Date> updatedSerializer = serializer.withFormat(true, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    
        assertEquals(Boolean.TRUE, updatedSerializer._useTimestamp);
        assertEquals(DateFormat.class, updatedSerializer._customFormat.getClass());
    }

    @Test
    public void testCreateContextual() {
        // Create a DateTimeSerializerBase object
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    
        // Test the createContextual method
        JsonSerializer<?> returnedSerializer = serializer.createContextual(null, null);
    
        assertTrue(returnedSerializer instanceof DateTimeSerializerBase);
    }

    @Test
    public void testIsEmpty() {
        // Create a DateTimeSerializerBase object
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    
        // Test the isEmpty method
        boolean result = serializer.isEmpty(null, new Date());
    
        assertEquals(false, result);
    }
  
    @Test
    public void testGetSchema() {
        // Create a DateTimeSerializerBase object
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    
        // Test the getSchema method
        JsonNode schema = serializer.getSchema(null, null);
    
        assertNotNull(schema);
    }
  
    @Test
    public void testAcceptJsonFormatVisitor() {
        // Create a DateTimeSerializerBase object
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    
        // Test the acceptJsonFormatVisitor method
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper();
        serializer.acceptJsonFormatVisitor(visitor, null);
    
        assertTrue(visitor.hasVisited("number"));
        assertFalse(visitor.hasVisited("string"));
    }
    
    @Test
    public void testSerialize() throws IOException {
        // Create a DateTimeSerializerBase object
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    
        // Test the serialize method
        StringWriter writer = new StringWriter();
        JsonGenerator gen = new JsonFactory().createGenerator(writer);
        serializer.serialize(new Date(), gen, null);
    
        String json = writer.toString();
    
        assertNotNull(json);
    }

    @Test
    public void testAsTimestamp() {
        // Create a DateTimeSerializerBase object
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, true, null);
    
        // Test the _asTimestamp method
        boolean result = serializer._asTimestamp(null);
    
        assertEquals(true, result);
    }

    @Test
    public void testSerializeAsString() throws IOException {
        // Create a DateTimeSerializerBase object
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    
        // Test the _serializeAsString method
        StringWriter writer = new StringWriter();
        JsonGenerator gen = new JsonFactory().createGenerator(writer);
        serializer._serializeAsString(new Date(), gen, null);
    
        String json = writer.toString();
    
        assertNotNull(json);
    }
}