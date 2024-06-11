package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

public class SubTypeValidatorTest {

    @Test
    public void testValidateSubType() throws JsonMappingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = null;
        JavaType type = null;

        // Test with invalid raw class
        try {
            validator.validateSubType(ctxt, type);
            Assert.fail("Expected JsonMappingException not thrown");
        } catch (JsonMappingException e) {
            Assert.assertEquals("Illegal type (null) to deserialize: prevented for security reasons", e.getMessage());
        }

        // Test with valid raw class
        type = new JavaType();
        type.setRawClass(Object.class);
        try {
            validator.validateSubType(ctxt, type);
        } catch (JsonMappingException e) {
            Assert.fail("Unexpected JsonMappingException thrown");
        }
    }
}