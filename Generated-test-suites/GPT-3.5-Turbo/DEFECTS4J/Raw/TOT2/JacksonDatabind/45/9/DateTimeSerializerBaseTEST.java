package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {
    
    @Test
    public void testWithFormat() {
        DateTimeSerializerBase<?> serializer = new DateTimeSerializerBase<>(Object.class, true, new SimpleDateFormat());
        DateTimeSerializerBase<?> result = serializer.withFormat(true, new SimpleDateFormat());
        
        assertNotNull(result);
    }
    
    @Test
    public void testCreateContextual() throws JsonMappingException {
        DateTimeSerializerBase<?> serializer = new DateTimeSerializerBase<>(Object.class, true, new SimpleDateFormat());
        JsonSerializer<?> result = serializer.createContextual(null, new BeanProperty() {
            public Annotated getMember() {
                return null;
            }
        });
        
        assertNotNull(result);
    }
    
    @Test
    public void testIsEmpty() {
        DateTimeSerializerBase<?> serializer = new DateTimeSerializerBase<>(Object.class, true, new SimpleDateFormat());
        boolean result = serializer.isEmpty(null);
        
        assertTrue(result);
    }
    
    @Test
    public void testGetSchema() {
        DateTimeSerializerBase<?> serializer = new DateTimeSerializerBase<>(Object.class, true, new SimpleDateFormat());
        Object result = serializer.getSchema(null, Object.class);
        
        assertNotNull(result);
    }
    
    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        DateTimeSerializerBase<?> serializer = new DateTimeSerializerBase<>(Object.class, true, new SimpleDateFormat());
        serializer.acceptJsonFormatVisitor(new JsonFormatVisitorWrapper.Base(), null);
    }
    
    @Test
    public void testSerialize() {
        DateTimeSerializerBase<?> serializer = new DateTimeSerializerBase<>(Object.class, true, new SimpleDateFormat());
        serializer.serialize(null, null, null);
    }

}