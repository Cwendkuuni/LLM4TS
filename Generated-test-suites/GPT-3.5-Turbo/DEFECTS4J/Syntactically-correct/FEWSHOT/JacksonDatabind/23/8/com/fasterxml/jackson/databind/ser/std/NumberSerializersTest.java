package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class NumberSerializersTest {
    @Test
    public void testShortSerializer() {
        ShortSerializer serializer = new ShortSerializer();
        Short value = 10;
        assertEquals("10", serializer.serialize(value));
    }
    
    @Test
    public void testIntegerSerializer() {
        IntegerSerializer serializer = new IntegerSerializer();
        Integer value = 10;
        assertEquals("10", serializer.serialize(value));
    }
    
    @Test
    public void testIntLikeSerializer() {
        IntLikeSerializer serializer = new IntLikeSerializer();
        Number value = 10;
        assertEquals("10", serializer.serialize(value));
    }
    
    @Test
    public void testLongSerializer() {
        LongSerializer serializer = new LongSerializer();
        Long value = 10000000000L;
        assertEquals("10000000000", serializer.serialize(value));
    }
    
    @Test
    public void testFloatSerializer() {
        FloatSerializer serializer = new FloatSerializer();
        Float value = 10.5f;
        assertEquals("10.5", serializer.serialize(value));
    }
    
    @Test
    public void testDoubleSerializer() {
        DoubleSerializer serializer = new DoubleSerializer();
        Double value = 10.5;
        assertEquals("10.5", serializer.serialize(value));
    }
}