package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class AtomicReferenceDeserializerTest {

    @Test
    public void testWithResolved() {
        // Test withResolved method
        // Create test objects
        AtomicReferenceDeserializer deser = new AtomicReferenceDeserializer(null, null, null, null);
        TypeDeserializer typeDeser = new TypeDeserializer(null, null, null);
        JsonDeserializer<?> valueDeser = new JsonDeserializer<>(null, null);

        // Call withResolved method
        AtomicReferenceDeserializer result = deser.withResolved(typeDeser, valueDeser);

        // Assert result is not null
        assertNotNull(result);
    }

    @Test
    public void testGetNullValue() {
        // Test getNullValue method
        // Create test objects
        AtomicReferenceDeserializer deser = new AtomicReferenceDeserializer(null, null, null, null);

        // Call getNullValue method
        AtomicReference<Object> result = deser.getNullValue(null);

        // Assert result is not null
        assertNotNull(result);
    }

    @Test
    public void testGetEmptyValue() {
        // Test getEmptyValue method
        // Create test objects
        AtomicReferenceDeserializer deser = new AtomicReferenceDeserializer(null, null, null, null);

        // Call getEmptyValue method
        Object result = deser.getEmptyValue(null);

        // Assert result is not null
        assertNotNull(result);
    }

    @Test
    public void testReferenceValue() {
        // Test referenceValue method
        // Create test object
        Object contents = new Object();
        AtomicReferenceDeserializer deser = new AtomicReferenceDeserializer(null, null, null, null);

        // Call referenceValue method
        AtomicReference<Object> result = deser.referenceValue(contents);

        // Assert result contains contents
        assertEquals(contents, result.get());
    }

    @Test
    public void testGetReferenced() {
        // Test getReferenced method
        // Create test object
        Object contents = new Object();
        AtomicReference<Object> reference = new AtomicReference<>(contents);
        AtomicReferenceDeserializer deser = new AtomicReferenceDeserializer(null, null, null, null);

        // Call getReferenced method
        Object result = deser.getReferenced(reference);

        // Assert result is contents
        assertEquals(contents, result);
    }

    @Test
    public void testUpdateReference() {
        // Test updateReference method
        // Create test objects
        Object contents = new Object();
        AtomicReference<Object> reference = new AtomicReference<>(null);
        AtomicReferenceDeserializer deser = new AtomicReferenceDeserializer(null, null, null, null);

        // Call updateReference method
        AtomicReference<Object> result = deser.updateReference(reference, contents);

        // Assert result contains contents
        assertEquals(contents, result.get());
    }

    @Test
    public void testSupportsUpdate() {
        // Test supportsUpdate method
        // Create test object
        DeserializationConfig config = new DeserializationConfig(null);
        AtomicReferenceDeserializer deser = new AtomicReferenceDeserializer(null, null, null, null);

        // Call supportsUpdate method
        Boolean result = deser.supportsUpdate(config);

        // Assert result is true
        assertEquals(Boolean.TRUE, result);
    }

}