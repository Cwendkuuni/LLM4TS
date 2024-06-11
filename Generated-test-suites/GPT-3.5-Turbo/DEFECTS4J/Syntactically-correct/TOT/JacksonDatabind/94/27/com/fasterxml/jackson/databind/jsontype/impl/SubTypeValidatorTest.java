package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

public class SubTypeValidatorTest {

    private SubTypeValidator validator;

    @Before
    public void setUp() {
        validator = SubTypeValidator.instance();
    }

    @Test
    public void testValidateSubType_ValidType_Success() {
        DeserializationContext ctxt = new DeserializationContext(null, null);
        JavaType type = JavaType.construct(TestObject.class);
        
        try {
            validator.validateSubType(ctxt, type);
        } catch (JsonMappingException e) {
            fail("Unexpected exception");
        }
    }
    
    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_IllegalType_ExceptionThrown() throws JsonMappingException {
        DeserializationContext ctxt = new DeserializationContext(null, null);
        JavaType type = JavaType.construct(IllegalObject.class);
        
        validator.validateSubType(ctxt, type);
    }
    
    @Test
    public void testValidateSubType_SpringAbstractPointcutAdvisor_ExceptionThrown() {
        DeserializationContext ctxt = new DeserializationContext(null, null);
        JavaType type = JavaType.construct(AbstractPointcutAdvisor.class);
        
        try {
            validator.validateSubType(ctxt, type);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Illegal type (AbstractPointcutAdvisor) to deserialize: prevented for security reasons", e.getMessage());
        }
    }
    
    @Test
    public void testValidateSubType_SpringAbstractApplicationContext_ExceptionThrown() {
        DeserializationContext ctxt = new DeserializationContext(null, null);
        JavaType type = JavaType.construct(AbstractApplicationContext.class);
        
        try {
            validator.validateSubType(ctxt, type);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Illegal type (AbstractApplicationContext) to deserialize: prevented for security reasons", e.getMessage());
        }
    }
    
    @Test
    public void testValidateSubType_Interface_NoExceptionThrown() {
        DeserializationContext ctxt = new DeserializationContext(null, null);
        JavaType type = JavaType.construct(TestInterface.class);
        
        try {
            validator.validateSubType(ctxt, type);
        } catch (JsonMappingException e) {
            fail("Unexpected exception");
        }
    }

    private class TestObject {

    }
    
    private class IllegalObject {
        
    }
    
    private interface TestInterface {
        
    }
    
    private abstract class AbstractPointcutAdvisor {
        
    }
    
    private abstract class AbstractApplicationContext {
        
    }

}