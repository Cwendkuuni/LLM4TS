
package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

public class SubTypeValidatorTest {
    private SubTypeValidator validator;

    @Before
    public void setUp() {
        validator = SubTypeValidator.instance();
    }

    @Test
    public void testValidateSubType() throws JsonMappingException {
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        JavaType type = JavaType.defaultType();

        // Test for valid type
        validator.validateSubType(ctxt, type);

        // Test for invalid type
        type = JavaType.invalidType();
        try {
            validator.validateSubType(ctxt, type);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException ex) {
            assertEquals("Illegal type (com.test.InvalidType) to deserialize: prevented for security reasons",
                    ex.getMessage());
        }
    }
}
