package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;

import java.util.Collections;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class SubTypeValidatorTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testValidateSubTypeWithAllowedClass() throws Exception {
        SubTypeValidator validator = SubTypeValidator.instance();
        JavaType allowedType = TypeFactory.defaultInstance().constructType(String.class);
        validator.validateSubType(null, allowedType);
    }

    @Test
    public void testValidateSubTypeWithDeniedClass() throws Exception {
        SubTypeValidator validator = SubTypeValidator.instance();
        JavaType deniedType = TypeFactory.defaultInstance().constructType(org.apache.commons.collections4.functors.InstantiateTransformer.class);
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(null, deniedType));
    }

    @Test
    public void testValidateSubTypeWithSpringApplicationContext() throws Exception {
        SubTypeValidator validator = SubTypeValidator.instance();
        JavaType applicationContextType = TypeFactory.defaultInstance().constructType(org.springframework.context.ApplicationContext.class);
        validator.validateSubType(null, applicationContextType);
    }

    @Test
    public void testValidateSubTypeWithSpringBeanFactoryPointcutAdvisor() throws Exception {
        SubTypeValidator validator = SubTypeValidator.instance();
        JavaType advisorType = TypeFactory.defaultInstance().constructType(org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor.class);
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(null, advisorType));
    }

    @Test
    public void testValidateSubTypeWithUnsupportedClass() throws Exception {
        SubTypeValidator validator = SubTypeValidator.instance();
        JavaType unsupportedType = TypeFactory.defaultInstance().constructType(Object[].class);
        assertThrows(JsonProcessingException.class, () -> mapper.readValue("{}", unsupportedType));
        validator.validateSubType(null, unsupportedType);
    }

    @Test
    public void testValidateSubTypeWithAbstractClass() throws Exception {
        SubTypeValidator validator = SubTypeValidator.instance();
        JavaType abstractType = TypeFactory.defaultInstance().constructType(AbstractPointcutAdvisor.class);
        validator.validateSubType(null, abstractType);
    }

    static class AbstractPointcutAdvisor {}
}