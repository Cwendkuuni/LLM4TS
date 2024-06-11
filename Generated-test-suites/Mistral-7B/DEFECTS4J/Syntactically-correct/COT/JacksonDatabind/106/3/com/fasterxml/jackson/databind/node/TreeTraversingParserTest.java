package com.example.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

import org.junit.Test;

public class TreeTraversingParserTest {

 private ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testConstructorWithJsonNode() throws IOException {
 TreeTraversingParser parser = new TreeTraversingParser(mapper.readTree("[]"));
 assertEquals(JsonToken.START_ARRAY, parser.nextToken());
 }

 @Test
 public void testConstructorWithJsonNodeAndCodec() throws IOException {
 ObjectNode objNode = mapper.createObjectNode();
 TreeTraversingParser parser = new TreeTraversingParser(objNode, mapper.getFactory());
 assertEquals(JsonToken.START_OBJECT, parser.nextToken());
 }

 @Test
 public void testSetCodec() throws IOException {
 ObjectNode objNode = mapper.createObjectNode();
 TreeTraversingParser parser = new TreeTraversingParser(objNode, mapper.getFactory());
 ObjectMapper newMapper = new ObjectMapper();
 parser.setCodec(newMapper.getFactory());
 assertEquals(newMapper.getFactory(), parser.getCodec());
 }

 @Test
 public void testVersion() throws IOException {
 TreeTraversingParser parser = new TreeTraversingParser(mapper.readTree("[]"));
 assertEquals(com.fasterxml.jackson.databind.cfg.PackageVersion.VERSION, parser.version());
 }

 @Test
 public void testClose() throws IOException {
 TreeTraversingParser parser = new TreeTraversingParser(mapper.readTree("[]"));
 parser.close();
 assertNull(parser.nextToken());
 }

 @Test
 public void testNextTokenWithStartContainer() throws IOException {
 TreeTraversingParser parser = new TreeTraversingParser(mapper.readTree("[]"));
 parser.nextToken(); // START_ARRAY
 parser.nextToken(); // END_ARRAY
 parser._startContainer = true;
 parser.nextToken(); // START_OBJECT
 assertTrue(parser._startContainer);
 parser.nextToken(); // END_OBJECT
 assertFalse(parser._startContainer);
 }

 @Test
 public void testSkipChildren() throws IOException {
 TreeTraversingParser parser = new TreeTraversingParser(mapper.readTree("[]"));
 parser.nextToken(); // START_ARRAY
 parser.skipChildren();
 assertEquals(JsonToken.END_ARRAY, parser.nextToken());
 }

 @Test
 public void testIsClosed() throws IOException {
 TreeTraversingParser parser = new TreeTraversingParser(mapper.readTree("[]"));
 assertFalse(parser.isClosed());
 parser.close();
 assertTrue(parser.isClosed());
 }

 @Test
 public void testCurrentName() throws IOException {
 ObjectNode objNode = mapper.createObjectNode();
 objNode.put("name", new TextNode("test"));
 TreeTraversingParser parser = new TreeTraversingParser(objNode, mapper.getFactory());
 assertEquals("name", parser.getCurrentName());
 }

 @Test
 public void testOverrideCurrentName() throws IOException {
 ObjectNode objNode = mapper.createObjectNode();
 objNode.put("name", new TextNode("test"));
 TreeTraversingParser parser = new TreeTraversingParser(objNode, mapper.getFactory());
 parser.overrideCurrentName("newName");
 assertEquals("newName", parser.getCurrentName());
 }

 @Test
 public void testGetParsingContext() throws IOException {
 TreeTraversingParser parser = new TreeTraversingParser(mapper.readTree("[]"));
 assertNotNull(parser.getParsingContext());
 }

 @Test
 public void testGetTokenLocation() throws IOException {
 TreeTraversingParser parser = new TreeTraversingParser(mapper.readTree("[]"));
 assertEquals(JsonLocation.NA, parser.getTokenLocation());
 }

 @Test
 public void testGetCurrentLocation() throws IOException {
 TreeTraversingParser parser = new TreeTraversingParser(mapper.readTree("[]"));
 assertEquals(JsonLocation.NA, parser.getCurrentLocation());
 }

