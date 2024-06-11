package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;

/**
 * Test class for {@link JsonParserSequence} class
 */
public class JsonParserSequenceTest {

    @Test
    public void testJsonParserSequenceConstructor() throws IOException {
        // Test constructor with valid arguments
        JsonParser parser1 = mock(JsonParser.class);
        JsonParser parser2 = mock(JsonParser.class);
        JsonParserSequence jsonParserSequence = new JsonParserSequence(new JsonParser[] { parser1, parser2 });
        assertEquals(parser1, jsonParserSequence.getDelegate());
        assertEquals(parser2, jsonParserSequence.getNextParser());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJsonParserSequenceConstructorInvalidArguments() {
        // Test constructor with invalid arguments
        JsonParser parser = mock(JsonParser.class);
        JsonParserSequence jsonParserSequence = new JsonParserSequence(new JsonParser[] { parser, parser });
    }

    @Test
    public void testCreateFlattened() throws IOException {
        // Test createFlattened method
        JsonParser parser1 = mock(JsonParser.class);
        JsonParser parser2 = mock(JsonParser.class);
        JsonParser parser3 = mock(JsonParser.class);

        JsonParserSequence jsonParserSequence1 = new JsonParserSequence(new JsonParser[] { parser1, parser2 });
        JsonParserSequence jsonParserSequence2 = new JsonParserSequence(new JsonParser[] { parser2, parser3 });

        JsonParserSequence result = JsonParserSequence.createFlattened(jsonParserSequence1, jsonParserSequence2);

        List<JsonParser> parsers = new ArrayList<>();
        jsonParserSequence1.addFlattenedActiveParsers(parsers);
        jsonParserSequence2.addFlattenedActiveParsers(parsers);

        assertEquals(parser1, result.getDelegate());
        assertEquals(parser2, result.getNextParser());
        assertEquals(3, result.containedParsersCount());
        assertEquals(parsers.size(), result.containedParsersCount());
    }

    @Test
    public void testNextToken() throws IOException, JsonParseException {
        // Test nextToken method
        JsonParser parser1 = mock(JsonParser.class);
        JsonParser parser2 = mock(JsonParser.class);

        when(parser1.nextToken()).thenReturn(JsonToken.START_OBJECT);
        when(parser2.nextToken()).thenReturn(JsonToken.START_ARRAY);

        JsonParserSequence jsonParserSequence = new JsonParserSequence(new JsonParser[] { parser1, parser2 });

        JsonToken token = jsonParserSequence.nextToken();

        assertEquals(JsonToken.START_OBJECT, token);

        token = jsonParserSequence.nextToken();
        assertEquals(JsonToken.START_ARRAY, token);

        token = jsonParserSequence.nextToken(); // Should throw exception
        try {
            jsonParserSequence.nextToken();
        } catch (JsonParseException e) {
            assertEquals("No token to read from root parser", e.getMessage());
        }
    }

    @Test
    public void testClose() throws IOException {
        // Test close method
        JsonParser parser = mock(JsonParser.class);
        JsonParserSequence jsonParserSequence = new JsonParserSequence(new JsonParser[] { parser });

        jsonParserSequence.close();

        verify(parser).close();
    }

    @Test
    public void testContainedParsersCount() throws IOException {
        // Test containedParsersCount method
        JsonParser parser1 = mock(JsonParser.class);
        JsonParser parser2 = mock(JsonParser.class);

        JsonParserSequence jsonParserSequence = new JsonParserSequence(new JsonParser[] { parser1, parser2 });

        assertEquals(2, jsonParserSequence.containedParsersCount());
    }
}