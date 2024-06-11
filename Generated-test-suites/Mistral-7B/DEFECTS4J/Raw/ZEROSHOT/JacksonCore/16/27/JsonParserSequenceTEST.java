package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonParserSequenceTest {

 private JsonParser sequenceParser;
 private JsonParser parser1;
 private JsonParser parser2;
 private JsonParser parser3;

 @Before
 public void setUp() throws IOException {
 JsonFactory jsonFactory = new JsonFactory();
 parser1 = jsonFactory.createParser("{ \"a\": 1 }");
 parser2 = jsonFactory.createParser("{ \"b\": 2 }");
 parser3 = jsonFactory.createParser("{ \"c\": 3 }");
 sequenceParser = JsonParserSequence.createFlattened(parser1, parser2, parser3);
 }

 @Test
 public void testContainedParsersCount() throws IOException {
 assertEquals(3, sequenceParser.containedParsersCount());
 }

 @Test
 public void testClose() throws IOException {
 parser1.close();
 parser2.close();
 parser3.close();
 sequenceParser.close();
 }

 @Test
 public void testNextToken() throws IOException {
 JsonToken token = null;
 while (token == null) {
 token = sequenceParser.nextToken();
 }
 assertEquals(JsonToken.START_OBJECT, token);

 token = sequenceParser.nextToken();
 assertEquals(JsonToken.FIELD_NAME, token);
 assertEquals("a", sequenceParser.getCurrentName());

 token = sequenceParser.nextToken();
 assertEquals(JsonToken.VALUE_NUMBER_INT, token);
 assertEquals(1, sequenceParser.getValueAsInt());

 token = sequenceParser.nextToken();
 assertEquals(JsonToken.FIELD_NAME, token);
 assertEquals("b", sequenceParser.getCurrentName());

 token = sequenceParser.nextToken();
 assertEquals(JsonToken.VALUE_NUMBER_INT, token);
 assertEquals(2, sequenceParser.getValueAsInt());

 token = sequenceParser.nextToken();
 assertEquals(JsonToken.FIELD_NAME, token);
 assertEquals("c", sequenceParser.getCurrentName());

 token = sequenceParser.nextToken();
 assertEquals(JsonToken.VALUE_NUMBER_INT, token);
 assertEquals(3, sequenceParser.getValueAsInt());

 token = sequenceParser.nextToken();
 assertEquals(JsonToken.END_OBJECT, token);

 token = sequenceParser.nextToken();
 assertEquals(JsonToken.END_OBJECT, token); // should return END_OBJECT again
 }

 @Test(expected = IOException.class)
 public void testNextTokenWhenParsersAreEmpty() throws IOException {
 JsonParser emptySequenceParser = new JsonParserSequence(new JsonParser[0]);
 emptySequenceParser.nextToken();
 }
}