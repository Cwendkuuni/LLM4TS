package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SubTypeValidatorTest {

    @Mock
    private DeserializationContext deserializationContext;

    @Mock
    private JavaType javaType;

    private SubTypeValidator subTypeValidator;

    public SubTypeValidatorTest() {
        MockitoAnnotations.initMocks(this);
        subTypeValidator = SubTypeValidator.instance();
    }

    @Test
    public void testValidateSubType_ThrowsException() {
        try {
            subTypeValidator.validateSubType(deserializationContext, javaType);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Illegal type (%s) to deserialize: prevented for security reasons", e.getMessage());
        }
    }

    @Test
    public void testValidateSubType_ReturnsWithoutException_WhenValidJavaType() {
        when(javaType.getRawClass()).thenReturn(Object.class);
        try {
            subTypeValidator.validateSubType(deserializationContext, javaType);
        } catch (JsonMappingException e) {
            fail("Unexpected JsonMappingException");
        }
    }

    @Test
    public void testValidateSubType_ReturnsWithoutException_WhenValidJavaTypeInterface() {
        when(javaType.getRawClass()).thenReturn(SubTypeValidator.class);
        try {
            subTypeValidator.validateSubType(deserializationContext, javaType);
        } catch (JsonMappingException e) {
            fail("Unexpected JsonMappingException");
        }
    }

    @Test
    public void testValidateSubType_ReturnsWithoutException_WhenValidJavaTypeWithPrefix() {
        when(javaType.getRawClass()).thenReturn(SpringTestClass.class);
        try {
            subTypeValidator.validateSubType(deserializationContext, javaType);
        } catch (JsonMappingException e) {
            fail("Unexpected JsonMappingException");
        }
    }

    private static class SpringTestClass {}

}