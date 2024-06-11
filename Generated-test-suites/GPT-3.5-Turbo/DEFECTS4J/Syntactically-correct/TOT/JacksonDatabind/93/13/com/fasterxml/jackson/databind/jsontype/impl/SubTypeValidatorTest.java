package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

public class SubTypeValidatorTest {

    private DeserializationContext ctxt;
    private JavaType validType;
    private JavaType invalidType;
    
    private SubTypeValidator validator;
    
    @Before
    public void setup() {
        ctxt = null; // Set the deserialization context as per requirement
        validType = JavaType.builder().classType(ValidClass.class).build();
        invalidType = JavaType.builder().classType(InvalidClass.class).build();
        validator = SubTypeValidator.instance();
    }
    
    @Test
    public void testValidateSubType_InvalidClass_ThrowsException() {
        try {
            validator.validateSubType(ctxt, invalidType);
            fail("Expected JsonMappingException to be thrown");
        } catch (JsonMappingException e) {
            assertEquals("Illegal type (com.fasterxml.jackson.databind.jsontype.impl.InvalidClass) to deserialize: prevented for security reasons", e.getMessage());
        }
    }
    
    @Test
    public void testValidateSubType_ValidClass_DoesNotThrowException() {
        try {
            validator.validateSubType(ctxt, validType);
        } catch (JsonMappingException e) {
            fail("Expected no JsonMappingException to be thrown");
        }
    }
    
    // Edge case where the class starts with the prefix string, but it is not an invalid class
    @Test
    public void testValidateSubType_ClassWithPrefixString_DoesNotThrowException() {
        JavaType typeWithPrefix = JavaType.builder().classType(PrefixStringClass.class).build();
        try {
            validator.validateSubType(ctxt, typeWithPrefix);
        } catch (JsonMappingException e) {
            fail("Expected no JsonMappingException to be thrown");
        }
    }
    
    // Error scenario where the deserialization context is null
    @Test
    public void testValidateSubType_NullDeserializationContext_ThrowsException() {
        try {
            validator.validateSubType(null, validType);
            fail("Expected JsonMappingException to be thrown");
        } catch (JsonMappingException e) {
            assertEquals("Illegal type (com.fasterxml.jackson.databind.jsontype.impl.ValidClass) to deserialize: prevented for security reasons", e.getMessage());
        }
    }
}