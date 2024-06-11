package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

/**
 * Test class for SubTypeValidator
 *
 * @author John Doe
 */
public class SubTypeValidatorTest {

    private SubTypeValidator subTypeValidator;

    @Before
    public void setup() {
        subTypeValidator = SubTypeValidator.instance();
    }

    @Test
    public void testValidateSubTypeWithValidType() throws JsonMappingException {
        DeserializationContext context = mock(DeserializationContext.class);
        JavaType type = mock(JavaType.class);

        subTypeValidator.validateSubType(context, type);
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubTypeWithInvalidType() throws JsonMappingException, JsonProcessingException {
        DeserializationContext context = mock(DeserializationContext.class);
        JavaType type = JavaType.construct(SubTypeValidatorTest.class, String.class);

        subTypeValidator.validateSubType(context, type);
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubTypeWithSpringClass() throws JsonMappingException, JsonProcessingException {
        DeserializationContext context = mock(DeserializationContext.class);
        JavaType type = JavaType.construct(Object.class, "org.springframework.beans.factory.config.PropertyPathFactoryBean");

        subTypeValidator.validateSubType(context, type);
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubTypeWithNastyClass() throws JsonMappingException, JsonProcessingException {
        DeserializationContext context = mock(DeserializationContext.class);
        JavaType type = JavaType.construct(Object.class, "org.apache.commons.collections4.functors.InstantiateTransformer");

        subTypeValidator.validateSubType(context, type);
    }
}