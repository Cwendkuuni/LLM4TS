package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class SubTypeValidatorTest {

    private SubTypeValidator subTypeValidator;

    @Before
    public void setUp() {
        subTypeValidator = SubTypeValidator.instance();
    }

    @Test
    public void testValidateSubType_ValidType() throws JsonMappingException {
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        JavaType javaType = mock(JavaType.class);

        subTypeValidator.validateSubType(deserializationContext, javaType);

        // No exception means the validation passed
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_IllegalType() throws JsonMappingException {
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        JavaType javaType = mock(JavaType.class);
        Class<?> rawClass = mock(Class.class);
        Mockito.when(javaType.getRawClass()).thenReturn(rawClass);
        Mockito.when(rawClass.getName()).thenReturn("org.springframework.beans.factory.config.PropertyPathFactoryBean");

        subTypeValidator.validateSubType(deserializationContext, javaType);

        // The expected exception should be thrown
    }
}