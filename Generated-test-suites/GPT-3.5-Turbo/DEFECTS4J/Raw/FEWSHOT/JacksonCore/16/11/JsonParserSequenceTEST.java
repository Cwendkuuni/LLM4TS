package com.fasterxml.jackson.core.util;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class JsonParserSequenceTest {

    private JsonParserSequence sequence;
    private JsonParser firstParser;
    private JsonParser secondParser;

    @Before
    public void setup() {
        firstParser = new DummyParser();
        secondParser = new DummyParser();
        sequence = JsonParserSequence.createFlattened(firstParser, secondParser);
    }

    @Test
    public void testContainedParsersCount() {
        assertEquals(2, sequence.containedParsersCount());
    }

    @Test
    public void testClose() throws Exception {
        firstParser.setClosed(false);
        secondParser.setClosed(false);

        sequence.close();

        assertEquals(true, firstParser.isClosed());
        assertEquals(true, secondParser.isClosed());
    }

    @Test
    public void testNextToken() throws Exception {
        firstParser.setNextToken(JsonToken.START_OBJECT);
        secondParser.setNextToken(JsonToken.FIELD_NAME);

        assertEquals(JsonToken.START_OBJECT, sequence.nextToken());
        assertEquals(JsonToken.FIELD_NAME, sequence.nextToken());
        assertNull(sequence.nextToken());
    }

    private static class DummyParser extends JsonParser {
        private boolean closed;
        private JsonToken nextToken;

        @Override
        public boolean isClosed() {
            return closed;
        }

        @Override
        public JsonToken nextToken() throws JsonParseException {
            JsonToken token = nextToken;
            nextToken = null;
            return token;
        }

        @Override
        public void close() {
            closed = true;
        }

        public void setClosed(boolean closed) {
            this.closed = closed;
        }

        public void setNextToken(JsonToken nextToken) {
            this.nextToken = nextToken;
        }

    }
}