package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.core.base.ParserMinimalBase;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TreeTraversingParserTest {

 private TreeTraversingParser parser;
 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testBasicFunctionality_EmptyJsonNode() throws IOException {
 parser = new TreeTraversingParser(JsonNode.nullNode);
 Assert.assertNull(parser.nextToken());
 parser.close();
 }

 @Test
 public void testBasicFunctionality_StringValueJsonNode() throws IOException {
 JsonNode jsonNode = objectMapper.readTree("{\"name\": \"John\"}");
 parser = new TreeTraversingParser(jsonNode);
 Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
 Assert.assertEquals("name", parser.getCurrentName());
 Assert.assertEquals("John", parser.getText());
 parser.close();
 }

 @Test
 public void testBasicFunctionality_NumberValueJsonNode() throws IOException {
 JsonNode jsonNode = objectMapper.readTree("{123.45}");
 parser = new TreeTraversingParser(jsonNode);
 Assert.assertEquals(JsonToken.VALUE_NUMBER_FLOAT, parser.nextToken());
 Assert.assertEquals(123.45, parser.getDecimalValue().doubleValue(), 0.001);
 parser.close();
 }

 @Test
 public void testBasicFunctionality_ArrayValueJsonNode() throws IOException {
 JsonNode jsonNode = objectMapper.readTree("[1, 2, 3]");
 parser = new TreeTraversingParser(jsonNode);
 Assert.assertEquals(JsonToken.START_ARRAY, parser.nextToken());
 Assert.assertEquals(1, parser.getIntValue());
 Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
 Assert.assertEquals(2, parser.getIntValue());
 Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
 Assert.assertEquals(3, parser.getIntValue());
 Assert.assertEquals(JsonToken.END_ARRAY, parser.nextToken());
 parser.close();
 }

 @Test
 public void testBasicFunctionality_ObjectValueJsonNode() throws IOException {
 JsonNode jsonNode = objectMapper.readTree("{\"name\": \"John\", \"age\": 30}");
 parser = new TreeTraversingParser(jsonNode);
 Assert.assertEquals(JsonToken.START_OBJECT, parser.nextToken());
 Assert.assertEquals("name", parser.getCurrentName());
 Assert.assertEquals("John", parser.getText());
 Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
 Assert.assertEquals("age", parser.getCurrentName());
 Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
 Assert.assertEquals(30, parser.getIntValue());
 Assert.assertEquals(JsonToken.END_OBJECT, parser.nextToken());
 parser.close();
 }

 @Test(expected = IOException.class)
 public void testExceptionHandling_ClosedParser() throws IOException {
 parser = new TreeTraversingParser(JsonNode.nullNode);
 parser.close();
 parser.nextToken();
 }

 @Test(expected = JsonParseException.class)
 public void testExceptionHandling_NullJsonNode() throws IOException, JsonParseException {
 parser = new TreeTraversingParser(null);
 parser.nextToken();
 }

 @Test
 public void testExceptionHandling_EmptyStringJsonNode() throws IOException, JsonParseException {
 JsonNode jsonNode = JsonNode.value("");
 parser = new TreeTraversingParser(jsonNode);
 Assert.assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
 Assert.assertEquals("", parser.getText());
 parser.close();
 }

 @Test
 public void testExceptionHandling_NonJsonNode() throws IOException, JsonParseException {
 JsonNode jsonNode = JsonNode.value("{\"notJson\": true}");
 parser = new TreeTraversingParser(jsonNode);
 parser.nextToken();
 Assert.fail();
 }
}