package com.example.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.jsontype.ClassIdTypeResolver;
import com.fasterxml.jackson.databind.node.POJONode;
import com.fasterxml.jackson.databind.node.TextNode;

public class TreeTraversingParserTest {

 private TreeTraversingParser parser;
 private JsonNode jsonNode;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 // Create a sample JsonNode for testing
 JsonFactory jsonFactory = new JsonFactory();
 jsonNode = jsonFactory.createObjectNode();
 jsonNode.put("name", new TextNode("John Doe"));
 jsonNode.put("age", new NumberNode(30));
 jsonNode.put("isMarried", new BooleanNode(true));
 jsonNode.put("hobbies", new ArrayNode(new TextNode("Reading"), new TextNode("Swimming")));
 }

 @Test
 public void testConstructor() {
 parser = new TreeTraversingParser(jsonNode);
 assertEquals(JsonToken.START_OBJECT, parser.nextToken());
 }

 @Test
 public void testConstructorWithCodec() {
 parser = new TreeTraversingParser(jsonNode, new ObjectMapper().getFactory());
 assertEquals(JsonToken.START_OBJECT, parser.nextToken());
 }

 @Test
 public void testSetCodec() {
 ObjectMapper mapper = new ObjectMapper();
 parser = new TreeTraversingParser(jsonNode, mapper.getFactory());
 ClassIdTypeResolver typeResolver = mapper.getTypeResolver();
 parser.setCodec(mapper);
 assertEquals(mapper, parser.getCodec());
 }

 @Test
 public void testVersion() {
 assertEquals(com.fasterxml.jackson.databind.cfg.PackageVersion.VERSION, parser.version());
 }

 @Test
 public void testClose() throws IOException {
 parser.close();
 assertFalse(parser.isOpen());
 }

 @Test
 public void testNextToken() throws IOException, JsonParseException {
 // Test next token for each node type
 assertEquals(JsonToken.START_OBJECT, parser.nextToken());
 assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
 assertEquals("name", parser.getCurrentName());
 assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
 assertEquals("John Doe", parser.getText());
 assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
 assertEquals("age", parser.getCurrentName());
 assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
 assertEquals(30, parser.getIntValue());
 assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
 assertEquals("isMarried", parser.getCurrentName());
 assertEquals(JsonToken.VALUE_BOOLEAN, parser.nextToken());
 assertEquals(true, parser.getBooleanValue());
 assertEquals(JsonToken.END_OBJECT, parser.nextToken());
 }

 @Test
 public void testSkipChildren() throws IOException, JsonParseException {
 assertEquals(JsonToken.START_OBJECT, parser.nextToken());
 parser.skipChildren();
 assertEquals(JsonToken.END_OBJECT, parser.nextToken());
 }

 @Test
 public void testIsClosed() {
 assertTrue(parser.isClosed());
 parser.close();
 assertTrue(parser.isClosed());
 }

 @Test
 public void testGetCurrentName() throws IOException, JsonParseException {
 assertEquals("name", parser.getCurrentName());
 assertEquals("age", parser.getCurrentName());
 assertEquals("isMarried", parser.getCurrentName());
 }

 @Test
 public void testOverrideCurrentName() throws IOException, JsonParseException {
 parser.overrideCurrentName("testName");
 assertEquals("testName", parser.getCurrentName());
 }

 @Test
 public void testGetParsingContext() {
 assertEquals(jsonNode, parser.getParsingContext());
 }

 @Test
 public void testGetTokenLocation() {
 assertEquals(JsonLocation.NA, parser.getTokenLocation());
 }

 @Test
 public void testGetCurrentLocation() {
 assertEquals(JsonLocation.NA, parser.getCurrentLocation());
 }

 @Test
 public void testgetText() throws IOException, JsonParseException {
 assertEquals("John Doe", parser.getText());
 }

 @Test
 public void testgetTextCharacters() throws IOException, JsonParseException {
 char[] text = parser.getTextCharacters();
 assertEquals(11, text.length);
 }

 @Test
 public void testgetTextLength() throws IOException, JsonParseException {
 assertEquals(11, parser.getTextLength());
 }

 @Test
 public void testgetTextOffset() throws IOException, JsonParseException {
 assertEquals(0, parser.getTextOffset());
 }

 @Test
 public void testHasTextCharacters() {
 assertFalse(parser.hasTextCharacters());
 }

 @Test
 public void testGetNumberType() throws IOException, JsonParseException {
 assertEquals(JsonType.NUMBER_INT, parser.getNumberType());
 }

 @Test
 public void testGetBigIntegerValue() throws IOException, JsonParseException {
 assertEquals(new BigInteger("30"), parser.getBigIntegerValue());
 }

 @Test
 public void testGetDecimalValue() throws IOException, JsonParseException {
 assertEquals(new BigDecimal("30"), parser.getDecimalValue());
 }

 @Test
 public void testGetDoubleValue() throws IOException, JsonParseException {
 assertEquals(30.0, parser.getDoubleValue(), 0.0);
 }

 @Test
 public void testGetFloatValue() throws IOException, JsonParseException {
 assertEquals(30.0f, parser.getFloatValue(), 0.0f);
 }

 @Test
 public void testGetLongValue() throws IOException, JsonParseException {
 assertEquals(30, parser.getLongValue());
 }

 @Test
 public void testGetIntValue() throws IOException, JsonParseException {
 assertEquals(30, parser.getIntValue());
 }

 @Test
 public void testGetNumberValue() throws IOException, JsonParseException {
 assertEquals(new BigInteger("30"), parser.getNumberValue());
 }

 @Test
 public void testGetEmbeddedObject() {
 POJONode pojoNode = (POJONode) parser.getEmbeddedObject();
 assertNotNull(pojoNode);
 assertEquals("John Doe", pojoNode.getPojo().getName());
 assertEquals(30, pojoNode.getPojo().getAge());
 assertTrue(pojoNode.getPojo().isMarried());
 }

 @Test
 public void testIsNaN() throws IOException, JsonParseException {
 assertFalse(parser.isNaN());
 }

 @Test
 public void testGetBinaryValue() throws IOException, JsonParseException {
 byte[] binaryData = parser.getBinaryValue(Base64Variant.DEFAULT);
 assertArrayEquals(new byte[]{1, 2, 3}, binaryData);
 }

 @Test
 public void testReadBinaryValue() throws IOException, JsonParseException {
 byte[] binaryData = new byte[]{1, 2, 3};
 OutputStream outputStream = new ByteArrayOutputStream();
 int length = parser.readBinaryValue(Base64Variant.DEFAULT, outputStream);
 assertEquals(3, length);
 byte[] readBinaryData = outputStream.toByteArray();
 assertArrayEquals(binaryData, readBinaryData);
 }

 @Test(expected = JsonParseException.class)
 public void testGetBinaryValueWithNullNode() throws IOException {
 parser.nextToken();
 parser.getBinaryValue(Base64Variant.DEFAULT);
 }

 @Test(expected = JsonParseException.class)
 public void testGetBinaryValueWithNullVariant() throws IOException {
 parser.nextToken();
 parser.getBinaryValue(null);
 }

 @Test
 public void testNextValue() throws IOException, JsonParseException {
 // not implemented in TreeTraversingParser
 }
}