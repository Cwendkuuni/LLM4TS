package com.fasterxml.jackson.databind.node; // or the package where TreeTraversingParser is located

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TreeTraversingParserTest {

 private ObjectMapper mapper;
 private TreeTraversingParser parser;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 }

 @Test
 public void testEmptyArray() throws JsonParseException {
 JsonNode jsonNode = mapper.valueToTree("[{}]");
 parser = new TreeTraversingParser(jsonNode);

 Assert.assertEquals(JsonToken.START_ARRAY, parser.nextToken());
 Assert.assertEquals(JsonToken.START_OBJECT, parser.nextToken());
 Assert.assertEquals(JsonToken.END_OBJECT, parser.nextToken());
 Assert.assertEquals(JsonToken.END_ARRAY, parser.nextToken());
 Assert.assertTrue(parser.isClosed());
 }

 @Test
 public void testEmptyObject() throws JsonParseException {
 JsonNode jsonNode = mapper.valueToTree("{}}");
 parser = new TreeTraversingParser(jsonNode);

 Assert.assertEquals(JsonToken.START_OBJECT, parser.nextToken());
 Assert.assertEquals(JsonToken.END_OBJECT, parser.nextToken());
 Assert.assertTrue(parser.isClosed());
 }

 @Test
 public void testSimpleValue() throws JsonParseException {
 JsonNode jsonNode = mapper.valueToTree("123");
 parser = new TreeTraversingParser(jsonNode);

 Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
 Assert.assertEquals(123, parser.getIntValue());
 Assert.assertEquals("123", parser.getText());
 Assert.assertTrue(parser.isClosed());
 }

 @Test
 public void testStringValue() throws JsonParseException {
 JsonNode jsonNode = mapper.valueToTree("\"Hello, World!\"");
 parser = new TreeTraversingParser(jsonNode);

 Assert.assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
 Assert.assertEquals("Hello, World!", parser.getText());
 Assert.assertEquals("Hello, World!", parser.getTextCharacters());
 Assert.assertEquals(13, parser.getTextLength());
 Assert.assertEquals(0, parser.getTextOffset());
 Assert.assertTrue(parser.hasTextCharacters());
 Assert.assertTrue(parser.isClosed());
 }

 @Test
 public void testArrayWithNestedObjects() throws JsonParseException {
 JsonNode jsonNode = mapper.valueToTree("[{ \"name\": \"John\", \"age\": 30 }, { \"name\": \"Jane\", \"age\": 25 }]");
 parser = new TreeTraversingParser(jsonNode);

 Assert.assertEquals(JsonToken.START_ARRAY, parser.nextToken());

 // First object
 Assert.assertEquals(JsonToken.START_OBJECT, parser.nextToken());
 Assert.assertEquals("name", parser.getCurrentName());
 Assert.assertEquals("John", parser.getText());
 Assert.assertEquals(JsonToken.NAME, parser.nextToken());
 Assert.assertEquals("age", parser.getCurrentName());
 Assert.assertEquals(30, parser.getIntValue());
 Assert.assertEquals(JsonToken.END_OBJECT, parser.nextToken());

 // Second object
 Assert.assertEquals(JsonToken.START_OBJECT, parser.nextToken());
 Assert.assertEquals("name", parser.getCurrentName());
 Assert.assertEquals("Jane", parser.getText());
 Assert.assertEquals(JsonToken.NAME, parser.nextToken());
 Assert.assertEquals("age", parser.getCurrentName());
 Assert.assertEquals(25, parser.getIntValue());
 Assert.assertEquals(JsonToken.END_OBJECT, parser.nextToken());

 Assert.assertEquals(JsonToken.END_ARRAY, parser.nextToken());
 Assert.assertTrue(parser.isClosed());
 }

 @Test(expected = JsonParseException.class)
 public void testMissingRootNode() throws JsonParseException {
 parser = new TreeTraversingParser(null);
 parser.nextToken(); // should throw exception
 }

 @Test(expected = JsonParseException.class)
 public void testMalformedJson() throws JsonParseException {
 JsonNode jsonNode = mapper.valueToTree("{<invalid>");
 parser = new TreeTraversingParser(jsonNode);
 parser.nextToken(); // should throw exception
 }
}