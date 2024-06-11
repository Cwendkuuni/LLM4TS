package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.Test;

import static org.junit.Assert.*;

public class SubTypeValidatorTest {

    @Test
    public void testValidateSubType_ValidType() throws JsonMappingException {
        SubTypeValidator instance = SubTypeValidator.instance();
        DeserializationContext ctxt = null; // mock DeserializationContext object
        JavaType type = null; // mock JavaType object
        instance.validateSubType(ctxt, type);
        // Assert no exception is thrown
    }

    @Test
    public void testValidateSubType_IllegalType() {
        SubTypeValidator instance = SubTypeValidator.instance();
        DeserializationContext ctxt = null; // mock DeserializationContext object
        JavaType type = null; // mock JavaType object
        try {
            instance.validateSubType(ctxt, type);
            fail("JsonMappingException should have been thrown");
        } catch (JsonMappingException e) {
            // Assert JsonMappingException is thrown
            assertTrue(e.getMessage().contains("Illegal type"));
        }
    }

}