package com.fasterxml.jackson.core.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import static org.junit.Assert.*;

public class JsonParserSequenceTest {

    @Test
    public void testCreateFlattened() {
        JsonParser parser1 = new DummyParser();
        JsonParser parser2 = new DummyParser();
        JsonParserSequence sequence = JsonParserSequence.createFlattened(parser1, parser2);

        assertEquals(2, sequence.containedParsersCount());
    }

    @Test
    public void testClose() throws IOException {
        JsonParser parser1 = new DummyParser();
        JsonParser parser2 = new DummyParser();
        JsonParserSequence sequence = JsonParserSequence.createFlattened(parser1, parser2);

        sequence.close();
        assertTrue(parser1.isClosed());
        assertTrue(parser2.isClosed());
    }

    @Test
    public void testNextToken() throws IOException, JsonParseException {
        JsonParser parser1 = new DummyParser();
        JsonParser parser2 = new DummyParser();
        JsonParserSequence sequence = JsonParserSequence.createFlattened(parser1, parser2);

        assertEquals(JsonToken.START_OBJECT, sequence.nextToken());
        assertEquals(JsonToken.FIELD_NAME, sequence.nextToken());
        assertEquals(JsonToken.VALUE_STRING, sequence.nextToken());
        assertEquals(JsonToken.END_OBJECT, sequence.nextToken());
        assertNull(sequence.nextToken());
    }

    // DummyParser class for testing
    private static class DummyParser extends JsonParser {
        private boolean closed = false;

        @Override
        public void close() throws IOException {
            closed = true;
        }

        @Override
        public JsonToken nextToken() throws IOException, JsonParseException {
            return null;
        }

        @Override
        public boolean isClosed() {
            return closed;
        }
    }
}