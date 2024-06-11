package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonNodeDeserializerTest {

 private ObjectMapper objectMapper;
 private JsonNodeDeserializer jsonNodeDeserializer;
 private JsonParser mockJsonParser;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 jsonNodeDeserializer = JsonNodeDeserializer.getInstance();
 mockJsonParser = createMockJsonParser();
 }

 @Test
 public void testNullValue() throws Exception {
 JsonNode actual = jsonNodeDeserializer.deserialize(mockJsonParser, objectMapper.getDeserializationContext());
 assertEquals(JsonNode.Value.NULL, actual.getValueAsToken());
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeObjectInvalidToken() throws Exception {
 mockJsonParser.expectAndReturn("nextToken", JsonToken.VALUE_STRING, null);
 jsonNodeDeserializer.deserialize(mockJsonParser, objectMapper.getDeserializationContext());
 fail("Expected JsonMappingException");
 }

 @Test
 public void testDeserializeObjectStartObject() throws Exception {
 mockJsonParser.expectNextToken(JsonToken.START_OBJECT);
 JsonNode actual = jsonNodeDeserializer.deserialize(mockJsonParser, objectMapper.getDeserializationContext());
 assertEquals(JsonNodeType.OBJECT, actual.getType());
 }

 @Test
 public void testDeserializeObjectEndObject() throws Exception {
 mockJsonParser.expectNextToken(JsonToken.END_OBJECT);
 JsonNode actual = jsonNodeDeserializer.deserialize(mockJsonParser, objectMapper.getDeserializationContext());
 assertNull(actual);
 }

 @Test
 public void testDeserializeObjectFieldName() throws Exception {
 mockJsonParser.expectNextToken(JsonToken.FIELD_NAME);
 mockJsonParser.expectNextToken(JsonToken.START_OBJECT);
 JsonNode expected = new ObjectNode(objectMapper.getNodeFactory());
 expected.put("fieldName1", new TextNode("value1"));
 expected.put("fieldName2", new IntNode(42));
 mockJsonParser.expectNextFieldName("fieldName1");
 JsonNode actual1 = jsonNodeDeserializer.deserialize(mockJsonParser, objectMapper.getDeserializationContext());
 assertEquals(expected, actual1);
 mockJsonParser.expectNextFieldName("fieldName2");
 JsonNode actual2 = jsonNodeDeserializer.deserialize(mockJsonParser, objectMapper.getDeserializationContext());
 assertEquals(JsonNodeType.INT, actual2.getType());
 assertEquals(42, actual2.asInt());
 }

 @Test
 public void testDeserializeObjectString() throws Exception {
 mockJsonParser.expectNextToken(JsonToken.FIELD_NAME);
 mockJsonParser.expectNextToken(JsonToken.VALUE_STRING);
 String expected = "value";
 mockJsonParser.expectNextFieldName("fieldName");
 JsonNode actual = jsonNodeDeserializer.deserialize(mockJsonParser, objectMapper.getDeserializationContext());
 assertEquals(expected, actual.getTextValue());
 }

 @Test
 public void testDeserializeObjectNumberInt() throws Exception {
 mockJsonParser.expectNextToken(JsonToken.FIELD_NAME);
 mockJsonParser.expectNextValue(JsonToken.VALUE_NUMBER_INT, 42);
 mockJsonParser.expectNextFieldName("fieldName");
 JsonNode actual = jsonNodeDeserializer.deserialize(mockJsonParser, objectMapper.getDeserializationContext());
 assertEquals(JsonNodeType.INT, actual.getType());
 assertEquals(42, actual.asInt());
 }

 @Test
 public void testDeserializeObjectNumberLong() throws Exception {
 mockJsonParser.expectNextToken(JsonToken.FIELD_NAME);
 mockJsonParser.expectNextValue(JsonToken.VALUE_NUMBER_LONG, 42L);
 mockJsonParser.expectNextFieldName("fieldName");
 JsonNode actual = jsonNodeDeserializer.deserialize(mockJsonParser, objectMapper.getDeserializationContext());
 assertEquals(JsonNodeType.NUMBER, actual.getType());
 assertEquals(42L, actual.asLong());
 }

 @Test
 public void testDeserializeObjectNumberBigDecimal() throws Exception {
 mockJsonParser.expectNextToken(JsonToken.FIELD_NAME);
 mockJsonParser.expectNextValue(JsonToken.VALUE_BIG_DECIMAL, new BigDecimal("42.42"));
 mockJsonParser.expectNextFieldName("fieldName");
 JsonNode actual = jsonNodeDeserializer.deserialize(mockJsonParser, objectMapper.getDeserializationContext());
 assertEquals(JsonNodeType.NUMBER, actual.getType());
 assertEquals("42.42", actual.asText());
 }

 @Test
 public void testDeserializeObjectBooleanTrue() throws Exception {
 mockJsonParser.expectNextToken(JsonToken.FIELD_NAME);
 mockJsonParser.expectNextValue(JsonToken.VALUE_TRUE);
 mockJsonParser.expectNextFieldName("fieldName");
 JsonNode actual = jsonNodeDeserializer.deserialize(mockJsonParser, objectMapper.getDeserializationContext());
 assertEquals(JsonNodeType.BOOLEAN, actual.getType());
 assertTrue(actual.asBoolean());
 }

 @Test
 public void testDeserializeObjectBooleanFalse() throws Exception {
 mockJsonParser.expectNextToken(JsonToken.FIELD_NAME);
 mockJsonParser.expectNextValue(JsonToken.VALUE_FALSE);
 mockJsonParser.expectNextFieldName("fieldName");
 JsonNode actual = jsonNodeDeserializer.deserialize(mockJsonParser, objectMapper.getDeserializationContext());
 assertEquals(JsonNodeType.BOOLEAN, actual.getType());
 assertFalse(actual.asBoolean());
 }

 @Test
 public void testDeserializeObjectArray() throws Exception {
 mockJsonParser.expectNextToken(JsonToken.FIELD_NAME);
 mockJsonParser.expectNextToken(JsonToken.START_ARRAY);
 JsonNode expected = new ArrayNode(objectMapper.getNodeFactory());
 expected.add(new IntNode(1));
 expected.add(new TextNode("2"));
 mockJsonParser.expectNextFieldName("fieldName");
 JsonNode actual = jsonNodeDeserializer.deserialize(mockJsonParser, objectMapper.getDeserializationContext());
 assertEquals(expected, actual);
 }

 @Test
 public void testDeserializeObjectEmptyArray() throws Exception {
 mockJsonParser.expectNextToken(JsonToken.FIELD_NAME);
 mockJsonParser.expectNextToken(JsonToken.END_ARRAY);
 mockJsonParser.expectNextFieldName("fieldName");
 JsonNode actual = jsonNodeDeserializer.deserialize(mockJsonParser, objectMapper.getDeserializationContext());
 assertNull(actual);
 }

 @Test
 public void testDeserializeObjectEmptyObject() throws Exception {
 mockJsonParser.expectNextToken(JsonToken.FIELD_NAME);
 mockJsonParser.expectNextToken(JsonToken.END_OBJECT);
 mockJsonParser.expectNextFieldName("fieldName");
 JsonNode actual = jsonNodeDeserializer.deserialize(mockJsonParser, objectMapper.getDeserializationContext());
 assertNull(actual);
 }

 @Test
 public void testDeserializeObjectNullValue() throws Exception {
 mockJsonParser.expectNextToken(JsonToken.FIELD_NAME);
 mockJsonParser.expectNextToken(JsonToken.VALUE_NULL);
 mockJsonParser.expectNextFieldName("fieldName");
 JsonNode actual = jsonNodeDeserializer.deserialize(mockJsonParser, objectMapper.getDeserializationContext());
 assertNull(actual);
 }

 @Test
 public void testDeserializeObjectObject() throws Exception {
 mockJsonParser.expectNextToken(JsonToken.FIELD_NAME);
 mockJsonParser.expectNextToken(JsonToken.START_OBJECT);
 ObjectNode expected = new ObjectNode(objectMapper.getNodeFactory());
 expected.put("field1", new IntNode(1));
 expected.put("field2", new TextNode("2"));
 mockJsonParser.expectNextFieldName("fieldName");
 JsonNode actual = jsonNodeDeserializer.deserialize(mockJsonParser, objectMapper.getDeserializationContext());
 assertEquals(expected, actual);
 }

 @Test
 public void testDeserializeObjectArrayObject() throws Exception {
 mockJsonParser.expectNextToken(JsonToken.FIELD_NAME);
 mockJsonParser.expectNextToken(JsonToken.START_ARRAY);
 JsonNode array = new ArrayNode(objectMapper.getNodeFactory());
 array.add(new ObjectNode(objectMapper.getNodeFactory()));
 array.add(new ObjectNode(objectMapper.getNodeFactory()));
 mockJsonParser.expectNextFieldName("fieldName");
 JsonNode actual = jsonNodeDeserializer.deserialize(mockJsonParser, objectMapper.getDeserializationContext());
 assertEquals(array, actual);
 }

 private JsonParser createMockJsonParser() {
 DefaultJsonParser parser = new DefaultJsonParser();
 return PowerMockito.mock(JsonParser.class, new ProxySettings().isProxy(true).isInterface(true))
         .withAnswer(invocation -> parser)
         .withMethods(new Class[] { JsonParser.class }, new Method[] { "nextToken", "nextValue", "getValueAsToken", "getTextValue", "getDecimalValue", "getBigIntegerValue", "getNumberType" })
         .usingGetifier(true);
 }
}