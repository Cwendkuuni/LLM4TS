package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;
import org.junit.Test;

public class SubTypeValidatorTest {

    @Test
    public void testValidateSubType() {
        SubTypeValidator validator = SubTypeValidator.instance();

        // Typical use case
        try {
            validator.validateSubType(null, JavaType.class);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected exception
        }

        // Edge cases
        try {
            validator.validateSubType(null, Void.TYPE);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected exception
        }

        try {
            validator.validateSubType(null, SubTypeValidator.class);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected exception
        }

        // Error scenarios
        try {
            validator.validateSubType(null, null);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected exception
        }

        try {
            validator.validateSubType(null, JavaType.class);
        } catch (JsonMappingException e) {
            fail("Unexpected JsonMappingException");
        }
    }
}