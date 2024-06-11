package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        assertEquals("test", deserializer.deserializeKey("test", null));
    }

    @Test
    public void testGetIntKey() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        assertEquals(Integer.valueOf(123), deserializer._parseInt("123"));
    }

    @Test
    public void testGetLongKey() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LONG, Long.class);
        assertEquals(Long.valueOf(1234567890), deserializer._parseLong("1234567890"));
    }

    @Test
    public void testGetDoubleKey() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DOUBLE, Double.class);
        assertEquals(Double.valueOf(3.14), deserializer._parseDouble("3.14"), 0.0001);
    }

    @Test
    public void testGetKeyClass() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        assertEquals(String.class, deserializer.getKeyClass());
    }
}