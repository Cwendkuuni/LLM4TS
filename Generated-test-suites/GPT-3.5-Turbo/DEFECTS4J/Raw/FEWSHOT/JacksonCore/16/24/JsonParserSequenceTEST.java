
package com.fasterxml.jackson.core.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonToken;

public class JsonParserSequenceTest {

    @Test
    public void testCreateFlattened() {
        // Create two Mock JsonParsers
        MockJsonParser first = new MockJsonParser();
        MockJsonParser second = new MockJsonParser();
        
        // Create JsonParserSequence and check the containedParsersCount()
        JsonParserSequence sequence = JsonParserSequence.createFlattened(first, second);
        assertEquals(2, sequence.containedParsersCount());
        
        // Check if the delegate is the first parser
        assertEquals(first, sequence.delegate);
        
        // Check nextToken method returns correct tokens
        assertEquals(JsonToken.START_ARRAY, sequence.nextToken());
        
        // Check switchToNext method
        assertTrue(sequence.switchToNext());
        assertEquals(second, sequence.delegate);
        
        // Check nextToken method returns correct tokens after switchToNext
        assertEquals(JsonToken.START_OBJECT, sequence.nextToken());
        assertEquals(JsonToken.FIELD_NAME, sequence.nextToken());
    }
    
    @Test
    public void testClose() throws Exception {
        // Create two Mock JsonParsers
        MockJsonParser first = new MockJsonParser();
        MockJsonParser second = new MockJsonParser();
        
        // Create JsonParserSequence
        JsonParserSequence sequence = JsonParserSequence.createFlattened(first, second);
        
        // Call close
        sequence.close();
        
        // Check if parsers are closed
        assertTrue(first.isClosed());
        assertTrue(second.isClosed());
    }
    
    @Test
    public void testNextToken() throws Exception {
        // Create two Mock JsonParsers
        MockJsonParser first = new MockJsonParser();
        MockJsonParser second = new MockJsonParser();
        
        // Create JsonParserSequence
        JsonParserSequence sequence = JsonParserSequence.createFlattened(first, second);
        
        // Check nextToken method returns correct tokens
        assertEquals(JsonToken.START_ARRAY, sequence.nextToken());
        assertEquals(JsonToken.VALUE_STRING, sequence.nextToken());
        assertEquals(JsonToken.END_ARRAY, sequence.nextToken());
        assertEquals(JsonToken.START_OBJECT, sequence.nextToken());
        assertEquals(JsonToken.FIELD_NAME, sequence.nextToken());
        assertEquals(JsonToken.VALUE_NUMBER_INT, sequence.nextToken());
        assertEquals(JsonToken.END_OBJECT, sequence.nextToken());
        assertEquals(JsonToken.START_ARRAY, sequence.nextToken());
        assertEquals(JsonToken.VALUE_NUMBER_FLOAT, sequence.nextToken());
        assertEquals(JsonToken.END_ARRAY, sequence.nextToken());
        
        // Check if switchToNext method is called
        assertTrue(first.switchToNextCalled());
        assertTrue(second.switchToNextCalled());
    }
    
    @Test
    public void testNextToken_NoMoreTokens() throws Exception {
        // Create a Mock JsonParser with no more tokens
        MockJsonParser first = new MockJsonParser(JsonToken.END_ARRAY);
        
        // Create JsonParserSequence
        JsonParserSequence sequence = JsonParserSequence.createFlattened(first);
        
        // Check nextToken method returns null
        assertNull(sequence.nextToken());
        
        // Check if switchToNext method is not called
        assertFalse(first.switchToNextCalled());
    }
    
    @Test(expected = JsonParseException.class)
    public void testNextToken_IOException() throws Exception {
        // Create a Mock JsonParser with IOException
        MockJsonParser first = new MockJsonParser(new IOException());
        
        // Create JsonParserSequence
        JsonParserSequence sequence = JsonParserSequence.createFlattened(first);
        
        // Call nextToken method and expect JsonParseException
        sequence.nextToken();
    }

    private static class MockJsonParser extends JsonParser {
        
        private final JsonToken returnToken;
        private boolean switchToNextCalled;
        private boolean closed;
        private IOException ioException;
        
        public MockJsonParser() {
            this.returnToken = null;
        }
        
        public MockJsonParser(JsonToken returnToken) {
            this.returnToken = returnToken;
        }

        public MockJsonParser(IOException ioException) {
            this.returnToken = null;
            this.ioException = ioException;
        }
        
        public boolean switchToNextCalled() {
            return switchToNextCalled;
        }
        
        public boolean isClosed() {
            return closed;
        }

        @Override
        public void close() throws IOException {
            closed = true;
        }

        @Override
        public JsonToken nextToken() throws IOException {
            if (ioException != null) {
                throw ioException;
            }
            return returnToken;
        }
        
        @Override
        public boolean switchToNextValue() throws IOException {
            switchToNextCalled = true;
            return true;
        }

        @Override
        public JsonParser skipChildren() throws IOException {
            return this;
        }

        @Override
        public String getCurrentName() throws IOException {
            return null;
        }

        @Override
        public void overrideCurrentName(String name) {
        }

        @Override
        public String getText() throws IOException {
            return null;
        }

        @Override
        public char[] getTextCharacters() throws IOException {
            return null;
        }

        @Override
        public int getTextLength() throws IOException {
            return 0;
        }

        @Override
        public int getTextOffset() throws IOException {
            return 0;
        }

        @Override
        public boolean hasTextCharacters() {
            return false;
        }

        @Override
        public JsonLocation getTokenLocation() {
            return null;
        }

        @Override
        public JsonToken getCurrentToken() {
            return null;
        }
        
        // Other overridden methods

        //...
    }
}