 @Test
 public void testGetText() throws IOException {
 ObjectNode objNode = mapper.createObjectNode();
 objNode.put("name", new TextNode("test"));
 TreeTraversingParser parser = new TreeTraversingParser(objNode, mapper.getFactory());
 assertEquals("test", parser.getText());
 }

 @Test
 public void testGetTextCharacters() throws IOException {
 ObjectNode objNode = mapper.createObjectNode();
 objNode.put("name", new TextNode("test"));
 TreeTraversingParser parser = new TreeTraversingParser(objNode, mapper.getFactory());
 char[] chars = parser.getTextCharacters();
 assertEquals("test", new String(chars));
 }

 @Test
 public void testGetTextLength() throws IOException {
 ObjectNode objNode = mapper.createObjectNode();
 objNode.put("name", new TextNode("test"));
 TreeTraversingParser parser = new TreeTraversingParser(objNode, mapper.getFactory());
 assertEquals(4, parser.getTextLength());
 }

 @Test
 public void testGetTextOffset() throws IOException {
 ObjectNode objNode = mapper.createObjectNode();
 objNode.put("name", new TextNode("test"));
 TreeTraversingParser parser = new TreeTraversingParser(objNode, mapper.getFactory());
 assertEquals(0, parser.getTextOffset());
 }

 @Test
 public void testHasTextCharacters() throws IOException {
 ObjectNode objNode = mapper.createObjectNode();
 objNode.put("name", new TextNode("test"));
 TreeTraversingParser parser = new TreeTraversingParser(objNode, mapper.getFactory());
 assertFalse(parser.hasTextCharacters());
 }

 @Test
 public void testGetNumberType() throws IOException {
 ObjectNode objNode = mapper.createObjectNode();
 objNode.put("number", new NumberNode(123L));
 TreeTraversingParser parser = new TreeTraversingParser(objNode, mapper.getFactory());
 assertEquals(JsonNumberType.LONG, parser.getNumberType());
 }

 @Test
 public void testGetBigIntegerValue() throws IOException {
 ObjectNode objNode = mapper.createObjectNode();
 objNode.put("number", new NumberNode(BigInteger.valueOf(123)));
 TreeTraversingParser parser = new TreeTraversingParser(objNode, mapper.getFactory());
 assertEquals(BigInteger.valueOf(123), parser.getBigIntegerValue());
 }

 @Test
 public void testGetDecimalValue() throws IOException {
 ObjectNode objNode = mapper.createObjectNode();
 objNode.put("number", new NumberNode(new BigDecimal("123.45")));
 TreeTraversingParser parser = new TreeTraversingParser(objNode, mapper.getFactory());
 assertEquals(new BigDecimal("123.45"), parser.getDecimalValue());
 }

 @Test
 public void testGetDoubleValue() throws IOException {
 ObjectNode objNode = mapper.createObjectNode();
 objNode.put("number", new NumberNode(123.45));
 TreeTraversingParser parser = new TreeTraversingParser(objNode, mapper.getFactory());
 assertEquals(123.45, parser.getDoubleValue(), 0.001);
 }

 @Test
 public void testGetFloatValue() throws IOException {
 ObjectNode objNode = mapper.createObjectNode();
 objNode.put("number", new NumberNode(123.45f));
 TreeTraversingParser parser = new TreeTraversingParser(objNode, mapper.getFactory());
 assertEquals(123.45f, parser.getFloatValue(), 0.001f);
 }

 @Test
 public void testGetIntValue() throws IOException {
 ObjectNode objNode = mapper.createObjectNode();
 objNode.put("number", new NumberNode(123));
 TreeTraversingParser parser = new TreeTraversingParser(objNode, mapper.getFactory());
 assertEquals(123, parser.getIntValue());
 }

 @Test
 public void testGetLongValue() throws IOException {
 ObjectNode objNode = mapper.createObjectNode();
 objNode.put("number", new NumberNode(123L));
 TreeTraversingParser parser = new TreeTraversingParser(objNode, mapper.getFactory());
 assertEquals(123L, parser.getLongValue());
 }

