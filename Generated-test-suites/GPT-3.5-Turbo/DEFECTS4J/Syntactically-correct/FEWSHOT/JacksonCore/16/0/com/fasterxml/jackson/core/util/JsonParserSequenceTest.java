package com.fasterxml.jackson.core.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.*;

public class JsonParserSequenceTest {
    private JsonParserMock parser1;
    private JsonParserMock parser2;
    private JsonParserSequence sequence;
    
    @Before
    public void setUp() {
        parser1 = new JsonParserMock();
        parser2 = new JsonParserMock();
        sequence = JsonParserSequence.createFlattened(parser1, parser2);
    }
    
    @Test
    public void testContainedParsersCount() {
        assertEquals(2, sequence.containedParsersCount());
    }
    
    @Test
    public void testNextToken() throws IOException, JsonParseException {
        parser1.nextTokenReturn = JsonToken.START_ARRAY;
        JsonToken token = sequence.nextToken();
        assertEquals(JsonToken.START_ARRAY, token);
        assertTrue(parser1.nextTokenCalled);
        assertFalse(parser2.nextTokenCalled);
        
        parser1.nextTokenReturn = null;
        parser2.nextTokenReturn = JsonToken.FIELD_NAME;
        token = sequence.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        assertFalse(parser1.nextTokenCalled);
        assertTrue(parser2.nextTokenCalled);
        
        parser2.nextTokenReturn = null;
        token = sequence.nextToken();
        assertNull(token);
    }
    
    @Test
    public void testClose() throws IOException {
        sequence.close();
        assertTrue(parser1.closeCalled);
        assertTrue(parser2.closeCalled);
    }
    
    private static class JsonParserMock extends JsonParser {
        public boolean nextTokenCalled;
        public JsonToken nextTokenReturn;
        public boolean closeCalled;
        
        @Override
        public JsonToken nextToken() throws IOException, JsonParseException {
            nextTokenCalled = true;
            return nextTokenReturn;
        }

        @Override
        public void close() throws IOException {
            closeCalled = true;
        }

        // Other overridden methods...
    }
}