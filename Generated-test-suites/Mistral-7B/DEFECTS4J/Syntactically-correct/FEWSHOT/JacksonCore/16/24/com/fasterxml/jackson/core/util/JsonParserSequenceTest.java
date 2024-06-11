package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.*;

public class JsonParserSequenceTest {

    JsonParser firstParserMock;
    JsonParser secondParserMock;
    JsonParser thirdParserMock;
    JsonParser sequence;

    @Before
    public void setUp() throws Exception {
        firstParserMock = mock(JsonParser.class);
        secondParserMock = mock(JsonParser.class);
        thirdParserMock = mock(JsonParser.class);
        sequence = new JsonParserSequence(new JsonParser[] { firstParserMock, secondParserMock, thirdParserMock });
    }

    @Test
    public void testCreateFlattened() throws IOException {
        assertEquals(3, sequence.containedParsersCount());
        JsonParser expectedSequence = new JsonParserSequence(new JsonParser[] { firstParserMock, secondParserMock });
        assertEquals(expectedSequence.getClass(), sequence.getClass());
        assertSame(expectedSequence, sequence);
    }

    @Test
    public void testSwitchToNextParser() throws IOException {
        when(firstParserMock.nextToken()).thenReturn(JsonToken.START_OBJECT);
        when(secondParserMock.nextToken()).thenReturn(JsonToken.FIELD_NAME);
        when(thirdParserMock.nextToken()).thenReturn(JsonToken.VALUE_STRING);

        JsonToken actualToken;
        int parserIndex = 0;

        actualToken = sequence.nextToken();
        assertEquals(JsonToken.START_OBJECT, actualToken);
        parserIndex++;

        actualToken = sequence.nextToken();
        assertEquals(JsonToken.FIELD_NAME, actualToken);
        parserIndex++;

        actualToken = sequence.nextToken();
        assertEquals(JsonToken.VALUE_STRING, actualToken);
        parserIndex++;

        assertTrue(sequence.switchToNext());
        parserIndex++;

        actualToken = sequence.nextToken();
        assertNull(actualToken);
        assertEquals(secondParserMock, sequence.delegate);
        parserIndex++;

        actualToken = sequence.nextToken();
        assertEquals(JsonToken.FIELD_NAME, actualToken);
        parserIndex++;

        actualToken = sequence.nextToken();
        assertEquals(JsonToken.START_ARRAY, actualToken);
        parserIndex++;

        assertTrue(sequence.switchToNext());
        parserIndex++;

        actualToken = sequence.nextToken();
        assertEquals(JsonToken.VALUE_NULL, actualToken);
        assertEquals(thirdParserMock, sequence.delegate);
        parserIndex++;

        assertFalse(sequence.switchToNext());
    }
}