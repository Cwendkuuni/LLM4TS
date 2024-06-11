package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;

@SuppressWarnings("serial")
public class DateTimeSerializerBaseTest {

    public class MockDateTimeSerializerBase extends DateTimeSerializerBase<Object> {
        public MockDateTimeSerializerBase(Class<Object> type, Boolean useTimestamp, DateFormat customFormat) {
            super(type, useTimestamp, customFormat);
        }

        @Override
        public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, DateFormat customFormat) {
            return null;
        }

        @Override
        public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

        }

        @Override
        public boolean isEmpty(SerializerProvider serializers, Object value) {
            return false;
        }

        @Override
        protected long _timestamp(Object value) {
            return 0;
        }

        @Override
        public JsonNode getSchema(SerializerProvider serializers, Type typeHint) {
            return null;
        }

        @Override
        public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {

        }
    }

    public void testCreateContextual() throws JsonMappingException {
        MockDateTimeSerializerBase serializer = new MockDateTimeSerializerBase(Object.class, false, null);
        SerializerProvider provider = new DefaultSerializerProvider.Impl();
        BeanProperty property = null;
        ContextualSerializer result = serializer.createContextual(provider, property);

        assertSame(serializer, result);
    }

    public void testIsEmpty() {
        MockDateTimeSerializerBase serializer = new MockDateTimeSerializerBase(Object.class, false, null);
        boolean result = serializer.isEmpty(null, null);
        assertFalse(result);
    }

    public void testTimestamp() {
        MockDateTimeSerializerBase serializer = new MockDateTimeSerializerBase(Object.class, true, null);
        boolean result = serializer._asTimestamp(null);
        assertTrue(result);
    }

    public void testNonTimestamp() {
        MockDateTimeSerializerBase serializer = new MockDateTimeSerializerBase(Object.class, false, null);
        boolean result = serializer._asTimestamp(null);
        assertFalse(result);
    }

    public void testSerialize() throws IOException {
        MockDateTimeSerializerBase serializer = new MockDateTimeSerializerBase(Object.class, false, null);
        serializer.serialize(null, null, null);
    }

    public void testGetSchema() {
        MockDateTimeSerializerBase serializer = new MockDateTimeSerializerBase(Object.class, false, null);
        JsonNode result = serializer.getSchema(null, null);
        assertNull(result);
    }

    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        MockDateTimeSerializerBase serializer = new MockDateTimeSerializerBase(Object.class, false, null);
        serializer.acceptJsonFormatVisitor(null, null);
    }
}