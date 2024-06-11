package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;
import org.junit.Test;

public class AsWrapperTypeDeserializerTest {

    @Test
    public void testDeserializeTypedFromObject() {
        // Arrange
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
        // Act
        Object result = deserializer.deserializeTypedFromObject(null, null);
        // Assert
        assertNull(result);
    }

    @Test
    public void testDeserializeTypedFromArray() {
        // Arrange
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
        // Act
        Object result = deserializer.deserializeTypedFromArray(null, null);
        // Assert
        assertNull(result);
    }

    @Test
    public void testDeserializeTypedFromScalar() {
        // Arrange
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
        // Act
        Object result = deserializer.deserializeTypedFromScalar(null, null);
        // Assert
        assertNull(result);
    }

    @Test
    public void testDeserializeTypedFromAny() {
        // Arrange
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
        // Act
        Object result = deserializer.deserializeTypedFromAny(null, null);
        // Assert
        assertNull(result);
    }
}