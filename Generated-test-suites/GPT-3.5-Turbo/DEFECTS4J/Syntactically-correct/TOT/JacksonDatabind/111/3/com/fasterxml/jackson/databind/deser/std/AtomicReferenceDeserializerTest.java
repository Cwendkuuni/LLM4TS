package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;
import org.junit.Test;

public class AtomicReferenceDeserializerTest {

    @Test
    public void testGetNullValue() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer();
        AtomicReference<Object> reference = deserializer.getNullValue(null);
        assertNull(reference.get());
    }

    @Test
    public void testGetEmptyValue() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer();
        AtomicReference<Object> reference = deserializer.getEmptyValue(null);
        assertNull(reference.get());
    }

    @Test
    public void testReferenceValue() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer();
        AtomicReference<Object> reference = deserializer.referenceValue("test");
        assertEquals("test", reference.get());
    }

    @Test
    public void testGetReferenced() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer();
        AtomicReference<Object> reference = new AtomicReference<Object>("test");
        Object result = deserializer.getReferenced(reference);
        assertEquals("test", result);
    }

    @Test
    public void testUpdateReference() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer();
        AtomicReference<Object> reference = new AtomicReference<Object>();
        deserializer.updateReference(reference, "test");
        assertEquals("test", reference.get());
    }

    @Test
    public void testSupportsUpdate() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer();
        Boolean result = deserializer.supportsUpdate(null);
        assertTrue(result);
    }
}