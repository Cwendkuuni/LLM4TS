package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class SubTypeValidatorTest {

 private static final SubTypeValidator instance = SubTypeValidator.instance();

 @Test
 public void testValidateSubType_validType() throws JsonMappingException, JsonProcessingException {
 JavaType validType = TypeFactory.defaultInstance().constructType(String.class);
 DeserializationContext context = new DeserializationContext(null, new StandardDeserializer(), null);
 instance.validateSubType(context, validType);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_illegalType() throws JsonMappingException, JsonProcessingException {
 JavaType illegalType = TypeFactory.defaultInstance().constructType(SubTypeValidator.class);
 DeserializationContext context = new DeserializationContext(null, new StandardDeserializer(), null);
 instance.validateSubType(context, illegalType);
 }

 @Test
 public void testValidateSubType_prefixString() throws JsonMappingException, JsonProcessingException {
 JavaType prefixStringType = TypeFactory.defaultInstance().constructType(Object.class, "org.springframework.AbstractApplicationContext");
 DeserializationContext context = new DeserializationContext(null, new StandardDeserializer(), null);
 instance.validateSubType(context, prefixStringType);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubType_prefixStringWithAbstractPointcutAdvisor() throws JsonMappingException, JsonProcessingException {
 JavaType prefixStringType = TypeFactory.defaultInstance().constructType(Object.class, "org.springframework.AbstractPointcutAdvisor");
 DeserializationContext context = new DeserializationContext(null, new StandardDeserializer(), null);
 instance.validateSubType(context, prefixStringType);
 }

 @Test
 public void testValidateSubType_unmodifiableSet() {
 Set<String> set = new HashSet<String>() {{
 add("java.lang.String");
 add("java.util.Set");
 add("com.fasterxml.jackson.databind.jsontype.impl.SubTypeValidator");
 }};
 SubTypeValidator validator = new SubTypeValidator();
 Set<String> illegalClassNames = validator._cfgIllegalClassNames;
 assertEquals(set, illegalClassNames);
 }

 @Test
 public void testInstance() {
 SubTypeValidator validator1 = SubTypeValidator.instance();
 SubTypeValidator validator2 = SubTypeValidator.instance();
 assertEquals(validator1, validator2);
 }
}