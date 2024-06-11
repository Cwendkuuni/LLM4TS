package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.core.type.JavaType;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaTypeFactory;

public class SubTypeValidatorTest {

 private static final JavaTypeFactory typeFactory = new JavaTypeFactory();
 private static final SubTypeValidator validator = SubTypeValidator.instance();

 @Test
 public void testValidType() throws JsonMappingException {
 Class<String> stringType = String.class;
 JavaType stringJavaType = typeFactory.constructType(stringType);
 validator.validateSubType(createContext(), stringJavaType);
 }

 @Test
 public void testNastyClass() throws JsonMappingException {
 Class<Object> objectType = Object.class;
 JavaType objectJavaType = typeFactory.constructType(objectType);
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(createContext(), objectJavaType));
 }

 @Test
 public void testSpringClass() throws JsonMappingException {
 Class<AbstractApplicationContext> springApplicationContextType = AbstractApplicationContext.class;
 JavaType springApplicationContextJavaType = typeFactory.constructType(springApplicationContextType);
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(createContext(), springApplicationContextJavaType));
 }

 @Test
 public void testSpringPointcutAdvisor() throws JsonMappingException {
 Class<AbstractPointcutAdvisor> springPointcutAdvisorType = AbstractPointcutAdvisor.class;
 JavaType springPointcutAdvisorJavaType = typeFactory.constructType(springPointcutAdvisorType);
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(createContext(), springPointcutAdvisorJavaType));
 }

 @Test
 public void testNonSpringClass() throws JsonMappingException {
 Class<Integer> integerType = Integer.class;
 JavaType integerJavaType = typeFactory.constructType(integerType);
 validator.validateSubType(createContext(), integerJavaType);
 }

 @Test
 public void testNastyClassWithPrefix() throws JsonMappingException {
 Class<com.mchange.v2.c3p0.WrapperConnectionPoolDataSource> nastyDataSourceType = WrapperConnectionPoolDataSource.class;
 JavaType nastyDataSourceJavaType = typeFactory.constructType(nastyDataSourceType);
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(createContext(), nastyDataSourceJavaType));
 }

 @Test
 public void testNastyClassWithoutPrefix() throws JsonMappingException {
 Class<com.mchange.v2.c3p0.JndiRefForwardingDataSource> nastyDataSourceType = JndiRefForwardingDataSource.class;
 JavaType nastyDataSourceJavaType = typeFactory.constructType(nastyDataSourceType);
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(createContext(), nastyDataSourceJavaType));
 }

 private DeserializationContext createContext() {
 return new TestContext();
 }

 private static class TestContext extends DeserializationContext {
 }

}