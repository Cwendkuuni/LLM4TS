package com.fasterxml.jackson.databind.node; // package import for consistency

import static org.junit.Assert.*;
import static com.fasterxml.jackson.core.JsonToken.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Before;
import org.junit.Test;

public class TreeTraversingParserTest {

 private TreeTraversingParser parser;
 private ObjectMapper mapper;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 parser = mapper.treeAsTokens("[]");
 }

 // Test for empty array
 @Test
 public void testEmptyArray() throws Exception {
 assertEquals(START_ARRAY, parser.nextToken());
 assertEquals(END_ARRAY, parser.nextToken());
 }

 // Test for empty object
 @Test
 public void testEmptyObject() throws Exception {
 parser = mapper.treeAsTokens("{}");
 assertEquals(START_OBJECT, parser.nextToken());
 assertEquals(END_OBJECT, parser.nextToken());
 }

 // Test for simple string value
 @Test
 public void testSimpleStringValue() throws Exception {
 parser = mapper.treeAsTokens("{\"name\": \"John Doe\"}");
 assertEquals(FIELD_NAME, parser.nextToken());
 assertEquals("name", parser.getCurrentName());
 assertEquals(VALUE_STRING, parser.nextToken());
 assertEquals("John Doe", parser.getText());
 }

 // Test for simple number value
 @Test
 public void testSimpleNumberValue() throws Exception {
 parser = mapper.treeAsTokens("{123}");
 assertEquals(VALUE_NUMBER_INT, parser.nextToken());
 assertEquals(123, parser.getIntValue());
 }

 // Test for nested structure
 @Test
 public void testNestedStructure() throws Exception {
 parser = mapper.treeAsTokens("{\"name\": {\"first\": \"John\", \"last\": \"Doe\"}}");
 assertEquals(START_OBJECT, parser.nextToken());
 assertEquals(FIELD_NAME, parser.nextToken());
 assertEquals("name", parser.getCurrentName());
 assertEquals(START_OBJECT, parser.nextToken());
 assertEquals(FIELD_NAME, parser.nextToken());
 assertEquals("first", parser.getCurrentName());
 assertEquals(VALUE_STRING, parser.nextToken());
 assertEquals("John", parser.getText());
 assertEquals(FIELD_NAME, parser.nextToken());
 assertEquals("last", parser.getCurrentName());
 assertEquals(VALUE_STRING, parser.nextToken());
 assertEquals("Doe", parser.getText());
 assertEquals(END_OBJECT, parser.nextToken());
 assertEquals(END_OBJECT, parser.nextToken());
 }

 // Test for exception handling when end of input is reached
 @Test(expected = IOException.class)
 public void testEndOfInput() throws Exception {
 parser.nextToken();
 parser.nextToken(); // should not be possible
 }

 // Test for exception handling when empty array is encountered
 @Test(expected = JsonParseException.class)
 public void testEmptyArrayException() throws Exception {
 parser = mapper.treeAsTokens("[]");
 parser.nextToken();
 parser.nextValue(); // should throw an exception
 }

 // Test for exception handling when empty object is encountered
 @Test(expected = JsonParseException.class)
 public void testEmptyObjectException() throws Exception {
 parser = mapper.treeAsTokens("{}");
 parser.nextToken();
 parser.nextValue(); // should throw an exception
 }

 // Test for exception handling when non-valid JSON is encountered
 @Test(expected = JsonParseException.class)
 public void testInvalidJson() throws Exception {
 parser = mapper.treeAsTokens("{123}"); // missing colon
 parser.nextToken();
 parser.nextValue(); // should throw an exception
 }
}