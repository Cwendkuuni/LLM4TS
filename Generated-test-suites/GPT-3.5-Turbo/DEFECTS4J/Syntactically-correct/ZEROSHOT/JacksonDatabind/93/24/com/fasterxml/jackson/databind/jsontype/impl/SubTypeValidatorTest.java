package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class SubTypeValidatorTest {

    private SubTypeValidator subTypeValidator;
    private DeserializationContext deserializationContext;
    private JavaType javaType;

    @Before
    public void setUp() throws Exception {
        subTypeValidator = SubTypeValidator.instance();
        deserializationContext = new DeserializationContext(null, null, null, null, null);
        javaType = new JavaType();
    }

    @Test
    public void validateSubType_validSubType_noExceptionThrown() throws JsonMappingException {
        DeserializationContext ctxt = deserializationContext;
        JavaType type = javaType;

        subTypeValidator.validateSubType(ctxt, type);

        assertTrue(true);
    }

    @Test
    public void validateSubType_illegalSubType_exceptionThrown() throws JsonMappingException {
        DeserializationContext ctxt = deserializationContext;
        JavaType type = new JavaType(IllegalSubType.class);

        try {
            subTypeValidator.validateSubType(ctxt, type);
            fail("Expected JsonMappingException to be thrown");
        } catch (JsonMappingException e) {
            assertEquals("Illegal type (com.fasterxml.jackson.databind.jsontype.impl.SubTypeValidatorTest$IllegalSubType) to deserialize: prevented for security reasons", e.getMessage());
        }
    }

    static class IllegalSubType {
        // define illegal subtype
    }
}