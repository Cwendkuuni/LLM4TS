package com.fasterxml.jackson.core.util;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonToken;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.mockito.Mockito.*;

public class JsonParserSequenceTest {

    private JsonParserSequence jsonParserSequence;
    private JsonParser[] parsers;

    @Before
    public void setUp() {
        parsers = new JsonParser[2];
        parsers[0] = mock(JsonParser.class);
        parsers[1] = mock(JsonParser.class);
        jsonParserSequence = new JsonParserSequence(parsers);
    }

    @Test
    public void testClose() throws IOException {
        doReturn(true).when(parsers[0]).isActiveParser();
        doReturn(false).when(parsers[1]).isActiveParser();
        
        jsonParserSequence.close();
        
        verify(parsers[0], times(1)).close();
        verify(parsers[1], times(0)).close();
    }

    @Test
    public void testNextToken() throws IOException, JsonParseException {
        JsonToken token1 = mock(JsonToken.class);
        JsonToken token2 = mock(JsonToken.class);
        JsonToken token3 = mock(JsonToken.class);
        
        doReturn(token1).when(parsers[0]).nextToken();
        doReturn(token2).when(parsers[1]).nextToken();
        doReturn(token3).when(parsers[1]).nextToken();

        doReturn(false).when(parsers[0]).isActiveParser();
        doReturn(true).when(parsers[1]).isActiveParser();
        
        jsonParserSequence.nextToken();
        
        verify(parsers[0], times(1)).nextToken();
        verify(parsers[1], times(1)).nextToken();
    }

    @Test
    public void testContainedParsersCount() {
        assertEquals(2, jsonParserSequence.containedParsersCount());
    }

    // Additional test cases can be added for other methods as well

}