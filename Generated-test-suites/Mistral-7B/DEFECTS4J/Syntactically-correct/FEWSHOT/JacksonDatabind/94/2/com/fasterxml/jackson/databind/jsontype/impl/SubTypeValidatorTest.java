package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.jsontype.impl.SubTypeValidator;

public class SubTypeValidatorTest {

    private static SubTypeValidator instance;
    private static DeserializationContext context;

    @BeforeClass
    public static void setup() throws Exception {
        instance = SubTypeValidator.instance();
        context = new DeserializationContext(null, null, null);
    }

    @Test
    public void testValidateSubType_ValidClass() throws JsonMappingException {
        JavaType type = context.getTypeFactory().constructType(String.class);
        instance.validateSubType(context, type);
    }

    @Test
    public void testValidateSubType_InvalidClass() throws JsonMappingException, IllegalAccessException, InstantiationException, InvocationTargetException {
        JavaType type = context.getTypeFactory().constructType(SubTypeValidator.class);
        Method method = SubTypeValidator.class.getDeclaredMethod("instance");
        method.setAccessible(true);
        SubTypeValidator invalidInstance = (SubTypeValidator) method.invoke(null);

        assertThrows(JsonMappingException.class, () -> invalidInstance.validateSubType(context, type));
    }

    @Test
    public void testValidateSubType_SpringClass() throws JsonMappingException {
        JavaType type = context.getTypeFactory().constructType("org.springframework.beans.factory.config.PropertyPathFactoryBean");
        instance.validateSubType(context, type);
    }

    @Test
    public void testValidateSubType_NastyClass() throws JsonMappingException {
        JavaType type = context.getTypeFactory().constructType("org.apache.commons.collections4.functors.InstantiateTransformer");
        assertThrows(JsonMappingException.class, () -> instance.validateSubType(context, type));
    }

    @Test
    public void testValidateSubType_Interface() throws JsonMappingException {
        JavaType type = context.getTypeFactory().constructType(Comparable.class);
        instance.validateSubType(context, type);
    }

    @Test
    public void testValidateSubType_PrefixSpringClass() throws JsonMappingException {
        JavaType type = context.getTypeFactory().constructType("org.springframework.beans.factory.config.AbstractApplicationContext");
        instance.validateSubType(context, type);
    }
}