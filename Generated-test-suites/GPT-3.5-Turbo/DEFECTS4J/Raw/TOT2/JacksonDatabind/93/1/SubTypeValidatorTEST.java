package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

public class SubTypeValidatorTest
{
    SubTypeValidator validator = SubTypeValidator.instance();

    @Test
    public void testValidateSubType_validType() {
        try {
            validator.validateSubType(null, JavaType.class);
        } catch (JsonMappingException e) {
            fail("Validation should pass for a valid type");
        }
    }

    @Test
    public void testValidateSubType_illegalType() {
        try {
            validator.validateSubType(null, MutableTransformer.class);
            fail("Validation should throw JsonMappingException for an illegal type");
        } catch (JsonMappingException e) {
            assertEquals("Illegal type (com.fasterxml.jackson.datatype.jsr310.deser.DurationDeserializer) to deserialize: prevented for security reasons", e.getMessage());
        }
    }

    @Test
    public void testValidateSubType_springFrameworkType() {
        try {
            validator.validateSubType(null, AbstractBeanFactoryPointcutAdvisor.class);
            fail("Validation should throw JsonMappingException for a Spring framework type");
        } catch (JsonMappingException e) {
            assertEquals("Illegal type (org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor) to deserialize: prevented for security reasons", e.getMessage());
        }
    }
}