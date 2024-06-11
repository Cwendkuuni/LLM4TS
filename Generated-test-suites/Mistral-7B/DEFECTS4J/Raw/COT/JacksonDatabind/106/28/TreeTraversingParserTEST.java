package com.example.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectCodec;
import com.fasterxml.jackson.databind.node.TreeTraversingParser;

public class TreeTraversingParserTest {

 private ObjectCodec _objectCodec;
 private JsonNode _jsonNode;
 private TreeTraversingParser _parser;

 @Before
 public void setUp() {
 _objectCodec = null; // replace with valid ObjectCodec instance if needed
 _jsonNode = new Object() {
 @SuppressWarnings("unchecked")
 public JsonNode asJsonNode() {
 return (JsonNode) this;
 }
 }.asJsonNode();
 _parser = new TreeTraversingParser(_jsonNode, _objectCodec);
 }

 @Test
 public void testIsClosed_initialState() {
 assertFalse(_parser.isClosed());
 }

 @Test
 public void testIsClosed_afterClose() throws IOException {
 _parser.close();
 assertTrue(_parser.isClosed());
 }

 @Test
 public void testGetCodec() {
 assertEquals(_objectCodec, _parser.getCodec());
 }

 @Test
 public void testSetCodec() {
 ObjectCodec newCodec = new ObjectCodec() {}; // replace with valid ObjectCodec instance if needed
 _parser.setCodec(newCodec);
 assertEquals(newCodec, _parser.getCodec());
 }

 @Test
 public void testVersion() {
 // Expected version number can be obtained from the class file or project configuration
 assertEquals(com.fasterxml.jackson.databind.cfg.PackageVersion.VERSION, _parser.version());
 }

 @Test
 public void testNextToken_initialState() throws IOException, JsonParseException {
 assertNull(_parser.nextToken());
 }

 @Test
 public void testNextToken_startArray() throws IOException, JsonParseException {
 _parser = new TreeTraversingParser(new Object() {
 @SuppressWarnings("unchecked")
 public JsonNode asJsonNode() {
 return (JsonNode) this;
 }
 }.asJsonNode().get("array"));

 assertEquals(JsonToken.START_ARRAY, _parser.nextToken());
 }

 @Test
 public void testNextToken_startObject() throws IOException, JsonParseException {
 _parser = new TreeTraversingParser(new Object() {
 @SuppressWarnings("unchecked")
 public JsonNode asJsonNode() {
 return (JsonNode) this;
 }
 }.asJsonNode().get("object"));

 assertEquals(JsonToken.START_OBJECT, _parser.nextToken());
 }

 @Test
 public void testNextToken_valueNode() throws IOException, JsonParseException {
 _parser = new TreeTraversingParser(new Object() {
 @SuppressWarnings("unchecked")
 public JsonNode asJsonNode() {
 return (JsonNode) this;
 }
 }.asJsonNode().get("value"));

 assertNull(_parser.nextToken());
 }

 @Test
 public void testNextToken_endArray() throws IOException, JsonParseException {
 _parser = new TreeTraversingParser(new Object() {
 @SuppressWarnings("unchecked")
 public JsonNode asJsonNode() {
 return (JsonNode) this;
 }
 }.asJsonNode().get("array").get(0));

 assertEquals(JsonToken.END_ARRAY, _parser.nextToken());
 }

 @Test
 public void testNextToken_endObject() throws IOException, JsonParseException {
 _parser = new TreeTraversingParser(new Object() {
 @SuppressWarnings("unchecked")
 public JsonNode asJsonNode() {
 return (JsonNode) this;
 }
 }.asJsonNode().get("object").get("field"));

 assertEquals(JsonToken.END_OBJECT, _parser.nextToken());
 }

 @Test
 public void testNextToken_null() throws IOException, JsonParseException {
 _parser = new TreeTraversingParser(null);
 assertNull(_parser.nextToken());
 }

 @Test(expected = JsonParseException.class)
 public void testNextToken_invalidJson() throws IOException, JsonParseException {
 _parser = new TreeTraversingParser("invalidJson");
 _parser.nextToken();
 }

 @Test
 public void testSkipChildren() throws IOException, JsonParseException {
 _parser = new TreeTraversingParser(new Object() {
 @SuppressWarnings("unchecked")
 public JsonNode asJsonNode() {
 return (JsonNode) this;
 }
 }.asJsonNode().get("array"));
 _parser.skipChildren();
 assertEquals(JsonToken.END_ARRAY, _parser.nextToken());
 }

 @Test
 public void testGetText() throws IOException, JsonParseException {
 assertEquals("value", _parser.getText());
 _parser = new TreeTraversingParser(_jsonNode);
 _parser.nextToken();
 assertEquals("fieldName", _parser.getText());
 }

