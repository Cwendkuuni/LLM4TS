package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class SubTypeValidatorTest {

 private static final SubTypeValidator instance = SubTypeValidator.instance();

 @Test
 public void testValidateSubTypeWithValidType() throws JsonMappingException {
 JavaType validType = TypeFactory.defaultInstance().constructType(String.class);
 instance.validateSubType(createContext(), validType);
 }

 @Test
 public void testValidateSubTypeWithBlacklistedClass() throws JsonMappingException {
 JavaType blacklistedType = TypeFactory.defaultInstance().constructType(org.apache.commons.collections4.functors.InstantiateTransformer.class);
 try {
 instance.validateSubType(createContext(), blacklistedType);
 fail("Expected JsonMappingException to be thrown");
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test
 public void testValidateSubTypeWithSpringClass() throws JsonMappingException {
 JavaType springClassType = TypeFactory.defaultInstance().constructType(org.springframework.beans.factory.ObjectFactory.class);
 instance.validateSubType(createContext(), springClassType);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubTypeWithBlacklistedSpringClass() throws JsonMappingException {
 JavaType blacklistedSpringType = TypeFactory.defaultInstance().constructType(org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor.class);
 instance.validateSubType(createContext(), blacklistedSpringType);
 }

 @Test
 public void testValidateSubTypeWithBlacklistedClassWithSuperClassCheck() throws JsonMappingException {
 JavaType blacklistedWithSuperClassType = TypeFactory.defaultInstance().constructType(org.apache.commons.collections4.functors.InvokerTransformer.class);
 try {
 instance.validateSubType(createContext(), blacklistedWithSuperClassType);
 fail("Expected JsonMappingException to be thrown");
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test
 public void testValidateSubTypeWithBlacklistedClassWithSimpleNameCheck() throws JsonMappingException {
 JavaType blacklistedSimpleNameType = TypeFactory.defaultInstance().constructType(AbstractPointcutAdvisor.class);
 try {
 instance.validateSubType(createContext(), blacklistedSimpleNameType);
 fail("Expected JsonMappingException to be thrown");
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test
 public void testValidateSubTypeWithBlacklistedJdkClass() throws JsonMappingException {
 JavaType jdkClassType = TypeFactory.defaultInstance().constructType(java.util.logging.FileHandler.class);
 try {
 instance.validateSubType(createContext(), jdkClassType);
 fail("Expected JsonMappingException to be thrown");
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubTypeWithBlacklistedThirdPartyClass() throws JsonMappingException {
 JavaType thirdPartyClassType = TypeFactory.defaultInstance().constructType(com.mchange.v2.c3p0.WrapperConnectionPoolDataSource.class);
 instance.validateSubType(createContext(), thirdPartyClassType);
 }

 @Test
 public void testValidateSubTypeWithBlacklistedThirdPartyClassWithSimpleNameCheck() throws JsonMappingException {
 JavaType thirdPartySimpleNameType = TypeFactory.defaultInstance().constructType(com.mchange.v2.c3p0.BasicDataSource.class);
 try {
 instance.validateSubType(createContext(), thirdPartySimpleNameType);
 fail("Expected JsonMappingException to be thrown");
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 private DeserializationContext createContext() {
 return new DeserializationContext(createTestInstance(), new StandardDeserializer(), Collections.emptyBeanContext(), Collections.emptyMap(), null, new ObjectMapper());
 }

 private Object createTestInstance() {
 return new Object() {};
 }

}