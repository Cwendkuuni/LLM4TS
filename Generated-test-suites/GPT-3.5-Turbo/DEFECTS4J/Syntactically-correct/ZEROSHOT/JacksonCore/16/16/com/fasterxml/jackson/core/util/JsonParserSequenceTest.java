package com.fasterxml.jackson.core.util;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class JsonParserSequenceTest {

    private JsonParserSequence jsonParserSequence;

    @Before
    public void setUp() {
        // Create an array of JsonParsers
        JsonParser[] parsers = new JsonParser[2];
        parsers[0] = new DummyJsonParser();
        parsers[1] = new DummyJsonParser();
        jsonParserSequence = new JsonParserSequence(parsers);
    }

    @Test
    public void testContainedParsersCount() {
        assertEquals(2, jsonParserSequence.containedParsersCount());
    }

    @Test
    public void testClose() throws IOException {
        jsonParserSequence.close();
        assertTrue(jsonParserSequence.isClosed());
    }

    @Test
    public void testNextToken() throws IOException {
        jsonParserSequence.nextToken();
        assertFalse(jsonParserSequence.isClosed());
    }

    /* DummyJsonParser for testing purpose */
    private class DummyJsonParser extends JsonParser {

        public DummyJsonParser() {
        }

        @Override
        public void close() throws IOException {
        }

        @Override
        public JsonToken nextToken() throws IOException, JsonParseException {
            return null;
        }
    }
}