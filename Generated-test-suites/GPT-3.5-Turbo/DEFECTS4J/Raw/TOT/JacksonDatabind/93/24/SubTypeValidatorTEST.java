package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class SubTypeValidatorTest {

    private static SubTypeValidator validator;

    @BeforeClass
    public static void setUp() {
        validator = SubTypeValidator.instance();
    }

    @Test
    public void testValidateSubType_ValidType_NoException() {
        try {
            DeserializationContext ctxt = null; // Create or mock a DeserializationContext instance
            JavaType type = null; // Create or mock a JavaType instance
            validator.validateSubType(ctxt, type);
        } catch (JsonMappingException e) {
            Assert.fail("Expected no exception, but got: " + e.getMessage());
        }
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_IllegalType_ThrowsJsonMappingException() throws JsonMappingException {
        DeserializationContext ctxt = null; // Create or mock a DeserializationContext instance
        JavaType type = null; // Create or mock a JavaType instance
        validator.validateSubType(ctxt, type);
    }

    @Test
    public void testValidateSubType_AbstractPointcutAdvisor_ThrowsJsonMappingException() {
        try {
            DeserializationContext ctxt = null; // Create or mock a DeserializationContext instance
            JavaType type = null; // Create or mock a JavaType instance
            Class<?> cls = type.getRawClass();
            cls = SubTypeValidatorTest.class; // Any class that is a sub class of AbstractPointcutAdvisor
            type = JavaType.construct(cls);
            validator.validateSubType(ctxt, type);
            Assert.fail("Expected JsonMappingException, but no exception was thrown");
        } catch (JsonMappingException e) {
            Assert.assertEquals("Illegal type (com.fasterxml.jackson.databind.jsontype.impl.SubTypeValidatorTest) to deserialize: prevented for security reasons", e.getMessage());
        }
    }

    @Test
    public void testValidateSubType_AbstractApplicationContext_ThrowsJsonMappingException() {
        try {
            DeserializationContext ctxt = null; // Create or mock a DeserializationContext instance
            JavaType type = null; // Create or mock a JavaType instance
            Class<?> cls = type.getRawClass();
            cls = SubTypeValidator.class; // Any class that is a sub class of AbstractApplicationContext
            type = JavaType.construct(cls);
            validator.validateSubType(ctxt, type);
            Assert.fail("Expected JsonMappingException, but no exception was thrown");
        } catch (JsonMappingException e) {
            Assert.assertEquals("Illegal type (com.fasterxml.jackson.databind.jsontype.impl.SubTypeValidator) to deserialize: prevented for security reasons", e.getMessage());
        }
    }
}