package com.fasterxml.jackson.databind.deser.std;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

    private StdKeyDeserializer keyDeserializer;

    @Before
    public void setUp() throws Exception {
        keyDeserializer = new StdKeyDeserializer(1, boolean.class);
    }

    @Test
    public void testDeserializeKey() throws Exception {
        Object result = keyDeserializer.deserializeKey("true", null);
        assertTrue((Boolean) result);
        result = keyDeserializer.deserializeKey("false", null);
        assertFalse((Boolean) result);
    }

    @Test
    public void testGetKeyClass() throws Exception {
        Class<?> result = keyDeserializer.getKeyClass();
        assertEquals(boolean.class, result);
    }

    @Test
    public void testParse() throws Exception {
        Object result = keyDeserializer._parse("true", null);
        assertTrue((Boolean) result);
        result = keyDeserializer._parse("false", null);
        assertFalse((Boolean) result);
    }

    @Test
    public void testParseInt() throws Exception {
        int result = keyDeserializer._parseInt("123");
        assertEquals(123, result);
    }

    @Test
    public void testParseLong() throws Exception {
        long result = keyDeserializer._parseLong("123");
        assertEquals(123, result);
    }

    @Test
    public void testParseDouble() throws Exception {
        double result = keyDeserializer._parseDouble("123.456");
        assertEquals(123.456, result, 0.0001);
    }
}