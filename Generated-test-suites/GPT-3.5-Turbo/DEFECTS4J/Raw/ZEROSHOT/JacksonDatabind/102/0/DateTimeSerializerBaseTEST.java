package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateTimeSerializerBaseTest {

    private DateTimeSerializerBase<TestClass> serializer;

    @Before
    public void setUp() {
        serializer = new DateTimeSerializerBase<TestClass>(TestClass.class, true, null) {
            @Override
            public DateTimeSerializerBase<TestClass> withFormat(Boolean timestamp, java.text.DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(TestClass value) {
                return 0;
            }

            @Override
            public void serialize(TestClass value, com.fasterxml.jackson.core.JsonGenerator gen, SerializerProvider serializers) {

            }
        };
    }

    @Test
    public void testCreateContextual() throws Exception {
        SerializerProvider serializers = new SerializerProvider();

        JsonSerializer<?> jsonSerializer = serializer.createContextual(serializers, null);
        Assert.assertEquals(serializer, jsonSerializer);
    }

    @Test
    public void testIsEmpty() throws Exception {
        TestClass testClass = new TestClass();
        SerializerProvider serializers = new SerializerProvider();

        boolean isEmpty = serializer.isEmpty(serializers, testClass);
        Assert.assertFalse(isEmpty);
    }

    @Test
    public void testGetSchema() throws Exception {
        SerializerProvider serializers = new SerializerProvider();
        JsonNode schema = serializer.getSchema(serializers, null);

        Assert.assertTrue(schema instanceof ObjectNode);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws Exception {
        SerializerProvider serializers = new SerializerProvider();
        serializer.acceptJsonFormatVisitor(null, null);
        serializer.acceptJsonFormatVisitor(serializers.getVisitorWrapper(), null);
    }

    @Test
    public void testSerialize() throws Exception {
        SerializerProvider serializers = new SerializerProvider();
        serializer.serialize(null, null, serializers);
    }

    private static class TestClass {
    }
}