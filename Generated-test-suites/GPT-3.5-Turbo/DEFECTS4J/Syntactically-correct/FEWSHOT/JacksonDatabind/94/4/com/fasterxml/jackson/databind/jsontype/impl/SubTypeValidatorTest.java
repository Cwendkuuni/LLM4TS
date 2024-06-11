package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SubTypeValidatorTest {
    
    private DeserializationContext ctxt;
    private JavaType type;
    private SubTypeValidator subTypeValidator;

    @Before
    public void setUp() {
        ctxt = mock(DeserializationContext.class);
        type = mock(JavaType.class);
        subTypeValidator = SubTypeValidator.instance();
    }

    @Test
    public void testValidateSubType_DisallowedClass() throws JsonMappingException {
        Class<?> raw = mock(Class.class);
        String full = "org.apache.commons.collections.functors.InvokerTransformer";
        when(type.getRawClass()).thenReturn(raw);
        when(raw.getName()).thenReturn(full);
     
        try {
            subTypeValidator.validateSubType(ctxt, type);
        } catch (JsonMappingException ex) {
            assertEquals("Illegal type (org.apache.commons.collections.functors.InvokerTransformer) to deserialize: prevented for security reasons", ex.getMessage());
        }
    }
    
    @Test
    public void testValidateSubType_AllowedClass() throws JsonMappingException {
        Class<?> raw = mock(Class.class);
        String full = "com.fasterxml.jackson.databind.jsontype.impl.SubTypeValidator";
        when(type.getRawClass()).thenReturn(raw);
        when(raw.getName()).thenReturn(full);
     
        subTypeValidator.validateSubType(ctxt, type);
    }
  
}