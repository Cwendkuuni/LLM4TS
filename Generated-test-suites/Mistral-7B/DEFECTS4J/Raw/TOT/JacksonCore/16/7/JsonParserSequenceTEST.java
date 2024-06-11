package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JsonNode;

public class JsonParserSequenceTest {

 // Test constructor with single parser
 @Test
 public void testSingleParser() throws IOException {
 JsonParser parser = new JsonFactory().createParser(new StringReader("{\"name\": \"John\"}"));
 JsonParserSequence sequence = new JsonParserSequence(new JsonParser[] { parser });
 JsonToken token = sequence.nextToken();
 assertEquals(JsonToken.START_OBJECT, token.getId());
 // Consume all tokens in the sequence
 while (token != null) {
 token = sequence.nextToken();
 }
 }

 // Test constructor with multiple parsers
 @Test
 public void testMultipleParsers() throws IOException {
 JsonParser parser1 = new JsonFactory().createParser(new StringReader("{\"name\": \"John\"}"));
 JsonParser parser2 = new JsonFactory().createParser(new StringReader("{\"age\": 30}"));
 JsonParserSequence sequence = JsonParserSequence.createFlattened(parser1, parser2);
 // Both parsers should be consumed in sequence
 JsonToken token = sequence.nextToken();
 assertEquals(JsonToken.START_OBJECT, token.getId(), "First parser should return START_OBJECT");
 token = sequence.nextToken();
 assertEquals(JsonToken.FIELD_NAME, token.getId(), "First parser should return FIELD_NAME");
 assertEquals("name", token.getText());
 token = sequence.nextToken();
 assertEquals(JsonToken.VALUE_STRING, token.getId(), "First parser should return VALUE_STRING");
 assertEquals("John", token.getText());
 // Consume the rest of the tokens from the first parser
 while (token != null) {
 token = sequence.nextToken();
 }
 // Second parser should return START_OBJECT
 token = sequence.nextToken();
 assertEquals(JsonToken.START_OBJECT, token.getId(), "Second parser should return START_OBJECT");
 token = sequence.nextToken();
 assertEquals(JsonToken.FIELD_NAME, token.getId(), "Second parser should return FIELD_NAME");
 assertEquals("age", token.getText());
 token = sequence.nextToken();
 assertEquals(JsonToken.VALUE_NUMBER_INT, token.getId(), "Second parser should return VALUE_NUMBER_INT");
 assertEquals(30, token.getIntValue());
 token = sequence.nextToken();
 assertEquals(JsonToken.END_OBJECT, token.getId(), "Both parsers should return END_OBJECT");
 }

 // Test that multiple parsers are consumed in order
 @Test
 public void testMultipleParsersConsumedInOrder() throws IOException {
 JsonParser parser1 = new JsonFactory().createParser(new StringReader("{\"name\": \"John\"}"));
 JsonParser parser2 = new JsonFactory().createParser(new StringReader("{\"age\": 30}"));
 JsonParserSequence sequence = JsonParserSequence.createFlattened(parser1, parser2);
 JsonToken token = sequence.nextToken();
 // Both parsers should be consumed in order
 assertEquals(JsonToken.START_OBJECT, token.getId(), "First parser should return START_OBJECT");
 token = sequence.nextToken();
 assertEquals(JsonToken.FIELD_NAME, token.getId(), "First parser should return FIELD_NAME");
 assertEquals("name", token.getText());
 token = sequence.nextToken();
 assertEquals(JsonToken.VALUE_STRING, token.getId(), "First parser should return VALUE_STRING");
 assertEquals("John", token.getText());
 token = sequence.nextToken();
 assertEquals(JsonToken.START_OBJECT, token.getId(), "Second parser should return START_OBJECT");
 token = sequence.nextToken();
 assertEquals(JsonToken.FIELD_NAME, token.getId(), "Second parser should return FIELD_NAME");
 assertEquals("age", token.getText());
 token = sequence.nextToken();
 assertEquals(JsonToken.VALUE_NUMBER_INT, token.getId(), "Second parser should return VALUE_NUMBER_INT");
 assertEquals(30, token.getIntValue());
 token = sequence.nextToken();
 assertEquals(JsonToken.END_OBJECT, token.getId(), "Both parsers should return END_OBJECT");
 }

 // Test that exception is thrown when no more parsers are available
 @Test(expected = IOException.class)
 public void testNoMoreParsers() throws IOException {
 JsonParser parser1 = new JsonFactory().createParser(new StringReader("{\"name\": \"John\"}"));
 JsonParser parser2 = new JsonFactory().createParser(new StringReader("{\"age\": 30}"));
 JsonParserSequence sequence = JsonParserSequence.createFlattened(parser1, parser2);
 JsonToken token = sequence.nextToken();
 // Consume all tokens in the sequence
 while (token != null) {
 token = sequence.nextToken();
 }
 // Should throw IOException when no more parsers are available
 sequence.nextToken();
 }

 // Test that exception is thrown when one of the parsers throws an exception
 @Test(expected = JsonParseException.class)
 public void testParserThrowsException() throws IOException {
 JsonParser parser1 = new JsonFactory().createParser(new StringReader("{\"name\": \"John\"}"));
 JsonParser parser2 = new JsonFactory().createParser(new StringReader("{\"age\": }")); // Invalid JSON
 JsonParserSequence sequence = JsonParserSequence.createFlattened(parser1, parser2);
 JsonToken token = sequence.nextToken();
 // Consume tokens from the first parser
 while (token != JsonToken.END_OBJECT) {
 token = sequence.nextToken();
 }
 // Second parser should throw JsonParseException
 JsonParseException ex = (JsonParseException) assertThrows(JsonParseException.class, () -> sequence.nextToken());
 assertEquals("Not enough tokens: missing value to end ObjectInstance", ex.getMessage());
 }
}