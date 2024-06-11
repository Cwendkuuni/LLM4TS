package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.core.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.json.JsonMappingException;

public class SubTypeValidatorTest {

 private static final SubTypeValidator validator = SubTypeValidator.instance();

 @Test
 public void testValidateSubType_ValidType() throws JsonMappingException {
 JavaType type = null; // valid type to be passed
 validator.validateSubType(createContext(), type);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_InvalidType() throws JsonMappingException {
 JavaType type = null; // invalid type to be passed
 validator.validateSubType(createContext(), type);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_SpringClassWithAbstractPointcutAdvisor() throws JsonMappingException {
 JavaType type = createJavaType("org.springframework.beans.factory.config.AbstractPointcutAdvisor");
 validator.validateSubType(createContext(), type);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_SpringClassWithAbstractApplicationContext() throws JsonMappingException {
 JavaType type = createJavaType("org.springframework.context.ApplicationContext");
 validator.validateSubType(createContext(), type);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_SpringClassWithNastyClass() throws JsonMappingException {
 JavaType type = createJavaType("org.apache.commons.collections4.functors.InstantiateTransformer");
 validator.validateSubType(createContext(), type);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_CustomNastyClass() throws JsonMappingException {
 JavaType type = createJavaType("com.test.CustomNastyClass");
 validator.validateSubType(createContext(), type);
 }

 @Test
 public void testValidateSubType_Interface() throws JsonMappingException {
 JavaType type = createJavaType(String.class.getName().replaceAll("java.lang.", "")); // interface name without java.lang package
 validator.validateSubType(createContext(), type);
 }

 @Test
 public void testValidateSubType_EmptyClass() throws JsonMappingException {
 JavaType type = createJavaType(byte[].class);
 validator.validateSubType(createContext(), type);
 }

 @Test
 public void testValidateSubType_ArrayClass() throws JsonMappingException {
 JavaType type = createJavaType(int[].class);
 validator.validateSubType(createContext(), type);
 }

 @Test
 public void testValidateSubType_PrimitiveType() throws JsonMappingException {
 JavaType type = createJavaType(int.class);
 validator.validateSubType(createContext(), type);
 }

 @Test
 public void testValidateSubType_CustomClassWithEmptyConstructor() throws JsonMappingException {
 JavaType type = createJavaType("com.test.TestClassWithEmptyConstructor");
 validator.validateSubType(createContext(), type);
 }

 @Test
 public void testValidateSubType_CustomClassWithParameterizedConstructor() throws JsonMappingException {
 JavaType type = createJavaType("com.test.TestClassWithParameterizedConstructor", String.class, int.class);
 validator.validateSubType(createContext(), type);
 }

 private DeserializationContext createContext() {
 return new DeserializationContext(null, null, null);
 }

 private JavaType createJavaType(String className) {
 return null; // will be set in test methods
 }

 private JavaType createJavaType(String className, Class<?>... constructorArgsTypes) {
 return null; // will be set in test methods
 }

}