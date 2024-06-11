package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class SubTypeValidatorTest {
    
    private SubTypeValidator subTypeValidator;
    
    @Before
    public void setUp() {
        subTypeValidator = SubTypeValidator.instance();
    }
    
    @Test
    public void testValidateSubType() throws JsonMappingException {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JavaType type = mock(JavaType.class);
        
        // Test when _cfgIllegalClassNames contains the tested class name
        Set<String> cfgIllegalClassNames = new HashSet<String>();
        cfgIllegalClassNames.add("org.apache.commons.collections.functors.InvokerTransformer");
        subTypeValidator._cfgIllegalClassNames = cfgIllegalClassNames;
        
        try {
            subTypeValidator.validateSubType(ctxt, type);
        } catch(JsonMappingException e) {
            assertEquals("Illegal type (org.apache.commons.collections.functors.InvokerTransformer) to deserialize: prevented for security reasons", e.getMessage());
        }
        
        // Test when _cfgIllegalClassNames does not contain the tested class name
        Set<String> emptyCfgIllegalClassNames = new HashSet<String>();
        subTypeValidator._cfgIllegalClassNames = emptyCfgIllegalClassNames;
        
        try {
            subTypeValidator.validateSubType(ctxt, type);
        } catch(JsonMappingException e) {
            // Exception should not be thrown
            fail("Exception should not be thrown");
        }
        
        verify(ctxt, times(2)).getRawClass();
        verify(type, times(2)).getRawClass();
    }
}