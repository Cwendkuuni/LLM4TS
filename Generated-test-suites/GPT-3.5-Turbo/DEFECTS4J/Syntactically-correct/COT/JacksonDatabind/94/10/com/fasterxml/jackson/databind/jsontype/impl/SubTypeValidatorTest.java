package com.fasterxml.jackson.databind.jsontype.impl;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.Assert;
import org.junit.Test;

public class SubTypeValidatorTest {

    @Test
    public void testValidateSubType_validSubType() throws JsonMappingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = new DeserializationContext(null, null);
        JavaType type = new JavaType(Object.class);
        validator.validateSubType(ctxt, type);
        // No exception should be thrown
    }

    @Test
    public void testValidateSubType_illegalSubType() {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = new DeserializationContext(null, null);
        JavaType type = new JavaType(InvokerTransformer.class);
        try {
            validator.validateSubType(ctxt, type);
            Assert.fail("Expected JsonMappingException not thrown");
        } catch (JsonMappingException e) {
            // Exception thrown as expected
            Assert.assertEquals("Illegal type (org.apache.commons.collections.functors.InvokerTransformer) to deserialize: prevented for security reasons", e.getMessage());
        }
    }

    @Test
    public void testValidateSubType_springFrameworkSubType() throws JsonMappingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = new DeserializationContext(null, null);
        JavaType type = new JavaType(org.springframework.beans.factory.config.PropertyPathFactoryBean.class);
        validator.validateSubType(ctxt, type);
        // No exception should be thrown
    }

    @Test
    public void testValidateSubType_interfaceSubType() throws JsonMappingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = new DeserializationContext(null, null);
        JavaType type = new JavaType(Runnable.class);
        validator.validateSubType(ctxt, type);
        // No exception should be thrown
    }
}