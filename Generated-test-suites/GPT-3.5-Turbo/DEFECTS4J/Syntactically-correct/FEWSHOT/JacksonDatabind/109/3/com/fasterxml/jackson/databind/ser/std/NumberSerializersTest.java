package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class NumberSerializersTest {
    @Test
    public void testShortSerializer() {
        ShortSerializer serializer = new ShortSerializer();

        Short value = 10;
        assertEquals(10, serializer.serialize(value));

        value = -5;
        assertEquals(-5, serializer.serialize(value));
    }

    @Test
    public void testIntegerSerializer() {
        IntegerSerializer serializer = new IntegerSerializer();

        Integer value = 100;
        assertEquals(100, serializer.serialize(value));

        value = -50;
        assertEquals(-50, serializer.serialize(value));
    }

    @Test
    public void testIntLikeSerializer() {
        IntLikeSerializer serializer = new IntLikeSerializer();

        Number value = 200;
        assertEquals(200, serializer.serialize(value));

        value = -100;
        assertEquals(-100, serializer.serialize(value));
    }

    @Test
    public void testLongSerializer() {
        LongSerializer serializer = new LongSerializer();

        Long value = 1000L;
        assertEquals(1000L, serializer.serialize(value));

        value = -500L;
        assertEquals(-500L, serializer.serialize(value));
    }

    @Test
    public void testFloatSerializer() {
        FloatSerializer serializer = new FloatSerializer();

        Float value = 3.14f;
        assertEquals(3.14f, serializer.serialize(value));

        value = -2.5f;
        assertEquals(-2.5f, serializer.serialize(value));
    }

    @Test
    public void testDoubleSerializer() {
        DoubleSerializer serializer = new DoubleSerializer();

        Double value = 3.14159265359;
        assertEquals(3.14159265359, serializer.serialize(value));

        value = -2.71828182846;
        assertEquals(-2.71828182846, serializer.serialize(value));
    }
}