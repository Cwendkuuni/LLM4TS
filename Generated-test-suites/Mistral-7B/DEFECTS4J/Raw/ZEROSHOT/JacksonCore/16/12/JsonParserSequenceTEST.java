package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.core.*;

public class JsonParserSequenceTest {

 private static final JsonParser[] PARSERS = new JsonParser[] {
     new TestJsonParser("{\"name\":\"John\"}"),
     new TestJsonParser("{\"name\":\"Jane\"}")
 };

 @Test
 public void testCreateFlattened() throws IOException {
 JsonParser parser = JsonParserSequence.createFlattened(PARSERS[0], PARSERS[1]);
 assertEquals(PARSERS.length, parser.containedParsersCount());
 testNextToken(parser);
 }

 @Test
 public void testAddFlattenedActiveParsers() {
 List<JsonParser> result = new ArrayList<>();
 JsonParserSequence seq = new JsonParserSequence(new JsonParser[] { PARSERS[0], PARSERS[1] });
 seq.addFlattenedActiveParsers(result);
 assertEquals(PARSERS.length, result.size());
 }

 @Test
 public void testClose() throws IOException {
 JsonParser parser = JsonParserSequence.createFlattened(PARSERS[0], PARSERS[1]);
 parser.close();
 }

 @Test
 public void testNextToken() throws IOException, JsonParseException {
 JsonParser parser = JsonParserSequence.createFlattened(PARSERS[0], PARSERS[1]);
 testNextToken(parser);
 }

 @Test
 public void testSwitchToNext() throws IOException {
 JsonParser parser = JsonParserSequence.createFlattened(PARSERS[0], PARSERS[1]);
 for (int i = 0; i < parser.containedParsersCount() - 1; i++) {
 assertTrue(parser.switchToNext());
 }
 assertFalse(parser.switchToNext());
 }

 private void testNextToken(JsonParser parser) throws IOException, JsonParseException {
 JsonToken token = parser.nextToken();
 assertNotNull(token);
 assertEquals(JsonToken.START_OBJECT, token.getId());

 token = parser.nextToken();
 assertNotNull(token);
 assertEquals(JsonToken.FIELD_NAME, token.getId());
 assertEquals("name", token.getText());

 token = parser.nextToken();
 assertNotNull(token);
 assertEquals(JsonToken.VALUE_STRING, token.getId());
 assertEquals("John", token.getText());

 token = parser.nextToken();
 assertNotNull(token);
 assertEquals(JsonToken.FIELD_NAME, token.getId());
 assertEquals("name", token.getText());

 token = parser.nextToken();
 assertNotNull(token);
 assertEquals(JsonToken.VALUE_STRING, token.getId());
 assertEquals("Jane", token.getText());

 token = parser.nextToken();
 assertNull(token);
 }

 private static class TestJsonParser extends JsonParser {

 private TestJsonParser(String jsonString) throws IOException {
 super(new StringCharFeatures(new StringReader(jsonString)));
 }

 @Override
 public void close() throws IOException {
 // empty
 }
 }

}