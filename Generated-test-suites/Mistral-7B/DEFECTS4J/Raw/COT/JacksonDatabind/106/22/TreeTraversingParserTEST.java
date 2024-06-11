package com.example.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Before;
import org.junit.Test;

public class TreeTraversingParserTest {

 private JsonNode testNode;
 private TreeTraversingParser parser;

 @Before
 public void setup() throws Exception {
 JsonFactory factory = new JsonFactory();
 ObjectMapper mapper = new ObjectMapper(factory);
 String jsonString = "{\"name\":\"John\",\"age\":30,\"hobbies\":[\"reading\",\"swimming\",\"sleeping\"]}";
 testNode = mapper.readTree(jsonString);
 }

 // Test for constructor with JsonNode
 @Test
 public void testTreeTraversingParserWithJsonNode() {
 parser = new TreeTraversingParser(testNode);
 assertEquals(JsonToken.START_OBJECT, parser.nextToken());
 }

 // Test for constructor with JsonNode and ObjectCodec
 @Test
 public void testTreeTraversingParserWithJsonNodeAndObjectCodec() {
 parser = new TreeTraversingParser(testNode, null);
 assertEquals(JsonToken.START_OBJECT, parser.nextToken());
 }

 // Test for setCodec method
 @Test
 public void testSetCodecMethod() {
 parser = new TreeTraversingParser(testNode);
 parser.setCodec(null);
 assertEquals(null, parser.getCodec());
 }

 // Test for getCodec method
 @Test
 public void testGetCodecMethod() {
 parser = new TreeTraversingParser(testNode);
 ObjectCodec codec = new ObjectMapper().getFactory().getCodec();
 parser.setCodec(codec);
 assertEquals(codec, parser.getCodec());
 }

 // Test for version method
 @Test
 public void testVersionMethod() {
 parser = new TreeTraversingParser(testNode);
 assertEquals(com.fasterxml.jackson.databind.cfg.PackageVersion.VERSION, parser.version());
 }

 // Test for close method
 @Test
 public void testCloseMethod() throws IOException {
 parser = new TreeTraversingParser(testNode);
 parser.close();
 assertNull(parser.getCodec());
 }

