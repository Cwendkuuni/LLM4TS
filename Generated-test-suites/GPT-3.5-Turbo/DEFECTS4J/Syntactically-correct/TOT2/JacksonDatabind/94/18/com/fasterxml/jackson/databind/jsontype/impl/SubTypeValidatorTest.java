package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Test;
import static org.junit.Assert.*;

public class SubTypeValidatorTest {

    @Test
    public void testValidateSubType() {
        SubTypeValidator validator = SubTypeValidator.instance();

        // Typical use case
        try {
            validator.validateSubType(null, JavaType);
        } catch (JsonMappingException e) {
            fail("Unexpected JsonMappingException for valid type");
        }

        // Edge cases
        try {
            validator.validateSubType(null, null);
            fail("Expected JsonMappingException for null type");
        } catch (JsonMappingException e) {
            // Success
        }

        try {
            validator.validateSubType(null, String.class);
        } catch (JsonMappingException e) {
            fail("Unexpected JsonMappingException for non-JsonType");
        }

        try {
            validator.validateSubType(null, Class.forName("org.apache.commons.collections.functors.InvokerTransformer"));
            fail("Expected JsonMappingException for illegal type");
        } catch (JsonMappingException e) {
            // Success
        }
    }
}