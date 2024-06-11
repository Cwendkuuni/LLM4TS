package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

@RunWith(MockitoJUnitRunner.class)
public class SubTypeValidatorTest {

    private DeserializationContext createMockContext() {
        return Mockito.mock(DeserializationContext.class);
    }

    private JavaType createMockType() {
        return Mockito.mock(JavaType.class);
    }

    @Test
    public void testValidateSubTypeWithValidClass() throws JsonMappingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = createMockContext();
        JavaType type = createMockType();

        validator.validateSubType(ctxt, type);
    }

    @Test
    public void testValidateSubTypeWithIllegalClass() throws JsonMappingException, JsonProcessingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = createMockContext();
        JavaType type = Mockito.mock(JavaType.class);
        Mockito.when(type.getRawClass()).thenReturn(getClass());

        assertThrows(JsonMappingException.class, () -> validator.validateSubType(ctxt, type));
    }

    @Test
    public void testValidateSubTypeWithSpringClass() throws JsonMappingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = createMockContext();
        JavaType type = Mockito.mock(JavaType.class);
        Mockito.when(type.getRawClass()).thenReturn(getClassByName("org.springframework.beans.factory.config.PropertyPathFactoryBean"));

        validator.validateSubType(ctxt, type);
    }

    @Test
    public void testValidateSubTypeWithSpringInterface() throws JsonMappingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = createMockContext();
        JavaType type = Mockito.mock(JavaType.class);
        Mockito.when(type.getRawClass()).thenReturn(getClassByName("org.springframework.beans.factory.config.ConfigurableListableBeanFactory"));

        validator.validateSubType(ctxt, type);
    }

    @Test
    public void testValidateSubTypeWithIllegalSpringClass() throws JsonMappingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = createMockContext();
        JavaType type = Mockito.mock(JavaType.class);
        Mockito.when(type.getRawClass()).thenReturn(getClassByName("org.springframework.beans.factory.ObjectFactory"));

        assertThrows(JsonMappingException.class, () -> validator.validateSubType(ctxt, type));
    }

    @Test
    public void testValidateSubTypeWithIllegalClassInHierarchy() throws JsonMappingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = createMockContext();
        JavaType type = Mockito.mock(JavaType.class);
        Class<?> clazz = getClassByName("com.fasterxml.jackson.databind.jsontype.impl.SubTypeValidator");
        Class<?> superClass = getClassByName("java.lang.Object");
        Class<?> superSuperClass = getClassByName("com.mchange.v2.c3p0.ComboPooledDataSource");
        Mockito.when(type.getRawClass()).thenReturn(clazz);
        Mockito.when(clazz.getSuperclass()).thenReturn(superClass);
        Mockito.when(superClass.getSuperclass()).thenReturn(superSuperClass);

        assertThrows(JsonMappingException.class, () -> validator.validateSubType(ctxt, type));
    }

    private Class<?> getClassByName(String name) {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}