 // Test for nextToken method with no children
 @Test
 public void testNextTokenNoChildren() throws IOException {
 parser = new TreeTraversingParser(testNode);
 assertEquals(JsonToken.START_OBJECT, parser.nextToken());
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

 // Test for nextToken with children - array
 @Test
 public void testNextTokenWithArrayChildren() throws IOException {
 parser = new TreeTraversingParser(testNode);
 assertEquals(JsonToken.START_OBJECT, parser.nextToken());
 assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
 assertEquals("hobbies", parser.getCurrentName());
 assertEquals(JsonToken.START_ARRAY, parser.nextToken());
 JsonToken token = parser.nextToken();
 assertEquals(JsonToken.VALUE_STRING, token);
 assertEquals("reading", parser.getText());
 token = parser.nextToken();
 assertEquals(JsonToken.VALUE_STRING, token);
 assertEquals("swimming", parser.getText());
 token = parser.nextToken();
 assertEquals(JsonToken.VALUE_STRING, token);
 assertEquals("sleeping", parser.getText());
 assertEquals(JsonToken.END_ARRAY, parser.nextToken());
 assertEquals(JsonToken.END_OBJECT, parser.nextToken());
 }

 // Test for nextToken with children - object
 @Test
 public void testNextTokenWithObjectChildren() throws IOException {
 parser = new TreeTraversingParser(testNode);
 assertEquals(JsonToken.START_OBJECT, parser.nextToken());
 assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
 assertEquals("hobbies", parser.getCurrentName());
 assertEquals(JsonToken.START_OBJECT, parser.nextToken());
 JsonToken token = parser.nextToken();
 assertEquals(JsonToken.FIELD_NAME, token);
 assertEquals("reading", parser.getText());
 token = parser.nextToken();
 assertEquals(JsonToken.FIELD_NAME, token);
 assertEquals("type", parser.getCurrentName());
 assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
 assertEquals("hobby", parser.getText());
 token = parser.nextToken();
 assertEquals(JsonToken.END_OBJECT, token);
 assertEquals(JsonToken.END_OBJECT, parser.nextToken());
 }

 // Test for skipChildren method
 @Test
 public void testSkipChildren() throws IOException {
 parser = new TreeTraversingParser(testNode);
 assertEquals(JsonToken.START_OBJECT, parser.nextToken());
 assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
 assertEquals("hobbies", parser.getCurrentName());
 parser.skipChildren();
 assertEquals(JsonToken.END_OBJECT, parser.nextToken());
 }

 // Test for isClosed method
 @Test
 public void testIsClosedMethod() throws IOException {
 parser = new TreeTraversingParser(testNode);
 assertFalse(parser.isClosed());
 parser.close();
 assertTrue(parser.isClosed());
 }

 // Test for getText method with string value
 @Test
 public void testGetTextWithStringValue() throws IOException {
 parser = new TreeTraversingParser(testNode);
 assertEquals("John", parser.getText());
 }

 // Test for getText method with null value
 @Test (expected = JsonParseException.class)
 public void testGetTextWithNullValue() throws IOException {
 parser = new TreeTraversingParser(testNode);
 parser.nextToken(); // consume name field
 parser.nextToken(); // consume ':' and ':'
 parser.nextToken(); // consume 'null'
 parser.getText(); // should throw JsonParseException
 }

 // Test for getText method with number value
 @Test
 public void testGetTextWithNumberValue() throws IOException {
 parser = new TreeTraversingParser(testNode);
 assertEquals("30", parser.getText());
 }

 // Test for getText method with boolean value
 @Test
 public void testGetTextWithBooleanValue() throws IOException {
 parser = new TreeTraversingParser(testNode);
 assertEquals("true", parser.getText());
 }

 // Test for getText method with null token
 @Test (expected = JsonParseException.class)
 public void testGetTextWithNullToken() throws IOException {
 parser = new TreeTraversingParser(testNode);
 parser.nextToken(); // consume object start
 parser.nextToken(); // consume field name
 parser.nextToken(); // consume ':'
 parser.nextToken(); // consume null token
 parser.getText(); // should throw JsonParseException
 }

 // Test for getTextCharacters method
 @Test
 public void testGetTextCharactersMethod() throws IOException {
 parser = new TreeTraversingParser(testNode);
 char[] textChars = parser.getTextCharacters();
 assertEquals("John", new String(textChars));
 }

 // Test for getTextLength method
 @Test
 public void testGetTextLengthMethod() throws IOException {
 parser = new TreeTraversingParser(testNode);
 assertEquals(5, parser.getTextLength());
 }

 // Test for getTextOffset method
 @Test
 public void testGetTextOffsetMethod() throws IOException {
 parser = new TreeTraversingParser(testNode);
 assertEquals(0, parser.getTextOffset());
 }

 // Test for hasTextCharacters method
 @Test
 public void testHasTextCharactersMethod() throws IOException {
 parser = new TreeTraversingParser(testNode);
 assertFalse(parser.hasTextCharacters());
 }

 // Test for getNumberType method
 @Test
 public void testGetNumberTypeMethod() throws IOException {
 parser = new TreeTraversingParser(testNode);
 assertEquals(JsonType.NUMBER, parser.getNumberType());
 }

 // Test for getBigIntegerValue method
 @Test
 public void testGetBigIntegerValueMethod() throws IOException {
 parser = new TreeTraversingParser(testNode);
 assertEquals(BigInteger.valueOf(30), parser.getBigIntegerValue());
 }

 // Test for getDecimalValue method
 @Test
 public void testGetDecimalValueMethod() throws IOException {
 parser = new TreeTraversingParser(testNode);
 assertEquals(new BigDecimal("30.0"), parser.getDecimalValue());
 }

 // Test for getDoubleValue method
 @Test
 public void testGetDoubleValueMethod() throws IOException {
 parser = new TreeTraversingParser(testNode);
 assertEquals(30.0, parser.getDoubleValue(), 0.0);
 }

 // Test for getFloatValue method
 @Test
 public void testGetFloatValueMethod() throws IOException {
 parser = new TreeTraversingParser(testNode);
 assertEquals(30.0f, parser.getFloatValue(), 0.0f);
 }

 // Test for getIntValue method
 @Test
 public void testGetIntValueMethod() throws IOException {
 parser = new TreeTraversingParser(testNode);
 assertEquals(30, parser.getIntValue());
 }

 // Test for getLongValue method
 @Test
 public void testGetLongValueMethod() throws IOException {
 parser = new TreeTraversingParser(testNode);
 assertEquals(30L, parser.getLongValue());
 }

 // Test for getNumberValue method
 @Test
 public void testGetNumberValueMethod() throws IOException {
 parser = new TreeTraversingParser(testNode);
 assertEquals(30, parser.getNumberValue().intValue());
 }

 // Test for getEmbeddedObject method with Pojo node
 @Test
 public void testGetEmbeddedObjectWithPojoNode() throws IOException {
 parser = new TreeTraversingParser(testNode);
 assertEquals("John", parser.getEmbeddedObject().getName());
 }

 // Test for getEmbeddedObject method with Binary node
 @Test
 public void testGetEmbeddedObjectWithBinaryNode() throws IOException {
 parser = new TreeTraversingParser(testNode);
 byte[] binaryData = parser.getEmbeddedObject().getBinaryValue(Base64Variant.BASE64_URL);
 assertEquals(16, binaryData.length);
 }

 // Test for getBinaryValue method with Base64Variant
 @Test
 public void testGetBinaryValueWithBase64Variant() throws IOException {
 parser = new TreeTraversingParser(testNode);
 byte[] binaryData = parser.getBinaryValue(Base64Variant.BASE64_URL);
 assertArrayEquals(new byte[] { 72, 65, 63, 73, 65, 72, 73, 73, 77, 68, 72, 65, 76, 65, 72, 65, 65, 64 }, binaryData);
 }

 // Test for readBinaryValue method with Base64Variant
 @Test
 public void testReadBinaryValueWithBase64Variant() throws IOException {
 parser = new TreeTraversingParser(testNode);
 byte[] outputBuffer = new byte[17];
 int bytesRead = parser.readBinaryValue(Base64Variant.BASE64_URL, new ByteArrayOutputStream(outputBuffer));
 assertEquals(16, bytesRead);
 assertArrayEquals(new byte[] { 72, 65, 63, 73, 65, 72, 73, 73, 77, 68, 72, 65, 76, 65, 72, 65, 65 }, outputBuffer);
 }
}