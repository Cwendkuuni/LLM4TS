package com.fasterxml.jackson.databind.ser.std;

import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializersTest {

    @Test
    public void testSerializeShort() {
        NumberSerializers.ShortSerializer serializer = new NumberSerializers.ShortSerializer();
        Short value = 10;
        assertEquals("10", serializer.serialize(value));
    }
    
    @Test
    public void testSerializeInteger() {
        NumberSerializers.IntegerSerializer serializer = new NumberSerializers.IntegerSerializer();
        Integer value = 100;
        assertEquals("100", serializer.serialize(value));
    }
    
    @Test
    public void testSerializeIntLike() {
        NumberSerializers.IntLikeSerializer serializer = new NumberSerializers.IntLikeSerializer();
        Number value = 100;
        assertEquals("100", serializer.serialize(value));
    }
    
    @Test
    public void testSerializeLong() {
        NumberSerializers.LongSerializer serializer = new NumberSerializers.LongSerializer();
        Long value = 10000000000L;
        assertEquals("10000000000", serializer.serialize(value));
    }
    
    @Test
    public void testSerializeFloat() {
        NumberSerializers.FloatSerializer serializer = new NumberSerializers.FloatSerializer();
        Float value = 3.14f;
        assertEquals("3.14", serializer.serialize(value));
    }
    
    @Test
    public void testSerializeDouble() {
        NumberSerializers.DoubleSerializer serializer = new NumberSerializers.DoubleSerializer();
        Double value = 3.14159265359;
        assertEquals("3.14159265359", serializer.serialize(value));
    }
}