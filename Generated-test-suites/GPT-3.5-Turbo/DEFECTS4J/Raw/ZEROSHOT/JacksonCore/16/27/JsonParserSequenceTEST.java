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
    public void testClose() throws IOException {
        JsonParser first = new MockJsonParser();
        JsonParser second = new MockJsonParser();
        JsonParserSequence sequence = JsonParserSequence.createFlattened(first, second);
        
        sequence.close();
        assertTrue(first.isClosed());
        assertTrue(second.isClosed());
    }

    @Test
    public void testNextToken() throws IOException, JsonParseException {
        JsonParser first = new MockJsonParser();
        JsonParser second = new MockJsonParser();
        JsonParserSequence sequence = JsonParserSequence.createFlattened(first, second);
        
        assertEquals(JsonToken.START_OBJECT, sequence.nextToken());
        assertEquals(JsonToken.FIELD_NAME, sequence.nextToken());
        assertEquals(JsonToken.VALUE_STRING, sequence.nextToken());
        // ... additional assertions for testing nextToken() method
    }

}