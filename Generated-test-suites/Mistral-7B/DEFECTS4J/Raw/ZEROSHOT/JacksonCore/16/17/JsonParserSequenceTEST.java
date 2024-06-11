package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class JsonParserSequenceTest {

 private JsonParser[] parsers;
 private JsonParserSequence parserSequence;

 @Before
 public void setup() {
 JsonParser parser1 = setupParser("{ \"a\": 1 }");
 JsonParser parser2 = setupParser("{ \"b\": 2 }");
 JsonParser parser3 = setupParser("{ \"c\": 3 }");
 parsers = new JsonParser[] { parser1, parser2, parser3 };
 parserSequence = new JsonParserSequence(parsers);
 }

 @Test
 public void testCreateFlattened() throws IOException {
 JsonParser parser12 = JsonParserSequence.createFlattened(parsers[0], parsers[1]);
 assertEquals(3, parser12.containedParsersCount());
 JsonParser parser23 = JsonParserSequence.createFlattened(parsers[1], parsers[2]);
 assertEquals(3, parser23.containedParsersCount());
 JsonParser parser123 = JsonParserSequence.createFlattened(parsers[0], parsers[1], parsers[2]);
 assertEquals(3, parser123.containedParsersCount());
 }

 @Test
 public void testNextToken() throws IOException, JsonParseException {
 JsonToken token = parserSequence.nextToken();
 assertEquals(JsonToken.START_OBJECT, token.getId());
 token = parserSequence.nextToken();
 assertEquals(JsonToken.FIELD_NAME, token.getId());
 assertEquals("a", token.getText());
 token = parserSequence.nextToken();
 assertEquals(JsonToken.VALUE_NUMBER_INT, token.getId());
 assertEquals(1, token.asInt());
 token = parserSequence.nextToken();
 assertEquals(JsonToken.FIELD_NAME, token.getId());
 assertEquals("b", token.getText());
 token = parserSequence.nextToken();
 assertEquals(JsonToken.VALUE_NUMBER_INT, token.getId());
 assertEquals(2, token.asInt());
 token = parserSequence.nextToken();
 assertEquals(JsonToken.FIELD_NAME, token.getId());
 assertEquals("c", token.getText());
 token = parserSequence.nextToken();
 assertEquals(JsonToken.VALUE_NUMBER_INT, token.getId());
 assertEquals(3, token.asInt());
 token = parserSequence.nextToken();
 assertNull(token);
 }

 @Test
 public void testClose() throws IOException {
 parserSequence.close();
 for (JsonParser parser : parsers) {
 parser.close();
 }
 }

 private JsonParser setupParser(String json) throws IOException {
 StringResource.ByteArray resource = new StringResource.ByteArray(json.getBytes());
 return new ObjectMapper().readerForTree(resource);
}

}