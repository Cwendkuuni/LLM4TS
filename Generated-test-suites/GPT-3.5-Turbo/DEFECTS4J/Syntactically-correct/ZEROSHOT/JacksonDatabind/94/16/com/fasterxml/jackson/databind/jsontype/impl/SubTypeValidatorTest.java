package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Test;
import static org.junit.Assert.*;

public class SubTypeValidatorTest {

    @Test
    public void testValidateSubType() {
        // Test case 1: Valid sub type
        DeserializationContext ctxt1 = new DeserializationContext();
        JavaType type1 = new JavaType();
        try {
            SubTypeValidator.instance().validateSubType(ctxt1, type1);
        } catch (JsonMappingException e) {
            fail("Unexpected exception");
        }
        
        // Test case 2: Invalid sub type
        DeserializationContext ctxt2 = new DeserializationContext();
        JavaType type2 = new JavaType();
        type2.setRawClass("org.apache.commons.collections.functors.InvokerTransformer");
        try {
            SubTypeValidator.instance().validateSubType(ctxt2, type2);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Illegal type (org.apache.commons.collections.functors.InvokerTransformer) to deserialize: prevented for security reasons", e.getMessage());
        }
    }
}