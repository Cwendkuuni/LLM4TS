package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;

public class SubTypeValidatorTest {

 private SubTypeValidator validator = SubTypeValidator.instance();

 @Test
 public void testValidateSubType_ValidType() {
 JavaType validType = JavaType.construct(String.class);
 validator.validateSubType(null, validType);
 }

 @Test
 public void testValidateSubType_NastyClass_WithPrefixSpring() {
 JavaType nastyType = JavaType.construct(org.springframework.beans.factory.config.PropertyPathFactoryBean.class);
 try {
 validator.validateSubType(null, nastyType);
 fail("Expected JsonMappingException to be thrown");
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test
 public void testValidateSubType_NastyClass_NotStartingWithSpring() {
 JavaType nastyType = JavaType.construct(java.lang.String.class);
 try {
 validator.validateSubType(null, nastyType);
 // Should not reach here
 } catch (JsonMappingException e) {
 fail("Unexpected JsonMappingException was thrown");
 }
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_NastyClass_Interface() {
 JavaType nastyType = JavaType.construct(com.fasterxml.jackson.databind.JsonDeserializer.NullValueProvider[].class);
 validator.validateSubType(null, nastyType);
 }

 @Test
 public void testValidateSubType_NastyClass_InSuperclassChain_Spring() {
 JavaType nastyType = JavaType.construct(com.fasterxml.jackson.databind.BeanProperty.StdHandler[].class);
 try {
 validator.validateSubType(null, nastyType);
 fail("Expected JsonMappingException to be thrown");
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test
 public void testValidateSubType_NastyClass_InSuperclassChain_NotSpring() {
 JavaType nastyType = JavaType.construct(java.util.List.class);
 try {
 validator.validateSubType(null, nastyType);
 // Should not reach here
 } catch (JsonMappingException e) {
 fail("Unexpected JsonMappingException was thrown");
 }
 }

 @Test
 public void testValidateSubType_NullType() {
 try {
 validator.validateSubType(null, null);
 fail("Expected JsonMappingException to be thrown");
 } catch (JsonMappingException e) {
 // Expected
 }
 }
}