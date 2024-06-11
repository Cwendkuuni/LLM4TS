package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class JsonParserSequenceTest {

 private JsonParser parser1;
 private JsonParser parser2;
 private JsonParserSequence parserSequence;

 @Before
 public void setup() throws IOException {
 parser1 = new JsonFactory().createParser("[]");
 parser2 = new JsonFactory().createParser("{}");
 parserSequence = JsonParserSequence.createFlattened(parser1, parser2);
 }

 @Test
 public void testCreateFlattened() throws IOException {
 JsonParser parsedParserSequence = parserSequence;
 assertEquals(2, parsedParserSequence.containedParsersCount());

 JsonParser parsedParser1 = parserSequence;
 assertEquals(JsonToken.START_ARRAY, parsedParser1.nextToken());
 assertFalse(parsedParser1.nextToken() == null); // next token is not null

 parsedParser1.close();
 JsonParser parsedParser2 = parserSequence;

 assertEquals(JsonToken.START_OBJECT, parsedParser2.nextToken());
 assertFalse(parsedParser2.nextToken() == null); // next token is not null

 parsedParser2.close();
 JsonParser parsedParserSequenceLast = parserSequence;

 assertNull(parsedParserSequence.nextToken()); // no more tokens
 assertTrue(parsedParserSequenceLast.nextToken() == null); // no more tokens
 }

 @Test
 public void testSwitchToNext() throws IOException {
 JsonParser parsedParserSequence = parserSequence;
 assertEquals(JsonToken.START_ARRAY, parsedParserSequence.nextToken());

 parsedParserSequence.close();
 parsedParserSequence = parserSequence;

 assertEquals(JsonToken.START_OBJECT, parsedParserSequence.nextToken());

 assertTrue(parserSequence.switchToNext()); // next parser is active
 assertEquals(JsonToken.START_ARRAY, parserSequence.nextToken());

 parsedParserSequence.close();
 assertTrue(parserSequence.switchToNext()); // next parser is active
 assertEquals(JsonToken.START_OBJECT, parserSequence.nextToken());

 assertFalse(parserSequence.switchToNext()); // no more parsers
 }
}