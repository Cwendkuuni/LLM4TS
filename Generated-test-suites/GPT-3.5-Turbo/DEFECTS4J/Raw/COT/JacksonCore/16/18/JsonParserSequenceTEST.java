
package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class JsonParserSequenceTest {

    @Test
    public void testClose() {
        // Create an array of mock JsonParsers
        JsonParser[] parsers = new JsonParser[2];
        parsers[0] = new MockJsonParser();
        parsers[1] = new MockJsonParser();

        // Create a JsonParserSequence with the mock parsers
        JsonParserSequence sequence = new JsonParserSequence(parsers);

        // Call close() on the sequence
        try {
            sequence.close();
        } catch (IOException e) {
            fail("Unexpected exception thrown: " + e.getMessage());
        }

        // Verify that close() was called on each mock parser
        for (JsonParser parser : parsers) {
            MockJsonParser mockParser = (MockJsonParser) parser;
            assertTrue(mockParser.isCloseCalled());
        }
    }

    @Test
    public void testNextToken() {
        // Create an array of mock JsonParsers
        JsonParser[] parsers = new JsonParser[2];
        parsers[0] = new MockJsonParser();
        parsers[1] = new MockJsonParser();

        // Create a JsonParserSequence with the mock parsers
        JsonParserSequence sequence = new JsonParserSequence(parsers);

        // Call nextToken() on the sequence
        try {
            JsonToken token = sequence.nextToken();
            assertNull(token);
        } catch (IOException e) {
            fail("Unexpected exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testContainedParsersCount() {
        // Create an array of mock JsonParsers
        JsonParser[] parsers = new JsonParser[3];
        parsers[0] = new MockJsonParser();
        parsers[1] = new MockJsonParser();
        parsers[2] = new MockJsonParser();

        // Create a JsonParserSequence with the mock parsers
        JsonParserSequence sequence = new JsonParserSequence(parsers);

        // Call containedParsersCount() on the sequence
        int count = sequence.containedParsersCount();
        assertEquals(3, count);
    }

    // Additional test cases for edge cases and exceptions can be added here

}

