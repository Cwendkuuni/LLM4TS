package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.ParserMinimalBase;
import com.fasterxml.jackson.core.json.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class JsonParserSequenceTest {

    private JsonParser firstParser;
    private JsonParser secondParser;
    private JsonParser thirdParser;
    private JsonParser sequence;

    @Before
    public void setup() {
        firstParser = mock(JsonParser.class);
        secondParser = mock(JsonParser.class);
        thirdParser = mock(JsonParser.class);
        when(firstParser.nextToken()).thenReturn(JsonToken.START_OBJECT);
        when(secondParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(thirdParser.nextToken()).thenReturn(JsonToken.END_OBJECT);

        sequence = JsonParserSequence.createFlattened(firstParser, secondParser, thirdParser);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateFlattened() {
        List<JsonParser> parsers = new ArrayList<>();
        parsers.add(firstParser);
        parsers.add(secondParser);
        parsers.add(thirdParser);
        JsonParser expectedSequence = new JsonParserSequence(parsers.toArray(new JsonParser[parsers.size()]));
        assertEquals(expectedSequence, sequence);
    }

    @Test
    public void testContainedParsersCount() {
        assertEquals(3, sequence.containedParsersCount());
    }

    @Test
    public void testNextToken() throws IOException {
        JsonToken actualToken;

        actualToken = sequence.nextToken();
        assertEquals(JsonToken.START_OBJECT, actualToken);

        actualToken = sequence.nextToken();
        assertEquals(JsonToken.VALUE_STRING, actualToken);

        actualToken = sequence.nextToken();
        assertEquals(JsonToken.END_OBJECT, actualToken);

        actualToken = sequence.nextToken();
        assertNull(actualToken);
    }

    @Test
    public void testSwitchToNext() throws IOException {
        boolean actualSwitchSucceeded;

        actualSwitchSucceeded = sequence.switchToNext();
        assertTrue(actualSwitchSucceeded);

        actualSwitchSucceeded = sequence.switchToNext();
        assertTrue(actualSwitchSucceeded);

        actualSwitchSucceeded = sequence.switchToNext();
        assertFalse(actualSwitchSucceeded);

        verify(firstParser).close();
        verify(secondParser).close();
        verify(thirdParser).close();
    }
}