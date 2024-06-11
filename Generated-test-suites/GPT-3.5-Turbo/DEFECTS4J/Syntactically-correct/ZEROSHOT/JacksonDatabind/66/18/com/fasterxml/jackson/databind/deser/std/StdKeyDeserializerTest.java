package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {
    @Test
    public void testDeserializeKey() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        assertEquals("test", deserializer.deserializeKey("test", null));
    }
    
    @Test
    public void testGetKeyClass() {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        assertEquals(String.class, deserializer.getKeyClass());
    }
    
    @Test
    public void testParse() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        assertEquals("test", deserializer._parse("test", null));
    }
    
    @Test
    public void testParseInt() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        assertEquals(123, deserializer._parseInt("123"));
    }
    
    @Test
    public void testParseLong() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LONG, Long.class);
        assertEquals(1234567890L, deserializer._parseLong("1234567890"));
    }
    
    @Test
    public void testParseDouble() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DOUBLE, Double.class);
        assertEquals(3.14, deserializer._parseDouble("3.14"), 0.00001);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testUnknownKeyType() throws Exception {
        StdKeyDeserializer deserializer = new StdKeyDeserializer(17, Object.class);
        deserializer._parse("test", null);
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