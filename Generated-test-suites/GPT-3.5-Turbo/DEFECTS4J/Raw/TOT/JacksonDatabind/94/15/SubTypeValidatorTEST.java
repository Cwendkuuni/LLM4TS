package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class SubTypeValidatorTest {

    @Test
    public void testValidateSubType_validSubType() {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext context = null;
        JavaType type = null;
        try {
            validator.validateSubType(context, type);
            // If the validation passes, no exception should be thrown
        } catch (JsonMappingException e) {
            fail("Exception should not be thrown for a valid subtype");
        }
    }

    @Test
    public void testValidateSubType_illegalSubType() {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext context = null;
        JavaType type = null;
        try {
            type = JavaType.constructUnsafe(String.class);
            validator.validateSubType(context, type);
            fail("Exception should be thrown for an illegal subtype");
        } catch (JsonMappingException e) {
            assertEquals("Illegal type (java.lang.String) to deserialize: prevented for security reasons",
                    e.getOriginalMessage());
        }
    }
}