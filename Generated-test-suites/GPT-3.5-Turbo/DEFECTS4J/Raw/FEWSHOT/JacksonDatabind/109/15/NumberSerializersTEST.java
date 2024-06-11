package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class NumberSerializersTest {
    @Test
    public void testShortSerializer() {
        ShortSerializer shortSerializer = new ShortSerializer();
        assertEquals("number", shortSerializer._schemaType);
        assertEquals(JsonParser.NumberType.INT, shortSerializer._numberType);
        
        Short value = 10;
        JsonGenerator gen = new JsonGenerator(null, 0, null, null);
        gen.setNumberType(JsonGenerator.NumberType.INT);
        
        assertEquals("10",gen.writeNumber(value.shortValue()));
    }

    @Test
    public void testIntegerSerializer() {
        IntegerSerializer integerSerializer = new IntegerSerializer(Integer.class);
        assertEquals("integer", integerSerializer._schemaType);
        assertEquals(JsonParser.NumberType.INT, integerSerializer._numberType);
        
        Integer value = 10;
        JsonGenerator gen = new JsonGenerator(null, 0, null, null);
        gen.setNumberType(JsonGenerator.NumberType.INT);
        
        assertEquals("10",gen.writeNumber(value.intValue()));
    }

    @Test
    public void testIntLikeSerializer() {
        IntLikeSerializer intLikeSerializer = IntLikeSerializer.instance;
        assertEquals("integer", intLikeSerializer._schemaType);
        assertEquals(JsonParser.NumberType.INT, intLikeSerializer._numberType);
        
        Number value = 10;
        JsonGenerator gen = new JsonGenerator(null, 0, null, null);
        gen.setNumberType(JsonGenerator.NumberType.INT);
        
        assertEquals("10",gen.writeNumber(value.intValue()));
    }

    @Test
    public void testLongSerializer() {
        LongSerializer longSerializer = new LongSerializer(Long.class);
        assertEquals("number", longSerializer._schemaType);
        assertEquals(JsonParser.NumberType.LONG, longSerializer._numberType);
        
        Long value = 10L;
        JsonGenerator gen = new JsonGenerator(null, 0, null, null);
        gen.setNumberType(JsonGenerator.NumberType.LONG);
        
        assertEquals("10",gen.writeNumber(value.longValue()));
    }

    @Test
    public void testFloatSerializer() {
        FloatSerializer floatSerializer = FloatSerializer.instance;
        assertEquals("number", floatSerializer._schemaType);
        assertEquals(JsonParser.NumberType.FLOAT, floatSerializer._numberType);
        
        Float value = 10.0F;
        JsonGenerator gen = new JsonGenerator(null, 0, null, null);
        gen.setNumberType(JsonGenerator.NumberType.FLOAT);
        
        assertEquals("10.0",gen.writeNumber(value.floatValue()));
    }

    @Test
    public void testDoubleSerializer() {
        DoubleSerializer doubleSerializer = new DoubleSerializer(Double.class);
        assertEquals("number", doubleSerializer._schemaType);
        assertEquals(JsonParser.NumberType.DOUBLE, doubleSerializer._numberType);
        
        Double value = 10.0;
        JsonGenerator gen = new JsonGenerator(null, 0, null, null);
        gen.setNumberType(JsonGenerator.NumberType.DOUBLE);
        
        assertEquals("10.0",gen.writeNumber(value.doubleValue()));
    }
}