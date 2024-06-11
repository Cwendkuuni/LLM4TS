package com.fasterxml.jackson.databind.ser.std;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicReference;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

import org.junit.Test;
import static org.junit.Assert.*;

@SuppressWarnings("serial")
public class DateTimeSerializerBaseTest {

    @Test
    public void testWithContextual() throws JsonMappingException {
        DateTimeSerializerBase serializer = new DateTimeSerializerBaseTestImpl();

        SerializerProvider serializers = null;
        BeanProperty property = null;

        assertNotNull(serializer.createContextual(serializers, property));
    }

    @Test
    public void testIsEmpty() {
        DateTimeSerializerBase serializer = new DateTimeSerializerBaseTestImpl();

        SerializerProvider serializers = null;
        T value = null;

        assertFalse(serializer.isEmpty(serializers, value));
    }

    @Test
    public void testGetSchema() {
        DateTimeSerializerBase serializer = new DateTimeSerializerBaseTestImpl();

        SerializerProvider serializers = null;
        Type typeHint = null;

        assertNotNull(serializer.getSchema(serializers, typeHint));
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        DateTimeSerializerBase serializer = new DateTimeSerializerBaseTestImpl();

        JsonFormatVisitorWrapper visitor = null;
        JavaType typeHint = null;

        serializer.acceptJsonFormatVisitor(visitor, typeHint);
    }

    @Test
    public void testSerialize() throws IOException {
        DateTimeSerializerBase serializer = new DateTimeSerializerBaseTestImpl();

        SerializerProvider serializers = null;
        JsonGenerator gen = null;
        T value = null;

        serializer.serialize(value, gen, serializers);
    }

    private class DateTimeSerializerBaseTestImpl extends DateTimeSerializerBase {

        public DateTimeSerializerBaseTestImpl() {
            super(T.class, true, null);
        }

        @Override
        public DateTimeSerializerBase withFormat(Boolean timestamp, DateFormat customFormat) {
            return null;
        }

        @Override
        protected long _timestamp(T value) {
            return 0;
        }

        @Override
        public void serialize(T value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

        }

    }
}