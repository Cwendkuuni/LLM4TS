package com.fasterxml.jackson.core.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class JsonParserSequenceTest {

    @Test
    public void testClose() throws IOException {
        JsonParser[] parsers = {new DummyParser(), new DummyParser(), new DummyParser()};
        JsonParserSequence parserSequence = new JsonParserSequence(parsers);
        parserSequence.close();
        for(JsonParser parser : parsers) {
            assertEquals(true, ((DummyParser) parser).isClosed());
        }
    }

    @Test
    public void testNextToken() throws IOException, JsonParseException {
        JsonParser[] parsers = {new DummyParser(JsonToken.START_ARRAY), 
                                new DummyParser(JsonToken.FIELD_NAME),
                                new DummyParser(JsonToken.VALUE_STRING),
                                new DummyParser(JsonToken.END_OBJECT)};
        JsonParserSequence parserSequence = new JsonParserSequence(parsers);
        assertEquals(JsonToken.START_ARRAY, parserSequence.nextToken());
        assertEquals(JsonToken.FIELD_NAME, parserSequence.nextToken());
        assertEquals(JsonToken.VALUE_STRING, parserSequence.nextToken());
        assertEquals(JsonToken.END_OBJECT, parserSequence.nextToken());
        assertNull(parserSequence.nextToken());
    }

    private static class DummyParser extends JsonParser {
        
        private JsonToken token;
        private boolean isClosed;

        public DummyParser() {
            this(null);
        }

        public DummyParser(JsonToken token) {
            this.token = token;
            this.isClosed = false;
        }

        @Override
        public void close() throws IOException {
            isClosed = true;
        }

        @Override
        public JsonToken nextToken() throws IOException, JsonParseException {
            return token;
        }

        public boolean isClosed() {
            return isClosed;
        }

        // other overridden methods with empty implementations

    }

}