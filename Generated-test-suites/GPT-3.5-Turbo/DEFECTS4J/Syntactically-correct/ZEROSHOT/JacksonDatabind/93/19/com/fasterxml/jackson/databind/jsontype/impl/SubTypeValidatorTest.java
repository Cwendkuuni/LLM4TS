package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class SubTypeValidatorTest {

    private SubTypeValidator subTypeValidator;

    @Before
    public void setUp() {
        subTypeValidator = new SubTypeValidator();
    }

    @Test
    public void testValidateSubType_IllegalType() {
        DeserializationContext ctxt = null;
        JavaType type = null;
        try {
            subTypeValidator.validateSubType(ctxt, type);
            Assert.fail("Expected exception JsonMappingException");
        } catch (JsonMappingException e) {
            Assert.assertEquals("Illegal type (null) to deserialize: prevented for security reasons", e.getMessage());
        }
    }

    @Test
    public void testValidateSubType_ValidType() {
        DeserializationContext ctxt = createDeserializationContext();
        JavaType type = createJavaType();

        try {
            subTypeValidator.validateSubType(ctxt, type);
        } catch (JsonMappingException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    private DeserializationContext createDeserializationContext() {
        return null; // TODO: implement method
    }

    private JavaType createJavaType() {
        return null; // TODO: implement method
    }
}