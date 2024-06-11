package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class JsonParserSequenceTest {

 private JsonParser[] parsers;
 private JsonParserSequence jsonParserSequence;

 @Before
 public void setUp() throws IOException {
 String json1 = "{\"name\":\"John\",\"age\":30}";
 JsonParser parser1 = new ObjectMapper().readTree(json1);
 String json2 = "[1,2,3]";
 JsonParser parser2 = new ObjectMapper().readTree(json2);
 parsers = new JsonParser[] { parser1, parser2 };
 jsonParserSequence = new JsonParserSequence(parsers);
 }

 @Test
 public void testBasicFunctionality() throws IOException {
 JsonToken token = jsonParserSequence.nextToken();
 assertNotNull(token);
 assertEquals(JsonToken.START_OBJECT, token.getId());

 token = jsonParserSequence.nextToken();
 assertNotNull(token);
 assertEquals(JsonToken.FIELD_NAME, token.getId());
 assertEquals("name", token.getText().toString());

 token = jsonParserSequence.nextToken();
 assertNotNull(token);
 assertEquals(JsonToken.VALUE_STRING, token.getId());
 assertEquals("John", token.getText().toString());

 token = jsonParserSequence.nextToken();
 assertNotNull(token);
 assertEquals(JsonToken.FIELD_NAME, token.getId());
 assertEquals("age", token.getText().toString());

 token = jsonParserSequence.nextToken();
 assertNotNull(token);
 assertEquals(JsonToken.VALUE_NUMBER_INT, token.getId());
 assertEquals(30, token.getIntValue());

 token = jsonParserSequence.nextToken();
 assertNull(token);
 }

 @Test
 public void testContainedParsersCount() throws IOException {
 assertEquals(parsers.length, jsonParserSequence.containedParsersCount());
 }

 @Test(expected = IOException.class)
 public void testIOExceptionOnFirstParser() throws IOException {
 JsonParser parser1 = new ObjectMapper().readTree("{throwException:true}");
 parsers = new JsonParser[] { parser1, parser2 };
 JsonParserSequence jsonParserSequence = new JsonParserSequence(parsers);
 jsonParserSequence.nextToken();
 jsonParserSequence.nextToken();
 jsonParserSequence.nextToken();
 jsonParserSequence.nextToken();
 jsonParserSequence.nextToken();
 assertFalse(jsonParserSequence.switchToNext());
 jsonParserSequence.nextToken(); // this should throw an IOException
 }

 @Test(expected = IllegalStateException.class)
 public void testIllegalStateExceptionOnNoParsers() throws IOException {
 JsonParser[] parsers = {};
 JsonParserSequence jsonParserSequence = new JsonParserSequence(parsers);
 jsonParserSequence.nextToken();
 jsonParserSequence.nextToken();
 jsonParserSequence.nextToken();
 jsonParserSequence.nextToken();
 jsonParserSequence.nextToken();
 jsonParserSequence.nextToken();
 jsonParserSequence.switchToNext(); // this should throw an IllegalStateException
 }

 @Test
 public void testSwitchToNext() throws IOException {
 JsonToken token1 = jsonParserSequence.nextToken();
 JsonToken token2 = jsonParserSequence.nextToken();
 JsonToken token3 = jsonParserSequence.nextToken();
 JsonToken token4 = jsonParserSequence.nextToken();
 JsonToken token5 = jsonParserSequence.nextToken();

 assertNotNull(token1);
 assertNotNull(token2);
 assertNotNull(token3);
 assertNotNull(token4);
 assertNotNull(token5);

 assertTrue(jsonParserSequence.switchToNext());
 JsonToken token6 = jsonParserSequence.nextToken();
 assertNotNull(token6);

 assertTrue(jsonParserSequence.switchToNext());
 JsonToken token7 = jsonParserSequence.nextToken();
 assertNotNull(token7);

 assertTrue(jsonParserSequence.switchToNext());
 JsonToken token8 = jsonParserSequence.nextToken();
 assertNotNull(token8);

 assertFalse(jsonParserSequence.switchToNext());
 JsonToken token9 = jsonParserSequence.nextToken();
 assertNull(token9);
 }

 @Test
 public void testToString() throws IOException {
 assertEquals("JsonParserSequence[parsers=[ObjectMapper@1a83f693, ObjectMapper@483d6f20]]", jsonParserSequence.toString());
 }
}