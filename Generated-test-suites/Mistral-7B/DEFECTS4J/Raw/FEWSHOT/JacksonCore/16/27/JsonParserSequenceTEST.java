package com.fasterxml.jackson.core.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JsonNode;

public class JsonParserSequenceTest {

    @Test
    public void testCreateFlattened() throws IOException {
        JsonParser parser1 = mock(JsonParser.class);
        JsonParser parser2 = mock(JsonParser.class);
        JsonParser parser3 = mock(JsonParser.class);

        JsonParserSequence sequence = JsonParserSequence.createFlattened(parser1, parser2);
        JsonParser[] containedParsers = sequence.getParsers();
        assertEquals(2, containedParsers.length);
        assertEquals(parser1, containedParsers[0]);
        assertEquals(parser2, containedParsers[1]);

        List<JsonParser> parsers = new ArrayList<>();
        JsonParserSequence sequence2 = JsonParserSequence.createFlattened(parser1, sequence);
        sequence2.addFlattenedActiveParsers(parsers);
        assertEquals(3, parsers.size());
        assertEquals(parser1, parsers.get(0));
        assertEquals(parser2, parsers.get(1));
        assertEquals(sequence, parsers.get(2));

        parser3 = mock(JsonParser.class);
        JsonParserSequence sequence3 = JsonParserSequence.createFlattened(parser1, parser3);
        JsonParser[] containedParsers3 = sequence3.getParsers();
        assertEquals(2, containedParsers3.length);
        assertEquals(parser1, containedParsers3[0]);
        assertEquals(parser3, containedParsers3[1]);

        List<JsonParser> parsers3 = new ArrayList<>();
        JsonParserSequence sequence4 = JsonParserSequence.createFlattened(sequence, parser3);
        sequence4.addFlattenedActiveParsers(parsers3);
        assertEquals(4, parsers3.size());
        assertEquals(parser1, parsers3.get(0));
        assertEquals(sequence, parsers3.get(1));
        assertEquals(parser3, parsers3.get(2));
        assertEquals(parser3, parsers3.get(3));
    }

    @Test
    public void testNextToken() throws IOException {
        JsonParser parser1 = mock(JsonParser.class);
        JsonParser parser2 = mock(JsonParser.class);

        when(parser1.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(parser2.nextToken()).thenReturn(JsonToken.VALUE_NUMBER_INT);

        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[] { parser1, parser2 });
        assertEquals(JsonToken.START_ARRAY, sequence.nextToken());
        assertEquals(JsonToken.VALUE_NUMBER_INT, sequence.nextToken());
        assertNull(sequence.nextToken());

        JsonParser[] containedParsers = sequence.getParsers();
        assertEquals(2, containedParsers.length);
        assertEquals(parser1, containedParsers[0]);
        assertEquals(parser2, containedParsers[1]);
    }

    @Test
    public void testClose() throws IOException {
        JsonParser parser1 = mock(JsonParser.class);
        JsonParser parser2 = mock(JsonParser.class);

        when(parser1.close()).thenReturn(null);
        when(parser2.close()).thenThrow(IOException.class);

        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[] { parser1, parser2 });
        sequence.close();

        JsonParser[] containedParsers = sequence.getParsers();
        assertEquals(2, containedParsers.length);
        assertEquals(parser1, containedParsers[0]);
        assertEquals(parser2, containedParsers[1]);

        sequence.nextToken();
        sequence.nextToken();
        sequence.nextToken();
        sequence.close();

