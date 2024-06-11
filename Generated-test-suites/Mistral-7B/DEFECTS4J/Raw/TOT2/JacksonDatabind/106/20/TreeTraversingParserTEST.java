package com.example.junit;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Before;
import org.junit.Test;

public class TreeTraversingParserTest {

 private ObjectMapper mapper;
 private JsonNode rootNode;

 @Before
 public void setUp() {
 mapper = new ObjectMapper();
 try {
 rootNode = mapper.readTree("{\"name\": \"John\", \"age\": 30, \"city\": \"New York\"}");
 } catch (IOException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testNextToken_NormalUseCase() throws IOException {
 TreeTraversingParser parser = new TreeTraversingParser(rootNode);
 JsonToken token;

 token = parser.nextToken();
 assertEquals(JsonToken.START_OBJECT, token);

 token = parser.nextToken();
 assertEquals(JsonToken.FIELD_NAME, token);
 assertEquals("name", parser.getCurrentName());

 token = parser.nextToken();
 assertEquals(JsonToken.VALUE_STRING, token);
 assertEquals("John", parser.getText());

 token = parser.nextToken();
 assertEquals(JsonToken.FIELD_NAME, token);
 assertEquals("age", parser.getCurrentName());

 token = parser.nextToken();
 assertEquals(JsonToken.VALUE_NUMBER_INT, token);
 assertEquals(30, parser.getIntValue());

 token = parser.nextToken();
 assertEquals(JsonToken.FIELD_NAME, token);
 assertEquals("city", parser.getCurrentName());

 token = parser.nextToken();
 assertEquals(JsonToken.VALUE_STRING, token);
 assertEquals("New York", parser.getText());

 token = parser.nextToken();
 assertEquals(JsonToken.END_OBJECT, token);
 }

 @Test(expected = JsonParseException.class)
 public void testNextToken_NullNode() throws IOException {
 TreeTraversingParser parser = new TreeTraversingParser(null);
 parser.nextToken();
 }

 @Test(expected = JsonParseException.class)
 public void testNextToken_EmptyArray() throws IOException {
 TreeTraversingParser parser = new TreeTraversingParser(mapper.createArrayNode());
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();
 }

 @Test(expected = JsonParseException.class)
 public void testNextToken_EmptyObject() throws IOException {
 TreeTraversingParser parser = new TreeTraversingParser(mapper.createObjectNode());
 parser.nextToken();
 parser.nextToken();
 }

 @Test(expected = JsonParseException.class)
 public void testNextToken_NonTokenNode() throws IOException {
 TreeTraversingParser parser = new TreeTraversingParser(mapper.valueNode("Hello"));
 parser.nextToken();
 }

 @Test(expected = JsonParseException.class)
 public void testNextToken_EndOfStream() throws IOException {
 TreeTraversingParser parser = new TreeTraversingParser(mapper.readTree(""));
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();
 parser.nextToken();
 }
}