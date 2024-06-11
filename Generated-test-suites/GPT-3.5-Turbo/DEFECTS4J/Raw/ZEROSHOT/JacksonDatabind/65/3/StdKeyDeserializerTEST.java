package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.databind.DeserializationContext;
import org.junit.Assert;
import org.junit.Test;
import java.io.IOException;

public class StdKeyDeserializerTest {
    
    @Test
    public void testDeserializeKey() throws IOException {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        DeserializationContext ctxt = null;
        
        // Test null key
        Assert.assertNull(keyDeserializer.deserializeKey(null, ctxt));
        
        // Test valid string key
        Assert.assertEquals("testKey", keyDeserializer.deserializeKey("testKey", ctxt));
    }
    
    @Test
    public void testGetKeyClass() {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        
        // Test key class is String
        Assert.assertEquals(String.class, keyDeserializer.getKeyClass());
    }
    
    @Test
    public void testParse() throws Exception {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        DeserializationContext ctxt = null;
        
        // Test parsing string key
        Assert.assertEquals("testKey", keyDeserializer._parse("testKey", ctxt));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseInt() throws Exception {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        DeserializationContext ctxt = null;
        
        // Test parsing int key
        keyDeserializer._parseInt("123abc");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseLong() throws Exception {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_LONG, Long.class);
        DeserializationContext ctxt = null;
        
        // Test parsing long key
        keyDeserializer._parseLong("123abc");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseDouble() throws Exception {
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_DOUBLE, Double.class);
        DeserializationContext ctxt = null;
        
        // Test parsing double key
        keyDeserializer._parseDouble("123abc");
    }
}