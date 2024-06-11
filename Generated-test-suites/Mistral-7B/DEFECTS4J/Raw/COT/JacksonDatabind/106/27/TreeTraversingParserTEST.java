package com.example.junit;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;

public class TreeTraversingParserTest {

 private TreeTraversingParser parser;
 private ObjectCodec objectCodec;
 private JsonNode jsonNode;

 @Before
 public void setUp() throws Exception {
 objectCodec = new ObjectMapper().getFactory().createObjectCodec();
 jsonNode = JsonNodeFactory.instance.textNode("{\"name\":\"John\",\"age\":30,\"cities\":[\"New York\",\"Chicago\",\"Los Angeles\"]}");
 parser = new TreeTraversingParser(jsonNode, objectCodec);
 }

 @Test
 public void testConstructorWithJsonNode() {
 TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
 assertNotNull(parser);
 }

 @Test
 public void testConstructorWithJsonNodeAndObjectCodec() {
 TreeTraversingParser parser = new TreeTraversingParser(jsonNode, objectCodec);
 assertNotNull(parser);
 }

 @Test
 public void testSetCodec() {
 ObjectCodec newObjectCodec = new ObjectMapper().getFactory().createObjectCodec();
 parser.setCodec(newObjectCodec);
 assertEquals(newObjectCodec, parser.getCodec());
 }

 @Test
 public void testGetCodec() {
 assertEquals(objectCodec, parser.getCodec());
 }

 @Test
 public void testVersion() {
 Version version = parser.version();
 assertNotNull(version);
 }

 @Test
 public void testClose() throws IOException {
 parser.close();
 assertTrue(parser.isClosed());
 }

 @Test(expected = IOException.class)
 public void testNextTokenAfterClose() throws IOException {
 parser.close();
 parser.nextToken();
 fail("Expected IOException after closing parser");
 }

 @Test
 public void testNextTokenIsStartObject() throws IOException, JsonParseException {
 JsonToken nextToken = parser.nextToken();
 assertEquals(JsonToken.START_OBJECT, nextToken);
 }

 @Test
 public void testNextTokenIsEndObject() throws IOException, JsonParseException {
 parser.nextToken();
 parser.nextToken();
 JsonToken nextToken = parser.nextToken();
 assertEquals(JsonToken.END_OBJECT, nextToken);
 }

 @Test
 public void testNextTokenIsStartArray() throws IOException, JsonParseException {
 parser.nextToken();
 JsonToken nextToken = parser.nextToken();
 assertEquals(JsonToken.START_ARRAY, nextToken);
 }

 @Test
 public void testNextTokenIsEndArray() throws IOException, JsonParseException {
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();
 JsonToken nextToken = parser.nextToken();
 assertEquals(JsonToken.END_ARRAY, nextToken);
 }

 @Test
 public void testNextTokenIsValueString() throws IOException, JsonParseException {
 JsonToken nextToken = parser.nextToken();
 JsonToken valueToken = JsonToken.VALUE_STRING;
 assertEquals(valueToken, nextToken);
 }

 @Test
 public void testNextTokenIsValueNumberInt() throws IOException, JsonParseException {
 JsonToken nextToken = parser.nextToken();
 JsonToken valueToken = JsonToken.VALUE_NUMBER_INT;
 assertEquals(valueToken, nextToken);
 }

 @Test
 public void testNextTokenIsValueNumberFloat() throws IOException, JsonParseException {
 JsonToken nextToken = parser.nextToken();
 JsonToken valueToken = JsonToken.VALUE_NUMBER_FLOAT;
 assertEquals(valueToken, nextToken);
 }

 @Test
 public void testNextTokenIsFieldName() throws IOException, JsonParseException {
 JsonToken nextToken = parser.nextToken();
 JsonToken fieldNameToken = JsonToken.FIELD_NAME;
 assertEquals(fieldNameToken, nextToken);
 }

 @Test
 public void testSkipChildren() throws IOException, JsonParseException {
 parser.nextToken();
 parser.nextToken();
 parser.skipChildren();
 JsonToken nextToken = parser.nextToken();
 assertEquals(JsonToken.END_ARRAY, nextToken);
 }

 @Test
 public void testIsClosed() {
 assertFalse(parser.isClosed());
 }

 @Test
 public void testGetText() throws IOException, JsonParseException {
 String text = parser.getText();
 assertEquals("{\"name\":\"John\",\"age\":30,\"cities\":[\"New York\",\"Chicago\",\"Los Angeles\"]}", text);
 }

