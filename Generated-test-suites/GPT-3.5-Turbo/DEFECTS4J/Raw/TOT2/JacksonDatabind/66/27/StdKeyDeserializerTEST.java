package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;
import org.junit.Test;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKeyNull() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(TYPE_BOOLEAN, Boolean.class);
        assertNull(keyDeserializer.deserializeKey(null, new DeserializationContext()));
    }

    @Test
    public void testDeserializeKeyException() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(TYPE_INT, Integer.class);
        try {
            keyDeserializer.deserializeKey("abc", new DeserializationContext());
            fail("Expected IOException");
        } catch (IOException e) {
            // success
        }
    }

    @Test
    public void testDeserializeKeyBoolean() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(TYPE_BOOLEAN, Boolean.class);
        assertEquals(Boolean.TRUE, keyDeserializer.deserializeKey("true", new DeserializationContext()));
    }

    @Test
    public void testParseInt() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(TYPE_INT, Integer.class);
        assertEquals(123, keyDeserializer._parseInt("123"));
    }

    @Test
    public void testParseLong() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(TYPE_LONG, Long.class);
        assertEquals(123456789L, keyDeserializer._parseLong("123456789"));
    }

    @Test
    public void testParseDouble() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(TYPE_DOUBLE, Double.class);
        assertEquals(3.14, keyDeserializer._parseDouble("3.14"), 0.001);
    }
}