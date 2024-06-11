package com.fasterxml.jackson.core.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;

public class JsonParserSequenceTest {

    @Test
    public void testCreateFlattened() throws IOException {
        JsonParser parser1 = new TestParser();
        JsonParser parser2 = new TestParser();
        JsonParser parser3 = new TestParser();

        JsonParserSequence sequence = JsonParserSequence.createFlattened(parser1, parser2);
        assertSame(parser1, sequence.getDelegate());
        assertEquals(2, sequence.containedParsersCount());

        sequence.add(parser3);
        assertEquals(3, sequence.containedParsersCount());

        JsonParserSequence sequence2 = JsonParserSequence.createFlattened(sequence, parser2);
        assertSame(parser1, sequence2.getDelegate());
        assertEquals(3, sequence2.containedParsersCount());
    }

    @Test
    public void testNextToken() throws IOException {
        JsonParser parser1 = new TestParser();
        JsonParser parser2 = new TestParser();
        JsonParserSequence sequence = JsonParserSequence.createFlattened(parser1, parser2);

        JsonToken token = sequence.nextToken();
        assertNotNull(token);
        assertEquals(JsonToken.START_OBJECT, token.getId());

        sequence.close();

        token = sequence.nextToken();
        assertNull(token);

        parser1 = new TestParser();
        parser2 = new TestParser();
        sequence = JsonParserSequence.createFlattened(parser1, parser2);

        parser1.nextToken();
        parser1.nextToken();

        token = sequence.nextToken();
        assertNotNull(token);
        assertEquals(JsonToken.START_OBJECT, token.getId());
    }

    @Test
    public void testSwitchToNext() throws IOException {
        JsonParser parser1 = new TestParser();
        JsonParser parser2 = new TestParser();
        JsonParser parser3 = new TestParser();
        JsonParserSequence sequence = JsonParserSequence.createFlattened(parser1, parser2, parser3);

        JsonToken token = sequence.nextToken();
        assertNotNull(token);
        assertEquals(JsonToken.START_OBJECT, token.getId());

        sequence.switchToNext();
        token = sequence.nextToken();
        assertNotNull(token);
        assertEquals(JsonToken.START_ARRAY, token.getId());

        sequence.switchToNext();
        assertNull(sequence.nextToken());
    }

    @Test
    public void testClose() throws IOException {
        JsonParser parser1 = new TestParser();
        JsonParser parser2 = new TestParser();
        JsonParser parser3 = new TestParser();
        JsonParserSequence sequence = JsonParserSequence.createFlattened(parser1, parser2, parser3);

        sequence.nextToken();
        sequence.nextToken();

        sequence.close();
        sequence.nextToken();
        assertNull(sequence.nextToken());
    }

    private static class TestParser extends JsonParser {
        @Override
        public JsonToken nextToken() throws IOException, JsonParseException {
            return new TestToken();
        }
    }

    private static class TestToken extends JsonToken {
        public TestToken() {
            super(JsonTokenId.START_OBJECT);
        }
    }
}