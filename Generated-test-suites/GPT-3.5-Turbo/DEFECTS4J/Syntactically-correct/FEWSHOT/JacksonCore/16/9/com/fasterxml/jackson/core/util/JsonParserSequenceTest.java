package com.fasterxml.jackson.core.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonToken;

public class JsonParserSequenceTest {
    @Test
    public void testClose() throws Exception {
        // Create a sequence of two parsers
        JsonParser parser1 = new MyJsonParser();
        JsonParser parser2 = new MyJsonParser();
        JsonParserSequence sequence = JsonParserSequence.createFlattened(parser1, parser2);
        
        // Close the sequence
        sequence.close();
        
        // Verify that both parsers are closed
        assertFalse(parser1.isOpen());
        assertFalse(parser2.isOpen());
    }
    
    @Test
    public void testNextToken() throws Exception {
        // Create a sequence of two parsers
        JsonParser parser1 = new MyJsonParser();
        JsonParser parser2 = new MyJsonParser();
        JsonParserSequence sequence = JsonParserSequence.createFlattened(parser1, parser2);
        
        // Set the return values for the nextToken() method of the parsers
        parser1.setNextTokenReturnValues(JsonToken.START_OBJECT, JsonToken.FIELD_NAME, JsonToken.VALUE_STRING);
        parser2.setNextTokenReturnValues(JsonToken.FIELD_NAME, JsonToken.VALUE_NUMBER_INT);
        
        // Call the nextToken() method of the sequence
        JsonToken token1 = sequence.nextToken();
        JsonToken token2 = sequence.nextToken();
        JsonToken token3 = sequence.nextToken();
        JsonToken token4 = sequence.nextToken();
        
        // Verify the returned tokens
        assertEquals(JsonToken.START_OBJECT, token1);
        assertEquals(JsonToken.FIELD_NAME, token2);
        assertEquals(JsonToken.VALUE_STRING, token3);
        assertEquals(JsonToken.VALUE_NUMBER_INT, token4);
    }

    @Test
    public void testContainedParsersCount() throws Exception {
        // Create a sequence of three parsers
        JsonParser parser1 = new MyJsonParser();
        JsonParser parser2 = new MyJsonParser();
        JsonParser parser3 = new MyJsonParser();
        JsonParserSequence sequence = JsonParserSequence.createFlattened(parser1, parser2, parser3);
        
        // Verify the containedParsersCount() method
        assertEquals(3, sequence.containedParsersCount());
    }
    
    // Custom implementation of JsonParser for testing
    private static class MyJsonParser extends JsonParser {
        private JsonToken[] nextTokenReturnValues;
        private int nextTokenIndex;
        private boolean isOpen = true;
        
        public void setNextTokenReturnValues(JsonToken... tokens) {
            this.nextTokenReturnValues = tokens;
            this.nextTokenIndex = 0;
        }
        
        public boolean isOpen() {
            return this.isOpen;
        }
        
        @Override
        public void close() {
            this.isOpen = false;
        }
        
        @Override
        public JsonToken nextToken() throws JsonParseException {
            if (nextTokenReturnValues != null && nextTokenIndex < nextTokenReturnValues.length) {
                return nextTokenReturnValues[nextTokenIndex++];
            } else {
                return null;
            }
        }
        
        // ...Override other abstract methods of JsonParser if necessary...
    }
}