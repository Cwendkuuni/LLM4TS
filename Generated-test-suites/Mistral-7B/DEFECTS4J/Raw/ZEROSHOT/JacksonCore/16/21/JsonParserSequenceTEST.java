package com.fasterxml.jackson.core.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class JsonParserSequenceTest {

 private JsonParser firstParser;
 private JsonParser secondParser;
 private JsonParser sequenceParser;

 @Before
 public void setup() {
 firstParser = mock(JsonParser.class);
 secondParser = mock(JsonParser.class);
 sequenceParser = new JsonParserSequence(new JsonParser[] {firstParser, secondParser});
 }

 @Test
 public void testCreateFlattened() throws Exception {
 JsonParser parserOne = JsonParserSequence.createFlattened(firstParser, secondParser);
 JsonParser expectedParser = new JsonParserSequence(new JsonParser[] {firstParser, secondParser});
 verify(firstParser).close();
 verify(secondParser).close();
 verify(firstParser).close();
 verify(secondParser).close();
 verify(firstParser).addFlattenedActiveParsers(Mockito.anyList());
 verify(secondParser).addFlattenedActiveParsers(Mockito.anyList());

 JsonParser expectedActiveParsers = Mockito.argThat(parsers ->  parsers.toString().equals("[Lcom.fasterxml.jackson.core.JsonParser;@" +
         "1b2d6522, Lcom.fasterxml.jackson.core.JsonParser;@" +
         "32d9a93d]"));

 verify(firstParser).addFlattenedActiveParsers(expectedActiveParsers);
 verify(secondParser).addFlattenedActiveParsers(expectedActiveParsers);

 assert(parserOne == expectedParser);
 }

 @Test
 public void testClose() throws Exception {
 sequenceParser.close();
 verify(firstParser).close();
 verify(secondParser).close();
 }

 @Test
 public void testNextToken() throws Exception {
 JsonToken firstToken = sequenceParser.nextToken();
 assert(firstToken != null);
 JsonToken secondToken = sequenceParser.nextToken();
 assert(secondToken != null);

 verify(firstParser).nextToken();
 verify(firstParser).nextToken();
 verify(secondParser).nextToken();
 verify(secondParser).nextToken();
 }

 @Test(expected = IllegalStateException.class)
 public void testNextTokenAfterAllParsersClosed() throws Exception {
 sequenceParser.close();
 sequenceParser.nextToken();
 }

 @Test
 public void testContainedParsersCount() throws Exception {
 assert(sequenceParser.containedParsersCount() == 2);
 }
}