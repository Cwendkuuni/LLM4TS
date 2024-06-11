package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import org.junit.Test;

public class NumberSerializersTest {

    @Test
    public void testShortSerializer() {
        ShortSerializer shortSerializer = new ShortSerializer();
        assertEquals(Short.TYPE, shortSerializer.getParameterizedType(Short.class));
        assertEquals(JsonParser.NumberType.INT, shortSerializer._numberType);
        assertEquals("number", shortSerializer._schemaType);
        shortSerializer.serialize(Short.MIN_VALUE, new JsonGenerator(new StringWriter()), new SerializerProvider());
        shortSerializer.serialize(Short.MAX_VALUE, new JsonGenerator(new StringWriter()), new SerializerProvider());
    }

    @Test
    public void testIntegerSerializer() {
        IntegerSerializer integerSerializer = new IntegerSerializer();
        assertEquals(Integer.TYPE, integerSerializer.getParameterizedType(Object.class));
        assertEquals(JsonParser.NumberType.INT, integerSerializer._numberType);
        assertEquals("integer", integerSerializer._schemaType);
        integerSerializer.serialize(Integer.MIN_VALUE, new JsonGenerator(new StringWriter()), new SerializerProvider());
        integerSerializer.serialize(Integer.MAX_VALUE, new JsonGenerator(new StringWriter()), new SerializerProvider());
    }

    @Test
    public void testIntLikeSerializer() {
        IntLikeSerializer intLikeSerializer = new IntLikeSerializer();
        assertEquals(Number.class, intLikeSerializer.getParameterizedType(Number.class));
        assertEquals(JsonParser.NumberType.INT, intLikeSerializer._numberType);
        assertEquals("integer", intLikeSerializer._schemaType);
        intLikeSerializer.serialize(Byte.MIN_VALUE, new JsonGenerator(new StringWriter()), new SerializerProvider());
        intLikeSerializer.serialize(Byte.MAX_VALUE, new JsonGenerator(new StringWriter()), new SerializerProvider());
    }

    @Test
    public void testLongSerializer() {
        LongSerializer longSerializer = new LongSerializer();
        assertEquals(Long.TYPE, longSerializer.getParameterizedType(Object.class));
        assertEquals(JsonParser.NumberType.LONG, longSerializer._numberType);
        assertEquals("number", longSerializer._schemaType);
        longSerializer.serialize(Long.MIN_VALUE, new JsonGenerator(new StringWriter()), new SerializerProvider());
        longSerializer.serialize(Long.MAX_VALUE, new JsonGenerator(new StringWriter()), new SerializerProvider());
    }

    @Test
    public void testFloatSerializer() {
        FloatSerializer floatSerializer = new FloatSerializer();
        assertEquals(Float.TYPE, floatSerializer.getParameterizedType(Object.class));
        assertEquals(JsonParser.NumberType.FLOAT, floatSerializer._numberType);
        assertEquals("number", floatSerializer._schemaType);
        floatSerializer.serialize(Float.MIN_VALUE, new JsonGenerator(new StringWriter()), new SerializerProvider());
        floatSerializer.serialize(Float.MAX_VALUE, new JsonGenerator(new StringWriter()), new SerializerProvider());
    }

    @Test
    public void testDoubleSerializer() {
        DoubleSerializer doubleSerializer = new DoubleSerializer();
        assertEquals(Double.TYPE, doubleSerializer.getParameterizedType(Object.class));
        assertEquals(JsonParser.NumberType.DOUBLE, doubleSerializer._numberType);
        assertEquals("number", doubleSerializer._schemaType);
        doubleSerializer.serialize(Double.MIN_VALUE, new JsonGenerator(new StringWriter()), new SerializerProvider());
        doubleSerializer.serialize(Double.MAX_VALUE, new JsonGenerator(new StringWriter()), new SerializerProvider());
    }
}