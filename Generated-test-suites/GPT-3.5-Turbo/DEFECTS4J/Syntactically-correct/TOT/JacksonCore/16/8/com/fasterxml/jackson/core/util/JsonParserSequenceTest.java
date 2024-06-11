package com.fasterxml.jackson.core.util;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class JsonParserSequenceTest {

    @Test
    public void testClose() throws IOException {
        JsonParser p1 = new DummyJsonParser(); // a dummy parser
        JsonParser p2 = new DummyJsonParser(); // another dummy parser
        JsonParserSequence sequence = JsonParserSequence.createFlattened(p1, p2);
        
        sequence.close();
        
        Assert.assertTrue(((DummyJsonParser) p1).isClosed());
        Assert.assertTrue(((DummyJsonParser) p2).isClosed());
    }
    
    @Test
    public void testNextToken() throws IOException, JsonParseException {
        JsonParser p1 = new DummyJsonParser(); // a dummy parser
        JsonParser p2 = new DummyJsonParser(); // another dummy parser
        ((DummyJsonParser) p2).overrideNextToken(JsonToken.VALUE_STRING);
        JsonParserSequence sequence = JsonParserSequence.createFlattened(p1, p2);

        Assert.assertEquals(JsonToken.VALUE_STRING, sequence.nextToken());
        
        Assert.assertTrue(((DummyJsonParser) p1).isNextTokenCalled());
        Assert.assertTrue(((DummyJsonParser) p2).isNextTokenCalled());
    }
    
    @Test
    public void testContainedParsersCount() {
        JsonParser p1 = new DummyJsonParser(); // a dummy parser
        JsonParser p2 = new DummyJsonParser(); // another dummy parser
        JsonParserSequence sequence = JsonParserSequence.createFlattened(p1, p2);
        
        Assert.assertEquals(2, sequence.containedParsersCount());
    }

    // Additional test cases can be added here for other methods in the JsonParserSequence class
    
    private class DummyJsonParser extends JsonParser {
        private boolean closed;
        private boolean nextTokenCalled;
        private JsonToken nextToken;
        
        public DummyJsonParser() {
            super(null);
        }
        
        public void close() throws IOException {
            closed = true;
        }
        
        public JsonToken nextToken() throws IOException, JsonParseException {
            nextTokenCalled = true;
            return nextToken;
        }
        
        public void overrideNextToken(JsonToken token) {
            nextToken = token;
        }
        
        public boolean isClosed() {
            return closed;
        }
        
        public boolean isNextTokenCalled() {
            return nextTokenCalled;
        }
    }
}