package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class AtomicReferenceDeserializerTest {

    @Test
    public void testWithResolved() {
        JavaType fullType = null;
        ValueInstantiator inst = null;
        TypeDeserializer typeDeser = null;
        JsonDeserializer<?> valueDeser = null;
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(fullType, inst, typeDeser, valueDeser);
        
        AtomicReferenceDeserializer updated = deserializer.withResolved(typeDeser, valueDeser);
        
        assertNotNull(updated);
        assertEquals(fullType, updated._fullType);
        assertEquals(inst, updated._valueInstantiator);
        assertEquals(typeDeser, updated._valueTypeDeserializer);
        assertEquals(valueDeser, updated._valueDeserializer);
    }
    
    @Test
    public void testGetNullValue() throws JsonMappingException {
        DeserializationContext ctxt = null;
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        
        AtomicReference<Object> nullValue = deserializer.getNullValue(ctxt);
        
        assertNotNull(nullValue);
    }
    
    @Test
    public void testGetEmptyValue() {
        DeserializationContext ctxt = null;
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        
        Object emptyValue = deserializer.getEmptyValue(ctxt);
        
        assertNotNull(emptyValue);
    }
    
    @Test
    public void testReferenceValue() {
        Object contents = new Object();
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        
        AtomicReference<Object> referenceValue = deserializer.referenceValue(contents);
        
        assertNotNull(referenceValue);
        assertEquals(contents, referenceValue.get());
    }
    
    @Test
    public void testGetReferenced() {
        AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        
        Object referenced = deserializer.getReferenced(reference);
        
        assertNotNull(referenced);
        assertEquals(reference.get(), referenced);
    }
    
    @Test
    public void testUpdateReference() {
        AtomicReference<Object> reference = new AtomicReference<Object>();
        Object contents = new Object();
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        
        AtomicReference<Object> updatedReference = deserializer.updateReference(reference, contents);
        
        assertNotNull(updatedReference);
        assertEquals(contents, updatedReference.get());
    }
    
    @Test
    public void testSupportsUpdate() {
        DeserializationConfig config = null;
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        
        Boolean supportsUpdate = deserializer.supportsUpdate(config);
        
        assertTrue(supportsUpdate);
    }
}