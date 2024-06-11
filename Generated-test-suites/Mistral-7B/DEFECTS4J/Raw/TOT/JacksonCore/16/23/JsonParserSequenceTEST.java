package com.fasterxml.jackson.core.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.BaseTest;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonParserSequenceTest extends BaseTest {

 private JsonParserSequence _parserSequence;
 private JsonParser _parser1;
 private JsonParser _parser2;
 private JsonParser _parser3;

 @Before
 public void setUp() throws Exception {
 JsonFactory jsonFactory = new JsonFactory();
 _parser1 = jsonFactory.createParser(getClass().getResourceAsStream("/test.json"));
 _parser2 = jsonFactory.createParser(getClass().getResourceAsStream("/test2.json"));
 _parser3 = jsonFactory.createParser(getClass().getResourceAsStream("/test3.json"));
 _parserSequence = JsonParserSequence.createFlattened(_parser1, _parser2, _parser3);
 }

 @Test
 public void testNextToken_FirstParser() throws Exception {
 JsonToken token = _parserSequence.nextToken();
 Assert.assertEquals(JsonToken.START_ARRAY, token);
 }

 @Test
 public void testNextToken_SecondParser() throws Exception {
 _parserSequence.nextToken(); // consume START_ARRAY
 JsonToken token = _parserSequence.nextToken();
 Assert.assertEquals(JsonToken.START_OBJECT, token);
 }

 @Test
 public void testNextToken_ThirdParser() throws Exception {
 _parserSequence.nextToken(); // consume START_ARRAY
 _parserSequence.nextToken(); // consume START_OBJECT
 JsonToken token = _parserSequence.nextToken();
 Assert.assertEquals(JsonToken.VALUE_STRING, token);
 }

 @Test(expected = IOException.class)
 public void testNextToken_InvalidFormat() throws Exception {
 _parserSequence.nextToken(); // consume START_ARRAY
 _parserSequence.nextToken(); // consume START_OBJECT
 _parserSequence.nextToken(); // consume VALUE_STRING
 _parserSequence.nextToken(); // consume VALUE_STRING
 _parserSequence.nextToken(); // consume END_OBJECT
 _parserSequence.nextToken(); // consume END_ARRAY
 _parserSequence.nextToken(); // consume END_ARRAY
 }

 @Test
 public void testContainedParsersCount() {
 int containedParsersCount = _parserSequence.containedParsersCount();
 Assert.assertEquals(3, containedParsersCount);
 }
}