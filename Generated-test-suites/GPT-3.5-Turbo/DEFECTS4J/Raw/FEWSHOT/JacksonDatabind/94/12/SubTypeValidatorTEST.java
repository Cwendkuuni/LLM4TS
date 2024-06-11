package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class SubTypeValidatorTest {

    @Test
    public void testValidateSubType_ValidClassName() throws JsonMappingException {
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        JavaType type = Mockito.mock(JavaType.class);
        Class<?> raw = Mockito.mock(Class.class);
        Mockito.when(type.getRawClass()).thenReturn(raw);
        Mockito.when(raw.getName()).thenReturn("com.fasterxml.jackson.databind.jsontype.impl.SubTypeValidator");

        try {
            SubTypeValidator.instance().validateSubType(ctxt, type);
        } catch (JsonMappingException e) {
            Assert.fail("Should not throw an exception for a valid class name");
        }
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_IllegalClassName() throws JsonMappingException {
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        JavaType type = Mockito.mock(JavaType.class);
        Class<?> raw = Mockito.mock(Class.class);
        Mockito.when(type.getRawClass()).thenReturn(raw);
        Mockito.when(raw.getName()).thenReturn("org.apache.commons.collections.functors.InvokerTransformer");

        SubTypeValidator.instance().validateSubType(ctxt, type);
    }
}