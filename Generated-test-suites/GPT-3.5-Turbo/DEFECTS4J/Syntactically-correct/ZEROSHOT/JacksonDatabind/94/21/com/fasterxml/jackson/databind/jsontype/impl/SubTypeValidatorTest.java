package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;
import org.junit.Test;

public class SubTypeValidatorTest {
    @Test
    public void testValidateSubType() {
        SubTypeValidator validator = SubTypeValidator.instance();
        
        // Test valid class
        try {
            validator.validateSubType(null, JavaType.fromClass(Integer.class));
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
        
        // Test invalid class
        try {
            validator.validateSubType(null, JavaType.fromClass(String.class));
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Illegal type (java.lang.String) to deserialize: prevented for security reasons", e.getMessage());
        }
    }
}