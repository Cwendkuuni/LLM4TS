package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class AtomicReferenceDeserializerTest {
    
    // Test getNullValue() method
    @Test
    public void testGetNullValue() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer();
        AtomicReference<Object> reference = deserializer.getNullValue(null);
        
        assertNotNull(reference);
    }
    
    // Test getEmptyValue() method
    @Test
    public void testGetEmptyValue() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer();
        Object emptyValue = deserializer.getEmptyValue(null);
        
        assertNotNull(emptyValue);
    }
    
    // Test referenceValue() method
    @Test
    public void testReferenceValue() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer();
        Object contents = new Object();
        AtomicReference<Object> reference = deserializer.referenceValue(contents);
        
        assertNotNull(reference);
        assertEquals(contents, reference.get());
    }
    
    // Test getReferenced() method
    @Test
    public void testGetReferenced() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer();
        Object contents = new Object();
        AtomicReference<Object> reference = new AtomicReference<>(contents);
        
        Object referenced = deserializer.getReferenced(reference);
        
        assertEquals(contents, referenced);
    }
    
    // Test updateReference() method
    @Test
    public void testUpdateReference() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer();
        Object contents = new Object();
        AtomicReference<Object> reference = new AtomicReference<>();
        
        AtomicReference<Object> updatedReference = deserializer.updateReference(reference, contents);
        
        assertNotNull(updatedReference);
        assertEquals(contents, updatedReference.get());
    }
    
    // Test supportsUpdate() method
    @Test
    public void testSupportsUpdate() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer();
        Boolean supportsUpdate = deserializer.supportsUpdate(null);
        
        assertTrue(supportsUpdate);
    }
    
}