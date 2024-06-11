package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

public class SubTypeValidatorTest {
    
    private SubTypeValidator validator;
    private DeserializationContext mockContext;
    private JavaType mockType;
    
    @Before
    public void setUp() {
        validator = SubTypeValidator.instance();
        mockContext = new DeserializationContext(null, null, null);
        mockType = new JavaType(null, null, null);
    }
    
    @Test
    public void testValidateSubType_ValidType() {
        try {
            validator.validateSubType(mockContext, mockType);
        } catch (JsonMappingException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testValidateSubType_InvalidType() {
        mockType.setRawClass(Object.class);
        try {
            validator.validateSubType(mockContext, mockType);
            fail("Expected JsonMappingException was not thrown");
        } catch (JsonMappingException e) {
            assertEquals("Illegal type (java.lang.Object) to deserialize: prevented for security reasons", e.getMessage());
        }
    }
}