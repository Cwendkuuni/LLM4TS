package com.example.jsonnodedeserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.BaseNodeDeserializer;
import com.fasterxml.jackson.databind.deser.std.JsonNodeDeserializer;
import com.fasterxml.jackson.databind.node.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class JsonNodeDeserializerTest {

 private JsonParser parser;
 private JsonNodeFactory nodeFactory;

 @Before
 public void setup() throws JsonProcessingException {
 parser = new ObjectMapper().getFactory().createParser("{\"a\":1,\"b\":[2,3]}");
 nodeFactory = parser.getCodec().getNodeFactory();
 }

 @Test
 public void testGetDeserializer() throws IOException {
 JsonNode expectedObjectNode = nodeFactory.objectNode();
 expectedObjectNode.putPOJO("test", new Object());
 JsonDeserializer<?> deserializerObjectNode = JsonNodeDeserializer.getDeserializer(expectedObjectNode.getClass());
 Assert.assertEquals(ObjectNode.class, deserializerObjectNode.getHandledType());

 JsonNode expectedArrayNode = nodeFactory.arrayNode();
 JsonDeserializer<?> deserializerArrayNode = JsonNodeDeserializer.getDeserializer(expectedArrayNode.getClass());
 Assert.assertEquals(ArrayNode.class, deserializerArrayNode.getHandledType());
 }

 @Test
 public void testNullValue() throws IOException {
 JsonNode result = JsonNodeDeserializer.instance.deserialize(parser, null);
 Assert.assertEquals(NullNode.getInstance(), result);
 }

 @Test
 public void testDeserializeObject() throws IOException {
 ObjectNode result = (ObjectNode) JsonNodeDeserializer.instance.deserialize(parser, null);
 ObjectNode expectedObjectNode = nodeFactory.objectNode();
 expectedObjectNode.put("a", 1);
 expectedObjectNode.put("b", 2.0);
 Assert.assertEquals(expectedObjectNode, result);
 }

 @Test
 public void testDeserializeArray() throws IOException {
 ArrayNode result = (ArrayNode) JsonNodeDeserializer.instance.deserialize(parser, null);
 ArrayNode expectedArrayNode = nodeFactory.arrayNode();
 expectedArrayNode.add(3.0);
 expectedArrayNode.add(true);
 Assert.assertEquals(expectedArrayNode, result);
 }

 @Test
 public void testDeserializeAny() throws IOException {
 JsonNode result = JsonNodeDeserializer.instance.deserializeAny(parser, null, nodeFactory);
 Assert.assertEquals(expectedObjectNode, result); // should be ObjectNode as it's the first token
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeInvalidType() throws IOException {
 JsonParser invalidParser = new ObjectMapper().getFactory().createParser("{1}");
 JsonNodeDeserializer.instance.deserialize(invalidParser, null);
 }

 @Test
 public void testHandleDuplicateField() throws IOException {
 ObjectNode objectNode = nodeFactory.objectNode();
 objectNode.put("a", 1);
 objectNode.put("a", 2);
 JsonParser parserWithDuplicateField = new ObjectMapper().getFactory().createParser(objectNode.toString());
 JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
 JsonNode deserializedNode = deserializer.deserialize(parserWithDuplicateField, null);
 Assert.assertEquals(objectNode, deserializedNode); // No exception thrown as per the default implementation
 }
}