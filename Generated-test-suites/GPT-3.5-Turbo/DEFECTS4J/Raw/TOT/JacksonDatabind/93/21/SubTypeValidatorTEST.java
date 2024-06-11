
package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.Assert;
import org.junit.Test;

public class SubTypeValidatorTest
{
    @Test
    public void testValidateSubType_InvalidClass() {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext context = new DeserializationContext(null, null, null);

        // Attempt to validate an invalid class
        try {
            validator.validateSubType(context, JavaType.construct(String.class));
        } catch (JsonMappingException e) {
            Assert.assertEquals("Illegal type (java.lang.String) to deserialize: prevented for security reasons", e.getMessage());
        }
    }

    @Test
    public void testValidateSubType_ValidClass() {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext context = new DeserializationContext(null, null, null);

        // Attempt to validate a valid class
        try {
            validator.validateSubType(context, JavaType.construct(Integer.class));
        } catch (JsonMappingException e) {
            Assert.fail("Unexpected JsonMappingException");
        }
    }
}
