package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ser.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.*;

public class NumberSerializersTest {

    private Map<String, JsonSerializer<?>> allDeserializers;

    @Before
    public void setUp() {
        allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);
    }

    @Test
    public void testAddAll() {
        assertNotNull(allDeserializers);
        assertEquals(10, allDeserializers.size());
    }

    @Test
    public void testShortSerializer() {
        ShortSerializer shortSerializer = new ShortSerializer();
        assertNotNull(shortSerializer.instance);
        assertEquals(Short.class, shortSerializer.handledType());
    }

    @Test
    public void testIntegerSerializer() {
        IntegerSerializer integerSerializer = new IntegerSerializer();
        assertNotNull(integerSerializer.instance);
        assertEquals(Integer.class, integerSerializer.handledType());
    }

    @Test
    public void testIntLikeSerializer() {
        IntLikeSerializer intLikeSerializer = new IntLikeSerializer();
        assertNotNull(intLikeSerializer.instance);
        assertEquals(Number.class, intLikeSerializer.handledType());
    }

    @Test
    public void testLongSerializer() {
        LongSerializer longSerializer = new LongSerializer();
        assertNotNull(longSerializer.instance);
        assertEquals(Long.class, longSerializer.handledType());
    }

    @Test
    public void testFloatSerializer() {
        FloatSerializer floatSerializer = new FloatSerializer();
        assertNotNull(floatSerializer.instance);
        assertEquals(Float.class, floatSerializer.handledType());
    }

    @Test
    public void testDoubleSerializer() {
        DoubleSerializer doubleSerializer = new DoubleSerializer();
        assertNotNull(doubleSerializer.instance);
        assertEquals(Double.class, doubleSerializer.handledType());
    }
}