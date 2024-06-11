package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.atomic.AtomicReference;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

import org.junit.Test;

public class AtomicReferenceDeserializerTest {

    @Test
    public void testConstructor() {
        JavaType fullType = new JavaType();
        ValueInstantiator inst = new ValueInstantiator();
        TypeDeserializer typeDeser = new TypeDeserializer();
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(fullType, inst, typeDeser, deser);
        assertNotNull(deserializer);
    }

    @Test
    public void testWithResolved() {
        TypeDeserializer typeDeser = new TypeDeserializer();
        JsonDeserializer<?> valueDeser = new JsonDeserializer<>();
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(new JavaType(), new ValueInstantiator(), typeDeser, valueDeser);
        AtomicReferenceDeserializer resolved = deserializer.withResolved(new TypeDeserializer(), new JsonDeserializer<>());
        assertNotNull(resolved);
    }

    @Test
    public void testGetNullValue() throws JsonMappingException {
        DeserializationContext ctxt = new DeserializationContext();
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(new JavaType(), new ValueInstantiator(), new TypeDeserializer(), new JsonDeserializer<>());
        AtomicReference<Object> nullValue = deserializer.getNullValue(ctxt);
        assertNotNull(nullValue);
    }

    @Test
    public void testGetEmptyValue() {
        DeserializationContext ctxt = new DeserializationContext();
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(new JavaType(), new ValueInstantiator(), new TypeDeserializer(), new JsonDeserializer<>());
        Object emptyValue = deserializer.getEmptyValue(ctxt);
        assertNotNull(emptyValue);
    }

    @Test
    public void testReferenceValue() {
        Object contents = new Object();
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(new JavaType(), new ValueInstantiator(), new TypeDeserializer(), new JsonDeserializer<>());
        AtomicReference<Object> referenceValue = deserializer.referenceValue(contents);
        assertNotNull(referenceValue);
        assertEquals(contents, referenceValue.get());
    }

    @Test
    public void testGetReferenced() {
        AtomicReference<Object> reference = new AtomicReference<>();
        Object contents = new Object();
        reference.set(contents);
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(new JavaType(), new ValueInstantiator(), new TypeDeserializer(), new JsonDeserializer<>());
        Object referenced = deserializer.getReferenced(reference);
        assertNotNull(referenced);
        assertEquals(contents, referenced);
    }

    @Test
    public void testUpdateReference() {
        AtomicReference<Object> reference = new AtomicReference<>();
        Object contents = new Object();
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(new JavaType(), new ValueInstantiator(), new TypeDeserializer(), new JsonDeserializer<>());
        AtomicReference<Object> updated = deserializer.updateReference(reference, contents);
        assertNotNull(updated);
        assertEquals(contents, updated.get());
    }

    @Test
    public void testSupportsUpdate() {
        DeserializationConfig config = new DeserializationConfig();
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(new JavaType(), new ValueInstantiator(), new TypeDeserializer(), new JsonDeserializer<>());
        Boolean supportsUpdate = deserializer.supportsUpdate(config);
        assertTrue(supportsUpdate);
    }
}