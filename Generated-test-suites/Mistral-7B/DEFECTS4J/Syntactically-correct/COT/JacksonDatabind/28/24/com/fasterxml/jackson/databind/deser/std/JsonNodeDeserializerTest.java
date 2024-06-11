package com.example.jsonnodedeserializer;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.*;
import org.junit.Before;
import org.junit.Test;

public class JsonNodeDeserializerTest {

 private JsonParser parser;
 private JsonNodeDeserializer deserializer;
 private ObjectMapper mapper;

 @Before
 public void setup() {
 deserializer = new JsonNodeDeserializer();
 mapper = new ObjectMapper();
 parser = mapper.getFactory().createParser("{}");
 }

 @Test
 public void testGetNullValue() throws IOException {
 JsonNode result = deserializer.getNullValue(parser.getDeserializationContext());
 assertNotNull(result);
 assertEquals(JsonNode.ValueType.NULL, result.getNodeType());
 }

 @Test
 public void testGetNullValueDeprecated() throws IOException {
 JsonNode result = deserializer.getNullValue();
 assertNotNull(result);
 assertEquals(JsonNode.ValueType.NULL, result.getNodeType());
 }

 @Test
 public void testDeserializeObject() throws IOException {
 ObjectNode expected = mapper.readValue("{\"name\":\"John\",\"age\":30}", ObjectNode.class);
 ObjectNode result = (ObjectNode) deserializer.deserialize(parser, parser.getDeserializationContext(), mapper.getNodeFactory());
 assertEquals(expected.toString(), result.toString());
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeObjectWithInvalidToken() throws IOException {
 ObjectDeserializer.getInstance(); // Trigger deserialization of ObjectNode
 parser.nextToken(); // Skip START_OBJECT
 deserializer.deserialize(parser, parser.getDeserializationContext(), mapper.getNodeFactory());
 }

 @Test
 public void testDeserializeArray() throws IOException {
 ArrayNode expected = mapper.readValue("[1, 2, 3]", ArrayNode.class);
 ArrayNode result = (ArrayNode) deserializer.deserialize(parser, parser.getDeserializationContext(), mapper.getNodeFactory());
 assertEquals(expected.toString(), result.toString());
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeArrayWithInvalidToken() throws IOException {
 ArrayDeserializer.getInstance(); // Trigger deserialization of ArrayNode
 parser.nextToken(); // Skip START_ARRAY
 deserializer.deserialize(parser, parser.getDeserializationContext(), mapper.getNodeFactory());
 }

 @Test
 public void testDeserializeAny() throws IOException {
 parser.nextValue("42"); // INT
 JsonNode result = deserializer.deserialize(parser, parser.getDeserializationContext(), mapper.getNodeFactory());
 assertEquals(42, result.asInt());

 parser.nextValue(true); // BOOLEAN
 result = deserializer.deserialize(parser, parser.getDeserializationContext(), mapper.getNodeFactory());
 assertTrue(result.asBoolean());

 parser.nextValue(null); // NULL
 result = deserializer.deserialize(parser, parser.getDeserializationContext(), mapper.getNodeFactory());
 assertNull(result);

 parser.nextValue("{\"name\":\"John\"}"); // OBJECT
 result = deserializer.deserialize(parser, parser.getDeserializationContext(), mapper.getNodeFactory());
 assertInstanceOf(ObjectNode.class, result);

 parser.nextValue("[1, 2, 3]"); // ARRAY
 result = deserializer.deserialize(parser, parser.getDeserializationContext(), mapper.getNodeFactory());
 assertInstanceOf(ArrayNode.class, result);
 }

 @Test
 public void testDeserializeAnyWithInvalidToken() throws IOException {
 parser.nextValue(null); // NULL
 deserializer.deserialize(parser, parser.getDeserializationContext(), mapper.getNodeFactory());
 parser.nextValue("invalid"); // INVALID_TOKEN
 assertThrows(JsonMappingException.class, () -> deserializer.deserialize(parser, parser.getDeserializationContext(), mapper.getNodeFactory()));
 }
}