package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.HashSet;
import java.util.Set;

public class SubTypeValidatorTest {

    @Test
    public void testValidateSubType_validType() throws JsonMappingException {
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        JavaType type = Mockito.mock(JavaType.class);
        Class<?> raw = Mockito.mock(Class.class);
        Mockito.when(type.getRawClass()).thenReturn(raw);
        Mockito.when(raw.getName()).thenReturn("com.fasterxml.jackson.databind.jsontype.impl.SubTypeValidator");

        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(ctxt, type);
    }

    @Test
    public void testValidateSubType_illegalType_throwException() {
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        JavaType type = Mockito.mock(JavaType.class);
        Class<?> raw = Mockito.mock(Class.class);
        Mockito.when(type.getRawClass()).thenReturn(raw);
        Mockito.when(raw.getName()).thenReturn("org.apache.commons.collections.functors.InvokerTransformer");

        SubTypeValidator validator = SubTypeValidator.instance();
        assertThrows(JsonMappingException.class, () -> {
            validator.validateSubType(ctxt, type);
        });
    }

    @Test
    public void testValidateSubType_springFrameworkType_throwException() {
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        JavaType type = Mockito.mock(JavaType.class);
        Class<?> raw = Mockito.mock(Class.class);
        Mockito.when(type.getRawClass()).thenReturn(raw);
        Mockito.when(raw.getName()).thenReturn("org.springframework.beans.factory.config.PropertyPathFactoryBean");

        SubTypeValidator validator = SubTypeValidator.instance();
        assertThrows(JsonMappingException.class, () -> {
            validator.validateSubType(ctxt, type);
        });
    }

    @Test
    public void testValidateSubType_unknownType_throwException() {
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        JavaType type = Mockito.mock(JavaType.class);
        Class<?> raw = Mockito.mock(Class.class);
        Mockito.when(type.getRawClass()).thenReturn(raw);
        Mockito.when(raw.getName()).thenReturn("com.example.UnknownType");

        SubTypeValidator validator = SubTypeValidator.instance();
        assertThrows(JsonMappingException.class, () -> {
            validator.validateSubType(ctxt, type);
        });
    }
}