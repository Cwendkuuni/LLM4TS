package com.fasterxml.jackson.core.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class JsonParserSequenceTest {

    @Test
    public void testCreateFlattened() {
        JsonParser first = new MockJsonParser();
        JsonParser second = new MockJsonParser();

        JsonParserSequence sequence = JsonParserSequence.createFlattened(first, second);

        assertEquals(2, sequence.containedParsersCount());
    }

    @Test
    public void testNextToken() throws IOException {
        JsonParser first = new MockJsonParser(JsonToken.START_OBJECT);
        JsonParser second = new MockJsonParser(JsonToken.FIELD_NAME);

        JsonParserSequence sequence = JsonParserSequence.createFlattened(first, second);

        assertEquals(JsonToken.START_OBJECT, sequence.nextToken());
        assertEquals(JsonToken.FIELD_NAME, sequence.nextToken());
    }

    @Test
    public void testClose() throws IOException {
        JsonParser first = new MockJsonParser(JsonToken.START_OBJECT);
        JsonParser second = new MockJsonParser(JsonToken.FIELD_NAME);

        JsonParserSequence sequence = JsonParserSequence.createFlattened(first, second);
        
        sequence.close();

        assertTrue(second.isClosed());
        assertTrue(first.isClosed());
    }
    
    @Test
    public void testSwitchToNext() throws IOException {
        JsonParser first = new MockJsonParser(JsonToken.START_OBJECT);
        JsonParser second = new MockJsonParser(JsonToken.FIELD_NAME);

        JsonParserSequence sequence = JsonParserSequence.createFlattened(first, second);

        assertTrue(sequence.switchToNext());
        assertEquals(JsonToken.FIELD_NAME, sequence.nextToken());

        assertFalse(sequence.switchToNext());
        assertNull(sequence.nextToken());
    }

    private static class MockJsonParser extends JsonParser {
        private JsonToken token;
        private boolean isClosed;

        public MockJsonParser() {
            this.token = null;
            this.isClosed = false;
        }

        public MockJsonParser(JsonToken token) {
            this.token = token;
            this.isClosed = false;
        }

        @Override
        public JsonToken nextToken() throws IOException, JsonParseException {
            return token;
        }

        @Override
        public void close() throws IOException {
            isClosed = true;
        }

        public boolean isClosed() {
            return isClosed;
        }

        // Other abstract methods to implement for MockJsonParser
    }
}