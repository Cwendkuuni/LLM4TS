package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class JsonParserSequenceTest {
    
    @Test
    public void testCreateFlattened() {
        JsonParserSequence sequence = JsonParserSequence.createFlattened(null, null);
        assertTrue(sequence.containedParsersCount() == 2);
    }
    
    @Test
    public void testNextToken() {
        JsonParserSequence sequence = new JsonParserSequence(null);
        try {
            sequence.nextToken();
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testClose() {
        JsonParserSequence sequence = new JsonParserSequence(null);
        try {
            sequence.close();
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testContainedParsersCount() {
        JsonParserSequence sequence = new JsonParserSequence(null);
        int count = sequence.containedParsersCount();
        assertTrue(count >= 0);
    }
}