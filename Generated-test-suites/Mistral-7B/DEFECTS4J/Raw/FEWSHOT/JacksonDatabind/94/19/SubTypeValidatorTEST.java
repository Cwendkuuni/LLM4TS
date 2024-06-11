package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert. assertThrows;
import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

public class SubTypeValidatorTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testValidateSubTypeWithValidClass() throws JsonMappingException {
 JavaType validClassType = objectMapper.getTypeFactory().constructType(String.class);
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(objectMapper.getDeserializationContext(), validClassType);
 }

 @Test
 public void testValidateSubTypeWithNastyClass() throws JsonMappingException {
 JavaType nastyClassType = objectMapper.getTypeFactory()
 .constructType(new TypeReference<Class<?>>() {});
 nastyClassType = objectMapper.getTypeFactory().constructType(nastyClassType.getRawType().getName());
 SubTypeValidator validator = SubTypeValidator.instance();
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(objectMapper.getDeserializationContext(), nastyClassType));
 }

 @Test
 public void testValidateSubTypeWithSpringClass() throws JsonMappingException {
 JavaType springClassType = objectMapper.getTypeFactory()
 .constructType("org.springframework.beans.factory.config.PropertyPathFactoryBean");
 SubTypeValidator validator = SubTypeValidator.instance();
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(objectMapper.getDeserializationContext(), springClassType));
 }

 @Test
 public void testValidateSubTypeWithSubSpringClass() throws JsonMappingException {
 JavaType subSpringClassType = objectMapper.getTypeFactory()
 .constructType("com.example.MyClassThatExtendsPropertyPathFactoryBean");
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(objectMapper.getDeserializationContext(), subSpringClassType);
 }

 @Test
 public void testValidateSubTypeWithInterface() throws JsonMappingException {
 JavaType interfaceType = objectMapper.getTypeFactory()
 .constructType(Iterable.class);
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(objectMapper.getDeserializationContext(), interfaceType);
 }

 @Test
 public void testValidateSubTypeWithClassStartingWithSpringPrefix() throws JsonMappingException {
 JavaType springPrefixClassType = objectMapper.getTypeFactory()
 .constructType("org.springframework.MyCustomClass");
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(objectMapper.getDeserializationContext(), springPrefixClassType);
 }

 @Test
 public void testValidateSubTypeWithClassNotStartingWithSpringPrefix() throws JsonMappingException {
 JavaType notSpringPrefixClassType = objectMapper.getTypeFactory()
 .constructType("com.example.MyCustomClass");
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(objectMapper.getDeserializationContext(), notSpringPrefixClassType);
 }
}