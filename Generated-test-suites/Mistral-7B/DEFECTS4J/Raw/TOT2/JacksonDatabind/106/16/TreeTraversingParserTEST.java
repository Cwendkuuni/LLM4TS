package com.example.junit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

import org.junit.Before;
import org.junit.Test;

public class TreeTraversingParserTest {

 private JsonFactory jsonFactory;
 private ObjectMapper objectMapper;

 @Before
 public void setUp() {
 jsonFactory = new JsonFactory();
 objectMapper = new ObjectMapper(jsonFactory);
 }

 @Test
 public void testNewParserWithJsonNode() throws Exception {
 JsonNode jsonNode = objectMapper.readTree("{\"a\":1,\"b\":[1,2,3]}");
 TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

 // Basic functionality test for START_OBJECT token
 assertEquals(JsonToken.START_OBJECT, parser.nextToken());

 // Basic functionality test for FIELD_NAME token
 assertEquals("a", parser.getCurrentName());

 // Basic functionality test for VALUE_NUMBER_INT token
 assertEquals(1, parser.getIntValue());

 // Basic functionality test for START_ARRAY token
 assertEquals(JsonToken.START_ARRAY, parser.nextToken());

 // Basic functionality test for VALUE_NUMBER_INT token in an array
 assertEquals(1, parser.getIntValue());

 // Basic functionality test for END_ARRAY token
 assertEquals(JsonToken.END_ARRAY, parser.nextToken());

 // Basic functionality test for START_OBJECT token
 assertEquals(JsonToken.START_OBJECT, parser.nextToken());

 // Basic functionality test for FIELD_NAME token
 assertEquals("b", parser.getCurrentName());

 // Basic functionality test for START_ARRAY token
 assertEquals(JsonToken.START_ARRAY, parser.nextToken());

 // Basic functionality test for VALUE_NUMBER_INT token in an array
 assertEquals(1, parser.getIntValue());

 // Basic functionality test for VALUE_NUMBER_INT token in an array
 assertEquals(2, parser.getIntValue());

 // Basic functionality test for VALUE_NUMBER_INT token in an array
 assertEquals(3, parser.getIntValue());

 // Basic functionality test for END_ARRAY token
 assertEquals(JsonToken.END_ARRAY, parser.nextToken());

 // Basic functionality test for END_OBJECT token
 assertEquals(JsonToken.END_OBJECT, parser.nextToken());
 }

 @Test(expected = IOException.class)
 public void testNewParserWithNullJsonNodeThrowsIOException() throws Exception {
 TreeTraversingParser parser = new TreeTraversingParser(null);
 parser.nextToken(); // should throw IOException
 }

 @Test(expected = JsonParseException.class)
 public void testNewParserWithNonJsonNodeThrowsJsonParseException() throws Exception {
 JsonNode jsonNode = new ObjectNode(objectMapper.getValueFactory());
 jsonNode.put("a", new TextNode("not a number"));
 TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
 parser.nextToken(); // should throw JsonParseException
 }

 @Test
 public void testNextTokenAfterClosedParserReturnsNull() throws Exception {
 JsonNode jsonNode = objectMapper.readTree("{\"a\":1,\"b\":[1,2,3]}");
 TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
 parser.close();
 assertNull(parser.nextToken());
 }

 @Test
 public void testNextTokenAfterClosedParserReturnsEndMarker() throws Exception {
 JsonNode jsonNode = objectMapper.readTree("{\"a\":1,\"b\":[1,2,3]}");
 TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
 parser.close();
 parser.nextToken(); // should return END_OBJECT token
 parser.nextToken(); // should return END_ARRAY token
 assertNull(parser.nextToken());
 }

 @Test
 public void testSkipChildren() throws Exception {
 JsonNode jsonNode = objectMapper.readTree("{\"a\":[1,2,3],\"b\":[4,5,6]}");
 TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
 parser.nextToken(); // START_OBJECT
 parser.nextToken(); // FIELD_NAME: a
 parser.nextToken(); // START_ARRAY
 parser.nextToken(); // VALUE_NUMBER_INT: 1
 parser.nextToken(); // VALUE_NUMBER_INT: 2
 parser.nextToken(); // VALUE_NUMBER_INT: 3
 parser.nextToken(); // END_ARRAY
 parser.nextToken(); // FIELD_NAME: b
 parser.nextToken(); // START_ARRAY
 parser.skipChildren(); // skip the content of the arrays
 parser.nextToken(); // END_ARRAY
 parser.nextToken(); // END_OBJECT
 assertNull(parser.nextToken());
 }

 @Test
 public void testIsClosed() throws Exception {
 JsonNode jsonNode = objectMapper.readTree("{\"a\":1,\"b\":[1,2,3]}");
 TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
 assertFalse(parser.isClosed());
 parser.close();
 assertTrue(parser.isClosed());
 }
}