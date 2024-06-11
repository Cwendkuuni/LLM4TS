package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.core.*;

public class JsonParserSequenceTest {

    @Test
    public void testJsonParserSequenceConstruction() throws IOException {
        JsonParser parser1 = new StringContextsParser(new StringReader("{}"));
        JsonParser parser2 = new StringContextsParser(new StringReader("[]"));
        JsonParserSequence jsonParserSequence = JsonParserSequence.createFlattened(parser1, parser2);
        assertEquals(2, jsonParserSequence.containedParsersCount());
    }

    @Test
    public void testJsonParserSequenceNextToken() throws IOException {
        JsonParser parser1 = new StringContextsParser(new StringReader("{\"name\":\"John\",\"age\":30}"));
        JsonParser parser2 = new StringContextsParser(new StringReader("[]"));
        JsonParserSequence jsonParserSequence = JsonParserSequence.createFlattened(parser1, parser2);

        JsonToken token = jsonParserSequence.nextToken();
        assertEquals(JsonToken.START_OBJECT, token.getId());
        token = jsonParserSequence.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token.getId());
        assertEquals("name", token.getText());
        token = jsonParserSequence.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token.getId());
        assertEquals("John", token.getText());

        // Switch to next parser
        jsonParserSequence.nextToken();
        jsonParserSequence.nextToken(); // consume [ ]
        JsonToken tokenFromSecondParser = jsonParserSequence.nextToken();
        assertEquals(JsonToken.END_ARRAY, tokenFromSecondParser.getId());
    }

    @Test(expected = IOException.class)
    public void testJsonParserSequenceClose() throws IOException {
        JsonParser parser1 = new StringContextsParser(new StringReader("{}"));
        JsonParser parser2 = new StringContextsParser(new StringReader("[]"));
        JsonParserSequence jsonParserSequence = JsonParserSequence.createFlattened(parser1, parser2);

        jsonParserSequence.close();
        jsonParserSequence.nextToken(); // should throw IOException
    }

    @Test
    public void testJsonParserSequenceSwitchToNextParser() throws IOException {
        JsonParser parser1 = new StringContextsParser(new StringReader("{}"));
        JsonParser parser2 = new StringContextsParser(new StringReader("[]"));
        JsonParserSequence jsonParserSequence = JsonParserSequence.createFlattened(parser1, parser2);

        JsonToken token = jsonParserSequence.nextToken();
        assertEquals(JsonToken.START_OBJECT, token.getId());
        boolean switchSucceeded = jsonParserSequence.switchToNext();
        assertTrue(switchSucceeded);
        token = jsonParserSequence.nextToken();
        assertEquals(JsonToken.START_ARRAY, token.getId());

        // Switch back to first parser
        jsonParserSequence.switchToNext();
        jsonParserSequence.nextToken(); // consume { }
        jsonParserSequence.nextToken(); // consume "name"
        jsonParserSequence.nextToken(); // consume "John"
        JsonToken tokenFromFirstParser = jsonParserSequence.nextToken();
        assertEquals(JsonToken.FIELD_NAME, tokenFromFirstParser.getId());
        assertEquals("age", tokenFromFirstParser.getText());
    }
}