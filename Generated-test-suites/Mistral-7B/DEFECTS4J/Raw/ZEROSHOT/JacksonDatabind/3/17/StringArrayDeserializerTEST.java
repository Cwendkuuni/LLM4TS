package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class StringArrayDeserializerTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @Test
 public void testDefaultDeserialization() throws IOException, JsonProcessingException {
 StringArrayDeserializer serializer = StringArrayDeserializer.instance;
 ObjectReader reader = MAPPER.readerForArrayOf(String.class);
 String[] expected = {"test1", "test2", "test3"};
 JsonParser jsonParser = MAPPER.getFactory().createParser("[\"" + String.join("\"\",\"", expected) + "\"]");
 String[] result = serializer.deserialize(jsonParser, DeserializationContext.emptyContext());
 assertArrayEquals(expected, result);
 }

 @Test
 public void testCustomDeserialization() throws IOException, JsonProcessingException {
 StringArrayDeserializer serializer = new StringArrayDeserializer(new MyStringDeserializer());
 ObjectReader reader = MAPPER.readerForArrayOf(String.class);
 String[] expected = {"custom1", "custom2", "custom3"};
 ObjectNode node = MAPPER.createObjectNode();
 node.put("elements", "[{\"class\":\"com.mypackage.MyStringDeserializer\",\"value\":\"custom1\"},{\"class\":\"com.mypackage.MyStringDeserializer\",\"value\":\"custom2\"},{\"class\":\"com.mypackage.MyStringDeserializer\",\"value\":\"custom3\"}]");
 String[] result = serializer.deserialize(MAPPER.treeToValues(node), DeserializationContext.emptyContext());
 assertArrayEquals(expected, result);
 }

 @Test(expected = IOException.class)
 public void testInvalidInput() throws IOException {
 StringArrayDeserializer serializer = StringArrayDeserializer.instance;
 JsonParser jsonParser = MAPPER.getFactory().createParser("\"invalid input\"");
 String[] result = serializer.deserialize(jsonParser, DeserializationContext.emptyContext());
 fail("Expected IOException but got " + result.getClass().getName());
 }

 @Test(expected = IOException.class)
 public void testNullInput() throws IOException {
 StringArrayDeserializer serializer = StringArrayDeserializer.instance;
 JsonParser jsonParser = MAPPER.getFactory().createParser("null");
 String[] result = serializer.deserialize(jsonParser, DeserializationContext.emptyContext());
 fail("Expected IOException but got " + result.getClass().getName());
 }

 @Test(expected = JsonMappingException.class)
 public void testNonArrayInput() throws IOException, JsonProcessingException {
 StringArrayDeserializer serializer = StringArrayDeserializer.instance;
 ObjectReader reader = MAPPER.readerForValue("{\"key\":\"value\"}");
 String[] result = serializer.deserialize(reader.readValueAsTree(), DeserializationContext.emptyContext());
 fail("Expected JsonMappingException but got " + result.getClass().getName());
 }

 @Test(expected = JsonMappingException.class)
 public void testSingleValueInput() throws IOException, JsonProcessingException {
 StringArrayDeserializer serializer = new StringArrayDeserializer(); // Instance without _elementDeserializer
 ObjectReader reader = MAPPER.readerForValue("\"testValue\"");
 String[] result = serializer.deserialize(reader.readValueAsTree(), DeserializationContext.emptyContext());
 fail("Expected JsonMappingException but got " + result.getClass().getName());
 }

 @Test(expected = JsonMappingException.class)
 public void testEmptyArrayInput() throws IOException, JsonProcessingException {
 StringArrayDeserializer serializer = StringArrayDeserializer.instance;
 ObjectReader reader = MAPPER.readerForArrayOf(String.class);
 String[] result = serializer.deserialize(reader.readValueAsTree(), DeserializationContext.emptyContext());
 assertEquals(new String[0], result);
 }

 @Test
 public void testEmptyInput() throws IOException {
 StringArrayDeserializer serializer = StringArrayDeserializer.instance;
 JsonParser jsonParser = MAPPER.getFactory().createParser("");
 String[] result = serializer.deserialize(jsonParser, DeserializationContext.emptyContext());
 assertEquals(new String[0], result);
 }

 @Test
 public void testInlineDeserialization() throws IOException, JsonProcessingException {
 StringArrayDeserializer serializer = StringArrayDeserializer.instance;
 ObjectReader reader = MAPPER.readerForArrayOf(String.class);
 ObjectNode node = MAPPER.createObjectNode();
 node.put("elements", "[\"test1\",\"test2\",\"test3\"]");
 TypeDeserializer typeDeserializer = MAPPER.getTypeFactory().constructType(String[].class);
 String[] result = serializer.deserializeWithType(MAPPER.treeToValues(node), DeserializationContext.emptyContext(), typeDeserializer);
 assertArrayEquals(new String[]{"test1", "test2", "test3"}, result);
 }

 @Test
 public void testComplexDeserialization() throws IOException, JsonProcessingException {
 StringArrayDeserializer serializer = new StringArrayDeserializer(new ComplexStringDeserializer());
 ObjectReader reader = MAPPER.readerForArrayOf(String.class);
 ObjectNode node = MAPPER.createObjectNode();
 node.put("elements", "[{\"class\":\"com.mypackage.ComplexStringDeserializer\",\"value\":\"complex1\"},{\"class\":\"com.mypackage.ComplexStringDeserializer\",\"value\":\"complex2\"},{\"class\":\"com.mypackage.ComplexStringDeserializer\",\"value\":\"complex3\"}]");
 String[] result = serializer.deserialize(MAPPER.treeToValues(node), DeserializationContext.emptyContext());
 assertArrayEquals(new String[]{"complex1", "complex2", "complex3"}, result);
 }
}

class MyStringDeserializer extends StdDeserializer<String> {
 @Override
 public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return "custom";
 }
}

class ComplexStringDeserializer extends StdDeserializer<String> {
 @Override
 public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return "complex";
 }
}