package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

public class SubTypeValidatorTest {

    private SubTypeValidator subTypeValidator;
    private DeserializationContext ctxt;
    private JavaType type;

    @Before
    public void setUp() {
        subTypeValidator = SubTypeValidator.instance();
        ctxt = new DeserializationContext(null, null);
        type = new JavaType();
    }

    @Test
    public void testValidateSubType() throws JsonMappingException {
        try {
            subTypeValidator.validateSubType(ctxt, type);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Illegal type (java.lang.String) to deserialize: prevented for security reasons", e.getMessage());
        }
    }

    @Test
    public void testValidateSubType_WithValidClass() throws JsonMappingException {
        JavaType validType = new JavaType();
        validType.setRawClass(Object.class);

        subTypeValidator.validateSubType(ctxt, validType);

        // No exception should be thrown
    }
}