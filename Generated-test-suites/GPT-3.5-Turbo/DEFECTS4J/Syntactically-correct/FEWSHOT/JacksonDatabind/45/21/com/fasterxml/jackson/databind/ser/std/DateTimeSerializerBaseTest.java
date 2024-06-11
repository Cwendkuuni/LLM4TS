package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonValueFormat;

public class DateTimeSerializerBaseTest {
    private final Class<JavaType> type = JavaType.class;
    private final Boolean useTimestamp = true;
    private final DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    @Test
    public void testConstructor() {
        DateTimeSerializerBase<JavaType> dateTimeSerializerBase = new DateTimeSerializerBase<JavaType>(type, useTimestamp, customFormat) {
            @Override
            public DateTimeSerializerBase<JavaType> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(JavaType value) {
                return 0;
            }

            @Override
            public void serialize(JavaType value, JsonGenerator gen, SerializerProvider serializers) {

            }
        };

        assertEquals(useTimestamp, dateTimeSerializerBase._useTimestamp);
        assertEquals(customFormat, dateTimeSerializerBase._customFormat);
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        DateTimeSerializerBase<JavaType> dateTimeSerializerBase = new DateTimeSerializerBase<JavaType>(type, null, null) {
            @Override
            public DateTimeSerializerBase<JavaType> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(JavaType value) {
                return 0;
            }

            @Override
            public void serialize(JavaType value, JsonGenerator gen, SerializerProvider serializers) {

            }
        };

        JsonSerializer<?> result = dateTimeSerializerBase.createContextual(null, null);
        assertEquals(dateTimeSerializerBase, result);
    }

    @Test
    public void testIsEmpty() {
        DateTimeSerializerBase<JavaType> dateTimeSerializerBase = new DateTimeSerializerBase<JavaType>(type, useTimestamp, customFormat) {
            @Override
            public DateTimeSerializerBase<JavaType> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(JavaType value) {
                return 0;
            }

            @Override
            public void serialize(JavaType value, JsonGenerator gen, SerializerProvider serializers) {

            }
        };

        assertTrue(dateTimeSerializerBase.isEmpty(null, null));
        assertTrue(dateTimeSerializerBase.isEmpty(null));
    }

    @Test
    public void testGetSchema() {
        DateTimeSerializerBase<JavaType> dateTimeSerializerBase = new DateTimeSerializerBase<JavaType>(type, useTimestamp, customFormat) {
            @Override
            public DateTimeSerializerBase<JavaType> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(JavaType value) {
                return 0;
            }

            @Override
            public void serialize(JavaType value, JsonGenerator gen, SerializerProvider serializers) {

            }
        };

        JsonNode result = dateTimeSerializerBase.getSchema(null, null);
        assertEquals("number", result.asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        DateTimeSerializerBase<JavaType> dateTimeSerializerBase = new DateTimeSerializerBase<JavaType>(type, useTimestamp, customFormat) {
            @Override
            public DateTimeSerializerBase<JavaType> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(JavaType value) {
                return 0;
            }

            @Override
            public void serialize(JavaType value, JsonGenerator gen, SerializerProvider serializers) {

            }
        };

        dateTimeSerializerBase.acceptJsonFormatVisitor(visitor, typeHint);
    }
}