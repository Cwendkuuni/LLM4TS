package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import java.util.HashSet;
import java.util.Set;
import org.junit.Test;

public class SubTypeValidatorTest {

    @Test
    public void testValidateSubType() {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = new DeserializationContext(null, null);
        JavaType type;

        // Valid class
        type = JavaType.emptyType();
        type.setRawClass(Object.class);
        try {
            validator.validateSubType(ctxt, type);
        } catch (JsonMappingException e) {
            fail("Valid class should not throw exception");
        }

        // Invalid class
        type = JavaType.emptyType();
        type.setRawClass(org.apache.commons.collections.functors.InvokerTransformer.class);
        try {
            validator.validateSubType(ctxt, type);
        } catch (JsonMappingException e) {
            assertEquals("JsonMappingException message is incorrect", "Illegal type (org.apache.commons.collections.functors.InvokerTransformer) to deserialize: prevented for security reasons", e.getMessage());
        }

        // Invalid class - Spring framework type
        type = JavaType.emptyType();
        type.setRawClass(org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor.class);
        try {
            validator.validateSubType(ctxt, type);
        } catch (JsonMappingException e) {
            assertEquals("JsonMappingException message is incorrect", "Illegal type (org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor) to deserialize: prevented for security reasons", e.getMessage());
        }
    }
}