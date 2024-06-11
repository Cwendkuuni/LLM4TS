package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.InnerClassProperty;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Constructor;

public class InnerClassPropertyTest {

 private ObjectNode rootNode;
 private JsonFactory jsonFactory;
 private DeserializationTest _deserializerTest;

 @Before
 public void setup() throws Exception {
 jsonFactory = new JsonFactory();
 _deserializerTest = new DeserializationTest();
 rootNode = jsonFactory.createObjectNode();
 }

 @After
 public void teardown() throws Exception {
 _deserializerTest.close();
 }

 @Test
 public void testNormalCase() throws IOException {
 // Given - create test class and inner class with a public constructor
 public static class TestClass {
 public InnerClass innerClass;

 public static class InnerClass {}
 }

 // When - deserialize JSON to TestClass instance
 ObjectNode node = rootNode.putObject("testClass");
 node.setFactory(jsonFactory);
 node.putObject("innerClass", jsonFactory.createObjectNode());
 JsonParser jp = node.asParser();
 TestClass testClass = _deserializerTest.readValue(jp, TestClass.class);

 // Then - innerClass property should be initialized correctly
 InnerClass actualInnerClass = testClass.innerClass;
 assertNotNull(actualInnerClass);

 // And - we can set a new value and it should be deserialized correctly
 ObjectNode newNode = rootNode.putObject("newInnerClass");
 JsonParser newJp = newNode.asParser();
 testClass.innerClass = _deserializerTest.readValue(newJp, InnerClass.class);
 ObjectNode updatedNode = jsonFactory.createObjectNode();
 testClass.innerClass.getClass().getDeclaredMethod("writeValueAsString").invoke(testClass.innerClass, updatedNode);
 String newValue = updatedNode.getTextValue();
 assertEquals("newValue", newValue);

 // And - when deserializing again, the new value should be present
 TestClass deserializedClass = _deserializerTest.readValue(node.asParser(), TestClass.class);
 InnerClass deserializedInnerClass = deserializedClass.innerClass;
 assertNotNull(deserializedInnerClass);
 assertEquals("newValue", deserializedInnerClass.getClass().getDeclaredMethod("writeValueAsString").invoke(deserializedInnerClass).asText());
 }

 @Test
 public void testMissingConstructor() throws IOException {
 // Given - create test class with a non-existent inner constructor
 public static class TestClassMissingConstructor {
 public InnerClass innerClass;

 public static class InnerClass {
 private InnerClass(Object o) {} // constructor not public
 }
 }

 // When - attempt to deserialize JSON to TestClass instance
 ObjectNode node = rootNode.putObject("testClass");
 node.setFactory(jsonFactory);
 node.putObject("innerClass", jsonFactory.createObjectNode());
 JsonParser jp = node.asParser();
 try {
 TestClass testClass = _deserializerTest.readValue(jp, TestClassMissingConstructor.class);
 fail("Expected IllegalArgumentException due to missing constructor");
 } catch (IllegalArgumentException e) {
 // Then - an exception should be thrown
 }
 }

 @Test
 public void testAnnotatedConstructor() throws IOException {
 // Given - create test class with an annotated inner constructor
 public static class TestClassAnnotatedConstructor {
 public InnerClass innerClass;

 public static class InnerClass {
 @JsonCreator
 public InnerClass(@JsonProperty("name") String name) {
 this.name = name;
 }

 private String name;
 }
 }

 // When - deserialize JSON to TestClass instance with innerClass property having a valid name
 ObjectNode node = rootNode.putObject("testClass");
 node.setFactory(jsonFactory);
 node.putObject("innerClass", jsonFactory.createObjectNode().put("name", "testName"));
 JsonParser jp = node.asParser();
 TestClass testClass = _deserializerTest.readValue(jp, TestClassAnnotatedConstructor.class);
 InnerClass actualInnerClass = testClass.innerClass;
 assertEquals("testName", actualInnerClass.name);

 // And - when deserializing again, the inner class instance should be the same
 TestClass deserializedClass = _deserializerTest.readValue(node.asParser(), TestClassAnnotatedConstructor.class);
 InnerClass deserializedInnerClass = deserializedClass.innerClass;
 assertSame(actualInnerClass, deserializedInnerClass);

 // When - deserialize JSON to TestClass instance with innerClass property having an invalid name
 ObjectNode invalidNode = rootNode.putObject("testClass");
 JsonParser invalidJp = invalidNode.asParser();
 try {
 TestClass testClass = _deserializerTest.readValue(invalidJp, TestClassAnnotatedConstructor.class);
 fail("Expected JsonMappingException due to invalid name");
 } catch (JsonMappingException e) {
 // Then - a JsonMappingException should be thrown
 }
 }

 @Test
 public void testValueDeserializer() throws IOException {
 // Given - create test class with an inner class having a value deserializer
 public static class TestClassValueDeserializer {
 public InnerClass innerClass;

 public static class InnerClass {
 @JsonDeserialize(using = StdSerializer.class)
 public int value;
 }
 }

 // When - deserialize JSON to TestClass instance with innerClass property having a valid value
 ObjectNode node = rootNode.putObject("testClass");
 node.setFactory(jsonFactory);
 node.putObject("innerClass", jsonFactory.createObjectNode().putNumberField("value", 42));
 JsonParser jp = node.asParser();
 TestClass testClass = _deserializerTest.readValue(jp, TestClassValueDeserializer.class);
 InnerClass actualInnerClass = testClass.innerClass;
 assertEquals(42, actualInnerClass.value);

 // And - when deserializing again, the inner class instance should be the same
 TestClass deserializedClass = _deserializerTest.readValue(node.asParser(), TestClassValueDeserializer.class);
 InnerClass deserializedInnerClass = deserializedClass.innerClass;
 assertSame(actualInnerClass, deserializedInnerClass);

 // When - deserialize JSON to TestClass instance with innerClass property having an invalid value
 ObjectNode invalidNode = rootNode.putObject("testClass");
 JsonParser invalidJp = invalidNode.asParser();
 try {
 TestClass testClass = _deserializerTest.readValue(invalidJp, TestClassValueDeserializer.class);
 fail("Expected JsonMappingException due to invalid value");
 } catch (JsonMappingException e) {
 // Then - a JsonMappingException should be thrown
 }
 }
}