package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Arrays;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.util.JsonParserSequence;
import com.fasterxml.jackson.databind.JsonNode;

public class JsonParserSequenceTest {

    JsonParser mockParser1, mockParser2, mockParser3;

    @Before
    public void setUp() {
        mockParser1 = mock(JsonParser.class);
        mockParser2 = mock(JsonParser.class);
        mockParser3 = mock(JsonParser.class);
    }

    @Test
    public void testCreateFlattened() throws IOException {
        // Test with single parsers
        JsonParser parser1 = new JsonParser() {};
        JsonParser parser2 = new JsonParser() {};
        JsonParserSequence sequence = JsonParserSequence.createFlattened(parser1, parser2);
        assertEquals(2, sequence.containedParsersCount());

        // Test with parser sequences
        JsonParserSequence parserSequence1 = new JsonParserSequence(new JsonParser[] { mockParser1, mockParser2 });
        JsonParserSequence parserSequence2 = new JsonParserSequence(new JsonParser[] { mockParser2, mockParser3 });
        JsonParserSequence finalSequence = JsonParserSequence.createFlattened(parserSequence1, parserSequence2);
        assertEquals(5, finalSequence.containedParsersCount());

        // Test that contained parsers are in correct order
        JsonParser[] expectedParsers = { mockParser1, mockParser2, mockParser3 };
        JsonParser[] actualParsers = Arrays.copyOf(finalSequence._parsers, finalSequence._parsers.length);
        assertArrayEquals(expectedParsers, actualParsers);
    }

    @Test
    public void testSwitchToNext() throws IOException {
        when(mockParser1.nextToken()).thenReturn(JsonToken.START_OBJECT);
        when(mockParser2.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(mockParser3.nextToken()).thenReturn(JsonToken.END_OBJECT);

        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[] { mockParser1, mockParser2, mockParser3 });
        JsonToken token;

        token = sequence.nextToken();
        assertEquals(JsonToken.START_OBJECT, token);
        verify(mockParser1).nextToken();

        token = sequence.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token);
        verify(mockParser2).nextToken();

        token = sequence.nextToken();
        assertEquals(JsonToken.END_OBJECT, token);
        verify(mockParser3).nextToken();

        // Test that all parsers have been used
        token = sequence.nextToken();
        assertNull(token);
        verify(mockParser1).close();
        verify(mockParser2).close();
        verify(mockParser3).close();
    }

    @Test
    public void testClose() throws IOException {
        when(mockParser1.nextToken()).thenReturn(JsonToken.START_OBJECT);
        when(mockParser2.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(mockParser3.nextToken()).thenReturn(JsonToken.END_OBJECT);

        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[] { mockParser1, mockParser2, mockParser3 });
        sequence.close();

        verify(mockParser1).close();
        verify(mockParser2).close();
        verify(mockParser3).close();
    }

    @Test
    public void testNextToken() throws IOException {
        when(mockParser1.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(mockParser2.nextToken()).thenReturn(JsonToken.VALUE_STRING);

        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[] { mockParser1, mockParser2 });
        JsonToken token = sequence.nextToken();
        assertEquals(JsonToken.START_ARRAY, token);
        verify(mockParser1).nextToken();

        token = sequence.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token);
        verify(mockParser2).nextToken();

        token = sequence.nextToken();
        assertNull(token);
        verify(mockParser1).close();
        verify(mockParser2).close();
    }
}