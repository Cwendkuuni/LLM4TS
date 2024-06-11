package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

public class NumberSerializersTest {

    @Test
    public void testIntegerSerializer() throws IOException {
        NumberSerializers.IntegerSerializer serializer = new NumberSerializers.IntegerSerializer();

        // Test typical use case
        assertEquals("Serialize integer", "10", serializeValue(10, serializer));

        // Test edge case
        assertEquals("Serialize negative integer", "-5", serializeValue(-5, serializer));

        // Test error scenario
        assertNull("Serialize null value", serializeValue(null, serializer));
    }

    @Test
    public void testShortSerializer() throws IOException {
        NumberSerializers.ShortSerializer serializer = new NumberSerializers.ShortSerializer();

        // Test typical use case
        assertEquals("Serialize short", "5", serializeValue((short) 5, serializer));

        // Test edge case
        assertEquals("Serialize negative short", "-2", serializeValue((short) -2, serializer));

        // Test error scenario
        assertNull("Serialize null value", serializeValue(null, serializer));
    }

    @Test
    public void testIntLikeSerializer() throws IOException {
        NumberSerializers.IntLikeSerializer serializer = new NumberSerializers.IntLikeSerializer();

        // Test typical use case
        assertEquals("Serialize number", "15", serializeValue(15, serializer));

        // Test edge case
        assertEquals("Serialize negative number", "-8", serializeValue(-8, serializer));

        // Test error scenario
        assertNull("Serialize null value", serializeValue(null, serializer));
    }

    @Test
    public void testLongSerializer() throws IOException {
        NumberSerializers.LongSerializer serializer = new NumberSerializers.LongSerializer();

        // Test typical use case
        assertEquals("Serialize long", "10000", serializeValue(10000L, serializer));

        // Test edge case
        assertEquals("Serialize negative long", "-500", serializeValue(-500L, serializer));

        // Test error scenario
        assertNull("Serialize null value", serializeValue(null, serializer));
    }

    @Test
    public void testFloatSerializer() throws IOException {
        NumberSerializers.FloatSerializer serializer = new NumberSerializers.FloatSerializer();

        // Test typical use case
        assertEquals("Serialize float", "3.14", serializeValue(3.14f, serializer));

        // Test edge case
        assertEquals("Serialize negative float", "-2.5", serializeValue(-2.5f, serializer));

        // Test error scenario
        assertNull("Serialize null value", serializeValue(null, serializer));
    }

    @Test
    public void testDoubleSerializer() throws IOException {
        NumberSerializers.DoubleSerializer serializer = new NumberSerializers.DoubleSerializer();

        // Test typical use case
        assertEquals("Serialize double", "3.14159", serializeValue(3.14159, serializer));

        // Test edge case
        assertEquals("Serialize negative double", "-2.71828", serializeValue(-2.71828, serializer));

        // Test error scenario
        assertNull("Serialize null value", serializeValue(null, serializer));
    }

    private String serializeValue(Object value, JsonSerializer<Object> serializer) throws IOException {
        JsonGenerator generator = new TestGenerator();
        serializer.serialize(value, generator, null);
        generator.flush();
        return generator.getResult();
    }

    private static class TestGenerator extends JsonGenerator {
        private StringBuilder result = new StringBuilder();

        @Override
        public void writeNumber(int v) throws IOException {
            result.append(v);
        }

        @Override
        public void writeNumber(long v) throws IOException {
            result.append(v);
        }

        @Override
        public void writeNumber(double d) throws IOException {
            result.append(d);
        }

        @Override
        public void writeNumber(float f) throws IOException {
            result.append(f);
        }

        @Override
        public void flush() throws IOException {
            // Do nothing
        }

        @Override
        public void close() throws IOException {
            // Do nothing
        }

        public String getResult() {
            return result.toString();
        }
    }

}