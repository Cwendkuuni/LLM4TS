package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.core.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class SubTypeValidatorTest {

 private static final SubTypeValidator validator = SubTypeValidator.instance();

 @Test
 public void testValidDeserialization() throws Exception {
 JavaType validType = TypeFactory.defaultInstance().constructType(String.class);
 validator.validateSubType(createContext(), validType);
 }

 @Test
 public void testInvalidDeserializationWithIncludedClassName() throws Exception {
 JavaType invalidType1 = TypeFactory.defaultInstance().constructType(Object.class);
 validator.validateSubType(createContext(), invalidType1);

 JavaType invalidType2 = TypeFactory.defaultInstance().constructType(com.fasterxml.jackson.databind.jsontype.impl.SubTypeValidator.class);
 validator.validateSubType(createContext(), invalidType2);
 }

 @Test
 public void testInvalidDeserializationWithExcludedSpringClassName() throws Exception {
 JavaType springType = TypeFactory.defaultInstance().constructType(org.springframework.beans.factory.config.PropertyPathFactoryBean.class);
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(createContext(), springType));
 }

 @Test
 public void testInvalidDeserializationWithExcludedThirdPartyClassName() throws Exception {
 JavaType thirdPartyType = TypeFactory.defaultInstance().constructType(com.mchange.v2.c3p0.ComboPooledDataSource.class);
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(createContext(), thirdPartyType));
 }

 @Test
 public void testInvalidDeserializationWithExcludedSpringSuperClass() throws Exception {
 Class<?> abstractPointcutAdvisorClass = Class.forName("org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor");
 JavaType abstractPointcutAdvisorType = TypeFactory.defaultInstance().constructType(abstractPointcutAdvisorClass);
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(createContext(), abstractPointcutAdvisorType));
 }

 @Test
 public void testInvalidDeserializationWithExcludedSpringInterface() throws Exception {
 Class<?> abstractApplicationContextClass = Class.forName("org.springframework.beans.factory.config.AbstractApplicationContext");
 JavaType abstractApplicationContextType = TypeFactory.defaultInstance().constructType(abstractApplicationContextClass.asInterface());
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(createContext(), abstractApplicationContextType));
 }

 @Test
 public void testInvalidDeserializationWithExcludedSpringInterfaceInSuperclassChain() throws Exception {
 Class<?> abstractPointcutAdvisorSuperclass = Class.forName("org.springframework.aop.support.AbstractPointcut");
 JavaType abstractPointcutAdvisorType = TypeFactory.defaultInstance().constructType(Class.forName("org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor"));
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(createContext(), abstractPointcutAdvisorType));
 }

 @Test
 public void testInvalidDeserializationWithExcludedSpringInterfaceMultipleInheritance() throws Exception {
 Class<?> abstractPointcutAdvisorMultipleInheritance = Class.forName("org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor$1");
 JavaType abstractPointcutAdvisorMultipleInheritanceType = TypeFactory.defaultInstance().constructType(abstractPointcutAdvisorMultipleInheritance);
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(createContext(), abstractPointcutAdvisorMultipleInheritanceType));
 }

 private DeserializationContext createContext() {
 return new TestContext();
 }

 private static class TestContext extends DeserializationContext {
 }
}