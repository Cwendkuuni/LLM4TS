package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TextNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TreeTraversingParserTest {

 private ObjectMapper mapper;
 private TreeTraversingParser parser;

 @Before
 public void setUp() {
 mapper = new ObjectMapper();
 }

 @Test
 public void testParseEmptyJson() throws JsonProcessingException {
 parser = mapper.treeAsTokens(new TextNode(""));
 Assert.assertTrue(parser.isClosed());
 }

 @Test
 public void testParseSimpleJsonObject() throws JsonProcessingException {
 JsonNode rootNode = mapper.readTree("{\"name\":\"John\",\"age\":30}");
 parser = new TreeTraversingParser(rootNode);

 // Basic functionality test
 Assert.assertEquals(JsonToken.START_OBJECT, parser.nextToken());
 Assert.assertEquals("name", parser.getCurrentName());
 Assert.assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
 Assert.assertEquals("John", parser.getText());
 Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
 Assert.assertEquals("age", parser.getCurrentName());
 Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
 Assert.assertEquals(30, parser.getIntValue());
 Assert.assertEquals(JsonToken.END_OBJECT, parser.nextToken());

 // Edge cases and exceptions tests
 Assert.assertNull(parser.nextToken()); // End of stream
 Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken()); // Missing value
 Assert.assertEquals(JsonToken.START_ARRAY, parser.nextToken()); // Unexpected array
 Assert.assertEquals(JsonToken.VALUE_NULL, parser.nextToken()); // Null value
 }

 @Test
 public void testParseSimpleJsonArray() throws JsonProcessingException {
 JsonNode rootNode = mapper.readTree("[1,2,3]");
 parser = new TreeTraversingParser(rootNode);

 // Basic functionality test
 Assert.assertEquals(JsonToken.START_ARRAY, parser.nextToken());
 Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
 Assert.assertEquals(1, parser.getIntValue());
 Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
 Assert.assertEquals(2, parser.getIntValue());
 Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
 Assert.assertEquals(3, parser.getIntValue());
 Assert.assertEquals(JsonToken.END_ARRAY, parser.nextToken());

 // Edge cases and exceptions tests
 Assert.assertEquals(JsonToken.END_ARRAY, parser.nextToken()); // Empty array
 Assert.assertEquals(JsonToken.START_OBJECT, parser.nextToken()); // Unexpected object
 Assert.assertEquals(JsonToken.VALUE_STRING, parser.nextToken()); // Unexpected string
 }

 @Test
 public void testParseComplexJson() throws JsonProcessingException {
 JsonNode rootNode = mapper.readTree("{\"name\":\"John\",\"age\":30,\"hobbies\":[\"reading\",\"swimming\",\"coding\"]}");
 parser = new TreeTraversingParser(rootNode);

 // Basic functionality test
 Assert.assertEquals(JsonToken.START_OBJECT, parser.nextToken());
 Assert.assertEquals("name", parser.getCurrentName());
 Assert.assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
 Assert.assertEquals("John", parser.getText());
 Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
 Assert.assertEquals("age", parser.getCurrentName());
 Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
 Assert.assertEquals(30, parser.getIntValue());
 Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
 Assert.assertEquals("hobbies", parser.getCurrentName());
 Assert.assertEquals(JsonToken.START_ARRAY, parser.nextToken());
 Assert.assertEquals("reading", parser.getText());
 Assert.assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
 Assert.assertEquals("swimming", parser.getText());
 Assert.assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
 Assert.assertEquals("coding", parser.getText());
 Assert.assertEquals(JsonToken.END_ARRAY, parser.nextToken());
 Assert.assertEquals(JsonToken.END_OBJECT, parser.nextToken());

 // Edge cases and exceptions tests
 Assert.assertEquals(JsonToken.END_OBJECT, parser.nextToken()); // Empty object
 Assert.assertEquals(JsonToken.START_ARRAY, parser.nextToken()); // Unexpected object
 Assert.assertEquals(JsonToken.VALUE_STRING, parser.nextToken()); // Unexpected number
 }

 @Test(expected = JsonParseException.class)
 public void testParseInvalidJson() throws JsonProcessingException {
 parser = mapper.treeAsTokens("{name:30}"); // Missing quotes around key
 Assert.assertEquals(JsonToken.START_OBJECT, parser.nextToken());
 parser.nextToken(); // name
 Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
 Assert.assertFalse(parser.isClosed());
 parser.nextToken(); // Should throw an exception
 }
}