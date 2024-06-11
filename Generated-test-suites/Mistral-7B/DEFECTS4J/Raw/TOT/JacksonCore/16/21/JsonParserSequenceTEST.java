package com.fasterxml.jackson.core.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import com.fasterxml.jackson.core.Base64Variants;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonParserSequenceTest {

 private JsonParser firstParser;
 private JsonParser secondParser;
 private JsonParser thirdParser;
 private JsonParserSequence sequence;

 @Before
 public void setUp() throws IOException {
 JsonFactory factory = new JsonFactory();
 firstParser = factory.createParser(new ByteArrayInputStream(new byte[] { '{'.charValue(), '}'.charValue()}));
 secondParser = factory.createParser(new ByteArrayInputStream(new byte[] { '['.charValue(), ']'.charValue()}));
 thirdParser = factory.createParser(new ByteArrayInputStream(new byte[] { '['.charValue(), '1'.charValue(), ']'.charValue()}));
 sequence = JsonParserSequence.createFlattened(firstParser, secondParser, thirdParser);
 }

 @Test
 public void testNextToken_ValidSequnce() throws IOException {
 assertEquals(JsonToken.START_OBJECT, sequence.nextToken());
 assertEquals(JsonToken.END_OBJECT, sequence.nextToken());
 assertEquals(JsonToken.START_ARRAY, sequence.nextToken());
 assertEquals(JsonToken.END_ARRAY, sequence.nextToken());
 assertEquals(JsonToken.VALUE_NUMBER_INT, sequence.nextToken());
 }

 @Test
 public void testNextToken_EmptyParserSequence() throws IOException {
 sequence = new JsonParserSequence(new JsonParser[0]);
 assertNull(sequence.nextToken());
 }

 @Test
 public void testNextToken_InvalidParserSequence() throws IOException {
 firstParser = null;
 sequence = JsonParserSequence.createFlattened(secondParser);
 assertNull(sequence.nextToken());
 }

 @Test
 public void testNextToken_InvalidParser_First() throws IOException {
 firstParser = new ObjectMapper().getFactory().createParser(null);
 sequence = JsonParserSequence.createFlattened(firstParser, secondParser);
 assertNull(sequence.nextToken());
 }

 @Test
 public void testNextToken_InvalidParser_Middle() throws IOException {
 secondParser = new ObjectMapper().getFactory().createParser(null);
 sequence = JsonParserSequence.createFlattened(firstParser, secondParser);
 assertEquals(JsonToken.START_OBJECT, sequence.nextToken());
 assertNull(sequence.nextToken());
 }

 @Test
 public void testNextToken_InvalidParser_Last() throws IOException {
 thirdParser = new ObjectMapper().getFactory().createParser(null);
 sequence = JsonParserSequence.createFlattened(firstParser, secondParser, thirdParser);
 assertEquals(JsonToken.START_OBJECT, sequence.nextToken());
 assertEquals(JsonToken.START_ARRAY, sequence.nextToken());
 assertNull(sequence.nextToken());
 }

 @Test
 public void testContainsdParsersCount() throws IOException {
 assertEquals(3, sequence.containedParsersCount());
 }

 @After
 public void tearDown() throws IOException {
 if (firstParser != null) firstParser.close();
 if (secondParser != null) secondParser.close();
 if (thirdParser != null) thirdParser.close();
 if (sequence != null) sequence.close();
 }
}