package com.fasterxml.jackson.core.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.BaseTest;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.util.JsonParserSequence;

public class JsonParserSequenceTest extends BaseTest {

 private JsonParser firstParserMock;
 private JsonParser secondParserMock;
 private JsonParser thirdParserMock;

 @Test
 public void testCreateFlattened() throws IOException {
 JsonParser parserSequence = JsonParserSequence.createFlattened(firstParserMock, secondParserMock);
 assertEquals(2, parserSequence.containedParsersCount());
 }

 @Test
 public void testCreateFlattenedWithSingleParserAsFirstArgument() throws IOException {
 JsonParser parserSequence = JsonParserSequence.createFlattened(firstParserMock, firstParserMock);
 assertEquals(1, parserSequence.containedParsersCount());
 }

 @Test
 public void testCreateFlattenedWithSequenceAsFirstArgument() throws IOException {
 JsonParser sequenceFirstParserMock = mock(JsonParser.class);
 JsonParser sequenceSecondParserMock = mock(JsonParser.class);
 JsonParser[] sequenceParsers = new JsonParser[] { sequenceFirstParserMock, sequenceSecondParserMock };
 JsonParser parserSequence = JsonParserSequence.createFlattened(sequenceParsers[0], secondParserMock);
 assertEquals(2, parserSequence.containedParsersCount());
 }

 @Test
 public void testNextToken_oneParser() throws IOException {
 firstParserMock = mock(JsonParser.class);
 when(firstParserMock.nextToken()).thenReturn(JsonToken.START_OBJECT);
 JsonParser parserSequence = new JsonParserSequence(new JsonParser[] { firstParserMock });
 assertEquals(JsonToken.START_OBJECT, parserSequence.nextToken());
 }

 @Test
 public void testNextToken_multipleParsers() throws IOException {
 firstParserMock = mock(JsonParser.class);
 secondParserMock = mock(JsonParser.class);
 when(firstParserMock.nextToken()).thenReturn(JsonToken.START_OBJECT);
 when(secondParserMock.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 JsonParser parserSequence = new JsonParserSequence(new JsonParser[] { firstParserMock, secondParserMock });
 assertEquals(JsonToken.START_OBJECT, parserSequence.nextToken());
 assertEquals(JsonToken.VALUE_STRING, parserSequence.nextToken());
 }

 @Test
 public void testNextToken_allParsersEmpty() throws IOException {
 firstParserMock = mock(JsonParser.class);
 secondParserMock = mock(JsonParser.class);
 thirdParserMock = mock(JsonParser.class);
 JsonParser parserSequence = new JsonParserSequence(new JsonParser[] { firstParserMock, secondParserMock, thirdParserMock });
 assertNull(parserSequence.nextToken());
 assertNull(parserSequence.nextToken());
 assertNull(parserSequence.nextToken());
 }

 @Test
 public void testNextToken_allParsersHaveNullToken() throws IOException {
 firstParserMock = mock(JsonParser.class);
 secondParserMock = mock(JsonParser.class);
 thirdParserMock = mock(JsonParser.class);
 when(firstParserMock.nextToken()).thenReturn(null);
 when(secondParserMock.nextToken()).thenReturn(null);
 when(thirdParserMock.nextToken()).thenReturn(null);
 JsonParser parserSequence = new JsonParserSequence(new JsonParser[] { firstParserMock, secondParserMock, thirdParserMock });
 assertNull(parserSequence.nextToken());
 assertNull(parserSequence.nextToken());
 assertNull(parserSequence.nextToken());
 }

 @Test
 public void testNextToken_firstParserHasNullToken_switchToSecondParser() throws IOException {
 firstParserMock = mock(JsonParser.class);
 secondParserMock = mock(JsonParser.class);
 when(firstParserMock.nextToken()).thenReturn(null);
 when(secondParserMock.nextToken()).thenReturn(JsonToken.VALUE_STRING);
 JsonParser parserSequence = new JsonParserSequence(new JsonParser[] { firstParserMock, secondParserMock });
 assertNull(parserSequence.nextToken());
 assertEquals(JsonToken.VALUE_STRING, parserSequence.nextToken());
 }

 @Test
 public void testNextToken_firstParserHasToken_keepUsingIt() throws IOException {
 firstParserMock = mock(JsonParser.class);
 when(firstParserMock.nextToken()).thenReturn(JsonToken.START_OBJECT);
 JsonParser parserSequence = new JsonParserSequence(new JsonParser[] { firstParserMock });
 assertEquals(JsonToken.START_OBJECT, parserSequence.nextToken());
 assertEquals(JsonToken.START_OBJECT, parserSequence.nextToken());
 }

 @Test
 public void testClose() throws IOException {
 firstParserMock = mock(JsonParser.class);
 secondParserMock = mock(JsonParser.class);
 when(firstParserMock.close()).thenReturn(null);
 when(secondParserMock.close()).thenReturn(null);
 JsonParser parserSequence = new JsonParserSequence(new JsonParser[] { firstParserMock, secondParserMock });
 parserSequence.close();
 }

 @Test(expected = IOException.class)
 public void testClose_oneParserThrowsIOException() throws IOException {
 firstParserMock = mock(JsonParser.class);
 when(firstParserMock.close()).thenThrow(new IOException());
 JsonParser parserSequence = new JsonParserSequence(new JsonParser[] { firstParserMock });
 parserSequence.close();
 }

 @Test(expected = IOException.class)
 public void testClose_multipleParsersThrowIOException() throws IOException {
 firstParserMock = mock(JsonParser.class);
 secondParserMock = mock(JsonParser.class);
 when(firstParserMock.close()).thenThrow(new IOException());
 when(secondParserMock.close()).thenThrow(new IOException());
 JsonParser parserSequence = new JsonParserSequence(new JsonParser[] { firstParserMock, secondParserMock });
 parserSequence.close();
 }
}