 @Test
 public void testGetTextCharacters() throws IOException, JsonParseException {
 char[] text = parser.getTextCharacters();
 assertEquals("{\"name\":\"John\",\"age\":30,\"cities\":[\"New York\",\"Chicago\",\"Los Angeles\"]}", new String(text));
 }

 @Test
 public void testGetTextLength() throws IOException, JsonParseException {
 int length = parser.getTextLength();
 assertEquals(41, length);
 }

 @Test
 public void testGetTextOffset() throws IOException, JsonParseException {
 int offset = parser.getTextOffset();
 assertEquals(0, offset);
 }

 @Test
 public void testHasTextCharacters() {
 // generally we do not have efficient access as char[], hence:
 assertFalse(parser.hasTextCharacters());
 }

 @Test
 public void testGetNumberType() throws IOException {
 NumberType numberType = parser.getNumberType();
 assertEquals(NumberType.FLOAT, numberType);
 }

 @Test
 public void testGetBigIntegerValue() throws IOException {
 BigInteger bigInteger = parser.getBigIntegerValue();
 assertEquals(new BigInteger("30"), bigInteger);
 }

 @Test
 public void testGetDecimalValue() throws IOException {
 BigDecimal decimal = parser.getDecimalValue();
 assertEquals(new BigDecimal("30.0"), decimal);
 }

 @Test
 public void testGetDoubleValue() throws IOException {
 double doubleValue = parser.getDoubleValue();
 assertEquals(30.0, doubleValue, 0.01);
 }

 @Test
 public void testGetFloatValue() throws IOException {
 float floatValue = parser.getFloatValue();
 assertEquals(30.0f, floatValue, 0.01f);
 }

 @Test
 public void testGetIntValue() throws IOException {
 int intValue = parser.getIntValue();
 assertEquals(30, intValue);
 }

 @Test
 public void testGetLongValue() throws IOException {
 long longValue = parser.getLongValue();
 assertEquals(30L, longValue);
 }

 @Test
 public void testGetNumberValue() throws IOException {
 Number numberValue = parser.getNumberValue();
 assertEquals(new Double(30.0), numberValue);
 }

 @Test
 public void testGetEmbeddedObject() {
 Object embeddedObject = parser.getEmbeddedObject();
 assertNull(embeddedObject);
 }

 @Test
 public void testIsNaN() {
 assertFalse(parser.isNaN());
 }

 @Test
 public void testGetBinaryValue() throws IOException, JsonParseException {
 byte[] binaryValue = parser.getBinaryValue(Base64Variant.STANDARD);
 assertTrue(binaryValue.length > 0);
 }

 @Test
 public void testReadBinaryValue() throws IOException, JsonParseException {
 byte[] binaryValue = parser.getBinaryValue(Base64Variant.STANDARD);
 int read = parser.readBinaryValue(Base64Variant.STANDARD, new ByteArrayOutputStream());
 assertEquals(binaryValue.length, read);
 }

 @Test(expected = JsonParseException.class)
 public void testGetBinaryValueWithoutBase64Variant() throws IOException, JsonParseException {
 parser.getBinaryValue(null);
 fail("Expected JsonParseException without Base64Variant");
 }

 @Test(expected = IOException.class)
 public void testReadBinaryValueWithoutOutputstream() throws IOException, JsonParseException {
 byte[] binaryValue = parser.getBinaryValue(Base64Variant.STANDARD);
 parser.readBinaryValue(Base64Variant.STANDARD);
 fail("Expected IOException without Outputstream");
 }

 @Test
 public void testGetCurrentName() throws IOException, JsonParseException {
 String currentName = parser.getCurrentName();
 assertEquals("name", currentName);
 }

 @Test(expected = JsonParseException.class)
 public void testOverrideCurrentNameWithNull() throws IOException, JsonParseException {
 parser.overrideCurrentName(null);
 fail("Expected JsonParseException with null");
 }

 @Test(expected = JsonParseException.class)
 public void testOverrideCurrentNameWithEmptyString() throws IOException, JsonParseException {
 parser.overrideCurrentName("");
 fail("Expected JsonParseException with empty string");
 }

 @Test
 public void testGetParsingContext() {
 assertEquals(jsonNode, parser.getParsingContext());
 }

 @Test
 public void testGetTokenLocation() {
 JsonLocation jsonLocation = parser.getTokenLocation();
 assertEquals(JsonLocation.NA, jsonLocation);
 }

 @Test
 public void testGetCurrentLocation() {
 JsonLocation jsonLocation = parser.getCurrentLocation();
 assertEquals(JsonLocation.NA, jsonLocation);
 }
}