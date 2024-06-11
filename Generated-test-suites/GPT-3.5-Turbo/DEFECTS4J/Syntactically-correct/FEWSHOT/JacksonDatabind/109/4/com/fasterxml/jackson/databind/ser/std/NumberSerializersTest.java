package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class NumberSerializersTest {
    @Test
    public void testShortSerializer() {
        NumberSerializers.ShortSerializer serializer = new NumberSerializers.ShortSerializer();
        StringBuilder sb = new StringBuilder();
        serializer.serialize((short) 10, new JsonGenerator(sb), new SerializerProvider());
        assertEquals("10", sb.toString());
    }

    @Test
    public void testIntegerSerializer() {
        NumberSerializers.IntegerSerializer serializer = new NumberSerializers.IntegerSerializer(Integer.class);
        StringBuilder sb = new StringBuilder();
        serializer.serialize(10, new JsonGenerator(sb), new SerializerProvider());
        assertEquals("10", sb.toString());
    }

    @Test
    public void testIntLikeSerializer() {
        NumberSerializers.IntLikeSerializer serializer = new NumberSerializers.IntLikeSerializer();
        StringBuilder sb = new StringBuilder();
        serializer.serialize(10, new JsonGenerator(sb), new SerializerProvider());
        assertEquals("10", sb.toString());
    }

    @Test
    public void testLongSerializer() {
        NumberSerializers.LongSerializer serializer = new NumberSerializers.LongSerializer(Long.class);
        StringBuilder sb = new StringBuilder();
        serializer.serialize(10L, new JsonGenerator(sb), new SerializerProvider());
        assertEquals("10", sb.toString());
    }

    @Test
    public void testFloatSerializer() {
        NumberSerializers.FloatSerializer serializer = new NumberSerializers.FloatSerializer();
        StringBuilder sb = new StringBuilder();
        serializer.serialize(10.5f, new JsonGenerator(sb), new SerializerProvider());
        assertEquals("10.5", sb.toString());
    }

    @Test
    public void testDoubleSerializer() {
        NumberSerializers.DoubleSerializer serializer = new NumberSerializers.DoubleSerializer(Double.class);
        StringBuilder sb = new StringBuilder();
        serializer.serialize(10.5, new JsonGenerator(sb), new SerializerProvider());
        assertEquals("10.5", sb.toString());
    }
}