package com.example.test; // Replace with your package name

import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.*;

public class AtomicReferenceDeserializerTest {

    @Test
    public void testConstructor() {
        JavaType fullType = null;
        ValueInstantiator inst = null;
        TypeDeserializer typeDeser = null;
        JsonDeserializer<?> deser = null;

        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(fullType, inst, typeDeser, deser);

        assertNotNull(atomicReferenceDeserializer);
        assertEquals(fullType, atomicReferenceDeserializer.getFullType());
        assertEquals(inst, atomicReferenceDeserializer.getValueInstantiator());
        assertEquals(typeDeser, atomicReferenceDeserializer.getTypeDeserializer());
        assertEquals(deser, atomicReferenceDeserializer.getDeserializer());
    }

    @Test
    public void testWithResolved() {
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);
        TypeDeserializer typeDeser = new TypeDeserializer() {};
        JsonDeserializer<?> valueDeser = new JsonDeserializer<Object>() {};

        AtomicReferenceDeserializer newInstance = atomicReferenceDeserializer.withResolved(typeDeser, valueDeser);

        assertNotSame(atomicReferenceDeserializer, newInstance);
    }

    @Test
    public void testGetNullValue() {
        DeserializationContext ctxt = null;

        AtomicReference<Object> result = new AtomicReferenceDeserializer(null, null, null, null).getNullValue(ctxt);

        assertEquals(result, new AtomicReference<Object>());
    }

    @Test
    public void testGetEmptyValue() {
        DeserializationContext ctxt = null;

        AtomicReference<Object> result = new AtomicReferenceDeserializer(null, null, null, null).getEmptyValue(ctxt);

        assertEquals(result, new AtomicReference<Object>());
    }

    @Test
    public void testReferenceValue() {
        Object contents = new Object();
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);

        AtomicReference<Object> result = atomicReferenceDeserializer.referenceValue(contents);

        assertNotNull(result);
        assertEquals(contents, result.get());
    }

    @Test
    public void testGetReferenced() {
        AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);

        Object result = atomicReferenceDeserializer.getReferenced(reference);

        assertEquals(reference.get(), result);
    }

    @Test
    public void testUpdateReference() {
        AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
        Object newValue = new Object();
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);

        AtomicReference<Object> updatedReference = atomicReferenceDeserializer.updateReference(reference, newValue);

        assertEquals(reference, updatedReference);
        assertEquals(newValue, updatedReference.get());
    }

    @Test(expected = RuntimeException.class)
    public void testSupportsUpdateThrowsException() {
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);

        atomicReferenceDeserializer.supportsUpdate(null);
    }

    @Test
    public void testSupportsUpdateReturnsTrue() {
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);

        assertTrue(atomicReferenceDeserializer.supportsUpdate(null));
    }
}