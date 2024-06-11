package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;

import org.junit.Test;

import com.fasterxml.jackson.core.type.ClassType;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;

public class SubTypeValidatorTest {

    @Test
    public void testValidateSubTypeNullType() {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext context = null;
        assertThrows(IllegalArgumentException.class, () -> validator.validateSubType(context, null));
    }

    @Test
    public void testValidateSubTypeNullContext() {
        SubTypeValidator validator = SubTypeValidator.instance();
        JavaType type = new ClassType(Object.class);
        assertThrows(IllegalArgumentException.class, () -> validator.validateSubType(null, type));
    }

    @Test
    public void testValidateSubTypeSimpleClass() {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext context = new DeserializationContext(new TestDeserializationConfig(), null);
        JavaType type = new ClassType(String.class);
        validator.validateSubType(context, type);
    }

    @Test
    public void testValidateSubTypeSpringNastyClass() {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext context = new DeserializationContext(new TestDeserializationConfig(), null);
        JavaType type = new ClassType(org.apache.commons.collections4.functors.InstantiateTransformer.class);
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(context, type));
    }

    @Test
    public void testValidateSubTypeSpringAbstractClass() {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext context = new DeserializationContext(new TestDeserializationConfig(), null);
        JavaType type = new ClassType(AbstractPointcutAdvisor.class);
        validator.validateSubType(context, type);
    }

    @Test
    public void testValidateSubTypeSpringApplicationContext() {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext context = new DeserializationContext(new TestDeserializationConfig(), null);
        JavaType type = new ClassType(AbstractApplicationContext.class);
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(context, type));
    }

    @Test
    public void testValidateSubTypeWithPrefixString() {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext context = new DeserializationContext(new TestDeserializationConfig(), null);
        JavaType type = new ClassType(org.springframework.beans.factory.config.PropertyPathFactoryBean.class);
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(context, type));
    }

    @Test
    public void testValidateSubTypeValidClass() {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext context = new DeserializationContext(new TestDeserializationConfig(), null);
        JavaType type = new ClassType(Integer.class);
        validator.validateSubType(context, type);
    }

    static class TestDeserializationConfig {
    }
}