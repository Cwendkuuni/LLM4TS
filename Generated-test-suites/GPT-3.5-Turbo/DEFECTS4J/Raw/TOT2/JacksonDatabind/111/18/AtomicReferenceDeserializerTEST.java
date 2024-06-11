package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.AtomicReferenceDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

import java.util.concurrent.atomic.AtomicReference;

public class AtomicReferenceDeserializerTest {

    @Test
    public void testWithResolved() {
        // Create instance of AtomicReferenceDeserializer
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        
        AtomicReferenceDeserializer resolved = deserializer.withResolved(null, null);
        
        assertNotNull(resolved);
    }

    @Test
    public void testGetNullValue() throws JsonMappingException {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        
        AtomicReference<Object> nullValue = deserializer.getNullValue(null);
        
        assertNotNull(nullValue);
    }
    
    @Test
    public void testGetEmptyValue() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        
        Object emptyValue = deserializer.getEmptyValue(null);
        
        assertNotNull(emptyValue);
        assertTrue(emptyValue instanceof AtomicReference);
    }
    
    @Test
    public void testReferenceValue() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        
        Object contents = new Object();
        AtomicReference<Object> reference = deserializer.referenceValue(contents);
        
        assertNotNull(reference);
        assertEquals(reference.get(), contents);
    }
    
    @Test
    public void testGetReferenced() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        
        Object contents = new Object();
        AtomicReference<Object> reference = new AtomicReference<>(contents);
        
        Object referenced = deserializer.getReferenced(reference);
        
        assertEquals(referenced, contents);
    }

    @Test
    public void testUpdateReference() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        
        Object contents = new Object();
        AtomicReference<Object> reference = new AtomicReference<>();
        
        AtomicReference<Object> updatedReference = deserializer.updateReference(reference, contents);
        
        assertEquals(updatedReference.get(), contents);
    }
    
    @Test
    public void testSupportsUpdate() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        
        Boolean supportsUpdate = deserializer.supportsUpdate(null);
        
        assertTrue(supportsUpdate);
    }
}