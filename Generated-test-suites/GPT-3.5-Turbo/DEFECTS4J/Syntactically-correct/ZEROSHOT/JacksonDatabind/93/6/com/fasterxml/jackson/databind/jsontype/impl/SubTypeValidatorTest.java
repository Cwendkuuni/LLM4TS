package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class SubTypeValidatorTest {

    private SubTypeValidator subTypeValidator;

    @Before
    public void setup() {
        subTypeValidator = SubTypeValidator.instance();
    }

    @Test
    public void validateSubType_ValidSubType_DoesNotThrowException() {
        DeserializationContext ctxt = null;
        JavaType type = null;
        try {
            subTypeValidator.validateSubType(ctxt, type);
            // If no exception is thrown, the test has passed
        } catch (JsonMappingException e) {
            fail("Unexpected exception was thrown: " + e.getMessage());
        }
    }

    @Test
    public void validateSubType_InvalidSubType_ThrowsException() {
        DeserializationContext ctxt = null;
        JavaType type = null;
        try {
            subTypeValidator.validateSubType(ctxt, type);
            fail("Expected JsonMappingException to be thrown");
        } catch (JsonMappingException e) {
            assertEquals("Illegal type (null) to deserialize: prevented for security reasons", e.getMessage());
        }
    }
}