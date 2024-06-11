package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonNodeFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonNodeDeserializerTest {

 private ObjectMapper mapper;
 private JsonNodeFactory nodeFactory;

 @BeforeEach
 public void setUp() {
 mapper = new ObjectMapper();
 nodeFactory = mapper.getNodeFactory();
 }

 @Test
 public void testDeserializeNullValue() throws JsonMappingException, JsonProcessingException {
 JsonParser p = mapper.getFactory().createParser("null");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 JsonNode result = JsonNodeDeserializer.instance.deserialize(p, ctxt);
 Assertions.assertInstanceOf(JsonNode.NullNode.class, result);
 }

 @Test
 public void testDeserializeEmptyObject() throws JsonMappingException, JsonProcessingException {
 JsonParser p = mapper.getFactory().createParser("{}");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 JsonNode result = JsonNodeDeserializer.instance.deserialize(p, ctxt);
 ObjectNode objectNode = (ObjectNode) result;
 Assertions.assertEquals(0, objectNode.size());
 }

 @Test
 public void testDeserializeEmptyArray() throws JsonMappingException, JsonProcessingException {
 JsonParser p = mapper.getFactory().createParser("[]");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 JsonNode result = JsonNodeDeserializer.instance.deserialize(p, ctxt);
 ArrayNode arrayNode = (ArrayNode) result;
 Assertions.assertEquals(0, arrayNode.size());
 }

 @Test
 public void testDeserializeSimpleObject1() throws JsonMappingException, JsonProcessingException {
 JsonParser p = mapper.getFactory().createParser("{" +
 "\"name\":\"John\", " +
 "\"age\":30, " +
 "\"city\":\"New York\"" +
 "}");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 JsonNode result = JsonNodeDeserializer.instance.deserialize(p, ctxt);
 ObjectNode objectNode = (ObjectNode) result;
 Assertions.assertEquals("John", objectNode.get("name").asText());
 Assertions.assertEquals(30, objectNode.get("age").asInt());
 Assertions.assertEquals("New York", objectNode.get("city").asText());
 }

 @Test
 public void testDeserializeSimpleObject2() throws JsonMappingException, JsonProcessingException {
 JsonParser p = mapper.getFactory().createParser("{" +
 "\"name\":\"John\"" +
 "}");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 JsonNode result = JsonNodeDeserializer.instance.deserialize(p, ctxt);
 ObjectNode objectNode = (ObjectNode) result;
 Assertions.assertEquals("John", objectNode.get("name").asText());
 Assertions.assertEquals(null, objectNode.get("age"));
 }

 @Test
 public void testDeserializeSimpleArray1() throws JsonMappingException, JsonProcessingException {
 JsonParser p = mapper.getFactory().createParser("[1,2,3]");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 JsonNode result = JsonNodeDeserializer.instance.deserialize(p, ctxt);
 ArrayNode arrayNode = (ArrayNode) result;
 Assertions.assertEquals(3, arrayNode.size());
 Assertions.assertEquals(1, arrayNode.get(0).asInt());
 Assertions.assertEquals(2, arrayNode.get(1).asInt());
 Assertions.assertEquals(3, arrayNode.get(2).asInt());
 }

 @Test
 public void testDeserializeComplexObject1() throws JsonMappingException, JsonProcessingException {
 JsonParser p = mapper.getFactory().createParser("{" +
 "\"name\":{" +
 "\"first\":\"John\", " +
 "\"last\":\"Doe\"" +
 "}," +
 "\"age\":30," +
 "\"city\":{" +
 "\"name\":\"New York\"" +
 "}," +
 "\"hobbies\":[" +
 "\"swimming\"," +
 "\"reading\"" +
 "]" +
 "}");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 JsonNode result = JsonNodeDeserializer.instance.deserialize(p, ctxt);
 ObjectNode objectNode = (ObjectNode) result;
 ObjectNode nameNode = (ObjectNode) objectNode.get("name");
 Assertions.assertEquals("John", nameNode.get("first").asText());
 Assertions.assertEquals("Doe", nameNode.get("last").asText());
 Assertions.assertEquals(30, objectNode.get("age").asInt());
 ObjectNode cityNode = (ObjectNode) objectNode.get("city");
 Assertions.assertEquals("New York", cityNode.get("name").asText());
 Assertions.assertEquals(2, objectNode.get("hobbies").size());
 Assertions.assertEquals("swimming", objectNode.get("hobbies").get(0).asText());
 Assertions.assertEquals("reading", objectNode.get("hobbies").get(1).asText());
 }

 @Test
 public void testDeserializeComplexObject2() throws JsonMappingException, JsonProcessingException {
 JsonParser p = mapper.getFactory().createParser("{" +
 "\"name\":{" +
 "\"first\":null," +
 "\"last\":\"Doe\"" +
 "}," +
 "\"age\":null," +
 "\"city\":{" +
 "\"name\":null" +
 "}" +
 "}");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 JsonNode result = JsonNodeDeserializer.instance.deserialize(p, ctxt);
 ObjectNode objectNode = (ObjectNode) result;
 ObjectNode nameNode = (ObjectNode) objectNode.get("name");
 Assertions.assertNull(nameNode.get("first"));
 Assertions.assertEquals("Doe", nameNode.get("last").asText());
 Assertions.assertNull(objectNode.get("age"));
 ObjectNode cityNode = (ObjectNode) objectNode.get("city");
 Assertions.assertNull(cityNode.get("name"));
 }

 @Test
 public void testDeserializeComplexArray1() throws JsonMappingException, JsonProcessingException {
 JsonParser p = mapper.getFactory().createParser("[{" +
 "\"name\":\"John\"," +
 "\"age\":30," +
 "\"city\":{" +
 "\"name\":\"New York\"" +
 "}" +
 "},{" +
 "\"name\":\"Jane\"," +
 "\"age\":25," +
 "\"city\":{" +
 "\"name\":\"Chicago\"" +
 "}" +
 "}]");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 JsonNode result = JsonNodeDeserializer.instance.deserialize(p, ctxt);
 ArrayNode arrayNode = (ArrayNode) result;
 Assertions.assertEquals(2, arrayNode.size());
 ObjectNode firstObjectNode = (ObjectNode) arrayNode.get(0);
 ObjectNode firstNameNode = (ObjectNode) firstObjectNode.get("name");
 Assertions.assertEquals("John", firstNameNode.get("first").asText());
 Assertions.assertEquals(30, firstObjectNode.get("age").asInt());
 ObjectNode firstCityNode = (ObjectNode) firstObjectNode.get("city");
 Assertions.assertEquals("New York", firstCityNode.get("name").asText());
 ObjectNode secondObjectNode = (ObjectNode) arrayNode.get(1);
 ObjectNode secondNameNode = (ObjectNode) secondObjectNode.get("name");
 Assertions.assertEquals("Jane", secondNameNode.get("first").asText());
 Assertions.assertEquals(25, secondObjectNode.get("age").asInt());
 ObjectNode secondCityNode = (ObjectNode) secondObjectNode.get("city");
 Assertions.assertEquals("Chicago", secondCityNode.get("name").asText());
 }

 @Test
 public void testDeserializeEmptyObjectWithDuplicateKey() throws JsonMappingException, JsonProcessingException {
 JsonParser p = mapper.getFactory().createParser("{1:1, 1:2}");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 ctxt.configure(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY, false);
 JsonNode result = JsonNodeDeserializer.instance.deserialize(p, ctxt);
 ObjectNode objectNode = (ObjectNode) result;
 Assertions.assertEquals(1, objectNode.size());
 Assertions.assertEquals(2, objectNode.get(0).size());
 Assertions.assertEquals(1, objectNode.get(0).get(0).size());
 Assertions.assertEquals(1, objectNode.get(0).get(0).size());
 Assertions.assertEquals(1, objectNode.get(0).get(0).get(0).intValue());
 Assertions.assertEquals(1, objectNode.get(0).get(0).get(1).intValue());
 }

 @Test
 public void testDeserializeEmptyObjectWithDuplicateKey_fail() throws JsonMappingException, JsonProcessingException {
 JsonParser p = mapper.getFactory().createParser("{1:1, 1:2}");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 ctxt.configure(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY, true);
 JsonNode result = JsonNodeDeserializer.instance.deserialize(p, ctxt);
 Assertions.assertThrows(JsonMappingException.class, () -> {
 ObjectNode objectNode = (ObjectNode) result;
 ObjectNode firstObjectNode = (ObjectNode) objectNode.get(0);
 ObjectNode secondObjectNode = (ObjectNode) objectNode.get(1);
 });
 }

 @Test
 public void testDeserializeInvalidJson1() throws JsonMappingException, JsonProcessingException {
 JsonParser p = mapper.getFactory().createParser("{1:\"John\"}");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 JsonNode result = JsonNodeDeserializer.instance.deserialize(p, ctxt);
 Assertions.assertThrows(JsonMappingException.class, () -> {
 ObjectNode objectNode = (ObjectNode) result;
 ObjectNode nameNode = (ObjectNode) objectNode.get(0);
 Assertions.assertFalse(nameNode.isTextual());
 Assertions.assertEquals("1:\"John\"", nameNode.asText());
 });
 }

 @Test
 public void testDeserializeInvalidJson2() throws JsonMappingException, JsonProcessingException {
 JsonParser p = mapper.getFactory().createParser("[1,2,3,4]");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 JsonNode result = JsonNodeDeserializer.instance.deserialize(p, ctxt);
 ArrayNode arrayNode = (ArrayNode) result;
 Assertions.assertEquals(4, arrayNode.size());
 Assertions.assertEquals(1, arrayNode.get(0).size());
 Assertions.assertEquals("1", arrayNode.get(0).asText());
 Assertions.assertEquals(1, arrayNode.get(1).size());
 Assertions.assertEquals("2", arrayNode.get(1).asText());
 Assertions.assertEquals(1, arrayNode.get(2).size());
 Assertions.assertEquals("3", arrayNode.get(2).asText());
 Assertions.assertEquals(1, arrayNode.get(3).size());
 Assertions.assertEquals("4", arrayNode.get(3).asText());
 }
}