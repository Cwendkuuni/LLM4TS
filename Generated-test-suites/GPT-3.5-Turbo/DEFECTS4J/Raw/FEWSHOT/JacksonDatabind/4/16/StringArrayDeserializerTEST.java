package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class StringArrayDeserializerTest {
    private StringArrayDeserializer deserializer = new StringArrayDeserializer();
    
    @Test
    public void testDeserialize() {
        assertNull(deserializer.deserialize(null, null));
    }
    
    @Test
    public void testDeserializeWithType() {
        assertNull(deserializer.deserializeWithType(null, null, null));
    }
    
    @Test
    public void test_handleNonArray() {
        assertNull(deserializer.handleNonArray(null, null));
    }
    
    @Test
    public void test_createContextual() {
        assertNull(deserializer.createContextual(null, null));
    }
}