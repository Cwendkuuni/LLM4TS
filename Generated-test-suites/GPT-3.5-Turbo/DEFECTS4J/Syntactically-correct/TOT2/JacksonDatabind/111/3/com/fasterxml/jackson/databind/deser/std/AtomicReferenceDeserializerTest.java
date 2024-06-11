package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;

public class AtomicReferenceDeserializerTest {

    @Test
    public void testWithResolved() {
        // Test withResolved method in AtomicReferenceDeserializer
        // Create relevant objects
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        DeserializationContext ctxt = null;

        // Test the method
        AtomicReferenceDeserializer resolvedDeserializer = deserializer.withResolved(null, null);

        // Assert the result
        assertEquals(deserializer.getFullType(), resolvedDeserializer.getFullType());
        assertEquals(deserializer.getValueInstantiator(), resolvedDeserializer.getValueInstantiator());
    }

    @Test
    public void testGetNullValue() throws JsonMappingException {
        // Test getNullValue method in AtomicReferenceDeserializer
        // Create relevant objects
        DeserializationContext ctxt = null;
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);

        // Test the method
        AtomicReference<Object> nullValue = deserializer.getNullValue(ctxt);

        // Assert the result
        assertNull(nullValue.get());
    }

    @Test
    public void testGetEmptyValue() {
        // Test getEmptyValue method in AtomicReferenceDeserializer
        // Create relevant objects
        DeserializationContext ctxt = null;
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);

        // Test the method
        Object emptyValue = deserializer.getEmptyValue(ctxt);

        // Assert the result
        assertTrue(emptyValue instanceof AtomicReference);
        assertNull(((AtomicReference<?>) emptyValue).get());
    }

    @Test
    public void testReferenceValue() {
        // Test referenceValue method in AtomicReferenceDeserializer
        // Create relevant objects
        Object contents = "TestContent";
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);

        // Test the method
        AtomicReference<Object> reference = deserializer.referenceValue(contents);

        // Assert the result
        assertEquals(contents, reference.get());
    }

    @Test
    public void testGetReferenced() {
        // Test getReferenced method in AtomicReferenceDeserializer
        // Create relevant objects
        AtomicReference<Object> reference = new AtomicReference<>("TestContent");
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);

        // Test the method
        Object referenced = deserializer.getReferenced(reference);

        // Assert the result
        assertEquals(reference.get(), referenced);
    }

    @Test
    public void testUpdateReference() {
        // Test updateReference method in AtomicReferenceDeserializer
        // Create relevant objects
        AtomicReference<Object> reference = new AtomicReference<>("TestContent");
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
        Object newContents = "NewContent";

        // Test the method
        AtomicReference<Object> updatedReference = deserializer.updateReference(reference, newContents);

        // Assert the result
        assertEquals(newContents, updatedReference.get());
    }

    @Test
    public void testSupportsUpdate() {
        // Test supportsUpdate method in AtomicReferenceDeserializer
        // Create relevant objects
        DeserializationConfig config = null;
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);

        // Test the method
        Boolean supportsUpdate = deserializer.supportsUpdate(config);

        // Assert the result
        assertTrue(supportsUpdate);
    }
}