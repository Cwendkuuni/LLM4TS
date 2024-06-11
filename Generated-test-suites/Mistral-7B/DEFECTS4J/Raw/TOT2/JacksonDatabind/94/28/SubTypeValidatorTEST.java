package com.example.junit4;

import org.junit.Test;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;

public class SubTypeValidatorTest {

    @Test
    public void testInstance() {
        SubTypeValidator actual = SubTypeValidator.instance();
        SubTypeValidator expected = SubTypeValidator.instance();
        assertSame(expected, actual);
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubTypeWithInvalidClass() {
        DeserializationContext context = null; // Initialize it with valid context
        JavaType type = null; // Initialize it with a type that is not in the list of banned classes

        SubTypeValidator validator = new SubTypeValidator();
        try {
            validator.validateSubType(context, type);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected exception
        }
    }

    @Test
    public void testValidateSubTypeWithValidClass() {
        DeserializationContext context = null; // Initialize it with valid context
        JavaType type = null; // Initialize it with a valid class name

        SubTypeValidator validator = new SubTypeValidator();
        validator.validateSubType(context, type);
    }
}