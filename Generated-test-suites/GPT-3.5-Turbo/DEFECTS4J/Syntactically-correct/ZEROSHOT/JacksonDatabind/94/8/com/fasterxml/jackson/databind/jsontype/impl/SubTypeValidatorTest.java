package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class SubTypeValidatorTest {

    private SubTypeValidator validator;

    @Before
    public void setUp() {
        validator = SubTypeValidator.instance();
    }

    @Test
    public void testValidateSubType_InvalidClass() throws JsonMappingException {
        DeserializationContext ctxt = null;
        JavaType type = null;

        try {
            validator.validateSubType(ctxt, type);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Illegal type (null) to deserialize: prevented for security reasons", e.getMessage());
        }
    }

    @Test
    public void testValidateSubType_ValidClass() throws JsonMappingException {
        DeserializationContext ctxt = null;
        JavaType type = JavaTypeBuilder.create().build();

        validator.validateSubType(ctxt, type);
    }

}