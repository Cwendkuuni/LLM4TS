package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;

public class JsonParserSequenceTest {

 private JsonParser firstParser;
 private JsonParser secondParser;
 private JsonParser thirdParser;

 @Test
 public void testJsonParserSequenceConstruction() {
 JsonParser[] parsers = new JsonParser[] { firstParser, secondParser };
 JsonParserSequence jsonParserSequence = new JsonParserSequence(parsers);
 assertEquals(parsers.length, jsonParserSequence.containedParsersCount());
 }

 @Test
 public void testNextToken_FirstParser() throws IOException {
 JsonParser firstParser = createMockParser();
 JsonParserSequence jsonParserSequence = new JsonParserSequence(new JsonParser[] { firstParser });
 JsonToken token = jsonParserSequence.nextToken();
 assertNotNull(token);
 }

 @Test
 public void testNextToken_SecondParser() throws IOException {
 JsonParser firstParser = createMockParser();
 JsonParser secondParser = createMockParser();
 JsonParserSequence jsonParserSequence = new JsonParserSequence(new JsonParser[] { firstParser, secondParser });
 JsonToken token = jsonParserSequence.nextToken();
 assertNotNull(token);
 jsonParserSequence.switchToNext();
 JsonToken token2 = jsonParserSequence.nextToken();
 assertNotNull(token2);
 }

 @Test
 public void testNextToken_MultipleParsers() throws IOException {
 JsonParser firstParser = createMockParser();
 JsonParser secondParser = createMockParser();
 JsonParser thirdParser = createMockParser();
 JsonParserSequence jsonParserSequence = new JsonParserSequence(new JsonParser[] { firstParser, secondParser, thirdParser });
 JsonToken token = jsonParserSequence.nextToken();
 assertNotNull(token);
 jsonParserSequence.switchToNext();
 jsonParserSequence.switchToNext();
 JsonToken token2 = jsonParserSequence.nextToken();
 assertNotNull(token2);
 }

 @Test(expected = IllegalStateException.class)
 public void testNextToken_NoParsers() throws IOException {
 JsonParserSequence jsonParserSequence = new JsonParserSequence(new JsonParser[0]);
 jsonParserSequence.nextToken();
 }

 @Test
 public void testSwitchToNext_NoParsersLeft() throws IOException {
 JsonParser firstParser = createMockParser();
 JsonParserSequence jsonParserSequence = new JsonParserSequence(new JsonParser[] { firstParser });
 jsonParserSequence.switchToNext();
 assertFalse(jsonParserSequence.switchToNext());
 }

 @Test
 public void testSwitchToNext_SwitchToParser() throws IOException {
 JsonParser firstParser = createMockParser();
 JsonParser secondParser = createMockParser();
 JsonParserSequence jsonParserSequence = new JsonParserSequence(new JsonParser[] { firstParser, secondParser });
 jsonParserSequence.switchToNext();
 JsonToken token = jsonParserSequence.nextToken();
 assertNotNull(token);
 }

 @Test(expected = IOException.class)
 public void testClose_NoParsersLeft() throws IOException {
 JsonParser firstParser = createMockParser();
 JsonParserSequence jsonParserSequence = new JsonParserSequence(new JsonParser[] { firstParser });
 jsonParserSequence.close();
 }

 @Test
 public void testCreateFlattened() throws IOException {
 JsonParser firstParser = createMockParser();
 JsonParser secondParser = createMockParser();
 JsonParserSequence jsonParserSequence = JsonParserSequence.createFlattened(firstParser, secondParser);
 assertEquals(2, jsonParserSequence.containedParsersCount());
 }

 private JsonParser createMockParser() throws IOException {
 return mock(JsonParser.class);
 }

}