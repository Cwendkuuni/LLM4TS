package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;

import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SubTypeValidatorTest {

 private ObjectMapper objectMapper = new ObjectMapper();

 @Test
 public void testValidateSubTypeWithValidClass() throws JsonMappingException {
 SubTypeValidator validator = SubTypeValidator.instance();
 JavaType validType = objectMapper.getTypeFactory().constructType(String.class);
 validator.validateSubType(new DeserializationContext(null), validType);
 }

 @Test
 public void testValidateSubTypeWithNastyClass() throws JsonMappingException {
 SubTypeValidator validator = SubTypeValidator.instance();
 JavaType nastyType = objectMapper.getTypeFactory().constructType(String[].class);
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(new DeserializationContext(null), nastyType));
 }

 @Test
 public void testValidateSubTypeWithSpringClass() throws JsonMappingException {
 SubTypeValidator validator = SubTypeValidator.instance();
 JavaType springType = objectMapper.getTypeFactory().constructType(AbstractApplicationContext.class);
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(new DeserializationContext(null), springType));
 }

 @Test
 public void testValidateSubTypeWithSpringInterface() throws JsonMappingException {
 SubTypeValidator validator = SubTypeValidator.instance();
 JavaType springInterfaceType = objectMapper.getTypeFactory().constructType(AbstractPointcutAdvisor.class);
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(new DeserializationContext(null), springInterfaceType));
 }

 @Test
 public void testValidateSubTypeWithSpringClassWithSuperClassCheck() throws JsonMappingException {
 SubTypeValidator validator = SubTypeValidator.instance();
 JavaType springWithSuperClassType = objectMapper.getTypeFactory().constructType(AbstractPointcutAdvisor.class.getSuperclass());
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(new DeserializationContext(null), springWithSuperClassType));
 }

 @Test
 public void testValidateSubTypeWithCustomClass() throws JsonMappingException {
 SubTypeValidator validator = SubTypeValidator.instance();
 JavaType customType = objectMapper.getTypeFactory().constructType(MyCustomClass.class);
 validator.validateSubType(new DeserializationContext(null), customType);
 }

 @Test
 public void testValidateSubTypeWithCustomInterface() throws JsonMappingException {
 SubTypeValidator validator = SubTypeValidator.instance();
 JavaType customInterfaceType = objectMapper.getTypeFactory().constructType(MyCustomInterface.class);
 validator.validateSubType(new DeserializationContext(null), customInterfaceType);
 }
}

class MyCustomClass {
 // Empty custom class
}

interface MyCustomInterface {
 // Empty custom interface
}