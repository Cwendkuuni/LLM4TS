package com.fasterxml.jackson.databind.ser.std;

import org.junit.Assert;
import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

public class DateTimeSerializerBaseTest {

    @Test
    public void testWithFormat() {
        DateTimeSerializerBase<TestClass> serializer = new DateTimeSerializerBase<TestClass>(TestClass.class, true, new SimpleDateFormat()) {
            @Override
            public DateTimeSerializerBase<TestClass> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(TestClass value) {
                return 0;
            }

            @Override
            public void serialize(TestClass value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }
        };
        Boolean useTimestamp = true;
        DateFormat customFormat = new SimpleDateFormat();
        DateTimeSerializerBase<TestClass> result = serializer.withFormat(useTimestamp, customFormat);
        Assert.assertNotNull(result);
        Assert.assertEquals(useTimestamp, result._useTimestamp);
        Assert.assertEquals(customFormat, result._customFormat);
    }

    @Test
    public void testCreateContextual() {
        DateTimeSerializerBase<TestClass> serializer = new DateTimeSerializerBase<TestClass>(TestClass.class, true, new SimpleDateFormat()) {
            @Override
            public DateTimeSerializerBase<TestClass> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(TestClass value) {
                return 0;
            }

            @Override
            public void serialize(TestClass value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }
        };
        SerializerProvider serializers = null;
        BeanProperty property = null;
        JsonSerializer<?> result = serializer.createContextual(serializers, property);
        Assert.assertNotNull(result);
        Assert.assertEquals(serializer, result);
    }

    @Test
    public void testIsEmpty() {
        DateTimeSerializerBase<TestClass> serializer = new DateTimeSerializerBase<TestClass>(TestClass.class, true, new SimpleDateFormat()) {
            @Override
            public DateTimeSerializerBase<TestClass> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(TestClass value) {
                return 0;
            }

            @Override
            public void serialize(TestClass value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }
        };
        TestClass value = null;
        boolean result = serializer.isEmpty(value);
        Assert.assertTrue(result);
        value = new TestClass();
        value.setTimestamp(0L);
        result = serializer.isEmpty(value);
        Assert.assertTrue(result);
        value.setTimestamp(1L);
        result = serializer.isEmpty(value);
        Assert.assertFalse(result);
    }

    @Test
    public void testGetSchema() {
        DateTimeSerializerBase<TestClass> serializer = new DateTimeSerializerBase<TestClass>(TestClass.class, true, new SimpleDateFormat()) {
            @Override
            public DateTimeSerializerBase<TestClass> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(TestClass value) {
                return 0;
            }

            @Override
            public void serialize(TestClass value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }
        };
        SerializerProvider serializers = null;
        Type typeHint = null;
        JsonNode result = serializer.getSchema(serializers, typeHint);
        Assert.assertNotNull(result);
        Assert.assertEquals("number", result.get("type").asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        DateTimeSerializerBase<TestClass> serializer = new DateTimeSerializerBase<TestClass>(TestClass.class, true, new SimpleDateFormat()) {
            @Override
            public DateTimeSerializerBase<TestClass> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(TestClass value) {
                return 0;
            }

            @Override
            public void serialize(TestClass value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }
        };
        JsonFormatVisitorWrapper visitor = null;
        JavaType typeHint = null;
        boolean asNumber = true;
        serializer.acceptJsonFormatVisitor(visitor, typeHint, asNumber);
        //todo: Add assertions based on the expected behavior of acceptJsonFormatVisitor
    }

    // Custom TestClass used for testing purposes
    class TestClass {
        private long timestamp;

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }
    }
}