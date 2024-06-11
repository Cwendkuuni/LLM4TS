package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {

    private class DateTimeSerializerBaseImpl extends DateTimeSerializerBase<Date> {

        public DateTimeSerializerBaseImpl(Class<Date> type, Boolean useTimestamp, DateFormat customFormat) {
            super(type, useTimestamp, customFormat);
        }

        @Override
        public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) {
            // TODO: Implement serialization of Date
        }

        @Override
        protected long _timestamp(Date value) {
            // TODO: Implement timestamp calculation of Date
            return 0;
        }

        @Override
        public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
            // TODO: Implement format customization of DateTimeSerializerBase
            return null;
        }
    }

    private static final Class<Date> TYPE = Date.class;
    private static final Boolean USE_TIMESTAMP = Boolean.FALSE;
    private static final DateFormat CUSTOM_FORMAT = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

    @Test
    public void testCreateContextual() throws JsonMappingException {
        DateTimeSerializerBase<Date> serializerBase = new DateTimeSerializerBaseImpl(TYPE, USE_TIMESTAMP, CUSTOM_FORMAT);
        SerializerProvider serializers = new SerializerProvider();
        BeanProperty property = new BeanProperty();

        assertNull(serializerBase.createContextual(serializers, null));

        JsonFormat.Value format = new JsonFormat.Value();
        assertNull(serializerBase.createContextual(serializers, property));

        format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.STRING);
        assertNull(serializerBase.createContextual(serializers, property));

        format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.NUMBER);
        assertNotNull(serializerBase.createContextual(serializers, property));
    }

    @Test
    public void testIsEmpty() {
        DateTimeSerializerBase<Date> serializerBase = new DateTimeSerializerBaseImpl(TYPE, USE_TIMESTAMP, CUSTOM_FORMAT);
        Date value = null;
        assertTrue(serializerBase.isEmpty(value));

        value = new Date();
        assertFalse(serializerBase.isEmpty(value));
    }

    @Test
    public void testGetSchema() {
        DateTimeSerializerBase<Date> serializerBase = new DateTimeSerializerBaseImpl(TYPE, USE_TIMESTAMP, CUSTOM_FORMAT);
        SerializerProvider serializers = new SerializerProvider();
        Type typeHint = Date.class;

        assertNotNull(serializerBase.getSchema(serializers, typeHint));
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        DateTimeSerializerBase<Date> serializerBase = new DateTimeSerializerBaseImpl(TYPE, USE_TIMESTAMP, CUSTOM_FORMAT);
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper();
        JavaType typeHint = new JavaType();

        serializerBase.acceptJsonFormatVisitor(visitor, typeHint);
    }

    @Test
    public void testSerialize() {
        DateTimeSerializerBase<Date> serializerBase = new DateTimeSerializerBaseImpl(TYPE, USE_TIMESTAMP, CUSTOM_FORMAT);
        JsonGenerator gen = new JsonGenerator();
        SerializerProvider serializers = new SerializerProvider();
        Date value = new Date();

        serializerBase.serialize(value, gen, serializers);
    }

    @Test
    public void testAsTimestamp() {
        DateTimeSerializerBase<Date> serializerBase = new DateTimeSerializerBaseImpl(TYPE, USE_TIMESTAMP, CUSTOM_FORMAT);
        SerializerProvider serializers = new SerializerProvider();

        assertFalse(serializerBase._asTimestamp(serializers));
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        DateTimeSerializerBase<Date> serializerBase = new DateTimeSerializerBaseImpl(TYPE, USE_TIMESTAMP, CUSTOM_FORMAT);
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper();
        JavaType typeHint = new JavaType();

        serializerBase._acceptJsonFormatVisitor(visitor, typeHint, true);
    }
}