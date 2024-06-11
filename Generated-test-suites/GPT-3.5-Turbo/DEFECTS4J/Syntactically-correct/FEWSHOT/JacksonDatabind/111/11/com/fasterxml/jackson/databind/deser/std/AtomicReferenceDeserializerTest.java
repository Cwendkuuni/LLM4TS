package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
    public void testConstructor() {
        JavaType fullType = null;
        ValueInstantiator inst = null;
        TypeDeserializer typeDeser = null;
        JsonDeserializer<?> deser = null;
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(fullType, inst, typeDeser, deser);
        
        assertEquals(fullType, deserializer._fullType);
        assertEquals(inst, deserializer._valueInstantiator);
        assertEquals(typeDeser, deserializer._valueTypeDeserializer);
        assertEquals(deser, deserializer._valueDeserializer);
    }
    
    @Test
    public void testWithResolved() {
        TypeDeserializer typeDeser = null;
        JsonDeserializer<?> valueDeser = null;
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        AtomicReferenceDeserializer newDeserializer = deserializer.withResolved(typeDeser, valueDeser);
        
        assertEquals(deserializer._fullType, newDeserializer._fullType);
        assertEquals(deserializer._valueInstantiator, newDeserializer._valueInstantiator);
        assertEquals(typeDeser, newDeserializer._valueTypeDeserializer);
        assertEquals(valueDeser, newDeserializer._valueDeserializer);
    }
    
    @Test
    public void testGetNullValue() throws JsonMappingException {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        DeserializationContext ctxt = null;
        AtomicReference<Object> nullValue = deserializer.getNullValue(ctxt);
        
        assertTrue(nullValue instanceof AtomicReference);
    }
    
    @Test
    public void testGetEmptyValue() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        DeserializationContext ctxt = null;
        Object emptyValue = deserializer.getEmptyValue(ctxt);
        
        assertTrue(emptyValue instanceof AtomicReference);
    }
    
    @Test
    public void testReferenceValue() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        Object contents = new Object();
        AtomicReference<Object> referenceValue = deserializer.referenceValue(contents);
        
        assertEquals(contents, referenceValue.get());
    }
    
    @Test
    public void testGetReferenced() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
        Object referenced = deserializer.getReferenced(reference);
        
        assertEquals(reference.get(), referenced);
    }
    
    @Test
    public void testUpdateReference() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        AtomicReference<Object> reference = new AtomicReference<Object>(null);
        Object contents = new Object();
        
        AtomicReference<Object> updatedReference = deserializer.updateReference(reference, contents);
        
        assertEquals(contents, updatedReference.get());
    }
    
    @Test
    public void testSupportsUpdate() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        DeserializationConfig config = null;
        
        assertTrue(deserializer.supportsUpdate(config));
    }
}