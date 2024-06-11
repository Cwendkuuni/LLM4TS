package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import static org.junit.Assert.*;

public class AtomicReferenceDeserializerTest {

    @Test
    public void testWithResolved() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        AtomicReferenceDeserializer resolvedDeserializer = deserializer.withResolved(null, null);
        
        assertNotNull(resolvedDeserializer);
        assertNotSame(deserializer, resolvedDeserializer);
    }
    
    @Test
    public void testGetNullValue() throws Exception {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        Object value = deserializer.getNullValue(null);
        
        assertNotNull(value);
        assertTrue(value instanceof AtomicReference);
    }
    
    @Test
    public void testGetEmptyValue() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        Object value = deserializer.getEmptyValue(null);
        
        assertNotNull(value);
        assertTrue(value instanceof AtomicReference);
    }
    
    @Test
    public void testReferenceValue() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        Object contents = new Object();
        AtomicReference<Object> reference = deserializer.referenceValue(contents);
        
        assertNotNull(reference);
        assertEquals(contents, reference.get());
    }
    
    @Test
    public void testGetReferenced() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        Object contents = new Object();
        AtomicReference<Object> reference = new AtomicReference<Object>(contents);
        Object referenced = deserializer.getReferenced(reference);
        
        assertEquals(contents, referenced);
    }
    
    @Test
    public void testUpdateReference() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        Object contents = new Object();
        AtomicReference<Object> reference = new AtomicReference<Object>();
        AtomicReference<Object> updatedReference = deserializer.updateReference(reference, contents);
        
        assertNotNull(updatedReference);
        assertSame(reference, updatedReference);
        assertEquals(contents, updatedReference.get());
    }
    
    @Test
    public void testSupportsUpdate() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        Boolean supportsUpdate = deserializer.supportsUpdate(null);
        
        assertNotNull(supportsUpdate);
        assertTrue(supportsUpdate);
    }
}