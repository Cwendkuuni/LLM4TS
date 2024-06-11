package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class NumberSerializersTest {
    @Test
    public void testShortSerializer() {
        ShortSerializer shortSerializer = new ShortSerializer();
        Short value = (short) 100;
        assertEquals("100", shortSerializer.serialize(value));
    }
    
    @Test
    public void testIntegerSerializer() {
        IntegerSerializer integerSerializer = new IntegerSerializer(Integer.class);
        Integer value = 100;
        assertEquals("100", integerSerializer.serialize(value));
    }
    
    @Test
    public void testIntLikeSerializer() {
        IntLikeSerializer intLikeSerializer = new IntLikeSerializer();
        Byte value = (byte) 100;
        assertEquals("100", intLikeSerializer.serialize(value));
    }
    
    @Test
    public void testLongSerializer() {
        LongSerializer longSerializer = new LongSerializer(Long.class);
        Long value = 100L;
        assertEquals("100", longSerializer.serialize(value));
    }
    
    @Test
    public void testFloatSerializer() {
        FloatSerializer floatSerializer = new FloatSerializer();
        Float value = 100.0f;
        assertEquals("100.0", floatSerializer.serialize(value));
    }
    
    @Test
    public void testDoubleSerializer() {
        DoubleSerializer doubleSerializer = new DoubleSerializer(Double.class);
        Double value = 100.0;
        assertEquals("100.0", doubleSerializer.serialize(value));
    }
}