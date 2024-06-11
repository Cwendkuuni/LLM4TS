package com.fasterxml.jackson.databind.jsontype.impl.test;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.test.BaseMapTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import static org.testng.Assert.*;

public class AsWrapperTypeDeserializerTest extends BaseMapTest {

 private ObjectMapper mapper;

 @BeforeClass
 public void setup() throws Exception {
 mapper = new ObjectMapper();
 mapper.registerModule(new SimpleModule()
 .addDeserializer(String.class, new StringDeserializer())
 .addDeserializer(Integer.class, new IntDeserializer())
 );
 }

 @Test
 public void testDeserializeTypedFromObject() throws IOException {
 ObjectNode node = mapper.createObjectNode();
 node.put("type", "com.fasterxml.jackson.databind.jsontype.impl.AsWrapperTypeDeserializerTest$StringDeserializer");
 node.put("value", "{\"type\":\"java.lang.String\",\"value\":\"hello\"}");
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 mapper.constructType(AsWrapperTypeDeserializer.class),
 mapper.getTypeIdResolver(),
 "_typeId",
 false,
 null
 );
 Object result = deserializer.deserializeTypedFromObject(mapper.getFactory().createParser(node.getText()), mapper.getDeserializationContext());
 assertEquals("hello", result);
 }

 @Test
 public void testDeserializeTypedFromArray() throws IOException {
 ObjectNode node = mapper.createObjectNode();
 node.put("type", "com.fasterxml.jackson.databind.jsontype.impl.AsWrapperTypeDeserializerTest$StringDeserializer");
 ObjectNode valueNode = mapper.createObjectNode();
 valueNode.put("type", "java.lang.String");
 valueNode.put("value", "hello");
 node.put("value", valueNode);
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 mapper.constructType(AsWrapperTypeDeserializer.class),
 mapper.getTypeIdResolver(),
 "_typeId",
 false,
 null
 );
 Object result = deserializer.deserializeTypedFromArray(mapper.getFactory().createParser(node.getText()), mapper.getDeserializationContext());
 assertEquals("hello", result);
 }

 @Test
 public void testDeserializeTypedFromScalar() throws IOException {
 ObjectNode node = mapper.createObjectNode();
 node.put("type", "com.fasterxml.jackson.databind.jsontype.impl.AsWrapperTypeDeserializerTest$IntDeserializer");
 node.put("value", 123);
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 mapper.constructType(AsWrapperTypeDeserializer.class),
 mapper.getTypeIdResolver(),
 "_typeId",
 false,
 null
 );
 Object result = deserializer.deserializeTypedFromScalar(mapper.getFactory().createParser(node.getText()), mapper.getDeserializationContext());
 assertEquals(123, result);
 }

 @Test
 public void testDeserializeWithNativeTypeId() throws IOException {
 JsonFactory factory = mapper.getFactory();
 ObjectNode node = factory.createParser("{\"type\":\"_class\",\"value\":{\"name\":\"java.lang.String\",\"typeId\":\"com.fasterxml.jackson.databind.jsontype.impl.AsWrapperTypeDeserializerTest$StringDeserializer\"}}").readValueAsTree();
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 mapper.constructType(AsWrapperTypeDeserializer.class),
 mapper.getTypeIdResolver(),
 "_typeId",
 false,
 null
 );
 Object result = deserializer.deserializeTypedFromObject(factory.createParser(node.getText()), mapper.getDeserializationContext());
 assertEquals("", result); // empty string is the default value for String deserializer
 }

 @Test(expectedExceptions = IOException.class)
 public void testMissingTypeId() throws IOException {
 ObjectNode node = mapper.createObjectNode();
 node.put("type", "com.fasterxml.jackson.databind.jsontype.impl.AsWrapperTypeDeserializerTest$StringDeserializer");
 node.put("value", "hello");
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 mapper.constructType(AsWrapperTypeDeserializer.class),
 mapper.getTypeIdResolver(),
 "_typeId",
 false,
 null
 );
 deserializer.deserializeTypedFromObject(mapper.getFactory().createParser(node.getText()), mapper.getDeserializationContext());
 fail("Expected IOException");
 }

 @Test(expectedExceptions = IOException.class)
 public void testInvalidTypeId() throws IOException {
 ObjectNode node = mapper.createObjectNode();
 node.put("type", "com.fasterxml.jackson.databind.jsontype.impl.AsWrapperTypeDeserializerTest$InvalidDeserializer");
 node.put("value", "hello");
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 mapper.constructType(AsWrapperTypeDeserializer.class),
 mapper.getTypeIdResolver(),
 "_typeId",
 false,
 null
 );
 deserializer.deserializeTypedFromObject(mapper.getFactory().createParser(node.getText()), mapper.getDeserializationContext());
 fail("Expected IOException");
 }

 @Test
 public void testTypeIdVisible() throws IOException {
 ObjectNode node = mapper.createObjectNode();
 node.put("type", "com.fasterxml.jackson.databind.jsontype.impl.AsWrapperTypeDeserializerTest$StringDeserializer");
 node.put("_typeId", "com.fasterxml.jackson.databind.jsontype.impl.AsWrapperTypeDeserializerTest$StringDeserializer");
 node.put("value", "hello");
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 mapper.constructType(AsWrapperTypeDeserializer.class),
 mapper.getTypeIdResolver(),
 "_typeId",
 true,
 null
 );
 Object result = deserializer.deserializeTypedFromObject(mapper.getFactory().createParser(node.getText()), mapper.getDeserializationContext());
 assertEquals("hello", result);
 }

 @Test
 public void testDeserializeWithEmptyTypeId() throws IOException {
 ObjectNode node = mapper.createObjectNode();
 node.put("type", "com.fasterxml.jackson.databind.jsontype.impl.AsWrapperTypeDeserializerTest$StringDeserializer");
 node.put("_typeId", "");
 node.put("value", "hello");
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 mapper.constructType(AsWrapperTypeDeserializer.class),
 mapper.getTypeIdResolver(),
 "_typeId",
 true,
 null
 );
 Object result = deserializer.deserializeTypedFromObject(mapper.getFactory().createParser(node.getText()), mapper.getDeserializationContext());
 assertEquals("hello", result);
 }

 @Test
 public void testDeserializeWithMissingValue() throws IOException {
 ObjectNode node = mapper.createObjectNode();
 node.put("type", "com.fasterxml.jackson.databind.jsontype.impl.AsWrapperTypeDeserializerTest$StringDeserializer");
 node.put("_typeId", "com.fasterxml.jackson.databind.jsontype.impl.AsWrapperTypeDeserializerTest$StringDeserializer");
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 mapper.constructType(AsWrapperTypeDeserializer.class),
 mapper.getTypeIdResolver(),
 "_typeId",
 false,
 null
 );
 try {
 deserializer.deserializeTypedFromObject(mapper.getFactory().createParser(node.getText()), mapper.getDeserializationContext());
 fail("Expected IOException");
 } catch (IOException e) {
 // expected
 }
 }

 @Test
 public void testDeserializeWithUnexpectedToken() throws IOException {
 ObjectNode node = mapper.createObjectNode();
 node.put("type", "com.fasterxml.jackson.databind.jsontype.impl.AsWrapperTypeDeserializerTest$StringDeserializer");
 node.put("_typeId", "com.fasterxml.jackson.databind.jsontype.impl.AsWrapperTypeDeserializerTest$StringDeserializer");
 node.put("value", 123);
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 mapper.constructType(AsWrapperTypeDeserializer.class),
 mapper.getTypeIdResolver(),
 "_typeId",
 false,
 null
 );
 try {
 deserializer.deserializeTypedFromObject(mapper.getFactory().createParser(node.getText()), mapper.getDeserializationContext());
 fail("Expected IOException");
 } catch (IOException e) {
 // expected
 }
 }

 @Test
 public void testDeserializeWithMissingClosingObject() throws IOException {
 ObjectNode node = mapper.createObjectNode();
 node.put("type", "com.fasterxml.jackson.databind.jsontype.impl.AsWrapperTypeDeserializerTest$StringDeserializer");
 node.put("_typeId", "com.fasterxml.jackson.databind.jsontype.impl.AsWrapperTypeDeserializerTest$StringDeserializer");
 node.put("value", "hello");
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 mapper.constructType(AsWrapperTypeDeserializer.class),
 mapper.getTypeIdResolver(),
 "_typeId",
 false,
 null
 );
 try {
 deserializer.deserializeTypedFromObject(mapper.getFactory().createParser(node.getText()), mapper.getDeserializationContext());
 fail("Expected IOException");
 } catch (IOException e) {
 // expected
 }
 }

 private static class StringDeserializer extends StdDeserializer<String> {
 }

 private static class IntDeserializer extends StdDeserializer<Integer> {
 }

 private static class InvalidDeserializer extends StdDeserializer<Object> {
 }
}