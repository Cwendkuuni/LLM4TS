
package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SubTypeValidatorTest {

    @Mock
    private DeserializationContext deserializationContextMock;

    @Mock
    private JavaType javaTypeMock;

    private SubTypeValidator subTypeValidator;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        subTypeValidator = SubTypeValidator.instance();
    }

    @Test
    public void testValidateSubType_withIllegalClassName_shouldThrowException() throws JsonMappingException {
        when(javaTypeMock.getRawClass()).thenReturn(Object.class);
        when(javaTypeMock.getName()).thenReturn("IllegalClassName");

        try {
            subTypeValidator.validateSubType(deserializationContextMock, javaTypeMock);
            fail("Expected JsonMappingException was not thrown");
        } catch (JsonMappingException ex) {
            assertEquals("Illegal type (IllegalClassName) to deserialize: prevented for security reasons", ex.getMessage());
        }
    }

    @Test
    public void testValidateSubType_withValidClassName_shouldNotThrowException() throws JsonMappingException {
        when(javaTypeMock.getRawClass()).thenReturn(Object.class);
        when(javaTypeMock.getName()).thenReturn("ValidClassName");

        try {
            subTypeValidator.validateSubType(deserializationContextMock, javaTypeMock);
        } catch (JsonMappingException ex) {
            fail("Unexpected JsonMappingException was thrown: " + ex.getMessage());
        }
    }

    @Test
    public void testValidateSubType_withSpringAbstractPointcutAdvisor_shouldThrowException() throws JsonMappingException {
        when(javaTypeMock.getRawClass()).thenReturn(AbstractPointcutAdvisor.class);
        when(javaTypeMock.getName()).thenReturn("org.springframework.aop.support.AbstractPointcutAdvisor");

        try {
            subTypeValidator.validateSubType(deserializationContextMock, javaTypeMock);
            fail("Expected JsonMappingException was not thrown");
        } catch (JsonMappingException ex) {
            assertEquals("Illegal type (org.springframework.aop.support.AbstractPointcutAdvisor) to deserialize: prevented for security reasons", ex.getMessage());
        }
    }

    @Test
    public void testValidateSubType_withSpringAbstractApplicationContext_shouldThrowException() throws JsonMappingException {
        when(javaTypeMock.getRawClass()).thenReturn(AbstractApplicationContext.class);
        when(javaTypeMock.getName()).thenReturn("org.springframework.context.support.AbstractApplicationContext");

        try {
            subTypeValidator.validateSubType(deserializationContextMock, javaTypeMock);
            fail("Expected JsonMappingException was not thrown");
        } catch (JsonMappingException ex) {
            assertEquals("Illegal type (org.springframework.context.support.AbstractApplicationContext) to deserialize: prevented for security reasons", ex.getMessage());
        }
    }
}
