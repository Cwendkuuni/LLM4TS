package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.test.BaseMapTest;

public class AsPropertyTypeDeserializerTest extends BaseMapTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @SuppressWarnings("unchecked")
 @Test
 public void testDeserializeTypedFromObject() throws IOException {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 Map.class, MAPPER.getTypeFactory(), "type", true, String.class);
 ObjectNode node = MAPPER.createObjectNode();
 node.put("type", "java.util.Map");
 node.put("value", Map.of("key1", "value1"));
 JsonParser parser = MAPPER.treeToValues(node, JsonParser.StandardCreator.instance());
 Object result = deserializer.deserializeTypedFromObject(parser, MAPPER.contextDescriptor());
 assertEquals(Map.class, result.getClass());
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testDeserializeTypedFromObjectWithNativeTypeId() throws IOException {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 Map.class, MAPPER.getTypeFactory(), "type", true, String.class);
 ObjectNode node = MAPPER.createObjectNode();
 node.put("type", "java.util.Map");
 node.put("_class", Map.class.getName());
 node.put("value", Map.of("key1", "value1"));
 JsonParser parser = MAPPER.treeToValues(node, JsonParser.StandardCreator.instance());
 Object result = deserializer.deserializeTypedFromObject(parser, MAPPER.contextDescriptor());
 assertEquals(Map.class, result.getClass());
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testDeserializeTypedFromObjectWithMissingProperty() throws IOException {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 Map.class, MAPPER.getTypeFactory(), "type", true, String.class);
 ObjectNode node = MAPPER.createObjectNode();
 node.put("value", Map.of("key1", "value1"));
 JsonParser parser = MAPPER.treeToValues(node, JsonParser.StandardCreator.instance());
 try {
 deserializer.deserializeTypedFromObject(parser, MAPPER.contextDescriptor());
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // expected
 }
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testDeserializeTypedFromObjectWithWrongPropertyName() throws IOException {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 Map.class, MAPPER.getTypeFactory(), "type", true, String.class);
 ObjectNode node = MAPPER.createObjectNode();
 node.put("wrongType", "java.util.Map");
 node.put("value", Map.of("key1", "value1"));
 JsonParser parser = MAPPER.treeToValues(node, JsonParser.StandardCreator.instance());
 try {
 deserializer.deserializeTypedFromObject(parser, MAPPER.contextDescriptor());
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // expected
 }
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testDeserializeTypedFromObjectWithMissingTypeId() throws IOException {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 Map.class, MAPPER.getTypeFactory(), "type", true, String.class);
 ObjectNode node = MAPPER.createObjectNode();
 node.put("value", Map.of("key1", "value1"));
 JsonParser parser = MAPPER.treeToValues(node, JsonParser.StandardCreator.instance());
 try {
 deserializer.deserializeTypedFromObject(parser, MAPPER.contextDescriptor());
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // expected
 }
 }

 @Test
 public void testDeserializeTypedFromArray() throws IOException {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 Map.class, MAPPER.getTypeFactory(), "type", true, String.class);
 ObjectNode node = MAPPER.createObjectNode();
 node.put("value", new Object[] { Map.of("key1", "value1"), Map.of("key2", "value2") });
 JsonParser parser = MAPPER.treeToValues(node, JsonParser.StandardCreator.instance());
 Object result = deserializer.deserializeTypedFromArray(parser, MAPPER.contextDescriptor());
 assertEquals(Map[].class, result.getClass());
 }

 @Test
 public void testDeserializeTypedUsingDefaultImpl() throws IOException {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 Object.class, MAPPER.getTypeFactory(), "type", true, String.class);
 ObjectNode node = MAPPER.createObjectNode();
 node.put("value", "defaultValue");
 JsonParser parser = MAPPER.treeToValues(node, JsonParser.StandardCreator.instance());
 Object result = deserializer.deserializeTypedFromObject(parser, MAPPER.contextDescriptor());
 assertNotNull(result);
 }

 @Test
 public void testDeserializeTypedWithWrongTypeId() throws IOException {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 Map.class, MAPPER.getTypeFactory(), "type", true, String.class);
 ObjectNode node = MAPPER.createObjectNode();
 node.put("type", "java.util.List");
 node.put("value", Map.of("key1", "value1"));
 JsonParser parser = MAPPER.treeToValues(node, JsonParser.StandardCreator.instance());
 try {
 deserializer.deserializeTypedFromObject(parser, MAPPER.contextDescriptor());
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // expected
 }
 }
}