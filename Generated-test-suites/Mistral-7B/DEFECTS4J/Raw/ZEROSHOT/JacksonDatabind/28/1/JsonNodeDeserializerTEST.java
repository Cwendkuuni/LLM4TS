package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.util.RawValue;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonNodeDeserializerTest {

 private JsonParser jsonParser;
 private JsonNodeFactory nodeFactory;
 private JsonDeserializer<JsonNode> jsonNodeDeserializer;

 @Before
 public void setup() throws JsonProcessingException {
 jsonParser = new JsonParser();
 jsonParser.setCodec(new ObjectMapper());
 nodeFactory = jsonParser.getNodeFactory();
 jsonNodeDeserializer = JsonNodeDeserializer.getDeserializer(JsonNode.class);
 }

 @Test
 public void testDeserializeNullValue() throws IOException {
 JsonNode result = jsonNodeDeserializer.deserialize(jsonParser, null);
 assertEquals(JsonNode.Null.class, result.getClass());
 }

 @Test
 public void testDeserializeObject() throws IOException {
 String json = "{\"name\":\"John\",\"age\":30}";
 jsonParser.setCharacterEscapes(false);
 jsonParser.readValueAsString(json);
 JsonNode expected = nodeFactory.objectNode();
 expected.put("name", nodeFactory.textNode("John"));
 expected.put("age", nodeFactory.numberNode(30));
 JsonNode result = jsonNodeDeserializer.deserialize(jsonParser, null);
 assertEquals(expected, result);
 }

 @Test
 public void testDeserializeArray() throws IOException {
 String json = "[1,2,3]";
 jsonParser.setCharacterEscapes(false);
 jsonParser.readValueAsString(json);
 JsonNode expected = nodeFactory.arrayNode();
 expected.add(nodeFactory.numberNode(1));
 expected.add(nodeFactory.numberNode(2));
 expected.add(nodeFactory.numberNode(3));
 JsonNode result = jsonNodeDeserializer.deserialize(jsonParser, null);
 assertEquals(expected, result);
 }

 @Test
 public void testDeserializeAny() throws IOException {
 String json = "{\"number\":1,\"string\":\"test\"}";
 jsonParser.setCharacterEscapes(false);
 jsonParser.readValueAsString(json);
 JsonNode result = jsonNodeDeserializer.deserialize(jsonParser, null);
 JsonNode expectedNumber = nodeFactory.numberNode(1);
 JsonNode expectedString = nodeFactory.textNode("test");
 JsonNode expectedObject = nodeFactory.objectNode();
 expectedObject.put("number", expectedNumber);
 expectedObject.put("string", expectedString);
 assertTrue(expectedObject.isPojo());
 assertTrue(expectedObject.isContainerNode());
 assertEquals(expectedObject, result);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeInvalidJson() throws IOException {
 jsonParser.setCharacterEscapes(false);
 jsonParser.readValueAsString("{");
 JsonNode result = jsonNodeDeserializer.deserialize(jsonParser, null);
 fail("Should throw JsonMappingException for invalid JSON");
 }
}