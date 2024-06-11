package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class JsonNodeDeserializerTest {

 @Test
 public void testDeserializeNullValue() throws IOException {
 JsonParser p = new ObjectMapper().getFactory().createParser("null");
 DeserializationContext ctxt = new TestDeserializationContext();
 JsonNode node = JsonNodeDeserializer.instance.deserialize(p, ctxt);
 assertEquals(JsonNode.Value.nullValue(), node);
 }

 @Test
 public void testDeserializeObject() throws IOException {
 JsonParser p = new ObjectMapper().getFactory().createParser("{\"name\":\"John\",\"age\":30}");
 DeserializationContext ctxt = new TestDeserializationContext();
 JsonNode node = JsonNodeDeserializer.instance.deserialize(p, ctxt);
 ObjectNode objNode = (ObjectNode) node;
 assertEquals("John", objNode.get("name").asText());
 assertEquals(30, objNode.get("age").asInt());
 }

 @Test
 public void testDeserializeArray() throws IOException {
 JsonParser p = new ObjectMapper().getFactory().createParser("[1,2,3]");
 DeserializationContext ctxt = new TestDeserializationContext();
 JsonNode node = JsonNodeDeserializer.instance.deserialize(p, ctxt);
 ArrayNode arrNode = (ArrayNode) node;
 assertEquals(3, arrNode.size());
 assertEquals(1, arrNode.get(0).asInt());
 assertEquals(2, arrNode.get(1).asInt());
 assertEquals(3, arrNode.get(2).asInt());
 }

 @Test
 public void testDeserializeObjectDeserializerForObjectNode() throws IOException {
 Class<?> nodeClass = ObjectNode.class;
 JsonDeserializer<? extends JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(nodeClass);
 JsonParser p = new ObjectMapper().getFactory().createParser("{\"name\":\"John\",\"age\":30}");
 DeserializationContext ctxt = new TestDeserializationContext();
 JsonNode node = deserializer.deserialize(p, ctxt);
 ObjectNode objNode = (ObjectNode) node;
 assertEquals("John", objNode.get("name").asText());
 assertEquals(30, objNode.get("age").asInt());
 }

 @Test
 public void testDeserializeArrayDeserializerForArrayNode() throws IOException {
 Class<?> nodeClass = ArrayNode.class;
 JsonDeserializer<? extends JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(nodeClass);
 JsonParser p = new ObjectMapper().getFactory().createParser("[1,2,3]");
 DeserializationContext ctxt = new TestDeserializationContext();
 JsonNode node = deserializer.deserialize(p, ctxt);
 ArrayNode arrNode = (ArrayNode) node;
 assertEquals(3, arrNode.size());
 assertEquals(1, arrNode.get(0).asInt());
 assertEquals(2, arrNode.get(1).asInt());
 assertEquals(3, arrNode.get(2).asInt());
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeInvalidJson() throws IOException {
 JsonParser p = new ObjectMapper().getFactory().createParser("invalidJson");
 DeserializationContext ctxt = new TestDeserializationContext();
 JsonNode node = JsonNodeDeserializer.instance.deserialize(p, ctxt);
 fail("Expected JsonMappingException but got: " + node.getClass());
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeMissingField() throws IOException {
 JsonParser p = new ObjectMapper().getFactory().createParser("{\"age\":30}");
 DeserializationContext ctxt = new TestDeserializationContext();
 JsonNode node = JsonNodeDeserializer.instance.deserialize(p, ctxt);
 fail("Expected JsonMappingException but got: " + node.getClass());
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeFieldValueMismatch() throws IOException {
 JsonParser p = new ObjectMapper().getFactory().createParser("{\"name\":\"John\",\"age\":\"30\"}");
 DeserializationContext ctxt = new TestDeserializationContext();
 JsonNode node = JsonNodeDeserializer.instance.deserialize(p, ctxt);
 fail("Expected JsonMappingException but got: " + node.getClass());
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeDuplicateField() throws IOException {
 JsonParser p = new ObjectMapper().getFactory().createParser("{\"name\":\"John\",\"name\":\"Jane\"}");
 DeserializationContext ctxt = new TestDeserializationContext();
 JsonNode node = JsonNodeDeserializer.instance.deserialize(p, ctxt);
 fail("Expected JsonMappingException but got: " + node.getClass());
 }
}