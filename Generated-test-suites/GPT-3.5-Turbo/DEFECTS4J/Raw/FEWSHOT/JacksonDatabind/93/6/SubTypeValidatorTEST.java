package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

public class SubTypeValidatorTest {

    @Test
    public void testValidateSubType() {
        // Test when _cfgIllegalClassNames is empty
        SubTypeValidator instance = new SubTypeValidator();
        DeserializationContext ctxt = null; // Use appropriate value
        JavaType type = null; // Use appropriate value
        try {
            instance.validateSubType(ctxt, type);
        } catch (JsonMappingException e) {
            fail("Exception not expected");
        } catch (Exception e) {
            fail("Exception not expected");
        }

        // Test when _cfgIllegalClassNames contains a valid class name
        SubTypeValidator instance = new SubTypeValidator();
        DeserializationContext ctxt = null; // Use appropriate value
        JavaType type = null; // Use appropriate value
        instance._cfgIllegalClassNames.add("java.util.logging.FileHandler");
        try {
            instance.validateSubType(ctxt, type);
            fail("Expected JsonMappingException not thrown");
        } catch (JsonMappingException e) {
            assertEquals("Illegal type (java.util.logging.FileHandler) to deserialize: prevented for security reasons", e.getMessage());
        } catch (Exception e) {
            fail("Expected JsonMappingException not thrown");
        }

        // Test when _cfgIllegalClassNames contains an invalid class name
        SubTypeValidator instance = new SubTypeValidator();
        DeserializationContext ctxt = null; // Use appropriate value
        JavaType type = null; // Use appropriate value
        instance._cfgIllegalClassNames.add("com.example.SomeClass");
        try {
            instance.validateSubType(ctxt, type);
        } catch (JsonMappingException e) {
            fail("Exception not expected");
        } catch (Exception e) {
            fail("Exception not expected");
        }
    }
}