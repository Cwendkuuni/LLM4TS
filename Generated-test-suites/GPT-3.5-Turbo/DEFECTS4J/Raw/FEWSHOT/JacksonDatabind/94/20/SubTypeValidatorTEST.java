package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import java.util.*;
import org.junit.Test;
import org.mockito.Mockito;

public class SubTypeValidatorTest {

    private SubTypeValidator subTypeValidator;

    @Before
    public void setup() {
        subTypeValidator = SubTypeValidator.instance();
    }

    @Test
    public void testValidateSubType_WithValidType_ShouldNotThrowException() {
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        JavaType type = Mockito.mock(JavaType.class);
        Class rawClass = Mockito.mock(Class.class);

        Mockito.when(type.getRawClass()).thenReturn(rawClass);
        Mockito.when(rawClass.getName()).thenReturn("com.fasterxml.jackson.databind.jsontype.impl.ValidType");

        try {
            subTypeValidator.validateSubType(ctxt, type);
        } catch (JsonMappingException e) {
            fail("Should not throw an exception");
        }
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_WithIllegalType_ShouldThrowException() throws JsonMappingException {
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        JavaType type = Mockito.mock(JavaType.class);
        Class rawClass = Mockito.mock(Class.class);

        Mockito.when(type.getRawClass()).thenReturn(rawClass);
        Mockito.when(rawClass.getName()).thenReturn("org.apache.commons.collections.functors.InvokerTransformer");

        subTypeValidator.validateSubType(ctxt, type);
    }
}