package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class JsonParserSequenceTest {
    @Test
    public void testNextToken_SingleParser() throws IOException {
        JsonParser parser1 = mock(JsonParser.class);
        JsonParserSequence parserSequence = new JsonParserSequence(new JsonParser[] { parser1 });

        JsonToken token = parserSequence.nextToken();
        assertEquals(JsonToken.START_ARRAY, token);

        // Check if parser1 was called
        verify(parser1).nextToken();
    }

    @Test
    public void testNextToken_MultipleParsers() throws IOException {
        JsonParser parser1 = mock(JsonParser.class);
        JsonParser parser2 = mock(JsonParser.class);
        JsonParserSequence parserSequence = new JsonParserSequence(new JsonParser[] { parser1, parser2 });

        parserSequence.nextToken();
        parserSequence.nextToken();

        // Check if both parsers were called and in order
        inOrder(parser1, parser2).verify(parser1).nextToken();
        inOrder(parser1, parser2).verify(parser2).nextToken();
    }

    @Test
    public void testCreateFlattened_SingleParser() throws IOException {
        JsonParser parser1 = mock(JsonParser.class);
        JsonParser parser2 = mock(JsonParser.class);

        JsonParserSequence parserSequence = JsonParserSequence.createFlattened(parser1, parser2);

        assertEquals(2, parserSequence.containedParsersCount());
        assertEquals(parser1, parserSequence.getDelegate());
        verify(parser1).close();
        verifyNoMoreInteractions(parser1);
        verify(parser2).close();
        verifyNoMoreInteractions(parser2);
    }

    @Test
    public void testCreateFlattened_TwoParsers() throws IOException {
        JsonParser parser1 = mock(JsonParser.class);
        JsonParser parser2 = mock(JsonParser.class);
        JsonParser parser3 = mock(JsonParser.class);

        JsonParserSequence parserSequence = JsonParserSequence.createFlattened(parser1, parser2);
        parserSequence.add(parser3);

        assertEquals(3, parserSequence.containedParsersCount());
        assertEquals(parser1, parserSequence.getDelegate());
        verify(parser1).close();
        verifyNoMoreInteractions(parser1);
        verify(parser2).close();
        verifyNoMoreInteractions(parser2);
        verify(parser3).close();
        verifyNoMoreInteractions(parser3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateFlattened_InvalidParsers() throws IOException {
        JsonParser parser1 = mock(JsonParser.class);
        Object obj = new Object();

        JsonParserSequence parserSequence = JsonParserSequence.createFlattened(parser1, obj);
    }
}