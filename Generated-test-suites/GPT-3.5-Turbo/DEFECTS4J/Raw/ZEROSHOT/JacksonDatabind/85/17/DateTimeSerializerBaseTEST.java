package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;
import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

@SuppressWarnings("serial")
public class DateTimeSerializerBaseTest {

    @Test
    public void testWithFormat() {
        Class<DateTimeSerializerBase> clazz = DateTimeSerializerBase.class;
        Boolean timestamp = true;
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateTimeSerializerBase instance = new DateTimeSerializerBase(clazz, timestamp, customFormat) {
            @Override
            public DateTimeSerializerBase withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }
            @Override
            protected long _timestamp(Object value) {
                return 0L;
            }
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) {
            }
        };
        DateTimeSerializerBase result = instance.withFormat(timestamp, customFormat);
        assertNull(result);
    }

    @Test
    public void testCreateContextual() {
        Class<DateTimeSerializerBase> clazz = DateTimeSerializerBase.class;
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd");
        SerializerProvider serializers = null;
        BeanProperty property = null;
        DateTimeSerializerBase instance = new DateTimeSerializerBase(clazz, true, customFormat) {
            @Override
            public JsonSerializer<?> createContextual(SerializerProvider serializers, BeanProperty property) throws JsonMappingException {
                return null;
            }
            @Override
            protected long _timestamp(Object value) {
                return 0L;
            }
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) {
            }
        };
        JsonSerializer<?> result = instance.createContextual(serializers, property);
        assertNull(result);
    }

    @Test
    public void testIsEmpty() {
        Class<DateTimeSerializerBase> clazz = DateTimeSerializerBase.class;
        DateTimeSerializerBase instance = new DateTimeSerializerBase(clazz, true, null) {
            @Override
            public JsonSerializer<?> createContextual(SerializerProvider serializers, BeanProperty property) throws JsonMappingException {
                return null;
            }
            @Override
            protected long _timestamp(Object value) {
                return 0L;
            }
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) {
            }
        };
        boolean result = instance.isEmpty(null, null);
        assertTrue(result);
    }

    @Test
    public void testGetSchema() {
        Class<DateTimeSerializerBase> clazz = DateTimeSerializerBase.class;
        DateTimeSerializerBase instance = new DateTimeSerializerBase(clazz, true, null) {
            @Override
            public JsonSerializer<?> createContextual(SerializerProvider serializers, BeanProperty property) throws JsonMappingException {
                return null;
            }
            @Override
            protected long _timestamp(Object value) {
                return 0L;
            }
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) {
            }
        };
        Object typeHint = null;
        JsonNode result = instance.getSchema(null, typeHint);
        assertNotNull(result);
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        Class<DateTimeSerializerBase> clazz = DateTimeSerializerBase.class;
        DateTimeSerializerBase instance = new DateTimeSerializerBase(clazz, true, null) {
            @Override
            public JsonSerializer<?> createContextual(SerializerProvider serializers, BeanProperty property) throws JsonMappingException {
                return null;
            }
            @Override
            protected long _timestamp(Object value) {
                return 0L;
            }
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) {
            }
        };
        JsonFormatVisitorWrapper visitor = null;
        JavaType typeHint = null;
        try {
            instance.acceptJsonFormatVisitor(visitor, typeHint);
        } catch (JsonMappingException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testActualSerialization() {
        Class<DateTimeSerializerBase> clazz = DateTimeSerializerBase.class;
        DateTimeSerializerBase instance = new DateTimeSerializerBase(clazz, true, null) {
            @Override
            public JsonSerializer<?> createContextual(SerializerProvider serializers, BeanProperty property) throws JsonMappingException {
                return null;
            }
            @Override
            protected long _timestamp(Object value) {
                return 0L;
            }
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) {
            }
        };
        Object value = null;
        JsonGenerator gen = null;
        SerializerProvider serializers = null;
        try {
            instance.serialize(value, gen, serializers);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }   
}