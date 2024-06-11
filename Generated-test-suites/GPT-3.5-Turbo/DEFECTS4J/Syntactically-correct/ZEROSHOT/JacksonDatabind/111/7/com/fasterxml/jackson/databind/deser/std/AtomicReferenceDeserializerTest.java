package com.fasterxml.jackson.databind.deser.std;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.*;

public class AtomicReferenceDeserializerTest {

    private AtomicReferenceDeserializer deserializer;
    private AtomicReference<Object> reference;

    @Before
    public void setUp() {
        deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        reference = new AtomicReference<>();
    }

    @Test
    public void testWithResolved() {
        AtomicReferenceDeserializer updatedDeserializer = deserializer.withResolved(null, null);
        assertNotNull(updatedDeserializer);
    }

    @Test
    public void testGetNullValue() {
        AtomicReference<Object> nullValue = deserializer.getNullValue(null);
        assertNotNull(nullValue);
    }

    @Test
    public void testGetEmptyValue() {
        Object emptyValue = deserializer.getEmptyValue(null);
        assertNotNull(emptyValue);
    }

    @Test
    public void testReferenceValue() {
        Object contents = new Object();
        AtomicReference<Object> referenceValue = deserializer.referenceValue(contents);
        assertNotNull(referenceValue);
        assertEquals(contents, referenceValue.get());
    }

    @Test
    public void testGetReferenced() {
        Object contents = new Object();
        reference.set(contents);
        Object referencedValue = deserializer.getReferenced(reference);
        assertEquals(contents, referencedValue);
    }

    @Test
    public void testUpdateReference() {
        Object contents = new Object();
        AtomicReference<Object> updatedReference = deserializer.updateReference(reference, contents);
        assertEquals(contents, updatedReference.get());
    }

    @Test
    public void testSupportsUpdate() {
        Boolean supportsUpdate = deserializer.supportsUpdate(null);
        assertTrue(supportsUpdate);
    }
}