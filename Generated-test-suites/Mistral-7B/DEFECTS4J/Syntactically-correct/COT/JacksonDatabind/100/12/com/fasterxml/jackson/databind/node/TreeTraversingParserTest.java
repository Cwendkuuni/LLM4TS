package com.example.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.util.ByteSource;

import com.fasterxml.jackson.databind.node.TreeTraversingParser;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

public class TreeTraversingParserTest {

 private JsonNode rootNode;
 private TreeTraversingParser parser;

 @Before
 public void setUp() throws IOException {
 JsonFactory jsonFactory = new JsonFactory();
 ObjectMapper objectMapper = new ObjectMapper();
 String jsonString = "{\"name\":\"John\",\"age\":30,\"hobbies\":[\"reading\",\"swimming\"]}";
 rootNode = jsonFactory.readTree(jsonString);
 }

 @Test
 public void testConstructorWithJsonNode() {
 parser = new TreeTraversingParser(rootNode);
 }

 @Test
 public void testConstructorWithJsonNodeAndObjectCodec() {
 ObjectNode objectCodec = JsonNodeFactory.instance.objectNode();
 parser = new TreeTraversingParser(rootNode, objectCodec);
 }

 @Test
 public void testSetCodec() {
 ObjectNode objectCodec = JsonNodeFactory.instance.objectNode();
 parser = new TreeTraversingParser(rootNode);
 parser.setCodec(objectCodec);
 assertEquals(objectCodec, parser.getCodec());
 }

 @Test
 public void testGetCodec() {
 ObjectNode objectCodec = JsonNodeFactory.instance.objectNode();
 parser = new TreeTraversingParser(rootNode, objectCodec);
 assertEquals(objectCodec, parser.getCodec());
 }

 @Test
 public void testVersion() {
 assertEquals(com.fasterxml.jackson.databind.cfg.PackageVersion.VERSION, parser.version());
 }

 @Test
 public void testClose() throws IOException {
 parser = new TreeTraversingParser(rootNode);
 parser.close();
 assertNull(parser._currToken);
 assertNull(parser._nodeCursor);
 }

 @Test(expected = IOException.class)
 public void testNextTokenWhenClosed() throws IOException {
 parser = new TreeTraversingParser(rootNode);
 parser.close();
 parser.nextToken();
 }

 @Test
 public void testNextToken() throws IOException, JsonParseException {
 parser = new TreeTraversingParser(rootNode);
 assertEquals(JsonToken.START_OBJECT, parser.nextToken());
 }

 @Test
 public void testNextTokenWithStartContainer() throws IOException, JsonParseException {
 parser = new TreeTraversingParser(rootNode);
 parser.nextToken(); // skip START_OBJECT
 parser._startContainer = true;
 assertEquals(JsonToken.START_ARRAY, parser.nextToken());
 parser._startContainer = false;
 assertEquals(JsonToken.END_ARRAY, parser.nextToken());
 }

 @Test
 public void testSkipChildren() throws IOException, JsonParseException {
 parser = new TreeTraversingParser(rootNode);
 parser.nextToken(); // skip START_OBJECT
 parser.skipChildren();
 assertEquals(JsonToken.END_OBJECT, parser.nextToken());
 }

 @Test
 public void testIsClosed() {
 parser = new TreeTraversingParser(rootNode);
 assertFalse(parser.isClosed());
 }

 @Test
 public void testIsClosedAfterClose() {
 parser = new TreeTraversingParser(rootNode);
 parser.close();
 assertTrue(parser.isClosed());
 }

 @Test
 public void testGetText() throws IOException, JsonParseException {
 parser = new TreeTraversingParser(rootNode);
 assertEquals("John", parser.getText());
 }

 @Test
 public void testGetTextForNullValue() throws IOException, JsonParseException {
 parser = new TreeTraversingParser(JsonNodeFactory.instance.nullNode());
 assertNull(parser.getText());
 }

 @Test
 public void testGetTextForNumber() throws IOException, JsonParseException {
 parser = new TreeTraversingParser(JsonNodeFactory.instance.numberNode(123));
 assertEquals("123", parser.getText());
 }

