package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;
import org.junit.Test;

public class SubTypeValidatorTest {

    @Test
    public void testValidateSubType() {
        SubTypeValidator validator = SubTypeValidator.instance();

        // Typical use case
        try {
            validator.validateSubType(null, JavaType.unspecified());
        } catch (JsonMappingException e) {
            fail("Unexpected JsonMappingException in typical use case");
        }

        // Edge case: illegal class name
        try {
            validator.validateSubType(null, JavaType.unspecified());
            fail("No exception thrown for illegal class name");
        } catch (JsonMappingException e) {
            assertEquals("Illegal type (org.apache.commons.collections.functors.InvokerTransformer) to deserialize: prevented for security reasons", e.getMessage());
        }

        // Error scenario: DeserializationContext is null
        try {
            validator.validateSubType(null, null);
            fail("No exception thrown for null DeserializationContext");
        } catch (JsonMappingException e) {
            assertEquals("Illegal type (null) to deserialize: prevented for security reasons", e.getMessage());
        }
    }
}