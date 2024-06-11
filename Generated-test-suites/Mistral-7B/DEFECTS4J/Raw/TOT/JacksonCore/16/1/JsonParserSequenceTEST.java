package com.fasterxml.jackson.core.util;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Arrays;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class JsonParserSequenceTest {

 private JsonParserSequence sequence;
 private JsonParser parser1;
 private JsonParser parser2;
 private JsonParser parser3;

 @Before
 public void setup() {
 JsonParser[] parsers = new JsonParser[] { parser1 = mock(JsonParser.class), parser2 = mock(JsonParser.class), parser3 = mock(JsonParser.class) };
 sequence = new JsonParserSequence(parsers);
 }

 @Test
 public void testEmptySequence() throws IOException {
 assertEquals(0, sequence.containedParsersCount());
 assertNull(sequence.nextToken());
 sequence.close();
 }

 @Test
 public void testSingleParserSequence() throws IOException {
 // Test single parser sequence
 parser1.expects(once()).method("nextToken").andReturns(JsonToken.START_ARRAY);
 sequence = new JsonParserSequence(new JsonParser[] { parser1 });
 assertEquals(JsonToken.START_ARRAY, sequence.nextToken());
 parser1.verify();
 sequence.close();
 }

 @Test
 public void testTwoParsersSequence() throws IOException {
 // Test sequence with 2 parsers
 parser1.expects(once()).method("nextToken").andReturns(JsonToken.START_ARRAY);
 parser2.expects(once()).method("nextToken").andReturns(JsonToken.VALUE_STRING);
 sequence = new JsonParserSequence(new JsonParser[] { parser1, parser2 });
 assertEquals(JsonToken.START_ARRAY, sequence.nextToken());
 assertEquals(JsonToken.VALUE_STRING, sequence.nextToken());
 parser1.verify();
 parser2.verify();
 sequence.close();
 }

 @Test
 public void testThreeParsersSequence() throws IOException {
 // Test sequence with 3 parsers
 parser1.expects(once()).method("nextToken").andReturns(JsonToken.START_ARRAY);
 parser2.expects(once()).method("nextToken").andReturns(JsonToken.VALUE_STRING);
 parser3.expects(once()).method("nextToken").andReturns(JsonToken.VALUE_NUMBER_INT);
 sequence = new JsonParserSequence(new JsonParser[] { parser1, parser2, parser3 });
 assertEquals(JsonToken.START_ARRAY, sequence.nextToken());
 assertEquals(JsonToken.VALUE_STRING, sequence.nextToken());
 assertEquals(JsonToken.VALUE_NUMBER_INT, sequence.nextToken());
 parser1.verify();
 parser2.verify();
 parser3.verify();
 sequence.close();
 }

 @Test(expected = IllegalStateException.class)
 public void testParseError() throws IOException {
 // Test sequence with error parsing
 parser1.expects(once()).method("nextToken").andThrows(IOException.class);
 sequence = new JsonParserSequence(new JsonParser[] { parser1, parser2, parser3 });
 sequence.nextToken();
 }
}