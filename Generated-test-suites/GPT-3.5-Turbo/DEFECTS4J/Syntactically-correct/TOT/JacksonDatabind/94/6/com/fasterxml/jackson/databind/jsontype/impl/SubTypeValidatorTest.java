package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

@RunWith(MockitoJUnitRunner.class)
public class SubTypeValidatorTest {
    
    private SubTypeValidator subTypeValidator;
    
    @Mock
    private DeserializationContext deserializationContext;
    
    @Mock
    private JavaType javaType;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        subTypeValidator = SubTypeValidator.instance();
    }
    
    @Test
    public void testValidateSubType_WhenValidType_DeserializationAllowed() throws JsonMappingException {
        subTypeValidator.validateSubType(deserializationContext, javaType);
        // No exception should be thrown
    }
    
    @Test(expected=JsonMappingException.class)
    public void testValidateSubType_WhenInvalidType_DeserializationPrevented() throws JsonMappingException {
        JavaType invalidType = Mockito.mock(JavaType.class);
        Mockito.when(invalidType.getRawClass()).thenReturn(Object.class);
        
        subTypeValidator.validateSubType(deserializationContext, invalidType);
        // JsonMappingException should be thrown
    }
    
    @Test
    public void testValidateSubType_WhenSpringClass_DeserializationPrevented() throws JsonMappingException {
        JavaType springClass = Mockito.mock(JavaType.class);
        Class<?> springRawClass = Mockito.mock(Class.class);
        Mockito.when(springClass.getRawClass()).thenReturn(springRawClass);
        Mockito.when(springRawClass.getName()).thenReturn("org.springframework.SomeClass");
        
        try {
            subTypeValidator.validateSubType(deserializationContext, springClass);
            fail("Expected JsonMappingException to be thrown");
        } catch (JsonMappingException e) {
            assertEquals("Illegal type (org.springframework.SomeClass) to deserialize: prevented for security reasons", e.getMessage());
        }
    }
    
    @Test
    public void testValidateSubType_WhenSpringAbstractPointcutAdvisor_DeserializationPrevented() throws JsonMappingException {
        JavaType classWithAbstractPointcutAdvisor = Mockito.mock(JavaType.class);
        Class<?> rawClass = Mockito.mock(Class.class);
        Mockito.when(classWithAbstractPointcutAdvisor.getRawClass()).thenReturn(rawClass);
        Mockito.when(rawClass.getName()).thenReturn("org.springframework.SomeClass");
        Mockito.when(rawClass.isInterface()).thenReturn(false);
        
        Class<?> parentClass = Mockito.mock(Class.class);
        Mockito.when(rawClass.getSuperclass()).thenReturn(parentClass);
        Mockito.when(parentClass.getSimpleName()).thenReturn("AbstractPointcutAdvisor");
        
        try {
            subTypeValidator.validateSubType(deserializationContext, classWithAbstractPointcutAdvisor);
            fail("Expected JsonMappingException to be thrown");
        } catch (JsonMappingException e) {
            assertEquals("Illegal type (org.springframework.SomeClass) to deserialize: prevented for security reasons", e.getMessage());
        }
    }
    
    @Test
    public void testValidateSubType_WhenThirdPartyClass_DeserializationAllowed() throws JsonMappingException {
        JavaType thirdPartyClass = Mockito.mock(JavaType.class);
        Class<?> thirdPartyRawClass = Mockito.mock(Class.class);
        Mockito.when(thirdPartyClass.getRawClass()).thenReturn(thirdPartyRawClass);
        Mockito.when(thirdPartyRawClass.getName()).thenReturn("com.thirdparty.SomeClass");
        
        subTypeValidator.validateSubType(deserializationContext, thirdPartyClass);
        // No exception should be thrown
    }
}