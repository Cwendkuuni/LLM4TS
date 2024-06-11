package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

public class SubTypeValidatorTest {
    
    @Test
    public void testValidateSubType_edgeCases() throws JsonMappingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = null;
        JavaType type = null;
        
        // Edge case: type is null
        try {
            validator.validateSubType(ctxt, type);
            fail("Expected exception to be thrown for null type");
        } catch (JsonMappingException e) {
            // Expected
        }
        
        // Edge case: type is a well-known "nasty class"
        type = JavaType.construct(null, SubTypeValidator.PREFIX_STRING + "org.apache.commons.collections.functors.InvokerTransformer", null, null);
        try {
            validator.validateSubType(ctxt, type);
            fail("Expected exception to be thrown for well-known nasty class");
        } catch (JsonMappingException e) {
            // Expected
        }
        
        // Edge case: type is a Spring framework class to be blocked
        type = JavaType.construct(null, SubTypeValidator.PREFIX_STRING + "org.springframework.beans.factory.ObjectFactory", null, null);
        try {
            validator.validateSubType(ctxt, type);
            fail("Expected exception to be thrown for Spring framework class to be blocked");
        } catch (JsonMappingException e) {
            // Expected
        }
    }
}