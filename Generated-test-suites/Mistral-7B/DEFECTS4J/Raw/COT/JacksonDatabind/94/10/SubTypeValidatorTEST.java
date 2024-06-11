package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.core.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

public class SubTypeValidatorTest {

 private SubTypeValidator validator = SubTypeValidator.instance();

 @Test
 public void testValidateSubType_NullType() {
  try {
   validator.validateSubType(null, null);
   fail("Expected JsonMappingException");
  } catch (JsonMappingException e) {
   // Expected
  }
 }

 @Test
 public void testValidateSubType_EmptyType() {
  JavaType emptyType = null;
  try {
   validator.validateSubType(new DeserializationContext(null), emptyType);
   fail("Expected JsonMappingException");
  } catch (JsonMappingException e) {
   // Expected
  }
 }

 @Test
 public void testValidateSubType_ValidType() {
  JavaType validType = null; // Initialize with some valid type
  validator.validateSubType(new DeserializationContext(null), validType);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_IllegalClass1() {
  JavaType illegalType1 = new ClassType(String.class);
  validator.validateSubType(new DeserializationContext(null), illegalType1);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_IllegalClass2() {
  JavaType illegalType2 = new ClassType(SubTypeValidator.class);
  validator.validateSubType(new DeserializationContext(null), illegalType2);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_SpringClass1() {
  JavaType springType1 = new ClassType(org.apache.commons.collections.functors.InvokerTransformer.class);
  validator.validateSubType(new DeserializationContext(null), springType1);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_SpringClass2() {
  JavaType springType2 = new ClassType(org.springframework.beans.factory.ObjectFactory.class);
  validator.validateSubType(new DeserializationContext(null), springType2);
 }

 @Test
 public void testValidateSubType_SpringInterface() {
  JavaType springInterface = new ClassType(org.springframework.beans.factory.config.PropertyPathFactoryBean.class.getInterfaces()[0]);
  validator.validateSubType(new DeserializationContext(null), new TypeFactory().constructType(springInterface.getRawType()));
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_SpringInterfaceWithSuperclass() {
  JavaType springInterface = new ClassType(org.springframework.beans.factory.config.PropertyPathFactoryBean.class.getInterfaces()[0]);
  JavaType springSuperclass = new ClassType(org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor.class);
  validator.validateSubType(new DeserializationContext(null), new TypeFactory().constructType(springInterface.getRawType(), springSuperclass));
 }

 @Test
 public void testValidateSubType_ThirdPartyClass1() {
  JavaType thirdPartyType1 = new ClassType(com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl.class);
  validator.validateSubType(new DeserializationContext(null), thirdPartyType1);
 }

 @Test
 public void testValidateSubType_ThirdPartyClass2() {
  JavaType thirdPartyType2 = new ClassType(org.apache.tomcat.dbcp.dbcp2.BasicDataSource.class);
  validator.validateSubType(new DeserializationContext(null), thirdPartyType2);
 }

}