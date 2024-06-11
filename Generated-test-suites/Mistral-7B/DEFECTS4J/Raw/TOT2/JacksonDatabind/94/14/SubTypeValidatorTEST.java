package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

import com.fasterxml.jackson.core.type.JavaType;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SubTypeValidatorTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testValidateSubType_ValidType() throws JsonMappingException {
        JavaType validType = objectMapper.getTypeFactory().constructType(String.class);
        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(mock(DeserializationContext.class), validType);
    }

    @Test
    public void testValidateSubType_IllegalType() {
        JavaType illegalType = objectMapper.getTypeFactory().constructType(SubTypeValidator.class);
        SubTypeValidator validator = SubTypeValidator.instance();
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(mock(DeserializationContext.class), illegalType));
    }

    @Test
    public void testValidateSubType_SpringType() throws JsonMappingException {
        JavaType springType = objectMapper.getTypeFactory().constructType(Object.class);
        SubTypeValidator validator = SubTypeValidator.instance();
        String fullName = "org.springframework.beans.factory.config.PropertyPathFactoryBean";
        String simpleName = "PropertyPathFactoryBean";
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(mock(DeserializationContext.class), objectMapper.getTypeFactory().constructType(fullName)));
    }

    @Test
    public void testValidateSubType_NastyClass() throws JsonMappingException {
        JavaType nastyClass = objectMapper.getTypeFactory().constructType("org.apache.commons.collections.functors.InvokerTransformer");
        SubTypeValidator validator = SubTypeValidator.instance();
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(mock(DeserializationContext.class), nastyClass));
    }

    
}