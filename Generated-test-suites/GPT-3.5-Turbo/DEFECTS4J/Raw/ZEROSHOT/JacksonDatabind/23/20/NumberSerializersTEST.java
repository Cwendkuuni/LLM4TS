package com.fasterxml.jackson.databind.ser.std.test;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Map;

public class NumberSerializersTest {

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);

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
    public void testShortSerializer() {
        NumberSerializers.ShortSerializer serializer = new NumberSerializers.ShortSerializer();

        Short value = new Short("10");

        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        serializer.serialize(value, generator, null);
        generator.flush();

        assertEquals("10", writer.toString());
    }

    @Test
    public void testIntegerSerializer() {
        NumberSerializers.IntegerSerializer serializer = new NumberSerializers.IntegerSerializer();

        Integer value = 10;

        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        serializer.serialize(value, generator, null);
        generator.flush();

        assertEquals("10", writer.toString());
    }

    @Test
    public void testIntLikeSerializer() {
        NumberSerializers.IntLikeSerializer serializer = new NumberSerializers.IntLikeSerializer();

        Number value = 10;

        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        serializer.serialize(value, generator, null);
        generator.flush();

        assertEquals("10", writer.toString());
    }

    @Test
    public void testLongSerializer() {
        NumberSerializers.LongSerializer serializer = new NumberSerializers.LongSerializer();

        Long value = 10L;

        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        serializer.serialize(value, generator, null);
        generator.flush();

        assertEquals("10", writer.toString());
    }

    @Test
    public void testFloatSerializer() {
        NumberSerializers.FloatSerializer serializer = new NumberSerializers.FloatSerializer();

        Float value = 10.0F;

        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        serializer.serialize(value, generator, null);
        generator.flush();

        assertEquals("10.0", writer.toString());
    }

    @Test
    public void testDoubleSerializer() {
        NumberSerializers.DoubleSerializer serializer = new NumberSerializers.DoubleSerializer();

        Double value = 10.0;

        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        serializer.serialize(value, generator, null);
        generator.flush();

        assertEquals("10.0", writer.toString());
    }
}