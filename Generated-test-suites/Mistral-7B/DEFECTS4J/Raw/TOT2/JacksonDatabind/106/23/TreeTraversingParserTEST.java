package com.fasterxml.jackson.databind.node; // Import necessary packages here

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

import org.junit.Test;

public class TreeTraversingParserTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testBasicUsage() throws Exception {
 TreeTraversingParser parser = new TreeTraversingParser(mapper.readTree("[]"));
 JsonToken token = parser.nextToken();
 assertEquals(JsonToken.START_ARRAY, token);

 // Consume all array elements
 while (parser.nextToken().isStartArray()) {
 // Do something with each array element here
 }
 assertEquals(JsonToken.END_ARRAY, parser.nextToken());
 }

 @Test
 public void testBasicUsage_Object() throws Exception {
 TreeTraversingParser parser = new TreeTraversingParser(mapper.readTree("{}"));
 JsonToken token = parser.nextToken();
 assertEquals(JsonToken.START_OBJECT, token);

 // Consume all object fields
 while (parser.nextToken().isFieldName()) {
 // Do something with each field here
 }
 assertEquals(JsonToken.END_OBJECT, parser.nextToken());
 }

 @Test
 public void testBasicUsage_ValueNode() throws Exception {
 TreeTraversingParser parser = new TreeTraversingParser(mapper.readTree("{\"name\": \"John\"}"));
 JsonToken token = parser.nextToken();
 assertEquals(JsonToken.FIELD_NAME, token);
 assertEquals("name", parser.getCurrentName());
 token = parser.nextToken();
 assertEquals(JsonToken.VALUE_STRING, token);
 assertEquals("John", parser.getText());
 }

 @Test
 public void testEdgeCases_EmptyArray() throws Exception {
 TreeTraversingParser parser = new TreeTraversingParser(mapper.readTree("[]"));
 JsonToken token = parser.nextToken();
 assertEquals(JsonToken.START_ARRAY, token);
 // Consume empty array
 assertNull(parser.getText());
 assertEquals(JsonToken.END_ARRAY, parser.nextToken());
 }

 @Test
 public void testEdgeCases_EmptyObject() throws Exception {
 TreeTraversingParser parser = new TreeTraversingParser(mapper.readTree("{}"));
 JsonToken token = parser.nextToken();
 assertEquals(JsonToken.START_OBJECT, token);
 // Consume empty object
 assertNull(parser.getText());
 assertEquals(JsonToken.END_OBJECT, parser.nextToken());
 }

 @Test
 public void testEdgeCases_NullNode() throws Exception {
 TreeTraversingParser parser = new TreeTraversingParser(mapper.readTree(null));
 JsonToken token = parser.nextToken();
 assertNull(token);
 }

 @Test(expected = IOException.class)
 public void testExceptionHandling_ClosedParser() throws Exception {
 TreeTraversingParser parser = new TreeTraversingParser(mapper.readTree("[]"));
 parser.close();
 parser.nextToken();
 }

 @Test(expected = JsonParseException.class)
 public void testExceptionHandling_InvalidToken() throws Exception {
 TreeTraversingParser parser = new TreeTraversingParser(mapper.readTree("{}"));
 parser.nextToken();
 parser.nextToken();
 }

 @Test(expected = JsonParseException.class)
 public void testExceptionHandling_UnexpectedToken() throws Exception {
 TreeTraversingParser parser = new TreeTraversingParser(mapper.readTree("[]"));
 parser.nextToken();
 parser.nextToken();
 parser.nextToken(); // Expected END_ARRAY but encountered FIELD_NAME
 }
}