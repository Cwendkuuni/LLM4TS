package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

    @Test
    public void deserializeKeyTest() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
        String key = "true";
        Boolean expectedResult = Boolean.TRUE;
        Object result = deserializer.deserializeKey(key, null);
        assertEquals(expectedResult, result);
    }

    @Test
    public void getKeyClassTest() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BYTE, Byte.class);
        Class<?> expectedResult = Byte.class;
        Class<?> result = deserializer.getKeyClass();
        assertEquals(expectedResult, result);
    }

    @Test
    public void _parseTest() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_CHAR, Character.class);
        String key = "a";
        Character expectedResult = 'a';
        Object result = deserializer._parse(key, null);
        assertEquals(expectedResult, result);
    }

    @Test
    public void _parseIntTest() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        String key = "123";
        int expectedResult = 123;
        int result = deserializer._parseInt(key);
        assertEquals(expectedResult, result);
    }

    @Test
    public void _parseLongTest() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LONG, Long.class);
        String key = "123";
        long expectedResult = 123L;
        long result = deserializer._parseLong(key);
        assertEquals(expectedResult, result);
    }

    @Test
    public void _parseDoubleTest() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_FLOAT, Float.class);
        String key = "123.45";
        double expectedResult = 123.45;
        double result = deserializer._parseDouble(key);
        assertEquals(expectedResult, result, 0.001);
    }
}