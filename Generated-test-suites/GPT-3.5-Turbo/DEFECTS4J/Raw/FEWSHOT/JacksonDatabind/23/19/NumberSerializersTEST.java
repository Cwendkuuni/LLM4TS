package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class NumberSerializersTest {
    
    @Test
    public void testShortSerializer() {
        ShortSerializer serializer = new ShortSerializer();
        Short value = 10;
        assertEquals("10", serialize(value, serializer));
    }
    
    @Test
    public void testIntegerSerializer() {
        IntegerSerializer serializer = new IntegerSerializer();
        Integer value = 10;
        assertEquals("10", serialize(value, serializer));
    }
    
    @Test
    public void testIntLikeSerializer() {
        IntLikeSerializer serializer = new IntLikeSerializer();
        Number value = 10;
        assertEquals("10", serialize(value, serializer));
    }
    
    @Test
    public void testLongSerializer() {
        LongSerializer serializer = new LongSerializer();
        Long value = 10L;
        assertEquals("10", serialize(value, serializer));
    }
    
    @Test
    public void testFloatSerializer() {
        FloatSerializer serializer = new FloatSerializer();
        Float value = 10.5f;
        assertEquals("10.5", serialize(value, serializer));
    }
    
    @Test
    public void testDoubleSerializer() {
        DoubleSerializer serializer = new DoubleSerializer();
        Double value = 10.5;
        assertEquals("10.5", serialize(value, serializer));
    }
    
    private <T> String serialize(T value, Base<T> serializer) {
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        try {
            serializer.serialize(value, generator, null);
            generator.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return writer.toString();
    }
}