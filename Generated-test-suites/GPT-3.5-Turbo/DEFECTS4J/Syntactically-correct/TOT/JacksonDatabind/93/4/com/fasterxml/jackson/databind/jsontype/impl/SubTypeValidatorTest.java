package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

public class SubTypeValidatorTest {

    @Test
    public void testValidateSubType_typicalUseCase() {
        DeserializationContext ctxt = null; // dummy value
        JavaType type = null; // dummy value

        SubTypeValidator validator = SubTypeValidator.instance();

        try {
            validator.validateSubType(ctxt, type);
            // If validation passes, there should be no exception thrown
        } catch (JsonMappingException e) {
            Assert.fail("Validation threw an unexpected JsonMappingException");
        }
    }

    @Test
    public void testValidateSubType_edgeCase() {
        DeserializationContext ctxt = null; // dummy value
        // Use an abstract class as the type
        JavaType type = JavaType.forType(AbstractClass.class);

        SubTypeValidator validator = SubTypeValidator.instance();

        try {
            validator.validateSubType(ctxt, type);
            Assert.fail("Validation did not throw an expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected exception because an abstract class should not be deserialized
        }
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_errorScenario() throws JsonMappingException {
        DeserializationContext ctxt = null; // dummy value
        JavaType type = JavaType.forType(ErrorClass.class);

        SubTypeValidator validator = SubTypeValidator.instance();

        validator.validateSubType(ctxt, type);
        // Expected exception because ErrorClass is in the default set of illegal class names
    }
}