
package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
import com.fasterxml.jackson.databind.jsontype.impl.SubTypeValidator;

public class SubTypeValidatorTest {

    private ObjectMapper mapper = new ObjectMapper();
    private DefaultDeserializationContext ctxt = new DefaultDeserializationContext.Impl(mapper, null);

    @Test
    public void testValidateSubTypeValid() {
        SubTypeValidator validator = SubTypeValidator.instance();
        JavaType type = mapper.constructType(String.class);
        try {
            validator.validateSubType(ctxt, type);
            assertTrue(true);
        } catch (JsonMappingException e) {
            fail("An unexpected exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testValidateSubTypeIllegalClass() {
        SubTypeValidator validator = SubTypeValidator.instance();
        JavaType type = mapper.constructType(org.apache.commons.collections.functors.InvokerTransformer.class);
        try {
            validator.validateSubType(ctxt, type);
            fail("Expected JsonMappingException was not thrown");
        } catch (JsonMappingException e) {
            assertEquals("Illegal type (org.apache.commons.collections.functors.InvokerTransformer) to deserialize: prevented for security reasons", e.getMessage());
        }
    }

    @Test
    public void testValidateSubTypeSpringFrameworkType() {
        SubTypeValidator validator = SubTypeValidator.instance();
        JavaType type = mapper.constructType(org.springframework.beans.factory.config.PropertyPathFactoryBean.class);
        try {
            validator.validateSubType(ctxt, type);
            fail("Expected JsonMappingException was not thrown");
        } catch (JsonMappingException e) {
            assertEquals("Illegal type (org.springframework.beans.factory.config.PropertyPathFactoryBean) to deserialize: prevented for security reasons", e.getMessage());
        }
    }
}

