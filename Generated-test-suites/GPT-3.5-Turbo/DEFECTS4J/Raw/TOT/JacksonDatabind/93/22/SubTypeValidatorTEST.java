package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SubTypeValidatorTest {

    @Mock
    private DeserializationContext deserializationContext;

    @Mock
    private JavaType javaType;

    @Test
    public void testValidateSubType_ValidType_NoExceptionThrown() throws JsonMappingException {
        // Arrange
        SubTypeValidator validator = SubTypeValidator.instance();

        // Act
        validator.validateSubType(deserializationContext, javaType);

        // Assert
        // No exception should be thrown
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_IllegalClass_ExceptionThrown() throws JsonMappingException {
        // Arrange
        SubTypeValidator validator = SubTypeValidator.instance();

        // Mock an illegal class name
        when(javaType.getRawClass()).thenReturn(IllegalTestClass.class);

        // Act
        validator.validateSubType(deserializationContext, javaType);

        // Assert
        // Expect JsonMappingException to be thrown
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_SpringFrameworkClass_ExceptionThrown() throws JsonMappingException {
        // Arrange
        SubTypeValidator validator = SubTypeValidator.instance();

        // Mock a Spring Framework class
        when(javaType.getRawClass()).thenReturn(SpringTestClass.class);

        // Act
        validator.validateSubType(deserializationContext, javaType);

        // Assert
        // Expect JsonMappingException to be thrown
    }

    private static abstract class IllegalTestClass {}

    private static abstract class SpringTestClass {}

}