package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;

import java.util.Collections;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SubTypeValidatorTest {

 private final ObjectMapper objectMapper = new ObjectMapper();

 @Test
 public void testValidateSubType_validType() throws JsonMappingException, JsonProcessingException {
 final JavaType validType = objectMapper.getTypeFactory().constructType(String.class);
 final SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(mock(DeserializationContext.class), validType);
 }

 @Test
 public void testValidateSubType_invalidType_prefixString() throws JsonMappingException, JsonProcessingException {
 final String invalidTypeName = "org.springframework.AbstractBeanFactoryPointcutAdvisor";
 final JavaType invalidType = objectMapper.getTypeFactory().constructType(Class.forName(invalidTypeName));
 final SubTypeValidator validator = SubTypeValidator.instance();
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(mock(DeserializationContext.class), invalidType));
 }

 @Test
 public void testValidateSubType_invalidType_prefixApplicationContext() throws JsonMappingException, JsonProcessingException {
 final String invalidTypeName = "org.springframework.AbstractApplicationContext";
 final JavaType invalidType = objectMapper.getTypeFactory().constructType(Class.forName(invalidTypeName));
 final SubTypeValidator validator = SubTypeValidator.instance();
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(mock(DeserializationContext.class), invalidType));
 }

 @Test
 public void testValidateSubType_invalidType_defaultNoDeserClassNames() throws JsonMappingException, JsonProcessingException {
 final String invalidTypeName = "org.apache.commons.collections4.functors.InstantiateTransformer";
 final JavaType invalidType = objectMapper.getTypeFactory().constructType(Class.forName(invalidTypeName));
 final SubTypeValidator validator = SubTypeValidator.instance();
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(mock(DeserializationContext.class), invalidType));
 }

 @Test
 public void testGetInstance() {
 final SubTypeValidator validator1 = SubTypeValidator.instance();
 final SubTypeValidator validator2 = SubTypeValidator.instance();
 assertEquals(validator1, validator2);
 }

 private static DeserializationContext mock(DeserializationContext... args) {
 return mock(DeserializationContext.class);
 }

 private static DeserializationContext mock(Class<?> clazz) {
 return mock(new TypeToken<DeserializationContext>() {}.getType());
 }
}