package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class JsonNodeDeserializerTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @Test
 public void testDeserializeNull() throws IOException {
 JsonParser p = MAPPER.getFactory().createParser("null");
 JsonNode node = JsonNodeDeserializer.getInstance().deserialize(p, MAPPER.getDeserializationContext());
 assertNull(node);
 }

 @Test
 public void testDeserializeObject() throws IOException {
 String jsonObject = "{\"name\":\"John\",\"age\":30,\"city\":\"New York\"}";
 JsonParser p = MAPPER.getFactory().createParser(jsonObject);
 JsonNode node = JsonNodeDeserializer.getInstance().deserialize(p, MAPPER.getDeserializationContext());
 assertNotNull(node);
 assertEquals("ObjectNode", node.getClass().getName());
 ObjectNode objNode = (ObjectNode) node;
 assertEquals("John", objNode.get("name").asText());
 assertEquals(30, objNode.get("age").asInt());
 assertEquals("New York", objNode.get("city").asText());
 }

 @Test
 public void testDeserializeArray() throws IOException {
 String jsonArray = "[1, 2, 3, 4, 5]";
 JsonParser p = MAPPER.getFactory().createParser(jsonArray);
 JsonNode node = JsonNodeDeserializer.getInstance().deserialize(p, MAPPER.getDeserializationContext());
 assertNotNull(node);
 assertEquals("ArrayNode", node.getClass().getName());
 ArrayNode arrNode = (ArrayNode) node;
 assertEquals(5, arrNode.size());
 for (int i = 0; i < arrNode.size(); i++) {
 assertEquals(i + 1, arrNode.get(i).asInt());
 }
 }

 @Test
 public void testDeserializeInvalidJson() throws IOException {
 JsonParser p = MAPPER.getFactory().createParser("{name:John,age:30,city:New York\"}");
 assertThrows(JsonMappingException.class, () -> JsonNodeDeserializer.getInstance().deserialize(p, MAPPER.getDeserializationContext()));
 }

 @Test
 public void testDeserializeEmptyObject() throws IOException {
 JsonParser p = MAPPER.getFactory().createParser("{}");
 JsonNode node = JsonNodeDeserializer.getInstance().deserialize(p, MAPPER.getDeserializationContext());
 assertNotNull(node);
 assertEquals("ObjectNode", node.getClass().getName());
 ObjectNode objNode = (ObjectNode) node;
 assertFalse(objNode.has("name"));
 assertFalse(objNode.has("age"));
 assertFalse(objNode.has("city"));
 }

 @Test
 public void testDeserializeEmptyArray() throws IOException {
 JsonParser p = MAPPER.getFactory().createParser("[]");
 JsonNode node = JsonNodeDeserializer.getInstance().deserialize(p, MAPPER.getDeserializationContext());
 assertNotNull(node);
 assertEquals("ArrayNode", node.getClass().getName());
 ArrayNode arrNode = (ArrayNode) node;
 assertTrue(arrNode.isEmpty());
 }

 @Test
 public void testDeserializeObjectWithEmptyArray() throws IOException {
 String jsonObjectWithEmptyArray = "{\"name\":\"John\",\"age\":30,\"cities\":[]}";
 JsonParser p = MAPPER.getFactory().createParser(jsonObjectWithEmptyArray);
 JsonNode node = JsonNodeDeserializer.getInstance().deserialize(p, MAPPER.getDeserializationContext());
 assertNotNull(node);
 assertEquals("ObjectNode", node.getClass().getName());
 ObjectNode objNode = (ObjectNode) node;
 assertEquals("John", objNode.get("name").asText());
 assertEquals(30, objNode.get("age").asInt());
 ArrayNode citiesNode = (ArrayNode) objNode.get("cities");
 assertTrue(citiesNode.isEmpty());
 }

 @Test
 public void testDeserializeDuplicateField() throws IOException {
 MAPPER.disable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY);
 String jsonObjectWithDuplicateField = "{\"name\":\"John\",\"name\":\"Jane\"}";
 JsonParser p = MAPPER.getFactory().createParser(jsonObjectWithDuplicateField);
 assertThrows(JsonMappingException.class, () -> JsonNodeDeserializer.getInstance().deserialize(p, MAPPER.getDeserializationContext()));
 }

 @Test
 public void testDeserializeObjectWithNullValue() throws IOException {
 String jsonObjectWithNullValue = "{\"name\":\"John\",\"age\":null}";
 JsonParser p = MAPPER.getFactory().createParser(jsonObjectWithNullValue);
 JsonNode node = JsonNodeDeserializer.getInstance().deserialize(p, MAPPER.getDeserializationContext());
 assertNotNull(node);
 assertEquals("ObjectNode", node.getClass().getName());
 ObjectNode objNode = (ObjectNode) node;
 assertEquals("John", objNode.get("name").asText());
 assertNull(objNode.get("age"));
 }

 @Test
 public void testDeserializeArrayWithNullValue() throws IOException {
 String jsonArrayWithNullValue = "[null, 1, 2, 3, 4]";
 JsonParser p = MAPPER.getFactory().createParser(jsonArrayWithNullValue);
 JsonNode node = JsonNodeDeserializer.getInstance().deserialize(p, MAPPER.getDeserializationContext());
 assertNotNull(node);
 assertEquals("ArrayNode", node.getClass().getName());
 ArrayNode arrNode = (ArrayNode) node;
 assertEquals(5, arrNode.size());
 assertEquals(null, arrNode.get(0));
 for (int i = 1; i < arrNode.size(); i++) {
 assertEquals(i, arrNode.get(i).asInt());
 }
 }

 @Test
 public void testDeserializeObjectWithInvalidType() throws IOException {
 String jsonObjectWithInvalidType = "{\"name\":\"John\",\"age\":30.5}"; // age should be an integer
 JsonParser p = MAPPER.getFactory().createParser(jsonObjectWithInvalidType);
 assertThrows(JsonMappingException.class, () -> JsonNodeDeserializer.getInstance().deserialize(p, MAPPER.getDeserializationContext()));
 }

 @Test
 public void testDeserializeArrayWithInvalidType() throws IOException {
 String jsonArrayWithInvalidType = "[1, \"John\", 3, 4]"; // second element should be an integer
 JsonParser p = MAPPER.getFactory().createParser(jsonArrayWithInvalidType);
 assertThrows(JsonMappingException.class, () -> JsonNodeDeserializer.getInstance().deserialize(p, MAPPER.getDeserializationContext()));
 }
}