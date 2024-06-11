package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson. core.Base64Variant;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser.NumberType;
import com.fasterxml.jackson.core.util.DefaultPreprendableCharArray;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.POJONode;
import com.fasterxml.jackson.databind.node.TextNode;

public class TreeTraversingParserTest {

 private JsonFactory jsonFactory;
 private TreeTraversingParser parser;
 private JsonNode testNode;

 @Before
 public void setUp() throws IOException {
 jsonFactory = new JsonFactory();
 testNode = jsonFactory.readValue("{\"a\":1,\"b\":[2,\"c\":3]}\"", JsonNode.class);
 parser = new TreeTraversingParser(testNode);
 }

 @After
 public void tearDown() throws IOException {
 parser.close();
 }

 @Test
 public void testVersion() throws IOException {
 assertEquals("2.11.4", parser.version().toString());
 }

 @Test
 public void testClose() throws IOException {
 parser.close();
 assertNull(parser._objectCodec);
 assertNull(parser._nodeCursor);
 assertNull(parser._currToken);
 }

 @Test
 public void testNextToken() throws IOException {
 assertEquals(JsonToken.START_OBJECT, parser.nextToken());
 assertEquals("a", parser.getCurrentName());
 assertEquals("a", parser.getText());
 assertEquals(NumberType.INT, parser.getNumberType());
 assertEquals(1, parser.getIntValue());

 assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
 assertEquals("b", parser.getCurrentName());

 assertEquals(JsonToken.START_ARRAY, parser.nextToken());
 assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
 assertEquals(2, parser.getIntValue());

 assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
 assertEquals("c", parser.getCurrentName());

 assertEquals(JsonToken.START_OBJECT, parser.nextToken());
 assertEquals(null, parser.getCurrentName());

 assertEquals(JsonToken.END_ARRAY, parser.nextToken());
 assertEquals(JsonToken.END_OBJECT, parser.nextToken());
 assertEquals(null, parser.getCurrentName());
 }

 @Test
 public void testSkipChildren() throws IOException {
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();

 parser.skipChildren();
 assertFalse(parser.isClosed());
 assertEquals(JsonToken.END_ARRAY, parser.nextToken());
 }

 @Test
 public void testIsClosed() throws IOException {
 parser.close();
 assertTrue(parser.isClosed());
 }

 @Test
 public void testHasTextCharacters() throws IOException {
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();

 assertFalse(parser.hasTextCharacters());
 }

 @Test
 public void testGetText() throws IOException {
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();

 assertEquals("c", parser.getText());
 }

 @Test
 public void testGetTextCharacters() throws IOException {
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();

 char[] chars = parser.getTextCharacters();
 assertEquals(3, chars.length);
 assertEquals('c', chars[0]);
 assertEquals('\u0000', chars[1]);
 assertEquals('\u0000', chars[2]);
 }

 @Test
 public void testGetTextLength() throws IOException {
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();

 assertEquals(3, parser.getTextLength());
 }

 @Test
 public void testGetTextOffset() throws IOException {
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();

 assertEquals(0, parser.getTextOffset());
 }

 @Test
 public void testGetNumberType() throws IOException {
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();

 assertEquals(NumberType.INT, parser.getNumberType());
 }

 @Test
 public void testGetBigIntegerValue() throws IOException {
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();

 BigInteger expected = new BigInteger("42");
 assertEquals(expected, parser.getBigIntegerValue());
 }

 @Test
 public void testGetDecimalValue() throws IOException {
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();

 BigDecimal expected = new BigDecimal("3.14159265358979323846");
 assertEquals(expected, parser.getDecimalValue());
 }

 @Test
 public void testGetDoubleValue() throws IOException {
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();

 assertEquals(3.0, parser.getDoubleValue(), 0.001);
 }

 @Test
 public void testGetFloatValue() throws IOException {
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();

 assertEquals(3.0f, parser.getFloatValue(), 0.001f);
 }

 @Test
 public void testGetIntValue() throws IOException {
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();

 assertEquals(42, parser.getIntValue());
 }

 @Test
 public void testGetLongValue() throws IOException {
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();

 assertEquals(42L, parser.getLongValue());
 }

 @Test
 public void testGetNumberValue() throws IOException {
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();

 assertEquals(new Long(42), parser.getNumberValue());
 }

 @Test
 public void testGetEmbeddedObject() throws IOException {
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();

 POJONode expected = new POJONode(new Object() {
 @Override
 public String toString() {
 return "EmbeddedObject";
 }
 });
 assertEquals(expected, parser.getEmbeddedObject());
 }

 @Test
 public void testIsNaN() throws IOException {
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();

 assertFalse(parser.isNaN());
 }

 @Test
 public void testGetBinaryValue() throws IOException {
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();

 byte[] expected = "test".getBytes();
 assertEquals(expected, parser.getBinaryValue(Base64Variant.DEFAULT));
 }

 @Test
 public void testGetBinaryValueWithWrongVariant() throws IOException {
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();

 assertNull(parser.getBinaryValue(Base64Variant.URL_SAFE));
 }

 @Test
 public void testReadBinaryValue() throws IOException {
 ByteArrayOutputStream output = new ByteArrayOutputStream();
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();

 int result = parser.readBinaryValue(Base64Variant.DEFAULT, output);
 assertEquals(5, result);
 assertEquals("test".getBytes(), output.toByteArray());
 }

 @Test
 public void testReadBinaryValueWithWrongVariant() throws IOException {
 ByteArrayOutputStream output = new ByteArrayOutputStream();
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();

 assertNull(parser.readBinaryValue(Base64Variant.URL_SAFE, output));
 }

 @Test
 public void testGetTextName() throws IOException {
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();

 assertEquals("c", parser.getTextName());
 }

 @Test
 public void testOverrideCurrentName() throws IOException {
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();

 parser.overrideCurrentName("override");
 assertEquals("override", parser.getCurrentName());
 }

 @Test
 public void testGetParsingContext() throws IOException {
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();

 assertEquals(testNode, parser.getParsingContext());
 }

 @Test
 public void testGetTokenLocation() throws IOException {
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();

 assertEquals(JsonLocation.NA, parser.getTokenLocation());
 }

 @Test
 public void testGetCurrentLocation() throws IOException {
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();

 assertEquals(JsonLocation.NA, parser.getCurrentLocation());
 }
}