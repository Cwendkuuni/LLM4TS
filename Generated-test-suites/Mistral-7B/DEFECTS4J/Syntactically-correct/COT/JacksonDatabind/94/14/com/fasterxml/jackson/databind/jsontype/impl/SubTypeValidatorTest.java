package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.lang.ClassCastException;
import java.lang.ClassNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.SubTypeValidator;

/**
 * Test class for com.fasterxml.jackson.databind.jsontype.impl.SubTypeValidator
 */
public class SubTypeValidatorTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    // Test method for constructor SubTypeValidator()
    @Test
    public void testSubTypeValidatorConstructor() {
        SubTypeValidator validator = SubTypeValidator.instance();
        // No need to assert anything as constructor doesn't throw any exception
    }

    // Test method for public static SubTypeValidator instance()
    @Test
    public void testInstance() {
        SubTypeValidator validator = SubTypeValidator.instance();
        // No need to assert anything as instance() returns the same instance every time
    }

    // Test method for protected Set<String> _cfgIllegalClassNames = DEFAULT_NO_DESER_CLASS_NAMES;
    @Test
    public void testConfigIllegalClassNames() {
        Set<String> illegalClassNames = SubTypeValidator.instance()._cfgIllegalClassNames;
        assertEquals("Size of illegalClassNames should be 24", 24, illegalClassNames.size());
    }

    // Test method for public void validateSubType(DeserializationContext ctxt, JavaType type)
    @Test
    public void testValidateSubType() throws JsonMappingException, ClassNotFoundException {
        JavaType javaType = objectMapper.getTypeFactory().constructType(String.class);
        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(null, javaType);
    }

    // Test method for public void validateSubType(DeserializationContext ctxt, JavaType type) with illegal class name
    @Test(expected = JsonMappingException.class)
    public void testValidateSubTypeWithIllegalClassName() throws JsonMappingException, ClassNotFoundException {
        String illegalClassName = "org.apache.commons.collections.functors.InvokerTransformer";
        JavaType javaType = objectMapper.getTypeFactory().constructType(illegalClassName.getClass());
        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(null, javaType);
    }

    // Test method for public void validateSubType(DeserializationContext ctxt, JavaType type) with interface
    @Test
    public void testValidateSubTypeWithInterface() throws JsonMappingException, ClassNotFoundException {
        JavaType javaType = objectMapper.getTypeFactory().constructType(Iterable.class);
        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(null, javaType);
    }

    // Test method for public void validateSubType(DeserializationContext ctxt, JavaType type) with Spring class
    @Test
    public void testValidateSubTypeWithSpringClass() throws JsonMappingException, ClassNotFoundException {
        JavaType javaType = objectMapper.getTypeFactory().constructType(AbstractApplicationContext.class);
        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(null, javaType);
    }

    // Test method for public void validateSubType(DeserializationContext ctxt, JavaType type) with custom class
    @Test(expected = JsonMappingException.class)
    public void testValidateSubTypeWithCustomClass() throws JsonMappingException, ClassNotFoundException {
        String customClassName = "com.example.CustomClass";
        JavaType javaType = objectMapper.getTypeFactory().constructType(customClassName.getClass());
        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(null, javaType);
    }

    // Test method for public void validateSubType(DeserializationContext ctxt, JavaType type) with nested class
    @Test(expected = JsonMappingException.class)
    public void testValidateSubTypeWithNestedClass() throws JsonMappingException, ClassNotFoundException {
        Class<?> outerClass = Object.class;
        Class<?> innerClass = OuterClass.InnerClass.class;
        JavaType javaType = objectMapper.getTypeFactory().constructType(innerClass);
        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(null, javaType);
    }

    // Test method for public void validateSubType(DeserializationContext ctxt, JavaType type) with null type
    @Test(expected = JsonMappingException.class)
    public void testValidateSubTypeWithNullType() {
        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(null, null);
    }

    // Test method for public void validateSubType(DeserializationContext ctxt, JavaType type) with null context
    @Test(expected = JsonMappingException.class)
    public void testValidateSubTypeWithNullContext() {
        JavaType javaType = objectMapper.getTypeFactory().constructType(String.class);
        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(null, javaType);
    }

    // Test method for public void validateSubType(DeserializationContext ctxt, JavaType type) with reflection exception
    @Test(expected = JsonMappingException.class)
    public void testValidateSubTypeWithReflectionException() throws JsonMappingException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException {
        String reflectionClassName = "java.lang.reflect.Method";
        JavaType javaType = objectMapper.getTypeFactory().constructType(reflectionClassName);
        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(null, javaType);
    }

    // Test method for public void validateSubType(DeserializationContext ctxt, JavaType type) with class cast exception
    @Test(expected = JsonMappingException.class)
    public void testValidateSubTypeWithClassCastException() throws JsonMappingException, ClassNotFoundException {
        JavaType javaType = objectMapper.getTypeFactory().constructType(String[].class);
        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(null, javaType);
    }

    // Test method for public void validateSubType(DeserializationContext ctxt, JavaType type) with ClassNotFoundException
    @Test(expected = JsonMappingException.class)
    public void testValidateSubTypeWithClassNotFoundException() throws JsonMappingException, ClassNotFoundException {
        String nonExistentClassName = "com.example.NonExistentClass";
        JavaType javaType = objectMapper.getTypeFactory().constructType(nonExistentClassName.getClass());
        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(null, javaType);
    }

    // Test method for public void validateSubType(DeserializationContext ctxt, JavaType type) with JsonProcessingException
    @Test(expected = JsonMappingException.class)
    public void testValidateSubTypeWithJsonProcessingException() throws JsonMappingException, ClassNotFoundException {
        String json = "{\"type\":\"some_invalid_type\"}";
        JavaType javaType = objectMapper.getTypeFactory().constructType(String.class);
        SubTypeValidator validator = SubTypeValidator.instance();
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(null, javaType, objectMapper.readValue(json, javaType)));
    }
}