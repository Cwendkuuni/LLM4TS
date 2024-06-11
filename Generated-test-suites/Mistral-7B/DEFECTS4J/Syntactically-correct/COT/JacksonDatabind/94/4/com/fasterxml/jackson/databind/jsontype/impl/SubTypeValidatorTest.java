package com.yourpackage.test;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;

public class SubTypeValidatorTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @Test
 public void testInstance() {
 SubTypeValidator validator = SubTypeValidator.instance();
 assert null != validator;
 }

 @Test
 public void testValidateSubType_legalType() throws JsonMappingException {
 JavaType type = MAPPER.getTypeFactory().constructType(String.class);
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(null, type);
 }

 @Test
 public void testValidateSubType_illegalType_baseClass() throws JsonMappingException {
 String illegalClassName = "java.rmi.server.UnicastRemoteObject";
 JavaType type = MAPPER.getTypeFactory().constructType(illegalClassName);
 SubTypeValidator validator = SubTypeValidator.instance();

 try {
 validator.validateSubType(null, type);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 String expectedMessage = String.format("Illegal type (%s) to deserialize: prevented for security reasons", illegalClassName);
 String actualMessage = e.getMessage();
 assertTrue(expectedMessage.contains(actualMessage));
 }
 }

 @Test
 public void testValidateSubType_illegalType_interface() throws JsonMappingException {
 String interfaceName = "com.fasterxml.jackson.databind.JsonDeserializer";
 JavaType type = MAPPER.getTypeFactory().constructType(interfaceName);
 SubTypeValidator validator = SubTypeValidator.instance();

 try {
 validator.validateSubType(null, type);
 } catch (JsonMappingException e) {
 // Interfaces are not checked for illegal types, so no exception should be thrown
 }
 }

 @Test
 public void testValidateSubType_illegalType_spring() throws JsonMappingException {
 String springClassName = "org.springframework.beans.factory.config.PropertyPathFactoryBean";
 JavaType type = MAPPER.getTypeFactory().constructType(springClassName);
 SubTypeValidator validator = SubTypeValidator.instance();

 try {
 validator.validateSubType(null, type);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 String expectedMessage = String.format("Illegal type (%s) to deserialize: prevented for security reasons", springClassName);
 String actualMessage = e.getMessage();
 assertTrue(expectedMessage.contains(actualMessage));
 }
 }

 @Test
 public void testValidateSubType_illegalType_thirdparty() throws JsonMappingException {
 String thirdPartyClassName = "com.mchange.v2.c3p0.ComboPooledDataSource";
 JavaType type = MAPPER.getTypeFactory().constructType(thirdPartyClassName);
 SubTypeValidator validator = SubTypeValidator.instance();

 try {
 validator.validateSubType(null, type);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 String expectedMessage = String.format("Illegal type (%s) to deserialize: prevented for security reasons", thirdPartyClassName);
 String actualMessage = e.getMessage();
 assertTrue(expectedMessage.contains(actualMessage));
 }
 }

 @Test(expected = JsonProcessingException.class)
 public void testValidateSubType_nullType() throws JsonMappingException {
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(null, null);
 }

 @Test
 public void testValidateSubType_emptyType() throws JsonMappingException {
 JavaType emptyType = MAPPER.getTypeFactory().constructType(Collections.class);
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(null, emptyType);
 }
}