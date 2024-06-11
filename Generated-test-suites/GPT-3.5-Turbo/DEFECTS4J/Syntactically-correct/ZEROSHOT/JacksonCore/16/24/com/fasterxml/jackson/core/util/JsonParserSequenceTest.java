package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class JsonParserSequenceTest {

    @Test
    public void testCreateFlattened() {
        JsonParser first = new MockJsonParser();
        JsonParser second = new MockJsonParser();
        
        JsonParserSequence sequence = JsonParserSequence.createFlattened(first, second);
        
        assertNotNull(sequence);
        assertEquals(2, sequence.containedParsersCount());
    }
    
    @Test
    public void testNextToken() {
        JsonParser first = new MockJsonParser();
        JsonParser second = new MockJsonParser();
        
        JsonParserSequence sequence = JsonParserSequence.createFlattened(first, second);
        
        try {
            while (sequence.nextToken() != null) {}
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }
    
    private static class MockJsonParser extends JsonParser {
        // Mock implementation of JsonParser for testing purposes
    }
}