package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;

public class SubTypeValidatorTest {
    
    private SubTypeValidator validator;
    
    @Before
    public void setUp() {
        validator = SubTypeValidator.instance();
    }
    
    @Test
    public void testValidateSubType() throws JsonMappingException {
        DeserializationContext ctxt = null;
        JavaType type = null;
        
        try {
            validator.validateSubType(ctxt, type);
            Assert.fail("Expected JsonMappingException to be thrown");
        } catch (JsonMappingException e) {
            Assert.assertEquals("Illegal type (null) to deserialize: prevented for security reasons", e.getMessage());
        }
    }
}