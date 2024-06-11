package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;
import org.junit.Test;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

public class SubTypeValidatorTest {

    @Test
    public void testValidateSubType_Illegal() {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = null;
        JavaType type = null;
       
        type = JavaType.construct(TestClass.class);
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(ctxt, type));
        
        type = JavaType.construct(AnotherTestClass.class);
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(ctxt, type));
        
        type = JavaType.construct(SecurityTestClass.class);
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(ctxt, type));
    }
    
    @Test
    public void testValidateSubType_Legal() {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = null;
        JavaType type = null;
       
        type = JavaType.construct(LegalTestClass.class);
        validator.validateSubType(ctxt, type);
        
        type = JavaType.construct(AnotherLegalTestClass.class);
        validator.validateSubType(ctxt, type);
        
        type = JavaType.construct(LegalSpringClass.class);
        validator.validateSubType(ctxt, type);
    }
    
    class TestClass {
    }
    
    class AnotherTestClass {
    }
    
    class SecurityTestClass {
    }
    
    class LegalTestClass {
    }
    
    class AnotherLegalTestClass {
    }
    
    class LegalSpringClass {
    }
}