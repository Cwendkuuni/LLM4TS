package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Test;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

public class SubTypeValidatorTest {
    
    @Test
    public void testValidateSubType_ValidType() {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext context = null; // provide a valid DeserializationContext object
        JavaType type = null; // provide a valid JavaType object
        
        try {
            validator.validateSubType(context, type);
        } catch (JsonMappingException e) {
            fail("Unexpected JsonMappingException");
        }
    }
    
    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_InvalidType() throws JsonMappingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext context = null; // provide a valid DeserializationContext object
        JavaType type = null; // provide an invalid JavaType object
        
        validator.validateSubType(context, type);
    }
    
    @Test
    public void testValidateSubType_SecurityReasons() {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext context = null; // provide a valid DeserializationContext object
        JavaType type = null; // provide a JavaType object with an illegal type
        
        try {
            validator.validateSubType(context, type);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Illegal type", e.getMessage());
        }
    }
}