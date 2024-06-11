package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.Test;
import static org.junit.Assert.*;

public class SubTypeValidatorTest {

    @Test
    public void testValidateSubType() throws JsonMappingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = null; // Mock DeserializationContext object
        JavaType type = null; // Mock JavaType object

        // Typical use case
        validator.validateSubType(ctxt, type);

        // Edge case: type is null
        try {
            validator.validateSubType(ctxt, null);
            fail("Expected JsonMappingException to be thrown");
        } catch (JsonMappingException e) {
            assertEquals("Illegal type (null) to deserialize: prevented for security reasons", e.getMessage());
        }
    }
}