package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import org.junit.Test;

public class AsWrapperTypeDeserializerTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testDeserializeTypedFromObject() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 mapper.getTypeFactory().constructType(String.class),
 mapper.getTypeIdResolver(),
 "type",
 true,
 String.class);
 ObjectNode input = mapper.createObjectNode();
 input.put("type", "java.lang.String");
 input.put("value", "test");
 JsonNode node = mapper.treeToNodes(input)[0];
 Object result = deserializer.deserializeTypedFromObject(mapper.treeToParser(node), mapper.getDeserializationContext());
 assertEquals("test", result);
 }

 @Test
 public void testDeserializeTypedFromArray() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 mapper.getTypeFactory().constructType(String[].class),
 mapper.getTypeIdResolver(),
 "type",
 true,
 String[].class);
 ObjectNode input = mapper.createObjectNode();
 input.put("type", "java.lang.String[]");
 JsonNode node = mapper.treeToNodes(input)[0];
 Object result = deserializer.deserializeTypedFromArray(mapper.treeToParser(node), mapper.getDeserializationContext());
 assertArrayEquals(new String[]{"test"}, (String[])result);
 }

 @Test
 public void testDeserializeTypedFromScalar() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 mapper.getTypeFactory().constructType(Integer.class),
 mapper.getTypeIdResolver(),
 "type",
 true,
 Integer.class);
 ObjectNode input = mapper.createObjectNode();
 input.put("type", "com.example.TestInt");
 ObjectNode valueNode = mapper.createObjectNode();
 valueNode.put("value", 1);
 input.set("value", valueNode);
 JsonNode node = mapper.treeToNodes(input)[0];
 Object result = deserializer.deserializeTypedFromScalar(mapper.treeToParser(node), mapper.getDeserializationContext());
 assertEquals(1, result);
 }

 @Test
 public void testDeserializeTypedFromAny() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 mapper.getTypeFactory().constructType(String.class),
 mapper.getTypeIdResolver(),
 "type",
 true,
 String.class);
 ObjectNode input = mapper.createObjectNode();
 input.put("type", "java.util.List");
 ObjectNode valueNode = mapper.createObjectNode();
 valueNode.put("value", mapper.createValueFactory().valueElement("test"));
 input.set("value", valueNode);
 JsonNode node = mapper.treeToNodes(input)[0];
 Object result = deserializer.deserializeTypedFromAny(mapper.treeToParser(node), mapper.getDeserializationContext());
 assertEquals("[test]", result.toString());
 }

 @Test
 public void testDeserializeWithNativeTypeId() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 mapper.getTypeFactory().constructType(String.class),
 mapper.getTypeIdResolver(),
 "type",
 true,
 String.class);
 ObjectNode input = mapper.createObjectNode();
 input.put("type", "java.lang.String");
 input.put("value", "test");
 input.put("@class", "com.example.TestString");
 JsonNode node = mapper.treeToNodes(input)[0];
 Object result = deserializer.deserializeTypedFromObject(mapper.treeToParser(node), mapper.getDeserializationContext());
 assertEquals("test", result);
 }

 @Test(expected = JsonProcessingException.class)
 public void testInvalidJsonObject() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 mapper.getTypeFactory().constructType(String.class),
 mapper.getTypeIdResolver(),
 "type",
 true,
 String.class);
 ObjectNode input = mapper.createObjectNode();
 input.put("type", "java.lang.String");
 input.put("value", "test");
 input.put("invalid", "field");
 JsonNode node = mapper.treeToNodes(input)[0];
 deserializer.deserializeTypedFromObject(mapper.treeToParser(node), mapper.getDeserializationContext());
 fail("Expected JsonProcessingException");
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidTypeId() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 mapper.getTypeFactory().constructType(String.class),
 mapper.getTypeIdResolver(),
 "type",
 true,
 String.class);
 ObjectNode input = mapper.createObjectNode();
 input.put("type", "java.lang.Integer");
 input.put("value", "test");
 JsonNode node = mapper.treeToNodes(input)[0];
 deserializer.deserializeTypedFromObject(mapper.treeToParser(node), mapper.getDeserializationContext());
 fail("Expected JsonMappingException");
 }

 @Test
 public void testDuplicateTypeId() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 mapper.getTypeFactory().constructType(String.class),
 mapper.getTypeIdResolver(),
 "type",
 true,
 String.class);
 ObjectNode input = mapper.createObjectNode();
 input.put("type", "java.lang.String");
 input.put("value", "test");
 ObjectNode innerInput = mapper.createObjectNode();
 innerInput.put("type", "java.lang.String");
 input.set("innerType", innerInput);
 JsonNode node = mapper.treeToNodes(input)[0];
 try {
 deserializer.deserializeTypedFromObject(mapper.treeToParser(node), mapper.getDeserializationContext());
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 assertEquals("Can not instantiate value of type [simple type, class java.lang.String] from JSON Object (need to add/enable type information)", e.getMessage());
 }
 }

 @Test
 public void testTypeIdVisible() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 mapper.getTypeFactory().constructType(String.class),
 mapper.getTypeIdResolver(),
 "type",
 true,
 String.class);
 ObjectNode input = mapper.createObjectNode();
 input.put("type", "java.lang.String");
 input.put("value", "test");
 input.put("typeIdVisible", true);
 ObjectNode typeIdNode = mapper.createObjectNode();
 typeIdNode.put("type", "java.lang.String");
 input.set("typeId", typeIdNode);
 JsonNode node = mapper.treeToNodes(input)[0];
 Object result = deserializer.deserializeTypedFromObject(mapper.treeToParser(node), mapper.getDeserializationContext());
 assertEquals("test", result);
 assertEquals("java.lang.String", result.getClass().getName());
 }
}