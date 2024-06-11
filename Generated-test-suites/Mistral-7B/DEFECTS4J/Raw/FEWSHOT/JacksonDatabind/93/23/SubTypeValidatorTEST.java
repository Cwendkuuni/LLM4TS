package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;

import java.lang.ClassCastException;
import java.lang.ClassNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;

import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class SubTypeValidatorTest {

    @Test
    public void testValidateSubTypeWithAllowedClasses() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        SubTypeValidator validator = SubTypeValidator.instance();

        // Test with valid classes
        JavaType validType1 = mapper.getTypeFactory().constructType(String.class);
        validator.validateSubType(mapper.getContext(), validType1);

        JavaType validType2 = mapper.getTypeFactory().constructType(Integer.class);
        validator.validateSubType(mapper.getContext(), validType2);

        JavaType validType3 = mapper.getTypeFactory().constructType(List.class);
        validator.validateSubType(mapper.getContext(), validType3);

        JavaType validType4 = mapper.getTypeFactory().constructType(HashMap.class);
        validator.validateSubType(mapper.getContext(), validType4);
    }

    @Test
    public void testValidateSubTypeWithDisallowedClasses() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        SubTypeValidator validator = SubTypeValidator.instance();

        // Test with disallowed classes
        JavaType disallowedType1 = mapper.getTypeFactory().constructType(InvokerTransformer.class);
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(mapper.getContext(), disallowedType1));

        JavaType disallowedType2 = mapper.getTypeFactory().constructType(java.rmi.server.UnicastRemoteObject.class);
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(mapper.getContext(), disallowedType2));

        JavaType disallowedType3 = mapper.getTypeFactory().constructType(com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl.class);
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(mapper.getContext(), disallowedType3));

        JavaType disallowedType4 = mapper.getTypeFactory().constructType(com.mchange.v2.c3p0.WrapperConnectionPoolDataSource.class);
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(mapper.getContext(), disallowedType4));

        JavaType disallowedType5 = mapper.getTypeFactory().constructType(org.springframework.beans.factory.config.PropertyPathFactoryBean.class);
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(mapper.getContext(), disallowedType5));
    }

    @Test
    public void testValidateSubTypeWithInvalidClass() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        SubTypeValidator validator = SubTypeValidator.instance();

        // Test with an invalid class - should throw ClassNotFoundException
        JavaType invalidType = mapper.getTypeFactory().constructType("com.invalid.ClassName");
        assertThrows(ClassNotFoundException.class, () -> validator.validateSubType(mapper.getContext(), invalidType));

        // Test with a ClassCastException - should not throw JsonMappingException
        JavaType invalidType2 = mapper.getTypeFactory().constructType(int[].class);
        try {
            validator.validateSubType(mapper.getContext(), invalidType2);
        } catch (JsonMappingException e) {
            throw new AssertionError("Should not throw JsonMappingException");
        }
    }

    @Test
    public void testValidateSubTypeWithInterface() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        SubTypeValidator validator = SubTypeValidator.instance();

        // Test with an interface
        JavaType interfaceType = mapper.getTypeFactory().constructType(Comparable.class);
        validator.validateSubType(mapper.getContext(), interfaceType);
    }

    @Test
    public void testValidateSubTypeWithAbstractClass() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        SubTypeValidator validator = SubTypeValidator.instance();

        // Test with an abstract class
        JavaType abstractType = mapper.getTypeFactory().constructType(AbstractPointcutAdvisor.class);
        validator.validateSubType(mapper.getContext(), abstractType);
    }
}