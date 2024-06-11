package com.example.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TreeTraversingParser;

import org.junit.Test;

public class TreeTraversingParserTest {

 private ObjectMapper objectMapper = new ObjectMapper();

 @Test
 public void testTreeTraversingParserConstructionWithNull() {
 TreeTraversingParser parser = new TreeTraversingParser(null);
 assertNull(parser._objectCodec);
 assertNull(parser._nodeCursor);
 assertNull(parser._nextToken);
 assertNull(parser._startContainer);
 assertNull(parser._closed);
 }

 @Test
 public void testTreeTraversingParserConstructionWithJsonNode() {
 JsonNode jsonNode = objectMapper.readTree("{'name': 'John', 'age': 30}");
 TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
 assertEquals(JsonToken.START_OBJECT, parser.nextToken());
 assertEquals("name", parser.getCurrentName());
 }

 @Test
 public void testTreeTraversingParserConstructionWithJsonNodeAndCodec() {
 JsonNode jsonNode = objectMapper.readTree("{'name': 'John', 'age': 30}");
 TreeTraversingParser parser = new TreeTraversingParser(jsonNode, objectMapper.getFactory());
 assertEquals(JsonToken.START_OBJECT, parser.nextToken());
 assertEquals("name", parser.getCurrentName());
 }

 @Test
 public void testTreeTraversingParserSetCodec() {
 ObjectMapper newObjectMapper = new ObjectMapper();
 TreeTraversingParser parser = new TreeTraversingParser(objectMapper.readTree("{'name': 'John', 'age': 30}"));
 parser.setCodec(newObjectMapper);
 assertEquals(newObjectMapper, parser.getCodec());
 }

 @Test
 public void testTreeTraversingParserGetCodec() {
 TreeTraversingParser parser = new TreeTraversingParser(objectMapper.readTree("{'name': 'John', 'age': 30}"));
 assertEquals(objectMapper, parser.getCodec());
 }

 @Test
 public void testTreeTraversingParserVersion() {
 TreeTraversingParser parser = new TreeTraversingParser(objectMapper.readTree("{'name': 'John', 'age': 30}"));
 assertEquals(com.fasterxml.jackson.databind.cfg.PackageVersion.VERSION, parser.version());
 }

 @Test
 public void testTreeTraversingParserClose() throws IOException {
 TreeTraversingParser parser = new TreeTraversingParser(objectMapper.readTree("{'name': 'John', 'age': 30}"));
 parser.nextToken(); // consume start object token
 parser.close();
 assertEquals(JsonToken.END_OBJECT, parser.nextToken());
 }

 @Test
 public void testTreeTraversingParserNextToken() throws IOException, JsonParseException {
 TreeTraversingParser parser = new TreeTraversingParser(objectMapper.readTree("{'name': 'John', 'age': 30}"));
 parser.nextToken(); // consume start object token
 assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
 assertEquals("name", parser.getCurrentName());
 assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
 assertEquals("John", parser.getText());
 assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
 assertEquals("age", parser.getCurrentName());
 assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
 assertEquals(30, parser.getIntValue());
 assertEquals(JsonToken.END_OBJECT, parser.nextToken());
 }

 @Test
 public void testTreeTraversingParserSkipChildren() throws IOException, JsonParseException {
 TreeTraversingParser parser = new TreeTraversingParser(objectMapper.readTree("{'name': {'age': 30}, 'city': 'New York'}"));
 parser.nextToken(); // consume start object token
 parser.skipChildren();
 assertEquals(JsonToken.END_OBJECT, parser.nextToken());
 }

 @Test
 public void testTreeTraversingParserIsNull() {
 TreeTraversingParser parser = new TreeTraversingParser(null);
 assertNull(parser._objectCodec);
 assertNull(parser._nodeCursor);
 assertNull(parser._nextToken);
 assertNull(parser._startContainer);
 assertNull(parser._closed);
 }

 @Test
 public void testTreeTraversingParserIsClosed() throws IOException {
 TreeTraversingParser parser = new TreeTraversingParser(objectMapper.readTree("{'name': 'John', 'age': 30}"));
 parser.nextToken(); // consume start object token
 parser.close();
 assertTrue(parser.isClosed());
 }

 @Test(expected = JsonParseException.class)
 public void testTreeTraversingParserNextTokenAfterClose() throws IOException {
 TreeTraversingParser parser = new TreeTraversingParser(objectMapper.readTree("{'name': 'John', 'age': 30}"));
 parser.nextToken(); // consume start object token
 parser.close();
 parser.nextToken();
 }

 @Test
 public void testTreeTraversingParserGetText() throws IOException, JsonParseException {
 TreeTraversingParser parser = new TreeTraversingParser(objectMapper.readTree("{'name': 'John', 'age': 30}"));
 assertEquals("John", parser.getText());
 }

 @Test
 public void testTreeTraversingParserGetTextLength() throws IOException, JsonParseException {
 TreeTraversingParser parser = new TreeTraversingParser(objectMapper.readTree("{'name': 'John', 'age': 30}"));
 assertEquals(4, parser.getTextLength());
 }

