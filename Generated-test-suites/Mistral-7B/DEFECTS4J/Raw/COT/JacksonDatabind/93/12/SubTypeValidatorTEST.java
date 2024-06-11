package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

public class SubTypeValidatorTest {

 private SubTypeValidator validator = SubTypeValidator.instance();

 @Test
 public void testValidateSubType_withValidClass() throws JsonMappingException {
 JavaType validType = null; // initialize with some valid type
 validator.validateSubType(null, validType);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_withIllegalClass() throws JsonMappingException {
 String illegalfClassName = "org.apache.commons.collections4.functors.InstantiateTransformer";
 JavaType illegalType = null; // initialize with the illegal class name
 validator.validateSubType(null, illegalType);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_withSpringClass() throws JsonMappingException {
 String springClassName = "org.springframework.beans.factory.config.PropertyPathFactoryBean";
 JavaType springType = null; // initialize with the spring class name
 validator.validateSubType(null, springType);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_withNestedSpringClass() throws JsonMappingException {
 String nestedSpringClassName = "com.mchange.v2.c3p0.WrapperConnectionPoolDataSource";
 JavaType nestedSpringType = null; // initialize with the nested spring class name
 Class<?> nestedSpringClass = Class.forName(nestedSpringClassName);
 JavaType parentType = JavaType.construct(nestedSpringClass.getSuperclass(), null);
 validator.validateSubType(null, parentType);
 }

 @Test
 public void testValidateSubType_withPrefixString() throws JsonMappingException {
 String prefixStringClassName = "org.springframework.beans.factory.config.AbstractApplicationContext";
 JavaType prefixStringType = null; // initialize with the prefix string class name
 validator.validateSubType(null, prefixStringType);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_withIllegalPrefixStringClass() throws JsonMappingException {
 String illegalPrefixStringClassName = "org.apache.commons.collections4.functors.InstantiateTransformer";
 JavaType illegalPrefixStringType = null; // initialize with the illegal prefix string class name
 validator.validateSubType(null, illegalPrefixStringType);
 }

 @Test
 public void testValidateSubType_withNullType() {
 try {
 validator.validateSubType(null, null);
 fail("Should have thrown JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test
 public void testValidateSubType_withEmptyType() {
 try {
 validator.validateSubType(null, JavaType.construct(Collections.emptySet(), null));
 fail("Should have thrown JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test
 public void testValidateSubType_withNullContext() {
 try {
 validator.validateSubType(null, JavaType.construct(String.class, null));
 fail("Should have thrown JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test
 public void testValidateSubType_withEmptyContext() {
 DeserializationContext emptyContext = new DeserializationContext(null, new ObjectMapper());
 try {
 validator.validateSubType(emptyContext, JavaType.construct(String.class, null));
 fail("Should have thrown JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test
 public void testValidateSubType_withValidTypeSet() {
 Set<String> validClasses = new HashSet<String>();
 validClasses.add("com.example.ValidClass");
 validator = new SubTypeValidator() {{
 _cfgIllegalClassNames = Collections.unmodifiableSet(validClasses);
 }};
 JavaType validType = JavaType.construct(String.class, null);
 validator.validateSubType(null, validType);
 }

 @Test
 public void testValidateSubType_withCustomIllegalClasses() throws JsonMappingException {
 Set<String> customIllegalClasses = new HashSet<String>();
 customIllegalClasses.add("com.example.CustomIllegalClass");
 validator = new SubTypeValidator() {{
 _cfgIllegalClassNames = Collections.unmodifiableSet(customIllegalClasses);
 }};
 String customIllegalClassName = "com.example.CustomIllegalClass";
 JavaType customIllegalType = null; // initialize with the custom illegal class name
 try {
 validator.validateSubType(null, customIllegalType);
 fail("Should have thrown JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }
 }

}