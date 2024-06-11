package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.Test;

public class SubTypeValidatorTest {

    @Test
    public void testValidateSubType_validType() {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = null;
        JavaType type = null;

        try {
            validator.validateSubType(ctxt, type);
        } catch (JsonMappingException e) {
            fail("JsonMappingException should not be thrown for valid type");
        }
    }

    @Test
    public void testValidateSubType_invalidType() {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = null;
        JavaType type = null;

        try {
            validator.validateSubType(ctxt, type);
            fail("JsonMappingException should be thrown for invalid type");
        } catch (JsonMappingException e) {
            assertEquals("Illegal type (null) to deserialize: prevented for security reasons", e.getMessage());
        }
    }
}