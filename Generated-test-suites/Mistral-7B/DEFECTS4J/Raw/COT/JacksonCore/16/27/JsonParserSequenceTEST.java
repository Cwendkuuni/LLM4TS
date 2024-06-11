package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.core.*;

public class JsonParserSequenceTest {

 private JsonParser[] createParsers() {
 return new JsonParser[] { createMockParser(), createMockParser() };
 }

 @Test
 public void testJsonParserSequenceConstruction() {
 JsonParserSequence parserSequence = new JsonParserSequence(createParsers());
 assertEquals(2, parserSequence.containedParsersCount());
 }

 @Test
 public void testNextTokenNormalFlow() throws IOException {
 JsonParserSequence parserSequence = new JsonParserSequence(createParsers());
 JsonToken token = parserSequence.nextToken();
 assertNotNull(token);
 // First parser should be used
 assertEquals(parserSequence.delegate.getClass(), parserSequence.getDelegate().getClass());
 }

 @Test
 public void testNextTokenSwitchToSecondParser() throws IOException {
 JsonParserSequence parserSequence = new JsonParserSequence(createParsers());
 JsonToken token = parserSequence.nextToken();
 assertNotNull(token);
 // First parser should be used
 assertEquals(parserSequence.delegate.getClass(), parserSequence.getDelegate().getClass());
 token = parserSequence.nextToken();
 assertNotNull(token);
 // Second parser should be used now
 assertNotSame(parserSequence.delegate, parserSequence.getDelegate());
 assertSame(parserSequence.getDelegate().getClass(), parserSequence.getParsers()[1].getClass());
 }

 @Test(expected = IOException.class)
 public void testNextTokenExceptionThrownByFirstParser() throws IOException {
 JsonParserSequence parserSequence = new JsonParserSequence(createExceptionThrowingParsers());
 JsonToken token = parserSequence.nextToken();
 assertNull(token);
 }

 @Test
 public void testNextTokenExceptionThrownBySecondParser() throws IOException {
 JsonParserSequence parserSequence = new JsonParserSequence(createExceptionThrowingParsers());
 JsonToken token = parserSequence.nextToken();
 assertNotNull(token);
 // First parser should be used
 assertEquals(parserSequence.delegate.getClass(), parserSequence.getDelegate().getClass());
 token = parserSequence.nextToken();
 assertNotNull(token);
 // Second parser should be used now
 assertNotSame(parserSequence.delegate, parserSequence.getDelegate());
 assertSame(parserSequence.getDelegate().getClass(), parserSequence.getParsers()[1].getClass());
 token = parserSequence.nextToken();
 throw new RuntimeException("Unexpected token: " + token.getClass().getName());
 }

 @Test
 public void testCreateFlattened() throws IOException {
 JsonParser firstParser = createMockParser();
 JsonParser secondParser = createMockParser();
 JsonParser sequence = JsonParserSequence.createFlattened(firstParser, secondParser);
 JsonParser parsedParser = sequence.nextToken() == null ? sequence.delegate : sequence.getDelegate();
 assertSame(parsedParser.getClass(), secondParser.getClass());
 }

 @Test
 public void testAddFlattenedActiveParsers() {
 JsonParser firstParser = createMockParser();
 JsonParser secondParser = createMockParser();
 JsonParser sequence = new JsonParserSequence(new JsonParser[] { firstParser, secondParser });
 List<JsonParser> parsers = new ArrayList<JsonParser>();
 sequence.addFlattenedActiveParsers(parsers);
 assertEquals(2, parsers.size());
 assertEquals(parsers.get(0).getClass(), firstParser.getClass());
 assertEquals(parsers.get(1).getClass(), secondParser.getClass());
 }

 @Test
 public void testClose() throws IOException {
 JsonParserSequence parserSequence = new JsonParserSequence(createParsers());
 parserSequence.close();
 for (JsonParser parser : parserSequence._parsers) {
 parser.close();
 }
 }

 private JsonParser createMockParser() {
 return mock(JsonParser.class);
 }

 private JsonParser[] createExceptionThrowingParsers() {
 JsonParser[] parsers = new JsonParser[2];
 for (int i = 0; i < parsers.length; i++) {
 try {
 parsers[i] = mock(JsonParser.class);
 when(parsers[i].nextToken()).thenThrow(new IOException("Test exception"));
 } catch (IOException e) {
 // Ignore
 }
 }
 return parsers;
 }

}