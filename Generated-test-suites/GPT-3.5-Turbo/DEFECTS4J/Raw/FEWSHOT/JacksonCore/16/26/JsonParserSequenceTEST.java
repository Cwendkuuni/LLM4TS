package com.fasterxml.jackson.core.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class JsonParserSequenceTest {

    @Test
    public void testNextToken() throws Exception {
        JsonParser parser1 = createJsonParser();
        JsonParser parser2 = createJsonParser();
        JsonParserSequence sequence = JsonParserSequence.createFlattened(parser1, parser2);
        assertEquals(JsonToken.START_OBJECT, sequence.getCurrentToken());
        assertTrue(sequence.switchToNext());
        assertEquals(JsonToken.FIELD_NAME, sequence.nextToken());
        assertTrue(sequence.switchToNext());
        assertEquals(JsonToken.VALUE_STRING, sequence.nextToken());
        assertTrue(sequence.switchToNext());
        assertEquals(JsonToken.FIELD_NAME, sequence.nextToken());
        assertTrue(sequence.switchToNext());
        assertEquals(JsonToken.START_ARRAY, sequence.nextToken());
        assertTrue(sequence.switchToNext());
        assertEquals(JsonToken.VALUE_NUMBER_INT, sequence.nextToken());
        assertFalse(sequence.switchToNext());
        assertNull(sequence.nextToken());
    }

    @Test(expected = JsonParseException.class)
    public void testNextTokenWithJsonParseException() throws Exception {
        JsonParser parserThatThrowsException = createJsonParserThatThrowsException();
        JsonParser parser = createJsonParser();
        JsonParserSequence sequence = JsonParserSequence.createFlattened(parserThatThrowsException, parser);
        assertEquals(JsonToken.START_OBJECT, sequence.getCurrentToken());
        assertTrue(sequence.switchToNext());
        assertEquals(JsonToken.FIELD_NAME, sequence.nextToken());
    }

    @Test
    public void testClose() throws Exception {
        JsonParser parser1 = createJsonParser();
        JsonParser parser2 = createJsonParser();
        JsonParserSequence sequence = JsonParserSequence.createFlattened(parser1, parser2);
        sequence.close();
        assertTrue(parser1.isClosed());
        assertTrue(parser2.isClosed());
    }

    private JsonParser createJsonParser() throws Exception {
        return new JsonParser() {
            @Override
            public JsonToken nextToken() {
                return null;
            }

            @Override
            public String getCurrentName() {
                return null;
            }

            @Override
            public JsonParser skipChildren() {
                return null;
            }

            @Override
            public void close() {

            }

            @Override
            public boolean isClosed() {
                return false;
            }
        };
    }

    private JsonParser createJsonParserThatThrowsException() throws Exception {
        return new JsonParser() {
            @Override
            public JsonToken nextToken() throws JsonParseException {
                throw new JsonParseException(null, null);
            }

            @Override
            public String getCurrentName() {
                return null;
            }

            @Override
            public JsonParser skipChildren() {
                return null;
            }

            @Override
            public void close() {

            }

            @Override
            public boolean isClosed() {
                return false;
            }
        };
    }
}