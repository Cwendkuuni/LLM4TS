package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.test.BaseTest;

public class AsPropertyTypeDeserializerTest extends BaseTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @SuppressWarnings("unchecked")
 @Test
 public void testDeserializeTypedProperty() throws IOException {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 mapper.getTypeFactory().constructType(String.class), mapper.getTypeIdResolver(), "type", true,
 mapper.getTypeFactory().constructType(Integer.class), As.PROPERTY);
 Object result = deserializer.deserializeTypedFromObject(createParser("{\"type\": \"java.lang.Integer\", \"value\": 42}"), context);
 assertEquals(Integer.class, result.getClass());
 assertEquals(42, result);
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testDeserializeTypedPropertyMissingTypeId() throws IOException {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 mapper.getTypeFactory().constructType(String.class), mapper.getTypeIdResolver(), "type", true,
 mapper.getTypeFactory().constructType(Integer.class), As.PROPERTY);
 Object result = deserializer.deserializeTypedFromObject(createParser("{\"value\": 42}"), context);
 assertNull(result);
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testDeserializeTypedPropertyWrongTypeId() throws IOException {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 mapper.getTypeFactory().constructType(String.class), mapper.getTypeIdResolver(), "type", true,
 mapper.getTypeFactory().constructType(Integer.class), As.PROPERTY);
 Object result = deserializer.deserializeTypedFromObject(createParser("{\"type\": \"java.lang.String\", \"value\": 42}"), context);
 assertNull(result);
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testDeserializeTypedPropertyMultipleFields() throws IOException {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 mapper.getTypeFactory().constructType(String.class), mapper.getTypeIdResolver(), "type", true,
 mapper.getTypeFactory().constructType(Integer.class), As.PROPERTY);
 Object result = deserializer.deserializeTypedFromObject(createParser("{\"firstType\": \"java.lang.String\", \"type\": \"java.lang.Integer\", \"value\": 42}"), context);
 assertNull(result);
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testDeserializeTypedPropertyFound() throws IOException {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 mapper.getTypeFactory().constructType(String.class), mapper.getTypeIdResolver(), "type", true,
 mapper.getTypeFactory().constructType(Integer.class), As.PROPERTY);
 Object result = deserializer.deserializeTypedFromObject(createParser("{\"type\": \"java.lang.Integer\", \"value\": 42}"), context);
 assertEquals(Integer.class, result.getClass());
 assertEquals(42, result);
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testDeserializeTypedPropertyDefaultImpl() throws IOException {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 mapper.getTypeFactory().constructType(String.class), mapper.getTypeIdResolver(), "type", true,
 mapper.getTypeFactory().constructType(String.class), As.PROPERTY);
 Object result = deserializer.deserializeTypedFromObject(createParser("{\"type\": \"java.lang.String\"}"), context);
 assertEquals(String.class, result.getClass());
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testDeserializeTypedPropertyNotFound() throws IOException {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 mapper.getTypeFactory().constructType(String.class), mapper.getTypeIdResolver(), "type", true,
 mapper.getTypeFactory().constructType(Integer.class), As.PROPERTY);
 Object result = deserializer.deserializeTypedFromObject(createParser("{}"), context);
 assertNull(result);
 }

}