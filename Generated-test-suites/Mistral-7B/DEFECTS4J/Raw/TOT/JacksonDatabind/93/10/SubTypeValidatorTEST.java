package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.type.ClassIdType;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class SubTypeValidatorTest {

 private ObjectMapper mapper;
 private DeserializationContext ctxt;
 private SubTypeValidator validator;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 ctxt = mapper.getDeserializationContext();
 validator = SubTypeValidator.instance();
 }

 @Test
 public void testValidType() throws JsonMappingException {
 JavaType validType = mapper.getTypeFactory().constructType(String.class);
 validator.validateSubType(ctxt, validType);
 }

 @Test
 public void testInvalidTypeWithPrefixString() throws JsonMappingException {
 JavaType invalidType = mapper.getTypeFactory().constructType(AbstractApplicationContext.class);
 try {
 validator.validateSubType(ctxt, invalidType);
 fail("Should have thrown JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 assertEquals("Illegal type (com.mchange.v2.c3p0.AbstractApplicationContext) to deserialize: prevented for security reasons", e.getMessage());
 }
 }

 @Test
 public void testInvalidTypeWithoutPrefixString() throws JsonMappingException {
 JavaType invalidType = mapper.getTypeFactory().constructType(AbstractPointcutAdvisor.class);
 try {
 validator.validateSubType(ctxt, invalidType);
 fail("Should have thrown JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 assertEquals("Illegal type (org.springframework.aop.support.AbstractPointcutAdvisor) to deserialize: prevented for security reasons", e.getMessage());
 }
 }

 @Test
 public void testInvalidTypeWithRawClass() throws JsonMappingException {
 JavaType invalidType = mapper.getTypeFactory().constructType(ClassIdType.class, "java.util.HashMap$Entry");
 try {
 validator.validateSubType(ctxt, invalidType);
 fail("Should have thrown JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 assertEquals("Illegal type (java.util.HashMap$Entry) to deserialize: prevented for security reasons", e.getMessage());
 }
 }

 @Test
 public void testInvalidTypeWithNestedClasses() throws JsonMappingException {
 JavaType nestedType = mapper.getTypeFactory().constructType(Object[].class, Arrays.class, "Object[][]");
 try {
 validator.validateSubType(ctxt, nestedType);
 fail("Should have thrown JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 assertEquals("Illegal type ([Ljava.lang.Object;[Ljava.lang.Object;) to deserialize: prevented for security reasons", e.getMessage());
 }
}
}