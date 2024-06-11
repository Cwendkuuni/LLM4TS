package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class NumberSerializersTest {

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);

        assertNotNull(allDeserializers);
        assertEquals(12, allDeserializers.size());
        assertTrue(allDeserializers.containsKey(Integer.class.getName()));
        assertTrue(allDeserializers.containsKey(Integer.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Long.class.getName()));
        assertTrue(allDeserializers.containsKey(Long.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Byte.class.getName()));
        assertTrue(allDeserializers.containsKey(Byte.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Short.class.getName()));
        assertTrue(allDeserializers.containsKey(Short.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Float.class.getName()));
        assertTrue(allDeserializers.containsKey(Float.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Double.class.getName()));
        assertTrue(allDeserializers.containsKey(Double.TYPE.getName()));
    }

    @Test
    public void testShortSerialize() throws IOException {
        Short value = 100;
        NumberSerializers.ShortSerializer shortSerializer = new NumberSerializers.ShortSerializer();
        JsonSerializer<Short> serializer = shortSerializer;
        SerializerProvider provider = null;

        assertEquals("100", serializeValue(value, serializer, provider));
    }

    @Test
    public void testIntegerSerialize() throws IOException {
        Integer value = 100;
        NumberSerializers.IntegerSerializer intSerializer = new NumberSerializers.IntegerSerializer();
        JsonSerializer<Object> serializer = intSerializer;
        SerializerProvider provider = null;

        assertEquals("100", serializeValue(value, serializer, provider));
    }

    @Test
    public void testIntLikeSerialize() throws IOException {
        Number value = 100;
        NumberSerializers.IntLikeSerializer intLikeSerializer = new NumberSerializers.IntLikeSerializer();
        JsonSerializer<Number> serializer = intLikeSerializer;
        SerializerProvider provider = null;

        assertEquals("100", serializeValue(value, serializer, provider));
    }

    @Test
    public void testLongSerialize() throws IOException {
        Long value = 100L;
        NumberSerializers.LongSerializer longSerializer = new NumberSerializers.LongSerializer();
        JsonSerializer<Object> serializer = longSerializer;
        SerializerProvider provider = null;

        assertEquals("100", serializeValue(value, serializer, provider));
    }

    @Test
    public void testFloatSerialize() throws IOException {
        Float value = 10.5f;
        NumberSerializers.FloatSerializer floatSerializer = new NumberSerializers.FloatSerializer();
        JsonSerializer<Object> serializer = floatSerializer;
        SerializerProvider provider = null;

        assertEquals("10.5", serializeValue(value, serializer, provider));
    }

    @Test
    public void testDoubleSerialize() throws IOException {
        Double value = 10.5;
        NumberSerializers.DoubleSerializer doubleSerializer = new NumberSerializers.DoubleSerializer();
        JsonSerializer<Object> serializer = doubleSerializer;
        SerializerProvider provider = null;

        assertEquals("10.5", serializeValue(value, serializer, provider));
    }

    private String serializeValue(Object value, JsonSerializer<?> serializer, SerializerProvider provider) throws IOException {
        CustomJsonGenerator gen = new CustomJsonGenerator();
        gen.open();
        try {
            serializer.serialize(value, gen, provider);
        } finally {
            gen.close();
        }
        return gen.toString();
    }

}