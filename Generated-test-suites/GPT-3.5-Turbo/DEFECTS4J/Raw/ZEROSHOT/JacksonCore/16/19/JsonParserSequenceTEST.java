package com.fasterxml.jackson.core.util;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class JsonParserSequenceTest {

    @Mock
    private JsonParser[] parsers;

    private JsonParserSequence parserSequence;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        parserSequence = new JsonParserSequence(parsers);
    }

    @Test
    public void containedParsersCount_shouldReturnCorrectNumberOfParsers() {
        when(parsers.length).thenReturn(3);
        int result = parserSequence.containedParsersCount();
        assertEquals(3, result);
    }

    @Test
    public void close_shouldCloseAllParsers() throws IOException {
        for (JsonParser parser : parsers) {
            doNothing().when(parser).close();
        }
        parserSequence.close();
        for (JsonParser parser : parsers) {
            verify(parser).close();
        }
    }

    @Test
    public void nextToken_whenNextTokenNotNull_shouldReturnNextToken() throws IOException, JsonParseException {
        JsonToken token = mock(JsonToken.class);
        when(parsers[0].nextToken()).thenReturn(token);

        JsonToken result = parserSequence.nextToken();

        assertEquals(token, result);
    }

    @Test
    public void nextToken_whenNextTokenIsNullAndSwitchToNextReturnsFalse_shouldReturnNull() throws IOException, JsonParseException {
        when(parsers[0].nextToken()).thenReturn(null);
        when(parsers.length).thenReturn(1);
        boolean result = parserSequence.nextToken() == null;
        assertTrue(result);
    }

    @Test
    public void nextToken_whenNextTokenIsNullAndSwitchToNextReturnsTrue_shouldKeepSwitchingAndReturnNextToken() throws IOException, JsonParseException {
        JsonToken token = mock(JsonToken.class);
        when(parsers[0].nextToken()).thenReturn(null);
        when(parsers[1].nextToken()).thenReturn(token);

        JsonToken result = parserSequence.nextToken();

        assertEquals(token, result);
    }
}