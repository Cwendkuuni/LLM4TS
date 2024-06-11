package com.fasterxml.jackson.core.util;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import com.fasterxml.jackson.core.*;

public class JsonParserSequenceTest {

    private JsonParserSequence jsonParserSequence;
    private JsonParser parser;
    private JsonParser[] parsers;

    @Before
    public void setUp() {
        parser = mock(JsonParser.class);
        parsers = new JsonParser[] { parser };
        jsonParserSequence = new JsonParserSequence(parsers);
    }

    @Test
    public void testClose() throws IOException {
        jsonParserSequence.close();
        verify(parser).close();
    }

    @Test
    public void testNextToken() throws IOException, JsonParseException {
        JsonToken token = mock(JsonToken.class);
        when(parser.nextToken()).thenReturn(token);

        JsonToken actualToken = jsonParserSequence.nextToken();
        
        verify(parser).nextToken();
        assertEquals(token, actualToken);
    }

    @Test
    public void testContainedParsersCount() {
        int count = jsonParserSequence.containedParsersCount();
        assertEquals(parsers.length, count);
    }

    @Test
    public void testSwitchToNext_SuccessfulSwitch() {
        int nextParser = 1;
        jsonParserSequence.switchToNext();
        int actualNextParser = jsonParserSequence.getSwitchedNextParser();
        assertEquals(nextParser, actualNextParser);
    }

    @Test
    public void testSwitchToNext_UnsuccessfulSwitch() {
        int nextParser = 1;
        jsonParserSequence.switchToNext();
        jsonParserSequence.switchToNext();
        int actualNextParser = jsonParserSequence.getSwitchedNextParser();
        assertEquals(nextParser, actualNextParser);
    }
}