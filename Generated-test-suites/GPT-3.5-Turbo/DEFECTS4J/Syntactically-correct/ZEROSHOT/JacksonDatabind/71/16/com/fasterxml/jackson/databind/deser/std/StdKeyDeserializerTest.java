package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey() throws Exception {
        StdKeyDeserializer deser = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);

        Object result = deser.deserializeKey("10", null);
        assertEquals(Integer.class, deser.getKeyClass());
        assertEquals(10, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseIntException() throws Exception {
        StdKeyDeserializer deser = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        deser._parseInt("abc");
    }

    @Test
    public void testParseInt() throws Exception {
        StdKeyDeserializer deser = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        int result = deser._parseInt("10");
        assertEquals(10, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseLongException() throws Exception {
        StdKeyDeserializer deser = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LONG, Long.class);
        deser._parseLong("abc");
    }

    @Test
    public void testParseLong() throws Exception {
        StdKeyDeserializer deser = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LONG, Long.class);
        long result = deser._parseLong("12345678901234");
        assertEquals(12345678901234L, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseDoubleException() throws Exception {
        StdKeyDeserializer deser = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DOUBLE, Double.class);
        deser._parseDouble("abc");
    }

    @Test
    public void testParseDouble() throws Exception {
        StdKeyDeserializer deser = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DOUBLE, Double.class);
        double result = deser._parseDouble("12.34");
        assertEquals(12.34, result, 0.0);
    }
}