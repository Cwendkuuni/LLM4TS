package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;

import java.lang.ClassCastException;
import java.lang.reflect.TypeVariable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class SubTypeValidatorTest {

    @Test
    public void testValidateSubTypeWithAllowedClass() throws JsonMappingException, JsonProcessingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext context = new DeserializationContext(null, new TypeDeserializer[0], null);
        JavaType javaType = context.constructType(String.class);
        validator.validateSubType(context, javaType);
    }

    @Test
    public void testValidateSubTypeWithSpringClassThroughInterface() throws JsonMappingException, JsonProcessingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext context = new DeserializationContext(null, new TypeDeserializer[0], null);
        JavaType javaType = context.constructType(AbstractApplicationContext.class);
        validator.validateSubType(context, javaType);
    }

    @Test
    public void testValidateSubTypeWithSpringClassThroughImplementation() throws JsonMappingException, JsonProcessingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext context = new DeserializationContext(null, new TypeDeserializer[0], null);
        TypeVariable<?> typeVariable = AbstractPointcutAdvisor.class.getTypeParameters()[0];
        JavaType javaType = context.constructType(AbstractPointcutAdvisor.class, typeVariable.getName(), String.class);
        validator.validateSubType(context, javaType);
    }

    @Test
    public void testValidateSubTypeWithDisallowedClass() {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext context = new DeserializationContext(null, new TypeDeserializer[0], null);
        JavaType javaType = context.constructType(java.rmi.server.UnicastRemoteObject.class);
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(context, javaType));
    }

    @Test
    public void testValidateSubTypeWithDisallowedClassThroughInterface() {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext context = new DeserializationContext(null, new TypeDeserializer[0], null);
        JavaType javaType = context.constructType(Object.class, Object.class.getTypeName());
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(context, javaType));
    }

    @Test
    public void testValidateSubTypeWithDisallowedClassThroughImplementation() {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext context = new DeserializationContext(null, new TypeDeserializer[0], null);
        JavaType javaType = context.constructType(java.util.logging.FileHandler.class);
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(context, javaType));
    }

    @Test
    public void testValidateSubTypeWithDisallowedClassAndParent() throws JsonMappingException, JsonProcessingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext context = new DeserializationContext(null, new TypeDeserializer[0], null);
        JavaType javaType = context.constructType(com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl.class);
        validator.validateSubType(context, javaType);
    }

    @Test
    public void testValidateSubTypeWithDisallowedClassAndParentInterface() throws JsonMappingException, JsonProcessingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext context = new DeserializationContext(null, new TypeDeserializer[0], null);
        JavaType javaType = context.constructType(java.util.logging.FileHandler.class, Object.class);
        validator.validateSubType(context, javaType);
    }

    @Test
    public void testValidateSubTypeWithDisallowedClassAndParentInterfaceAndImplementation() throws JsonMappingException, JsonProcessingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext context = new DeserializationContext(null, new TypeDeserializer[0], null);
        JavaType javaType = context.constructType(com.sun.org.apache.xalan.xsltc.trax.TemplatesImpl.class, Object.class, String.class);
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(context, javaType));
    }

    @Test(expected = ClassCastException.class)
    public void testValidateSubTypeWithInvalidType() {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext context = new DeserializationContext(null, new TypeDeserializer[0], null);
        JavaType javaType = (JavaType) new Object();
        validator.validateSubType(context, javaType);
    }
}