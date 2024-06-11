package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.BaseNodeDeserializer;
import com.fasterxml.jackson.databind.deser.std.JsonNodeDeserializer;
import com.fasterxml.jackson.databind.node.*;
import org.junit.*;
import static org.junit.Assert.*;

public class JsonNodeDeserializerTest {

 private JsonParser parser;
 private ObjectMapper mapper;

 @Before
 public void setUp() throws Exception {
 mapper = new ObjectMapper();
 parser = mapper.getFactory().createParser("[]");
 }

 @Test
 public void testGetDeserializerForObjectNode() throws Exception {
 JsonNode node = mapper.readValue(
 "{ \"name\": \"John\", \"age\": 30, \"city\": \"New York\" }", ObjectNode.class);
 JsonDeserializer<ObjectNode> deserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
 assertEquals(node, deserializer.deserialize(parser, new JsonFactory(), null));
 }

 @Test
 public void testGetDeserializerForArrayNode() throws Exception {
 JsonNode node = mapper.readValue("[1, 2, 3]", ArrayNode.class);
 JsonDeserializer<ArrayNode> deserializer = JsonNodeDeserializer.getDeserializer(ArrayNode.class);
 assertEquals(node, deserializer.deserialize(parser, new JsonFactory(), null));
 }

 @Test
 public void testGetDeserializerForAnyJsonNode() throws Exception {
 JsonNode node = mapper.readValue("[1, {\"name\": \"John\"}]", JsonNode.class);
 JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.getDeserializer(JsonNode.class);
 assertEquals(node, deserializer.deserialize(parser, new JsonFactory(), null));
 }

 @Test(expected = JsonMappingException.class)
 public void testGetDeserializerForInvalidNodeClass() throws Exception {
 JsonDeserializer<String> deserializer = JsonNodeDeserializer.getDeserializer(String.class);
 deserializer.deserialize(parser, new JsonFactory(), null);
 }

 @Test
 public void testDeserializeNullValue() throws Exception {
 JsonDeserializer<JsonNode> deserializer = JsonNodeDeserializer.instance;
 JsonNode node = deserializer.deserialize(parser, new JsonFactory(), null);
 assertNull(node);
 }

 @Test
 public void testDeserializeObject() throws Exception {
 JsonParser p = mapper.getFactory().createParser("{\"name\": \"John\", \"age\": 30, \"city\": \"New York\"}");
 JsonDeserializer<ObjectNode> deserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
 ObjectNode node = (ObjectNode) deserializer.deserialize(p, new JsonFactory(), null);
 assertEquals("John", node.get("name").asText());
 assertEquals(30, node.get("age").asInt());
 assertEquals("New York", node.get("city").asText());
 }

 @Test
 public void testDeserializeArray() throws Exception {
 JsonParser p = mapper.getFactory().createParser("[1, 2, 3]");
 JsonDeserializer<ArrayNode> deserializer = JsonNodeDeserializer.getDeserializer(ArrayNode.class);
 ArrayNode node = (ArrayNode) deserializer.deserialize(p, new JsonFactory(), null);
 assertEquals(3, node.size());
 assertEquals(1, node.get(0).asInt());
 assertEquals(2, node.get(1).asInt());
 assertEquals(3, node.get(2).asInt());
 }

 @Test
 public void testDeserializeEmptyObject() throws Exception {
 JsonParser p = mapper.getFactory().createParser("{}");
 JsonDeserializer<ObjectNode> deserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
 ObjectNode node = (ObjectNode) deserializer.deserialize(p, new JsonFactory(), null);
 assertTrue(node.isEmpty());
 }

 @Test
 public void testDeserializeEmptyArray() throws Exception {
 JsonParser p = mapper.getFactory().createParser("[]");
 JsonDeserializer<ArrayNode> deserializer = JsonNodeDeserializer.getDeserializer(ArrayNode.class);
 ArrayNode node = (ArrayNode) deserializer.deserialize(p, new JsonFactory(), null);
 assertTrue(node.isEmpty());
 }

 @Test
 public void testDeserializeNullValueInArray() throws Exception {
 JsonParser p = mapper.getFactory().createParser("[null]");
 JsonDeserializer<ArrayNode> deserializer = JsonNodeDeserializer.getDeserializer(ArrayNode.class);
 ArrayNode node = (ArrayNode) deserializer.deserialize(p, new JsonFactory(), null);
 assertEquals(1, node.size());
 assertNull(node.get(0));
 }

 @Test
 public void testDeserializeEmptyObjectWithDuplicateKey() throws Exception {
 JsonParser p = mapper.getFactory().createParser("{ \"name\": \"John\", \"name\": \"Jane\"}");
 JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
 ObjectNode node = (ObjectNode) deserializer.deserialize(p, new JsonFactory(), null);
 assertEquals(2, node.size());
 assertNotNull(node.get("name"));
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeInvalidJson() throws Exception {
 JsonParser p = mapper.getFactory().createParser("{ name: 1");
 JsonDeserializer<ObjectNode> deserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
 deserializer.deserialize(p, new JsonFactory(), null);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeMissingRoot() throws Exception {
 JsonParser p = mapper.getFactory().createParser("{ \"name\": \"John\"}");
 JsonDeserializer<ArrayNode> deserializer = JsonNodeDeserializer.getDeserializer(ArrayNode.class);
 deserializer.deserialize(p, new JsonFactory(), null);
 }

 @After
 public void tearDown() throws Exception {
 parser.close();
 }

}