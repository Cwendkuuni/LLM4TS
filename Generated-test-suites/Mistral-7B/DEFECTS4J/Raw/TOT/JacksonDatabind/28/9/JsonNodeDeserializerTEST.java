package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonNodeDeserializerTest {

 @Test
 public void testGetDeserializerObjectNode() {
 JsonNode node = new ObjectNode(JsonFactory.instance.objectNodeFactory());
 JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
 ObjectMapper mapper = new ObjectMapper();
 ObjectReader reader = mapper.readerFor(ObjectNode.class);

 try {
 ObjectNode deserializedNode = reader.readValue(new JsonFactory(), deserializer, "{\"key\": \"value\"}");
 assertEquals("value", deserializedNode.get("key").asText());
 } catch (JsonProcessingException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testGetDeserializerArrayNode() {
 JsonNode node = new ArrayNode(JsonFactory.instance.nodeFactory());
 JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(ArrayNode.class);
 ObjectMapper mapper = new ObjectMapper();
 ObjectReader reader = mapper.readerFor(ArrayNode.class);

 try {
 ArrayNode deserializedNode = reader.readValue(new JsonFactory(), deserializer, "[1, 2, 3]");
 assertEquals(3, deserializedNode.size());
 for (int i = 0; i < deserializedNode.size(); i++) {
 assertEquals((i + 1), deserializedNode.get(i).asInt());
 }
 } catch (JsonProcessingException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testGetDeserializerOtherType() {
 JsonNode node = new ObjectNode(JsonFactory.instance.objectNodeFactory());
 JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(String.class);
 ObjectMapper mapper = new ObjectMapper();
 ObjectReader reader = mapper.readerFor(String.class);

 try {
 String deserializedString = reader.readValue(new JsonFactory(), deserializer, "{\"key\": \"value\"}");
 assertEquals("value", deserializedString);
 } catch (JsonProcessingException e) {
 throw new RuntimeException(e);
 }
 }

 @Test(expected = JsonMappingException.class)
 public void testGetDeserializerInvalidType() {
 JsonNode node = new ObjectNode(JsonFactory.instance.objectNodeFactory());
 JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(Boolean.class);
 ObjectMapper mapper = new ObjectMapper();
 ObjectReader reader = mapper.readerFor(Boolean.class);

 try {
 Boolean deserializedBoolean = reader.readValue(new JsonFactory(), deserializer, "{\"key\": true}");
 fail("Should not have reached here.");
 } catch (JsonMappingException e) {
 // expected
 }
 }

 @Test
 public void testNullValue() {
 JsonParser parser = JsonFactory.instance.createParser("{}");
 DeserializationContext ctxt = new TestDeserializationContext();
 JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.instance;
 JsonNode node = deserializer.deserialize(parser, ctxt);
 assertNull(node);
 }

 @Test
 public void testInvalidJson() {
 JsonParser parser = JsonFactory.instance.createParser("{");
 DeserializationContext ctxt = new TestDeserializationContext();
 JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.instance;
 try {
 JsonNode node = deserializer.deserialize(parser, ctxt);
 fail("Should not have reached here.");
 } catch (JsonMappingException e) {
 // expected
 }
 }

 @Test
 public void testEmptyJsonArray() {
 JsonParser parser = JsonFactory.instance.createParser("[{}]");
 DeserializationContext ctxt = new TestDeserializationContext();
 JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.instance;
 JsonNode node = deserializer.deserialize(parser, ctxt);
 assertTrue(node instanceof ArrayNode);
 assertTrue(node.size() == 1);
 assertNull(node.get(0));
 }

 @Test
 public void testEmptyJsonObject() {
 JsonParser parser = JsonFactory.instance.createParser("{}");
 DeserializationContext ctxt = new TestDeserializationContext();
 JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.instance;
 JsonNode node = deserializer.deserialize(parser, ctxt);
 assertTrue(node instanceof ObjectNode);
 assertTrue(node.isEmpty());
 }

 @Test
 public void testSimpleJsonObject() {
 JsonParser parser = JsonFactory.instance.createParser("{\"key\": \"value\"}");
 DeserializationContext ctxt = new TestDeserializationContext();
 JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.instance;
 JsonNode node = deserializer.deserialize(parser, ctxt);
 assertTrue(node instanceof ObjectNode);
 assertEquals("value", node.get("key").asText());
 }

 @Test
 public void testSimpleJsonArray() {
 JsonParser parser = JsonFactory.instance.createParser("[1, 2, 3]");
 DeserializationContext ctxt = new TestDeserializationContext();
 JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.instance;
 JsonNode node = deserializer.deserialize(parser, ctxt);
 assertTrue(node instanceof ArrayNode);
 assertEquals(3, node.size());
 for (int i = 0; i < node.size(); i++) {
 assertEquals((i + 1), node.get(i).asInt());
 }
 }

 @Test
 public void testComplexJsonObject() {
 JsonParser parser = JsonFactory.instance.createParser("{\"key1\": 1,\"key2\": [1, 2],\"key3\": \"value3\",\"key4\": null}");
 DeserializationContext ctxt = new TestDeserializationContext();
 JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.instance;
 JsonNode node = deserializer.deserialize(parser, ctxt);
 assertTrue(node instanceof ObjectNode);
 assertEquals(4, node.size());
 assertEquals(1, node.get("key1").asInt());
 assertTrue(node.get("key2") instanceof ArrayNode);
 assertEquals(2, node.get("key2").size());
 assertEquals(1, node.get("key2").get(0).asInt());
 assertEquals(2, node.get("key2").get(1).asInt());
 assertEquals("value3", node.get("key3").asText());
 assertNull(node.get("key4"));
 }

 @Test
 public void testComplexJsonArray() {
 JsonParser parser = JsonFactory.instance.createParser("[1,\"two\",3.5,\"four\",null,\"five\",[6,7],{\"key1\": 1},{\"key2\": [1, 2]}]");
 DeserializationContext ctxt = new TestDeserializationContext();
 JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.instance;
 JsonNode node = deserializer.deserialize(parser, ctxt);
 assertTrue(node instanceof ArrayNode);
 assertEquals(8, node.size());
 assertEquals(1, node.get(0).asInt());
 assertEquals("two", node.get(1).asText());
 assertEquals(3.5, node.get(2).asDouble());
 assertEquals("four", node.get(3).asText());
 assertNull(node.get(4));
 assertEquals("five", node.get(5).asText());
 assertTrue(node.get(6) instanceof ObjectNode);
 assertEquals(1, node.get(6).get("key1").asInt());
 assertTrue(node.get(7) instanceof ArrayNode);
 assertEquals(2, node.get(7).size());
 assertEquals(1, node.get(7).get(0).asInt());
 assertEquals(2, node.get(7).get(1).asInt());
 }

 @Test
 public void testCircularJson() {
 ObjectMapper mapper = new ObjectMapper();
 ObjectNode rootNode = mapper.readValue(new JsonFactory(), ObjectNode.class, "{\"node\": {}}");
 ObjectNode node = rootNode.get("node");
 node.set("self", node);
 try {
 mapper.writeValueAsString(node);
 fail("Should not reach here.");
 } catch (JsonProcessingException e) {
 // expected
 }
 }

 @Test
 public void testEmptyJsonArrayWithComments() {
 JsonParser parser = JsonFactory.instance.createParser("[\r\n]"); // Empty JSON array with comments
 DeserializationContext ctxt = new TestDeserializationContext();
 JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.instance;
 JsonNode node = deserializer.deserialize(parser, ctxt);
 assertTrue(node instanceof ArrayNode);
 assertTrue(node.isEmpty());
 }

 @Test
 public void testEmptyJsonObjectWithComments() {
 JsonParser parser = JsonFactory.instance.createParser("{// Comment}\r\n"); // Empty JSON object with comments
 DeserializationContext ctxt = new TestDeserializationContext();
 JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.instance;
 JsonNode node = deserializer.deserialize(parser, ctxt);
 assertTrue(node instanceof ObjectNode);
 assertTrue(node.isEmpty());
 }

 @Test
 public void testJsonWithInvalidToken() {
 JsonParser parser = JsonFactory.instance.createParser("{\"key\": [1]"); // JSON with invalid token
 DeserializationContext ctxt = new TestDeserializationContext();
 JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.instance;
 try {
 JsonNode node = deserializer.deserialize(parser, ctxt);
 fail("Should not reach here.");
 } catch (JsonMappingException e) {
 // expected
 }
 }

 @Test
 public void testJsonWithNullNode() {
 JsonParser parser = JsonFactory.instance.createParser("[null]"); // JSON with null node
 DeserializationContext ctxt = new TestDeserializationContext();
 JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.instance;
 JsonNode node = deserializer.deserialize(parser, ctxt);
 assertTrue(node instanceof ArrayNode);
 assertEquals(1, node.size());
 assertNull(node.get(0));
 }

 @Test
 public void testJsonWithBinaryNode() {
 ObjectNode rootNode = new ObjectNode(JsonFactory.instance.objectNodeFactory());
 rootNode.put("binary", new BinaryNode(new byte[] { 0x01, 0x02, 0x03 }));
 JsonParser parser = JsonFactory.instance.createParser("{\"binary\": [0x01, 0x02, 0x03]}");
 DeserializationContext ctxt = new TestDeserializationContext();
 JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.instance;
 JsonNode node = deserializer.deserialize(parser, ctxt);
 assertTrue(node instanceof ObjectNode);
 assertEquals(1, node.size());
 assertTrue(node.get("binary") instanceof BinaryNode);
 assertEquals(3, node.get("binary").size());
 byte[] expectedBytes = new byte[] { 0x01, 0x02, 0x03 };
 assertArrayEquals(expectedBytes, node.get("binary").binaryValue());
 }

 @Test
 public void testJsonWithRawValueNode() {
 ObjectNode rootNode = new ObjectNode(JsonFactory.instance.objectNodeFactory());
 rootNode.put("rawValue", new RawValue("raw data"));
 JsonParser parser = JsonFactory.instance.createParser("{\"rawValue\": \"raw data\"}");
 DeserializationContext ctxt = new TestDeserializationContext();
 JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.instance;
 JsonNode node = deserializer.deserialize(parser, ctxt);
 assertTrue(node instanceof ObjectNode);
 assertEquals(1, node.size());
 assertTrue(node.get("rawValue") instanceof RawValue);
 assertEquals("raw data", node.get("rawValue").asText());
 }
}

class TestDeserializationContext extends DeserializationContextBase {
 @Override
 public ObjectFactory getFactory() {
 return new StandardSerializerFactory(StandardSerializer.Instance);
 }

 @Override
 public JsonNodeFactory nodeFactory() {
 return new StandardNodeFactory();
 }
}