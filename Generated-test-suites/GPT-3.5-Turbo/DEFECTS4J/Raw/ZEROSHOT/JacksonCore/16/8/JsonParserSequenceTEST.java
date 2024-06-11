package com.fasterxml.jackson.core.util;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.*;

import static org.mockito.Mockito.*;

public class JsonParserSequenceTest {

    @Mock
    private JsonParser[] parsers;

    private JsonParserSequence jsonParserSequence;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        jsonParserSequence = new JsonParserSequence(parsers);
    }

    @Test
    public void testClose() throws Exception {
        jsonParserSequence.close();
        for (JsonParser parser : parsers) {
            verify(parser).close();
        }
    }

    @Test
    public void testNextToken() throws Exception {
        JsonToken token1 = JsonToken.START_OBJECT;
        JsonToken token2 = JsonToken.FIELD_NAME;
        JsonToken token3 = JsonToken.VALUE_STRING;
        when(parsers[0].nextToken()).thenReturn(token1);
        when(parsers[1].nextToken()).thenReturn(token2);
        when(parsers[2].nextToken()).thenReturn(token3);

        JsonToken result1 = jsonParserSequence.nextToken();
        JsonToken result2 = jsonParserSequence.nextToken();
        JsonToken result3 = jsonParserSequence.nextToken();

        verify(parsers[0]).nextToken();
        verify(parsers[1]).nextToken();
        verify(parsers[2]).nextToken();
        verifyNoMoreInteractions(parsers);

        assertEquals(token1, result1);
        assertEquals(token2, result2);
        assertEquals(token3, result3);
    }

    @Test
    public void testContainedParsersCount() throws Exception {
        int expectedCount = 3;
        int actualCount = jsonParserSequence.containedParsersCount();
        assertEquals(expectedCount, actualCount);
    }

    @Test
    public void testSwitchToNext() throws Exception {
        when(parsers.length).thenReturn(3);
        jsonParserSequence.switchToNext();
        jsonParserSequence.switchToNext();
        jsonParserSequence.switchToNext();
        jsonParserSequence.switchToNext();

        verify(parsers[1]).nextToken();
        verify(parsers[2]).nextToken();
        verify(parsers[3]).nextToken();
        verifyNoMoreInteractions(parsers);
    }
}