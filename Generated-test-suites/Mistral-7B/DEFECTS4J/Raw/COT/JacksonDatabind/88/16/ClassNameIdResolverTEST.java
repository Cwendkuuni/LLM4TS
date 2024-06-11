package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class ClassNameIdResolverTest {

 private TypeIdResolver _classNameIdResolver;
 private ObjectMapper _objectMapper;

 @Before
 public void setup() throws Exception {
 _objectMapper = new ObjectMapper();
 _classNameIdResolver = new ClassNameIdResolver(_objectMapper.getTypeFactory().constructType(String.class), _objectMapper.getTypeFactory());
 }

 @Test
 public void testGetMechanism() {
 assertEquals(JsonTypeInfo.Id.CLASS, _classNameIdResolver.getMechanism());
 }

 @Test
 public void testRegisterSubtype() {
 // Not testing this method as it is not used with class name - based resolvers
 }

 @Test
 public void testIdFromValue() throws Exception {
 Object value = new Object();
 String id = _classNameIdResolver.idFromValue(value);
 assertEquals("java.lang.Object", id);
 }

 @Test
 public void testIdFromValueAndType() throws Exception {
 Object value = new Object();
 Class<?> type = String.class;
 String id = _classNameIdResolver.idFromValueAndType(value, type);
 assertEquals("java.lang.Object", id);
 }

 @Test
 public void testTypeFromId() throws Exception {
 Class<?> type = String[].class;
 String id = type.getName();
 JavaType javaType = _classNameIdResolver.typeFromId(_objectMapper.getDeserializationContext(), id);
 assertEquals(TypeReference.collectionValueType(String.class), javaType.getTypeId());
 }

 @Test
 public void testIdFromValueWithEnum() throws Exception {
 Enum e = Enum.valueOf(MyEnum.class, "MY_VALUE");
 String id = _classNameIdResolver.idFromValue(e);
 assertTrue(id.startsWith("com.example.MyEnum"));
 }

 @Test
 public void testIdFromValueWithEnumSubclass() throws Exception {
 Enum subEnum = Enum.valueOf(MyEnumSubclass.class, "MY_VALUE");
 String id = _classNameIdResolver.idFromValue(subEnum);
 assertTrue(id.startsWith("com.example.MyEnumSubclass"));
 }

 @Test
 public void testIdFromValueWithArray() throws Exception {
 ObjectNode node = _objectMapper.createObjectNode();
 node.put("items", new Object[] { 1, 2, 3 });
 String jsonString = _objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
 ObjectNode node2 = _objectMapper.readTree(jsonString);
 Object value = node2.get("items");
 String id = _classNameIdResolver.idFromValue(value);
 assertEquals("java.lang.Object[]", id);
 }

 @Test
 public void testIdFromValueWithMap() throws Exception {
 ObjectNode node = _objectMapper.createObjectNode();
 node.put("key", "value");
 node.put("map", new HashMap<String, Object>() { { put("key", "value"); } });
 String jsonString = _objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
 ObjectNode node2 = _objectMapper.readTree(jsonString);
 Object value = node2.get("map");
 String id = _classNameIdResolver.idFromValue(value);
 assertEquals("java.util.HashMap<java.lang.String,java.lang.Object>", id);
 }

 @Test
 public void testIdFromValueWithCollections() throws Exception {
 ObjectNode node = _objectMapper.createObjectNode();
 node.put("items", new ArrayList<String>() { { add("value1"); add("value2"); } });
 String jsonString = _objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
 ObjectNode node2 = _objectMapper.readTree(jsonString);
 Object value = node2.get("items");
 String id = _classNameIdResolver.idFromValue(value);
 assertEquals("java.util.ArrayList<java.lang.String>", id);
 }

 @Test
 public void testIdFromValueWithArraysWrappingInnerCollections() throws Exception {
 ObjectNode node = _objectMapper.createObjectNode();
 node.put("items", new Object[] { new ArrayList<String>() { { add("value1"); add("value2"); } } });
 String jsonString = _objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
 ObjectNode node2 = _objectMapper.readTree(jsonString);
 Object value = node2.get("items");
 String id = _classNameIdResolver.idFromValue(value);
 assertEquals("java.lang.Object[]", id);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIdFromValueWithInvalidTypeId() throws Exception {
 String id = "invalidTypeId";
 try {
 _classNameIdResolver.idFromValue(id);
 } catch (IOException e) {
 fail("Expected IllegalArgumentException but got IOException: " + e.getMessage());
 } catch (IllegalArgumentException e) {
 // Pass
 }
 }

 @Test(expected = IOException.class)
 public void testTypeFromIdWithUnknownTypeId() throws Exception {
 String id = "com.example.UnknownType";
 try {
 _classNameIdResolver.typeFromId(_objectMapper.getDeserializationContext(), id);
 } catch (IOException e) {
 // Pass
 }
 }
}