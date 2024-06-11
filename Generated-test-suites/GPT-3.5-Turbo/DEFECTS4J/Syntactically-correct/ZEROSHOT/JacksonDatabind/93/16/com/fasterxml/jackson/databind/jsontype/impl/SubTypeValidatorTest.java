package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

public class SubTypeValidatorTest {

    @Test
    public void testValidateSubType_InvalidSubType() {
        // Test when given a sub type that is in the illegal class names set
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = null;
        JavaType type = new JavaType();
        type.setRawClass(Object.class);
        try {
            validator.validateSubType(ctxt, type);
            fail("Expected JsonMappingException to be thrown");
        } catch (JsonMappingException e) {
            // Verify that the exception message is correct
            assertEquals("Illegal type (java.lang.Object) to deserialize: prevented for security reasons", e.getMessage());
        }
    }

    @Test
    public void testValidateSubType_ValidSubType() {
        // Test when given a sub type that is not in the illegal class names set
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = null;
        JavaType type = new JavaType();
        type.setRawClass(String.class);
        try {
            validator.validateSubType(ctxt, type);
        } catch (JsonMappingException e) {
            fail("Expected no JsonMappingException to be thrown");
        }
    }
}