package com.fasterxml.jackson.core.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class JsonParserSequenceTest {
    @Test
    public void testClose() throws IOException {
        DummyJsonParser parser1 = new DummyJsonParser();
        DummyJsonParser parser2 = new DummyJsonParser();
        JsonParserSequence sequence = JsonParserSequence.createFlattened(parser1, parser2);
        
        sequence.close();
        
        assertTrue(parser1.isClosed());
        assertTrue(parser2.isClosed());
    }
    
    @Test
    public void testNextToken() throws IOException, JsonParseException {
        DummyJsonParser parser1 = new DummyJsonParser();
        DummyJsonParser parser2 = new DummyJsonParser();
        parser1.setTokens(Arrays.asList(JsonToken.START_OBJECT, JsonToken.FIELD_NAME, JsonToken.VALUE_STRING, JsonToken.END_OBJECT));
        parser2.setTokens(Arrays.asList(JsonToken.START_ARRAY, JsonToken.VALUE_NUMBER_INT, JsonToken.END_ARRAY));
        JsonParserSequence sequence = JsonParserSequence.createFlattened(parser1, parser2);
        
        assertEquals(JsonToken.START_OBJECT, sequence.nextToken());
        assertEquals(JsonToken.FIELD_NAME, sequence.nextToken());
        assertEquals(JsonToken.VALUE_STRING, sequence.nextToken());
        assertEquals(JsonToken.END_OBJECT, sequence.nextToken());
        assertEquals(JsonToken.START_ARRAY, sequence.nextToken());
        assertEquals(JsonToken.VALUE_NUMBER_INT, sequence.nextToken());
        assertEquals(JsonToken.END_ARRAY, sequence.nextToken());
        assertNull(sequence.nextToken());
    }
    
    @Test
    public void testContainedParsersCount() {
        DummyJsonParser parser1 = new DummyJsonParser();
        DummyJsonParser parser2 = new DummyJsonParser();
        JsonParserSequence sequence = JsonParserSequence.createFlattened(parser1, parser2);
        
        assertEquals(2, sequence.containedParsersCount());
    }
    
    private static class DummyJsonParser extends JsonParser {
        private boolean closed = false;
        private int tokenIndex = 0;
        private List<JsonToken> tokens;
        
        public void setTokens(List<JsonToken> tokens) {
            this.tokens = tokens;
        }
        
        public boolean isClosed() {
            return closed;
        }

        @Override
        public void close() throws IOException {
            closed = true;
        }

        @Override
        public JsonToken nextToken() throws IOException, JsonParseException {
            if (tokens == null || tokenIndex >= tokens.size()) {
                return null;
            }
            return tokens.get(tokenIndex++);
        }
    }
}