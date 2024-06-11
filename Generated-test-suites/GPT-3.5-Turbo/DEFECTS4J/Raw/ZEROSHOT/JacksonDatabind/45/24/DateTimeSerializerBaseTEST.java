package com.fasterxml.jackson.databind.ser.std;

import java.io.IOException;
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

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DateTimeSerializerBaseTest {

    private DateTimeSerializerBase<TestObject> serializer;

    @Before
    public void setUp() {
        serializer = new DateTimeSerializerBase<TestObject>(TestObject.class, true, null) {
            @Override
            public DateTimeSerializerBase<TestObject> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(TestObject value) {
                return 0L;
            }

            @Override
            public void serialize(TestObject value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }
        };
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        BeanProperty property = null;
        SerializerProvider serializers = null;
        JsonSerializer result = serializer.createContextual(serializers, property);
        assertNotNull(result);
    }

    @Test
    public void testIsEmpty() {
        TestObject value = null;
        boolean result = serializer.isEmpty(value);
        assertTrue(result);
    }

    @Test
    public void testIsEmpty2() {
        SerializerProvider serializers = null;
        TestObject value = null;
        boolean result = serializer.isEmpty(serializers, value);
        assertTrue(result);
    }

    @Test
    public void testGetSchema() {
        SerializerProvider serializers = null;
        Type typeHint = null;
        JsonNode result = serializer.getSchema(serializers, typeHint);
        assertNotNull(result);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = null;
        JavaType typeHint = null;
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
    }
    
    @Test
    public void testSerialize() throws IOException {
        TestObject value = null;
        JsonGenerator gen = null;
        SerializerProvider serializers = null;
        serializer.serialize(value, gen, serializers);
    }
}