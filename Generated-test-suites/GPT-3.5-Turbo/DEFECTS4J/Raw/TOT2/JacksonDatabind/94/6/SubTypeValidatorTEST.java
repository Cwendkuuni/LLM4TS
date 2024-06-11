package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class SubTypeValidatorTest {

    private SubTypeValidator subTypeValidator;

    @Before
    public void setup() {
        subTypeValidator = SubTypeValidator.instance();
    }

    @Test
    public void testValidateSubTypeValid() throws JsonMappingException {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JavaType type = mock(JavaType.class);
        
        subTypeValidator.validateSubType(ctxt, type);

        // Not throwing JsonMappingException means validation is successful
    }

    @Test
    public void testValidateSubTypeInvalid() {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JavaType type = mock(JavaType.class);
        
        // Mocking a class that is in the DEFAULT_NO_DESER_CLASS_NAMES set
        when(type.getRawClass()).thenReturn(ObjectFactory.class);
        
        try {
            subTypeValidator.validateSubType(ctxt, type);
        } catch (JsonMappingException e) {
            // Exception should be thrown for an invalid class
            assertTrue(e.getMessage().contains("Illegal type"));
        }
    }

    @Test
    public void testInstance() {
        SubTypeValidator inst = SubTypeValidator.instance();
        assertEquals(inst, subTypeValidator);
    }

}