package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        assertEquals(Integer.valueOf(10), deserializer.deserializeKey("10", null));
        assertNull(deserializer.deserializeKey(null, null));
    }

    @Test
    public void testGetKeyClass() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        assertEquals(String.class, deserializer.getKeyClass());
    }

    @Test
    public void testParse() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
        assertEquals(Boolean.TRUE, deserializer._parse("true", null));
        assertEquals(Boolean.FALSE, deserializer._parse("false", null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidInt() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        deserializer._parse("abc", null);
    }

    @Test
    public void testParseInt() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        assertEquals(Integer.valueOf(10), deserializer._parse("10", null));
    }

    @Test
    public void testParseLong() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LONG, Long.class);
        assertEquals(Long.valueOf(9876543210L), deserializer._parse("9876543210", null));
    }

    @Test
    public void testParseDouble() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DOUBLE, Double.class);
        assertEquals(Double.valueOf(3.14), deserializer._parse("3.14", null));
    }
  
    @Test
    public void testStringCtorKeyDeserializer() throws Exception {
        StdKeyDeserializer.StringCtorKeyDeserializer deserializer = new StdKeyDeserializer.StringCtorKeyDeserializer(String.class.getConstructor(String.class));
        assertEquals("test", deserializer._parse("test", null));
    }

    @Test
    public void testStringFactoryKeyDeserializer() throws Exception {
        StdKeyDeserializer.StringFactoryKeyDeserializer deserializer = new StdKeyDeserializer.StringFactoryKeyDeserializer(String.class.getMethod("valueOf", String.class));
        assertEquals("test", deserializer._parse("test", null));
    }
}