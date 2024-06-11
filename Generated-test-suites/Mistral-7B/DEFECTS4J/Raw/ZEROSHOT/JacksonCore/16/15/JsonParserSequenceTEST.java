package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.ParserMinimalBase;
import com.fasterxml.jackson.databind.JsonNode;

public class JsonParserSequenceTest {

 private static JsonParser createTestParser() {
 return new ParserMinimalBase() {
 @Override
 public JsonToken nextToken() throws IOException, JsonProcessingException {
 return null;
 }
 };
 }

 @Test
 public void testJsonParserSequenceCreation() {
 JsonParser parser1 = createTestParser();
 JsonParser parser2 = createTestParser();
 JsonParser sequence = JsonParserSequence.createFlattened(parser1, parser2);
 assertEquals(2, sequence.containedParsersCount());
 }

 @Test
 public void testSwitchToNextParser() throws IOException {
 JsonParser parser1 = createTestParser();
 JsonParser parser2 = createTestParser();
 JsonParser sequence = JsonParserSequence.createFlattened(parser1, parser2);
 assertFalse(sequence.nextToken() != null);
 assertTrue(sequence.switchToNext());
 assertFalse(sequence.nextToken() != null);
 assertTrue(sequence.switchToNext());
 assertFalse(sequence.nextToken() != null);
 assertFalse(sequence.switchToNext());
 }

 @Test
 public void testJsonParserSequenceClose() throws IOException {
 JsonParser parser1 = createTestParser();
 JsonParser parser2 = createTestParser();
 JsonParser sequence = JsonParserSequence.createFlattened(parser1, parser2);
 sequence.close();
 assertFalse(parser1.isOpen());
 assertFalse(parser2.isOpen());
 }

 @Test
 public void testJsonParserSequenceNextToken() throws IOException {
 JsonParser parser1 = createTestParser();
 JsonParser parser2 = createTestParser();
 JsonParser sequence = JsonParserSequence.createFlattened(parser1, parser2);
 JsonToken token1 = sequence.nextToken();
 assertNull(token1);
 assertTrue(sequence.switchToNext());
 JsonToken token2 = sequence.nextToken();
 assertNull(token2);
 assertTrue(sequence.switchToNext());
 JsonToken token3 = sequence.nextToken();
 assertNull(token3);
 assertTrue(sequence.switchToNext());
 JsonToken token4 = sequence.nextToken();
 assertNull(token4);
 }

 @Test
 public void testJsonParserSequenceWithJsonNodes() throws IOException {
 JsonParser parser1 = new ObjectMapper().readerForValueType(JsonNode.class);
 JsonParser parser2 = new ObjectMapper().readerForValueType(JsonNode.class);
 JsonParser sequence = JsonParserSequence.createFlattened(parser1, parser2);
 JsonNode node1 = parser1.readValueAsTree("{ \"a\": 1 }");
 JsonNode node2 = parser2.readValueAsTree("{ \"b\": 2 }");
 sequence.nextToken(); // consume START_OBJECT
 assertSame(node1, sequence.nextValue());
 assertNull(sequence.nextToken()); // consume END_OBJECT
 assertTrue(sequence.switchToNext());
 assertSame(node2, sequence.nextValue());
 assertNull(sequence.nextToken()); // consume END_OBJECT
 }
}