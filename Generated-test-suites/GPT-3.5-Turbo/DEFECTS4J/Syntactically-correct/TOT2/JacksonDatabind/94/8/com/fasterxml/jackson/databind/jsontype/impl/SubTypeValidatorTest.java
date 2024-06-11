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
        DeserializationContext ctxt = new DeserializationContext(null);
        JavaType type = new JavaType(int.class);
        try {
            SubTypeValidator.instance().validateSubType(ctxt, type);
        } catch (JsonMappingException e) {
            fail("Unexpected exception thrown");
        }
    }

    @Test
    public void testValidateSubType_IllegalType() {
        DeserializationContext ctxt = new DeserializationContext(null);
        JavaType type = new JavaType("org.apache.commons.collections.functors.InvokerTransformer");
        try {
            SubTypeValidator.instance().validateSubType(ctxt, type);
            fail("Expected JsonMappingException to be thrown");
        } catch (JsonMappingException e) {
            assertEquals("Illegal type (org.apache.commons.collections.functors.InvokerTransformer) to deserialize: prevented for security reasons", e.getMessage());
        }
    }

    @Test
    public void testValidateSubType_SpringType() {
        DeserializationContext ctxt = new DeserializationContext(null);
        JavaType type = new JavaType("org.springframework.beans.factory.ObjectFactory");
        try {
            SubTypeValidator.instance().validateSubType(ctxt, type);
            fail("Expected JsonMappingException to be thrown");
        } catch (JsonMappingException e) {
            assertEquals("Illegal type (org.springframework.beans.factory.ObjectFactory) to deserialize: prevented for security reasons", e.getMessage());
        }
    }

}