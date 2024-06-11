package com.fasterxml.jackson.databind.deser.std;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        DeserializationContext ctxt = null;

        // Test null key
        Object result = deserializer.deserializeKey(null, ctxt);
        Assert.assertNull(result);

        // Test valid key
        result = deserializer.deserializeKey("test", ctxt);
        Assert.assertEquals("test", result);

        // Test invalid key
        result = deserializer.deserializeKey("", ctxt);
        Assert.assertNull(result);
    }

    @Test
    public void testGetKeyClass() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        Class<?> result = deserializer.getKeyClass();
        Assert.assertEquals(String.class, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseIntException() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        deserializer._parseInt("test");
    }

    @Test
    public void testParseInt() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        int result = deserializer._parseInt("10");
        Assert.assertEquals(10, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseLongException() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LONG, Long.class);
        deserializer._parseLong("test");
    }

    @Test
    public void testParseLong() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LONG, Long.class);
        long result = deserializer._parseLong("10000");
        Assert.assertEquals(10000, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseDoubleException() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DOUBLE, Double.class);
        deserializer._parseDouble("test");
    }

    @Test
    public void testParseDouble() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DOUBLE, Double.class);
        double result = deserializer._parseDouble("3.14");
        Assert.assertEquals(3.14, result, 0.001);
    }
}