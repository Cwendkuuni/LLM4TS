package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ReferenceTypeDeserializer;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class AtomicReferenceDeserializerTest {

    @Test
    public void testWithResolved() {
        JavaType fullType = new JavaType();
        ValueInstantiator inst = new ValueInstantiator();
        TypeDeserializer typeDeser = new TypeDeserializer();
        JsonDeserializer<?> deserializer = new JsonDeserializer<?>();
        
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(fullType, inst, typeDeser, deserializer);
        AtomicReferenceDeserializer result = atomicReferenceDeserializer.withResolved(typeDeser, deserializer);
        
        assertNotNull(result);
        assertEquals(fullType, result.getFullType());
        assertEquals(inst, result.getValueInstantiator());
        assertEquals(typeDeser, result.getTypeDeserializer());
        assertEquals(deserializer, result.getValueDeserializer());
    }
    
    @Test
    public void testGetNullValue() throws JsonMappingException {
        DeserializationContext ctxt = new DeserializationContext();
        
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer();
        AtomicReference<Object> result = atomicReferenceDeserializer.getNullValue(ctxt);
        
        assertNotNull(result);
    }
    
    @Test
    public void testGetEmptyValue() {
        DeserializationContext ctxt = new DeserializationContext();
        
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer();
        Object result = atomicReferenceDeserializer.getEmptyValue(ctxt);
        
        assertNotNull(result);
    }
    
    @Test
    public void testReferenceValue() {
        Object contents = new Object();
        
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer();
        AtomicReference<Object> result = atomicReferenceDeserializer.referenceValue(contents);
        
        assertNotNull(result);
        assertEquals(contents, result.get());
    }
    
    @Test
    public void testGetReferenced() {
        Object contents = new Object();
        AtomicReference<Object> reference = new AtomicReference<Object>(contents);
        
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer();
        Object result = atomicReferenceDeserializer.getReferenced(reference);
        
        assertEquals(contents, result);
    }
    
    @Test
    public void testUpdateReference() {
        Object contents = new Object();
        AtomicReference<Object> reference = new AtomicReference<Object>();
        
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer();
        AtomicReference<Object> result = atomicReferenceDeserializer.updateReference(reference, contents);
        
        assertEquals(contents, result.get());
    }
    
    @Test
    public void testSupportsUpdate() {
        DeserializationConfig config = new DeserializationConfig();
        
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer();
        Boolean result = atomicReferenceDeserializer.supportsUpdate(config);
        
        assertEquals(Boolean.TRUE, result);
    }
}