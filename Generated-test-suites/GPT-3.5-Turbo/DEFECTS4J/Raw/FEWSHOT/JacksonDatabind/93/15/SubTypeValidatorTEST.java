package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.Before;
import org.junit.Test;

public class SubTypeValidatorTest {

    private SubTypeValidator subTypeValidator;
    private DeserializationContext deserializationContext;
    private JavaType javaType;

    @Before
    public void setUp() {
        subTypeValidator = SubTypeValidator.instance();
        deserializationContext = mock(DeserializationContext.class);
        javaType = mock(JavaType.class);
    }

    @Test
    public void testValidateSubTypeWithValidType() throws JsonMappingException {
        assertFalse(subTypeValidator.validateSubType(deserializationContext, javaType));
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubTypeWithInvalidType() throws JsonMappingException {
        subTypeValidator.validateSubType(deserializationContext, javaType);
    }
}