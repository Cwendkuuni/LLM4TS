package com.fasterxml.jackson.core.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class JsonParserSequenceTest {

    private JsonParser firstParser;
    private JsonParser secondParser;
    private JsonParser sequenceParser;

    @Before
    public void setUp() throws IOException {
        firstParser = new TestJsonParser();
        secondParser = new TestJsonParser();
        sequenceParser = JsonParserSequence.createFlattened(firstParser, secondParser);
    }

    @Test
    public void testContainedParsersCount() {
        assertEquals(2, sequenceParser.containedParsersCount());
    }

    @Test
    public void testClose() throws IOException {
        sequenceParser.close();
    }

    @Test
    public void testNextTokenFromFirstParser() throws IOException, JsonParseException {
        JsonToken token = sequenceParser.nextToken();
        assertNotNull(token);
        assertEquals(JsonToken.START_OBJECT, token.getId());
    }

    @Test
    public void testNextTokenFromSecondParser() throws IOException, JsonParseException {
        sequenceParser.nextToken(); // consume token from first parser
        JsonToken token = sequenceParser.nextToken();
        assertNotNull(token);
        assertEquals(JsonToken.START_ARRAY, token.getId());
    }

    @Test
    public void testNextTokenFromEmptySequence() throws IOException, JsonParseException {
        JsonParser emptySequence = JsonParserSequence.createFlattened(new JsonParser[0].getClass().getDeclaredConstructor().newInstance());
        JsonToken token = emptySequence.nextToken();
        assertNull(token);
    }

    @Test(expected = JsonParseException.class)
    public void testNextTokenFromEmptySequenceThrowException() throws IOException, JsonParseException {
        JsonParser emptySequence = JsonParserSequence.createFlattened(new JsonParser[0].getClass().getDeclaredConstructor().newInstance());
        emptySequence.nextToken();
        emptySequence.nextToken();
    }

    @Test
    public void testSwitchToNextParser() throws IOException, JsonParseException {
        sequenceParser.nextToken(); // consume token from first parser
        assertTrue(sequenceParser.switchToNext());
        JsonToken token = sequenceParser.nextToken();
        assertNotNull(token);
        assertEquals(JsonToken.START_ARRAY, token.getId());
    }

    @Test
    public void testSwitchToNextParserFailed() throws IOException, JsonParseException {
        sequenceParser.nextToken(); // consume token from first parser
        assertFalse(sequenceParser.switchToNext());
        JsonToken token = sequenceParser.nextToken();
        assertNull(token);
    }

    static class TestJsonParser extends JsonParser {
        @Override
        public JsonToken nextToken() throws IOException, JsonParseException {
            if (next == null) {
                next = JsonToken.START_OBJECT;
            }
            JsonToken token = next;
            next = null;
            return token;
        }
    }
}