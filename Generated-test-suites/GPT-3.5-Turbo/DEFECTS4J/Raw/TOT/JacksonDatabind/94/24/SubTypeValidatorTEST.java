
package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

public class SubTypeValidatorTest {

    private SubTypeValidator subTypeValidator;

    @Before
    public void setUp() {
        subTypeValidator = SubTypeValidator.instance();
    }

    @Test
    public void testValidateSubType_WhenValidRawClass_ThrowsNoException() {
        DeserializationContext ctxt = null;
        JavaType type = null;

        try {
            subTypeValidator.validateSubType(ctxt, type);
            // No exception is thrown
        } catch (JsonMappingException e) {
            fail("Failed with exception: " + e.getMessage());
        }
    }

    @Test
    public void testValidateSubType_WhenInvalidRawClass_ThrowsJsonMappingException() {
        DeserializationContext ctxt = null;
        JavaType type = null;

        try {
            subTypeValidator.validateSubType(ctxt, type);
            fail("Expected JsonMappingException was not thrown");
        } catch (JsonMappingException e) {
            assertEquals("Illegal type to deserialize: prevented for security reasons", e.getMessage());
        }
    }
}
