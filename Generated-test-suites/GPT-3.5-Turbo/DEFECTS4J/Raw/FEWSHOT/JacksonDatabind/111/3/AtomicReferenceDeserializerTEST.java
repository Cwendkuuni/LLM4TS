package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;

public class AtomicReferenceDeserializerTest {
    
    @Test
    public void testWithResolved() {
        AtomicReferenceDeserializer deser = new AtomicReferenceDeserializer(null, null, null, null);
        AtomicReferenceDeserializer newDeser = deser.withResolved(null, null);
        assertEquals(deser, newDeser);
    }
    
    @Test
    public void testGetNullValue() {
        AtomicReferenceDeserializer deser = new AtomicReferenceDeserializer(null, null, null, null);
        AtomicReference<Object> result = deser.getNullValue(null);
        assertNull(result.get());
    }
    
    @Test
    public void testGetEmptyValue() {
        AtomicReferenceDeserializer deser = new AtomicReferenceDeserializer(null, null, null, null);
        AtomicReference<Object> result = deser.getEmptyValue(null);
        assertNull(result.get());
    }
    
    @Test
    public void testReferenceValue() {
        AtomicReferenceDeserializer deser = new AtomicReferenceDeserializer(null, null, null, null);
        Object contents = new Object();
        AtomicReference<Object> result = deser.referenceValue(contents);
        assertEquals(contents, result.get());
    }
    
    @Test
    public void testGetReferenced() {
        AtomicReferenceDeserializer deser = new AtomicReferenceDeserializer(null, null, null, null);
        Object contents = new Object();
        AtomicReference<Object> reference = new AtomicReference<>(contents);
        Object result = deser.getReferenced(reference);
        assertEquals(contents, result);
    }
    
    @Test
    public void testUpdateReference() {
        AtomicReferenceDeserializer deser = new AtomicReferenceDeserializer(null, null, null, null);
        Object contents = new Object();
        AtomicReference<Object> reference = new AtomicReference<>();
        AtomicReference<Object> result = deser.updateReference(reference, contents);
        assertEquals(contents, result.get());
    }
    
    @Test
    public void testSupportsUpdate() {
        AtomicReferenceDeserializer deser = new AtomicReferenceDeserializer(null, null, null, null);
        Boolean result = deser.supportsUpdate(null);
        assertEquals(Boolean.TRUE, result);
    }
}