
package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class SubTypeValidatorTest {

    private final SubTypeValidator validator = SubTypeValidator.instance();

    @Test
    public void testValidateSubType() {
        try {
            // Test valid class
            validator.validateSubType(null, JavaType.fromClass(SubTypeValidator.class));
        } catch (JsonMappingException e) {
            fail("Should not throw an exception for a valid class");
        }

        try {
            // Test class in DEFAULT_NO_DESER_CLASS_NAMES
            validator.validateSubType(null, JavaType.fromClass(
                    org.apache.commons.collections.functors.InvokerTransformer.class));
            fail("Should throw JsonMappingException for a class in DEFAULT_NO_DESER_CLASS_NAMES");
        } catch (JsonMappingException e) {
            assertEquals("Illegal type (" +
                    org.apache.commons.collections.functors.InvokerTransformer.class.getName() +
                    ") to deserialize: prevented for security reasons", e.getMessage());
        }
    }
}
