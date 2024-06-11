package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class SubTypeValidatorTest {
    @Mock
    private DeserializationContext deserializationContext;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testValidateSubType_withValidClass() throws JsonMappingException {
        SubTypeValidator instance = SubTypeValidator.instance();
        JavaType type = JavaType.fromClass(Object.class);

        try {
            instance.validateSubType(deserializationContext, type);
        } catch (JsonMappingException e) {
            fail("Unexpected JsonMappingException");
        }
    }

    @Test
    public void testValidateSubType_withIllegalClass() {
        SubTypeValidator instance = SubTypeValidator.instance();
        JavaType type = JavaType.fromClass(TemplatesImpl.class);

        try {
            instance.validateSubType(deserializationContext, type);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertTrue(e.getMessage().contains("Illegal type"));
        }
    }

    @Test
    public void testValidateSubType_withSpringInterface() throws JsonMappingException {
        SubTypeValidator instance = SubTypeValidator.instance();
        JavaType type = JavaType.fromClass(AbstractApplicationContext.class);

        try {
            instance.validateSubType(deserializationContext, type);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertTrue(e.getMessage().contains("Illegal type"));
        }
    }
}