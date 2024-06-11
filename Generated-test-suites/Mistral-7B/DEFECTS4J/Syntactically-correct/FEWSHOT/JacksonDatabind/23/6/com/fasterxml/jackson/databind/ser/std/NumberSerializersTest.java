package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.*;
import org.junit.Test;

public class NumberSerializersTest {

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new java.util.LinkedHashMap<>();
        NumberSerializers.addAll(allDeserializers);
        assertEquals(11, allDeserializers.size());
        assertEquals(Byte.class, allDeserializers.get(Byte.class.getName()));
        assertEquals(Byte.TYPE, allDeserializers.get(Byte.TYPE.getName()));
        assertEquals(Short.class, allDeserializers.get(Short.class.getName()));
        assertEquals(Short.TYPE, allDeserializers.get(Short.TYPE.getName()));
        assertEquals(Integer.class, allDeserializers.get(Integer.class.getName()));
        assertEquals(Integer.TYPE, allDeserializers.get(Integer.TYPE.getName()));
        assertEquals(Long.class, allDeserializers.get(Long.class.getName()));
        assertEquals(Long.TYPE, allDeserializers.get(Long.TYPE.getName()));
        assertEquals(Float.class, allDeserializers.get(Float.class.getName()));
        assertEquals(Float.TYPE, allDeserializers.get(Float.TYPE.getName()));
        assertEquals(Double.class, allDeserializers.get(Double.class.getName()));
        assertEquals(Double.TYPE, allDeserializers.get(Double.TYPE.getName()));
    }

    @Test
    public void testShortSerializer() {
        ShortSerializer shortSerializer = new ShortSerializer();
        assertNotNull(shortSerializer);
        assertEquals("number", shortSerializer._schemaType);
        assertEquals(JsonParser.NumberType.INT, shortSerializer._numberType);
        assertEquals("short", shortSerializer.getClass().getSimpleName());
    }

    @Test
    public void testIntegerSerializer() {
        IntegerSerializer integerSerializer = new IntegerSerializer();
        assertNotNull(integerSerializer);
        assertEquals("integer", integerSerializer._schemaType);
        assertEquals(JsonParser.NumberType.INT, integerSerializer._numberType);
        assertEquals("integer", integerSerializer.getClass().getSimpleName());
    }

    @Test
    public void testIntLikeSerializer() {
        IntLikeSerializer intLikeSerializer = new IntLikeSerializer();
        assertNotNull(intLikeSerializer);
        assertEquals("integer", intLikeSerializer._schemaType);
        assertEquals(JsonParser.NumberType.INT, intLikeSerializer._numberType);
        assertEquals("number", intLikeSerializer.getClass().getSimpleName());
    }

    @Test
    public void testLongSerializer() {
        LongSerializer longSerializer = new LongSerializer();
        assertNotNull(longSerializer);
        assertEquals("number", longSerializer._schemaType);
        assertEquals(JsonParser.NumberType.LONG, longSerializer._numberType);
        assertEquals("long", longSerializer.getClass().getSimpleName());
    }

    @Test
    public void testFloatSerializer() {
        FloatSerializer floatSerializer = new FloatSerializer();
        assertNotNull(floatSerializer);
        assertEquals("number", floatSerializer._schemaType);
        assertEquals(JsonParser.NumberType.FLOAT, floatSerializer._numberType);
        assertEquals("float", floatSerializer.getClass().getSimpleName());
    }

    @Test
    public void testDoubleSerializer() {
        DoubleSerializer doubleSerializer = new DoubleSerializer();
        assertNotNull(doubleSerializer);
        assertEquals("number", doubleSerializer._schemaType);
        assertEquals(JsonParser.NumberType.DOUBLE, doubleSerializer._numberType);
        assertEquals("double", doubleSerializer.getClass().getSimpleName());
    }
}