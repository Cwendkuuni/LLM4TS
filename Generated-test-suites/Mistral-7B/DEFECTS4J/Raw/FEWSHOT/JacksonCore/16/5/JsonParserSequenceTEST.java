package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;
import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;
import com.fasterxml.jackson.core.*;

public class JsonParserSequenceTest {

 private JsonParser firstParser;
 private JsonParser secondParser;
 private JsonParser thirdParser;
 private JsonParser sequenceParser;

 @Before
 public void setup() throws IOException {
 firstParser = new JsonTestFactory().createParser("[]");
 secondParser = new JsonTestFactory().createParser("{}");
 thirdParser = new JsonTestFactory().createParser("{\"foo\":1}");
 sequenceParser = JsonParserSequence.createFlattened(firstParser, secondParser, thirdParser);
 }

 @Test
 public void testCreateFlattened() throws IOException {
 JsonParser[] parsers = new JsonParser[] { firstParser, secondParser, thirdParser };
 JsonParser sequence = JsonParserSequence.createFlattened(parsers[0], parsers[1], parsers[2]);
 assertEquals(parsers.length, sequence.containedParsersCount());
 }

 @Test
 public void testSwitchToNext() throws IOException {
 JsonToken token;
 assertEquals(JsonToken.START_ARRAY, sequenceParser.nextToken());
 assertEquals(JsonToken.VALUE_NULL, sequenceParser.nextToken());
 assertTrue(sequenceParser.switchToNext());
 assertEquals(JsonToken.START_OBJECT, sequenceParser.nextToken());
 assertFalse(sequenceParser.switchToNext());
 assertEquals(JsonToken.END_OBJECT, sequenceParser.nextToken());
 assertEquals(JsonToken.END_ARRAY, sequenceParser.nextToken());
 assertFalse(sequenceParser.switchToNext());
 }

 @Test
 public void testNextTokenWithActiveParsers() throws IOException {
 int count = 0;
 while (sequenceParser.nextToken() != null) {
 count++;
 }
 assertEquals(15, count); // 3 parsers * 5 tokens each
 }

 @Test
 public void testSwitchToNextWithNullParser() throws IOException {
 JsonParser nullParser = null;
 JsonParser sequence = JsonParserSequence.createFlattened(firstParser, nullParser);
 assertEquals(JsonToken.START_ARRAY, sequence.nextToken());
 assertFalse(sequence.switchToNext());
 }

 @Test(expected = NullPointerException.class)
 public void testSwitchToNextWithNullSequence() throws IOException {
 JsonParser nullSequence = null;
 nullSequence.switchToNext();
 }

 @After
 public void teardown() throws IOException {
 firstParser.close();
 secondParser.close();
 thirdParser.close();
 sequenceParser.close();
 }
}