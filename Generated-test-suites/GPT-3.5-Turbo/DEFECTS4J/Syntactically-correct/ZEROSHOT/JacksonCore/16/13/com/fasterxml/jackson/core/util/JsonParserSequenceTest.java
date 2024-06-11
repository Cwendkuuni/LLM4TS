package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;
import org.junit.Test;

public class JsonParserSequenceTest {
    
    @Test
    public void testContainedParsersCount() {
        JsonParser jp1 = new FakeJsonParser();
        JsonParser jp2 = new FakeJsonParser();
        JsonParser jp3 = new FakeJsonParser();
        
        JsonParserSequence sequence = JsonParserSequence.createFlattened(jp1, jp2, jp3);
        
        int expectedCount = 3;
        int actualCount = sequence.containedParsersCount();
        
        assertEquals(expectedCount, actualCount);
    }
    
    @Test
    public void testClose() throws IOException {
        JsonParser jp1 = new FakeJsonParser();
        JsonParser jp2 = new FakeJsonParser();
        JsonParser jp3 = new FakeJsonParser();
        
        JsonParserSequence sequence = JsonParserSequence.createFlattened(jp1, jp2, jp3);
        
        sequence.close();
        
        assertTrue(jp1.isClosed());
        assertTrue(jp2.isClosed());
        assertTrue(jp3.isClosed());
    }
    
    @Test
    public void testNextToken() throws IOException, JsonParseException {
        JsonParser jp1 = new FakeJsonParser(new JsonToken[]{JsonToken.START_OBJECT, JsonToken.FIELD_NAME, JsonToken.VALUE_STRING});
        JsonParser jp2 = new FakeJsonParser(new JsonToken[]{JsonToken.FIELD_NAME, JsonToken.VALUE_NUMBER_INT});
        JsonParser jp3 = new FakeJsonParser(new JsonToken[]{JsonToken.VALUE_TRUE});
        
        JsonParserSequence sequence = JsonParserSequence.createFlattened(jp1, jp2, jp3);
        
        JsonToken[] expectedTokens = {JsonToken.START_OBJECT, JsonToken.FIELD_NAME, JsonToken.VALUE_STRING, JsonToken.FIELD_NAME, JsonToken.VALUE_NUMBER_INT, JsonToken.VALUE_TRUE};
        List<JsonToken> actualTokens = new ArrayList<>();
        
        JsonToken token = sequence.nextToken();
        while (token != null) {
            actualTokens.add(token);
            token = sequence.nextToken();
        }
        
        assertArrayEquals(expectedTokens, actualTokens.toArray());
    }
    
    private static class FakeJsonParser extends JsonParser {
        private boolean closed;
        private JsonToken[] tokens;
        private int index;
        
        public FakeJsonParser() {
            this.tokens = new JsonToken[0];
            this.index = 0;
        }
        
        public FakeJsonParser(JsonToken[] tokens) {
            this.tokens = tokens;
            this.index = 0;
        }
        
        @Override
        public JsonToken nextToken() throws IOException, JsonParseException {
            if (index < tokens.length) {
                return tokens[index++];
            } else {
                return null;
            }
        }
        
        @Override
        public void close() throws IOException {
            closed = true;
        }
        
        public boolean isClosed() {
            return closed;
        }
        
        // Other abstract methods of JsonParser that needs implementation
    }
}