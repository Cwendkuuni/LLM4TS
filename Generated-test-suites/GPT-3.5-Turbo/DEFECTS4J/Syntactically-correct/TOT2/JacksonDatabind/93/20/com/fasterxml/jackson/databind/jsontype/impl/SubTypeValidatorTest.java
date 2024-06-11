package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Test;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

public class SubTypeValidatorTest
{
    @Test
    public void testValidateSubTypeValid() throws JsonMappingException
    {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        JavaType type = new JavaType(String.class);
        validator.validateSubType(ctxt, type);
    }

    @Test
    public void testValidateSubTypeIllegal() throws JsonMappingException
    {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        JavaType type = new JavaType(org.apache.commons.collections.functors.InvokerTransformer.class);
        
        try {
            validator.validateSubType(ctxt, type);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Illegal type (org.apache.commons.collections.functors.InvokerTransformer) to deserialize: prevented for security reasons", e.getMessage());
        }
    }
}