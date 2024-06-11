package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.AtomicReferenceDeserializer;

public class AtomicReferenceDeserializerTest {

    @Test
    public void testConstructor() {
        JavaType fullType = JavaType.construct(Type.class);
        ValueInstantiator inst = new ValueInstantiator();
        TypeDeserializer typeDeser = new TypeDeserializer();
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(fullType, inst, typeDeser, deser);
        
        assertNotNull(deserializer);
    }

    @Test
    public void testWithResolved() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer();

        TypeDeserializer typeDeser = new TypeDeserializer();
        JsonDeserializer<?> valueDeser = new JsonDeserializer<>();
        AtomicReferenceDeserializer resolved = deserializer.withResolved(typeDeser, valueDeser);

        assertNotNull(resolved);
    }

    @Test
    public void testGetNullValue() {
        DeserializationContext ctxt = new DeserializationContext();

        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer();
        AtomicReference<Object> nullValue = deserializer.getNullValue(ctxt);

        assertNotNull(nullValue);
        assertNull(nullValue.get());
    }

    @Test
    public void testGetEmptyValue() {
        DeserializationContext ctxt = new DeserializationContext();

        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer();
        AtomicReference<Object> emptyValue = deserializer.getEmptyValue(ctxt);

        assertNotNull(emptyValue);
        assertNull(emptyValue.get());
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
        Object contents = new Object();
        AtomicReference<Object> reference = new AtomicReference<>(contents);

        Object result = deserializer.getReferenced(reference);

        assertNotNull(result);
        assertEquals(contents, result);
    }

    @Test
    public void testUpdateReference() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer();
        AtomicReference<Object> reference = new AtomicReference<>();

        Object contents = new Object();
        AtomicReference<Object> updatedReference = deserializer.updateReference(reference, contents);

        assertNotNull(updatedReference);
        assertEquals(contents, updatedReference.get());
    }

    @Test
    public void testSupportsUpdate() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer();
        DeserializationConfig config = new DeserializationConfig();

        Boolean result = deserializer.supportsUpdate(config);

        assertNotNull(result);
        assertTrue(result);
    }
}