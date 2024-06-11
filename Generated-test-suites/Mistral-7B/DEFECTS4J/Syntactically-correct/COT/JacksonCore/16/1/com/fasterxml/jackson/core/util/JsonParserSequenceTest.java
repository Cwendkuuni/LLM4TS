package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.*;

public class JsonParserSequenceTest {

 private JsonParser[] parsers;
 private JsonParserSequence jsonParserSequence;

 @Before
 public void setup() {
 JsonParser parser1 = new JsonFactory().createParser("[]");
 JsonParser parser2 = new JsonFactory().createParser("{}");
 parsers = new JsonParser[] { parser1, parser2 };
 jsonParserSequence = new JsonParserSequence(parsers);
 }

 @Test
 public void testConstructor() throws IOException {
 JsonParserSequence jsonParserSequence = JsonParserSequence.createFlattened(parsers[0], parsers[1]);
 assertEquals(parsers.length, jsonParserSequence.containedParsersCount());
 }

 @Test
 public void testNextToken() throws IOException {
 JsonToken token = jsonParserSequence.nextToken();
 assertNotNull(token);
 assertEquals(JsonToken.START_ARRAY, token.getId());

 token = jsonParserSequence.nextToken();
 assertNotNull(token);
 assertEquals(JsonToken.END_ARRAY, token.getId());

 token = jsonParserSequence.nextToken();
 assertNotNull(token);
 assertEquals(JsonToken.START_OBJECT, token.getId());

 token = jsonParserSequence.nextToken();
 assertNotNull(token);
 assertEquals(JsonToken.FIELD_NAME, token.getId());

 token = jsonParserSequence.nextToken();
 assertNotNull(token);
 assertEquals(JsonToken.VALUE_STRING, token.getId());

 token = jsonParserSequence.nextToken();
 assertNull(token);
 }

 @Test(expected = IOException.class)
 public void testClose() throws IOException {
 jsonParserSequence.close();
 }

 @Test(expected = JsonParseException.class)
 public void testNextTokenIOException() throws IOException {
 JsonParser parser = new JsonFactory().createParser("{\"error\": \"test\"}");
 JsonParserSequence jsonParserSequence = new JsonParserSequence(new JsonParser[] { parser });
 jsonParserSequence.nextToken();
 parser.close();
 jsonParserSequence.nextToken();
 }

 @Test(expected = JsonParseException.class)
 public void testNextTokenJsonParseException() throws IOException {
 JsonParser parser = new JsonFactory().createParser("{invalid_json}");
 JsonParserSequence jsonParserSequence = new JsonParserSequence(new JsonParser[] { parser });
 jsonParserSequence.nextToken();
 }

 @Test
 public void testSwitchToNextParser() throws IOException {
 JsonParser parser1 = new JsonFactory().createParser("[]");
 JsonParser parser2 = new JsonFactory().createParser("{}");
 JsonParser parser3 = new JsonFactory().createParser("{1,2,3}");
 JsonParser[] parsers = new JsonParser[] { parser1, parser2, parser3 };
 JsonParserSequence jsonParserSequence = new JsonParserSequence(parsers);

 JSONToken token = jsonParserSequence.nextToken();
 assertEquals(JsonToken.START_ARRAY, token.getId());

 token = jsonParserSequence.nextToken();
 assertEquals(JsonToken.END_ARRAY, token.getId());

 assertTrue(jsonParserSequence.switchToNext());
 assertEquals(JsonParser.class, jsonParserSequence.delegate.getClass());
 assertEquals(parsers[1], jsonParserSequence.delegate);

 token = jsonParserSequence.nextToken();
 assertEquals(JsonToken.START_OBJECT, token.getId());

 token = jsonParserSequence.nextToken();
 assertEquals(JsonToken.FIELD_NAME, token.getId());

 token = jsonParserSequence.nextToken();
 assertEquals(JsonToken.VALUE_NUMBER_INT, token.getId());
 assertEquals(1, token.getIntValue());

 assertTrue(jsonParserSequence.switchToNext());
 assertEquals(JsonParser.class, jsonParserSequence.delegate.getClass());
 assertEquals(parsers[2], jsonParserSequence.delegate);

 token = jsonParserSequence.nextToken();
 assertEquals(JsonToken.VALUE_NUMBER_INT, token.getId());
 assertEquals(2, token.getIntValue());

 token = jsonParserSequence.nextToken();
 assertEquals(JsonToken.VALUE_NUMBER_INT, token.getId());
 assertEquals(3, token.getIntValue());

 token = jsonParserSequence.nextToken();
 assertEquals(JsonToken.END_OBJECT, token.getId());

 assertTrue(jsonParserSequence.switchToNext());
 assertNull(jsonParserSequence.delegate);
 }

 @Test
 public void testSwitchToNextParserNoMoreParsers() throws IOException {
 JsonParser parser1 = new JsonFactory().createParser("[]");
 JsonParser parser2 = new JsonFactory().createParser("{}");
 JsonParser parser3 = new JsonFactory().createParser("{1,2,3}");
 JsonParser[] parsers = new JsonParser[] { parser1, parser2, parser3 };
 JsonParserSequence jsonParserSequence = new JsonParserSequence(parsers);

 JSONToken token = jsonParserSequence.nextToken();
 assertEquals(JsonToken.START_ARRAY, token.getId());

 token = jsonParserSequence.nextToken();
 assertEquals(JsonToken.END_ARRAY, token.getId());

 assertTrue(jsonParserSequence.switchToNext());
 assertEquals(JsonParser.class, jsonParserSequence.delegate.getClass());
 assertEquals(parsers[1], jsonParserSequence.delegate);

 token = jsonParserSequence.nextToken();
 assertEquals(JsonToken.START_OBJECT, token.getId());

 token = jsonParserSequence.nextToken();
 assertEquals(JsonToken.FIELD_NAME, token.getId());

 token = jsonParserSequence.nextToken();
 assertEquals(JsonToken.VALUE_NUMBER_INT, token.getId());
 assertEquals(1, token.getIntValue());

 assertTrue(jsonParserSequence.switchToNext());
 assertEquals(JsonParser.class, jsonParserSequence.delegate.getClass());
 assertEquals(parsers[2], jsonParserSequence.delegate);

 token = jsonParserSequence.nextToken();
 assertEquals(JsonToken.VALUE_NUMBER_INT, token.getId());
 assertEquals(2, token.getIntValue());

 token = jsonParserSequence.nextToken();
 assertEquals(JsonToken.VALUE_NUMBER_INT, token.getId());
 assertEquals(3, token.getIntValue());

 token = jsonParserSequence.nextToken();
 assertEquals(JsonToken.END_OBJECT, token.getId());

 assertTrue(jsonParserSequence.switchToNext());
 assertNull(jsonParserSequence.delegate);

 token = jsonParserSequence.nextToken();
 assertNull(token);

 assertFalse(jsonParserSequence.switchToNext());
 }
}