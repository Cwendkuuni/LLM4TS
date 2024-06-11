package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;

public class AtomicReferenceDeserializerTest {
    
    @Test
    public void testConstructor() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(JavaType.class, ValueInstantiator, TypeDeserializer, JsonDeserializer);
        
        assertNotNull(deserializer);
    }
    
    @Test
    public void testWithResolved() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(JavaType.class, ValueInstantiator, TypeDeserializer, JsonDeserializer);
        AtomicReferenceDeserializer newDeserializer = deserializer.withResolved(TypeDeserializer.class, JsonDeserializer.class);
        
        assertNotNull(newDeserializer);
        assertEquals(deserializer._fullType, newDeserializer._fullType);
        assertEquals(deserializer._valueInstantiator, newDeserializer._valueInstantiator);
        assertEquals(deserializer._valueDeserializer, newDeserializer._valueDeserializer);
        assertEquals(TypeDeserializer.class, newDeserializer._idResolver);
    }
    
    @Test
    public void testGetNullValue() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(JavaType.class, ValueInstantiator, TypeDeserializer, JsonDeserializer);
        AtomicReference<Object> reference = deserializer.getNullValue(DeserializationContext);
        
        assertNotNull(reference);
        assertNull(reference.get());
    }
    
    @Test
    public void testGetEmptyValue() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(JavaType.class, ValueInstantiator, TypeDeserializer, JsonDeserializer);
        Object emptyValue = deserializer.getEmptyValue(DeserializationContext);
        
        assertNotNull(emptyValue);
        assertEquals(AtomicReference.class, emptyValue.getClass());
        assertNull(((AtomicReference<Object>) emptyValue).get());
    }
    
    @Test
    public void testReferenceValue() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(JavaType.class, ValueInstantiator, TypeDeserializer, JsonDeserializer);
        AtomicReference<Object> reference = deserializer.referenceValue(Object);
        
        assertNotNull(reference);
        assertEquals(Object, reference.get());
    }
    
    @Test
    public void testGetReferenced() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(JavaType.class, ValueInstantiator, TypeDeserializer, JsonDeserializer);
        AtomicReference<Object> reference = new AtomicReference<Object>(Object);
        Object referenced = deserializer.getReferenced(reference);
        
        assertNotNull(referenced);
        assertEquals(Object, referenced);
    }
    
    @Test
    public void testUpdateReference() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(JavaType.class, ValueInstantiator, TypeDeserializer, JsonDeserializer);
        AtomicReference<Object> reference = new AtomicReference<Object>();
        AtomicReference<Object> updatedReference = deserializer.updateReference(reference, Object);
        
        assertNotNull(updatedReference);
        assertEquals(Object, updatedReference.get());
    }
    
    @Test
    public void testSupportsUpdate() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(JavaType.class, ValueInstantiator, TypeDeserializer, JsonDeserializer);
        Boolean supportsUpdate = deserializer.supportsUpdate(DeserializationConfig);
        
        assertNotNull(supportsUpdate);
        assertTrue(supportsUpdate);
    }
}