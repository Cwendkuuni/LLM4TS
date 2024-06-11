package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.BeanProperty;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.SerializerProvider;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {
    @Test
    public void testWithFormat() {
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
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) {

            }
        };

        assertNull(serializer.withFormat(Boolean.TRUE, null));
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
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
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) {

            }
        };

        BeanProperty property = null;
        SerializerProvider serializers = null;

        assertEquals(serializer, serializer.createContextual(serializers, property));
    }

    @Test
    public void testIsEmpty() {
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
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) {

            }
        };

        SerializerProvider serializers = null;
        Object value = null;

        assertEquals(false, serializer.isEmpty(serializers, value));
    }

    @Test
    public void testGetSchema() {
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
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) {

            }
        };

        SerializerProvider serializers = null;
        Type typeHint = null;

        assertEquals(serialize, serializer.getSchema(serializers, typeHint));
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
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
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) {

            }
        };

        JsonFormatVisitorWrapper visitor = null;
        JavaType typeHint = null;

        assertEquals(serialize, serializer.acceptJsonFormatVisitor(visitor, typeHint));
    }

    @Test
    public void testSerializeAsString() throws IOException {
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

        SimpleDateFormat format = new SimpleDateFormat();
        Date value = new Date();
        JsonGenerator g = null;
        SerializerProvider provider = null;

        assertEquals(null, serializer.serializeAsString(value, g, provider));
    }
}