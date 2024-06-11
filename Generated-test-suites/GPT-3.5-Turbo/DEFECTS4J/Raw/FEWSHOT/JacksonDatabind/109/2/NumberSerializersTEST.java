package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
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
        assertTrue(allDeserializers.containsKey(Double.class.getName()));
        assertTrue(allDeserializers.containsKey(Double.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Float.class.getName()));
        assertTrue(allDeserializers.containsKey(Float.TYPE.getName()));
    }

    @Test
    public void testShortSerializer() throws IOException {
        ShortSerializer serializer = new ShortSerializer();
        SerializerProvider provider = new SerializerProvider();
        JsonGenerator gen = new JsonGenerator();

        serializer.serialize((short) 123, gen, provider);

        assertEquals(gen.getOutput(), "123");
    }

    @Test
    public void testIntegerSerializer() throws IOException {
        IntegerSerializer serializer = new IntegerSerializer(Integer.class);
        SerializerProvider provider = new SerializerProvider();
        JsonGenerator gen = new JsonGenerator();

        serializer.serialize(123, gen, provider);

        assertEquals(gen.getOutput(), "123");
    }

    @Test
    public void testIntLikeSerializer() throws IOException {
        IntLikeSerializer serializer = new IntLikeSerializer();
        SerializerProvider provider = new SerializerProvider();
        JsonGenerator gen = new JsonGenerator();

        serializer.serialize(123, gen, provider);

        assertEquals(gen.getOutput(), "123");
    }

    @Test
    public void testLongSerializer() throws IOException {
        LongSerializer serializer = new LongSerializer(Long.class);
        SerializerProvider provider = new SerializerProvider();
        JsonGenerator gen = new JsonGenerator();

        serializer.serialize(123L, gen, provider);

        assertEquals(gen.getOutput(), "123");
    }

    @Test
    public void testFloatSerializer() throws IOException {
        FloatSerializer serializer = new FloatSerializer();
        SerializerProvider provider = new SerializerProvider();
        JsonGenerator gen = new JsonGenerator();

        serializer.serialize(123.45f, gen, provider);

        assertEquals(gen.getOutput(), "123.45");
    }

    @Test
    public void testDoubleSerializer() throws IOException {
        DoubleSerializer serializer = new DoubleSerializer(Double.class);
        SerializerProvider provider = new SerializerProvider();
        JsonGenerator gen = new JsonGenerator();

        serializer.serialize(123.45, gen, provider);

        assertEquals(gen.getOutput(), "123.45");
    }
}