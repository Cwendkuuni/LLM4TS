package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class JsonParserSequenceTest {

 private JsonParser parser1;
 private JsonParser parser2;
 private JsonParser sequence;

 @SuppressWarnings("deprecation")
 @Before
 public void setup() throws IOException {
 parser1 = new JsonFactory().createParser("[1,2,3]");
 parser2 = new JsonFactory().createParser("[4,5,6]");
 sequence = JsonParserSequence.createFlattened(parser1, parser2);
 }

 @Test
 public void testConstructWithParsers() {
 JsonParserSequence jsonParserSequence = new JsonParserSequence(new JsonParser[] { parser1, parser2 });
 assertEquals(2, jsonParserSequence.containedParsersCount());
 }

 @Test
 public void testCreateFlattened() {
 JsonParserSequence jsonParserSequence = JsonParserSequence.createFlattened(parser1, parser2);
 assertEquals(2, jsonParserSequence.containedParsersCount());
 }

 @Test
 public void testContainedParsersCount() {
 assertEquals(2, sequence.containedParsersCount());
 }

 @Test
 public void testClose() throws IOException {
 sequence.close();
 parser1.close();
 parser2.close();
 }

 @Test
 public void testNextToken() throws IOException, JsonParseException {
 JsonToken token = null;
 while (token == null) {
 token = sequence.nextToken();
 }
 assertEquals(JsonToken.START_ARRAY, token);

 token = sequence.nextToken();
 assertEquals(JsonToken.VALUE_NUMBER_INT, token.getIntValue());
 assertEquals(1, token.getIntValue());

 token = sequence.nextToken();
 assertEquals(JsonToken.VALUE_NUMBER_INT, token.getIntValue());
 assertEquals(2, token.getIntValue());

 token = sequence.nextToken();
 assertEquals(JsonToken.VALUE_NUMBER_INT, token.getIntValue());
 assertEquals(3, token.getIntValue());

 token = sequence.nextToken();
 assertEquals(JsonToken.END_ARRAY, token);

 token = sequence.nextToken(); // should be null
 assertNull(token);
 }

 @Test(expected = JsonParseException.class)
 public void testNextTokenWithException() throws IOException {
 sequence.nextToken(); // read valid token from parser1
 sequence.nextToken(); // read valid token from parser1
 sequence.nextToken(); // read valid token from parser1

 // parser2 should throw an exception, but sequence should not throw one
 sequence.nextToken(); // should throw JsonParseException
 }

 @Test
 public void testSwitchToNext() {
 JsonParser currentParser = sequence;
 boolean switched = false;
 while (!switched && currentParser != null) {
 switched = currentParser.switchToNext();
 currentParser = currentParser.getDelegate();
 }
 assertFalse(switched);

 sequence = sequence.next(); // switch to parser2
 assertFalse(sequence.isClosed());
 assertFalse(parser1.isClosed());
 assertFalse(parser2.isClosed());

 switched = false;
 while (!switched && sequence != null) {
 switched = sequence.switchToNext();
 sequence = sequence.getDelegate();
 }
 assertTrue(switched);
 assertNull(sequence);
 assertFalse(parser1.isClosed());
 assertFalse(parser2.isClosed());
 }

 @Test
 public void testSwitchToNextWhenNoParsersLeft() {
 JsonParser currentParser = sequence;
 boolean switched = false;
 while (!switched && currentParser != null) {
 switched = currentParser.switchToNext();
 currentParser = currentParser.getDelegate();
 }
 assertTrue(switched);
 assertNull(sequence);
 assertFalse(parser1.isClosed());
 assertFalse(parser2.isClosed());

 sequence.next(); // switch to parser2
 sequence.close();
 assertFalse(sequence.isClosed());
 assertFalse(parser1.isClosed());
 assertFalse(parser2.isClosed());

 // reading next token from closed parser should throw an exception
 sequence.nextToken(); // expected IOException
 }

}