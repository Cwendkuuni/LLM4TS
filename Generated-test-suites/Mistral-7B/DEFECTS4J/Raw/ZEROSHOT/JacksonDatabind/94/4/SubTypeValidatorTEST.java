package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class SubTypeValidatorTest {

 private static SubTypeValidator validator;

 @BeforeClass
 public static void setup() {
 validator = SubTypeValidator.instance();
 }

 @Test
 public void testValidateSubTypeWithValidType() {
 JavaType validType = TypeFactory.defaultInstance().constructType(String.class);
 validator.validateSubType(createContext(), validType);
 }

 @Test
 public void testValidateSubTypeWithSpringClass() {
 JavaType springClassType = TypeFactory.defaultInstance().constructType(String.class, "org.springframework.beans.factory.config.PropertyPathFactoryBean");
 validator.validateSubType(createContext(), springClassType);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubTypeWithBlacklistedType() {
 JavaType blacklistedType = TypeFactory.defaultInstance().constructType(String.class, "org.apache.commons.collections4.functors.InstantiateTransformer");
 validator.validateSubType(createContext(), blacklistedType);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubTypeWithSpringInterface() {
 JavaType springInterfaceType = TypeFactory.defaultInstance().constructType(String.class, "org.springframework.beans.factory.config.AbstractApplicationContext");
 validator.validateSubType(createContext(), springInterfaceType);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubTypeWithBlacklistedInterface() {
 JavaType blacklistedInterfaceType = TypeFactory.defaultInstance().constructType(String.class, "org.apache.commons.collections.functors.InstantiateTransformer");
 validator.validateSubType(createContext(), blacklistedInterfaceType);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubTypeWithBlacklistedClassInHierarchy() {
 JavaType blacklistedClassType = TypeFactory.defaultInstance().constructType(String.class, "org.apache.commons.collections4.functors.Transformer");
 validator.validateSubType(createContext(), blacklistedClassType);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubTypeWithJdkClass() {
 JavaType jdkClassType = TypeFactory.defaultInstance().constructType(String.class, "java.rmi.server.UnicastRemoteObject");
 validator.validateSubType(createContext(), jdkClassType);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubTypeWithThirdPartyClass() {
 JavaType thirdPartyClassType = TypeFactory.defaultInstance().constructType(String.class, "com.sun.org.apache.bcel.internal.util.ClassLoader");
 validator.validateSubType(createContext(), thirdPartyClassType);
 }

 private DeserializationContext createContext() {
 return new DeserializationContext(null, null, null);
 }
}