 @Test
 public void testGetTextForBinary() throws IOException, JsonParseException {
 byte[] binaryData = new byte[] { 72, 101, 108, 108, 111 };
 JsonNode binaryNode = JsonNodeFactory.instance.binaryNode(ByteSource.fromArray(binaryData));
 parser = new TreeTraversingParser(binaryNode);
 assertEquals("hr", parser.getText(JsonNode.MapValueType.TEXT_OR_ARRAY));
 }

 @Test
 public void testGetTextForBinaryWithBase64Variant() throws IOException, JsonParseException {
 byte[] binaryData = new byte[] { 72, 101, 108, 108, 111 };
 JsonNode binaryNode = JsonNodeFactory.instance.binaryNode(ByteSource.fromArray(binaryData));
 parser = new TreeTraversingParser(binaryNode);
 assertEquals("BinData", parser.getText(JsonNode.MapValueType.TEXT_OR_ARRAY, Base64Variants.STANDARD));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetTextForBinaryWithoutBase64Variant() throws IOException, JsonParseException {
 byte[] binaryData = new byte[] { 72, 101, 108, 108, 111 };
 JsonNode binaryNode = JsonNodeFactory.instance.binaryNode(ByteSource.fromArray(binaryData));
 parser = new TreeTraversingParser(binaryNode);
 parser.getText(JsonNode.MapValueType.TEXT_OR_ARRAY);
 }

 @Test
 public void testGetTextCharacters() throws IOException, JsonParseException {
 parser = new TreeTraversingParser(rootNode);
 char[] textCharacters = parser.getTextCharacters();
 assertEquals('J', textCharacters[0]);
 assertEquals('h', textCharacters[5]);
 }

 @Test
 public void testGetTextLength() throws IOException, JsonParseException {
 parser = new TreeTraversingParser(rootNode);
 assertEquals(5, parser.getTextLength());
 }

 @Test
 public void testGetTextOffset() throws IOException, JsonParseException {
 parser = new TreeTraversingParser(rootNode);
 assertEquals(0, parser.getTextOffset());
 }

 @Test
 public void testHasTextCharacters() {
 parser = new TreeTraversingParser(rootNode);
 assertFalse(parser.hasTextCharacters());
 }

 @Test
 public void testGetNumberType() throws IOException, JsonParseException {
 parser = new TreeTraversingParser(rootNode);
 assertEquals(JsonType.NUMBER, parser.getNumberType());
 }

 @Test
 public void testGetBigIntegerValue() throws IOException, JsonParseException {
 parser = new TreeTraversingParser(rootNode);
 assertEquals(new BigInteger("30"), parser.getBigIntegerValue());
 }

 @Test
 public void testGetDecimalValue() throws IOException, JsonParseException {
 parser = new TreeTraversingParser(rootNode);
 assertEquals(new BigDecimal("30.0"), parser.getDecimalValue());
 }

 @Test
 public void testGetDoubleValue() throws IOException, JsonParseException {
 parser = new TreeTraversingParser(rootNode);
 assertEquals(30.0, parser.getDoubleValue(), 0.0001);
 }

 @Test
 public void testGetFloatValue() throws IOException, JsonParseException {
 parser = new TreeTraversingParser(rootNode);
 assertEquals((float) 30.0, parser.getFloatValue(), 0.0001);
 }

 @Test
 public void testGetLongValue() throws IOException, JsonParseException {
 parser = new TreeTraversingParser(rootNode);
 assertEquals(30L, parser.getLongValue());
 }

 @Test
 public void testGetIntValue() throws IOException, JsonParseException {
 parser = new TreeTraversingParser(rootNode);
 assertEquals(30, parser.getIntValue());
 }

 @Test
 public void testGetNumberValue() throws IOException, JsonParseException {
 parser = new TreeTraversingParser(rootNode);
 assertEquals(new Number(30), parser.getNumberValue());
 }

 @Test
 public void testGetEmbeddedObject() throws IOException, JsonParseException {
 parser = new TreeTraversingParser(rootNode);
 ObjectNode embeddedObject = (ObjectNode) parser.getEmbeddedObject();
 assertEquals("John", embeddedObject.get("name").asText());
 assertEquals(30, embeddedObject.get("age").asInt());
 assertArrayEquals(new String[] { "reading", "swimming" }, ((ArrayNode) embeddedObject.get("hobbies")).toArray(new String[0]));
 }

 @Test
 public void testIsNaN() throws IOException, JsonParseException {
 parser = new TreeTraversingParser(JsonNodeFactory.instance.numberNode(Double.NaN));
 assertTrue(parser.isNaN());
 }

 @Test
 public void testGetBinaryValue() throws IOException, JsonParseException {
 byte[] binaryData = new byte[] { 72, 101, 108, 108, 111 };
 JsonNode binaryNode = JsonNodeFactory.instance.binaryNode(ByteSource.fromArray(binaryData));
 parser = new TreeTraversingParser(binaryNode);
 byte[] binaryValue = parser.getBinaryValue(null);
 assertArrayEquals(binaryData, binaryValue);
 }

 @Test
 public void testGetBinaryValueWithBase64Variant() throws IOException, JsonParseException {
 byte[] binaryData = new byte[] { 72, 101, 108, 108, 111 };
 JsonNode binaryNode = JsonNodeFactory.instance.binaryNode(ByteSource.fromArray(binaryData));
 parser = new TreeTraversingParser(binaryNode);
 byte[] binaryValue = parser.getBinaryValue(Base64Variants.STANDARD);
 assertArrayEquals(binaryData, binaryValue);
 }

 @Test
 public void testGetBinaryValueWithoutBase64Variant() throws IOException, JsonParseException {
 byte[] binaryData = new byte[] { 72, 101, 108, 108, 111 };
 JsonNode binaryNode = JsonNodeFactory.instance.binaryNode(ByteSource.fromArray(binaryData));
 parser = new TreeTraversingParser(binaryNode);
 try {
 parser.getBinaryValue(null);
 fail("Expected UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // expected
 }
 }

 @Test
 public void testReadBinaryValue() throws IOException, JsonParseException {
 byte[] binaryData = new byte[] { 72, 101, 108, 108, 111 };
 JsonNode binaryNode = JsonNodeFactory.instance.binaryNode(ByteSource.fromArray(binaryData));
 parser = new TreeTraversingParser(binaryNode);
 byte[] buffer = new byte[binaryData.length];
 parser.readBinaryValue(null, ByteSource.wrap(buffer));
 assertArrayEquals(binaryData, buffer);
 }

 @Test
 public void testReadBinaryValueWithBase64Variant() throws IOException, JsonParseException {
 byte[] binaryData = new byte[] { 72, 101, 108, 108, 111 };
 JsonNode binaryNode = JsonNodeFactory.instance.binaryNode(ByteSource.fromArray(binaryData));
 parser = new TreeTraversingParser(binaryNode);
 byte[] buffer = new byte[binaryData.length];
 parser.readBinaryValue(Base64Variants.STANDARD, ByteSource.wrap(buffer));
 assertArrayEquals(binaryData, buffer);
 }

 @Test
 public void testReadBinaryValueWithNullBuffer() throws IOException, JsonParseException {
 byte[] binaryData = new byte[] { 72, 101, 108, 108, 111 };
 JsonNode binaryNode = JsonNodeFactory.instance.binaryNode(ByteSource.fromArray(binaryData));
 parser = new TreeTraversingParser(binaryNode);
 parser.readBinaryValue(null, ByteSource.wrap(new byte[0]));
 }

 @Test(expected = IOException.class)
 public void testReadBinaryValueWithNegativeBufferLength() throws IOException, JsonParseException {
 byte[] binaryData = new byte[] { 72, 101, 108, 108, 111 };
 JsonNode binaryNode = JsonNodeFactory.instance.binaryNode(ByteSource.fromArray(binaryData));
 parser = new TreeTraversingParser(binaryNode);
 parser.readBinaryValue(null, ByteSource.wrap(new byte[5]));
 }

 @Test(expected = IOException.class)
 public void testReadBinaryValueWithTooSmallBufferLength() throws IOException, JsonParseException {
 byte[] binaryData = new byte[] { 72, 101, 108, 108, 111 };
 JsonNode binaryNode = JsonNodeFactory.instance.binaryNode(ByteSource.fromArray(binaryData));
 parser = new TreeTraversingParser(binaryNode);
 parser.readBinaryValue(null, ByteSource.wrap(new byte[binaryData.length - 1]));
 }
}