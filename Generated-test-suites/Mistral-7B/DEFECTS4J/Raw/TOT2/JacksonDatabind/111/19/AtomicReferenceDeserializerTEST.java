package com.example.junit.testcases.atomicreference;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.AtomicReferenceDeserializer;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class AtomicReferenceDeserializerTest {
    @Test
    public void testConstructor() {
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);
        assertNotNull(atomicReferenceDeserializer);
    }

    @Test
    public void testGetNullValue() {
        DeserializationContext context = mock(DeserializationContext.class);
        AtomicReference<Object> result = new AtomicReference<Object>();
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);
        assertEquals(result, atomicReferenceDeserializer.getNullValue(context));
    }

    @Test
    public void testGetEmptyValue() {
        AtomicReference<Object> result = new AtomicReference<Object>();
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);
        assertEquals(result, atomicReferenceDeserializer.getEmptyValue());
    }

    @Test
    public void testReferenceValue() throws Exception {
        Object contents = "testValue";
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);
        AtomicReference<Object> result = atomicReferenceDeserializer.referenceValue(contents);
        assertEquals(contents, result.get());
    }

    @Test
    public void testGetReferenced() {
        AtomicReference<Object> reference = new AtomicReference<Object>("testValue");
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);
        Object result = atomicReferenceDeserializer.getReferenced(reference);
        assertEquals("testValue", result);
    }

    @Test
    public void testUpdateReference() {
        AtomicReference<Object> reference = new AtomicReference<Object>("testValue");
        Object newValue = "newValue";
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);
        AtomicReference<Object> result = atomicReferenceDeserializer.updateReference(reference, newValue);
        assertEquals(newValue, result.get());
    }

    @Test
    public void testSupportsUpdate() {
        DeserializationConfig config = mock(DeserializationConfig.class);
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(null, null, null, null);
        assertTrue(atomicReferenceDeserializer.supportsUpdate(config));
    }
}