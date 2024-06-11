package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

import org.junit.Assert;
import org.junit.Test;

public class SubTypeValidatorTest {

    @Test
    public void testValidateSubType_ValidSubType() {
        // Arrange
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = new DeserializationContext(null, null);
        JavaType type = null; // Initialize with a valid sub-type JavaType object

        // Act
        validator.validateSubType(ctxt, type);

        // Assert (no exception should be thrown)
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_InvalidSubType() {
        // Arrange
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = new DeserializationContext(null, null);
        JavaType type = null; // Initialize with an invalid sub-type JavaType object

        // Act
        validator.validateSubType(ctxt, type);

        // Assert (expecting a JsonMappingException to be thrown)
    }
}