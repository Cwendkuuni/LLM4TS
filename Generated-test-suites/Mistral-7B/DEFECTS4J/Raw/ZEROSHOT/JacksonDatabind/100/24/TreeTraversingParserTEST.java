package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Before;
import org.junit.Test;

public class TreeTraversingParserTest {

 private ObjectMapper objectMapper;
 private JsonParser parser;
 private JsonNode node;

 @Before
 public void setup() throws Exception {
 objectMapper = new ObjectMapper();
 node = objectMapper.readTree("{\"name\":\"John\",\"age\":30,\"cities\":[\"New York\",\"Los Angeles\",\"Chicago\"]}");
 parser = new TreeTraversingParser(node);
 }

 @Test
 public void testNextTokenIsStartObject() throws Exception {
 JsonToken token = parser.nextToken();
 assertEquals(JsonToken.START_OBJECT, token);
 }

 @Test
 public void testNextTokenIsFieldName() throws Exception {
 JsonToken token = parser.nextToken();
 assertEquals(JsonToken.FIELD_NAME, token);
 assertEquals("name", token.asString());
 }

 @Test
 public void testNextTokenIsValueString() throws Exception {
 JsonToken token = parser.nextToken();
 assertEquals(JsonToken.VALUE_STRING, token);
 assertEquals("John", token.asString());
 }

 @Test
 public void testNextTokenIsValueNumberInt() throws Exception {
 JsonToken token = parser.nextToken();
 assertEquals(JsonToken.VALUE_NUMBER_INT, token);
 assertEquals(30, token.asInt());
 }

 @Test
 public void testNextTokenIsValueNumberFloat() throws Exception {
 JsonToken token = parser.nextToken();
 assertEquals(JsonToken.VALUE_NUMBER_FLOAT, token);
 assertEquals(30.0, token.asDouble(), 0.001);
 }

 @Test
 public void testNextTokenIsStartArray() throws Exception {
 JsonToken token = parser.nextToken();
 assertEquals(JsonToken.START_ARRAY, token);
 }

 @Test
 public void testNextTokenIsEndObject() throws Exception {
 JsonToken token = parser.nextToken();
 assertEquals(JsonToken.END_OBJECT, token);
 }

 @Test
 public void testNextTokenIsEndArray() throws Exception {
 JsonToken token = parser.nextToken();
 assertEquals(JsonToken.END_ARRAY, token);
 }

 @Test
 public void testSkipChildren() throws Exception {
 JsonToken token = parser.nextToken();
 assertEquals(JsonToken.START_ARRAY, token);
 parser.skipChildren();
 token = parser.nextToken();
 assertEquals(JsonToken.END_ARRAY, token);
 }

 @Test(expected = JsonParseException.class)
 public void testClosedParser() throws Exception {
 parser.close();
 parser.nextToken();
 }

 @Test
 public void testGetText() throws Exception {
 JsonToken token = parser.nextToken();
 assertEquals("John", parser.getText());
 token = parser.nextToken();
 assertEquals("age", parser.getText());
 token = parser.nextToken();
 assertEquals("30", parser.getText());
 token = parser.nextToken();
 assertEquals("name", parser.getText());
 token = parser.nextToken();
 assertEquals("[", parser.getText());
 token = parser.nextToken();
 assertEquals("New", parser.getText());
 token = parser.nextToken();
 assertEquals("York", parser.getText());
 token = parser.nextToken();
 assertEquals(",", parser.getText());
 token = parser.nextToken();
 assertEquals("Los", parser.getText());
 token = parser.nextToken();
 assertEquals("Angeles", parser.getText());
 token = parser.nextToken();
 assertEquals(",", parser.getText());
 token = parser.nextToken();
 assertEquals("Chicago", parser.getText());
 token = parser.nextToken();
 assertEquals("]", parser.getText());
 }

 @Test
 public void testGetTextCharacters() throws Exception {
 JsonToken token = parser.nextToken();
 char[] text = parser.getTextCharacters();
 assertEquals("John", new String(text, 0, text.length));
 token = parser.nextToken();
 char[] text2 = parser.getTextCharacters();
 assertEquals("age", new String(text2, 0, text2.length));
 }

 @Test
 public void testGetTextLength() throws Exception {
 JsonToken token = parser.nextToken();
 assertEquals(4, parser.getTextLength());
 token = parser.nextToken();
 assertEquals(3, parser.getTextLength());
 token = parser.nextToken();
 assertEquals(3, parser.getTextLength());
 token = parser.nextToken();
 assertEquals(5, parser.getTextLength());
 }

 @Test
 public void testIsClosed() throws Exception {
 JsonToken token = parser.nextToken();
 assertFalse(parser.isClosed());
 token = parser.nextToken();
 token = parser.nextToken();
 token = parser.nextToken();
 token = parser.nextToken();
 token = parser.nextToken();
 token = parser.nextToken();
 token = parser.nextToken();
 token = parser.nextToken();
 token = parser.nextToken();
 parser.close();
 assertTrue(parser.isClosed());
 token = parser.nextToken();
 assertNull(token);
 }
}