package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.DeserializationContext;

public class SubTypeValidatorTest {

    private SubTypeValidator subTypeValidator;
    private DeserializationContext deserializationContextMock;
    private JavaType javaTypeMock;

    @Before
    public void setUp() {
        subTypeValidator = SubTypeValidator.instance();
        deserializationContextMock = Mockito.mock(DeserializationContext.class);
        javaTypeMock = Mockito.mock(JavaType.class);
    }

    @Test
    public void testValidateSubType_ValidSubType_NoExceptionThrown() {
        try {
            subTypeValidator.validateSubType(deserializationContextMock, javaTypeMock);
        } catch (JsonMappingException e) {
            fail("Exception should not be thrown for valid subtype");
        }
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_InvalidSubType_ExceptionThrown() throws JsonMappingException {
        subTypeValidator.validateSubType(deserializationContextMock, javaTypeMock);
    }
}