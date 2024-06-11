package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

public class SubTypeValidatorTest
{
    @Test
    public void testValidateSubType_validType() throws JsonMappingException
    {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = null; // initialize DeserializationContext
        JavaType type = null; // initialize JavaType

        // test valid subtype
        validator.validateSubType(ctxt, type);
        // no exception should be thrown
    }

    @Test
    public void testValidateSubType_invalidNastyClass() throws JsonMappingException
    {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = null; // initialize DeserializationContext
        JavaType type = null; // initialize JavaType

        // test invalid nasty class
        type = new JavaType(org.apache.commons.collections.functors.InvokerTransformer.class);
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(ctxt, type));
    }

    @Test
    public void testValidateSubType_securityViolation() throws JsonMappingException
    {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = null; // initialize DeserializationContext
        JavaType type = null; // initialize JavaType

        // test security violation for Spring framework types
        type = new JavaType(org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor.class);
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(ctxt, type));
    }
}