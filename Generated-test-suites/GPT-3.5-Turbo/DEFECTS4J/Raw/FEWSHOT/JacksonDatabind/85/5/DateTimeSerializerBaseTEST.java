package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class DateTimeSerializerBaseTest {
    @Test
    public void testWithFormat() {
        DateTimeSerializerBase serializer = new DateTimeSerializerBase(DateTime.class, true, null) {
            @Override
            public DateTimeSerializerBase withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) {
                // do nothing
            }

            @Override
            protected long _timestamp(Object value) {
                return 0;
            }
        };

        DateTimeSerializerBase updatedSerializer = serializer.withFormat(false, new SimpleDateFormat("yyyy-MM-dd", Locale.US));
        assertEquals(false, updatedSerializer._useTimestamp);
        assertEquals(new SimpleDateFormat("yyyy-MM-dd", Locale.US), updatedSerializer._customFormat);
    }

    @Test
    public void testIsEmpty() {
        DateTimeSerializerBase serializer = new DateTimeSerializerBase(DateTime.class, false, null) {
            @Override
            public DateTimeSerializerBase withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) {
                // do nothing
            }

            @Override
            protected long _timestamp(Object value) {
                return 0;
            }
        };

        assertEquals(true, serializer.isEmpty(null));
        assertEquals(true, serializer.isEmpty(new DateTime(0L)));
        assertEquals(false, serializer.isEmpty(new DateTime()));
    }

    @Test
    public void testGetSchema() {
        DateTimeSerializerBase serializer = new DateTimeSerializerBase(DateTime.class, false, null) {
            @Override
            public DateTimeSerializerBase withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) {
                // do nothing
            }

            @Override
            protected long _timestamp(Object value) {
                return 0;
            }
        };

        assertEquals("string", serializer.getSchema(null, null).get("type").asText());
        assertEquals(true, serializer.getSchema(null, null).get("required").asBoolean());
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        DateTimeSerializerBase serializer = new DateTimeSerializerBase(DateTime.class, false, null) {
            @Override
            public DateTimeSerializerBase withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) {
                // do nothing
            }

            @Override
            protected long _timestamp(Object value) {
                return 0;
            }
        };

        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper.Base();
        serializer.acceptJsonFormatVisitor(visitor, null);
        // assert visitor's methods are called
    }
}