package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class AtomicReferenceDeserializerTest {

    @Test
    public void testAtomicReferenceDeserializerConstructor() {
        JavaType fullType = new JavaType();
        ValueInstantiator inst = new ValueInstantiator();
        TypeDeserializer typeDeser = new TypeDeserializer();
        JsonDeserializer<?> deser = new JsonDeserializer<?>();
        
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(fullType, inst, typeDeser, deser);
        
        assertNotNull(deserializer);
        assertEquals(fullType, deserializer.getFullType());
        assertEquals(inst, deserializer.getValueInstantiator());
        assertEquals(typeDeser, deserializer.getTypeDeserializer());
        assertEquals(deser, deserializer.getValueDeserializer());
    }
    
    @Test
    public void testGetNullValue() throws JsonMappingException {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer();
        DeserializationContext ctxt = new DeserializationContext();
        
        AtomicReference<Object> reference = deserializer.getNullValue(ctxt);
        
        assertNotNull(reference);
        assertNull(reference.get());
    }
    
    @Test
    public void testGetEmptyValue() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer();
        DeserializationContext ctxt = new DeserializationContext();
        
        Object emptyValue = deserializer.getEmptyValue(ctxt);
        
        assertNotNull(emptyValue);
        assertEquals(AtomicReference.class, emptyValue.getClass());
        assertEquals(null, ((AtomicReference<?>) emptyValue).get());
    }
    
    @Test
    public void testReferenceValue() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer();
        Object contents = new Object();
        
        AtomicReference<Object> reference = deserializer.referenceValue(contents);
        
        assertNotNull(reference);
        assertEquals(contents, reference.get());
    }
    
    @Test
    public void testGetReferenced() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer();
        AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
        
        Object referenced = deserializer.getReferenced(reference);
        
        assertNotNull(referenced);
        assertEquals(reference.get(), referenced);
    }
    
    @Test
    public void testUpdateReference() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer();
        AtomicReference<Object> reference = new AtomicReference<Object>();
        Object contents = new Object();
        
        AtomicReference<Object> updatedReference = deserializer.updateReference(reference, contents);
        
        assertNotNull(updatedReference);
        assertEquals(contents, updatedReference.get());
    }
    
    @Test
    public void testSupportsUpdate() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer();
        DeserializationConfig config = new DeserializationConfig();
        
        Boolean supportsUpdate = deserializer.supportsUpdate(config);
        
        assertNotNull(supportsUpdate);
        assertEquals(Boolean.TRUE, supportsUpdate);
    }
}