 @Test
 public void testGetNumberValue() throws IOException {
 ObjectNode objNode = mapper.createObjectNode();
 objNode.put("number", new NumberNode(123));
 TreeTraversingParser parser = new TreeTraversingParser(objNode, mapper.getFactory());
 assertEquals(123, parser.getNumberValue().longValue());
 }

 @Test
 public void testGetEmbeddedObject() throws IOException {
 ObjectNode objNode = mapper.createObjectNode();
 objNode.put("obj", new ObjectNode());
 TreeTraversingParser parser = new TreeTraversingParser(objNode, mapper.getFactory());
 assertNotNull(parser.getEmbeddedObject());
 }

 @Test
 public void testIsNaN() throws IOException {
 ObjectNode objNode = mapper.createObjectNode();
 objNode.put("nan", new NumberNode(Double.NaN));
 TreeTraversingParser parser = new TreeTraversingParser(objNode, mapper.getFactory());
 assertTrue(parser.isNaN());
 }

 @Test
 public void testGetBinaryValue() throws IOException {
 ObjectNode objNode = mapper.createObjectNode();
 objNode.put("binary", new TextNode("VG8gZXJyIGlzIGh1bWFuLCB7CiAgICAtLSBQYXVsIFIuIEVocmxpY2g="));
 TreeTraversingParser parser = new TreeTraversingParser(objNode, mapper.getFactory());
 byte[] data = parser.getBinaryValue(Base64Variants.STANDARD);
 assertEquals("VG8gZXJyIGlzIGh1bWFuLCB7CiAgICAtLSBQYXVsIFIuIEVocmxpY2g=", new String(data, "UTF-8"));
 }

 @Test
 public void testReadBinaryValue() throws IOException {
 ObjectNode objNode = mapper.createObjectNode();
 objNode.put("binary", new TextNode("VG8gZXJyIGlzIGh1bWFuLCB7CiAgICAtLSBQYXVsIFIuIEVocmxpY2g="));
 TreeTraversingParser parser = new TreeTraversingParser(objNode, mapper.getFactory());
 byte[] data = new byte[parser.readBinaryValue(Base64Variants.STANDARD, new byte[1024])];
 assertEquals("VG8gZXJyIGlzIGh1bWFuLCB7CiAgICAtLSBQYXVsIFIuIEVocmxpY2g=", new String(data, "UTF-8"));
 }

 @Test
 public void testgetCurrentNode() throws IOException {
 ObjectNode objNode = mapper.createObjectNode();
 objNode.put("array", new ArrayNode(new TextNode("test"), new TextNode("test2")));
 TreeTraversingParser parser = new TreeTraversingParser(objNode, mapper.getFactory());
 parser.nextToken();
 parser.nextToken();
 JsonNode currentNode = parser.getCurrentNode();
 assertEquals(JsonToken.VALUE_STRING, currentNode.getTokenType());
 assertEquals("test", currentNode.textValue());
 }

 @Test(expected = JsonParseException.class)
 public void testgetCurrentNodeWithClosedParser() throws IOException {
 ObjectNode objNode = mapper.createObjectNode();
 TreeTraversingParser parser = new TreeTraversingParser(objNode, mapper.getFactory());
 parser.close();
 parser.nextToken();
 parser.getCurrentNode();
 }

 @Test
 public void testcurrentNumericNode() throws IOException {
 ObjectNode objNode = mapper.createObjectNode();
 objNode.put("int", new NumberNode(123));
 TreeTraversingParser parser = new TreeTraversingParser(objNode, mapper.getFactory());
 assertEquals(JsonToken.VALUE_NUMBER_INT, parser.getCurrentToken().getClass());
 }

 @Test(expected = JsonParseException.class)
 public void testcurrentNumericNodeWithClosedParser() throws IOException {
 ObjectNode objNode = mapper.createObjectNode();
 TreeTraversingParser parser = new TreeTraversingParser(objNode, mapper.getFactory());
 parser.close();
 parser.getCurrentNumericNode();
 }
}