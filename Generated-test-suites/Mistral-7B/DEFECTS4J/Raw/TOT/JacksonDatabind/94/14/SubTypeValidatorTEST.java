package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.core.type.ClassIdType;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonFactory;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertThrows;

public class SubTypeValidatorTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper(new JsonFactory());
 }

 @Test
 public void testValidType() throws Exception {
 String json = "{\"className\":\"com.example.ValidType\"}";
 JavaType type = objectMapper.getTypeFactory().constructType(getClass(), new ClassIdType(SubTypeValidator.class, "ValidType"));
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(objectMapper.getContext(), type);
 }

 @Test
 public void testNastyTypeWithPrefixSpring() throws Exception {
 String json = "{\"className\":\"org.springframework.beans.factory.config.PropertyPathFactoryBean\"}";
 JavaType type = objectMapper.getTypeFactory().constructType(getClass(), new ClassIdType(SubTypeValidator.class, "NastyType"));
 SubTypeValidator validator = SubTypeValidator.instance();
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(objectMapper.getContext(), type));
 }

 @Test
 public void testNastyTypeWithoutPrefixSpring() throws Exception {
 String json = "{\"className\":\"com.mchange.v2.c3p0.ComboPooledDataSource\"}";
 JavaType type = objectMapper.getTypeFactory().constructType(getClass(), new ClassIdType(SubTypeValidator.class, "NastyType"));
 SubTypeValidator validator = SubTypeValidator.instance();
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(objectMapper.getContext(), type));
 }

 @Test
 public void testEmptyClassName() throws Exception {
 String json = "{\"className\":\"\"}";
 JavaType type = objectMapper.getTypeFactory().constructType(getClass(), new ClassIdType(SubTypeValidator.class, ""));
 SubTypeValidator validator = SubTypeValidator.instance();
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(objectMapper.getContext(), type));
 }

 @Test
 public void testNullClassName() throws Exception {
 String json = "{\"className\":null}";
 JavaType type = objectMapper.getTypeFactory().constructType(getClass(), new ClassIdType(SubTypeValidator.class, null));
 SubTypeValidator validator = SubTypeValidator.instance();
 assertThrows(JsonMappingException.class, () -> validator.validateSubType(objectMapper.getContext(), type));
 }

 @Test
 public void testEmptyConstructor() throws Exception {
 String json = "{\"className\":\"com.example.EmptyConstructor\"}";
 JavaType type = objectMapper.getTypeFactory().constructType(getClass(), new ClassIdType(SubTypeValidator.class, "EmptyConstructor"));
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(objectMapper.getContext(), type);
 }

 @Test
 public void testNastyInterface() throws Exception {
 String json = "{\"className\":\"java.util.logging.Level\"}";
 JavaType type = objectMapper.getTypeFactory().constructType(getClass(), new ClassIdType(SubTypeValidator.class, "NastyInterface"));
 SubTypeValidator validator = SubTypeValidator.instance();
 validator.validateSubType(objectMapper.getContext(), type);
 }
}