package com.fasterxml.jackson.databind.node; // Import necessary classes here

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Before;
import org.junit.Test;

public class TreeTraversingParserTest {

 private JsonParser parser;
 private ObjectMapper mapper;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 parser = mapper.treeAsTokensParser("[]");
 }

 @Test
 public void testEmptyParser() throws Exception {
 JsonToken token = parser.nextToken();
 assertNull(token);
 }

 @Test
 public void testTokenTypes() throws Exception {
 parser = mapper.treeAsTokensParser("{ \"string\": \"Hello\", \"number\": 42, \"null\": null }");

 assertEquals(JsonToken.START_OBJECT, parser.nextToken());
 assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
 assertEquals("string", parser.getCurrentName());
 assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
 assertEquals("Hello", parser.getText());

 assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
 assertEquals("number", parser.getCurrentName());
 assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
 assertEquals(42, parser.getIntValue());

 assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
 assertEquals("null", parser.getCurrentName());
 assertEquals(JsonToken.VALUE_NULL, parser.nextToken());

 assertEquals(JsonToken.END_OBJECT, parser.nextToken());
 }

 @Test
 public void testArrayParser() throws Exception {
 parser = mapper.treeAsTokensParser("[1, 2, 3]");

 assertEquals(JsonToken.START_ARRAY, parser.nextToken());

 assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
 assertEquals(1, parser.getIntValue());

 assertEquals(JsonToken.COMMA, parser.nextToken());

 assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
 assertEquals(2, parser.getIntValue());

 assertEquals(JsonToken.COMMA, parser.nextToken());

 assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
 assertEquals(3, parser.getIntValue());

 assertEquals(JsonToken.END_ARRAY, parser.nextToken());
 }

 @Test(expected = IOException.class)
 public void testClosedParser() throws Exception {
 parser.close();
 parser.nextToken();
 }

 @Test(expected = JsonParseException.class)
 public void testParsingInvalidJson() throws Exception {
 parser = mapper.treeAsTokensParser("{"); // no end object

 assertEquals(JsonToken.START_OBJECT, parser.nextToken());

 assertEquals(JsonToken.FIELD_NAME, parser.nextToken());

 // exception should be thrown here
 parser.nextToken();
 }
}