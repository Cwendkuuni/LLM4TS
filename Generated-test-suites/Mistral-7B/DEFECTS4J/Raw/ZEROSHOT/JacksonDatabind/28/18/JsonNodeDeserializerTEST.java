package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.util.RawValue;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonNodeDeserializerTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testDeserializeNullValue() throws Exception {
 JsonParser p = mapper.getFactory().createParser("null");
 JsonNodeDeserializer deserializer = JsonNodeDeserializer.getDeserializer(JsonNode.class);
 JsonNode result = deserializer.deserialize(p, null);
 assertTrue(result instanceof NullNode);
 }

 @Test
 public void testDeserializeObject() throws Exception {
 JsonParser p = mapper.getFactory().createParser("{\"name\":\"John\",\"age\":30}");
 JsonNodeDeserializer deserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
 ObjectNode result = (ObjectNode) deserializer.deserialize(p, null);
 assertEquals("John", result.get("name").asText());
 assertEquals(30, result.get("age").asInt());
 }

 @Test
 public void testDeserializeArray() throws Exception {
 JsonParser p = mapper.getFactory().createParser("[1, 2, 3]");
 JsonNodeDeserializer deserializer = JsonNodeDeserializer.getDeserializer(ArrayNode.class);
 ArrayNode result = (ArrayNode) deserializer.deserialize(p, null);
 assertEquals(3, result.size());
 assertEquals(1, result.get(0).asInt());
 assertEquals(2, result.get(1).asInt());
 assertEquals(3, result.get(2).asInt());
 }

 @Test
 public void testDeserializeAny() throws Exception {
 JsonParser p = mapper.getFactory().createParser("{\"name\":\"John\",\"age\":30, \"hobbies\":[\"reading\", \"swimming\"]}");
 JsonNodeDeserializer deserializer = JsonNodeDeserializer.getDeserializer(JsonNode.class);
 JsonNode result = deserializer.deserialize(p, null);
 assertInstanceOf(ObjectNode.class, result);
 ObjectNode objResult = (ObjectNode) result;
 assertEquals("John", objResult.get("name").asText());
 assertEquals(30, objResult.get("age").asInt());
 ArrayNode hobbies = (ArrayNode) objResult.get("hobbies");
 assertEquals(2, hobbies.size());
 assertEquals("reading", hobbies.get(0).asText());
 assertEquals("swimming", hobbies.get(1).asText());
 }

 @Test(expected = JsonProcessingException.class)
 public void testDeserializeInvalidJson() throws Exception {
 JsonParser p = mapper.getFactory().createParser("{invalid:json}");
 JsonNodeDeserializer deserializer = JsonNodeDeserializer.getDeserializer(JsonNode.class);
 deserializer.deserialize(p, null);
 fail("Expected JsonProcessingException");
 }
}