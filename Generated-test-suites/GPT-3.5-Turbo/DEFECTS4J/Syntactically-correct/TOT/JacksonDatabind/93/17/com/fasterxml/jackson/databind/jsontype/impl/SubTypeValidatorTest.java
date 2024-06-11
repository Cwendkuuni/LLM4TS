package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import org.junit.Test;

public class SubTypeValidatorTest {

    @Test
    public void testValidateSubType_typicalUseCase() {
        // Arrange
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        JavaType type = JavaType.singleton(Type.class);

        // Act and Assert
        assertDoesNotThrow(() -> validator.validateSubType(ctxt, type));
    }

    @Test
    public void testValidateSubType_edgeCases() {
        // Arrange
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        JavaType type = JavaType.singleton(null);

        // Act and Assert
        assertThrows(NullPointerException.class, () -> validator.validateSubType(ctxt, type));
    }

    @Test
    public void testValidateSubType_errorScenarios() {
        // Arrange
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        JavaType type = JavaType.singleton(Error.class);

        // Act and Assert
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(ctxt, type));
    }

    private static class Type { }

    private static class Error extends Type { }
}