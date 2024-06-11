package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

public class SubTypeValidatorTest {

    @Test
    public void testValidateSubType() {
        SubTypeValidator validator = SubTypeValidator.instance();

        // Test valid type
        DeserializationContext ctxt = null; // Provide a valid DeserializationContext object
        JavaType type = JavaType.fromClass(ValidClass.class); // Provide a valid class
        try {
            validator.validateSubType(ctxt, type);
        } catch (JsonMappingException e) {
            fail("Validation failed for a valid type");
        }

        // Test illegal type
        try {
            validator.validateSubType(ctxt, JavaType.fromClass(IllegalClass.class)); // Provide an illegal class
            fail("Validation passed for an illegal type");
        } catch (JsonMappingException e) {
            assertEquals("Illegal type (com.fasterxml.jackson.databind.jsontype.impl.IllegalClass) to deserialize: prevented for security reasons", e.getMessage());
        }
    }

    private static class ValidClass {
        // Define a valid class
    }

    private static class IllegalClass {
        // Define an illegal class
    }
}