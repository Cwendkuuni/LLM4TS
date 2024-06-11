package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

/**
 * Test cases for {@link SubTypeValidator} class
 *
 * @author John Doe
 */
public class SubTypeValidatorTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @Test
 public void testValidSubType() throws Exception {
 SubTypeValidator validator = SubTypeValidator.instance();
 JavaType validType = TypeFactory.defaultInstance().constructType(String.class);
 validator.validateSubType(mockContext(), validType);
 }

 @Test
 public void testInvalidSubType() throws Exception {
 SubTypeValidator validator = SubTypeValidator.instance();
 JavaType invalidType = TypeFactory.defaultInstance().constructType(Object.class);
 String fullName = invalidType.getRawClass().getName();
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(mockContext(), invalidType));
 }

 @Test
 public void testSpringComponentScanning() throws Exception {
 SubTypeValidator validator = SubTypeValidator.instance();
 JavaType springComponent = TypeFactory.defaultInstance()
 .constructType("com.example.MySpringComponent");
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(mockContext(), springComponent));
 }

 @Test
 public void testSpringBeanFactory() throws Exception {
 SubTypeValidator validator = SubTypeValidator.instance();
 JavaType beanFactory = TypeFactory.defaultInstance().constructType(org.springframework.beans.factory.config.BeanFactory.class);
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(mockContext(), beanFactory));
 }

 @Test
 public void testGroovyTransformers() throws Exception {
 SubTypeValidator validator = SubTypeValidator.instance();
 JavaType groovyTransformer = TypeFactory.defaultInstance()
 .constructType("org.codehaus.groovy.runtime.MethodClosure");
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(mockContext(), groovyTransformer));
 }

 @Test
 public void testApacheCommonsCollectionTransformers() throws Exception {
 SubTypeValidator validator = SubTypeValidator.instance();
 JavaType commonsTransformer = TypeFactory.defaultInstance()
 .constructType("org.apache.commons.collections.functors.InvokerTransformer");
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(mockContext(), commonsTransformer));
 }

 @Test
 public void testSpringDataSources() throws Exception {
 SubTypeValidator validator = SubTypeValidator.instance();
 JavaType c3p0DataSource = TypeFactory.defaultInstance()
 .constructType("com.mchange.v2.c3p0.WrapperConnectionPoolDataSource");
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(mockContext(), c3p0DataSource));
 JavaType tomcatDataSource = TypeFactory.defaultInstance()
 .constructType("org.apache.tomcat.dbcp.dbcp2.BasicDataSource");
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(mockContext(), tomcatDataSource));
 }

 @Test
 public void testJavaLoggingFileHandler() throws Exception {
 SubTypeValidator validator = SubTypeValidator.instance();
 JavaType loggingFileHandler = TypeFactory.defaultInstance()
 .constructType("java.util.logging.FileHandler");
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(mockContext(), loggingFileHandler));
 }

 private DeserializationContext mockContext() {
 return new DeserializationContext(MAPPER.getConfig());
 }
}