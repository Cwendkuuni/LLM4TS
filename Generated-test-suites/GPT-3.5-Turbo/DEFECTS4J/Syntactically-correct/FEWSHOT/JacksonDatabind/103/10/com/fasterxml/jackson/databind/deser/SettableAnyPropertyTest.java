package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class SettableAnyPropertyTest {
    private SettableAnyProperty anyProperty;
    
    @Before
    public void setUp() {
        anyProperty = new SettableAnyProperty(null, null, null, null, null, null);
    }
    
    @Test
    public void testGetProperty() {
        assertNull(anyProperty.getProperty());
    }
    
    @Test
    public void testHasValueDeserializer() {
        assertFalse(anyProperty.hasValueDeserializer());
    }
    
    @Test
    public void testGetType() {
        assertNull(anyProperty.getType());
    }
    
    @Test
    public void testDeserializeAndSet() {
        // Test for success
        // TODO: Implement the test
        
        // Test for failure
        // TODO: Implement the test
    }
    
    @Test
    public void testDeserialize() {
        // Test for success
        // TODO: Implement the test
        
        // Test for failure
        // TODO: Implement the test
    }
    
    @Test
    public void testSet() {
        // Test for success
        // TODO: Implement the test
        
        // Test for failure
        // TODO: Implement the test
    }
    
    @Test
    public void testThrowAsIOE() {
        // Test for success
        // TODO: Implement the test
        
        // Test for failure
        // TODO: Implement the test
    }
    
    @Test
    public void testToString() {
        assertEquals("[any property on class null]", anyProperty.toString());
    }
}