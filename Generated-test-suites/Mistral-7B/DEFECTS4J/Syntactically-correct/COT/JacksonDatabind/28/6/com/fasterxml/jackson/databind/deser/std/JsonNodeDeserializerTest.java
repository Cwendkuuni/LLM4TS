package com.example.jsonnodedeserializer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import org.junit.Before;
import org.junit.Test;

public class JsonNodeDeserializerTest {

 private JsonNodeFactory nodeFactory;
 private JsonParser mockParser;
 private DeserializationContext mockContext;

 @Before
 public void setup() {
 nodeFactory = mock(JsonNodeFactory.class);
 mockParser = mock(JsonParser.class);
 mockContext = mock(DeserializationContext.class);
 }

 @Test
 public void testDeserializeNullValue() throws IOException {
 // Arrange
 when(mockParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_NULL.getValue());

 // Act
 JsonNode result = JsonNodeDeserializer.getInstance().deserialize(mockParser, mockContext);

 // Assert
 assertEquals(JsonNodeType.NULL, result.getNodeType());
 assertNotNull(result);
 }

 @Test
 public void testDeserializeObject() throws IOException {
 // Arrange
 when(mockParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_OBJECT.getValue());

 // Act
 JsonNode result = JsonNodeDeserializer.getInstance().deserialize(mockParser, mockContext, nodeFactory);

 // Assert
 assertEquals(JsonNodeType.OBJECT, result.getNodeType());
 assertNotNull(result);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeObjectWithInvalidStartToken() throws IOException {
 // Arrange
 when(mockParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_ARRAY.getValue());

 // Act and Assert
 JsonNodeDeserializer.getInstance().deserialize(mockParser, mockContext, nodeFactory);
 }

 @Test
 public void testDeserializeArray() throws IOException {
 // Arrange
 when(mockParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_ARRAY.getValue());

 // Act
 JsonNode result = JsonNodeDeserializer.getInstance().deserialize(mockParser, mockContext, nodeFactory);

 // Assert
 assertEquals(JsonNodeType.ARRAY, result.getNodeType());
 assertNotNull(result);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeArrayWithInvalidStartToken() throws IOException {
 // Arrange
 when(mockParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_OBJECT.getValue());

 // Act and Assert
 JsonNodeDeserializer.getInstance().deserialize(mockParser, mockContext, nodeFactory);
 }

 @Test
 public void testDeserializeAny() throws IOException {
 // Arrange
 when(mockParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_STRING.getValue());

 // Act
 JsonNode result = JsonNodeDeserializer.getInstance().deserializeAny(mockParser, mockContext, nodeFactory);

 // Assert
 assertEquals(JsonNodeType.TEXT, result.getNodeType());
 assertNotNull(result);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeAnyWithInvalidToken() throws IOException {
 // Arrange
 when(mockParser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_END_ARRAY.getValue());

 // Act and Assert
 JsonNodeDeserializer.getInstance().deserializeAny(mockParser, mockContext, nodeFactory);
 }

 @Test
 public void testDeserializeObjectWithDuplicateField() throws IOException {
 // Arrange
 when(mockParser.isExpectedStartObjectToken()).thenReturn(true);
 when(mockParser.nextFieldName()).thenReturn("field1");
 when(mockParser.nextFieldName()).thenReturn("field1");

 // Act
 JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
 JsonParser parser = mockParser;
 ObjectNode objectNode = (ObjectNode) deserializer.deserialize(parser, mockContext, nodeFactory);

 // Assert
 assertEquals(1, objectNode.size());
 }

 @Test
 public void testDeserializeObjectWithEmptyName() throws IOException {
 // Arrange
 when(mockParser.isExpectedStartObjectToken()).thenReturn(true);
 when(mockParser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
 when(mockParser.getCurrentName()).thenReturn("");
 when(mockParser.nextToken()).thenReturn(JsonToken.END_OBJECT);

 // Act
 JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
 JsonParser parser = mockParser;
 ObjectNode objectNode = (ObjectNode) deserializer.deserialize(parser, mockContext, nodeFactory);

 // Assert
 assertEquals(0, objectNode.size());
 }

 @Test
 public void testDeserializeObjectWithEmptyObject() throws IOException {
 // Arrange
 when(mockParser.isExpectedStartObjectToken()).thenReturn(true);
 when(mockParser.nextToken()).thenReturn(JsonToken.END_OBJECT);

 // Act
 JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
 JsonParser parser = mockParser;
 ObjectNode objectNode = (ObjectNode) deserializer.deserialize(parser, mockContext, nodeFactory);

 // Assert
 assertEquals(0, objectNode.size());
 }

 @Test
 public void testDeserializeArrayWithEmptyArray() throws IOException {
 // Arrange
 when(mockParser.isExpectedStartArrayToken()).thenReturn(true);
 when(mockParser.nextToken()).thenReturn(JsonToken.END_ARRAY);

 // Act
 JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
 JsonParser parser = mockParser;
 ArrayNode arrayNode = (ArrayNode) deserializer.deserialize(parser, mockContext, nodeFactory);

 // Assert
 assertEquals(0, arrayNode.size());
 }
}