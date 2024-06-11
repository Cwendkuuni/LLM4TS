package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.ParserMinimalBase;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonParserSequenceTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @Test
 public void testJsonParserSequenceCreation() throws IOException {
 JsonParser parser1 = new ParserMinimalBase() {};
 JsonParser parser2 = new ParserMinimalBase() {};
 JsonParserSequence sequence = JsonParserSequence.createFlattened(parser1, parser2);
 assertEquals(2, sequence.containedParsersCount());
 }

 @Test
 public void testJsonParserSequenceNextToken() throws IOException {
 JsonParser parser1 = new ParserMinimalBase() {};
 JsonParser parser2 = new ParserMinimalBase() {};
 JsonParserSequence sequence = JsonParserSequence.createFlattened(parser1, parser2);
 JsonToken token1 = sequence.nextToken();
 assertNotNull(token1);
 JsonToken token2 = sequence.nextToken();
 assertNotNull(token2);
 }

 @Test
 public void testJsonParserSequenceClose() throws IOException {
 JsonParser parser1 = new ParserMinimalBase() {};
 JsonParser parser2 = new ParserMinimalBase() {};
 JsonParserSequence sequence = JsonParserSequence.createFlattened(parser1, parser2);
 sequence.close();
 parser1.close();
 parser2.close();
 }

 @Test(expected = IllegalStateException.class)
 public void testJsonParserSequenceInvalidCreation() throws IOException {
 JsonParser parser1 = new ParserMinimalBase() {};
 JsonParser parser2 = new ParserMinimalBase() {};
 JsonParserSequence sequence = new JsonParserSequence(new JsonParser[] { parser1, parser2 });
 sequence.close(); // Should not be able to call close on invalid sequence
 }

 @Test
 public void testJsonParserSequenceSwitchToNext() throws IOException {
 JsonParser parser1 = new ParserMinimalBase() {};
 JsonParser parser2 = new ParserMinimalBase() {};
 JsonParserSequence sequence = JsonParserSequence.createFlattened(parser1, parser2);
 assertTrue(sequence.switchToNext());
 assertTrue(sequence.delegate instanceof JsonParser);
 assertEquals(parser2, sequence.delegate);
 assertFalse(sequence.switchToNext());
 }

 @Test
 public void testJsonParserSequenceNestedSequences() throws IOException {
 JsonParser parser1 = new ParserMinimalBase() {};
 JsonParser parser2 = new ParserMinimalBase() {};
 JsonParser parser3 = new ParserMinimalBase() {};
 JsonParser parser4 = new ParserMinimalBase() {};
 JsonParserSequence sequence1 = JsonParserSequence.createFlattened(parser1, parser2);
 JsonParserSequence sequence2 = JsonParserSequence.createFlattened(parser3, parser4);
 JsonParser nestedSequence = JsonParserSequence.createFlattened(sequence1, sequence2);
 assertEquals(4, nestedSequence.containedParsersCount());
 }

}