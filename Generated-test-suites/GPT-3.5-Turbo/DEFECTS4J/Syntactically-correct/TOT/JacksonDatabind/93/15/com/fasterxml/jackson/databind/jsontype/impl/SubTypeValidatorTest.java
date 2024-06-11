package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Assert;
import org.junit.Test;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

public class SubTypeValidatorTest {

    @Test
    public void testValidateSubType_withValidType_expectNoException() {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = null; // Mocking DeserializationContext
        JavaType type = null; // Mocking JavaType

        try {
            validator.validateSubType(ctxt, type);
        } catch (Throwable exception) {
            Assert.fail("Should not have thrown any exception");
        }
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_withIllegalType_expectJsonMappingException() throws JsonMappingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = null; // Mocking DeserializationContext
        JavaType type = null; // Mocking JavaType

        validator.validateSubType(ctxt, type);
        Assert.fail("Should have thrown JsonMappingException");
    }

}