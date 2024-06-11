
package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class SubTypeValidatorTest {
    
    private SubTypeValidator validator;
    
    @Mock
    private DeserializationContext deserializationContext;
    
    @Mock
    private JavaType javaType;
    
    @Before
    public void setup() {
        validator = SubTypeValidator.instance();
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testValidateSubType_NotDeserializableClass() throws JsonMappingException {
        Class<?> notDeserializableClass = org.apache.commons.collections.functors.InvokerTransformer.class;
        when(javaType.getRawClass()).thenReturn(notDeserializableClass);
        
        try {
            validator.validateSubType(deserializationContext, javaType);
            fail("Expected JsonMappingException to be thrown");
        } catch (JsonMappingException e) {
            assertEquals("Illegal type (org.apache.commons.collections3.functors.InvokerTransformer) to deserialize: prevented for security reasons", e.getMessage());
        }
        
        verify(javaType).getRawClass();
    }
    
    @Test
    public void testValidateSubType_DeserializableClass() throws JsonMappingException {
        Class<?> deserializableClass = com.fasterxml.jackson.databind.JavaType.class;
        when(javaType.getRawClass()).thenReturn(deserializableClass);
        
        validator.validateSubType(deserializationContext, javaType);
        
        verify(javaType).getRawClass();
    }
}
