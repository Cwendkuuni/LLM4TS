package com.fasterxml.jackson.core.util;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonParserSequenceTest {

 private JsonParser firstParser;
 private JsonParser secondParser;
 private JsonParserSequence parserSequence;

 @Before
 public void setup() throws IOException {
 firstParser = new ObjectMapper().getFactory().createParser("[]");
 secondParser = new ObjectMapper().getFactory().createParser("{\"field\": \"value\"}");
 parserSequence = JsonParserSequence.createFlattened(firstParser, secondParser);
 }

 @Test
 public void testEmptySequence() throws IOException {
 // Test that empty sequence returns expected token
 JsonToken token = parserSequence.nextToken();
 assertEquals(JsonToken.START_ARRAY, token.getId());

 // Test that empty sequence returns null when all parsers have been consumed
 token = parserSequence.nextToken();
 assertNull(token);
 }

 @Test
 public void testSimpleSequence() throws IOException {
 // Test that sequence returns tokens from the first parser by default
 JsonToken token = parserSequence.nextToken();
 assertEquals(JsonToken.START_ARRAY, token.getId());

 // Test that sequence switches to the next parser when the first one has been consumed
 token = parserSequence.nextToken();
 assertEquals(JsonToken.FIELD_NAME, token.getId());
 assertEquals("field", token.getText());

 // Test that sequence returns null when all parsers have been consumed
 token = parserSequence.nextToken();
 assertNull(token);
 }

 @Test(expected = IOException.class)
 public void testIOExceptionSequential() throws IOException {
 // Test that an IOExcpetion thrown by one parser is propagated to the user
 firstParser.nextToken();
 secondParser.throwIOException();
 parserSequence.nextToken();
 }

 @Test(expected = JsonParseException.class)
 public void testJsonParseExceptionSequential() throws IOException {
 // Test that a JsonParseException thrown by one parser is propagated to the user
 firstParser.nextToken();
 secondParser.throwJsonParseException();
 parserSequence.nextToken();
 }

 @Test
 public void testContainedParsersCount() {
 // Test that containedParsersCount returns the correct number of contained parsers
 assertEquals(2, parserSequence.containedParsersCount());
 }

 @Test(expected = IllegalStateException.class)
 public void testSwitchToNextAfterClose() throws IOException {
 // Test that calling switchToNext after close throws an IllegalStateException
 parserSequence.close();
 parserSequence.nextToken();
 parserSequence.switchToNext();
 }

}