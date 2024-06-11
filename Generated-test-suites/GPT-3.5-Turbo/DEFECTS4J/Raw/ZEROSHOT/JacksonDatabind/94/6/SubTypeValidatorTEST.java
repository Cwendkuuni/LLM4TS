package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Test;
import static org.junit.Assert.*;

public class SubTypeValidatorTest {

    @Test
    public void testValidateSubType() throws Exception {
        SubTypeValidator validator = SubTypeValidator.instance();
        
        // Test valid subtype
        validator.validateSubType(null, JavaType.fromClass(String.class));
        
        // Test invalid subtype
        try {
            validator.validateSubType(null, JavaType.fromClass(com.fasterxml.jackson.databind.type.TypeBase.class));
            fail("Expected JsonMappingException to be thrown");
        } catch (JsonMappingException e) {
            String expectedMessage = "Illegal type (com.fasterxml.jackson.databind.type.TypeBase) to deserialize: prevented for security reasons";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}