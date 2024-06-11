package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;

import java.util.Collections;

import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class SubTypeValidatorTest {

 private static ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testValidType() throws JsonMappingException {
 SubTypeValidator validator = SubTypeValidator.instance();
 JavaType validType = mapper.getTypeFactory().constructType(String.class);
 validator.validateSubType(mockContext(), validType);
 }

 @Test
 public void testNastyClass() throws JsonMappingException {
 SubTypeValidator validator = SubTypeValidator.instance();
 JavaType nastyClassType = mapper.getTypeFactory().constructType(org.apache.commons.collections.functors.InvokerTransformer.class);
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(mockContext(), nastyClassType));
 }

 @Test
 public void testSpringApplicationContext() throws JsonMappingException {
 SubTypeValidator validator = SubTypeValidator.instance();
 JavaType applicationContextType = mapper.getTypeFactory().constructType(org.springframework.context.ApplicationContext.class);
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(mockContext(), applicationContextType));
 }

 @Test
 public void testSpringBeanFactory() throws JsonMappingException {
 SubTypeValidator validator = SubTypeValidator.instance();
 JavaType beanFactoryType = mapper.getTypeFactory().constructType(org.springframework.beans.factory.config.PropertyPathFactoryBean.class);
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(mockContext(), beanFactoryType));
 }

 @Test
 public void testJndiRefForwardingDataSource() throws JsonMappingException {
 SubTypeValidator validator = SubTypeValidator.instance();
 JavaType jndiRefForwardingDataSourceType = mapper.getTypeFactory().constructType(com.mchange.v2.c3p0.JndiRefForwardingDataSource.class);
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(mockContext(), jndiRefForwardingDataSourceType));
 }

 @Test
 public void testBasicDataSource() throws JsonMappingException {
 SubTypeValidator validator = SubTypeValidator.instance();
 JavaType basicDataSourceType = mapper.getTypeFactory().constructType(org.apache.tomcat.dbcp.dbcp2.BasicDataSource.class);
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(mockContext(), basicDataSourceType));
 }

 @Test
 public void testUnknownClass() throws JsonMappingException {
 SubTypeValidator validator = SubTypeValidator.instance();
 JavaType unknownClassType = mapper.getTypeFactory().constructType(UnknownClass.class);
 validator.validateSubType(mockContext(), unknownClassType);
 }

 private DeserializationContext mockContext() {
 DeserializationContext context = mapper.getDeserializationContext();
 context.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
 return context;
 }

 static class UnknownClass {}

}