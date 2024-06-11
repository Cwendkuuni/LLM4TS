package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

public class SubTypeValidatorTest {
    
    private SubTypeValidator validator = SubTypeValidator.instance();
    
    @Test
    public void testValidateSubTypeWithValidType() throws JsonMappingException {
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        JavaType type = new JavaType(TestObject.class);
        
        try {
            validator.validateSubType(ctxt, type);
        } catch (JsonMappingException e) {
            fail("Unexpected JsonMappingException");
        }
    }
    
    @Test(expected = JsonMappingException.class)
    public void testValidateSubTypeWithIllegalType() throws JsonMappingException {
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        JavaType type = new JavaType(IllegalObject.class);
        
        validator.validateSubType(ctxt, type);
    }
    
    private static class TestObject {
    }
    
    private static class IllegalObject {
    }
}