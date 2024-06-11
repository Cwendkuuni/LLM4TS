
package com.fasterxml.jackson.databind.ser.std;

import org.junit.Test;
import static org.junit.Assert.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase.*;

public class DateTimeSerializerBaseTest {

    @Test
    public void testWithFormat() {
        DateTimeSerializerBase<DateTime> serializer = new DateTimeSerializerBase<DateTime>(
            DateTime.class, Boolean.TRUE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        DateTimeSerializerBase<DateTime> result = serializer.withFormat(Boolean.FALSE, new SimpleDateFormat("yyyyMMdd"));
        assertFalse(result._useTimestamp);
        assertEquals("yyyyMMdd", result._customFormat.getDateFormatSymbols().toString());
    }
    
    @Test
    public void testCreateContextual() {
        DateTimeSerializerBase<DateTime> serializer = new DateTimeSerializerBase<DateTime>(
            DateTime.class, Boolean.TRUE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        SerializerProvider serializers = new SerializerProvider();
        BeanProperty property = new BeanProperty("timestamp", null, null);
        try {
            DateTimeSerializerBase<DateTime> result = serializer.createContextual(serializers, property);
            assertTrue(result._useTimestamp);
        } catch (JsonMappingException e) {
            fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void testIsEmpty() {
        DateTimeSerializerBase<DateTime> serializer = new DateTimeSerializerBase<DateTime>(
            DateTime.class, Boolean.TRUE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        DateTime value = null;
        assertTrue(serializer.isEmpty(value));
        value = new DateTime(0);
        assertTrue(serializer.isEmpty(value));
        value = new DateTime(1234567890);
        assertFalse(serializer.isEmpty(value));
    }
    
    @Test
    public void testGetSchema() {
        DateTimeSerializerBase<DateTime> serializer = new DateTimeSerializerBase<DateTime>(
            DateTime.class, Boolean.TRUE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        SerializerProvider serializers = new SerializerProvider();
        Type typeHint = new Type();
        JsonNode result = serializer.getSchema(serializers, typeHint);
        assertEquals("number", result.get("format").toString());
    }
    
    @Test
    public void testAcceptJsonFormatVisitor() {
        DateTimeSerializerBase<DateTime> serializer = new DateTimeSerializerBase<DateTime>(
            DateTime.class, Boolean.TRUE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper();
        JavaType typeHint = new JavaType();
        try {
            serializer.acceptJsonFormatVisitor(visitor, typeHint);
            assertTrue(true);
        } catch (JsonMappingException e) {
            fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void testSerialize() {
        DateTimeSerializerBase<DateTime> serializer = new DateTimeSerializerBase<DateTime>(
            DateTime.class, Boolean.TRUE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        JsonGenerator gen = new JsonGenerator();
        SerializerProvider serializers = new SerializerProvider();
        DateTime value = new DateTime(1234567890);
        try {
            serializer.serialize(value, gen, serializers);
            assertTrue(true);
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
    }
}
