package com.fasterxml.jackson.core.util;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class JsonParserSequenceTest {

 private JsonParser firstParser;
 private JsonParser secondParser;
 private JsonParser sequenceParser;

 @Before
 public void setUp() throws IOException {
 firstParser = new JsonFactory().createParser("[]");
 secondParser = new JsonFactory().createParser("{\"a\":1}");
 sequenceParser = JsonParserSequence.createFlattened(firstParser, secondParser);
 }

 @Test
 public void testContainedParsersCount() throws IOException {
 assertEquals(2, sequenceParser.containedParsersCount());
 }

 @Test
 public void testNextTokenHappyPath() throws IOException {
 JsonToken token = sequenceParser.nextToken();
 assertEquals(JsonToken.START_ARRAY, token.getId());

 token = sequenceParser.nextToken();
 assertEquals(JsonToken.END_ARRAY, token.getId());

 token = sequenceParser.nextToken();
 assertEquals(JsonToken.START_OBJECT, token.getId());

 token = sequenceParser.nextToken();
 assertEquals(JsonToken.FIELD_NAME, token.getId());
 assertEquals("a", token.getText());

 token = sequenceParser.nextToken();
 assertEquals(JsonToken.VALUE_NUMBER_INT, token.getId());
 assertEquals(1, token.asInt());

 token = sequenceParser.nextToken();
 assertEquals(JsonToken.END_OBJECT, token.getId());
 }

 @Test(expected = IOException.class)
 public void testNextTokenError() throws IOException {
 sequenceParser.nextToken(); // consume [
 sequenceParser.nextToken(); // consume []
 sequenceParser.nextToken(); // consume {
 sequenceParser.nextToken(); // consume "a"
 sequenceParser.nextToken(); // consume:
 sequenceParser.nextToken(); // consume 1
 sequenceParser.nextToken(); // consume }
 sequenceParser.nextToken(); // consume EOF

 // should throw an IOException here
 sequenceParser.nextToken();
 }

 @Test
 public void testSwitchToNextParser() throws IOException {
 // consume [
 JsonToken token1 = sequenceParser.nextToken();
 assertEquals(JsonToken.START_ARRAY, token1.getId());

 // switch to next parser
 JsonToken token2 = sequenceParser.nextToken();
 assertEquals(JsonToken.START_OBJECT, token2.getId());

 // switch back to first parser
 sequenceParser.switchToNext();

 // consume "a"
 token1 = sequenceParser.nextToken();
 assertEquals(JsonToken.FIELD_NAME, token1.getId());
 assertEquals("a", token1.getText());
 }
}