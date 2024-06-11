package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonNodeDeserializerTest {

 private JsonNodeDeserializer deserializer;
 private ObjectMapper mapper;

 @Before
 public void setup() {
 deserializer = JsonNodeDeserializer.instance;
 mapper = new ObjectMapper();
 }

 @Test
 public void testDeserializeNullValue() throws IOException {
 JsonParser parser = mapper.getFactory().createParser("null");
 JsonNode result = deserializer.deserialize(parser, mapper.getDeserializationContext());
 assertNotNull(result);
 assertTrue(result instanceof JsonNull);
 }

 @Test
 public void testDeserializeArray() throws IOException {
 JsonParser parser = mapper.getFactory().createParser("[1,2,3]");
 JsonNode result = deserializer.deserialize(parser, mapper.getDeserializationContext());
 assertEquals(JsonNodeType.ARRAY, result.getType());
 ArrayNode arrayNode = (ArrayNode) result;
 assertEquals(3, arrayNode.size());
 assertEquals(1, arrayNode.get(0).asInt());
 assertEquals(2, arrayNode.get(1).asInt());
 assertEquals(3, arrayNode.get(2).asInt());
 }

 @Test
 public void testDeserializeObject() throws IOException {
 JsonParser parser = mapper.getFactory().createParser("{\"a\":1,\"b\":\"hello\",\"c\":[1,2]}");
 JsonNode result = deserializer.deserialize(parser, mapper.getDeserializationContext());
 assertEquals(JsonNodeType.OBJECT, result.getType());
 ObjectNode objectNode = (ObjectNode) result;
 assertEquals("a", objectNode.get("a").asText());
 assertEquals("1", objectNode.get("a").asText());
 assertEquals("b", objectNode.get("b").asText());
 assertEquals("hello", objectNode.get("b").asText());
 ArrayNode arrayNode = (ArrayNode) objectNode.get("c");
 assertEquals(2, arrayNode.size());
 assertEquals(1, arrayNode.get(0).asInt());
 assertEquals(2, arrayNode.get(1).asInt());
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeInvalidJson() throws IOException {
 JsonParser parser = mapper.getFactory().createParser("{a:1,b:1}");
 deserializer.deserialize(parser, mapper.getDeserializationContext());
 fail("Expected JsonMappingException to be thrown");
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeInvalidJsonObject() throws IOException {
 JsonParser parser = mapper.getFactory().createParser("{a:1,b:[1,2]}");
 deserializer.deserialize(parser, mapper.getDeserializationContext());
 fail("Expected JsonMappingException to be thrown");
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeInvalidJsonArray() throws IOException {
 JsonParser parser = mapper.getFactory().createParser("[1,a:1]");
 deserializer.deserialize(parser, mapper.getDeserializationContext());
 fail("Expected JsonMappingException to be thrown");
 }
}