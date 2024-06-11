package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;

import java.util.Collections;
import java.util.HashSet;

import org.junit.Test;

import com.fasterxml.jackson.core.type.JavaType;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaTypeFactory;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class SubTypeValidatorTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @Test
 public void testValidClass() throws JsonMappingException {
 String json = "{\"className\":\"com.example.valid.ClassName\"}";
 JavaType type = TypeFactory.defaultInstance()
 .constructType(JavaTypeFactory.visitedTypes.get(SubTypeValidator.class), Map.class,
 map -> map.put("className", String.class));
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(new DeserializationContext(MAPPER.getConfig(), null, null), type);
 }

 @Test
 public void testNastyClass() throws JsonMappingException {
 String json = "{\"className\":\"org.apache.commons.collections4.functors.InstantiateTransformer\"}";
 JavaType type = TypeFactory.defaultInstance()
 .constructType(JavaTypeFactory.visitedTypes.get(SubTypeValidator.class), Map.class,
 map -> map.put("className", String.class));
 SubTypeValidator validator = SubTypeValidator.instance();
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(new DeserializationContext(MAPPER.getConfig(), null, null), type));
 }

 @Test
 public void testSpringClass() throws JsonMappingException {
 String json = "{\"className\":\"org.springframework.beans.factory.config.PropertyPathFactoryBean\"}";
 JavaType type = TypeFactory.defaultInstance()
 .constructType(JavaTypeFactory.visitedTypes.get(SubTypeValidator.class), Map.class,
 map -> map.put("className", String.class));
 SubTypeValidator validator = SubTypeValidator.instance();
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(new DeserializationContext(MAPPER.getConfig(), null, null), type));
 }

 @Test
 public void testSpringInterface() throws JsonMappingException {
 String json = "{\"className\":\"org.springframework.beans.factory.config.ApplicationContext\"}";
 JavaType type = TypeFactory.defaultInstance()
 .constructType(JavaTypeFactory.visitedTypes.get(SubTypeValidator.class), Map.class,
 map -> map.put("className", String.class));
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(new DeserializationContext(MAPPER.getConfig(), null, null), type);
 }

 @Test
 public void testEmptyClass() throws JsonMappingException {
 String json = "{\"className\":\"\"}";
 JavaType type = TypeFactory.defaultInstance()
 .constructType(JavaTypeFactory.visitedTypes.get(SubTypeValidator.class), Map.class,
 map -> map.put("className", String.class));
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(new DeserializationContext(MAPPER.getConfig(), null, null), type);
 }

 @Test
 public void testNullClass() throws JsonMappingException {
 String json = "{\"className\":null}";
 JavaType type = TypeFactory.defaultInstance()
 .constructType(JavaTypeFactory.visitedTypes.get(SubTypeValidator.class), Map.class,
 map -> map.put("className", String.class));
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(new DeserializationContext(MAPPER.getConfig(), null, null), type);
 }

 @Test
 public void testEmptySet() {
 Set<String> emptySet = Collections.emptySet();
 SubTypeValidator validator = new SubTypeValidator();
 validator._cfgIllegalClassNames = emptySet;
 assertThrows(NullPointerException.class, () -> validator.validateSubType(new DeserializationContext(MAPPER.getConfig(), null, null), TypeFactory.defaultInstance().constructType(String.class)));
 }
}