package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JavaType;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertThrows;

public class SubTypeValidatorTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testValidClass() throws Exception {
 String json = "{\"class\":\"com.example.ValidClass\"}";
 JavaType type = objectMapper.getTypeFactory().constructType(new TypeReference<JavaType>() {}.getType());
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(objectMapper.getDeserializationContext(), type);
 }

 @Test
 public void testInvalidClassWithPrefix() throws Exception {
 String json = "{\"class\":\"org.springframework.SomeSpringClass\"}";
 JavaType type = objectMapper.getTypeFactory().constructType(new TypeReference<JavaType>() {}.getType());
 SubTypeValidator validator = SubTypeValidator.instance();
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(objectMapper.getDeserializationContext(), type));
 }

 @Test
 public void testInvalidClassWithSimpleName() throws Exception {
 String json = "{\"class\":\"org.springframework.AbstractPointcutAdvisor\"}";
 JavaType type = objectMapper.getTypeFactory().constructType(new TypeReference<JavaType>() {}.getType());
 SubTypeValidator validator = SubTypeValidator.instance();
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(objectMapper.getDeserializationContext(), type));
 }

 @Test
 public void testInvalidClassWithSuperClass() throws Exception {
 String json = "{\"class\":\"com.example.ParentClass\"}";
 JavaType type = objectMapper.getTypeFactory().constructType(ParentClass.class);
 SubTypeValidator validator = SubTypeValidator.instance();
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(objectMapper.getDeserializationContext(), type));
 }
}