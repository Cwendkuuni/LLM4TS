package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Test;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

public class SubTypeValidatorTest {

    @Test
    public void testValidateSubType() throws JsonMappingException {
        // Test case 1: Typical use case
        DeserializationContext ctxt = null;
        JavaType type = null;
        try {
            SubTypeValidator.instance().validateSubType(ctxt, type);
        } catch (JsonMappingException e) {
            fail("Should not throw JsonMappingException for typical use case");
        }

        // Test case 2: Edge case - Empty class name
        type = new JavaType(null, null, 0, null, null, null, false, false, null, null);
        try {
            SubTypeValidator.instance().validateSubType(ctxt, type);
            fail("Should throw JsonMappingException for empty class name");
        } catch (JsonMappingException e) {
            assertEquals("Illegal type () to deserialize: prevented for security reasons", e.getMessage());
        }

        // Test case 3: Error scenario - Invalid class name
        type = new JavaType(null, null, 0, null, null, "com.acme.InvalidClass", false, false, null, null);
        try {
            SubTypeValidator.instance().validateSubType(ctxt, type);
            fail("Should throw JsonMappingException for invalid class name");
        } catch (JsonMappingException e) {
            assertEquals("Illegal type (com.acme.InvalidClass) to deserialize: prevented for security reasons", e.getMessage());
        }
    }
}