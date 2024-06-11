package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;

import static org.junit.Assert.*;

public class NumberSerializersTest {

    private NumberSerializers numberSerializers;

    @Before
    public void setUp() {
        numberSerializers = new NumberSerializers();
    }

    @Test
    public void testAddAll() {
        HashMap<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        numberSerializers.addAll(allDeserializers);

        assertNotNull(allDeserializers);
        assertFalse(allDeserializers.isEmpty());
        assertEquals(8, allDeserializers.size());
    }

    @Test
    public void testShortSerializerSerialize() throws IOException {
        NumberSerializers.ShortSerializer shortSerializer = new NumberSerializers.ShortSerializer();
        JsonGenerator generator = null;
        SerializerProvider provider = null;

        shortSerializer.serialize((short) 10, generator, provider);

        // Perform assertion here
    }

    @Test
    public void testIntegerSerializerSerialize() throws IOException {
        NumberSerializers.IntegerSerializer integerSerializer = new NumberSerializers.IntegerSerializer(Integer.class);
        JsonGenerator generator = null;
        SerializerProvider provider = null;

        integerSerializer.serialize(10, generator, provider);

        // Perform assertion here
    }

    @Test
    public void testIntLikeSerializerSerialize() throws IOException {
        NumberSerializers.IntLikeSerializer intLikeSerializer = new NumberSerializers.IntLikeSerializer();
        JsonGenerator generator = null;
        SerializerProvider provider = null;

        intLikeSerializer.serialize(10, generator, provider);

        // Perform assertion here
    }

    @Test
    public void testLongSerializerSerialize() throws IOException {
        NumberSerializers.LongSerializer longSerializer = new NumberSerializers.LongSerializer(Long.class);
        JsonGenerator generator = null;
        SerializerProvider provider = null;

        longSerializer.serialize(10L, generator, provider);

        // Perform assertion here
    }

    @Test
    public void testFloatSerializerSerialize() throws IOException {
        NumberSerializers.FloatSerializer floatSerializer = new NumberSerializers.FloatSerializer();
        JsonGenerator generator = null;
        SerializerProvider provider = null;

        floatSerializer.serialize(10.5f, generator, provider);

        // Perform assertion here
    }

    @Test
    public void testDoubleSerializerSerialize() throws IOException {
        NumberSerializers.DoubleSerializer doubleSerializer = new NumberSerializers.DoubleSerializer(Double.class);
        JsonGenerator generator = null;
        SerializerProvider provider = null;

        doubleSerializer.serialize(10.5, generator, provider);

        // Perform assertion here
    }
}