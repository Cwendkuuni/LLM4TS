package com.example.subtypevalidator.test;

import org.junit.Test;
import org.mockito.Mockito;
import java.util.Arrays;

public class SubTypeValidatorTest {
    // Existing test cases

    @Test
    public void testValidateSubTypeWithLegalSpringClass() {
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        JavaType javaType = Mockito.mock(JavaType.class);
        when(javaType.getName()).thenReturn("org.springframework.beans.factory.config.PropertyPathFactoryBean");
        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(context, javaType);
        // Add assertions if necessary
    }

    @Test
    public void testValidateSubTypeWithLegalNonSpringClass() {
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        JavaType javaType = Mockito.mock(JavaType.class);
        when(javaType.getName()).thenReturn("com.example.MyLegalClass");
        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(context, javaType);
        // Add assertions if necessary
    }
}