package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Assert;
import org.junit.Test;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

public class SubTypeValidatorTest {

    @Test
    public void testValidateSubType_ValidType_NoException() {
        // Arrange
        SubTypeValidator subTypeValidator = SubTypeValidator.instance();
        DeserializationContext deserializationContext = new DeserializationContext();
        JavaType type = new JavaType();

        // Act
        try {
            subTypeValidator.validateSubType(deserializationContext, type);
            // Assert
            Assert.assertTrue(true);
        } catch (JsonMappingException e) {
            // Assert
            Assert.fail("Exception should not be thrown for a valid type");
        }
    }

    @Test
    public void testValidateSubType_IllegalType_ThrowsJsonMappingException() {
        // Arrange
        SubTypeValidator subTypeValidator = SubTypeValidator.instance();
        DeserializationContext deserializationContext = new DeserializationContext();
        JavaType type = new JavaType();

        // Act
        try {
            subTypeValidator.validateSubType(deserializationContext, type);
            // Assert
            Assert.fail("Exception should be thrown for an illegal type");
        } catch (JsonMappingException e) {
            // Assert
            Assert.assertEquals("Illegal type (java.lang.Object) to deserialize: prevented for security reasons", e.getMessage());
        }
    }
}