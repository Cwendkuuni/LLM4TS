package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.type.ClassIdType;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonFactory;

public class SubTypeValidatorTest {

 private static ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testValidateSubType_ValidType() throws Exception {
 JavaType validType = mapper.getTypeFactory().constructType(String.class);
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(null, validType);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_InvalidType() throws Exception {
 JavaType invalidType = mapper.getTypeFactory().constructType(SubTypeValidator.class);
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(null, invalidType);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_SpringClass() throws Exception {
 JavaType springClassType = mapper.getTypeFactory().constructType("org.springframework.beans.factory.config.PropertyPathFactoryBean");
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(null, springClassType);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_CustomNastyClass() throws Exception {
 JavaType customNastyClassType = mapper.getTypeFactory().constructType("com.custom.package.NastyClass");
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(null, customNastyClassType);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_NestedSpringClass() throws Exception {
 JavaType nestedSpringClassType = mapper.getTypeFactory().constructType("com.custom.package.SpringNestedClass");
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(null, nestedSpringClassType);
 }

 @Test
 public void testValidateSubType_JavaBuiltInClass() throws Exception {
 JavaType javaBuiltInClassType = mapper.getTypeFactory().constructType(String[].class);
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(null, javaBuiltInClassType);
 }

 @Test
 public void testValidateSubType_ClassWithPrefix() throws Exception {
 JavaType classWithPrefixType = mapper.getTypeFactory().constructType("com.custom.package.MyPrefixClass");
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(null, classWithPrefixType);
 }
}