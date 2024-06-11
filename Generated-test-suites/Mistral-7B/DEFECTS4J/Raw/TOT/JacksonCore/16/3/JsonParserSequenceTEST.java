package com.fasterxml.jackson.core.util;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.*;

public class JsonParserSequenceTest {

 private static JsonParser createTestParser() throws IOException {
 return new JsonFactory().createParser(JsonParserSequenceTest.class.getResourceAsStream("/test.json"));
 }

 @Test
 public void testEmptySequence() throws IOException {
 JsonParser firstParser = createTestParser();
 JsonParser secondParser = createTestParser();
 JsonParserSequence jsonParserSequence = JsonParserSequence.createFlattened(firstParser, secondParser);

 Assert.assertEquals(JsonToken.START_ARRAY, jsonParserSequence.nextToken());
 Assert.assertEquals(JsonToken.END_ARRAY, jsonParserSequence.nextToken());
 Assert.assertEquals(JsonToken.END_OBJECT, jsonParserSequence.nextToken()); // end of stream

 firstParser.close();
 secondParser.close();
 jsonParserSequence.close();
 }

 @Test
 public void testSimpleSequence() throws IOException {
 JsonParser firstParser = createTestParser();
 JsonParser secondParser = createTestParser();
 JsonParserSequence jsonParserSequence = JsonParserSequence.createFlattened(firstParser, secondParser);

 Assert.assertEquals(JsonToken.START_ARRAY, jsonParserSequence.nextToken());
 Assert.assertEquals(JsonToken.VALUE_STRING, jsonParserSequence.nextToken());
 Assert.assertEquals("first", jsonParserSequence.getText());
 Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, jsonParserSequence.nextToken());
 Assert.assertEquals(1, jsonParserSequence.nextIntValue());
 Assert.assertEquals(JsonToken.VALUE_STRING, jsonParserSequence.nextToken());
 Assert.assertEquals("second", jsonParserSequence.getText());
 Assert.assertEquals(JsonToken.END_ARRAY, jsonParserSequence.nextToken());
 Assert.assertEquals(JsonToken.END_OBJECT, jsonParserSequence.nextToken()); // end of stream

 firstParser.close();
 secondParser.close();
 jsonParserSequence.close();
 }

 @Test(expected = JsonParseException.class)
 public void testErrorSequence() throws IOException {
 JsonParser firstParser = createTestParser();
 JsonParser secondParser = createTestParser();
 JsonParserSequence jsonParserSequence = JsonParserSequence.createFlattened(firstParser, secondParser);

 Assert.assertEquals(JsonToken.START_ARRAY, jsonParserSequence.nextToken());
 Assert.assertEquals(JsonToken.VALUE_STRING, jsonParserSequence.nextToken());
 Assert.assertEquals("first", jsonParserSequence.getText());

 JsonParser errorParser = createTestParser(); // intentionally creating a malformed parser
 JsonParserSequence.createFlattened(jsonParserSequence, errorParser); // will throw an exception

 firstParser.close();
 secondParser.close();
 jsonParserSequence.close();
 errorParser.close();
 }
}