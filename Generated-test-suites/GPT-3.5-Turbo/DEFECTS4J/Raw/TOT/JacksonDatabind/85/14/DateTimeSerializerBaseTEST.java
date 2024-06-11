
package com.fasterxml.jackson.databind.ser.std;

import org.junit.Test;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.*;

public class DateTimeSerializerBaseTest {

    @Test
    public void testSerialize_withNullDate() throws Exception {
        DateTimeSerializerBase<Object> dateTimeSerializerBase = new DateTimeSerializerBase<Object>(Object.class, true, null) {
            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, SimpleDateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Object value) {
                return 0;
            }

            @Override
            public void serialize(Object value, com.fasterxml.jackson.core.JsonGenerator gen, com.fasterxml.jackson.databind.SerializerProvider serializers) throws java.io.IOException {

            }
        };

        com.fasterxml.jackson.databind.SerializerProvider serializers = null;
        boolean isEmpty = dateTimeSerializerBase.isEmpty(serializers, null);
        assertTrue(isEmpty);
    }

    @Test
    public void testSerialize_withNonEmptyDate() throws Exception {
        DateTimeSerializerBase<Object> dateTimeSerializerBase = new DateTimeSerializerBase<Object>(Object.class, true, null) {
            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, SimpleDateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Object value) {
                return 1234567890L;
            }

            @Override
            public void serialize(Object value, com.fasterxml.jackson.core.JsonGenerator gen, com.fasterxml.jackson.databind.SerializerProvider serializers) throws java.io.IOException {

            }
        };

        com.fasterxml.jackson.databind.SerializerProvider serializers = null;
        boolean isEmpty = dateTimeSerializerBase.isEmpty(serializers, new Date());
        assertFalse(isEmpty);
    }

    @Test
    public void testGetSchema_asTimestamp() throws Exception {
        DateTimeSerializerBase<Object> dateTimeSerializerBase = new DateTimeSerializerBase<Object>(Object.class, true, null) {
            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, SimpleDateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Object value) {
                return 1234567890L;
            }

            @Override
            public void serialize(Object value, com.fasterxml.jackson.core.JsonGenerator gen, com.fasterxml.jackson.databind.SerializerProvider serializers) throws java.io.IOException {

            }
        };

        com.fasterxml.jackson.databind.SerializerProvider serializers = null;
        com.fasterxml.jackson.databind.JsonNode schema = dateTimeSerializerBase.getSchema(serializers, null);
        assertEquals("number", schema.get("type").asText());
    }

    @Test
    public void testGetSchema_asString() throws Exception {
        DateTimeSerializerBase<Object> dateTimeSerializerBase = new DateTimeSerializerBase<Object>(Object.class, false, null) {
            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, SimpleDateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Object value) {
                return 0;
            }

            @Override
            public void serialize(Object value, com.fasterxml.jackson.core.JsonGenerator gen, com.fasterxml.jackson.databind.SerializerProvider serializers) throws java.io.IOException {

            }
        };

        com.fasterxml.jackson.databind.SerializerProvider serializers = null;
        com.fasterxml.jackson.databind.JsonNode schema = dateTimeSerializerBase.getSchema(serializers, null);
        assertEquals("string", schema.get("type").asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor_asTimestamp() throws Exception {
        DateTimeSerializerBase<Object> dateTimeSerializerBase = new DateTimeSerializerBase<Object>(Object.class, true, null) {
            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, SimpleDateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Object value) {
                return 1234567890L;
            }

            @Override
            public void serialize(Object value, com.fasterxml.jackson.core.JsonGenerator gen, com.fasterxml.jackson.databind.SerializerProvider serializers) throws java.io.IOException {

            }
        };

        com.fasterxml.jackson.databind.JsonFormatVisitorWrapper visitor = null;
        com.fasterxml.jackson.databind.JavaType typeHint = null;
        dateTimeSerializerBase.acceptJsonFormatVisitor(visitor, typeHint);

        // TODO: Add assertions for visitIntFormat
    }

    @Test
    public void testAcceptJsonFormatVisitor_asString() throws Exception {
        DateTimeSerializerBase<Object> dateTimeSerializerBase = new DateTimeSerializerBase<Object>(Object.class, false, null) {
            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, SimpleDateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Object value) {
                return 0;
            }

            @Override
            public void serialize(Object value, com.fasterxml.jackson.core.JsonGenerator gen, com.fasterxml.jackson.databind.SerializerProvider serializers) throws java.io.IOException {

            }
        };

        com.fasterxml.jackson.databind.JsonFormatVisitorWrapper visitor = null;
        com.fasterxml.jackson.databind.JavaType typeHint = null;
        dateTimeSerializerBase.acceptJsonFormatVisitor(visitor, typeHint);

        // TODO: Add assertions for visitStringFormat
    }
}

