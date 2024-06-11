package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

public class SubTypeValidatorTest {

 private SubTypeValidator validator = SubTypeValidator.instance();

 @Test
 public void testValidTypes() {
 // Test valid types, which should not throw an exception
 JavaType validType1 = getType("java.lang.String");
 validator.validateSubType(createContext(), validType1);
 JavaType validType2 = getType("com.example.MyValidClass");
 validator.validateSubType(createContext(), validType2);
 }

 @Test
 public void testInvalidTypes() {
 // Test invalid types, which should throw an exception
 JavaType invalidType1 = getType("java.lang.Runtime");
 try {
 validator.validateSubType(createContext(), invalidType1);
 fail("Expected JsonMappingException for type 'java.lang.Runtime'");
 } catch (JsonMappingException e) {
 // Expected
 }
 JavaType invalidType2 = getType("org.apache.commons.collections.functors.InvokerTransformer");
 try {
 validator.validateSubType(createContext(), invalidType2);
 fail("Expected JsonMappingException for type 'org.apache.commons.collections.functors.InvokerTransformer'");
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test
 public void testSubTypesWithSpringPrefix() {
 // Test subtypes with Spring prefix, which should be checked more deeply
 JavaType springSubType1 = getType("org.springframework.beans.factory.config.PropertyPathFactoryBean");
 try {
 validator.validateSubType(createContext(), springSubType1);
 fail("Expected JsonMappingException for type 'org.springframework.beans.factory.config.PropertyPathFactoryBean'");
 } catch (JsonMappingException e) {
 // Expected
 }
 JavaType springSubType2 = getType("com.example.MySpringSubClass");
 try {
 validator.validateSubType(createContext(), springSubType2);
 fail("Expected JsonMappingException for type 'com.example.MySpringSubClass' with Spring prefix");
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test
 public void testEmptyType() {
 // Test empty type, which should not throw an exception
 validator.validateSubType(createContext(), getType(Collections.emptyList().getClass()));
 }

 private DeserializationContext createContext() {
 return new DeserializationContext(null, null, null);
 }

 private JavaType getType(Class<?> type) {
 return new JavaType(type);
 }

}