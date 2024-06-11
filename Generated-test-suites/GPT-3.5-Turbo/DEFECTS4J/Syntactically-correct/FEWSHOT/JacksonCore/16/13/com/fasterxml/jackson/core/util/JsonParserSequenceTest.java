package com.fasterxml.jackson.core.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonToken;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class JsonParserSequenceTest {

    @Test
    public void testClose() throws IOException {
        // Test case 1
        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[] {});
        sequence.close();

        // Test case 2
        JsonParser firstParser = new FakeJsonParser();
        JsonParser secondParser = new FakeJsonParser();
        sequence = JsonParserSequence.createFlattened(firstParser, secondParser);
        sequence.close();
    }

    @Test
    public void testNextToken() throws IOException, JsonParseException {
        // Test case 1
        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[] {});
        JsonToken token = sequence.nextToken();
        assertNull(token);

        // Test case 2
        JsonParser firstParser = new FakeJsonParser();
        JsonParser secondParser = new FakeJsonParser();
        sequence = JsonParserSequence.createFlattened(firstParser, secondParser);

        token = sequence.nextToken();
        assertEquals(JsonToken.START_OBJECT, token);
        token = sequence.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        token = sequence.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token);
        token = sequence.nextToken();
        assertEquals(JsonToken.END_OBJECT, token);

        token = sequence.nextToken();
        assertEquals(JsonToken.START_OBJECT, token);
        token = sequence.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        token = sequence.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token);
        token = sequence.nextToken();
        assertEquals(JsonToken.END_OBJECT, token);

        token = sequence.nextToken();
        assertNull(token);
    }

    @Test
    public void testContainedParsersCount() {
        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[] {});
        int count = sequence.containedParsersCount();
        assertEquals(0, count);

        JsonParser firstParser = new FakeJsonParser();
        JsonParser secondParser = new FakeJsonParser();
        sequence = JsonParserSequence.createFlattened(firstParser, secondParser);
        count = sequence.containedParsersCount();
        assertEquals(2, count);
    }

    private class FakeJsonParser extends JsonParser {
        @Override
        public JsonToken nextToken() throws IOException, JsonParseException {
            return null;
        }

        @Override
        public void close() throws IOException {

        }
    }
}