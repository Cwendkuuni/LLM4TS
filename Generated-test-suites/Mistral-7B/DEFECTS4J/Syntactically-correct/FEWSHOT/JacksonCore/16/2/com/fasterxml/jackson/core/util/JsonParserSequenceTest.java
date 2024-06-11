package com.fasterxml.jackson.core.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;

import com.fasterxml.jackson.core.*;

public class JsonParserSequenceTest {

    @Test
    public void testJsonParserSequenceConstruction() {
        JsonParser firstParser = createMockParser();
        JsonParser secondParser = createMockParser();
        JsonParserSequence parserSequence = new JsonParserSequence(new JsonParser[] { firstParser, secondParser });
        assertEquals(2, parserSequence.containedParsersCount());
        assertEquals(firstParser, parserSequence.getDelegate());
    }

    @Test
    public void testCreateFlattened() throws IOException {
        JsonParser firstParser = createMockParser();
        JsonParser secondParser = createMockParser();
        JsonParser thirdParser = createMockParser();

        JsonParser flattenedParser = JsonParserSequence.createFlattened(firstParser, JsonParserSequence.createFlattened(secondParser, thirdParser));

        assertEquals(3, flattenedParser.containedParsersCount());
        assertEquals(firstParser, flattenedParser.getDelegate());
    }

    @Test
    public void testSwitchToNext() throws IOException {
        JsonParser firstParser = createMockParser();
        JsonParser secondParser = createMockParser();

        JsonParserSequence parserSequence = new JsonParserSequence(new JsonParser[] { firstParser, secondParser });

        assertTrue(parserSequence.switchToNext());
        assertEquals(secondParser, parserSequence.getDelegate());
        assertFalse(parserSequence.switchToNext());
    }

    @Test
    public void testNextToken() throws IOException, JsonParseException {
        JsonParser firstParser = createMockParser();
        JsonParser secondParser = createMockParser();

        JsonParserSequence parserSequence = new JsonParserSequence(new JsonParser[] { firstParser, secondParser });

        JsonToken token = parserSequence.nextToken();
        assertEquals(firstParser.nextToken(), token);

        assertTrue(parserSequence.switchToNext());
        token = parserSequence.nextToken();
        assertEquals(secondParser.nextToken(), token);

        assertFalse(parserSequence.switchToNext());
        assertNull(parserSequence.nextToken());
    }

    @Test
    public void testClose() throws IOException {
        JsonParser firstParser = createMockParser();
        JsonParser secondParser = createMockParser();

        JsonParserSequence parserSequence = new JsonParserSequence(new JsonParser[] { firstParser, secondParser });

        parserSequence.close();
        for (JsonParser parser : Arrays.asList(firstParser, secondParser)) {
            parser.close();
        }
    }

    private JsonParser createMockParser() {
        // Create a mock parser returning known tokens for testing purposes
        return new JsonParser() {
            private int _nextToken;

            @Override
            public JsonToken nextToken() throws IOException, JsonParseException {
                JsonToken token = JsonToken.values()[_nextToken++];
                if (token == null) {
                    throw new IOException();
                }
                return token;
            }

            @Override
            public void close() throws IOException {
                // do nothing
            }
        };
    }
}