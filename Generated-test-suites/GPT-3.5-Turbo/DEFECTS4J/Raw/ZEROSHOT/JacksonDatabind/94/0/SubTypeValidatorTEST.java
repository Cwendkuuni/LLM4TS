package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class SubTypeValidatorTest {

    private static SubTypeValidator validator;

    @BeforeClass
    public static void setUp() {
        validator = SubTypeValidator.instance();
    }

    @Test
    public void testValidateSubType() throws JsonMappingException {
        // Test with valid JavaType
        DeserializationContext ctxt = null;
        JavaType type = null;
        try {
            validator.validateSubType(ctxt, type);
        } catch (JsonMappingException e) {
            fail("Expected no exception, but got: " + e);
        }

        // Test with invalid JavaType
    try {
        validator.validateSubType(ctxt, null);
        fail("Expected JsonMappingException");
    } catch (JsonMappingException e) {
        // Expected exception
    }
    }

}