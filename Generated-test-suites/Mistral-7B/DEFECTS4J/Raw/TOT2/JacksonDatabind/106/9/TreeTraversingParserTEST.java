package com.example.junit4;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.*;

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
 public void testEmptyJson() throws Exception {
 parser = mapper.treeAsTokensParser(JsonNode.NULL);
 assertEquals(JsonToken.END_DOCUMENT, parser.nextToken());
 }

 @Test
 public void testSimpleJsonObject() throws Exception {
 JsonNode json = mapper.readTree("{\"name\":\"John\",\"age\":30}");
 parser = new TreeTraversingParser(json);

 assertEquals(JsonToken.START_OBJECT, parser.nextToken());
 assertEquals("name", parser.getCurrentName());
 assertEquals("John", parser.getText());
 assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
 assertEquals("age", parser.getCurrentName());
 assertEquals("30", parser.getText());
 assertEquals(JsonToken.END_OBJECT, parser.nextToken());
 }

 @Test
 public void testSimpleJsonArray() throws Exception {
 JsonNode json = mapper.readTree("[1,2,3]");
 parser = new TreeTraversingParser(json);

 assertEquals(JsonToken.START_ARRAY, parser.nextToken());
 assertEquals(1, parser.getIntValue());
 assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
 assertEquals(2, parser.getIntValue());
 assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
 assertEquals(3, parser.getIntValue());
 assertEquals(JsonToken.END_ARRAY, parser.nextToken());
 }

 @Test
 public void testComplexJsonObject() throws Exception {
 JsonNode json = mapper.readTree("{\"name\":{\"first\":\"John\",\"last\":\"Doe\"},\"age\":30,\"hobbies\":[\"reading\",\"painting\"]}");
 parser = new TreeTraversingParser(json);

 assertEquals(JsonToken.START_OBJECT, parser.nextToken());
 assertEquals("name", parser.getCurrentName());
 assertEquals("name", parser.getText());
 assertEquals(JsonToken.START_OBJECT, parser.nextToken());
 assertEquals("first", parser.getCurrentName());
 assertEquals("John", parser.getText());
 assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
 assertEquals("last", parser.getCurrentName());
 assertEquals("Doe", parser.getText());
 assertEquals(JsonToken.END_OBJECT, parser.nextToken());
 assertEquals("age", parser.getCurrentName());
 assertEquals("age", parser.getText());
 assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
 assertEquals(30, parser.getIntValue());
 assertEquals(JsonToken.END_OBJECT, parser.nextToken());
 assertEquals("hobbies", parser.getCurrentName());
 assertEquals("hobbies", parser.getText());
 assertEquals(JsonToken.START_ARRAY, parser.nextToken());
 assertEquals("reading", parser.getText());
 assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
 assertEquals(1, parser.getIntValue());
 assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
 assertEquals("painting", parser.getText());
 assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
 assertEquals(JsonToken.END_ARRAY, parser.nextToken());
 assertEquals(JsonToken.END_OBJECT, parser.nextToken());
 }

 @Test(expected = IOException.class)
 public void testInvalidJson() throws Exception {
 JsonNode json = mapper.readTree("{name:30}"); // missing quotes
 parser = new TreeTraversingParser(json);
 parser.nextToken(); // will throw IOException
 }

 @Test(expected = JsonParseException.class)
 public void testNullNode() throws Exception {
 parser = mapper.treeAsTokensParser(null);
 parser.nextToken(); // will throw JsonParseException
 }

 @Test(expected = JsonParseException.class)
 public void testNullValue() throws Exception {
 JsonNode json = mapper.readTree("{\"name\":null}");
 parser = new TreeTraversingParser(json);
 parser.nextToken(); // will throw JsonParseException
 }

 @Test
 public void testSkipChildren() throws Exception {
 JsonNode json = mapper.readTree("{\"name\":{\"first\":\"John\",\"last\":\"Doe\"},\"age\":30,\"hobbies\":[\"reading\",\"painting\"]}");
 parser = new TreeTraversingParser(json);

 assertEquals(JsonToken.START_OBJECT, parser.nextToken());
 parser.skipChildren(); // skip name object
 assertEquals("age", parser.getCurrentName());
 assertEquals("age", parser.getText());
 assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
 assertEquals(30, parser.getIntValue());
 assertEquals(JsonToken.END_OBJECT, parser.nextToken());
 assertEquals("hobbies", parser.getCurrentName());
 assertEquals("hobbies", parser.getText());
 assertEquals(JsonToken.START_ARRAY, parser.nextToken());
 assertEquals("reading", parser.getText());
 assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
 assertEquals("painting", parser.getText());
 assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
 assertEquals(JsonToken.END_ARRAY, parser.nextToken());
 assertEquals(JsonToken.END_OBJECT, parser.nextToken());
 }

 @Test
 public void testCloseParser() throws Exception {
 JsonNode json = mapper.readTree("{\"name\":{\"first\":\"John\",\"last\":\"Doe\"},\"age\":30,\"hobbies\":[\"reading\",\"painting\"]}");
 parser = new TreeTraversingParser(json);

 assertEquals(JsonToken.START_OBJECT, parser.nextToken());
 assertEquals("name", parser.getCurrentName());
 assertEquals("name", parser.getText());
 assertEquals(JsonToken.START_OBJECT, parser.nextToken());
 assertEquals("first", parser.getCurrentName());
 assertEquals("John", parser.getText());
 assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
 assertEquals("last", parser.getCurrentName());
 assertEquals("Doe", parser.getText());
 assertEquals(JsonToken.END_OBJECT, parser.nextToken());
 assertEquals("age", parser.getCurrentName());
 assertEquals("age", parser.getText());
 assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
 assertEquals(30, parser.getIntValue());
 assertEquals(JsonToken.END_OBJECT, parser.nextToken());
 assertEquals("hobbies", parser.getCurrentName());
 assertEquals("hobbies", parser.getText());
 assertEquals(JsonToken.START_ARRAY, parser.nextToken());
 assertEquals("reading", parser.getText());
 assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
 assertEquals("painting", parser.getText());
 assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
 assertEquals(JsonToken.END_ARRAY, parser.nextToken());
 assertEquals(JsonToken.END_OBJECT, parser.nextToken());

 parser.close();

 assertEquals(JsonToken.END_DOCUMENT, parser.nextToken());
 }
}