package com.example.jsonnodedeserializer;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.*;
import org.junit.Test;

/**
 * Test class for JsonNodeDeserializer
 */
public class JsonNodeDeserializerTest {

 private ObjectMapper mapper;

 @BeforeClass
 public void setup() {
 mapper = new ObjectMapper();
 }

 @Test
 public void testGetDeserializer() throws IOException {
 Class<?> types[] = new Class<?>[]{ObjectNode.class, ArrayNode.class, String.class};
 for (Class<?> type : types) {
 JsonDeserializer<?> deserializer = JsonNodeDeserializer.getDeserializer(type);
 assertEquals(JsonNode.class, deserializer.getType());
 }
 }

 @Test
 public void testGetNullValue() throws IOException {
 JsonNode result = JsonNodeDeserializer.getInstance().getNullValue(mapper.getDeserializationContext());
 assertNotNull(result);
 assertEquals(JsonNode.ValueType.NULL, result.getNodeType());
 }

 @Test
 public void testDeserializeObject() throws IOException {
 ObjectNode expected = mapper.readValue("{\"name\":\"John\",\"age\":30}", ObjectNode.class);
 ObjectNode result = JsonNodeDeserializer.getInstance().deserializeObject(mapper.getFactory().createParser("{\"name\":\"John\",\"age\":30}"), mapper.getDeserializationContext(), mapper.getFactory());
 assertEquals(expected, result);
 }

 @Test
 public void testDeserializeArray() throws IOException {
 ArrayNode expected = mapper.readValue("[1,2,3]", ArrayNode.class);
 ArrayNode result = JsonNodeDeserializer.getInstance().deserializeArray(mapper.getFactory().createParser("[1,2,3]"), mapper.getDeserializationContext(), mapper.getFactory());
 assertEquals(expected, result);
 }

 @Test
 public void testDeserializeAny_ObjectNode() throws IOException {
 ObjectNode expected = mapper.readValue("{\"name\":\"John\",\"age\":30}", ObjectNode.class);
 JsonNode result = JsonNodeDeserializer.getInstance().deserializeAny(mapper.getFactory().createParser("{\"name\":\"John\",\"age\":30}"), mapper.getDeserializationContext(), mapper.getFactory());
 assertEquals(expected, result);
 }

 @Test
 public void testDeserializeAny_ArrayNode() throws IOException {
 ArrayNode expected = mapper.readValue("[1,2,3]", ArrayNode.class);
 JsonNode result = JsonNodeDeserializer.getInstance().deserializeAny(mapper.getFactory().createParser("[1,2,3]"), mapper.getDeserializationContext(), mapper.getFactory());
 assertEquals(expected, result);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeAny_UnexpectedToken() throws IOException {
 JsonNodeDeserializer deserializer = JsonNodeDeserializer.getInstance();
 JsonParser parser = mapper.getFactory().createParser("[]");
 deserializer.deserializeAny(parser, mapper.getDeserializationContext(), mapper.getFactory());
 parser.nextToken(); // Advance to unexpected token
 deserializer.deserializeAny(parser, mapper.getDeserializationContext(), mapper.getFactory());
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeObject_EmptyObject() throws IOException {
 JsonParser parser = mapper.getFactory().createParser("{}");
 ObjectDeserializer deserializer = JsonNodeDeserializer.ObjectDeserializer.getInstance();
 ObjectNode result = (ObjectNode) deserializer.deserialize(parser, mapper.getDeserializationContext());
 assertNotNull(result);
 assertTrue(result.isEmpty());
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeArray_EmptyArray() throws IOException {
 JsonParser parser = mapper.getFactory().createParser("[]");
 ArrayDeserializer deserializer = JsonNodeDeserializer.ArrayDeserializer.getInstance();
 ArrayNode result = (ArrayNode) deserializer.deserialize(parser, mapper.getDeserializationContext());
 assertNotNull(result);
 assertTrue(result.isEmpty());
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeObject_InvalidJson() throws IOException {
 JsonParser parser = mapper.getFactory().createParser("{\"name\":\"John\""); // Incomplete JSON Object
 ObjectDeserializer deserializer = JsonNodeDeserializer.ObjectDeserializer.getInstance();
 ObjectNode result = (ObjectNode) deserializer.deserialize(parser, mapper.getDeserializationContext());
 fail("Expected JsonMappingException but got: " + result);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeArray_InvalidJson() throws IOException {
 JsonParser parser = mapper.getFactory().createParser("[1,2"); // Incomplete JSON Array
 ArrayDeserializer deserializer = JsonNodeDeserializer.ArrayDeserializer.getInstance();
 ArrayNode result = (ArrayNode) deserializer.deserialize(parser, mapper.getDeserializationContext());
 fail("Expected JsonMappingException but got: " + result);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeObject_DuplicateField() throws IOException {
 JsonParser parser = mapper.getFactory().createParser("{\"name\":\"John\",\"name\":\"Jane\"}");
 ObjectDeserializer deserializer = JsonNodeDeserializer.ObjectDeserializer.getInstance();
 ObjectNode result = (ObjectNode) deserializer.deserialize(parser, mapper.getDeserializationContext());
 fail("Expected JsonMappingException but got: " + result);
 }

}