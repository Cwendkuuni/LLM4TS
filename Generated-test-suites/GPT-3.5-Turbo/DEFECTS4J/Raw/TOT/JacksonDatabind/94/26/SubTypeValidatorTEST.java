package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;

public class SubTypeValidatorTest {

    private SubTypeValidator validator;
    private DeserializationContext ctxt;
    private JavaType type;

    @Before
    public void setUp() {
        validator = SubTypeValidator.instance();
        ctxt = new DummyDeserializationContext();
        type = JavaTypeBuilder.buildType(String.class);
    }

    @Test
    public void testValidateSubType_ValidType() {
        try {
            validator.validateSubType(ctxt, type);
        } catch (JsonMappingException e) {
            Assert.fail("Validation should pass for a valid type");
        }
    }

    @Test
    public void testValidateSubType_IllegalType() {
        String illegalClassName = "org.springframework.beans.factory.ObjectFactory";

        type = JavaTypeBuilder.buildType(illegalClassName);

        try {
            validator.validateSubType(ctxt, type);
            Assert.fail("Validation should fail for an illegal type");
        } catch (JsonMappingException e) {
            Assert.assertEquals("Illegal type (org.springframework.beans.factory.ObjectFactory) to deserialize: prevented for security reasons", e.getMessage());
        }
    }

    @Test
    public void testValidateSubType_SuperclassOfIllegalType() {
        String illegalClassName = "org.springframework.beans.factory.config.PropertyPathFactoryBean";
        String superClass = "org.springframework.beans.factory.ObjectFactory";

        type = JavaTypeBuilder.buildType(illegalClassName);

        try {
            validator.validateSubType(ctxt, type);
            Assert.fail("Validation should fail for an illegal type");
        } catch (JsonMappingException e) {
            Assert.assertEquals("Illegal type (org.springframework.beans.factory.config.PropertyPathFactoryBean) to deserialize: prevented for security reasons", e.getMessage());
        }
    }

    private static class DummyDeserializationContext extends DeserializationContext {
        // Dummy DeserializationContext implementation
    }
}