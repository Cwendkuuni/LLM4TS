package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

/**
 * Test class for {@link JsonParserSequence}
 */
public class JsonParserSequenceTest {

    private static JsonParser createParser(String json) throws IOException {
        JsonFactory factory = new JsonFactory();
        return factory.createParser(new StringReader(json));
    }

    @Test
    public void testCreateFlattened() throws IOException {
        String json1 = "[1, 2, 3]";
        String json2 = "[4, 5]";
        JsonParser parser1 = createParser(json1);
        JsonParser parser2 = createParser(json2);

        JsonParser parserSequence = JsonParserSequence.createFlattened(parser1, parser2);

        // Test that we correctly added both parsers
        assertEquals(2, parserSequence.containedParsersCount());

        // Test that we get the expected first token
        JsonToken token = parserSequence.nextToken();
        assertEquals(JsonToken.START_ARRAY, token.getId());

        // Test that we get the expected second token
        token = parserSequence.nextToken();
        assertEquals(JsonToken.INT, token.getId());
        assertEquals(1, token.intValue());

        // Test that we get the expected third token
        token = parserSequence.nextToken();
        assertEquals(JsonToken.INT, token.getId());
        assertEquals(2, token.intValue());

        // Test that we get the expected fourth token
        token = parserSequence.nextToken();
        assertEquals(JsonToken.START_ARRAY, token.getId());

        // Test that we get the expected fifth token
        token = parserSequence.nextToken();
        assertEquals(JsonToken.INT, token.getId());
        assertEquals(4, token.intValue());

        // Test that we get the expected sixth token
        token = parserSequence.nextToken();
        assertEquals(JsonToken.INT, token.getId());
        assertEquals(5, token.intValue());

        // Test that we get null as expected since we reached end of input
        token = parserSequence.nextToken();
        assertNull(token);
    }

    @Test
    public void testSwitchToNext() throws IOException {
        String json1 = "[1, 2, 3]";
        String json2 = "[4, 5]";
        JsonParser parser1 = createParser(json1);
        JsonParser parser2 = createParser(json2);

        JsonParser parserSequence = new JsonParserSequence(new JsonParser[] { parser1, parser2 });

        // Test that we get the expected first token
        JsonToken token = parserSequence.nextToken();
        assertEquals(JsonToken.START_ARRAY, token.getId());

        // Test that we switch to the second parser and get the expected second token
        parserSequence.switchToNext();
        token = parserSequence.nextToken();
        assertEquals(JsonToken.START_ARRAY, token.getId());

        // Test that we get the expected third token from the second parser
        token = parserSequence.nextToken();
        assertEquals(JsonToken.INT, token.getId());
        assertEquals(4, token.intValue());

        // Test that we switch back to the first parser
        parserSequence.switchToNext();
        parserSequence.switchToNext();
        parserSequence.switchToNext();

        // Test that we get the expected fourth token from the first parser
        token = parserSequence.nextToken();
        assertEquals(JsonToken.INT, token.getId());
        assertEquals(3, token.intValue());

        // Test that we reach end of input
        token = parserSequence.nextToken();
        assertNull(token);
    }

    @Test
    public void testClose() throws IOException {
        String json1 = "[1, 2, 3]";
        String json2 = "[4, 5]";
        JsonParser parser1 = createParser(json1);
        JsonParser parser2 = createParser(json2);

        JsonParser parserSequence = new JsonParserSequence(new JsonParser[] { parser1, parser2 });

        // Test that we correctly close all parsers
        parserSequence.close();

        // Test that we get an IOException when trying to read more data
        try {
            parserSequence.nextToken();
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }
}