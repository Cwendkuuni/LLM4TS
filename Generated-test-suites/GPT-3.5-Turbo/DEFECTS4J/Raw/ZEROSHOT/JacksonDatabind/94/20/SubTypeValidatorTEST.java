package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.mockito.Mockito.*;

public class SubTypeValidatorTest {

    @Mock
    DeserializationContext mockDeserializationContext;
    @Mock
    JavaType mockJavaType;

    SubTypeValidator subTypeValidator;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        
        subTypeValidator = SubTypeValidator.instance();
    }

    @Test
    public void testValidateSubType_TypeInIllegalClassNames_SecurityExceptionThrown() throws JsonMappingException {
        // Arrange
        String className = "org.springframework.beans.factory.ObjectFactory";
        when(mockJavaType.getRawClass()).thenReturn(Object.class);
        when(mockJavaType.getRawClass().getName()).thenReturn(className);

        // Act & Assert
        try {
            subTypeValidator.validateSubType(mockDeserializationContext, mockJavaType);
            fail("Expected JsonMappingException to be thrown");
        } catch (JsonMappingException e) {
            assertEquals("Illegal type (org.springframework.beans.factory.ObjectFactory) to deserialize: prevented for security reasons", e.getMessage());
        }

        verify(mockJavaType, times(1)).getRawClass();
        verify(mockJavaType.getRawClass(), times(1)).getName();
    }

    @Test
    public void testValidateSubType_TypeNotInIllegalClassNames_NoExceptionThrown() throws JsonMappingException {
        // Arrange
        String className = "com.fasterxml.jackson.databind.BeanProperty";
        when(mockJavaType.getRawClass()).thenReturn(Object.class);
        when(mockJavaType.getRawClass().getName()).thenReturn(className);

        // Act
        subTypeValidator.validateSubType(mockDeserializationContext, mockJavaType);

        // Assert
        verify(mockJavaType, times(1)).getRawClass();
        verify(mockJavaType.getRawClass(), times(1)).getName();
    }

    @Test
    public void testValidateSubType_InterfaceType_NoExceptionThrown() throws JsonMappingException {
        // Arrange
        String className = "java.util.List";
        when(mockJavaType.getRawClass()).thenReturn(List.class);
        when(mockJavaType.getRawClass().getName()).thenReturn(className);

        // Act
        subTypeValidator.validateSubType(mockDeserializationContext, mockJavaType);

        // Assert
        verify(mockJavaType, times(1)).getRawClass();
        verify(mockJavaType.getRawClass(), times(1)).getName();
    }

    @Test
    public void testValidateSubType_TypeStartsWithPrefixSpring_IllegalTypeFound_ExceptionThrown() throws JsonMappingException {
        // Arrange
        String className = "org.springframework.beans.factory.AbstractPointcutAdvisor";
        when(mockJavaType.getRawClass()).thenReturn(Object.class);
        when(mockJavaType.getRawClass().getName()).thenReturn(className);

        // Act & Assert
        try {
            subTypeValidator.validateSubType(mockDeserializationContext, mockJavaType);
            fail("Expected JsonMappingException to be thrown");
        } catch (JsonMappingException e) {
            assertEquals("Illegal type (org.springframework.beans.factory.AbstractPointcutAdvisor) to deserialize: prevented for security reasons", e.getMessage());
        }

        verify(mockJavaType, times(1)).getRawClass();
        verify(mockJavaType.getRawClass(), times(1)).getName();
    }

    class List {
    }
}