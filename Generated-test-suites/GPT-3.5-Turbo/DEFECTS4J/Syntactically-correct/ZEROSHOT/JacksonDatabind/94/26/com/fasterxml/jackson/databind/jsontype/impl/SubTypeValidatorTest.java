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
    public void testValidateSubType() throws JsonMappingException {
        // Test case 1: validateSubType when type is in the list of illegal class names
        try {
            DeserializationContext ctxt = null;
            JavaType type = null;
            subTypeValidator.validateSubType(ctxt, type);
            fail("Expected JsonMappingException to be thrown");
        } catch (JsonMappingException e) {
            assertEquals("Illegal type (java.lang.Object) to deserialize: prevented for security reasons", e.getMessage());
        }
        
        // Test case 2: validateSubType when type is not in the list of illegal class names
        DeserializationContext ctxt = null;
        JavaType type = null;
        // Add the type to the illegal class names list so that it does not throw an exception
        subTypeValidator._cfgIllegalClassNames.add("test.package.Type");
        subTypeValidator.validateSubType(ctxt, type);
        // No exception should be thrown
        
        // Test case 3: validateSubType when type is an interface
        // Add the type to the illegal class names list to check if it gets ignored
        subTypeValidator._cfgIllegalClassNames.add("test.package.InterfaceType");
        // Set type to be an interface
        type = JavaTypeMock.getInterfaceType();
        subTypeValidator.validateSubType(ctxt, type);
        // No exception should be thrown
        
        // Test case 4: validateSubType when type is a Spring framework type
        // Add the type to the illegal class names list to check if it gets ignored
        subTypeValidator._cfgIllegalClassNames.add("test.package.SpringType");
        // Set type to be a Spring framework type
        type = JavaTypeMock.getSpringType();
        subTypeValidator.validateSubType(ctxt, type);
        // No exception should be thrown
    }
}