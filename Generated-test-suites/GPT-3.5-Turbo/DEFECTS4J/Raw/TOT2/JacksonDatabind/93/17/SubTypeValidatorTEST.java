package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;
import org.junit.Test;

public class SubTypeValidatorTest {

    @Test
    public void testInstance() {
        SubTypeValidator validator = SubTypeValidator.instance();
        assertNotNull(validator);
    }

    @Test
    public void testValidateSubTypeValid() {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = null; // pass in a valid DeserializationContext object
        JavaType type = null; // pass in a valid JavaType object
        assertDoesNotThrow(() -> validator.validateSubType(ctxt, type));
    }

    @Test
    public void testValidateSubTypeInvalid() {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = null; // pass in a valid DeserializationContext object
        JavaType type = null; // pass in a JavaType object with invalid class name
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(ctxt, type));
    }
}