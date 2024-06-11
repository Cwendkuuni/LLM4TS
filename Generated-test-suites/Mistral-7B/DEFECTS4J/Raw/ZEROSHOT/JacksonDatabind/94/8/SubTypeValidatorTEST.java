package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.jsontype.SubTypeValidator;

public class SubTypeValidatorTest {

 private static SubTypeValidator validator;

 @BeforeClass
 public static void setup() {
 validator = SubTypeValidator.instance();
 }

 @Test
 public void testValidateSubTypeNullType() {
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(null, null));
 }

 @Test
 public void testValidateSubTypeNullContext() {
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(null, new JavaType() {}));
 }

 @Test
 public void testValidateSubTypeValidType() {
 JavaType validType = new JavaType(String.class);
 validator.validateSubType(null, validType);
 }

 @Test
 public void testValidateSubTypeNastyClass() {
 // Given
 JavaType nastyType = new JavaType(com.mchange.v2.c3p0.ComboPooledDataSource.class);

 // When, Then
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(null, nastyType));
 }

 @Test
 public void testValidateSubTypeSpringInterface() {
 // Given
 JavaType springInterfaceType = new JavaType(Runnable.class);

 // When, Then
 validator.validateSubType(null, springInterfaceType);
 }

 @Test
 public void testValidateSubTypeSpringClass() {
 // Given
 JavaType springClassType = new JavaType(org.springframework.beans.factory.config.PropertyPathFactoryBean.class);

 // When, Then
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(null, springClassType));
 }

 @Test
 public void testValidateSubTypeSpringSuperClass() {
 // Given
 JavaType springSuperClassType = new JavaType(com.fasterxml.jackson.databind.jsontype.impl.AbstractApplicationContext.class);
 JavaType springSubClassType = new JavaType(org.springframework.beans.factory.config.PropertyPathFactoryBean.class);

 // When, Then
 validator.validateSubType(null, springSuperClassType);
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(null, springSubClassType));
 }

 @Test
 public void testValidateSubTypeSpringAbstractPointcutAdvisor() {
 // Given
 JavaType springAbstractPointcutAdvisorType = new JavaType(org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor.class);

 // When, Then
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(null, springAbstractPointcutAdvisorType));
 }

 @Test
 public void testValidateSubTypeSpringNastyClass() {
 // Given
 JavaType springNastyType = new JavaType(com.sun.org.apache.bcel.internal.util.ClassLoader.class);

 // When, Then
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(null, springNastyType));
 }
}