package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import org.junit.Test;

public class SubTypeValidatorTest {

    @Test
    public void testValidateSubType_NormalCase() {
        // Normal case: validateSubType should not throw any exceptions for valid type
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = null; // create a mock DeserializationContext
        JavaType type = JavaType.construct(String.class);
        
        try {
            validator.validateSubType(ctxt, type);
        } catch (JsonMappingException e) {
            fail("Exception thrown for valid type: " + e.getMessage());
        }
    }

    @Test
    public void testValidateSubType_NullInput() {
        // Edge case: validateSubType should throw NullPointerException for null input
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = null; // create a mock DeserializationContext
        JavaType type = null;
        
        try {
            validator.validateSubType(ctxt, type);
            fail("NullPointerException not thrown for null input");
        } catch (NullPointerException e) {
            // NullPointerException should be caught
        } catch (JsonMappingException e) {
            fail("Unexpected exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testValidateSubType_IllegalType() {
        // Error scenario: validateSubType should throw JsonMappingException for illegal type
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = null; // create a mock DeserializationContext
        JavaType type = JavaType.construct(Object.class);
        
        try {
            validator.validateSubType(ctxt, type);
            fail("JsonMappingException not thrown for illegal type");
        } catch (JsonMappingException e) {
            // JsonMappingException should be caught
            assertEquals("Illegal type (java.lang.Object) to deserialize: prevented for security reasons", e.getMessage());
        }
    }

}