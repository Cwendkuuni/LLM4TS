package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

@SuppressWarnings("serial")
public class DateTimeSerializerBaseTest {

    @Test
    public void testConstructor() {
        DateTimeSerializerBase<Object> serializer = new DateTimeSerializerBase<Object>(Object.class, true, null) {
            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }
            @Override
            protected long _timestamp(Object value) {
                return 0;
            }
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            }
        };
        
        assertEquals(Object.class, serializer.handledType());
        assertEquals(true, serializer._useTimestamp);
        assertNull(serializer._customFormat);
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        SerializerProvider provider = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);
        
        DateTimeSerializerBase<Object> serializer = new DateTimeSerializerBase<Object>(Object.class, null, null) {
            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }
            @Override
            protected long _timestamp(Object value) {
                return 0;
            }
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            }
        };
        
        Object result = serializer.createContextual(provider, property);
        
        assertEquals(serializer, result);
    }

    @Test
    public void testIsEmpty() {
        SerializerProvider provider = mock(SerializerProvider.class);
        
        DateTimeSerializerBase<Object> serializer = new DateTimeSerializerBase<Object>(Object.class, null, null) {
            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }
            @Override
            protected long _timestamp(Object value) {
                return 0;
            }
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            }
        };
        
        Object value = mock(Object.class);
        
        boolean result = serializer.isEmpty(provider, value);
        
        assertEquals(false, result);
    }

    @Test
    public void testGetSchema() {
        SerializerProvider provider = mock(SerializerProvider.class);
        JavaType typeHint = mock(JavaType.class);
        DateTimeSerializerBase<Object> serializer = new DateTimeSerializerBase<Object>(Object.class, null, null) {
            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }
            @Override
            protected long _timestamp(Object value) {
                return 0;
            }
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            }
        };
        
        JsonNode result = serializer.getSchema(provider, typeHint);
        
        assertEquals("string", result.get("type").asText());
        assertEquals(true, result.get("required").asBoolean());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        
        DateTimeSerializerBase<Object> serializer = new DateTimeSerializerBase<Object>(Object.class, null, null) {
            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }
            @Override
            protected long _timestamp(Object value) {
                return 0;
            }
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            }
        };
        
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        verify(visitor).getProvider();
    }
    
    @Test
    public void testSerialize() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider serializers = mock(SerializerProvider.class);
        
        DateTimeSerializerBase<Object> serializer = new DateTimeSerializerBase<Object>(Object.class, null, null) {
            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }
            @Override
            protected long _timestamp(Object value) {
                return 0;
            }
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            }
        };
        
        Object value = mock(Object.class);
        
        serializer.serialize(value, gen, serializers);
        
        verify(gen).writeStartObject();
        verify(gen).writeEndObject();
    }

}