 @Test
 public void testGetTextCharacters() throws IOException, JsonParseException {
 _parser = new TreeTraversingParser(_jsonNode);
 _parser.nextToken();
 char[] textCharacters = _parser.getTextCharacters();
 assertEquals("fieldName", new String(textCharacters));
 }

 @Test
 public void testGetTextLength() throws IOException, JsonParseException {
 _parser = new TreeTraversingParser(_jsonNode);
 _parser.nextToken();
 assertEquals(7, _parser.getTextLength());
 }

 @Test
 public void testGetTextOffset() throws IOException, JsonParseException {
 _parser = new TreeTraversingParser(_jsonNode);
 _parser.nextToken();
 assertEquals(0, _parser.getTextOffset());
 }

 @Test
 public void testHasTextCharacters() throws IOException, JsonParseException {
 _parser = new TreeTraversingParser(_jsonNode);
 _parser.nextToken();
 assertFalse(_parser.hasTextCharacters());
 }

 @Test
 public void testGetNumberType() throws IOException, JsonParseException {
 _parser = new TreeTraversingParser(_jsonNode);
 _parser.nextToken();
 assertNull(_parser.getNumberType());
 }

 @Test
 public void testGetBigIntegerValue() throws IOException, JsonParseException {
 _parser = new TreeTraversingParser(_jsonNode);
 _parser.nextToken();
 _parser.nextToken(); // skip "fieldName" token
 assertEquals(new BigInteger("12345678901234567890"), _parser.getBigIntegerValue());
 }

 @Test
 public void testGetDecimalValue() throws IOException, JsonParseException {
 _parser = new TreeTraversingParser(_jsonNode);
 _parser.nextToken();
 _parser.nextToken(); // skip "fieldName" token
 assertEquals(new BigDecimal("123.45"), _parser.getDecimalValue());
 }

 @Test
 public void testGetDoubleValue() throws IOException, JsonParseException {
 _parser = new TreeTraversingParser(_jsonNode);
 _parser.nextToken();
 _parser.nextToken(); // skip "fieldName" token
 assertEquals(123.45, _parser.getDoubleValue(), 0.01);
 }

 @Test
 public void testGetFloatValue() throws IOException, JsonParseException {
 _parser = new TreeTraversingParser(_jsonNode);
 _parser.nextToken();
 _parser.nextToken(); // skip "fieldName" token
 assertEquals(123.45f, _parser.getFloatValue(), 0.01f);
 }

 @Test
 public void testGetIntValue() throws IOException, JsonParseException {
 _parser = new TreeTraversingParser(_jsonNode);
 _parser.nextToken();
 _parser.nextToken(); // skip "fieldName" token
 assertEquals(123, _parser.getIntValue());
 }

 @Test
 public void testGetLongValue() throws IOException, JsonParseException {
 _parser = new TreeTraversingParser(_jsonNode);
 _parser.nextToken();
 _parser.nextToken(); // skip "fieldName" token
 assertEquals(123456789012345L, _parser.getLongValue());
 }

 @Test
 public void testGetNumberValue() throws IOException, JsonParseException {
 _parser = new TreeTraversingParser(_jsonNode);
 _parser.nextToken();
 _parser.nextToken(); // skip "fieldName" token
 assertEquals(123.45, _parser.getNumberValue().doubleValue(), 0.01);
 }

 @Test
 public void testGetEmbeddedObject() throws IOException, JsonParseException {
 _parser = new TreeTraversingParser(_jsonNode);
 _parser.nextToken();
 _parser.nextToken(); // skip "fieldName" token
 JsonNode embeddedObject = _parser.getEmbeddedObject();
 assertSame(_jsonNode.get("object"), embeddedObject);
 }

 @Test
 public void testIsNaN() throws IOException, JsonParseException {
 _parser = new TreeTraversingParser(_jsonNode);
 _parser.nextToken();
 _parser.nextToken(); // skip "fieldName" token
 assertTrue(_parser.isNaN());
 }

 @Test
 public void testGetBinaryValue_base64() throws IOException, JsonParseException {
 _parser = new TreeTraversingParser(_jsonNode);
 _parser.nextToken();
 _parser.nextToken(); // skip "fieldName" token
 byte[] binaryValue = _parser.getBinaryValue(Base64Variant.BASE64_STANDARD);
 assertEquals("VG8gZXJyIGlzIGxpa2UgZW1icmVha2luZw==", new String(binaryValue));
 }

 @Test
 public void testReadBinaryValue_base64() throws IOException, JsonParseException {
 byte[] input = "VG8gZXJyIGlzIGxpa2UgZW1icmVha2luZw==".getBytes();
 OutputStream outputStream = new ByteArrayOutputStream();
 int bytesRead = _parser.readBinaryValue(Base64Variant.BASE64_STANDARD, outputStream);
 assertEquals(input.length, bytesRead);
 byte[] readBinaryValue = outputStream.toByteArray();
 assertEquals(input, readBinaryValue);
 }
}