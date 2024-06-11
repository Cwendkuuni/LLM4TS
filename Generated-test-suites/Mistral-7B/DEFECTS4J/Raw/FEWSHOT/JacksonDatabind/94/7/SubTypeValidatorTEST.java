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

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testValidateSubTypeWithValidType() throws JsonMappingException, JsonProcessingException {
        JavaType validType = mapper.getTypeFactory().constructType(String.class);
        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(new DeserializationContext(mapper.getConfig()), validType);
    }

    @Test
    public void testValidateSubTypeWithNastyClass() throws JsonMappingException {
        JavaType nastyType = mapper.getTypeFactory().constructType(org.springframework.beans.factory.ObjectFactory.class);
        SubTypeValidator validator = SubTypeValidator.instance();
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(new DeserializationContext(mapper.getConfig()), nastyType));
    }

    @Test
    public void testValidateSubTypeWithSpringApplicationContext() throws JsonMappingException, JsonProcessingException {
        JavaType springType = mapper.getTypeFactory().constructType(org.springframework.context.ApplicationContext.class);
        SubTypeValidator validator = SubTypeValidator.instance();
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(new DeserializationContext(mapper.getConfig()), springType));
    }

    @Test
    public void testValidateSubTypeWithSpringBeanFactoryPointcutAdvisor() throws JsonMappingException, JsonProcessingException {
        JavaType springAdvisorType = mapper.getTypeFactory().constructType(org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor.class);
        SubTypeValidator validator = SubTypeValidator.instance();
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(new DeserializationContext(mapper.getConfig()), springAdvisorType));
    }

    @Test
    public void testValidateSubTypeWithNonDeserializableClass() throws JsonMappingException {
        JavaType nonDeserializableType = mapper.getTypeFactory().constructType(SubTypeValidator.class);
        SubTypeValidator validator = SubTypeValidator.instance();
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(new DeserializationContext(mapper.getConfig()), nonDeserializableType));
    }

    @Test
    public void testValidateSubTypeWithNullType() {
        SubTypeValidator validator = SubTypeValidator.instance();
        assertNull(validator.validateSubType(new DeserializationContext(mapper.getConfig()), null));
    }
}