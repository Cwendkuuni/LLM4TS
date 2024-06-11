package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;

import java.util.Collections;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class SubTypeValidatorTest {

 private ObjectMapper mapper;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 mapper.findAndRegisterModules();
 }

 @Test
 public void testValidClass() throws Exception {
 String json = "{\"class\":\"java.lang.String\"}";
 JavaType type = mapper.getTypeFactory().constructType(String.class);
 mapper.validateSubType(new DeserializationContext(mapper.getConfig(), null), type);
 mapper.readValue(json, type);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidClass() throws Exception {
 String json = "{\"class\":\"org.apache.commons.collections4.functors.InstantiateTransformer\"}";
 JavaType type = mapper.getTypeFactory().constructType(String.class);
 mapper.validateSubType(new DeserializationContext(mapper.getConfig(), null), type);
 mapper.readerForUpdating(new Object()).readValue(json);
 }

 @Test(expected = JsonMappingException.class)
 public void testSpringClass() throws Exception {
 String json = "{\"class\":\"org.springframework.beans.factory.config.PropertyPathFactoryBean\"}";
 JavaType type = mapper.getTypeFactory().constructType(String.class);
 mapper.validateSubType(new DeserializationContext(mapper.getConfig(), null), type);
 mapper.readerForUpdating(new Object()).readValue(json);
 }

 @Test(expected = JsonMappingException.class)
 public void testSpringAbstractClass() throws Exception {
 String json = "{\"class\":\"org.springframework.beans.factory.AbstractApplicationContext\"}";
 JavaType type = mapper.getTypeFactory().constructType(String.class);
 mapper.validateSubType(new DeserializationContext(mapper.getConfig(), null), type);
 mapper.readerForUpdating(new Object()).readValue(json);
 }

 @Test(expected = JsonMappingException.class)
 public void testSpringInterface() throws Exception {
 String json = "{\"class\":\"java.util.concurrent.Callable\"}";
 JavaType type = mapper.getTypeFactory().constructType(Callable.class);
 mapper.validateSubType(new DeserializationContext(mapper.getConfig(), null), type);
 mapper.readerForUpdating(new Object()).readValue(json);
 }

 @Test(expected = JsonMappingException.class)
 public void testUnknownPrefixClass() throws Exception {
 String json = "{\"class\":\"com.unknown.UnknownClass\"}";
 JavaType type = mapper.getTypeFactory().constructType(String.class);
 mapper.validateSubType(new DeserializationContext(mapper.getConfig(), null), type);
 mapper.readerForUpdating(new Object()).readValue(json);
 }

 @Test
 public void testEmptyClass() throws Exception {
 String json = "{}";
 JavaType type = mapper.getTypeFactory().constructType(String.class);
 mapper.validateSubType(new DeserializationContext(mapper.getConfig(), null), type);
 mapper.readerForUpdating(new Object()).readValue(json);
 }
}