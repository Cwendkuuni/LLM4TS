package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;

public class JsonParserSequenceTest {

 private JsonParser createTestParser() {
 return new JsonFactory().createParser("[]");
 }

 @Test
 public void testJsonParserSequenceConstruction() throws IOException {
 JsonParser firstParser = createTestParser();
 JsonParser secondParser = createTestParser();
 JsonParserSequence parserSequence = JsonParserSequence.createFlattened(firstParser, secondParser);
 assertEquals(2, parserSequence.containedParsersCount());
 }

 @Test
 public void testJsonParserSequenceNextToken() throws IOException {
 JsonParser firstParser = createTestParser();
 JsonParser secondParser = createTestParser();
 JsonParserSequence parserSequence = JsonParserSequence.createFlattened(firstParser, secondParser);
 JsonToken token = parserSequence.nextToken();
 assertNotNull(token);
 assertEquals(JsonToken.START_ARRAY, token.getId());
 // First parser should have returned START_ARRAY
 // Now check if second parser can return next token
 token = parserSequence.nextToken();
 assertNotNull(token);
 assertEquals(JsonToken.END_ARRAY, token.getId());
 // Second parser should have returned END_ARRAY
 // Check if switch to next parser works
 parserSequence.switchToNext();
 assertFalse(parserSequence.delegate instanceof JsonParserSequence);
 assertFalse(parserSequence.delegate instanceof JsonParser);
 assertTrue(parserSequence.delegate instanceof JsonParser);
 }

 @Test
 public void testJsonParserSequenceClose() throws IOException {
 JsonParser firstParser = createTestParser();
 JsonParser secondParser = createTestParser();
 JsonParserSequence parserSequence = JsonParserSequence.createFlattened(firstParser, secondParser);
 parserSequence.close();
 firstParser.close();
 secondParser.close();
 }

 @Test(expected = IllegalStateException.class)
 public void testJsonParserSequenceOutOfParsers() throws IOException {
 JsonParser firstParser = createTestParser();
 JsonParser secondParser = createTestParser();
 JsonParser thirdParser = createTestParser();
 JsonParserSequence parserSequence = new JsonParserSequence(new JsonParser[] { firstParser, secondParser, thirdParser });
 JsonToken token = parserSequence.nextToken();
 assertNotNull(token);
 assertEquals(JsonToken.START_ARRAY, token.getId());
 // First parser should have returned START_ARRAY
 // Now check if second parser can return next token
 token = parserSequence.nextToken();
 assertNotNull(token);
 assertEquals(JsonToken.END_ARRAY, token.getId());
 // Second parser should have returned END_ARRAY
 // Now check if third parser can return next token
 parserSequence.switchToNext();
 parserSequence.switchToNext();
 parserSequence.nextToken(); // Should throw exception
 }
}