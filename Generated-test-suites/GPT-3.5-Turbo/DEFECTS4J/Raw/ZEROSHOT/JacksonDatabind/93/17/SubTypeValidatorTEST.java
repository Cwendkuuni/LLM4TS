package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SubTypeValidatorTest {

    private SubTypeValidator subTypeValidator;

    @Before
    public void setUp() {
        subTypeValidator = SubTypeValidator.instance();
    }

    @Test
    public void testValidateSubType_validType() {
        // Arrange
        DeserializationContext ctxt = null;
        JavaType type = JavaType.defaultInstance();

        // Act
        try {
            subTypeValidator.validateSubType(ctxt, type);
        } catch (JsonMappingException e) {
            fail("Should not throw an exception");
        }

        // Assert
        // No exception thrown
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_illegalType() throws JsonMappingException {
        // Arrange
        DeserializationContext ctxt = null;
        JavaType type = JavaType.illegalInstance();

        // Act
        subTypeValidator.validateSubType(ctxt, type);

        // Assert
        // JsonMappingException should be thrown
    }
}