
package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

public class SubTypeValidatorTest {

    @Test
    public void testValidateSubType() throws JsonMappingException {
        DeserializationContext ctxt = null;
        JavaType type = null;
        
        // Testing valid type
        try {
            type = new JavaType(SubTypeValidator.class);
            SubTypeValidator.instance().validateSubType(ctxt, type);
            assertTrue(true);
        } catch (JsonMappingException e) {
            fail("Should not throw exception for a valid type");
        }
        
        // Testing illegal type
        try {
            type = new JavaType(org.springframework.beans.factory.ObjectFactory.class);
            SubTypeValidator.instance().validateSubType(ctxt, type);
            fail("Should throw exception for an illegal type");
        } catch (JsonMappingException e) {
            assertEquals("Illegal type (org.springframework.beans.factory.ObjectFactory) to deserialize: prevented for security reasons", e.getMessage());
        }
    }
}
