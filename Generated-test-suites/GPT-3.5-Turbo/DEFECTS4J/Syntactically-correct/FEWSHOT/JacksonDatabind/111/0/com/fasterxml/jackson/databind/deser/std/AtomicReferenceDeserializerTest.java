package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class AtomicReferenceDeserializerTest {
    
    private AtomicReferenceDeserializer atomicReferenceDeserializer;
    
    @Test
    public void testWithResolved() {
        TypeDeserializer typeDeser = null;
        JsonDeserializer<?> valueDeser = null;
        atomicReferenceDeserializer = new AtomicReferenceDeserializer(JavaType.nullType(), null, typeDeser, valueDeser);
        AtomicReferenceDeserializer result = atomicReferenceDeserializer.withResolved(typeDeser, valueDeser);
        assertNotNull(result);
        // Add more assertions here
    }
    
    @Test
    public void testGetNullValue() throws JsonMappingException {
        DeserializationContext ctxt = null;
        atomicReferenceDeserializer = new AtomicReferenceDeserializer(JavaType.nullType(), null, null, null);
        AtomicReference<Object> result = atomicReferenceDeserializer.getNullValue(ctxt);
        assertNotNull(result);
        // Add more assertions here
    }
    
    @Test
    public void testGetEmptyValue() {
        DeserializationContext ctxt = null;
        atomicReferenceDeserializer = new AtomicReferenceDeserializer(JavaType.nullType(), null, null, null);
        Object result = atomicReferenceDeserializer.getEmptyValue(ctxt);
        assertNotNull(result);
        // Add more assertions here
    }
    
    @Test
    public void testReferenceValue() {
        Object contents = null;
        atomicReferenceDeserializer = new AtomicReferenceDeserializer(JavaType.nullType(), null, null, null);
        AtomicReference<Object> result = atomicReferenceDeserializer.referenceValue(contents);
        assertNotNull(result);
        // Add more assertions here
    }
    
    @Test
    public void testGetReferenced() {
        AtomicReference<Object> reference = null;
        atomicReferenceDeserializer = new AtomicReferenceDeserializer(JavaType.nullType(), null, null, null);
        Object result = atomicReferenceDeserializer.getReferenced(reference);
        assertNull(result);
        // Add more assertions here
    }
    
    @Test
    public void testUpdateReference() {
        AtomicReference<Object> reference = null;
        Object contents = null;
        atomicReferenceDeserializer = new AtomicReferenceDeserializer(JavaType.nullType(), null, null, null);
        AtomicReference<Object> result = atomicReferenceDeserializer.updateReference(reference, contents);
        assertNotNull(result);
        // Add more assertions here
    }
    
    @Test
    public void testSupportsUpdate() {
        DeserializationConfig config = null;
        atomicReferenceDeserializer = new AtomicReferenceDeserializer(JavaType.nullType(), null, null, null);
        Boolean result = atomicReferenceDeserializer.supportsUpdate(config);
        assertEquals(Boolean.TRUE, result);
        // Add more assertions here
    }
}