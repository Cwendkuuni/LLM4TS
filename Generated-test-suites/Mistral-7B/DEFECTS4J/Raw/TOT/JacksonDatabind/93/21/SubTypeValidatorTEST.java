package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.type.ClassIdType;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SubTypeValidatorTest {

 private final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testValidateSubTypeNullType() {
 SubTypeValidator validator = SubTypeValidator.instance();
 JavaType nullType = null;
 try {
 validator.validateSubType(new DeserializationContext(null), nullType);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test
 public void testValidateSubTypeValidType() {
 SubTypeValidator validator = SubTypeValidator.instance();
 JavaType validType = mapper.getTypeFactory().constructType(String.class);
 validator.validateSubType(new DeserializationContext(null), validType);
 }

 @Test
 public void testValidateSubTypeIllegalType() {
 SubTypeValidator validator = SubTypeValidator.instance();
 JavaType illegalType = mapper.getTypeFactory().constructType(SubTypeValidator.class);
 try {
 validator.validateSubType(new DeserializationContext(null), illegalType);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 String actual = e.getMessage();
 String expected = String.format("Illegal type (%s) to deserialize: prevented for security reasons", illegalType.getRawClass().getName());
 assertEquals(expected, actual);
 }
 }

 @Test
 public void testValidateSubTypePrefixType() {
 SubTypeValidator validator = SubTypeValidator.instance();
 JavaType prefixType = mapper.getTypeFactory().constructType("org.springframework.beans.factory.config.PropertyPathFactoryBean");
 try {
 validator.validateSubType(new DeserializationContext(null), prefixType);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test
 public void testValidateSubTypeValidPrefixType() {
 SubTypeValidator validator = SubTypeValidator.instance();
 JavaType validPrefixType = mapper.getTypeFactory().constructType("org.springframework.beans.factory.config.PropertyDefinitionBean");
 validator.validateSubType(new DeserializationContext(null), validPrefixType);
 }

 @Test
 public void testValidateSubTypeIllegalPrefixType() {
 SubTypeValidator validator = SubTypeValidator.instance();
 JavaType illegalPrefixType = mapper.getTypeFactory().constructType("org.springframework.beans.factory.config.PropertyPathFactoryBean");
 try {
 validator.validateSubType(new DeserializationContext(null), illegalPrefixType);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test
 public void testValidateSubTypeListType() {
 SubTypeValidator validator = SubTypeValidator.instance();
 JavaType listType = mapper.getTypeFactory().constructType(new ClassIdType(List.class.getTypeName(), String[].class));
 validator.validateSubType(new DeserializationContext(null), listType);
 }

 @Test
 public void testValidateSubTypeIllegalListType() {
 SubTypeValidator validator = SubTypeValidator.instance();
 JavaType illegalListType = mapper.getTypeFactory().constructType(new ClassIdType(List.class.getTypeName(), Integer[].class));
 try {
 validator.validateSubType(new DeserializationContext(null), illegalListType);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }
 }
}