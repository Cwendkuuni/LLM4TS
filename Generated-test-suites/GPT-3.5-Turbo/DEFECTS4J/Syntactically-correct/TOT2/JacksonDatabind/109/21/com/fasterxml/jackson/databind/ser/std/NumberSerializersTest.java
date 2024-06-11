package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ser.SerializerProvider;

public class NumberSerializersTest {

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);

        assertNotNull(allDeserializers.get(Integer.class.getName()));
        assertNotNull(allDeserializers.get(Integer.TYPE.getName()));
        assertNotNull(allDeserializers.get(Long.class.getName()));
        assertNotNull(allDeserializers.get(Long.TYPE.getName()));
        assertNotNull(allDeserializers.get(Byte.class.getName()));
        assertNotNull(allDeserializers.get(Byte.TYPE.getName()));
        assertNotNull(allDeserializers.get(Short.class.getName()));
        assertNotNull(allDeserializers.get(Short.TYPE.getName()));
        assertNotNull(allDeserializers.get(Double.class.getName()));
        assertNotNull(allDeserializers.get(Double.TYPE.getName()));
        assertNotNull(allDeserializers.get(Float.class.getName()));
        assertNotNull(allDeserializers.get(Float.TYPE.getName()));
    }

    @Test
    public void testShortSerializer() {
        ShortSerializer serializer = ShortSerializer.instance;
        assertNotNull(serializer);
    }

    @Test
    public void testIntegerSerializer() {
        IntegerSerializer serializer = new IntegerSerializer(Integer.class);
        assertNotNull(serializer);
    }

    @Test
    public void testIntLikeSerializer() {
        IntLikeSerializer serializer = IntLikeSerializer.instance;
        assertNotNull(serializer);
    }

    @Test
    public void testLongSerializer() {
        LongSerializer serializer = new LongSerializer(Long.class);
        assertNotNull(serializer);
    }

    @Test
    public void testFloatSerializer() {
        FloatSerializer serializer = FloatSerializer.instance;
        assertNotNull(serializer);
    }

    @Test
    public void testDoubleSerializer() {
        DoubleSerializer serializer = new DoubleSerializer(Double.class);
        assertNotNull(serializer);
    }
}