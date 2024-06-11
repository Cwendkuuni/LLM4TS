package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;

public class SubTypeValidatorTest {

    private SubTypeValidator subTypeValidator;

    @Before
    public void setUp() {
        subTypeValidator = SubTypeValidator.instance();
    }

    @Test
    public void testValidateSubType_WithValidClass() throws JsonMappingException {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JavaType type = mock(JavaType.class);
        Class<?> rawClass = mock(Class.class);
        
        when(type.getRawClass()).thenReturn(rawClass);
        when(rawClass.getName()).thenReturn("com.fasterxml.jackson.core.JsonGenerator");
        
        subTypeValidator.validateSubType(ctxt, type);
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_WithInvalidType() throws JsonMappingException {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JavaType type = mock(JavaType.class);
        Class<?> rawClass = mock(Class.class);
        
        when(type.getRawClass()).thenReturn(rawClass);
        when(rawClass.getName()).thenReturn("org.springframework.beans.factory.config.PropertyPathFactoryBean");
        
        subTypeValidator.validateSubType(ctxt, type);
    }

    @Test
    public void testValidateSubType_CheckSpringClassInterfaces() throws JsonMappingException {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JavaType type = mock(JavaType.class);
        Class<?> rawClass = mock(Class.class);
        
        when(type.getRawClass()).thenReturn(rawClass);
        when(rawClass.getName()).thenReturn("org.springframework.core.type.AnnotationMetadata");
        when(rawClass.isInterface()).thenReturn(true);
        
        subTypeValidator.validateSubType(ctxt, type);
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_BlockAbstractBeanFactoryPointcutAdvisor() throws JsonMappingException {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JavaType type = mock(JavaType.class);
        Class<?> rawClass = mock(Class.class);
        
        when(type.getRawClass()).thenReturn(rawClass);
        when(rawClass.getName()).thenReturn("org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor");
        when(rawClass.isInterface()).thenReturn(false);
        
        subTypeValidator.validateSubType(ctxt, type);
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_BlockAbstractApplicationContext() throws JsonMappingException {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JavaType type = mock(JavaType.class);
        Class<?> rawClass = mock(Class.class);
        
        when(type.getRawClass()).thenReturn(rawClass);
        when(rawClass.getName()).thenReturn("org.springframework.context.support.AbstractApplicationContext");
        when(rawClass.isInterface()).thenReturn(false);
        
        subTypeValidator.validateSubType(ctxt, type);
    }
}