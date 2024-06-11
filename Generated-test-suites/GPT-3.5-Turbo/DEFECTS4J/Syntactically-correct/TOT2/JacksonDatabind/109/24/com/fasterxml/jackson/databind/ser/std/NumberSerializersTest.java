package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.Base;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.DoubleSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.FloatSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.IntegerSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.IntLikeSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.LongSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.ShortSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers;

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
    public void testShortSerializer() throws IOException {
        ShortSerializer shortSerializer = new ShortSerializer();
        JsonSerializer<?> serializer = (JsonSerializer<?>) shortSerializer;

        assertTrue(serializer instanceof Base);
    }

    @Test
    public void testIntegerSerializer() throws IOException {
        IntegerSerializer integerSerializer = new IntegerSerializer(Integer.class);
        JsonSerializer<?> serializer = (JsonSerializer<?>) integerSerializer;

        assertTrue(serializer instanceof Base);
    }

    @Test
    public void testIntLikeSerializer() throws IOException {
        IntLikeSerializer intLikeSerializer = new IntLikeSerializer();
        JsonSerializer<?> serializer = (JsonSerializer<?>) intLikeSerializer;

        assertTrue(serializer instanceof Base);
    }

    @Test
    public void testLongSerializer() throws IOException {
        LongSerializer longSerializer = new LongSerializer(Long.class);
        JsonSerializer<?> serializer = (JsonSerializer<?>) longSerializer;

        assertTrue(serializer instanceof Base);
    }

    @Test
    public void testFloatSerializer() throws IOException {
        FloatSerializer floatSerializer = new FloatSerializer();
        JsonSerializer<?> serializer = (JsonSerializer<?>) floatSerializer;

        assertTrue(serializer instanceof Base);
    }

    @Test
    public void testDoubleSerializer() throws IOException {
        DoubleSerializer doubleSerializer = new DoubleSerializer(Double.class);
        JsonSerializer<?> serializer = (JsonSerializer<?>) doubleSerializer;

        assertTrue(serializer instanceof Base);
    }
}