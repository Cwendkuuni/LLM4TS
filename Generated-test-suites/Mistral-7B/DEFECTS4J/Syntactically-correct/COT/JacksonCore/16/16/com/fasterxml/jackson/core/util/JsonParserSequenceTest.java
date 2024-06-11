package com.fasterxml.jackson.core.util;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;

public class JsonParserSequenceTest {

    private JsonParser parser1;
    private JsonParser parser2;
    private JsonParserSequence jsonParserSequence;

    @Before
    public void setUp() throws IOException {
        parser1 = new JsonFactory().createParser("[]");
        parser2 = new JsonFactory().createParser("{}");
        jsonParserSequence = JsonParserSequence.createFlattened(parser1, parser2);
    }

    
     // Basic test for constructor
    @Test
    public void testJsonParserSequenceConstructor() {
        JsonParserSequence jsonParserSequence = new JsonParserSequence(new JsonParser[] { parser1, parser2 });
        assertEquals(2, jsonParserSequence.containedParsersCount());
    }

    // Basic test for nextToken() method
    @Test
    public void testNextToken() throws IOException, JsonParseException {
        JsonToken token = jsonParserSequence.nextToken();
        assertNotNull(token);
        assertEquals(JsonToken.START_ARRAY, token.getId());

        token = jsonParserSequence.nextToken();
        assertNotNull(token);
        assertEquals(JsonToken.END_ARRAY, token.getId());

        token = jsonParserSequence.nextToken();
        assertNull(token);

        jsonParserSequence.switchToNext();

        token = jsonParserSequence.nextToken();
        assertNotNull(token);
        assertEquals(JsonToken.START_OBJECT, token.getId());

        // continue testing with other JsonToken types
    }

    // Test for edge cases and exceptions
    @Test(expected = IOException.class)
    public void testCloseMethod() throws IOException {
        jsonParserSequence.close();
        jsonParserSequence.close();
    }

    @Test
    public void testNextTokenAfterClosed() throws IOException, JsonParseException {
        jsonParserSequence.close();
        JsonToken token = jsonParserSequence.nextToken();
        assertNull(token);
    }

    @Test
    public void testSwitchToNextWithNoParsersLeft() {
        jsonParserSequence.switchToNext();
        assertFalse(jsonParserSequence.switchToNext());
    }

    @Test
    public void testSwitchToNextWithInvalidParserIndex() {
        JsonParser[] parsers = new JsonParser[3];
        JsonParserSequence jsonParserSequence = new JsonParserSequence(parsers);
        jsonParserSequence.switchToNext();
        jsonParserSequence.switchToNext();
        jsonParserSequence.switchToNext(); // out of bounds
        assertFalse(jsonParserSequence.switchToNext());
    }
    
}