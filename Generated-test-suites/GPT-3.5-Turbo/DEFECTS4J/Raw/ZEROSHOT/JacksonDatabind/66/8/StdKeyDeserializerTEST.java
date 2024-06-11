package com.fasterxml.jackson.databind.deser.std;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.DeserializationContext;

public class StdKeyDeserializerTest {
    
    private StdKeyDeserializer stdKeyDeserializer;
    
    @Before
    public void setUp() {
        stdKeyDeserializer = new StdKeyDeserializer(1, Boolean.class);
    }
    
    @Test
    public void testDeserializeKey() throws Exception {
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        String key = "true";
        Object result = stdKeyDeserializer.deserializeKey(key, ctxt);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean)result);
    }
    
    @Test
    public void testDeserializeKey_NullKey() throws Exception {
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        Object result = stdKeyDeserializer.deserializeKey(null, ctxt);
        assertNull(result);
    }
    
    @Test
    public void testDeserializeKey_InvalidKey() throws Exception {
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        String key = "test";
        Object result = stdKeyDeserializer.deserializeKey(key, ctxt);
        assertNull(result);
    }
}