package com.example.jsonnodedeserializertest;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

import org.junit.Test;

public class JsonNodeDeserializerTest {

 private ObjectMapper mapper;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 }

 @Test
 public void testGetDeserializer() {
 Class<?> objectNodeClass = ObjectNode.class;
 Class<?> arrayNodeClass = ArrayNode.class;
 JsonDeserializer<?> expectedObjectDeserializer = ObjectDeserializer.getInstance();
 JsonDeserializer<?> expectedArrayDeserializer = ArrayDeserializer.getInstance();

 JsonDeserializer<?> deserializerForObjectNode = JsonNodeDeserializer.getDeserializer(objectNodeClass);
 JsonDeserializer<?> deserializerForArrayNode = JsonNodeDeserializer.getDeserializer(arrayNodeClass);

 assertEquals(expectedObjectDeserializer.getClass(), deserializerForObjectNode.getClass());
 assertEquals(expectedArrayDeserializer.getClass(), deserializerForArrayNode.getClass());
 }

 @Test
 public void testGetNullValue() throws Exception {
 JsonNode actualNullNode = JsonNodeDeserializer.getInstance().getNullValue(mapper.getDeserializationContext());
 JsonNode expectedNullNode = NullNode.getInstance();

 assertEquals(expectedNullNode, actualNullNode);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeInvalidJson() throws Exception {
 JsonParser jsonParser = mapper.getFactory().createParser("{invalid:json}");
 JsonNodeDeserializer deserializer = JsonNodeDeserializer.getInstance();

 deserializer.deserialize(jsonParser, mapper.getDeserializationContext());
 fail("Expected JsonMappingException but it did not occur.");
 }

 @Test
 public void testDeserializeObject() throws Exception {
 String jsonString = "{\"field1\":1,\"field2\":[\"hello\",\"world\"]}";
 JsonParser jsonParser = mapper.getFactory().createParser(jsonString);
 JsonNode actualNode = JsonNodeDeserializer.getInstance().deserialize(jsonParser, mapper.getDeserializationContext());

 ObjectNode expectedNode = mapper.readValue(jsonString, ObjectNode.class);

 assertEquals(expectedNode, actualNode);
 }

 @Test
 public void testDeserializeArray() throws Exception {
 String jsonString = "[1,2,\"three\",null,true]";
 JsonParser jsonParser = mapper.getFactory().createParser(jsonString);
 JsonNode actualNode = JsonNodeDeserializer.getInstance().deserialize(jsonParser, mapper.getDeserializationContext());

 ArrayNode expectedNode = mapper.readValue(jsonString, ArrayNode.class);

 assertEquals(expectedNode, actualNode);
 }

 @Test
 public void testDeserializeEmptyObject() throws Exception {
 JsonParser jsonParser = mapper.getFactory().createParser("{}");
 JsonNode actualNode = JsonNodeDeserializer.getInstance().deserialize(jsonParser, mapper.getDeserializationContext());

 ObjectNode expectedNode = mapper.createObjectNode();

 assertEquals(expectedNode, actualNode);
 }

 @Test
 public void testDeserializeEmptyArray() throws Exception {
 JsonParser jsonParser = mapper.getFactory().createParser("[]");
 JsonNode actualNode = JsonNodeDeserializer.getInstance().deserialize(jsonParser, mapper.getDeserializationContext());

 ArrayNode expectedNode = mapper.createArrayNode();

 assertEquals(expectedNode, actualNode);
 }

 @Test
 public void testDeserializeObjectWithNestedArray() throws Exception {
 String jsonString = "{\"field1\":[1,2],\"field2\":{\"subfield\":[\"hello\",\"world\"]}}";
 JsonParser jsonParser = mapper.getFactory().createParser(jsonString);
 JsonNode actualNode = JsonNodeDeserializer.getInstance().deserialize(jsonParser, mapper.getDeserializationContext());

 ObjectNode expectedNode = mapper.readValue(jsonString, ObjectNode.class);
 ArrayNode nestedArrayNode = (ArrayNode) expectedNode.path("field2").get("subfield");

 assertEquals(nestedArrayNode.size(), actualNode.path("field2").path("subfield").size());
 for (int i = 0; i < nestedArrayNode.size(); i++) {
 assertEquals(nestedArrayNode.get(i).asText(), actualNode.path("field2").path("subfield").get(i).asText());
 }
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeInvalidJsonWithUnexpectedToken() throws Exception {
 String jsonString = "{\"field1\":[1,2],\"field2\":}"; // missing closing bracket
 JsonParser jsonParser = mapper.getFactory().createParser(jsonString);
 JsonNodeDeserializer deserializer = JsonNodeDeserializer.getInstance();

 deserializer.deserialize(jsonParser, mapper.getDeserializationContext());
 fail("Expected JsonMappingException but it did not occur.");
 }

 @Test
 public void testDeserializeInvalidJsonWithDuplicateField() throws Exception {
 String jsonString = "{\"field1\":1,\"field1\":[\"hello\",\"world\"]}";
 JsonParser jsonParser = mapper.getFactory().createParser(jsonString);
 JsonNodeDeserializer deserializer = JsonNodeDeserializer.getInstance();

 try {
 JsonNode actualNode = deserializer.deserialize(jsonParser, mapper.getDeserializationContext());
 fail("Expected JsonMappingException but it did not occur.");
 } catch (JsonMappingException e) {
 String expectedMessage = "Duplicate field 'field1' for ObjectNode: not allowed when FAIL_ON_READING_DUP_TREE_KEY enabled";
 assertTrue(e.getMessage().contains(expectedMessage));
 }
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeInvalidJsonWithUnexpectedObject() throws Exception {
 String jsonString = "{1}"; // number as opening brace
 JsonParser jsonParser = mapper.getFactory().createParser(jsonString);
 JsonNodeDeserializer deserializer = JsonNodeDeserializer.getInstance();

 deserializer.deserialize(jsonParser, mapper.getDeserializationContext());
 fail("Expected JsonMappingException but it did not occur.");
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeInvalidJsonWithUnexpectedString() throws Exception {
 String jsonString = "{\"field1\":[1,\"hello\"]}"; // string instead of array
 JsonParser jsonParser = mapper.getFactory().createParser(jsonString);
 JsonNodeDeserializer deserializer = JsonNodeDeserializer.getInstance();

 deserializer.deserialize(jsonParser, mapper.getDeserializationContext());
 fail("Expected JsonMappingException but it did not occur.");
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeInvalidJsonWithUnexpectedFieldName() throws Exception {
 String jsonString = "{\"field\":1}"; // invalid field name
 JsonParser jsonParser = mapper.getFactory().createParser(jsonString);
 JsonNodeDeserializer deserializer = JsonNodeDeserializer.getInstance();

 deserializer.deserialize(jsonParser, mapper.getDeserializationContext());
 fail("Expected JsonMappingException but it did not occur.");
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeInvalidJsonWithUnexpectedTokenAfterObject() throws Exception {
 String jsonString = "{\"field1\":1}invalid"; // invalid token after object
 JsonParser jsonParser = mapper.getFactory().createParser(jsonString);
 JsonNodeDeserializer deserializer = JsonNodeDeserializer.getInstance();

 deserializer.deserialize(jsonParser, mapper.getDeserializationContext());
 fail("Expected JsonMappingException but it did not occur.");
 }
}