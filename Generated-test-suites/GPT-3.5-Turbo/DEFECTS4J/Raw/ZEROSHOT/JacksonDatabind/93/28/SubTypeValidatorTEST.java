package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

public class SubTypeValidatorTest {

    private SubTypeValidator subTypeValidator;

    @Before
    public void setUp() {
        subTypeValidator = SubTypeValidator.instance();
    }

    @Test
    public void testValidateSubType_InvalidClass() throws JsonMappingException {
        DeserializationContext ctxt = null;
        JavaType type = null;

        try {
            subTypeValidator.validateSubType(ctxt, type);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException ex) {
            assertEquals("Illegal type (null) to deserialize: prevented for security reasons", ex.getMessage());
        }
    }

    @Test
    public void testValidateSubType_ValidClass() throws JsonMappingException {
        DeserializationContext ctxt = null;
        JavaType type = new JavaType(Object.class);

        subTypeValidator.validateSubType(ctxt, type);
    }

    @Test
    public void testValidateSubType_IllegalClass() throws JsonMappingException {
        DeserializationContext ctxt = null;
        JavaType type = new JavaType(AbstractApplicationContext.class);

        try {
            subTypeValidator.validateSubType(ctxt, type);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException ex) {
            assertEquals("Illegal type (org.springframework.context.support.AbstractApplicationContext) to deserialize: prevented for security reasons", ex.getMessage());
        }
    }
}