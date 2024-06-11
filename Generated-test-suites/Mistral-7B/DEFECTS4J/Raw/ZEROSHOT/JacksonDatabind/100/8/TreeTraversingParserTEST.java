package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TreeTraversingParserTest {

 private JsonParser parser;
 private JsonNode node;

 @Before
 public void setUp() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 node = mapper.readTree("{'a': 1, 'b': ['c', 'd'], 'e': {'f': 2.5}}");
 parser = new TreeTraversingParser(node);
 }

 @After
 public void tearDown() throws Exception {
 parser.close();
 }

 @Test
 public void testNextToken_Root() throws IOException, JsonParseException {
 JsonToken token = parser.nextToken();
 assertEquals(JsonToken.START_OBJECT, token);

 token = parser.nextToken();
 assertEquals(JsonToken.FIELD_NAME, token);
 assertEquals("a", token.asString());

 token = parser.nextToken();
 assertEquals(JsonToken.VALUE_NUMBER_INT, token);
 assertEquals(1, token.intValue());

 token = parser.nextToken();
 assertEquals(JsonToken.FIELD_NAME, token);
 assertEquals("b", token.asString());

 token = parser.nextToken();
 assertEquals(JsonToken.START_ARRAY, token);

 token = parser.nextToken();
 assertEquals(JsonToken.VALUE_STRING, token);
 assertEquals("c", token.asString());

 token = parser.nextToken();
 assertEquals(JsonToken.VALUE_STRING, token);
 assertEquals("d", token.asString());

 token = parser.nextToken();
 assertEquals(JsonToken.END_ARRAY, token);

 token = parser.nextToken();
 assertEquals(JsonToken.FIELD_NAME, token);
 assertEquals("e", token.asString());

 token = parser.nextToken();
 assertEquals(JsonToken.START_OBJECT, token);

 token = parser.nextToken();
 assertEquals(JsonToken.FIELD_NAME, token);
 assertEquals("f", token.asString());

 token = parser.nextToken();
 assertEquals(JsonToken.VALUE_NUMBER_FLOAT, token);
 assertEquals(2.5, token.doubleValue(), 0.001);

 token = parser.nextToken();
 assertEquals(JsonToken.END_OBJECT, token);

 token = parser.nextToken();
 assertEquals(JsonToken.END_OBJECT, token);
 }

 @Test
 public void testNextToken_Array() throws IOException, JsonParseException {
 parser = new TreeTraversingParser(node.get("b"));
 JsonToken token = parser.nextToken();
 assertEquals(JsonToken.START_ARRAY, token);

 token = parser.nextToken();
 assertEquals(JsonToken.VALUE_STRING, token);
 assertEquals("c", token.asString());

 token = parser.nextToken();
 assertEquals(JsonToken.VALUE_STRING, token);
 assertEquals("d", token.asString());

 token = parser.nextToken();
 assertEquals(JsonToken.END_ARRAY, token);
 }

 @Test
 public void testSkipChildren() throws IOException, JsonParseException {
 parser = new TreeTraversingParser(node);
 JsonToken token = parser.nextToken();
 assertEquals(JsonToken.START_OBJECT, token);

 token = parser.nextToken();
 assertEquals(JsonToken.FIELD_NAME, token);
 assertEquals("a", token.asString());

 token = parser.nextToken();
 assertEquals(JsonToken.VALUE_NUMBER_INT, token);
 assertEquals(1, token.intValue());

 parser.skipChildren();

 token = parser.nextToken();
 assertEquals(JsonToken.FIELD_NAME, token);
 assertEquals("e", token.asString());

 token = parser.nextToken();
 assertEquals(JsonToken.START_OBJECT, token);

 token = parser.nextToken();
 assertEquals(JsonToken.FIELD_NAME, token);
 assertEquals("f", token.asString());

 token = parser.nextToken();
 assertEquals(JsonToken.VALUE_NUMBER_FLOAT, token);
 assertEquals(2.5, token.doubleValue(), 0.001);

 token = parser.nextToken();
 assertEquals(JsonToken.END_OBJECT, token);

 token = parser.nextToken();
 assertEquals(JsonToken.END_OBJECT, token);
 }

 @Test(expected = JsonParseException.class)
 public void testNextToken_Closed() throws IOException {
 JsonParser parser = new TreeTraversingParser(node);
 parser.close();
 parser.nextToken();
 }

 @Test
 public void testIsClosed() throws Exception {
 parser = new TreeTraversingParser(node);
 assertFalse(parser.isClosed());
 parser.close();
 assertTrue(parser.isClosed());
 }
}