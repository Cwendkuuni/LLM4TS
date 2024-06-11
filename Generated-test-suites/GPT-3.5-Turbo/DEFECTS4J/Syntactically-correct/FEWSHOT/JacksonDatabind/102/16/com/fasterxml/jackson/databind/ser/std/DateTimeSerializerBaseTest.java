package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.JsonSerializer.None;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {

    @Test
    public void testConstructor() {
        Boolean useTimestamp = true;
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        new DateTimeSerializerBaseImpl(useTimestamp, customFormat);
    }

    @Test
    public void testWithFormat() {
        DateTimeSerializerBaseImpl serializer = new DateTimeSerializerBaseImpl(null, null);
        Boolean useTimestamp = true;
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        DateTimeSerializerBaseImpl result = serializer.withFormat(useTimestamp, customFormat);
        assertNull(result._customFormat);
        assertEquals(useTimestamp, result._useTimestamp);
    }

    @Test
    public void testCreateContextual() throws Exception {
        DateTimeSerializerBaseImpl serializer = new DateTimeSerializerBaseImpl(null, null);
        SerializerProvider serializers = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);
        None result = serializer.createContextual(serializers, property);
        assertEquals(serializer, result);
    }

    @Test
    public void testIsEmpty() {
        DateTimeSerializerBaseImpl serializer = new DateTimeSerializerBaseImpl(null, null);
        SerializerProvider serializers = mock(SerializerProvider.class);
        Date value = new Date();
        boolean result = serializer.isEmpty(serializers, value);
        assertEquals(false, result);
    }

    @Test
    public void testTimestamp() {
        DateTimeSerializerBaseImpl serializer = new DateTimeSerializerBaseImpl(null, null);
        Date value = new Date();
        long result = serializer._timestamp(value);
    }

    @Test
    public void testGetSchema() {
        DateTimeSerializerBaseImpl serializer = new DateTimeSerializerBaseImpl(null, null);
        SerializerProvider serializers = mock(SerializerProvider.class);
        Type typeHint = mock(Type.class);
        JsonNode result = serializer.getSchema(serializers, typeHint);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws Exception {
        DateTimeSerializerBaseImpl serializer = new DateTimeSerializerBaseImpl(null, null);
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
    }

    @Test
    public void testSerialize() throws Exception {
        DateTimeSerializerBaseImpl serializer = new DateTimeSerializerBaseImpl(null, null);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider serializers = mock(SerializerProvider.class);
        Date value = new Date();
        serializer.serialize(value, gen, serializers);
    }

    private class DateTimeSerializerBaseImpl extends DateTimeSerializerBase<Date> {

        protected DateTimeSerializerBaseImpl(Boolean useTimestamp, DateFormat customFormat) {
            super(Date.class, useTimestamp, customFormat);
        }

        @Override
        public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
            return null;
        }

        @Override
        protected long _timestamp(Date value) {
            return 0;
        }

        @Override
        public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) {
        }

        @Override
        public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
                throws com.fasterxml.jackson.databind.JsonMappingException {
        }

    }

}