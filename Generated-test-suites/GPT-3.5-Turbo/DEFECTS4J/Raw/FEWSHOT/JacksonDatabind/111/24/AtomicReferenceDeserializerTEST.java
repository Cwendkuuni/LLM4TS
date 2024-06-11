package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ReferenceTypeDeserializer;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.concurrent.atomic.AtomicReference;

public class AtomicReferenceDeserializerTest {
    @Test
    public void testWithResolved() {
        JavaType javaType = new JavaType(AtomicReference.class);
        ValueInstantiator valueInstantiator = new ValueInstantiator();
        TypeDeserializer typeDeser = new TypeDeserializer();
        JsonDeserializer<?> valueDeser = new JsonDeserializer<>();
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(javaType, valueInstantiator, typeDeser, valueDeser);
        
        AtomicReferenceDeserializer updatedDeserializer = deserializer.withResolved(typeDeser, valueDeser);
        
        assertNotNull(updatedDeserializer);
        assertEquals(javaType, updatedDeserializer._fullType);
        assertEquals(valueInstantiator, updatedDeserializer._valueInstantiator);
        assertEquals(typeDeser, updatedDeserializer._valueDeserializer);
        assertEquals(valueDeser, updatedDeserializer._valueDeserializer);
    }

    @Test
    public void testGetNullValue() throws JsonMappingException {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(new JavaType(AtomicReference.class), null, null, null);
        
        AtomicReference<Object> nullValue = deserializer.getNullValue(null);
        
        assertNotNull(nullValue);
        assertNull(nullValue.get());
    }

    @Test
    public void testGetEmptyValue() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(new JavaType(AtomicReference.class), null, null, null);
        
        Object emptyValue = deserializer.getEmptyValue(null);
        
        assertNotNull(emptyValue);
        assertEquals(new AtomicReference<Object>(), emptyValue);
    }

    @Test
    public void testReferenceValue() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(new JavaType(AtomicReference.class), null, null, null);
        
        Object contents = new Object();
        
        AtomicReference<Object> reference = deserializer.referenceValue(contents);
        
        assertNotNull(reference);
        assertEquals(contents, reference.get());
    }

    @Test
    public void testGetReferenced() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(new JavaType(AtomicReference.class), null, null, null);
        
        Object contents = new Object();
        
        AtomicReference<Object> reference = new AtomicReference<>(contents);
        
        Object referenced = deserializer.getReferenced(reference);
        
        assertEquals(contents, referenced);
    }

    @Test
    public void testUpdateReference() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(new JavaType(AtomicReference.class), null, null, null);
        
        AtomicReference<Object> reference = new AtomicReference<>();
        Object contents = new Object();
        
        AtomicReference<Object> updatedReference = deserializer.updateReference(reference, contents);
        
        assertNotNull(updatedReference);
        assertEquals(contents, updatedReference.get());
    }

    @Test
    public void testSupportsUpdate() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(new JavaType(AtomicReference.class), null, null, null);
        DeserializationConfig config = new DeserializationConfig();
        
        Boolean supportsUpdate = deserializer.supportsUpdate(config);
        
        assertNotNull(supportsUpdate);
        assertEquals(Boolean.TRUE, supportsUpdate);
    }
}