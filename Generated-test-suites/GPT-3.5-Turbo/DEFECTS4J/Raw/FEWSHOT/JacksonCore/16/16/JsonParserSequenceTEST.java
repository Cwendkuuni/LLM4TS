package com.fasterxml.jackson.core.util;

import com.fasterxml.jackson.core.*;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class JsonParserSequenceTest {

    private JsonParserSequence jsonParserSequence;
    private JsonParser[] parsers;

    @Before
    public void setUp() {
        parsers = new JsonParser[3];
        for (int i = 0; i < parsers.length; i++) {
            parsers[i] = mock(JsonParser.class);
        }
        jsonParserSequence = new JsonParserSequence(parsers);
    }

    @Test
    public void testClose() throws IOException {
        for (int i = 0; i < parsers.length; i++) {
            when(parsers[i].nextToken()).thenReturn(null);
        }
        jsonParserSequence.close();
        for (JsonParser parser : parsers) {
            verify(parser).close();
        }
    }

    @Test
    public void testNextToken() throws IOException, JsonParseException {
        List<JsonToken> tokens = new ArrayList<>();
        tokens.add(JsonToken.VALUE_FALSE);
        tokens.add(JsonToken.VALUE_NUMBER_INT);
        tokens.add(JsonToken.START_ARRAY);

        for (int i = 0; i < parsers.length; i++) {
            when(parsers[i].nextToken()).thenReturn(tokens.get(i));
        }

        for (int i = 0; i < tokens.size(); i++) {
            JsonToken token = jsonParserSequence.nextToken();
            verify(parsers[i]).nextToken();
            assert token == tokens.get(i);
        }
    }

    @Test
    public void testContainedParsersCount() {
        assert jsonParserSequence.containedParsersCount() == parsers.length;
    }

    @Test
    public void testCreateFlattened() {
        JsonParser first = mock(JsonParser.class);
        JsonParser second = mock(JsonParser.class);
        JsonParserSequence.createFlattened(first, second);
        verify(first).nextToken();
        verify(second).nextToken();
    }

}