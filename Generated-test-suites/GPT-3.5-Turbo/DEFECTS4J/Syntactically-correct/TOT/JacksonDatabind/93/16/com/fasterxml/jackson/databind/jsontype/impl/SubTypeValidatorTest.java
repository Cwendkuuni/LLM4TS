package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

public class SubTypeValidatorTest {

    private SubTypeValidator subTypeValidator;
    private DeserializationContext ctxt;
    private JavaType type;

    @Before
    public void setup() {
        subTypeValidator = SubTypeValidator.instance();
        ctxt = Mockito.mock(DeserializationContext.class);
        type = Mockito.mock(JavaType.class);
    }

    @Test
    public void testValidateSubType_ValidType() throws JsonMappingException {
        Mockito.when(type.getRawClass()).thenReturn(Object.class);
        
        try {
            subTypeValidator.validateSubType(ctxt, type);
            Assert.assertTrue(true);
        } catch (JsonMappingException e) {
            Assert.fail("Should have thrown JsonMappingException");
        }
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_IllegalType() throws JsonMappingException {
        Mockito.when(type.getRawClass()).thenReturn(String.class);
        subTypeValidator.validateSubType(ctxt, type);
    }

    @Test
    public void testValidateSubType_SpringFrameworkType() throws JsonMappingException {
        Mockito.when(type.getRawClass()).thenReturn(org.springframework.aop.support.AbstractPointcutAdvisor.class);
        
        try {
            subTypeValidator.validateSubType(ctxt, type);
            Assert.fail("Should have thrown JsonMappingException");
        } catch (JsonMappingException e) {
            Assert.assertEquals("Illegal type (org.springframework.aop.support.AbstractPointcutAdvisor) to deserialize: prevented for security reasons", e.getMessage());
        }
    }
}