package com.example.jsonnodedeserializer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.JsonNodeDeserializer;
import com.fasterxml.jackson.databind.node.*;

import org.junit.Test;

public class JsonNodeDeserializerTest {

 private static final ObjectMapper objectMapper = new ObjectMapper();

 @Test
 public void testGetDeserializerObjectNode() {
 JsonNodeDeserializer jsonNodeDeserializer = JsonNodeDeserializer.getDeserializer(ObjectNode.class);
 assertEquals(ObjectNode.class, jsonNodeDeserializer.getType());
 }

 @Test
 public void testGetDeserializerArrayNode() {
 JsonNodeDeserializer jsonNodeDeserializer = JsonNodeDeserializer.getDeserializer(ArrayNode.class);
 assertEquals(ArrayNode.class, jsonNodeDeserializer.getType());
 }

 @Test
 public void testGetDeserializerNull() {
 JsonNodeDeserializer jsonNodeDeserializer = JsonNodeDeserializer.getDeserializer(null);
 assertNotNull(jsonNodeDeserializer);
 assertEquals(JsonNode.class, jsonNodeDeserializer.getType());
 }

 @Test
 public void testDeserializeNullValue() throws IOException {
 JsonParser jsonParser = mock(JsonParser.class);
 JsonNode deserializedNode = JsonNodeDeserializer.instance.deserialize(jsonParser, mock(DeserializationContext.class));
 assertEquals(JsonNode.Value.nullValue(), deserializedNode);
 }

 @Test
 public void testDeserializeObject() throws IOException {
 ObjectNode expectedObjectNode = objectMapper.createObjectNode();
 expectedObjectNode.put("key1", "value1");
 expectedObjectNode.put("key2", "value2");

 JsonParser jsonParser = mock(JsonParser.class);
 when(jsonParser.parseValueAsObject(anyString())).thenReturn(expectedObjectNode);

 JsonNode deserializedNode = JsonNodeDeserializer.instance.deserialize(jsonParser, mock(DeserializationContext.class));
 ObjectNode deserializedObjectNode = (ObjectNode) deserializedNode;
 assertEquals(expectedObjectNode, deserializedObjectNode);
 }

 @Test
 public void testDeserializeArray() throws IOException {
 ArrayNode expectedArrayNode = objectMapper.createArrayNode();
 expectedArrayNode.add("value1");
 expectedArrayNode.add("value2");

 JsonParser jsonParser = mock(JsonParser.class);
 when(jsonParser.parseValueAsArray()).thenReturn(expectedArrayNode);

 JsonNode deserializedNode = JsonNodeDeserializer.instance.deserialize(jsonParser, mock(DeserializationContext.class));
 ArrayNode deserializedArrayNode = (ArrayNode) deserializedNode;
 assertEquals(expectedArrayNode, deserializedArrayNode);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeInvalidJson() throws IOException {
 JsonParser jsonParser = mock(JsonParser.class);
 JsonNodeDeserializer jsonNodeDeserializer = JsonNodeDeserializer.instance;
 jsonParser.expectNextToken(JsonToken.ID_NULL);
 jsonParser.nextToken();
 jsonParser.expectNextToken(JsonToken.ID_START_OBJECT);
 jsonParser.nextToken();
 jsonParser.expectNextToken(JsonToken.ID_NUMBER_INT);
 jsonParser.nextToken();
 jsonParser.expectNextToken(JsonToken.ID_FIELD_NAME);
 jsonParser.nextToken();
 jsonParser.expectNextToken(JsonToken.ID_STRING);
 jsonParser.nextToken();
 jsonParser.expectNextToken(JsonToken.ID_NUMBER_INT);
 jsonParser.nextToken();
 jsonParser.expectNextToken(JsonToken.ID_END_OBJECT);
 jsonParser.nextToken();
 jsonParser.expectNextToken(JsonToken.ID_END_ARRAY);
 jsonParser.nextToken();

 jsonNodeDeserializer.deserialize(jsonParser, mock(DeserializationContext.class));
 }

}