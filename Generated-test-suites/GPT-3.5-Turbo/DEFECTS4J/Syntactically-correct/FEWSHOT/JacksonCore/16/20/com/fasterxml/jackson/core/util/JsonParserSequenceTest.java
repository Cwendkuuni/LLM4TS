package com.fasterxml.jackson.core.util;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class JsonParserSequenceTest {
    
    @Test
    public void testClose() throws IOException {
        JsonParser mockParser1 = new MockJsonParser();
        JsonParser mockParser2 = new MockJsonParser();
        JsonParserSequence sequence = JsonParserSequence.createFlattened(mockParser1, mockParser2);
        
        sequence.close();
        
        assertEquals(true, ((MockJsonParser) mockParser1).isClosed());
        assertEquals(true, ((MockJsonParser) mockParser2).isClosed());
    }
    
    @Test
    public void testNextToken() throws IOException, JsonParseException {
        MockJsonParser mockParser1 = new MockJsonParser();
        mockParser1.setNextToken(JsonToken.FIELD_NAME);
        MockJsonParser mockParser2 = new MockJsonParser();
        mockParser2.setNextToken(JsonToken.VALUE_STRING);
        
        JsonParserSequence sequence = JsonParserSequence.createFlattened(mockParser1, mockParser2);
        
        JsonToken token = sequence.nextToken();
        
        assertEquals(JsonToken.FIELD_NAME, token);
        
        token = sequence.nextToken();
        
        assertEquals(JsonToken.VALUE_STRING, token);
    }
    
    @Test
    public void testContainedParsersCount() {
        JsonParser mockParser1 = new MockJsonParser();
        JsonParser mockParser2 = new MockJsonParser();
        JsonParserSequence sequence = JsonParserSequence.createFlattened(mockParser1, mockParser2);
        
        int count = sequence.containedParsersCount();
        
        assertEquals(2, count);
    }
    
    //MockJsonParser class for testing purposes
    public class MockJsonParser extends JsonParser {
        
        private JsonToken nextToken;
        private boolean closed;
        
        public void setNextToken(JsonToken token) {
            nextToken = token;
        }
        
        @Override
        public boolean isClosed() {
            return closed;
        }
        
        @Override
        public JsonToken nextToken() throws IOException, JsonParseException {
            JsonToken token = nextToken;
            nextToken = null;
            return token;
        }
        
        @Override
        public void close() throws IOException {
            closed = true;
        }
        
        //Override other abstract methods of JsonParser if necessary 
    }
}