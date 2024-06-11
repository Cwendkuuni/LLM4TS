package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonNodeFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class JsonNodeDeserializerTest {

 private ObjectMapper mapper;
 private JsonNodeFactory nodeFactory;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 nodeFactory = mapper.getNodeFactory();
 }

 @Test
 public void testNullDeserialization() throws JsonProcessingException {
 JsonParser parser = mapper.getFactory().createParser("null");
 JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(null.getClass());
 JsonNode result = deserializer.deserialize(parser, null);
 assertEquals(JsonNode.Value.nullValue(), result);
 }

 @Test
 public void testObjectDeserialization() throws JsonProcessingException {
 JsonParser parser = mapper.getFactory().createParser("{\"name\":\"John\",\"age\":30}");
 JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
 JsonNode result = deserializer.deserialize(parser, null);
 ObjectNode objNode = (ObjectNode) result;
 assertEquals("John", objNode.get("name").asText());
 assertEquals(30, objNode.get("age").asInt());
 }

 @Test
 public void testArrayDeserialization() throws JsonProcessingException {
 JsonParser parser = mapper.getFactory().createParser("[\"apple\",\"banana\",3]");
 JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(ArrayNode.class);
 JsonNode result = deserializer.deserialize(parser, null);
 ArrayNode arrayNode = (ArrayNode) result;
 assertEquals(3, arrayNode.size());
 assertEquals("apple", arrayNode.get(0).asText());
 assertEquals("banana", arrayNode.get(1).asText());
 assertEquals(3, arrayNode.get(2).asInt());
 }

 @Test
 public void testUnexpectedTokenDeserialization() throws JsonProcessingException {
 JsonParser parser = mapper.getFactory().createParser("{\"name\":\"John\"}");
 parser.nextToken(); // consume START_OBJECT token
 JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
 try {
 JsonNode result = deserializer.deserialize(parser, null);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // expected
 }
 }

 @Test
 public void testEmptyObjectDeserialization() throws JsonProcessingException {
 JsonParser parser = mapper.getFactory().createParser("{}");
 JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
 JsonNode result = deserializer.deserialize(parser, null);
 ObjectNode objNode = (ObjectNode) result;
 assertNotNull(objNode);
 assertEquals(0, objNode.size());
 }

 @Test
 public void testEmptyArrayDeserialization() throws JsonProcessingException {
 JsonParser parser = mapper.getFactory().createParser("[]");
 JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(ArrayNode.class);
 JsonNode result = deserializer.deserialize(parser, null);
 ArrayNode arrayNode = (ArrayNode) result;
 assertNotNull(arrayNode);
 assertEquals(0, arrayNode.size());
 }

 @Test
 public void testUnexpectedEndOfStreamDeserialization() throws JsonProcessingException {
 JsonParser parser = mapper.getFactory().createParser("{\"name\":\"John\"}");
 parser.nextToken(); // consume START_OBJECT token
 parser.nextToken(); // consume FIELD_NAME token
 parser.nextToken(); // consume END_OBJECT token
 JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
 try {
 JsonNode result = deserializer.deserialize(parser, null);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // expected
 }
 }

 @Test
 public void testDuplicateKeyDeserialization() throws JsonProcessingException {
 JsonParser parser = mapper.getFactory().createParser("{\"name\":\"John\",\"name\":\"Jane\"}");
 JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
 try {
 JsonNode result = deserializer.deserialize(parser, null);
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // expected
 assertEquals("Duplicate field 'name' for ObjectNode: not allowed when FAIL_ON_READING_DUP_TREE_KEY enabled", e.getMessage());
 }
 }
}