        verify(parser1).close();
        verify(parser2).close();
    }

    @Test
    public void testContainedParsersCount() throws IOException {
        JsonParser parser1 = mock(JsonParser.class);
        JsonParser parser2 = mock(JsonParser.class);
        JsonParser parser3 = mock(JsonParser.class);

        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[] { parser1, parser2, parser3 });
        assertEquals(3, sequence.containedParsersCount());
    }

    @Test
    public void testSwitchToNext() throws IOException {
        JsonParser parser1 = mock(JsonParser.class);
        JsonParser parser2 = mock(JsonParser.class);

        when(parser1.nextToken()).thenReturn(JsonToken.START_OBJECT);
        when(parser2.nextToken()).thenReturn(JsonToken.START_ARRAY);

        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[] { parser1, parser2 });
        assertEquals(JsonToken.START_OBJECT, sequence.nextToken());
        assertTrue(sequence.switchToNext());
        assertEquals(JsonToken.START_ARRAY, sequence.nextToken());
        assertFalse(sequence.switchToNext());
    }

    @Test
    public void testSwitchToNextAfterNullToken() throws IOException {
        JsonParser parser1 = mock(JsonParser.class);
        JsonParser parser2 = mock(JsonParser.class);

        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[] { parser1, parser2 });
        assertEquals(JsonToken.NULL, sequence.nextToken());
        assertTrue(sequence.switchToNext());
        assertNull(sequence.nextToken());
    }

    @Test
    public void testSwitchToNextAfterEndOfStream() throws IOException {
        JsonParser parser1 = mock(JsonParser.class);
        JsonParser parser2 = mock(JsonParser.class);

        when(parser1.nextToken()).thenReturn(JsonToken.END_ARRAY);
        when(parser2.nextToken()).thenReturn(JsonToken.START_OBJECT);

        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[] { parser1, parser2 });
        assertEquals(JsonToken.END_ARRAY, sequence.nextToken());
        assertTrue(sequence.switchToNext());
        assertEquals(JsonToken.START_OBJECT, sequence.nextToken());
        assertFalse(sequence.switchToNext());
    }

    @Test
    public void testSwitchToNextAfterException() throws IOException {
        JsonParser parser1 = mock(JsonParser.class);
        JsonParser parser2 = mock(JsonParser.class);

        when(parser1.nextToken()).thenReturn(JsonToken.START_OBJECT);
        when(parser1.nextToken()).thenThrow(JsonParseException.class);

        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[] { parser1, parser2 });
        assertEquals(JsonToken.START_OBJECT, sequence.nextToken());
        assertEquals(JsonToken.START_OBJECT, sequence.nextToken());
        assertTrue(sequence.switchToNext());
        assertEquals(JsonToken.START_ARRAY, sequence.nextToken());
        assertFalse(sequence.switchToNext());
    }

    @Test
    public void testSwitchToNextAfterIOException() throws IOException {
        JsonParser parser1 = mock(JsonParser.class);
        JsonParser parser2 = mock(JsonParser.class);

        when(parser1.nextToken()).thenReturn(JsonToken.START_OBJECT);
        when(parser1.nextToken()).thenThrow(IOException.class);

        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[] { parser1, parser2 });
        assertEquals(JsonToken.START_OBJECT, sequence.nextToken());
        assertEquals(JsonToken.START_OBJECT, sequence.nextToken());
        assertTrue(sequence.switchToNext());
        assertEquals(JsonToken.START_ARRAY, sequence.nextToken());
        assertFalse(sequence.switchToNext());
    }

    @Test
    public void testSwitchToNextAfterUnknownToken() throws IOException {
        JsonParser parser1 = mock(JsonParser.class);
        JsonParser parser2 = mock(JsonParser.class);

        when(parser1.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(parser2.nextToken()).thenReturn(JsonToken.START_ARRAY);

        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[] { parser1, parser2 });
        assertEquals(JsonToken.VALUE_STRING, sequence.nextToken());
        assertEquals(JsonToken.START_ARRAY, sequence.nextToken());
        assertFalse(sequence.switchToNext());
    }

    @Test
    public void testSwitchToNextAfterEOF() throws IOException {
        JsonParser parser1 = mock(JsonParser.class);
        JsonParser parser2 = mock(JsonParser.class);

        when(parser1.nextToken()).thenReturn(JsonToken.END_OBJECT);
        when(parser1.nextToken()).thenThrow(IOException.class);

        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[] { parser1, parser2 });
        assertEquals(JsonToken.END_OBJECT, sequence.nextToken());
        assertEquals(JsonToken.END_OBJECT, sequence.nextToken());
        assertTrue(sequence.switchToNext());
        assertEquals(JsonToken.START_ARRAY, sequence.nextToken());
        assertFalse(sequence.switchToNext());
    }

    @Test
    public void testSwitchToNextAfterNullParser() throws IOException {
        JsonParser parser1 = mock(JsonParser.class);
        JsonParser parser2 = null;

        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[] { parser1, parser2 });
        assertEquals(JsonToken.START_OBJECT, sequence.nextToken());
        assertFalse(sequence.switchToNext());
    }

    @Test
    public void testSwitchToNextAfterEmptyArray() throws IOException {
        JsonParser parser1 = mock(JsonParser.class);
        JsonParser parser2 = mock(JsonParser.class);

        when(parser1.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(parser1.nextToken()).thenThrow(IOException.class);

        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[] { parser1, parser2 });
        assertEquals(JsonToken.START_ARRAY, sequence.nextToken());
        assertEquals(JsonToken.START_ARRAY, sequence.nextToken());
        assertTrue(sequence.switchToNext());
        assertNull(sequence.nextToken());
        assertFalse(sequence.switchToNext());
    }

    @Test
    public void testJsonNodeEventWithJsonParserSequence() throws IOException {
        JsonParser parser1 = mock(JsonParser.class);
        JsonParser parser2 = mock(JsonParser.class);

        when(parser1.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(parser1.nextToken()).thenThrow(IOException.class);
        when(parser2.nextToken()).thenReturn(JsonToken.START_OBJECT);
        when(parser2.nextToken()).thenReturn(JsonToken.FIELD_NAME);
        when(parser2.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(parser2.nextToken()).thenReturn(JsonToken.END_OBJECT);

        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[] { parser1, parser2 });
        JsonNode rootNode = sequence.readValueAsTree();
        JsonNode arrayNode = rootNode.get(0);
        JsonNode objectNode = arrayNode.get(0);
        assertEquals("fieldName", objectNode.findValueOrNull("name"));
        assertEquals("valueString", objectNode.get("value").asText());
    }

    @Test
    public void testJsonNodeEventWithJsonParserSequenceEndOfStream() throws IOException {
        JsonParser parser1 = mock(JsonParser.class);
        JsonParser parser2 = mock(JsonParser.class);

        when(parser1.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(parser1.nextToken()).thenThrow(IOException.class);
        when(parser2.nextToken()).thenReturn(JsonToken.START_OBJECT);
        when(parser2.nextToken()).thenReturn(JsonToken.FIELD_NAME);
        when(parser2.nextToken()).thenReturn(JsonToken.END_OBJECT);
        when(parser2.nextToken()).thenReturn(null);

        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[] { parser1, parser2 });
        JsonNode rootNode = sequence.readValueAsTree();
        JsonNode arrayNode = rootNode.get(0);
        JsonNode objectNode = arrayNode.get(0);
        assertEquals("fieldName", objectNode.findValueOrNull("name"));
        assertEquals("valueString", objectNode.get("value").asText());
        assertFalse(sequence.hasNext());
    }
}