package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonNodeDeserializerTest {

 private ObjectMapper objectMapper;
 private JsonParser mockJsonParser;
 private JsonNodeFactory nodeFactory;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 nodeFactory = objectMapper.getNodeFactory();
 mockJsonParser = objectMapper.getFactory().createParser("{}");
 }

 @Test
 public void testNullValue() throws JsonProcessingException {
 JsonNode actual = JsonNodeDeserializer.instance.deserialize(mockJsonParser, objectMapper.getDeserializationContext());
 assertEquals(JsonNode.Null.class, actual.getClass());
 }

 @Test
 public void testDeserializeObject() throws JsonProcessingException {
 JsonNode expected = nodeFactory.objectNode();
 expected.put("name", nodeFactory.textNode("John"));
 expected.put("age", 30);

 JsonParser parser = objectMapper.getFactory().createParser("{\"name\":\"John\",\"age\":30}");
 JsonNode actual = JsonNodeDeserializer.ObjectDeserializer.getInstance().deserialize(parser, objectMapper.getDeserializationContext());
 assertEquals(expected, actual);
 }

 @Test
 public void testDeserializeArray() throws JsonProcessingException {
 JsonNode expected = nodeFactory.arrayNode();
 expected.add(nodeFactory.numberNode(1));
 expected.add(nodeFactory.numberNode(2));
 expected.add(nodeFactory.textNode("three"));

 JsonParser parser = objectMapper.getFactory().createParser("[1,2,\"three\"]");
 JsonNode actual = JsonNodeDeserializer.ArrayDeserializer.getInstance().deserialize(parser, objectMapper.getDeserializationContext());
 assertEquals(expected, actual);
 }

 @Test
 public void testDeserializeAny() throws JsonProcessingException {
 JsonParser parser = objectMapper.getFactory().createParser("{\"name\":\"John\",\"age\":30,\"status\":true}");
 JsonNode actual = JsonNodeDeserializer.instance.deserialize(parser, objectMapper.getDeserializationContext());
 JsonNode expectedObjectNode = nodeFactory.objectNode();
 expectedObjectNode.put("name", nodeFactory.textNode("John"));
 expectedObjectNode.put("age", nodeFactory.numberNode(30));
 expectedObjectNode.put("status", nodeFactory.booleanNode(true));
 assertEquals(expectedObjectNode, actual);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeInvalidJson() throws JsonProcessingException {
 JsonParser parser = objectMapper.getFactory().createParser("{name:\"John\"");
 JsonNodeDeserializer.instance.deserialize(parser, objectMapper.getDeserializationContext());
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeUnexpectedToken() throws JsonProcessingException {
 JsonParser parser = objectMapper.getFactory().createParser("[]1");
 JsonNodeDeserializer.instance.deserialize(parser, objectMapper.getDeserializationContext());
 }
}