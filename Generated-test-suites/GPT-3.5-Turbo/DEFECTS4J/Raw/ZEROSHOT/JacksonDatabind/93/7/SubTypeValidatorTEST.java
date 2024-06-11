package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

public class SubTypeValidatorTest {

    private SubTypeValidator validator = SubTypeValidator.instance();
    private DeserializationContext deserializationContext;

    @Test
    public void testValidateSubType_ValidClass() throws JsonMappingException {
        JavaType type = Mockito.mock(JavaType.class);
        Mockito.when(type.getRawClass()).thenReturn(String.class);
        
        try {
            validator.validateSubType(deserializationContext, type);
        } catch (JsonMappingException e) {
            Assert.fail("Exception should not have been thrown");
        }
    }
    
    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_IllegalClass() throws JsonMappingException {
        JavaType type = Mockito.mock(JavaType.class);
        Mockito.when(type.getRawClass()).thenReturn(TemplatesImpl.class);
        
        validator.validateSubType(deserializationContext, type);
    }
}