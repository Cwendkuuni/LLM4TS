package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;

public class AtomicReferenceDeserializerTest {
    @Test
    public void testWithResolved() {
        // Test case for withResolved() method
        AtomicReference<Object> atomicRef = new AtomicReference<>();
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        AtomicReferenceDeserializer newDeserializer = deserializer.withResolved(null, null);
        
        // Assertions
        assertEquals(deserializer.getClass(), newDeserializer.getClass());
    }
    
    @Test
    public void testGetNullValue() {
        // Test case for getNullValue() method
        AtomicReference<Object> atomicRef = new AtomicReference<>();
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        Object nullValue = deserializer.getNullValue(null);
        
        // Assertions
        assertEquals(atomicRef, nullValue);
    }
    
    @Test
    public void testGetEmptyValue() {
        // Test case for getEmptyValue() method
        AtomicReference<Object> atomicRef = new AtomicReference<>();
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        Object emptyValue = deserializer.getEmptyValue(null);
        
        // Assertions
        assertEquals(atomicRef, emptyValue);
    }
    
    @Test
    public void testReferenceValue() {
        // Test case for referenceValue() method
        AtomicReference<Object> atomicRef = new AtomicReference<>();
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        AtomicReference<Object> referenceValue = deserializer.referenceValue(null);
        
        // Assertions
        assertEquals(atomicRef, referenceValue);
    }
    
    @Test
    public void testGetReferenced() {
        // Test case for getReferenced() method
        AtomicReference<Object> atomicRef = new AtomicReference<>();
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        Object referencedValue = deserializer.getReferenced(atomicRef);
        
        // Assertions
        assertNull(referencedValue);
    }
    
    @Test
    public void testUpdateReference() {
        // Test case for updateReference() method
        AtomicReference<Object> atomicRef = new AtomicReference<>();
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        AtomicReference<Object> updatedReference = deserializer.updateReference(atomicRef, null);
        
        // Assertions
        assertEquals(atomicRef, updatedReference);
    }
    
    @Test
    public void testSupportsUpdate() {
        // Test case for supportsUpdate() method
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        Boolean supportsUpdate = deserializer.supportsUpdate(null);
        
        // Assertions
        assertEquals(Boolean.TRUE, supportsUpdate);
    }
}