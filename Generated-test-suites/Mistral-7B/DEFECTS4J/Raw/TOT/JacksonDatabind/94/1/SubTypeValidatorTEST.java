package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertThrows;

public class SubTypeValidatorTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new JsonMapper();
 objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 }

 @Test
 public void testValidateSubType_ValidClass() throws Exception {
 final JavaType validClassType = objectMapper.getTypeFactory().constructType(String.class);
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(objectMapper.getDeserializationContext(), validClassType);
 }

 @Test
 public void testValidateSubType_InvalidClass() throws Exception {
 final JavaType invalidClassType = objectMapper.getTypeFactory().constructType(SubTypeValidator.class);
 SubTypeValidator validator = SubTypeValidator.instance();
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(objectMapper.getDeserializationContext(), invalidClassType));
 }

 @Test
 public void testValidateSubType_SpringClassWithSubClassThatMatches() throws Exception {
 final String springPrefix = SubTypeValidator.PREFIX_SPRING;
 final JavaType springBaseType = objectMapper.getTypeFactory().constructType("org.springframework.beans.factory.config.BeanDefinition");
 final JavaType springSubclassType = objectMapper.getTypeFactory().constructType("org.springframework.beans.factory.config.PropertyPathFactoryBean");
 SubTypeValidator validator = SubTypeValidator.instance();
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(objectMapper.getDeserializationContext(), springSubclassType));
 final Class<? extends BeanDefinition> springBaseClass = (Class<? extends BeanDefinition>) springBaseType.getRawClass();
 final Class<? extends PropertyPathFactoryBean> springSubclass = (Class<? extends PropertyPathFactoryBean>) springSubclassType.getRawClass();
 Class<?> springSubclassSuperclass = springSubclass.getSuperclass();
 while (springSubclassSuperclass != null && !springSubclassSuperclass.isInterface()) {
 if (springSubclassSuperclass.getName().startsWith(springPrefix)) {
 Class<?> subClass = springSubclassSuperclass;
 while (subClass != null && !"AbstractApplicationContext".equals(subClass.getSimpleName())) {
 if ("AbstractPointcutAdvisor".equals(subClass.getSimpleName())) {
 break;
 }
 subClass = subClass.getSuperclass();
 }
 if (subClass != null && "AbstractPointcutAdvisor".equals(subClass.getSimpleName())) {
 break;
 }
 }
 springSubclassSuperclass = springSubclassSuperclass.getSuperclass();
 }
 }

 @Test
 public void testValidateSubType_SpringClassWithSubclassThatDoesNotMatch() throws Exception {
 final JavaType springClassType = objectMapper.getTypeFactory().constructType("org.springframework.beans.factory.config.PropertyPathFactoryBean");
 SubTypeValidator validator = SubTypeValidator.instance();
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(objectMapper.getDeserializationContext(), springClassType));
 }

 @Test
 public void testValidateSubType_ThirdPartyClassWithSuperclassThatMatches() throws Exception {
 final String thirdPartyPrefix = "com.mchange.v2.c3p0.";
 final JavaType thirdPartyType = objectMapper.getTypeFactory().constructType(new TypeReference<WrapperConnectionPoolDataSource>() {}.getType());
 SubTypeValidator validator = SubTypeValidator.instance();
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(objectMapper.getDeserializationContext(), thirdPartyType));
 Class<?> thirdPartySuperclass = thirdPartyType.getRawClass().getSuperclass();
 while (thirdPartySuperclass != null && !thirdPartySuperclass.isInterface()) {
 if (thirdPartySuperclass.getName().startsWith(thirdPartyPrefix)) {
 Class<?> superclass = thirdPartySuperclass;
 while (superclass != null && !"ComboPooledDataSource".equals(superclass.getSimpleName())) {
 if ("JdbcConnectionPool".equals(superclass.getSimpleName())) {
 break;
 }
 superclass = superclass.getSuperclass();
 }
 if (superclass != null && "JdbcConnectionPool".equals(superclass.getSimpleName())) {
 break;
 }
 }
 thirdPartySuperclass = thirdPartySuperclass.getSuperclass();
 }
 }

 @Test
 public void testValidateSubType_ThirdPartyClassWithSuperclassThatDoesNotMatch() throws Exception {
 final JavaType thirdPartyType = objectMapper.getTypeFactory().constructType("org.apache.tomcat.dbcp.dbcp2.BasicDataSource");
 SubTypeValidator validator = SubTypeValidator.instance();
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(objectMapper.getDeserializationContext(), thirdPartyType));
 }
}