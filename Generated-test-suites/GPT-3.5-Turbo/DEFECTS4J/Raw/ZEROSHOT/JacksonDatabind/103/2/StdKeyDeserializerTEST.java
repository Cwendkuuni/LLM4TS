package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

    private StdKeyDeserializer keyDeserializer;

    public StdKeyDeserializerTest() {
        keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
    }

    @Test
    public void testDeserializeKey() {
        assertEquals(Integer.valueOf(10), keyDeserializer.deserializeKey("10", null));
        assertNull(keyDeserializer.deserializeKey(null, null));
    }

    @Test
    public void testGetKeyClass() {
        assertEquals(Integer.class, keyDeserializer.getKeyClass());
    }

    @Test
    public void testParseInt() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        assertEquals(10, deserializer._parseInt("10"));
    }

    @Test
    public void testParseLong() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LONG, Long.class);
        assertEquals(10000000000L, deserializer._parseLong("10000000000"));
    }

    @Test
    public void testParseDouble() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DOUBLE, Double.class);
        assertEquals(10.5, deserializer._parseDouble("10.5"), 0);
    }

    @Test
    public void testWeirdKey() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        assertEquals("Invalid key: 10", deserializer._weirdKey(null, "10", new Exception("Invalid key: 10")));
    }
    
}