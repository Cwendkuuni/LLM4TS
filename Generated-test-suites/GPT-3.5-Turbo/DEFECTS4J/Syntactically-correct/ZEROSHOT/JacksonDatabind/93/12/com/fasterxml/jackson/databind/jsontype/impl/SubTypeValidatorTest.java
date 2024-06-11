package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.Test;
import java.util.Set;
import static org.junit.Assert.*;

public class SubTypeValidatorTest {

    @Test
    public void testValidateSubType() throws JsonMappingException {
        SubTypeValidator validator = SubTypeValidator.instance();

        // Test valid subtypes
        DeserializationContext ctxt = null;
        JavaType type = null;
        assertDoesNotThrow(() -> validator.validateSubType(ctxt, type));

        // Test invalid subtypes
        try {
            validator.validateSubType(ctxt, type);
            fail("Expected JsonMappingException to be thrown");
        } catch (JsonMappingException e) {
            assertEquals("Illegal type (java.lang.String) to deserialize: prevented for security reasons", e.getMessage());
        }
    }
}