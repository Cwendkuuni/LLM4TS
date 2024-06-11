package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class SubTypeValidatorTest {

    private DeserializationContext mockDeserializationContext;
    private JavaType mockJavaType;

    @Before
    public void setUp() {
        mockDeserializationContext = mock(DeserializationContext.class);
        mockJavaType = mock(JavaType.class);
    }

    @Test
    public void testValidateSubType_validClass() throws JsonMappingException {
        SubTypeValidator subTypeValidator = SubTypeValidator.instance();

        when(mockJavaType.getRawClass()).thenReturn(Object.class);

        subTypeValidator.validateSubType(mockDeserializationContext, mockJavaType);
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_illegalClass() throws JsonMappingException {
        SubTypeValidator subTypeValidator = SubTypeValidator.instance();

        when(mockJavaType.getRawClass()).thenReturn(String.class);

        subTypeValidator.validateSubType(mockDeserializationContext, mockJavaType);
    }

    @Test
    public void testValidateSubType_springAbstractPointcutAdvisor() throws JsonMappingException {
        SubTypeValidator subTypeValidator = SubTypeValidator.instance();

        when(mockJavaType.getRawClass()).thenReturn(AbstractPointcutAdvisor.class);
        when(mockJavaType.getName()).thenReturn("org.springframework.AbstractPointcutAdvisor");

        try {
            subTypeValidator.validateSubType(mockDeserializationContext, mockJavaType);
        } catch (JsonMappingException e) {
            assertEquals("Illegal type (org.springframework.AbstractPointcutAdvisor) to deserialize: prevented for security reasons", e.getMessage());
            throw e;
        }
    }

    @Test
    public void testValidateSubType_springAbstractApplicationContext() throws JsonMappingException {
        SubTypeValidator subTypeValidator = SubTypeValidator.instance();

        when(mockJavaType.getRawClass()).thenReturn(AbstractApplicationContext.class);
        when(mockJavaType.getName()).thenReturn("org.springframework.AbstractApplicationContext");

        try {
            subTypeValidator.validateSubType(mockDeserializationContext, mockJavaType);
        } catch (JsonMappingException e) {
            assertEquals("Illegal type (org.springframework.AbstractApplicationContext) to deserialize: prevented for security reasons", e.getMessage());
            throw e;
        }
    }

    static class AbstractPointcutAdvisor {
    }

    static class AbstractApplicationContext {
    }
}