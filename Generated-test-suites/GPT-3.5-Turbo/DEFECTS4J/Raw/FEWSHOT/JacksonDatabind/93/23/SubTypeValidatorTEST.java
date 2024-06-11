package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

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
    public void testValidateSubType_WithValidClass() {
        try {
            subTypeValidator.validateSubType(deserializationContext, javaType);
        } catch (JsonMappingException e) {
            fail("Should not throw an exception");
        }
    }

    @Test
    public void testValidateSubType_WithIllegalClass() {
        try {
            subTypeValidator.validateSubType(deserializationContext, javaType);
            fail("Should throw an exception");
        } catch (JsonMappingException e) {
            assertEquals("Illegal type (org.springframework.beans.factory.ObjectFactory) to deserialize: prevented for security reasons", e.getMessage());
        }
    }
}