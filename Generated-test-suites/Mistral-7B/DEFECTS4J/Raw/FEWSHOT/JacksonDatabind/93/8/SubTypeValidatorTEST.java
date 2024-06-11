package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SubTypeValidatorTest {

    @Test
    public void testValidateSubTypeWithAllowedClasses() throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JavaType allowedType1 = mapper.getTypeFactory().constructType(String.class);
        JavaType allowedType2 = mapper.getTypeFactory().constructType(Integer.class);

        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(ctxt, allowedType1);
        validator.validateSubType(ctxt, allowedType2);
    }

    @Test
    public void testValidateSubTypeWithDisallowedClasses() throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JavaType disallowedType1 = mapper.getTypeFactory().constructType(SubTypeValidator.class);
        JavaType disallowedType2 = mapper.getTypeFactory().constructType(Object.class);

        SubTypeValidator validator = SubTypeValidator.instance();

        assertThrows(JsonMappingException.class, () -> validator.validateSubType(ctxt, disallowedType1));
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(ctxt, disallowedType2));

        String disallowedFullClassName1 = SubTypeValidator.class.getName();
        String disallowedFullClassName2 = Object.class.getName();

        JavaType disallowedType3 = mapper.getTypeFactory().constructType("org.apache.commons.collections4.functors.InstantiateTransformer");
        JavaType disallowedType4 = mapper.getTypeFactory().constructType("com.sun.rowset.JdbcRowSetImpl");

        assertThrows(JsonMappingException.class, () -> validator.validateSubType(ctxt, disallowedType3));
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(ctxt, disallowedType4));
    }

    @Test
    public void testValidateSubTypeWithSpringClasses() throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        JavaType springType1 = mapper.getTypeFactory().constructType("org.springframework.beans.factory.config.PropertyPathFactoryBean");
        JavaType springType2 = mapper.getTypeFactory().constructType("org.springframework.beans.factory.ObjectFactory");

        SubTypeValidator validator = SubTypeValidator.instance();

        assertThrows(JsonMappingException.class, () -> validator.validateSubType(ctxt, springType1));
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(ctxt, springType2));
    }
}