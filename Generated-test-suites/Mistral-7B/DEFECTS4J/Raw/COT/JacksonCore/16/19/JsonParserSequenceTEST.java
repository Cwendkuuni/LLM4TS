package com.fasterxml.jackson.core.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class JsonParserSequenceTest {
    private JsonParser[] parsers;
    private JsonParser firstParser;
    private JsonParser secondParser;
    private JsonParserSequence jsonParserSequence;

    @Before
    public void setup() {
        firstParser = mock(JsonParser.class);
        secondParser = mock(JsonParser.class);
        parsers = new JsonParser[] { firstParser, secondParser };
        jsonParserSequence = new JsonParserSequence(parsers);
    }

    @Test
    public void testConstructor() {
        Assert.assertEquals(parsers.length, jsonParserSequence.containedParsersCount());
    }

    @Test
    public void testNextToken_FirstParserHasToken() throws Exception {
        when(firstParser.nextToken()).thenReturn(JsonToken.START_OBJECT);
        JsonToken token = jsonParserSequence.nextToken();
        assertEquals(JsonToken.START_OBJECT, token);
        verify(firstParser).nextToken();
        verify(secondParser, never()).nextToken();
    }

    @Test
    public void testNextToken_FirstParserHasNoToken() throws Exception {
        when(firstParser.nextToken()).thenReturn(null);
        jsonParserSequence.nextToken();
        verify(firstParser).nextToken();
        verify(secondParser).nextToken();
    }

    @Test
    public void testNextToken_AllParsersHaveNoToken() throws Exception {
        when(firstParser.nextToken()).thenReturn(null);
        when(secondParser.nextToken()).thenReturn(null);
        jsonParserSequence.nextToken();
        verify(firstParser).nextToken();
        verify(secondParser).nextToken();
    }

    @Test(expected = JsonParseException.class)
    public void testNextToken_AllParsersThrowException() throws Exception {
        when(firstParser.nextToken()).thenThrow(JsonParseException.class);
        when(secondParser.nextToken()).thenThrow(JsonParseException.class);
        jsonParserSequence.nextToken();
    }

    @Test
    public void testSwitchToNext() {
        jsonParserSequence.switchToNext();
        Assert.assertSame(secondParser, jsonParserSequence.delegate);
        Assert.assertNotSame(firstParser, jsonParserSequence.delegate);
    }

    @Test
    public void testCreateFlattened() {
        JsonParser firstParserSequence = mock(JsonParserSequence.class);
        given(firstParserSequence.delegate).willReturn(firstParser);
        JsonParser secondParserSequence = mock(JsonParserSequence.class);
        given(secondParserSequence.delegate).willReturn(secondParser);

        JsonParserSequence flattenedParserSequence = JsonParserSequence.createFlattened(firstParserSequence, secondParserSequence);
        Assert.assertEquals(3, flattenedParserSequence.containedParsersCount());
        Assert.assertNotSame(firstParserSequence, flattenedParserSequence.delegate);
        Assert.assertNotSame(secondParserSequence, flattenedParserSequence.delegate);
        Assert.assertSame(firstParser, ((JsonParserSequence) flattenedParserSequence.delegate)._parsers[0]);
        Assert.assertSame(secondParser, ((JsonParserSequence) flattenedParserSequence.delegate)._parsers[1]);
    }
}