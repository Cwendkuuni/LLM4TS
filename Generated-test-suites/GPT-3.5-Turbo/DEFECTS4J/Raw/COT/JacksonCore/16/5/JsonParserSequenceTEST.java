package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;
import org.junit.Test;

public class JsonParserSequenceTest {

    @Test
    public void testClose() {
        // Create an array of two dummy parsers
        JsonParser[] parsers = new JsonParser[2];
        for (int i = 0; i < parsers.length; i++) {
            parsers[i] = new DummyJsonParser();
        }

        // Create a JsonParserSequence with two parsers
        JsonParserSequence sequence = new JsonParserSequence(parsers);

        try {
            // Call the close method
            sequence.close();

            // Verify that the close method was called on all parsers
            for (JsonParser parser : parsers) {
                assertTrue(((DummyJsonParser) parser).isCloseCalled());
            }
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testNextToken() {
        // Create an array of two dummy parsers
        JsonParser[] parsers = new JsonParser[2];
        for (int i = 0; i < parsers.length; i++) {
            parsers[i] = new DummyJsonParser();
        }

        // Create a JsonParserSequence with two parsers
        JsonParserSequence sequence = new JsonParserSequence(parsers);

        try {
            // Call the nextToken method
            sequence.nextToken();

            // Verify that the nextToken method was called on the first parser
            assertTrue(((DummyJsonParser) parsers[0]).isNextTokenCalled());

            // Call the nextToken method again
            sequence.nextToken();

            // Verify that the nextToken method was called on the second parser
            assertTrue(((DummyJsonParser) parsers[1]).isNextTokenCalled());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testCreateFlattened() {
        // Create two dummy parsers
        JsonParser first = new DummyJsonParser();
        JsonParser second = new DummyJsonParser();

        // Create a JsonParserSequence using the createFlattened method
        JsonParserSequence sequence = JsonParserSequence.createFlattened(first, second);

        // Verify that the JsonParserSequence contains the two dummy parsers
        assertEquals(2, sequence.containedParsersCount());
    }

    private static class DummyJsonParser extends JsonParser {

        private boolean closeCalled = false;
        private boolean nextTokenCalled = false;

        @Override
        public void close() {
            closeCalled = true;
        }

        @Override
        public JsonToken nextToken() {
            nextTokenCalled = true;
            return null;
        }

        public boolean isCloseCalled() {
            return closeCalled;
        }

        public boolean isNextTokenCalled() {
            return nextTokenCalled;
        }
    }
}