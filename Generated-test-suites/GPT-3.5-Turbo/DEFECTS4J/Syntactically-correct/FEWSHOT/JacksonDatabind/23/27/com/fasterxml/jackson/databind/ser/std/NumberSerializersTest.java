package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class NumberSerializersTest {
    @Test
    public void testShortSerializer() {
        ShortSerializer instance = new ShortSerializer();
        assertEquals(Short.class, instance.handledType());
        assertEquals(JsonParser.NumberType.INT, instance._numberType);
        assertEquals("number", instance._schemaType);
        
        Short value = 10;
        assertEquals(value.shortValue(), instance.serialize(value, null, null).shortValue());
    }
    
    @Test
    public void testIntegerSerializer() {
        IntegerSerializer instance = new IntegerSerializer();
        assertEquals(Integer.class, instance.handledType());
        assertEquals(JsonParser.NumberType.INT, instance._numberType);
        assertEquals("integer", instance._schemaType);
        
        Integer value = 10;
        assertEquals(value.intValue(), instance.serialize(value, null, null).intValue());
        
        instance.serializeWithType(value, null, null, null);
    }
    
    @Test
    public void testIntLikeSerializer() {
        IntLikeSerializer instance = new IntLikeSerializer();
        assertEquals(Number.class, instance.handledType());
        assertEquals(JsonParser.NumberType.INT, instance._numberType);
        assertEquals("integer", instance._schemaType);
        
        Integer value = 10;
        assertEquals(value.intValue(), instance.serialize(value, null, null).intValue());
    }
    
    @Test
    public void testLongSerializer() {
        LongSerializer instance = new LongSerializer();
        assertEquals(Long.class, instance.handledType());
        assertEquals(JsonParser.NumberType.LONG, instance._numberType);
        assertEquals("number", instance._schemaType);
        
        Long value = 10L;
        assertEquals(value.longValue(), instance.serialize(value, null, null).longValue());
    }
    
    @Test
    public void testFloatSerializer() {
        FloatSerializer instance = new FloatSerializer();
        assertEquals(Float.class, instance.handledType());
        assertEquals(JsonParser.NumberType.FLOAT, instance._numberType);
        assertEquals("number", instance._schemaType);
        
        Float value = 10.0f;
        assertEquals(value.floatValue(), instance.serialize(value, null, null).floatValue(), 0.01);
    }
    
    @Test
    public void testDoubleSerializer() {
        DoubleSerializer instance = new DoubleSerializer();
        assertEquals(Double.class, instance.handledType());
        assertEquals(JsonParser.NumberType.DOUBLE, instance._numberType);
        assertEquals("number", instance._schemaType);
        
        Double value = 10.0;
        assertEquals(value.doubleValue(), instance.serialize(value, null, null).doubleValue(), 0.01);
        
        instance.serializeWithType(value, null, null, null);
    }
}