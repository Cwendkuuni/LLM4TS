package com.fasterxml.jackson.databind.ser.std;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

@SuppressWarnings("serial")
public class DateTimeSerializerBaseTest {
    
    @Test
    public void testCreateContextual() throws JsonMappingException {
        DateTimeSerializerBase serializer = new DateTimeSerializerBaseTestImpl(Boolean.TRUE, null);
        SerializerProvider serializers = null;
        BeanProperty property = null;
        JsonSerializer<?> result = serializer.createContextual(serializers, property);
        assertTrue(result instanceof DateTimeSerializerBase);
    }

    @Test
    public void testIsEmpty1() {
        DateTimeSerializerBase serializer = new DateTimeSerializerBaseTestImpl(Boolean.TRUE, null);
        Object value = null;
        boolean result = serializer.isEmpty(value);
        assertTrue(result);
    }

    @Test
    public void testIsEmpty2() {
        DateTimeSerializerBase serializer = new DateTimeSerializerBaseTestImpl(Boolean.TRUE, null);
        SerializerProvider serializers = null;
        Object value = null;
        boolean result = serializer.isEmpty(serializers, value);
        assertTrue(result);
    }

    private class DateTimeSerializerBaseTestImpl extends DateTimeSerializerBase {

        public DateTimeSerializerBaseTestImpl(Class type, Boolean useTimestamp, DateFormat customFormat) {
            super(type, useTimestamp, customFormat);
        }

        public DateTimeSerializerBaseTestImpl(Boolean useTimestamp, DateFormat customFormat) {
            super(null, useTimestamp, customFormat);
        }

        @Override
        public DateTimeSerializerBase withFormat(Boolean timestamp, DateFormat customFormat) {
            return null;
        }

        @Override
        protected long _timestamp(Object value) {
            return 0;
        }

        @Override
        public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

        }
    }
}