 @Test
 public void testTreeTraversingParserGetTextCharacters() throws IOException, JsonParseException {
 TreeTraversingParser parser = new TreeTraversingParser(objectMapper.readTree("{'name': 'John', 'age': 30}"));
 char[] text = parser.getTextCharacters();
 assertEquals('J', text[0]);
 assertEquals('o', text[1]);
 assertEquals('h', text[2]);
 assertEquals('n', text[3]);
 }

 @Test
 public void testTreeTraversingParserGetTextOffset() throws IOException, JsonParseException {
 TreeTraversingParser parser = new TreeTraversingParser(objectMapper.readTree("{'name': 'John', 'age': 30}"));
 assertEquals(0, parser.getTextOffset());
 }

 @Test
 public void testTreeTraversingParserHasTextCharacters() throws IOException, JsonParseException {
 TreeTraversingParser parser = new TreeTraversingParser(objectMapper.readTree("{'name': 'John', 'age': 30}"));
 assertFalse(parser.hasTextCharacters());
 }

 @Test
 public void testTreeTraversingParserGetNumberType() throws IOException, JsonParseException {
 TreeTraversingParser parser = new TreeTraversingParser(objectMapper.readTree("{'name': 'John', 'age': 30.5}"));
 assertEquals(JsonType.NUMBER, parser.getNumberType());
 }

 @Test
 public void testTreeTraversingParserGetBigIntegerValue() throws IOException, JsonParseException {
 TreeTraversingParser parser = new TreeTraversingParser(objectMapper.readTree("{'name': 'John', 'age': 1234567890123456789012345678901234567890}"));
 assertEquals(new BigInteger("1234567890123456789012345678901234567890"), parser.getBigIntegerValue());
 }

 @Test
 public void testTreeTraversingParserGetDecimalValue() throws IOException, JsonParseException {
 TreeTraversingParser parser = new TreeTraversingParser(objectMapper.readTree("{'name': 'John', 'age': 30.5}"));
 assertEquals(new BigDecimal("30.5"), parser.getDecimalValue());
 }

 @Test
 public void testTreeTraversingParserGetDoubleValue() throws IOException, JsonParseException {
 TreeTraversingParser parser = new TreeTraversingParser(objectMapper.readTree("{'name': 'John', 'age': 30.5}"));
 assertEquals(30.5, parser.getDoubleValue(), 0.001);
 }

 @Test
 public void testTreeTraversingParserGetFloatValue() throws IOException, JsonParseException {
 TreeTraversingParser parser = new TreeTraversingParser(objectMapper.readTree("{'name': 'John', 'age': 30.5}"));
 assertEquals(30.5f, parser.getFloatValue(), 0.001f);
 }

 @Test
 public void testTreeTraversingParserGetLongValue() throws IOException, JsonParseException {
 TreeTraversingParser parser = new TreeTraversingParser(objectMapper.readTree("{'name': 'John', 'age': 1234567890}"));
 assertEquals(1234567890L, parser.getLongValue());
 }

 @Test
 public void testTreeTraversingParserGetIntValue() throws IOException, JsonParseException {
 TreeTraversingParser parser = new TreeTraversingParser(objectMapper.readTree("{'name': 'John', 'age': 1234567890}"));
 assertEquals(1234567890, parser.getIntValue());
 }

 @Test
 public void testTreeTraversingParserGetNumberValue() throws IOException, JsonParseException {
 TreeTraversingParser parser = new TreeTraversingParser(objectMapper.readTree("{'name': 'John', 'age': 1234567890}"));
 assertEquals(1234567890, parser.getNumberValue().longValue());
 }

 @Test
 public void testTreeTraversingParserGetEmbeddedObject() throws IOException, JsonParseException {
 TreeTraversingParser parser = new TreeTraversingParser(objectMapper.readTree("{'name': {'first': 'John', 'last': 'Doe'}, 'age': 30}"));
 assertEquals("John", parser.getEmbeddedObject().getClass().getDeclaredField("first").get(parser.getEmbeddedObject()));
 assertEquals("Doe", parser.getEmbeddedObject().getClass().getDeclaredField("last").get(parser.getEmbeddedObject()));
 }

 @Test
 public void testTreeTraversingParserIsNaN() throws IOException, JsonParseException {
 TreeTraversingParser parser = new TreeTraversingParser(objectMapper.readTree("{'name': 'John', 'age': NaN}"));
 assertTrue(parser.isNaN());
 }

 @Test
 public void testTreeTraversingParserGetBinaryValue() throws IOException, JsonParseException {
 TreeTraversingParser parser = new TreeTraversingParser(objectMapper.readTree("{'name': 'John', 'image': 'data:image/jpeg;base64,/9j/4...'}"));
 byte[] binaryValue = parser.getBinaryValue(Base64Variants.STANDARD);
 assertNotNull(binaryValue);
 }

 @Test
 public void testTreeTraversingParserReadBinaryValue() throws IOException, JsonParseException {
 TreeTraversingParser parser = new TreeTraversingParser(objectMapper.readTree("{'name': 'John', 'image': 'data:image/jpeg;base64,/9j/4...'}"));
 byte[] binaryValue = new byte[parser.getBinaryValue(Base64Variants.STANDARD).length];
 int bytesRead = parser.readBinaryValue(Base64Variants.STANDARD, new ByteArrayOutputStream(binaryValue.length));
 assertEquals(binaryValue.length, bytesRead);
 }
}