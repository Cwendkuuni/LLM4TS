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
        try {
            validator.validateSubType(ctxt, type);
        } catch (JsonMappingException e) {
            Assert.assertEquals("Illegal type () to deserialize: prevented for security reasons", e.getMessage());
        }
    }

    @Test
    public void testConstructor() {
        SubTypeValidator validator = new SubTypeValidator();
        Assert.assertNotNull(validator);
    }
}