package com.yourpackage.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;

public class StringArrayDeserializerTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @org.junit.BeforeClass
 static void setup() {
 MAPPER.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
 }

 @org.junit.Test
 public void testDeserializeEmptyArray() throws IOException {
 String[] expected = new String[0];
 String[] result = StringArrayDeserializer.instance.deserialize(MAPPER.getFactory().createParser("[]"), MAPPER.getDeserializationContext());
 assertArrayEquals(expected, result);
 }

 @org.junit.Test
 public void testDeserializeArrayWithOneElement() throws IOException {
 String[] expected = new String[]{"test"};
 String[] result = StringArrayDeserializer.instance.deserialize(MAPPER.getFactory().createParser("[\"" + "test" + "\"]"), MAPPER.getDeserializationContext());
 assertArrayEquals(expected, result);
 }

 @org.junit.Test
 public void testDeserializeArrayWithMultipleElements() throws IOException {
 String[] expected = new String[]{"test1", "test2"};
 String[] result = StringArrayDeserializer.instance.deserialize(MAPPER.getFactory().createParser("[\"" + "test1" + "\",\"" + "test2" + "\"]"), MAPPER.getDeserializationContext());
 assertArrayEquals(expected, result);
 }

 @org.junit.Test
 public void testDeserializeArrayWithNullElement() throws IOException {
 String[] expected = new String[]{"test1", null, "test3"};
 String[] result = StringArrayDeserializer.instance.deserialize(MAPPER.getFactory().createParser("[\"" + "test1" + "\",null,\"" + "test3" + "\"]"), MAPPER.getDeserializationContext());
 assertArrayEquals(expected, result);
 }

 @org.junit.Test
 public void testDeserializeArrayWithCustomElementDeserializer() throws IOException {
 JsonDeserializer<String> customDeserializer = new StandardDeserializer<>(String.class);
 StringArrayDeserializer deserializerWithCustomElementDeserializer = new StringArrayDeserializer(customDeserializer);
 String[] expected = new String[]{"test1", "test2"};
 ObjectNode node = MAPPER.createObjectNode();
 ArrayNode arrayNode = node.putArray("values");
 arrayNode.add("test1");
 arrayNode.add("test2");
 String[] result = deserializerWithCustomElementDeserializer.deserialize(MAPPER.getFactory().createParser(node.getValueAsString()), MAPPER.getDeserializationContext());
 assertArrayEquals(expected, result);
 }

 @org.junit.Test(expected = JsonMappingException.class)
 public void testDeserializeNonArray() throws IOException {
 String[] result = StringArrayDeserializer.instance.deserialize(MAPPER.getFactory().createParser("{\"value\":\"test\"}"), MAPPER.getDeserializationContext());
 fail("Expected JsonMappingException but got: " + result.getClass().getName());
 }
}