package com.fasterxml.jackson.databind.ser.std;

import org.junit.Test;
import org.junit.Assert;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateTimeSerializerBaseTest {

    @Test
    public void testWithFormat() {
        DateTimeSerializerBase serializer = new DateTimeSerializerBase(Date.class, true, null) {
            @Override
            public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Date value) {
                return 0;
            }

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }
        };

        Assert.assertNull(serializer.withFormat(null, null));
    }

    @Test
    public void testCreateContextual() {
        DateTimeSerializerBase serializer = new DateTimeSerializerBase(Date.class, true, null) {
            @Override
            public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Date value) {
                return 0;
            }

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }
        };

        BeanProperty property = null;
        SerializerProvider serializers = null;
        Assert.assertEquals(serializer, serializer.createContextual(serializers, property));
    }

    @Test
    public void testIsEmpty() {
        DateTimeSerializerBase serializer = new DateTimeSerializerBase(Date.class, true, null) {
            @Override
            public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Date value) {
                return 0;
            }

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }
        };

        Date value = null;
        Assert.assertTrue(serializer.isEmpty(value));
    }

    @Test
    public void testGetSchema() {
        DateTimeSerializerBase serializer = new DateTimeSerializerBase(Date.class, true, null) {
            @Override
            public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Date value) {
                return 0;
            }

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }
        };

        SerializerProvider serializers = null;
        Type typeHint = null;
        Assert.assertNotNull(serializer.getSchema(serializers, typeHint));
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        DateTimeSerializerBase serializer = new DateTimeSerializerBase(Date.class, true, null) {
            @Override
            public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Date value) {
                return 0;
            }

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }
        };

        JsonFormatVisitorWrapper visitor = null;
        JavaType typeHint = null;
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
    }
}