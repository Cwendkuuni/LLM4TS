package com.fasterxml.jackson.core.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.io.IOException;
import java.util.*;

public class JsonParserSequenceTest {

 private JsonParser firstParser;
 private JsonParser secondParser;
 private JsonParser sequenceParser;

 @Before
 public void setUp() {
 firstParser = createMockParser("{ \"name\": \"John\" }");
 secondParser = createMockParser("{ \"age\": 30 }");
 sequenceParser = JsonParserSequence.createFlattened(firstParser, secondParser);
 }

 @After
 public void tearDown() throws IOException {
 firstParser.close();
 secondParser.close();
 sequenceParser.close();
 }

 @Test
 public void testCreateFlattened() throws IOException, JsonParseException {
 JsonParser expectedSequence = new JsonParserSequence(new JsonParser[] { firstParser, secondParser });
 JsonToken token;

 // Test nextToken()
 token = sequenceParser.nextToken();
 assertEquals(JsonToken.START_OBJECT, token.getId());
 // Test first parser
 firstParser.nextToken();
 firstParser.nextToken();
 assertEquals("name", firstParser.getCurrentName());

 // Test second parser
 token = sequenceParser.nextToken();
 assertEquals(JsonToken.FIELD_NAME, token.getId());
 assertEquals("age", sequenceParser.getCurrentName());

 // Test end of first parser
 firstParser.nextToken();
 firstParser.nextToken();
 firstParser.nextToken(); // End of first parser

 // Test end of second parser
 token = sequenceParser.nextToken();
 assertEquals(JsonToken.END_OBJECT, token.getId());
 // Test end of sequence
 sequenceParser.nextToken();
 sequenceParser.nextToken();
 sequenceParser.nextToken();
 assertNull(sequenceParser.nextToken());

 // Test containedParsersCount()
 assertEquals(2, sequenceParser.containedParsersCount());
 }

 @Test(expected = JsonParseException.class)
 public void testNextTokenWhenNoParsersLeft() throws IOException, JsonParseException {
 sequenceParser.nextToken();
 sequenceParser.nextToken();
 sequenceParser.nextToken();
 sequenceParser.nextToken();
 sequenceParser.nextToken();
 sequenceParser.nextToken();
 sequenceParser.nextToken();
 sequenceParser.nextToken();
 sequenceParser.nextToken();
 assertNull(sequenceParser.nextToken());
 sequenceParser.nextToken(); // Should throw JsonParseException
 }

 @Test
 public void testSwitchToNextParser() throws IOException, JsonParseException {
 JsonToken token;
 // Test first parser
 token = sequenceParser.nextToken();
 firstParser.nextToken();
 firstParser.nextToken();
 assertEquals("name", firstParser.getCurrentName());

 // Test switch to second parser
 token = sequenceParser.nextToken();
 assertEquals(JsonToken.FIELD_NAME, token.getId());
 assertEquals("age", sequenceParser.getCurrentName());

 // Test end of second parser
 secondParser.nextToken();
 secondParser.nextToken();
 secondParser.nextToken(); // End of second parser

 // Test switch back to first parser
 token = sequenceParser.nextToken();
 firstParser.nextToken();
 firstParser.nextToken();
 assertEquals("John", firstParser.getText());
 }

 private JsonParser createMockParser(String jsonString) throws IOException {
 Map<String, Object> jsonMap = new ObjectMapper().readValue(jsonString, Map.class);
 return new StringInputJsonParser(jsonMap.toString());
}
}