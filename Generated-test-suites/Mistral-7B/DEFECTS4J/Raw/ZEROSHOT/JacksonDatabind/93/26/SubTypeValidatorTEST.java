package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;

import java.util.Collections;

import org.junit.Test;

import com.fasterxml.jackson.core.type.JavaType;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.SubTypeValidator;

public class SubTypeValidatorTest {

 private static ObjectMapper objectMapper = new ObjectMapper();

 @Test
 public void testValidSubType() throws JsonMappingException {
 SubTypeValidator validator = SubTypeValidator.instance();
 JavaType validType = objectMapper.getTypeFactory().constructType(String.class);
 validator.validateSubType(objectMapper.getDeserializationContext(), validType);
 }

 @Test
 public void testInvalidSubType_NastyClassName() throws JsonMappingException {
 SubTypeValidator validator = SubTypeValidator.instance();
 JavaType invalidType = objectMapper.getTypeFactory()
 .constructType(com.fasterxml.jackson.databind.jsontype.impl.SubTypeValidator.class);
 validator.validateSubType(objectMapper.getDeserializationContext(), invalidType);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidSubType_SpringApplicationContext() throws JsonMappingException {
 SubTypeValidator validator = SubTypeValidator.instance();
 JavaType invalidType = objectMapper.getTypeFactory()
 .constructType(org.springframework.context.ApplicationContext.class);
 validator.validateSubType(objectMapper.getDeserializationContext(), invalidType);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidSubType_SpringPointcutAdvisor() throws JsonMappingException {
 SubTypeValidator validator = SubTypeValidator.instance();
 JavaType invalidType = objectMapper.getTypeFactory()
 .constructType(org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor.class);
 validator.validateSubType(objectMapper.getDeserializationContext(), invalidType);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidSubType_JavaLoggingFileHandler() throws JsonMappingException {
 SubTypeValidator validator = SubTypeValidator.instance();
 JavaType invalidType = objectMapper.getTypeFactory()
 .constructType(java.util.logging.FileHandler.class);
 validator.validateSubType(objectMapper.getDeserializationContext(), invalidType);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidSubType_JndiRefForwardingDataSource() throws JsonMappingException {
 SubTypeValidator validator = SubTypeValidator.instance();
 JavaType invalidType = objectMapper.getTypeFactory()
 .constructType(com.mchange.v2.c3p0.JndiRefForwardingDataSource.class);
 validator.validateSubType(objectMapper.getDeserializationContext(), invalidType);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidSubType_SunBcelClassLoader() throws JsonMappingException {
 SubTypeValidator validator = SubTypeValidator.instance();
 JavaType invalidType = objectMapper.getTypeFactory()
 .constructType(com.sun.org.apache.bcel.internal.util.ClassLoader.class);
 validator.validateSubType(objectMapper.getDeserializationContext(), invalidType);
 }

 @Test
 public void testNoExceptionThrownForDefaultType() throws JsonMappingException {
 SubTypeValidator validator = SubTypeValidator.instance();
 JavaType validType = objectMapper.getTypeFactory().constructType(Integer.class);
 validator.validateSubType(objectMapper.getDeserializationContext(), validType);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidSubType_CustomClass() throws JsonMappingException {
 SubTypeValidator validator = SubTypeValidator.instance();
 JavaType invalidType = objectMapper.getTypeFactory()
 .constructType(new CustomClass().getClass());
 validator.validateSubType(objectMapper.getDeserializationContext(), invalidType);
 }

 private static class CustomClass {
 // empty class
 }
}