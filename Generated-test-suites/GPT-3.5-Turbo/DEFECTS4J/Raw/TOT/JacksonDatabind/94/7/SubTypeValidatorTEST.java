package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SubTypeValidatorTest {

    private SubTypeValidator validator;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        validator = SubTypeValidator.instance();
    }

    @Test
    public void testValidateSubType_withValidClass_shouldNotThrowException() {
        try {
            validator.validateSubType(new DeserializationContext(), new JavaType(Object.class));
        } catch (JsonMappingException e) {
            fail("Unexpected JsonMappingException");
        }
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_withIllegalClass_shouldThrowException() throws JsonMappingException {
        validator.validateSubType(new DeserializationContext(), new JavaType(Security.class));
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_withNastyClass_shouldThrowException() throws JsonMappingException {
        validator.validateSubType(new DeserializationContext(), new JavaType(NastyClass.class));
    }
}