package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import org.junit.Test;

public class AsPropertyTypeDeserializerTest {

    @Test
    public void testDeserializeTypedFromObject() {
        // Setup
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(null, null, null, false, null);
        // Test
        Object result = deserializer.deserializeTypedFromObject(null, null);
        // Verify
        assertNull(result);
    }

    @Test
    public void testDeserializeTypedForId() {
        // Setup
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(null, null, null, false, null);
        // Test
        Object result = deserializer.deserializeTypedForId(null, null, null);
        // Verify
        assertNull(result);
    }

    @Test
    public void testDeserializeTypedUsingDefaultImpl() {
        // Setup
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(null, null, null, false, null);
        // Test
        Object result = deserializer.deserializeTypedUsingDefaultImpl(null, null, null);
        // Verify
        assertNull(result);
    }

    @Test
    public void testDeserializeTypedFromAny() {
        // Setup
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(null, null, null, false, null);
        // Test
        Object result = deserializer.deserializeTypedFromAny(null, null);
        // Verify
        assertNull(result);
    }

    @Test
    public void testForProperty() {
        // Setup
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(null, null, null, false, null);
        // Test
        TypeDeserializer result = deserializer.forProperty(null);
        // Verify
        assertNotNull(result);
        assertEquals(deserializer, result);
    }

    @Test
    public void testGetTypeInclusion() {
        // Setup
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(null, null, null, false, null);
        // Test
        As result = deserializer.getTypeInclusion();
        // Verify
        assertNotNull(result);
        assertEquals(As.PROPERTY, result);
    }

    @Test
    public void testDeserializeTypedFromArray() {
        // Setup
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(null, null, null, false, null);
        // Test
        Object result = deserializer.deserializeTypedFromArray(null, null);
        // Verify
        assertNull(result);
    }

    @Test
    public void testDeserializeTypedFromScalar() {
        // Setup
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(null, null, null, false, null);
        // Test
        Object result = deserializer.deserializeTypedFromScalar(null, null);
        // Verify
        assertNull(result);
    }

}