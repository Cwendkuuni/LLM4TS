package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;

import org.junit.Test;

import com.fasterxml.jackson.core.type.JavaType;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class SubTypeValidatorTest {

 private static final ObjectMapper objectMapper = new ObjectMapper();

 @Test
 public void testValidateSubTypeWithValidType() throws Exception {
 SubTypeValidator validator = SubTypeValidator.instance();
 JavaType validType = TypeFactory.defaultInstance().constructType(String.class);
 validator.validateSubType(mockContext(), validType);
 }

 @Test
 public void testValidateSubTypeWithInvalidType() throws Exception {
 SubTypeValidator validator = SubTypeValidator.instance();
 String fullClassName = "org.apache.commons.collections4.functors.InstantiateTransformer";
 JavaType invalidType = TypeFactory.defaultInstance().constructType(Class.forName(fullClassName));
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(mockContext(), invalidType));
 }

 @Test
 public void testValidateSubTypeWithSpringClass() throws Exception {
 SubTypeValidator validator = SubTypeValidator.instance();
 String fullClassName = "org.springframework.beans.factory.ObjectFactory";
 JavaType springType = TypeFactory.defaultInstance().constructType(Class.forName(fullClassName));
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(mockContext(), springType));
 }

 @Test
 public void testValidateSubTypeWithSpringSubClass() throws Exception {
 SubTypeValidator validator = SubTypeValidator.instance();
 String fullClassName = "org.springframework.beans.factory.config.PropertyPathFactoryBean";
 JavaType springSubClassType = TypeFactory.defaultInstance().constructType(Class.forName(fullClassName));
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(mockContext(), springSubClassType));
 }

 @Test
 public void testValidateSubTypeWithNastedSpringClass() throws Exception {
 SubTypeValidator validator = SubTypeValidator.instance();
 String fullClassName = "org.springframework.beans.factory.ObjectFactory";
 JavaType nestedSpringType = TypeFactory.defaultInstance()
 .constructType(Class.forName("$" + fullClassName.replaceAll("\\.", "\\$") + "$" + "AbstractPointcutAdvisor"));
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(mockContext(), nestedSpringType));
 }

 @Test
 public void testValidateSubTypeWithThirdPartyClass() throws Exception {
 SubTypeValidator validator = SubTypeValidator.instance();
 String fullClassName = "com.mchange.v2.c3p0.WrapperConnectionPoolDataSource";
 JavaType thirdPartyType = TypeFactory.defaultInstance().constructType(Class.forName(fullClassName));
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(mockContext(), thirdPartyType));
 }

 @Test
 public void testValidateSubTypeWithThirdPartySubClass() throws Exception {
 SubTypeValidator validator = SubTypeValidator.instance();
 String fullClassName = "com.mchange.v2.c3p0.PoolBackedDataSource";
 JavaType thirdPartySubClassType = TypeFactory.defaultInstance().constructType(Class.forName(fullClassName));
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(mockContext(), thirdPartySubClassType));
 }

 @Test
 public void testValidateSubTypeWithJdkClass() throws Exception {
 SubTypeValidator validator = SubTypeValidator.instance();
 String fullClassName = "java.util.logging.FileHandler";
 JavaType jdkType = TypeFactory.defaultInstance().constructType(Class.forName(fullClassName));
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(mockContext(), jdkType));
 }

 private DeserializationContext mockContext() {
 return new TestDeserializationContext();
 }

 private static class TestDeserializationContext extends DeserializationContext {
 @Override
 public void throwMappingException(String message) {
 // Do nothing
 }
 }
}