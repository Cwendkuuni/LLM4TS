package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import org.junit.*;

import com.fasterxml.jackson.core.*;

public class JsonParserSequenceTest {

 private JsonParser[] parsers;
 private JsonParserSequence jsonParserSequence;

 @Before
 public void setUp() {
 JsonParser firstParser = new JsonParser();
 JsonParser secondParser = new JsonParser();
 parsers = new JsonParser[] { firstParser, secondParser };
 jsonParserSequence = new JsonParserSequence(parsers);
 }

 @Test
 public void testConstructor() {
 JsonParserSequence jsonParserSequence = JsonParserSequence.createFlattened(parsers[0], parsers[1]);
 assertEquals(parsers.length, jsonParserSequence.containedParsersCount());
 }

 @Test
 public void testNextTokenNormalCase() throws IOException {
 JsonToken token = jsonParserSequence.nextToken();
 assertNotNull(token);
 }

 @Test(expected = IOException.class)
 public void testNextTokenIOException() throws IOException {
 JsonParserIOException exception = new JsonParserIOException("Test Error Message");
 jsonParserSequence.delegate.getInputSource().getParserConfig().setErrorHandler(new JsonParserDefaultErrorHandler() {
 @Override
 public void parseStartElement(JsonParserParserContext ctx, String name) throws IOException {
 throw exception;
 }
 });
 JsonToken token = jsonParserSequence.nextToken();
 fail("IOException expected but not thrown");
 }

 @Test(expected = JsonParseException.class)
 public void testNextTokenJsonParseException() throws IOException {
 JsonParseException exception = new JsonParseException("Test Error Message");
 jsonParserSequence.delegate.getInputSource().getParserConfig().setErrorHandler(new JsonParserDefaultErrorHandler() {
 @Override
 public void parseStartElement(JsonParserParserContext ctx, String name) throws IOException {
 throw exception;
 }
 });
 JsonToken token = jsonParserSequence.nextToken();
 fail("JsonParseException expected but not thrown");
 }

 @Test
 public void testSwitchToNextParser() throws IOException {
 JsonToken token1 = jsonParserSequence.nextToken();
 jsonParserSequence.switchToNext();
 JsonToken token2 = jsonParserSequence.nextToken();
 assertNotSame(token1, token2);
 }

 @Test(expected = IllegalStateException.class)
 public void testSwitchToNextParserWhenNoMoreParser() throws IOException {
 jsonParserSequence.switchToNext();
 JsonToken token = jsonParserSequence.nextToken();
 fail("IllegalStateException expected but not thrown");
 }

 @Test
 public void testClose() throws IOException {
 jsonParserSequence.close();
 for (JsonParser parser : parsers) {
 assertEquals(JsonToken.CLOSE_ARRAY, parser.nextToken());
 }
 }

 @After
 public void tearDown() throws Exception {
 for (JsonParser parser : parsers) {
 parser.close();
 }
 }
}