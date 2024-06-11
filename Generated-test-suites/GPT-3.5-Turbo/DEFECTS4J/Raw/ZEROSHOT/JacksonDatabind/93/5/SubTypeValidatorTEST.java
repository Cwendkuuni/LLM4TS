package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SubTypeValidatorTest {
    private SubTypeValidator validator;

    @Before
    public void setUp() {
        validator = SubTypeValidator.instance();
    }

    @Test
    public void testValidateSubType_ValidSubType() {
        try {
            DeserializationContext ctxt = null; // Provide the required DeserializationContext
            JavaType type = null; // Provide the required JavaType

            validator.validateSubType(ctxt, type);
        } catch (JsonMappingException e) {
            fail("Should not have thrown an exception");
        }
    }

    @Test
    public void testValidateSubType_InvalidSubType() {
        try {
            DeserializationContext ctxt = null; // Provide the required DeserializationContext
            JavaType type = null; // Provide an invalid JavaType

            validator.validateSubType(ctxt, type);
            fail("Should have thrown a JsonMappingException");
        } catch (JsonMappingException e) {
            // Success
        }
    }

}