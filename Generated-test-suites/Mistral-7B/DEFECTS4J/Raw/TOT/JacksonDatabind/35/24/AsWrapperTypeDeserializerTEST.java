package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test cases for {@link AsWrapperTypeDeserializer} class.
 */
public class AsWrapperTypeDeserializerTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @Test
 public void testDeserializationWithValidInput() throws IOException {
 // Prepare test data
 JavaType javaType = MAPPER.getTypeFactory().constructType(String.class);
 TypeIdResolver idResolver = new SimpleTypeIdResolver();
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(javaType, idResolver, "_type", false, String.class);
 JsonFactory jsonFactory = new JsonFactory();
 JsonParser jp = jsonFactory.createParser("{\"type\":\"java.lang.String\",\"value\":\"test\"}");

 // Deserialize with valid input
 Object deserializedObject = deserializer.deserializeTypedFromObject(jp, new TestContext());

 // Assert deserialization result
 assertEquals("test", deserializedObject);
 }

 @Test
 public void testDeserializationWithNullInput() throws IOException {
 // Prepare test data
 JavaType javaType = MAPPER.getTypeFactory().constructType(String.class);
 TypeIdResolver idResolver = new SimpleTypeIdResolver();
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(javaType, idResolver, "_type", false, String.class);
 JsonFactory jsonFactory = new JsonFactory();
 JsonParser jp = jsonFactory.createParser("{}");

 // Deserialize with null input
 try {
 deserializer.deserializeTypedFromObject(jp, new TestContext());
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected exception
 }
 }

 @Test
 public void testDeserializationWithMissingTypeId() throws IOException {
 // Prepare test data
 JavaType javaType = MAPPER.getTypeFactory().constructType(String.class);
 TypeIdResolver idResolver = new SimpleTypeIdResolver();
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(javaType, idResolver, "_type", false, String.class);
 JsonFactory jsonFactory = new JsonFactory();
 JsonParser jp = jsonFactory.createParser("{\"value\":\"test\"}");

 // Deserialize with missing type id
 try {
 deserializer.deserializeTypedFromObject(jp, new TestContext());
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected exception
 }
 }

 @Test
 public void testDeserializationWithInvalidTypeId() throws IOException {
 // Prepare test data
 JavaType javaType = MAPPER.getTypeFactory().constructType(Integer.class);
 TypeIdResolver idResolver = new SimpleTypeIdResolver();
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(javaType, idResolver, "_type", false, Integer.class);
 JsonFactory jsonFactory = new JsonFactory();
 JsonParser jp = jsonFactory.createParser("{\"type\":\"java.lang.String\",\"value\":42}");

 // Deserialize with invalid type id
 try {
 deserializer.deserializeTypedFromObject(jp, new TestContext());
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected exception
 }
 }

 @Test
 public void testDeserializationWithEmptyArray() throws IOException {
 // Prepare test data
 JavaType javaType = MAPPER.getTypeFactory().constructType(List.class);
 TypeIdResolver idResolver = new SimpleTypeIdResolver();
 TypeFactory typeFactory = MAPPER.getTypeFactory();
 SimpleType listType = typeFactory.constructType(new TypeReference<List<Object>>() {});
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(javaType, idResolver, "_type", false, List.class);
 JsonFactory jsonFactory = new JsonFactory();
 JsonParser jp = jsonFactory.createParser("[]");

 // Deserialize with empty array
 Object deserializedObject = deserializer.deserializeTypedFromArray(jp, new TestContext());

 // Assert deserialization result
 assertNull(deserializedObject);
 }

 @Test
 public void testDeserializationWithArrayContainingSingleObject() throws IOException {
 // Prepare test data
 JavaType javaType = MAPPER.getTypeFactory().constructType(List.class);
 TypeIdResolver idResolver = new SimpleTypeIdResolver();
 TypeFactory typeFactory = MAPPER.getTypeFactory();
 SimpleType listType = typeFactory.constructType(new TypeReference<List<Object>>() {});
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(javaType, idResolver, "_type", false, List.class);
 JsonFactory jsonFactory = new JsonFactory();
 JsonParser jp = jsonFactory.createParser("[\n" +
 "  {\"type\":\"java.lang.String\",\"value\":\"test1\"},\n" +
 "  {\"type\":\"java.lang.String\",\"value\":\"test2\"}\n" +
 "]");

 // Deserialize with array containing single object
 Object deserializedObject = deserializer.deserializeTypedFromArray(jp, new TestContext());

 // Assert deserialization result
 List<?> list = (List<?>) deserializedObject;
 assertEquals(2, list.size());
 assertEquals("test1", list.get(0));
 assertEquals("test2", list.get(1));
 }

 @Test
 public void testDeserializationWithScalar() throws IOException {
 // Prepare test data
 JavaType javaType = MAPPER.getTypeFactory().constructType(Integer.class);
 TypeIdResolver idResolver = new SimpleTypeIdResolver();
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(javaType, idResolver, "_type", false, Integer.class);
 JsonFactory jsonFactory = new JsonFactory();
 JsonParser jp = jsonFactory.createParser("42");

 // Deserialize with scalar
 Object deserializedObject = deserializer.deserializeTypedFromScalar(jp, new TestContext());

 // Assert deserialization result
 assertEquals(42, deserializedObject);
 }

 @Test
 public void testDeserializationWithAny() throws IOException {
 // Prepare test data
 JavaType javaType = MAPPER.getTypeFactory().constructType(Object.class);
 TypeIdResolver idResolver = new SimpleTypeIdResolver();
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(javaType, idResolver, "_type", false, Object.class);
 JsonFactory jsonFactory = new JsonFactory();
 JsonParser jp = jsonFactory.createParser("{\"type\":\"java.lang.String\",\"value\":\"test\"}");

 // Deserialize with any
 Object deserializedObject = deserializer.deserializeTypedFromAny(jp, new TestContext());

 // Assert deserialization result
 assertEquals("test", deserializedObject);
 }

 /**
 * Helper class for deserialization context.
 */
 public static class TestContext extends DeserializationContext {
 @Override
 public BeanDescription getBeanDescription() {
 return null;
 }
 }
}