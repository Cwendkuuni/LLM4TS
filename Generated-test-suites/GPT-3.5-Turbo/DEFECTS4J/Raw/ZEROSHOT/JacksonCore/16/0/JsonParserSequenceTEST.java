package com.fasterxml.jackson.core.util.test;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.util.JsonParserSequence;

public class JsonParserSequenceTest {

    private JsonParserSequence jsonParserSequence;
    private JsonParser[] parsers;

    @Before
    public void setUp() {
        parsers = new JsonParser[3];
        for (int i = 0; i < parsers.length; i++) {
            parsers[i] = new JsonParserMock();
        }
        jsonParserSequence = new JsonParserSequence(parsers);
    }

    @Test
    public void testCreateFlattened() {
        JsonParser first = new JsonParserMock();
        JsonParser second = new JsonParserMock();
        JsonParserSequence result = JsonParserSequence.createFlattened(first, second);
        assertEquals(2, result.containedParsersCount());

        JsonParser third = new JsonParserMock();
        JsonParserSequence nestedSequence = new JsonParserSequence(new JsonParser[] { first, second });
        result = JsonParserSequence.createFlattened(nestedSequence, third);
        assertEquals(3, result.containedParsersCount());

        JsonParserSequence nestedSequence2 = new JsonParserSequence(new JsonParser[] { nestedSequence, third });
        result = JsonParserSequence.createFlattened(nestedSequence2, first);
        assertEquals(4, result.containedParsersCount());
    }

    @Test
    public void testNextToken() throws IOException {
        assertEquals(JsonToken.START_OBJECT, jsonParserSequence.nextToken());
        jsonParserSequence.close();

        jsonParserSequence = new JsonParserSequence(new JsonParser[] {});
        assertNull(jsonParserSequence.nextToken());
    }

    @Test
    public void testContainedParsersCount() {
        assertEquals(3, jsonParserSequence.containedParsersCount());
    }

    private class JsonParserMock extends JsonParser {

        @Override
        public JsonToken nextToken() throws IOException {
            return null;
        }

        @Override
        public void close() throws IOException {
        }
    }
}