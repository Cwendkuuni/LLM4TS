package com.fasterxml.jackson.databind.deser.std;

import java.util.concurrent.atomic.AtomicReference;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class AtomicReferenceDeserializerTest {

    @Test
    public void testWithResolved() {
        JavaType fullType = new JavaType(AtomicReference.class);
        ValueInstantiator inst = new ValueInstantiator();
        TypeDeserializer typeDeser = new TypeDeserializer();
        JsonDeserializer<?> valueDeser = new JsonDeserializer<>();
        
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(fullType, inst, typeDeser, valueDeser);

        deserializer.withResolved(typeDeser, valueDeser);
        assertNotNull(deserializer);
    }

    @Test
    public void testGetNullValue() {
        DeserializationContext ctxt = new DeserializationContext();

        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer();

        AtomicReference<Object> reference = deserializer.getNullValue(ctxt);
        assertNotNull(reference);
    }

    @Test
    public void testGetEmptyValue() {
        DeserializationContext ctxt = new DeserializationContext();

        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer();

        Object value = deserializer.getEmptyValue(ctxt);
        assertNotNull(value);
    }
    
    @Test
    public void testReferenceValue() {
        Object contents = new Object();

        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer();

        AtomicReference<Object> reference = deserializer.referenceValue(contents);
        assertNotNull(reference);
        assertEquals(contents, reference.get());
    }

    @Test
    public void testGetReferenced() {
        Object contents = new Object();
        AtomicReference<Object> reference = new AtomicReference<>(contents);

        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer();

        Object referenced = deserializer.getReferenced(reference);
        assertEquals(contents, referenced);
    }

    @Test
    public void testUpdateReference() {
        AtomicReference<Object> reference = new AtomicReference<>();
        Object contents = new Object();

        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer();

        AtomicReference<Object> updatedReference = deserializer.updateReference(reference, contents);
        assertNotNull(updatedReference);
        assertEquals(contents, updatedReference.get());
    }

    @Test
    public void testSupportsUpdate() {
        DeserializationConfig config = new DeserializationConfig();

        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer();

        Boolean supportsUpdate = deserializer.supportsUpdate(config);
        assertNotNull(supportsUpdate);
        assertEquals(Boolean.TRUE, supportsUpdate);
    }
}