package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class SubTypeValidatorTest {
    private SubTypeValidator subTypeValidator;

    @Before
    public void setUp() {
        subTypeValidator = SubTypeValidator.instance();
    }

    @Test
    public void testValidateSubType_ValidType() throws JsonMappingException {
        DeserializationContext ctxt = null; // Initialize with necessary parameters
        JavaType type = null; // Initialize with necessary parameters

        try {
            subTypeValidator.validateSubType(ctxt, type);
        } catch (JsonMappingException e) {
            fail("Should not throw an exception");
        }
    }

    @Test
    public void testValidateSubType_IllegalClass() {
        DeserializationContext ctxt = null; // Initialize with necessary parameters
        JavaType type = null; // Initialize with necessary parameters

        try {
            subTypeValidator.validateSubType(ctxt, type);
            fail("Should throw a JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Illegal type (full class name) to deserialize: prevented for security reasons",
                    e.getMessage());
        }
    }
}