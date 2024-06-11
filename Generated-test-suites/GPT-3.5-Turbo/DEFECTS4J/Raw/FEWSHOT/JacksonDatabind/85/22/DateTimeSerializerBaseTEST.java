package com.fasterxml.jackson.databind.ser.std;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

@SuppressWarnings("serial")
public class DateTimeSerializerBaseTest {
    
    private DateTimeSerializerBase<Object> serializer;
    
    private final Boolean useTimestamp = Boolean.TRUE;
    private final DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
    
    @Before
    public void setup() {
        serializer = new DateTimeSerializerBase<Object>(Object.class, useTimestamp, customFormat) {
            
            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }
            
            @Override
            protected long _timestamp(Object value) {
                return 0;
            }
            
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
                // Do nothing
            }
        };
    }
    
    @Test
    public void testCreateContextualWhenPropertyIsNull() throws JsonMappingException {
        SerializerProvider serializers = mock(SerializerProvider.class);
        BeanProperty property = null;
        
        JsonSerializer<?> result = serializer.createContextual(serializers, property);
        
        assertEquals(serializer, result);
    }
    
    @Test
    public void testCreateContextualWhenFormatOverridesIsNull() throws JsonMappingException {
        SerializerProvider serializers = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);
        when(serializer.findFormatOverrides(serializers, property, Object.class)).thenReturn(null);
        
        JsonSerializer<?> result = serializer.createContextual(serializers, property);
        
        assertEquals(serializer, result);
    }
    
    @Test
    public void testCreateContextualWhenShapeIsNumeric() throws JsonMappingException {
        SerializerProvider serializers = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);
        JsonFormat.Value format = mock(JsonFormat.Value.class);
        when(serializer.findFormatOverrides(serializers, property, Object.class)).thenReturn(format);
        when(format.getShape()).thenReturn(JsonFormat.Shape.NUMBER);
        
        JsonSerializer<?> result = serializer.createContextual(serializers, property);
        
        assertNotNull(result);
        assertEquals(serializer, result);
    }
    
    @Test
    public void testCreateContextualWhenShapeIsStringAndHasPattern() throws JsonMappingException {
        SerializerProvider serializers = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);
        JsonFormat.Value format = mock(JsonFormat.Value.class);
        when(serializer.findFormatOverrides(serializers, property, Object.class)).thenReturn(format);
        when(format.getShape()).thenReturn(JsonFormat.Shape.STRING);
        when(format.hasPattern()).thenReturn(true);
        when(format.hasLocale()).thenReturn(false);
        when(format.hasTimeZone()).thenReturn(false);
        
        JsonSerializer<?> result = serializer.createContextual(serializers, property);
        
        assertNotNull(result);
        assertEquals(serializer, result);
    }
    
    @Test
    public void testCreateContextualWhenShapeIsStringAndHasLocale() throws JsonMappingException {
        SerializerProvider serializers = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);
        JsonFormat.Value format = mock(JsonFormat.Value.class);
        when(serializer.findFormatOverrides(serializers, property, Object.class)).thenReturn(format);
        when(format.getShape()).thenReturn(JsonFormat.Shape.STRING);
        when(format.hasPattern()).thenReturn(false);
        when(format.hasLocale()).thenReturn(true);
        when(format.hasTimeZone()).thenReturn(false);
        
        JsonSerializer<?> result = serializer.createContextual(serializers, property);
        
        assertNotNull(result);
        assertEquals(serializer, result);
    }
    
    @Test
    public void testCreateContextualWhenShapeIsStringAndHasTimeZone() throws JsonMappingException {
        SerializerProvider serializers = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);
        JsonFormat.Value format = mock(JsonFormat.Value.class);
        when(serializer.findFormatOverrides(serializers, property, Object.class)).thenReturn(format);
        when(format.getShape()).thenReturn(JsonFormat.Shape.STRING);
        when(format.hasPattern()).thenReturn(false);
        when(format.hasLocale()).thenReturn(false);
        when(format.hasTimeZone()).thenReturn(true);
        
        JsonSerializer<?> result = serializer.createContextual(serializers, property);
        
        assertNotNull(result);
        assertEquals(serializer, result);
    }
    
    @Test
    public void testIsEmptyWhenValueIsNullAndTimestampIsZero() {
        Object value = null;
        
        boolean result = serializer.isEmpty(value);
        
        assertEquals(true, result);
    }
    
    @Test
    public void testIsEmptyWhenValueIsNullAndTimestampIsNotZero() {
        serializer = new DateTimeSerializerBase<Object>(Object.class, useTimestamp, null) {
            
            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }
            
            @Override
            protected long _timestamp(Object value) {
                return 10;
            }
            
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
                // Do nothing
            }
        };
        
        Object value = null;
        
        boolean result = serializer.isEmpty(value);
        
        assertEquals(false, result);
    }
    
    @Test
    public void testIsEmptyWhenValueIsNotNullAndTimestampIsZero() {
        Object value = new Object();
        
        boolean result = serializer.isEmpty(value);
        
        assertEquals(false, result);
    }
    
    @Test
    public void testIsEmptyWhenValueIsNotNullAndTimestampIsNotZero() {
        serializer = new DateTimeSerializerBase<Object>(Object.class, useTimestamp, null) {
            
            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }
            
            @Override
            protected long _timestamp(Object value) {
                return 10;
            }
            
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
                // Do nothing
            }
        };
        
        Object value = new Object();
        
        boolean result = serializer.isEmpty(value);
        
        assertEquals(false, result);
    }
    
    @Test
    public void testGetSchemaWithStringFormat() {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        Type typeHint = Object.class;
        boolean asNumber = false;
        
        JsonNode result = serializer.getSchema(jsonGenerator, typeHint);
        
        assertEquals("string", result.get("type").textValue());
    }
    
    @Test
    public void testGetSchemaWithNumberFormat() {
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        Type typeHint = Object.class;
        boolean asNumber = true;
        
        JsonNode result = serializer.getSchema(jsonGenerator, typeHint);
        
        assertEquals("number", result.get("type").textValue());
    }
    
    @Test
    public void testAcceptJsonFormatVisitorWithStringFormat() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        boolean asNumber = false;
        
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        verify(visitor).prefixVisit(typeHint, JsonValueFormat.DATE_TIME);
        verify(visitor).expectStringFormat(typeHint);
    }
    
    @Test
    public void testAcceptJsonFormatVisitorWithNumberFormat() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        boolean asNumber = true;
        
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        verify(visitor).prefixVisit(typeHint, JsonValueFormat.UTC_MILLISEC);
        verify(visitor).expectNumberFormat(typeHint, JsonParser.NumberType.LONG);
    }
}