package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;

import java.util.Collections;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SubTypeValidatorTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testValidateSubTypeWithValidTypes() throws JsonMappingException, JsonProcessingException {
        SubTypeValidator validator = SubTypeValidator.instance();

        JavaType validType1 = objectMapper.getTypeFactory().constructType(String.class);
        JavaType validType2 = objectMapper.getTypeFactory().constructType(Integer.class);

        validator.validateSubType(createContext(), validType1);
        validator.validateSubType(createContext(), validType2);
    }

    @Test
    public void testValidateSubTypeWithNastyClasses() throws JsonMappingException, JsonProcessingException {
        SubTypeValidator validator = SubTypeValidator.instance();

        JavaType nastyType1 = objectMapper.getTypeFactory().constructType(org.apache.commons.collections.functors.InvokerTransformer.class);
        JavaType nastyType2 = objectMapper.getTypeFactory().constructType(com.sun.org.apache.bcel.internal.util.ClassLoader.class);

        assertThrows(JsonMappingException.class, () -> validator.validateSubType(createContext(), nastyType1));
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(createContext(), nastyType2));
    }

    @Test
    public void testValidateSubTypeWithSpringClasses() throws JsonMappingException, JsonProcessingException {
        SubTypeValidator validator = SubTypeValidator.instance();

        JavaType springType1 = objectMapper.getTypeFactory().constructType(org.springframework.beans.factory.config.PropertyPathFactoryBean.class);
        JavaType springType2 = objectMapper.getTypeFactory().constructType(org.apache.tomcat.dbcp.dbcp2.BasicDataSource.class);

        assertThrows(JsonMappingException.class, () -> validator.validateSubType(createContext(), springType1));
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(createContext(), springType2));
    }

    private DeserializationContext createContext() {
        return objectMapper.getDeserializationContext();
    }
}