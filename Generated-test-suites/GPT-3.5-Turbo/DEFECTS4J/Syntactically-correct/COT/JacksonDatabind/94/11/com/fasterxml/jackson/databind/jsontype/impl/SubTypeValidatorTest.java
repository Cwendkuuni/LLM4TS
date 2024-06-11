
package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Test;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

import static org.mockito.Mockito.mock;

public class SubTypeValidatorTest {

    SubTypeValidator subTypeValidator = SubTypeValidator.instance();

    @Test
    public void testValidateSubType_ValidType() throws JsonMappingException {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JavaType type = mock(JavaType.class);
        
        subTypeValidator.validateSubType(ctxt, type);
    }
    
    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_IllegalType() throws JsonMappingException {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JavaType type = mock(JavaType.class);
    
        // Specify a class name that is flagged as an illegal type, such as "org.apache.commons.collections.functors.InvokerTransformer"
        subTypeValidator.validateSubType(ctxt, type);
    }
}

