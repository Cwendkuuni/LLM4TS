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
    public static void setup() {
        validator = SubTypeValidator.instance();
    }

    @Test
    public void testValidateSubType_WithValidType_ShouldNotThrowException() {
        try {
            DeserializationContext ctxt = null; // Setup dummy DeserializationContext
            JavaType type = null; // Setup dummy JavaType

            validator.validateSubType(ctxt, type);

            // If no exception is thrown, the test passes
        } catch (JsonMappingException e) {
            Assert.fail("Unexpected JsonMappingException thrown");
        }
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_WithIllegalType_ShouldThrowException() throws JsonMappingException {
        DeserializationContext ctxt = null; // Setup dummy DeserializationContext
        JavaType type = null; // Setup dummy JavaType

        // Set the illegal class name
        validator._cfgIllegalClassNames.add("org.springframework.beans.factory.config.PropertyPathFactoryBean");

        validator.validateSubType(ctxt, type);

        // If an exception is thrown, the test passes
    }

    @Test
    public void testValidateSubType_WithSpringFrameworkType_ShouldThrowException() {
        try {
            DeserializationContext ctxt = null; // Setup dummy DeserializationContext
            JavaType type = null; // Setup dummy JavaType

            // Set the Spring framework class name
            validator._cfgIllegalClassNames.add("org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor");

            validator.validateSubType(ctxt, type);

            // If no exception is thrown, the test fails
            Assert.fail("Expected JsonMappingException but no exception was thrown");
        } catch (JsonMappingException e) {
            // If an exception is thrown, the test passes
        }
    }

    @Test
    public void testValidateSubType_WithSuperClass_ShouldBreakMainCheck() {
        try {
            DeserializationContext ctxt = null; // Setup dummy DeserializationContext
            JavaType type = null; // Setup dummy JavaType

            // Set the class name that breaks the main check
            validator._cfgIllegalClassNames.add("org.springframework.AbstractApplicationContext");

            validator.validateSubType(ctxt, type);

            // If no exception is thrown, the test fails
            Assert.fail("Expected JsonMappingException but no exception was thrown");
        } catch (JsonMappingException e) {
            // If an exception is thrown, the test passes
        }
    }
}