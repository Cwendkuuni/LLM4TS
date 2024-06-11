package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;
import org.junit.Test;

public class JsonParserSequenceTest {
    
    @Test
    public void testClose() throws IOException {
        // Create an array of JsonParsers
        JsonParser[] parsers = new JsonParser[3];
        parsers[0] = new DummyParser();
        parsers[1] = new DummyParser();
        parsers[2] = new DummyParser();
        
        // Create a JsonParserSequence
        JsonParserSequence sequence = new JsonParserSequence(parsers);
        
        // Call close method
        sequence.close();
        
        // Verify that close method was called on all parsers
        for (JsonParser parser : parsers) {
            assertTrue(((DummyParser)parser).isCloseCalled());
        }
    }
    
    @Test
    public void testNextToken() throws IOException, JsonParseException {
        // Create an array of JsonParsers
        JsonParser[] parsers = new JsonParser[3];
        parsers[0] = new DummyParser();
        parsers[1] = new DummyParser();
        parsers[2] = new DummyParser();
        
        // Create a JsonParserSequence
        JsonParserSequence sequence = new JsonParserSequence(parsers);
        
        // Call nextToken method
        JsonToken token = sequence.nextToken();
        
        // Verify that nextToken method was called on each parser until a non-null token is returned
        for (JsonParser parser : parsers) {
            assertTrue(((DummyParser)parser).isNextTokenCalled());
        }
        
        // Verify that the returned token is null
        assertNull(token);
        
    }
    
    @Test
    public void testContainedParsersCount() {
        // Create an array of JsonParsers
        JsonParser[] parsers = new JsonParser[3];
        parsers[0] = new DummyParser();
        parsers[1] = new DummyParser();
        parsers[2] = new DummyParser();
        
        // Create a JsonParserSequence
        JsonParserSequence sequence = new JsonParserSequence(parsers);
        
        // Call containedParsersCount method
        int count = sequence.containedParsersCount();
        
        // Verify that the returned count is the same as the length of the array
        assertEquals(parsers.length, count);
    }
    
    // Create a dummy implementation of JsonParser
    private class DummyParser extends JsonParser {
        private boolean closeCalled = false;
        private boolean nextTokenCalled = false;

        @Override
        public void close() throws IOException {
            closeCalled = true;
        }

        @Override
        public JsonToken nextToken() throws IOException, JsonParseException {
            nextTokenCalled = true;
            return null;
        }

        public boolean isCloseCalled() {
            return closeCalled;
        }

        public boolean isNextTokenCalled() {
            return nextTokenCalled;
        }
        
        // Other overridden methods...
    }
}