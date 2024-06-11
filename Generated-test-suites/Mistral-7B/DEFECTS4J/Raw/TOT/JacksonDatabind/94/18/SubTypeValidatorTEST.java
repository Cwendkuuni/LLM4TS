package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

/**
 * Test case for {@link SubTypeValidator} class.
 */
public class SubTypeValidatorTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @Test
 public void testValidType() throws IOException, JsonProcessingException {
 String json = "{\"className\":\"com.example.ValidType\"}";
 JavaType type = TypeFactory.defaultInstance().constructType(SubTypeValidator.class, String.class);
 DeserializationContext context = new DeserializationContext(MAPPER.getDefaultConfig(), null, type);
 SubTypeValidator validator = SubTypeValidator.instance().instance();
 validator.validateSubType(context, type);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidType() throws IOException, JsonProcessingException {
 String json = "{\"className\":\"org.apache.commons.collections.functors.InvokerTransformer\"}";
 JavaType type = TypeFactory.defaultInstance().constructType(SubTypeValidator.class, String.class);
 DeserializationContext context = new DeserializationContext(MAPPER.getDefaultConfig(), null, type);
 SubTypeValidator validator = SubTypeValidator.instance().instance();
 validator.validateSubType(context, type);
 }

 @Test(expected = JsonMappingException.class)
 public void testSpringType() throws IOException, JsonProcessingException {
 String json = "{\"className\":\"org.springframework.beans.factory.config.PropertyPathFactoryBean\"}";
 JavaType type = TypeFactory.defaultInstance().constructType(SubTypeValidator.class, String.class);
 DeserializationContext context = new DeserializationContext(MAPPER.getDefaultConfig(), null, type);
 SubTypeValidator validator = SubTypeValidator.instance().instance();
 validator.validateSubType(context, type);
 }

 @Test
 public void testPrefixSpringType() throws IOException, JsonProcessingException {
 String json = "{\"className\":\"com.example.MySpringComponent\"}";
 JavaType type = TypeFactory.defaultInstance().constructType(SubTypeValidator.class, String.class);
 DeserializationContext context = new DeserializationContext(MAPPER.getDefaultConfig(), null, type);
 SubTypeValidator validator = SubTypeValidator.instance().instance();
 validator.validateSubType(context, type);
 }

 @Test(expected = JsonMappingException.class)
 public void testPrefixedSpringTypeWithInvalidSubtype() throws IOException, JsonProcessingException {
 String json = "{\"className\":\"org.springframework.beans.factory.config.PropertyPathFactoryBean\"}";
 JavaType type = TypeFactory.defaultInstance().constructType(SubTypeValidator.class, String.class);
 DeserializationContext context = new DeserializationContext(MAPper.getDefaultConfig(), null, type);
 SubTypeValidator validator = SubTypeValidator.instance().instance();
 validator.validateSubType(context, type);
 }

 @Test
 public void testEmptyString() throws IOException, JsonProcessingException {
 String json = "{\"className\":\"\"}";
 JavaType type = TypeFactory.defaultInstance().constructType(SubTypeValidator.class, String.class);
 DeserializationContext context = new DeserializationContext(MAPper.getDefaultConfig(), null, type);
 SubTypeValidator validator = SubTypeValidator.instance().instance();
 validator.validateSubType(context, type);
 }

 @Test(expected = JsonMappingException.class)
 public void testNullValue() throws IOException, JsonProcessingException {
 String json = "{\"className\":null}";
 JavaType type = TypeFactory.defaultInstance().constructType(SubTypeValidator.class, String.class);
 DeserializationContext context = new DeserializationContext(MAPper.getDefaultConfig(), null, type);
 SubTypeValidator validator = SubTypeValidator.instance().instance();
 validator.validateSubType(context, type);
 }

 @Test(expected = JsonMappingException.class)
 public void testNullClassType() throws IOException, JsonProcessingException {
 JavaType type = null;
 DeserializationContext context = new DeserializationContext(MAPper.getDefaultConfig(), null, type);
 SubTypeValidator validator = SubTypeValidator.instance().instance();
 validator.validateSubType(context, type);
 }
}