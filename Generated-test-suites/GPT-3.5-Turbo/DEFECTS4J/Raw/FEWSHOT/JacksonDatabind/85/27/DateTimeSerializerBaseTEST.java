package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {
    @Test
    public void testConstructor() {
        Boolean useTimestamp = false;
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, useTimestamp, customFormat) {
            private static final long serialVersionUID = 1L;

            @Override
            protected long _timestamp(String value) {
                return 0;
            }

            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) {
            }
        };

        assertEquals(useTimestamp, serializer._useTimestamp);
        assertEquals(customFormat, serializer._customFormat);
    }

    @Test
    public void testWithFormat() {
        Boolean useTimestamp = true;
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, useTimestamp, customFormat) {
            private static final long serialVersionUID = 1L;

            @Override
            protected long _timestamp(String value) {
                return 0;
            }

            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) {
            }
        };

        Boolean newUseTimestamp = false;
        DateFormat newCustomFormat = new SimpleDateFormat("MM-dd-yyyy");
        DateTimeSerializerBase<String> newSerializer = serializer.withFormat(newUseTimestamp, newCustomFormat);

        assertEquals(newUseTimestamp, newSerializer._useTimestamp);
        assertEquals(newCustomFormat, newSerializer._customFormat);
    }

    @Test
    public void testCreateContextual() throws Exception {
        Boolean useTimestamp = false;
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, useTimestamp, customFormat) {
            private static final long serialVersionUID = 1L;

            @Override
            protected long _timestamp(String value) {
                return 0;
            }

            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) {
            }
        };

        SerializerProvider provider = new SerializerProvider(null, null, new StdDateFormat());
        assertNull(serializer.createContextual(provider, null));

        JsonFormat.Value value = new JsonFormat.Value();
        value.setShape(JsonFormat.Shape.STRING);
        assertNull(serializer.createContextual(provider, null));

        value.setShape(JsonFormat.Shape.NUMBER);
        JsonSerializer<?> jsonSerializer = serializer.createContextual(provider, null);
        assertTrue(jsonSerializer instanceof DateTimeSerializerBase);
    }

    @Test
    public void testIsEmpty() {
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, false, null) {
            private static final long serialVersionUID = 1L;

            @Override
            protected long _timestamp(String value) {
                return 0;
            }

            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) {
            }
        };

        assertTrue(serializer.isEmpty(null));
        assertFalse(serializer.isEmpty(""));
    }

    @Test
    public void testGetSchema() {
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, false, null) {
            private static final long serialVersionUID = 1L;

            @Override
            protected long _timestamp(String value) {
                return 0;
            }

            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) {
            }
        };

        assertNotNull(serializer.getSchema(null, null));
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws Exception {
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, false, null) {
            private static final long serialVersionUID = 1L;

            @Override
            protected long _timestamp(String value) {
                return 0;
            }

            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) {
            }
        };
        JsonFormatVisitorWrapper visitor = null;
        serializer.acceptJsonFormatVisitor(visitor, null);
        assertNotNull(visitor);
    }
}