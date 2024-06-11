package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonNodeDeserializerTest {

 @Test
 public void testDeserializeNullValue() throws IOException {
 JsonParser p = new JsonFactory().createParser("null");
 DeserializationContext ctxt = new TestContext();
 JsonNode node = JsonNodeDeserializer.instance.deserialize(p, ctxt);
 assertEquals(JsonNode.Value.NULL, node.getNodeType());
 }

 @Test
 public void testDeserializeObjectNode() throws IOException {
 JsonParser p = new JsonFactory().createParser("{\"name\":\"John\",\"age\":42}");
 DeserializationContext ctxt = new TestContext();
 JsonNode node = JsonNodeDeserializer.instance.deserialize(p, ctxt);
 ObjectNode objNode = (ObjectNode) node;
 assertEquals("John", objNode.get("name").asText());
 assertEquals(42, objNode.get("age").asInt());
 }

 @Test
 public void testDeserializeArrayNode() throws IOException {
 JsonParser p = new JsonFactory().createParser("[1,2,3]");
 DeserializationContext ctxt = new TestContext();
 JsonNode node = JsonNodeDeserializer.instance.deserialize(p, ctxt);
 ArrayNode arrNode = (ArrayNode) node;
 assertEquals(3, arrNode.size());
 assertEquals(1, arrNode.get(0).asInt());
 assertEquals(2, arrNode.get(1).asInt());
 assertEquals(3, arrNode.get(2).asInt());
 }

 @Test
 public void testDeserializeEmptyObjectNode() throws IOException {
 JsonParser p = new JsonFactory().createParser("{}");
 DeserializationContext ctxt = new TestContext();
 JsonNode node = JsonNodeDeserializer.instance.deserialize(p, ctxt);
 ObjectNode objNode = (ObjectNode) node;
 assertTrue(objNode.isEmpty());
 }

 @Test
 public void testDeserializeEmptyArrayNode() throws IOException {
 JsonParser p = new JsonFactory().createParser("[]");
 DeserializationContext ctxt = new TestContext();
 JsonNode node = JsonNodeDeserializer.instance.deserialize(p, ctxt);
 ArrayNode arrNode = (ArrayNode) node;
 assertTrue(arrNode.isEmpty());
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeUnexpectedToken() throws IOException {
 JsonParser p = new JsonFactory().createParser("true");
 DeserializationContext ctxt = new TestContext();
 JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
 JsonNode node = deserializer.deserialize(p, ctxt);
 fail("Should have thrown JsonMappingException for unexpected token 'true'");
 }

 @Test
 public void testDeserializeObjectDeserializer() throws IOException {
 JsonParser p = new JsonFactory().createParser("{\"name\":\"John\",\"age\":42}");
 DeserializationContext ctxt = new TestContext();
 JsonDeserializer<?> deserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
 JsonNode node = deserializer.deserialize(p, ctxt);
 ObjectNode objNode = (ObjectNode) node;
 assertEquals("John", objNode.get("name").asText());
 assertEquals(42, objNode.get("age").asInt());
 }

 @Test
 public void testDeserializeArrayDeserializer() throws IOException {
 JsonParser p = new JsonFactory().createParser("[1,2,3]");
 DeserializationContext ctxt = new TestContext();
 JsonDeserializer<?> deserializer = JsonNodeDeserializer.getDeserializer(ArrayNode.class);
 JsonNode node = deserializer.deserialize(p, ctxt);
 ArrayNode arrNode = (ArrayNode) node;
 assertEquals(3, arrNode.size());
 assertEquals(1, arrNode.get(0).asInt());
 assertEquals(2, arrNode.get(1).asInt());
 assertEquals(3, arrNode.get(2).asInt());
 }

 @Test
 public void testDeserializeUnknownType() throws IOException {
 JsonParser p = new JsonFactory().createParser("{\"type\":\"unknown\"}");
 DeserializationContext ctxt = new TestContext();
 JsonDeserializer<?> deserializer = JsonNodeDeserializer.instance;
 JsonNode node = deserializer.deserialize(p, ctxt);
 assertTrue(node instanceof ObjectNode);
 }

 @Test
 public void testDeserializeEmptyObjectDeserializer() throws IOException {
 JsonParser p = new JsonFactory().createParser("{}\\r\n"); // empty object with line break
 DeserializationContext ctxt = new TestContext();
 JsonDeserializer<?> deserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
 JsonNode node = deserializer.deserialize(p, ctxt);
 ObjectNode objNode = (ObjectNode) node;
 assertTrue(objNode.isEmpty());
 }

 @Test
 public void testDeserializeEmptyArrayDeserializer() throws IOException {
 JsonParser p = new JsonFactory().createParser("[]\\r\n"); // empty array with line break
 DeserializationContext ctxt = new TestContext();
 JsonDeserializer<?> deserializer = JsonNodeDeserializer.getDeserializer(ArrayNode.class);
 JsonNode node = deserializer.deserialize(p, ctxt);
 ArrayNode arrNode = (ArrayNode) node;
 assertTrue(arrNode.isEmpty());
 }
}

class TestContext extends DeserializationContext {
 @Override
 public NodeFactory nodeFactory() { return new StandardNodeFactory(); }
}