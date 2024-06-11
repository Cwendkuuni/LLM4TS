package com.example.jsonnodedeserializer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.BaseNodeDeserializer;
import com.fasterxml.jackson.databind.deser.std.JsonNodeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.*;

import org.junit.Before;
import org.junit.Test;

public class JsonNodeDeserializerTest {

 private JsonParser mockParser;
 private DeserializationContext mockContext;
 private JsonNodeFactory mockNodeFactory;
 private JsonDeserializer<?> mockDeserializer;

 @Before
 public void setup() {
 mockParser = mock(JsonParser.class);
 mockContext = mock(DeserializationContext.class);
 mockNodeFactory = mock(JsonNodeFactory.class);
 mockDeserializer = mock(JsonDeserializer.class);
 }

 @Test
 public void testGetNullValue() throws IOException {
 JsonNode result = JsonNodeDeserializer.instance.getNullValue(mockContext);
 assertNotNull(result);
 assertEquals(JsonNode.ValueType.NULL, result.getValueType());
 }

 @Test
 public void testGetNullValueDeprecated() throws IOException {
 JsonNode result = JsonNodeDeserializer.instance.getNullValue();
 assertNotNull(result);
 assertEquals(JsonNode.ValueType.NULL, result.getValueType());
 }

 @Test
 public void testDeserializeObject() throws IOException {
 when(mockParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_OBJECT);
 ObjectNode result = (ObjectNode) JsonNodeDeserializer.instance.deserialize(mockParser, mockContext, mockNodeFactory);
 assertNotNull(result);
 assertEquals(JsonNode.ValueType.OBJECT, result.getValueType());
 }

 @Test(expected = IOException.class)
 public void testDeserializeObjectInvalidToken() throws IOException {
 when(mockParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_ARRAY);
 JsonNodeDeserializer.instance.deserialize(mockParser, mockContext, mockNodeFactory);
 }

 @Test
 public void testDeserializeArray() throws IOException {
 when(mockParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_ARRAY);
 ArrayNode result = (ArrayNode) JsonNodeDeserializer.instance.deserialize(mockParser, mockContext, mockNodeFactory);
 assertNotNull(result);
 assertEquals(JsonNode.ValueType.ARRAY, result.getValueType());
 }

 @Test(expected = IOException.class)
 public void testDeserializeArrayInvalidToken() throws IOException {
 when(mockParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_OBJECT);
 JsonNodeDeserializer.instance.deserialize(mockParser, mockContext, mockNodeFactory);
 }

 @Test
 public void testDeserializeAnyWithObjectNode() throws IOException {
 when(mockParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_OBJECT);
 JsonNode node = JsonNodeDeserializer.instance.deserializeAny(mockParser, mockContext, mockNodeFactory);
 assertNotNull(node);
 assertEquals(JsonNode.ValueType.OBJECT, node.getValueType());
 }

 @Test
 public void testDeserializeAnyWithArrayNode() throws IOException {
 when(mockParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_ARRAY);
 JsonNode node = JsonNodeDeserializer.instance.deserializeAny(mockParser, mockContext, mockNodeFactory);
 assertNotNull(node);
 assertEquals(JsonNode.ValueType.ARRAY, node.getValueType());
 }

 @Test
 public void testDeserializeAnyWithString() throws IOException {
 when(mockParser.getText()).thenReturn("test");
 JsonNode node = JsonNodeDeserializer.instance.deserializeAny(mockParser, mockContext, mockNodeFactory);
 assertNotNull(node);
 assertEquals(JsonNode.ValueType.TEXT, node.getValueType());
 assertEquals("test", node.asText());
 }

 @Test
 public void testDeserializeAnyWithInt() throws IOException {
 when(mockParser.getText()).thenReturn("42");
 JsonNode node = JsonNodeDeserializer.instance.deserializeAny(mockParser, mockContext, mockNodeFactory);
 assertNotNull(node);
 assertEquals(JsonNode.ValueType.NUMBER, node.getValueType());
 assertEquals(42, node.asInt());
 }

 @Test
 public void testDeserializeAnyWithBoolean() throws IOException {
 when(mockParser.getText()).thenReturn("true");
 JsonNode node = JsonNodeDeserializer.instance.deserializeAny(mockParser, mockContext, mockNodeFactory);
 assertNotNull(node);
 assertEquals(JsonNode.ValueType.BOOLEAN, node.getValueType());
 assertEquals(true, node.asBoolean());
 }

 @Test
 public void testDeserializeAnyWithNull() throws IOException {
 when(mockParser.getText()).thenReturn(null);
 JsonNode node = JsonNodeDeserializer.instance.deserializeAny(mockParser, mockContext, mockNodeFactory);
 assertNotNull(node);
 assertEquals(JsonNode.ValueType.NULL, node.getValueType());
 }

 @Test
 public void testDeserializeAnyWithInvalidToken() throws IOException {
 when(mockParser.getText()).thenReturn("invalidToken");
 JsonNode node = JsonNodeDeserializer.instance.deserializeAny(mockParser, mockContext, mockNodeFactory);
 assertNotNull(node);
 assertEquals(JsonNode.ValueType.TEXT, node.getValueType());
 assertEquals("invalidToken", node.asText());
 }

 @Test
 public void testObjectDeserializer() throws IOException {
 JsonDeserializer<?> deserializer = JsonNodeDeserializer.ObjectDeserializer.getInstance();
 JsonNode node = deserializer.deserialize(mockParser, mockContext, mockNodeFactory);
 assertNotNull(node);
 assertEquals(JsonNode.ValueType.OBJECT, node.getValueType());
 }

 @Test(expected = IOException.class)
 public void testObjectDeserializerInvalidToken() throws IOException {
 JsonDeserializer<?> deserializer = JsonNodeDeserializer.ObjectDeserializer.getInstance();
 when(mockParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_ARRAY);
 deserializer.deserialize(mockParser, mockContext, mockNodeFactory);
 }

 @Test
 public void testArrayDeserializer() throws IOException {
 JsonDeserializer<?> deserializer = JsonNodeDeserializer.ArrayDeserializer.getInstance();
 JsonNode node = deserializer.deserialize(mockParser, mockContext, mockNodeFactory);
 assertNotNull(node);
 assertEquals(JsonNode.ValueType.ARRAY, node.getValueType());
 }

 @Test(expected = IOException.class)
 public void testArrayDeserializerInvalidToken() throws IOException {
 JsonDeserializer<?> deserializer = JsonNodeDeserializer.ArrayDeserializer.getInstance();
 when(mockParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_OBJECT);
 deserializer.deserialize(mockParser, mockContext, mockNodeFactory);
 }
}