package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.Before;
import org.junit.Test;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import static org.junit.Assert.*;

public class SubTypeValidatorTest {
    
    private SubTypeValidator subTypeValidator;
    private DeserializationContext deserializationContext;
    private JavaType javaType;
    
    @Before
    public void setUp() {
        subTypeValidator = SubTypeValidator.instance();
        deserializationContext = new DeserializationContext();
        javaType = new JavaType();
    }

    @Test
    public void testValidateSubType() {
        try {
            subTypeValidator.validateSubType(deserializationContext, javaType);
            fail("Expected an exception to be thrown");
        } catch (JsonMappingException e) {
            assertEquals("Illegal type (java.util.JavaType) to deserialize: prevented for security reasons", e.getMessage());
        }
    }
}