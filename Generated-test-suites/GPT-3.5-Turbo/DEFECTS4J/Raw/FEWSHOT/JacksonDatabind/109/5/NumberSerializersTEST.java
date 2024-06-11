package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class NumberSerializersTest {

    @Test
    public void testShortSerializer() {
        Short value = (short) 123;
        ShortSerializer serializer = new ShortSerializer();
        JsonSerializer<Object> jsonSerializer = (JsonSerializer<Object>) serializer;
        SerializerProvider provider = null;
        JsonGenerator generator = null;
        try {
            jsonSerializer.serialize(value, generator, provider);
            assertTrue(true);
        } catch (IOException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testIntegerSerializer() {
        Integer value = 123;
        IntegerSerializer serializer = new IntegerSerializer(Integer.class);
        JsonSerializer<Object> jsonSerializer = (JsonSerializer<Object>) serializer;
        SerializerProvider provider = null;
        JsonGenerator generator = null;
        try {
            jsonSerializer.serialize(value, generator, provider);
            assertTrue(true);
        } catch (IOException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testIntLikeSerializer() {
        Integer value = 123;
        IntLikeSerializer serializer = IntLikeSerializer.instance;
        JsonSerializer<Object> jsonSerializer = (JsonSerializer<Object>) serializer;
        SerializerProvider provider = null;
        JsonGenerator generator = null;
        try {
            jsonSerializer.serialize(value, generator, provider);
            assertTrue(true);
        } catch (IOException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testLongSerializer() {
        Long value = (long) 1234567890;
        LongSerializer serializer = new LongSerializer(Long.class);
        JsonSerializer<Object> jsonSerializer = (JsonSerializer<Object>) serializer;
        SerializerProvider provider = null;
        JsonGenerator generator = null;
        try {
            jsonSerializer.serialize(value, generator, provider);
            assertTrue(true);
        } catch (IOException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testFloatSerializer() {
        Float value = 12.34f;
        FloatSerializer serializer = FloatSerializer.instance;
        JsonSerializer<Object> jsonSerializer = (JsonSerializer<Object>) serializer;
        SerializerProvider provider = null;
        JsonGenerator generator = null;
        try {
            jsonSerializer.serialize(value, generator, provider);
            assertTrue(true);
        } catch (IOException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testDoubleSerializer() {
        Double value = 12.34567890;
        DoubleSerializer serializer = new DoubleSerializer(Double.class);
        JsonSerializer<Object> jsonSerializer = (JsonSerializer<Object>) serializer;
        SerializerProvider provider = null;
        JsonGenerator generator = null;
        try {
            jsonSerializer.serialize(value, generator, provider);
            assertTrue(true);
        } catch (IOException e) {
            assertTrue(false);
        }
    }
}