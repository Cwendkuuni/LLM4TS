package com.fasterxml.jackson.databind.deser.std;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AtomicReferenceDeserializerTest {

    private AtomicReferenceDeserializer atomicReferenceDeserializer;

    @Before
    public void setUp() {
        atomicReferenceDeserializer = new AtomicReferenceDeserializer();
    }

    @Test
    public void testWithResolved() {
        TypeDeserializer typeDeser = new TypeDeserializer();
        JsonDeserializer<?> valueDeser = new JsonDeserializer<?>();
        AtomicReferenceDeserializer result = atomicReferenceDeserializer.withResolved(typeDeser, valueDeser);
        assertNotNull(result);
        assertEquals(atomicReferenceDeserializer._fullType, result._fullType);
        assertEquals(atomicReferenceDeserializer._valueInstantiator, result._valueInstantiator);
        assertEquals(typeDeser, result._typeDeserializer);
        assertEquals(valueDeser, result._valueDeserializer);
    }

    @Test
    public void testGetNullValue() {
        DeserializationContext ctxt = new DeserializationContext();
        AtomicReference<Object> result = atomicReferenceDeserializer.getNullValue(ctxt);
        assertNotNull(result);
    }

    @Test
    public void testGetEmptyValue() {
        DeserializationContext ctxt = new DeserializationContext();
        Object result = atomicReferenceDeserializer.getEmptyValue(ctxt);
        assertNotNull(result);
    }

    @Test
    public void testReferenceValue() {
        Object contents = new Object();
        AtomicReference<Object> result = atomicReferenceDeserializer.referenceValue(contents);
        assertNotNull(result);
        assertEquals(contents, result.get());
    }

    @Test
    public void testGetReferenced() {
        AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
        Object result = atomicReferenceDeserializer.getReferenced(reference);
        assertEquals(reference.get(), result);
    }

    @Test
    public void testUpdateReference() {
        AtomicReference<Object> reference = new AtomicReference<Object>();
        Object contents = new Object();
        AtomicReference<Object> result = atomicReferenceDeserializer.updateReference(reference, contents);
        assertEquals(contents, result.get());
    }

    @Test
    public void testSupportsUpdate() {
        DeserializationConfig config = new DeserializationConfig();
        Boolean result = atomicReferenceDeserializer.supportsUpdate(config);
        assertTrue(result);
    }
}