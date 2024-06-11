package com.fasterxml.jackson.databind.ser.std;

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
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

import org.junit.*;
import static org.junit.Assert.*;

@SuppressWarnings("serial")
public class DateTimeSerializerBaseTest {

    @Test
    public void testWithFormat() {
        DateTimeSerializerBase<?> serializer = new DateTimeSerializerBase<Object>(
            Object.class, true, null) {
            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
            }
        };
        serializer.withFormat(true, new SimpleDateFormat("yyyy-MM-dd"));
    }

    @Test
    public void testContextualSerializer() throws JsonMappingException {
        DateTimeSerializerBase<?> serializer = new DateTimeSerializerBase<Object>(
            Object.class, true, null) {
            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
            }
        };
        JsonSerializer<?> result = serializer.createContextual(null, null);
        assertEquals(serializer, result);
    }

    @Test
    public void testIsEmpty() {
        DateTimeSerializerBase<?> serializer = new DateTimeSerializerBase<Object>(
            Object.class, true, null) {
            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
            }
        };
        assertTrue(serializer.isEmpty(null, null));
    }

    @Test
    public void testGetSchema() {
        DateTimeSerializerBase<?> serializer = new DateTimeSerializerBase<Object>(
            Object.class, true, null) {
            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
            }
        };
        JsonNode result = serializer.getSchema(null, null);
        assertNotNull(result);
    }

    @Test
    public void testSerialize() throws IOException {
        DateTimeSerializerBase<?> serializer = new DateTimeSerializerBase<Object>(
            Object.class, true, null) {
            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
            }
        };
        serializer.serialize(null, null, null);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        DateTimeSerializerBase<?> serializer = new DateTimeSerializerBase<Object>(
            Object.class, true, null) {
            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
            }
        };
        serializer.acceptJsonFormatVisitor(null, null);
    }
}