package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class SubTypeValidatorTest {

    private SubTypeValidator subTypeValidator;

    @Before
    public void setUp() {
        subTypeValidator = SubTypeValidator.instance();
    }

    @Test
    public void validateSubType_WithValidType_ShouldNotThrowException() {
        try {
            DeserializationContext deserializationContext = new DeserializationContext();
            JavaType javaType = new JavaType();
            subTypeValidator.validateSubType(deserializationContext, javaType);
        } catch (JsonMappingException e) {
            fail("Should not throw any exception");
        }
    }
    
    @Test
    public void validateSubType_WithIllegalType_ShouldThrowJsonMappingException() {
        try {
            DeserializationContext deserializationContext = new DeserializationContext();
            JavaType javaType = new JavaType();
            javaType.setRawClass(HashSet.class);
            subTypeValidator.validateSubType(deserializationContext, javaType);
            fail("Should throw JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Illegal type (java.util.HashSet) to deserialize: prevented for security reasons", e.getMessage());
        }
    }
    
    @Test
    public void validateSubType_WithUnsupportedInterface_ShouldNotThrowException() {
        try {
            DeserializationContext deserializationContext = new DeserializationContext();
            JavaType javaType = new JavaType();
            javaType.setRawClass(Set.class);
            subTypeValidator.validateSubType(deserializationContext, javaType);
        } catch (JsonMappingException e) {
            fail("Should not throw any exception");
        }
    }
    
    @Test
    public void validateSubType_WithSpringAbstractPointcutAdvisor_ShouldThrowJsonMappingException() {
        try {
            DeserializationContext deserializationContext = new DeserializationContext();
            JavaType javaType = new JavaType();
            javaType.setRawClass(AbstractPointcutAdvisor.class);
            subTypeValidator.validateSubType(deserializationContext, javaType);
            fail("Should throw JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Illegal type (org.springframework.aop.support.AbstractPointcutAdvisor) to deserialize: prevented for security reasons", e.getMessage());
        }
    }

    // Add more test cases for each method if needed

}