package com.example;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class JsonValueSerializerTest {

 private ObjectMapper mapper;
 private JsonValueSerializer serializer;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 }

 @Test
 public void testSerializationWithExplicitValueSerializer() throws Exception {
 // Given
 JsonValueSerializer serializer = new JsonValueSerializer(
 new AnnotatedMethod(TestClass.class.getMethod("getExplicitValue")),
 new StdSerializer<String>(String.class)
 );

 // When
 String json = mapper.writeValueAsString(new TestClass());

 // Then
 String expected = "\"explicitValue\"";
 assertEquals(expected, json);
 }

 @Test
 public void testSerializationWithDefaultValueSerializer() throws Exception {
 // Given
serializer = new JsonValueSerializer(
 new AnnotatedMethod(TestClass.class.getMethod("getValueFromAnnotatedMethod"))
 );

 // When
 String json = mapper.writeValueAsString(new TestClass());

 // Then
 String expected = "\"defaultValue\"";
 assertEquals(expected, json);
 }

 @Test
 public void testSerializationWithNaturalType() throws Exception {
 // Given
 JsonValueSerializer serializer = new JsonValueSerializer(
 new AnnotatedMethod(TestClass.class.getMethod("getBooleanValue"))
 );

 // When
 String json = mapper.writeValueAsString(new TestClass(true));

 // Then
 String expected = "true";
 assertEquals(expected, json);
 }

 @Test
 public void testSerializationWithNaturalTypeAndWrappedTypeInformation() throws Exception {
 // Given
 JsonValueSerializer serializer = new JsonValueSerializer(
 new AnnotatedMethod(TestClass.class.getMethod("getBooleanValue")),
 true
 );

 // When
 String json = mapper.writeValueAsString(new TestClass(true));

 // Then
 String expected = "true";
 assertEquals(expected, json);
 }

 @Test
 public void testSerializationWithEnum() throws Exception {
 // Given
 JsonValueSerializer serializer = new JsonValueSerializer(
 new AnnotatedMethod(TestEnumClass.class.getMethod("getValue"))
 );

 // When
 String json = mapper.writeValueAsString(TestEnumClass.BLUE);

 // Then
 String expected = "\"BLUE\"";
 assertEquals(expected, json);
 }

 @Test
 public void testSerializationWithNullValue() throws Exception {
 // Given
 JsonValueSerializer serializer = new JsonValueSerializer(
 new AnnotatedMethod(TestClass.class.getMethod("getValueFromNullAnnotatedMethod"))
 );

 // When
 String json = mapper.writeValueAsString(new TestClass());

 // Then
 assertTrue(json.isEmpty());
 }

 @Test
 public void testSerializationWithThrowingValueMethod() throws Exception {
 // Given
 JsonValueSerializer serializer = new JsonValueSerializer(
 new AnnotatedMethod(TestClass.class.getMethod("getValueFromThrowingAnnotatedMethod"))
 );

 // When / Then
 assertThrows(JsonProcessingException.class, () -> mapper.writeValueAsString(new TestClass()));
 }

 @Test
 public void testSerializationWithValueMethodThatReturnsCollection() throws Exception {
 // Given
 JsonValueSerializer serializer = new JsonValueSerializer(
 new AnnotatedMethod(TestClass.class.getMethod("getValueFromListAnnotatedMethod"))
 );

 // When
 String json = mapper.writeValueAsString(new TestClass(new int[] { 1, 2, 3 }));

 // Then
 String expected = "[1,2,3]";
 assertEquals(expected, json);
 }

 @Test
 public void testSerializationWithValueMethodThatReturnsMap() throws Exception {
 // Given
 JsonValueSerializer serializer = new JsonValueSerializer(
 new AnnotatedMethod(TestClass.class.getMethod("getValueFromMapAnnotatedMethod"))
 );

 // When
 String json = mapper.writeValueAsString(new TestClass(new java.util.HashMap<>()));
 // Note: Map serialization is not supported out of the box, you should use a custom MapSerializer

 // Then
 fail("Map serialization is not supported");
 }

 @Test
 public void testSerializationWithCustomContextualSerializer() throws Exception {
 // Given
 JsonSerializer<Object> customSerializer = new StdSerializer<String>(String.class);
 JsonValueSerializer serializer = new JsonValueSerializer(
 new AnnotatedMethod(TestClass.class.getMethod("getValueFromCustomContextualSerializerAnnotatedMethod")),
 customSerializer
 );

 // When
 String json = mapper.writeValueAsString(new TestClass());

 // Then
 String expected = "\"customValue\"";
 assertEquals(expected, json);
 }
}

class TestClass {
 @JsonValue
 public String getExplicitValue() { return "explicitValue"; }
 public String valueFromAnnotatedMethod;

 @JsonValue
 public String getValueFromAnnotatedMethod() { return "defaultValue"; }

 @JsonValue
 public boolean getBooleanValue() { return true; }

 public boolean valueFromBooleanAnnotatedMethod;

 @JsonValue
 public int getIntValue() { return 42; }

 public int valueFromIntAnnotatedMethod;

 @JsonValue
 public String getStringValue() { return "stringValue"; }

 public String valueFromStringAnnotatedMethod;

 @JsonValue
 public TestEnumClass getEnumValue() { return TestEnumClass.RED; }

 public TestEnumClass valueFromEnumAnnotatedMethod;

 public TestClass(Object value) { valueFromAnnotatedMethod = (String) value; }

 @JsonValue
 public Object getValueFromAnnotatedMethod() { return valueFromAnnotatedMethod; }

 @JsonValue
 public Object getValueFromNullAnnotatedMethod() { return null; }

 @JsonValue
 public Object getValueFromThrowingAnnotatedMethod() throws Exception { throw new RuntimeException(); }

 @JsonValue
 public Object getValueFromListAnnotatedMethod() { return new int[] { 1, 2, 3 }; }

 @JsonValue
 public Object getValueFromMapAnnotatedMethod() { return new java.util.HashMap<>(); }

 @JsonValue
 public Object getValueFromCustomContextualSerializerAnnotatedMethod(JsonSerializer<Object> serializer) { return "customValue"; }
}

enum TestEnumClass { RED, GREEN, BLUE }