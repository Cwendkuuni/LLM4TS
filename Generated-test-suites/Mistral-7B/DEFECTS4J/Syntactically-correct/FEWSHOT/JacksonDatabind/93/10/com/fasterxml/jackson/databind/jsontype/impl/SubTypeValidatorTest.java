package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;

import java.util.Collections;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

/**
 * Test class for SubTypeValidator
 */
@RunWith(MockitoJUnitRunner.class)
public class SubTypeValidatorTest {

    private DeserializationContext context;
    private JavaType type;

    @Test
    public void testValidSubType() {
        context = Mockito.mock(DeserializationContext.class);
        type = Mockito.mock(JavaType.class);

        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(context, type);
        // No exception should be thrown
    }

    @Test
    public void testInvalidSubTypeWithPrefixString() {
        context = Mockito.mock(DeserializationContext.class);
        type = Mockito.mock(JavaType.class);

        String fullName = "org.springframework.AbstractApplicationContext";
        Mockito.when(type.getRawClass().getName()).thenReturn(fullName);

        SubTypeValidator validator = SubTypeValidator.instance();
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(context, type));
    }

    @Test
    public void testInvalidSubType() {
        context = Mockito.mock(DeserializationContext.class);
        type = Mockito.mock(JavaType.class);

        String fullName = "org.apache.commons.collections4.functors.InstantiateTransformer";
        Mockito.when(type.getRawClass().getName()).thenReturn(fullName);

        SubTypeValidator validator = SubTypeValidator.instance();
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(context, type));
    }
}