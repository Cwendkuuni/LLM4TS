package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;

import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.jsontype.impl.SubTypeValidator;

public class SubTypeValidatorTest {

    @Test
    public void testValidateSubTypeWithIllegalClass() {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext context = new DeserializationContext(null, Collections.emptyMap(), null);
        JavaType type = context.constructType(Object.class);

        assertThrows(JsonMappingException.class, () -> validator.validateSubType(context, type));
    }

    @Test
    public void testValidateSubTypeWithSpringClass() {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext context = new DeserializationContext(null, Collections.emptyMap(), null);
        JavaType type = context.constructType(org.springframework.beans.factory.config.PropertyPathFactoryBean.class);

        assertThrows(JsonMappingException.class, () -> validator.validateSubType(context, type));
    }

    @Test
    public void testValidateSubTypeWithAllowedClass() {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext context = new DeserializationContext(null, Collections.emptyMap(), null);
        JavaType type = context.constructType(String.class);

        validator.validateSubType(context, type);
    }

    @Test
    public void testValidateSubTypeWithInterface() {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext context = new DeserializationContext(null, Collections.emptyMap(), null);
        JavaType type = context.constructType(Comparable.class);

        validator.validateSubType(context, type);
    }

    @Test
    public void testValidateSubTypeWithSpringClassHierarchy() throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext context = new DeserializationContext(null, Collections.emptyMap(), null);
        JavaType type = context.constructType(com.fasterxml.jackson.databind.jsontype.impl.SubTypeValidator.class);

        assertThrows(JsonMappingException.class, () -> validator.validateSubType(context, type));
    }
}