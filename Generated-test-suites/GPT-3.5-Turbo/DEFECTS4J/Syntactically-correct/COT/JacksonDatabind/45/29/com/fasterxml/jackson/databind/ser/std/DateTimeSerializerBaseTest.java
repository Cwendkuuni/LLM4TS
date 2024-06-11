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
    public void testWithFormat() {
        // Arrange
        DateTimeSerializerBase<?> serializer = new DateTimeSerializerBaseStub(Boolean.FALSE, null);

        // Act
        DateTimeSerializerBase<?> result = serializer.withFormat(Boolean.TRUE, null);

        // Assert
        assertNotNull(result);
    }

    @Test
    public void testCreateContextual_WithPropertyAndNumericShape() {
        // Arrange
        DateTimeSerializerBase<?> serializer = new DateTimeSerializerBaseStub(Boolean.FALSE, null);
        JsonSerializer<?> expected = new DateTimeSerializerBaseStub(Boolean.TRUE, null);
        SerializerProvider serializers = new SerializerProvider(null, null, null);
        BeanProperty property = new BeanProperty(null, null);

        // Act
        JsonSerializer<?> result = serializer.createContextual(serializers, property);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void testCreateContextual_WithPropertyAndStringShape() {
        // Arrange
        DateTimeSerializerBaseStub serializer = new DateTimeSerializerBaseStub(Boolean.FALSE, null);
        serializer.createSimpleDateFormat();
        JsonSerializer<?> expected = new DateTimeSerializerBaseStub(Boolean.FALSE, serializer.getCustomFormat());
        SerializerProvider serializers = new SerializerProvider(null, null, null);
        BeanProperty property = new BeanProperty(null, null);

        // Act
        JsonSerializer<?> result = serializer.createContextual(serializers, property);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void testCreateContextual_WithNullProperty() throws JsonMappingException {
        // Arrange
        DateTimeSerializerBase<?> serializer = new DateTimeSerializerBaseStub(Boolean.FALSE, null);
        SerializerProvider serializers = new SerializerProvider(null, null, null);

        // Act
        JsonSerializer<?> result = serializer.createContextual(serializers, null);

        // Assert
        assertEquals(serializer, result);
    }

    @Test
    public void testIsEmpty_WithValue() {
        // Arrange
        DateTimeSerializerBaseStub serializer = new DateTimeSerializerBaseStub(Boolean.FALSE, null);
        Object value = new Object();

        // Act
        boolean result = serializer.isEmpty(new SerializerProvider(null, null, null), value);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsEmpty_WithNullValue() {
        // Arrange
        DateTimeSerializerBaseStub serializer = new DateTimeSerializerBaseStub(Boolean.FALSE, null);
        Object value = null;

        // Act
        boolean result = serializer.isEmpty(new SerializerProvider(null, null, null), value);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testGetSchema_WithTimestamp() {
        // Arrange
        DateTimeSerializerBaseStub serializer = new DateTimeSerializerBaseStub(Boolean.TRUE, null);
        SerializerProvider serializers = new SerializerProvider(null, null, null);
        Type typeHint = null;

        // Act
        JsonNode result = serializer.getSchema(serializers, typeHint);

        // Assert
        assertNotNull(result);
        assertEquals("number", result.asText());
    }

    @Test
    public void testGetSchema_WithoutTimestamp() {
        // Arrange
        DateTimeSerializerBaseStub serializer = new DateTimeSerializerBaseStub(Boolean.FALSE, null);
        SerializerProvider serializers = new SerializerProvider(null, null, null);
        Type typeHint = null;

        // Act
        JsonNode result = serializer.getSchema(serializers, typeHint);

        // Assert
        assertNotNull(result);
        assertEquals("string", result.asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor_AsNumber() throws JsonMappingException {
        // Arrange
        DateTimeSerializerBaseStub serializer = new DateTimeSerializerBaseStub(Boolean.TRUE, null);
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(null, null);
        JavaType typeHint = null;

        // Act
        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        // Assert
        assertTrue(serializer.visitIntFormatCalled);
    }

    @Test
    public void testAcceptJsonFormatVisitor_AsString() throws JsonMappingException {
        // Arrange
        DateTimeSerializerBaseStub serializer = new DateTimeSerializerBaseStub(Boolean.FALSE, null);
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(null, null);
        JavaType typeHint = null;

        // Act
        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        // Assert
        assertTrue(serializer.visitStringFormatCalled);
    }

    @Test
    public void testSerialize() throws IOException {
        // Arrange
        DateTimeSerializerBaseSerializer serializer = new DateTimeSerializerBaseSerializer();
        Object value = new Object();
        JsonGenerator gen = new JsonGenerator(null, null);
        SerializerProvider serializers = new SerializerProvider(null, null, null);

        // Act
        serializer.serialize(value, gen, serializers);

        // Assert
        assertTrue(serializer.serializeCalled);
    }

    private class DateTimeSerializerBaseStub extends DateTimeSerializerBase<Object> {
        public boolean visitIntFormatCalled;
        public boolean visitStringFormatCalled;

        public DateTimeSerializerBaseStub(Boolean useTimestamp, DateFormat customFormat) {
            super(Object.class, useTimestamp, customFormat);
        }

        @Override
        public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, DateFormat customFormat) {
            return null;
        }

        @Override
        protected long _timestamp(Object value) {
            return 0;
        }

        @Override
        protected void visitIntFormat(JsonFormatVisitorWrapper visitor, JavaType typeHint,
                                              JsonParser.NumberType numberType, JsonValueFormat format) {
            visitIntFormatCalled = true;
        }

        @Override
        protected void visitStringFormat(JsonFormatVisitorWrapper visitor, JavaType typeHint, JsonValueFormat format) {
            visitStringFormatCalled = true;
        }

        public void createSimpleDateFormat() {
            setCustomFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()));
        }

        public DateFormat getCustomFormat() {
            return _customFormat;
        }
    }

    private class DateTimeSerializerBaseSerializer extends DateTimeSerializerBase<Object> {
        public boolean serializeCalled;

        public DateTimeSerializerBaseSerializer() {
            super(Object.class, Boolean.FALSE, null);
        }

        @Override
        public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, DateFormat customFormat) {
            return null;
        }

        @Override
        protected long _timestamp(Object value) {
            return 0;
        }

        @Override
        public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers)
                throws IOException {
            serializeCalled = true;
        }
    }
}