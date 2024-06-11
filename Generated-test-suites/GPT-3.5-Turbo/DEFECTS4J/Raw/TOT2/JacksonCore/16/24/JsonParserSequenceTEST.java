package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class JsonParserSequenceTest {

    @Test
    public void testConstruction() {
        JsonParser[] parsers = new JsonParser[] {new JsonParser(), new JsonParser()};
        JsonParserSequence jsonParserSequence = new JsonParserSequence(parsers);
        assertEquals(2, jsonParserSequence.containedParsersCount());
    }
    
    @Test
    public void testCreateFlattened() {
        JsonParser first = new JsonParser();
        JsonParser second = new JsonParser();
        JsonParserSequence jsonParserSequence = JsonParserSequence.createFlattened(first, second);
        assertEquals(2, jsonParserSequence.containedParsersCount());
    }
    
    @Test
    public void testNextToken() throws IOException, JsonParseException {
        JsonParser[] parsers = new JsonParser[] {new JsonParser(), new JsonParser()};
        JsonParserSequence jsonParserSequence = new JsonParserSequence(parsers);
        assertEquals(JsonToken.START_OBJECT, jsonParserSequence.nextToken());
    }
    
    @Test
    public void testSwitchToNext() {
        JsonParser first = new JsonParser();
        JsonParser second = new JsonParser();
        JsonParserSequence jsonParserSequence = JsonParserSequence.createFlattened(first, second);
        assertTrue(jsonParserSequence.switchToNext());
    }
}