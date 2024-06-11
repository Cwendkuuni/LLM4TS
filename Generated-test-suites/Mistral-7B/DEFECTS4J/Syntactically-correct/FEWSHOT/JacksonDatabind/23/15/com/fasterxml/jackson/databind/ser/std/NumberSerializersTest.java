package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonIntegerFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonNumberFormatVisitor;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.*;

import java.io.*;
import java.lang.reflect.Type;
import java.util.Map;

public class NumberSerializersTest {

    @Test
    public void testBaseClass() throws Exception {
        Base<Integer> intSerializer = new IntegerSerializer();
        assertEquals(JsonParser.NumberType.INT, intSerializer._numberType);
        assertEquals("number", intSerializer._schemaType);
        assertTrue(intSerializer._isInt);
    }

    @Test
    public void testShortSerializer() throws Exception {
        ShortSerializer shortSerializer = new ShortSerializer();
        assertEquals(Short.class, shortSerializer.getClass());
        assertEquals(JsonParser.NumberType.INT, shortSerializer._numberType);
        assertEquals("number", shortSerializer._schemaType);
        assertTrue(shortSerializer._isInt);
    }

    @Test
    public void testIntegerSerializer() throws Exception {
        IntegerSerializer integerSerializer = new IntegerSerializer();
        assertEquals(Object.class, integerSerializer.getClass());
        assertEquals(JsonParser.NumberType.INT, integerSerializer._numberType);
        assertEquals("integer", integerSerializer._schemaType);
        assertTrue(integerSerializer._isInt);
    }

    @Test
    public void testIntLikeSerializer() throws Exception {
        IntLikeSerializer intLikeSerializer = new IntLikeSerializer();
        assertEquals(Number.class, intLikeSerializer.getClass());
        assertEquals(JsonParser.NumberType.INT, intLikeSerializer._numberType);
        assertEquals("integer", intLikeSerializer._schemaType);
        assertTrue(intLikeSerializer._isInt);
    }

    @Test
    public void testLongSerializer() throws Exception {
        LongSerializer longSerializer = new LongSerializer();
        assertEquals(Object.class, longSerializer.getClass());
        assertEquals(JsonParser.NumberType.LONG, longSerializer._numberType);
        assertEquals("number", longSerializer._schemaType);
        assertFalse(longSerializer._isInt);
    }

    @Test
    public void testFloatSerializer() throws Exception {
        FloatSerializer floatSerializer = new FloatSerializer();
        assertEquals(Object.class, floatSerializer.getClass());
        assertEquals(JsonParser.NumberType.FLOAT, floatSerializer._numberType);
        assertEquals("number", floatSerializer._schemaType);
        assertFalse(floatSerializer._isInt);
    }

    @Test
    public void testDoubleSerializer() throws Exception {
        DoubleSerializer doubleSerializer = new DoubleSerializer();
        assertEquals(Object.class, doubleSerializer.getClass());
        assertEquals(JsonParser.NumberType.DOUBLE, doubleSerializer._numberType);
        assertEquals("number", doubleSerializer._schemaType);
        assertFalse(doubleSerializer._isInt);
    }

    @Test
    public void testSerialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Integer number = 123;
        String json = mapper.writeValueAsString(number);
        assertEquals("123", json);

        Long longNumber = 456L;
        json = mapper.writeValueAsString(longNumber);
        assertEquals("456", json);

        Float floatNumber = 789.123F;
        json = mapper.writeValueAsString(floatNumber);
        assertEquals("789.123", json);

        Double doubleNumber = 1000.5678;
        json = mapper.writeValueAsString(doubleNumber);
        assertEquals("1000.5678", json);
    }

    @Test
    public void testDeserialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Integer number = mapper.readValue("123", Integer.class);
        assertEquals(123, number.intValue());

        Long longNumber = mapper.readValue("456", Long.class);
        assertEquals(456, longNumber.longValue());

        Float floatingPointNumber = mapper.readValue("789.123", Float.class);
        assertEquals(789.123f, floatingPointNumber.floatValue());

        Double doubleNumber = mapper.readValue("1000.5678", Double.class);
        assertEquals(1000.5678, doubleNumber.doubleValue(), 0.0001);
    }
}