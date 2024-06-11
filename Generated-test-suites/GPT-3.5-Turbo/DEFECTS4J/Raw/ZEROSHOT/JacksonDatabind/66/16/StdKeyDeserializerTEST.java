package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        Object key = deserializer.deserializeKey("10", null);
        assertEquals(10, key);
    }

    @Test
    public void testDeserializeKeyWithNullValue() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        Object key = deserializer.deserializeKey(null, null);
        assertNull(key);
    }

    @Test
    public void testDeserializeKeyWithInvalidValue() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        Object key = deserializer.deserializeKey("abc", null);
        assertEquals(DeserializationException.class, key.getClass());
    }
}