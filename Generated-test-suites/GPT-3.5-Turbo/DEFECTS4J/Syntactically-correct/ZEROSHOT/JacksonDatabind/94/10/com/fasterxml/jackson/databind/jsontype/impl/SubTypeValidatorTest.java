package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Assert;
import org.junit.Test;

public class SubTypeValidatorTest {

    @Test
    public void testValidateSubType() {
        // Test 1: Valid subType, should not throw exception
        SubTypeValidator validator = SubTypeValidator.instance();
        try {
            validator.validateSubType(null, new JavaType() {
                @Override
                public Class<?> getRawClass() {
                    return Object.class;
                }
            });
        } catch (Exception e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        // Test 2: Invalid subType, should throw exception
        try {
            validator.validateSubType(null, new JavaType() {
                @Override
                public Class<?> getRawClass() {
                    return SubTypeValidatorTest.class;
                }
            });
            Assert.fail("Expected exception was not thrown");
        } catch (Exception e) {
            String expectedErrorMessage = "Illegal type (com.fasterxml.jackson.databind.jsontype.impl.SubTypeValidatorTest) to deserialize: prevented for security reasons";
            Assert.assertEquals(expectedErrorMessage, e.getMessage());
        }
    }
}