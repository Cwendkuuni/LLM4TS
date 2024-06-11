package com.fasterxml.jackson.core.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class JsonParserSequenceTest {

    @Test
    public void testCreateFlattened() throws IOException {
        JsonParser parser1 = createMockParser();
        JsonParser parser2 = createMockParser();
        JsonParser sequence = JsonParserSequence.createFlattened(parser1, parser2);
        assertEquals(2, sequence.containedParsersCount());
        assertSame(parser1, sequence.delegate);
        JsonToken token = sequence.nextToken();
        assertEquals(JsonToken.START_OBJECT, token.getId());
        assertFalse(parser1.isClosed());
        assertFalse(parser2.isClosed());

        sequence = JsonParserSequence.createFlattened(parser2, parser1);
        assertEquals(2, sequence.containedParsersCount());
        assertSame(parser2, sequence.delegate);
        token = sequence.nextToken();
        assertEquals(JsonToken.START_ARRAY, token.getId());
        assertFalse(parser1.isClosed());
        assertFalse(parser2.isClosed());

        sequence.close();
        assertTrue(parser1.isClosed());
        assertTrue(parser2.isClosed());
    }

    @Test
    public void testSwitchToNext() throws IOException {
        JsonParser parser1 = createMockParser();
        JsonParser parser2 = createMockParser();
        JsonParser sequence = new JsonParserSequence(new JsonParser[] { parser1, parser2 });
        assertEquals(2, sequence.containedParsersCount());
        assertSame(parser1, sequence.delegate);
        JsonToken token = sequence.nextToken();
        assertEquals(JsonToken.START_OBJECT, token.getId());
        assertFalse(parser1.isClosed());
        assertFalse(parser2.isClosed());

        sequence.switchToNext();
        assertSame(parser2, sequence.delegate);
        token = sequence.nextToken();
        assertEquals(JsonToken.START_ARRAY, token.getId());
        assertFalse(parser1.isClosed());
        assertFalse(parser2.isClosed());

        sequence.close();
        assertTrue(parser1.isClosed());
        assertTrue(parser2.isClosed());
    }

    @Test
    public void testNextTokenWhenNoParsersLeft() throws IOException {
        JsonParser parser1 = createMockParser();
        JsonParser parser2 = createMockParser();
        JsonParser sequence = new JsonParserSequence(new JsonParser[] { parser1, parser2 });
        assertEquals(2, sequence.containedParsersCount());
        assertSame(parser1, sequence.delegate);
        JsonToken token = sequence.nextToken();
        assertEquals(JsonToken.START_OBJECT, token.getId());
        assertFalse(parser1.isClosed());
        assertFalse(parser2.isClosed());

        sequence.switchToNext();
        assertSame(parser2, sequence.delegate);
        token = sequence.nextToken();
        assertEquals(JsonToken.START_ARRAY, token.getId());
        assertFalse(parser1.isClosed());
        assertFalse(parser2.isClosed());

        sequence.switchToNext();
        assertNull(sequence.delegate);
        assertNull(sequence.nextToken());
        assertTrue(parser1.isClosed());
        assertTrue(parser2.isClosed());
    }

    @Test
    public void testClose() throws IOException {
        JsonParser parser1 = createMockParser();
        JsonParser parser2 = createMockParser();
        JsonParser sequence = new JsonParserSequence(new JsonParser[] { parser1, parser2 });
        assertEquals(2, sequence.containedParsersCount());
        assertSame(parser1, sequence.delegate);
        JsonToken token = sequence.nextToken();
        assertEquals(JsonToken.START_OBJECT, token.getId());
        assertFalse(parser1.isClosed());
        assertFalse(parser2.isClosed());

        sequence.close();
        assertTrue(parser1.isClosed());
        assertTrue(parser2.isClosed());

        sequence = new JsonParserSequence(new JsonParser[] { parser1, parser2 });
        assertEquals(2, sequence.containedParsersCount());
        assertSame(parser1, sequence.delegate);
        token = sequence.nextToken();
        assertEquals(JsonToken.START_OBJECT, token.getId());
        assertFalse(parser1.isClosed());
        assertFalse(parser2.isClosed());

        sequence.close();
        assertTrue(parser1.isClosed());
        assertTrue(parser2.isClosed());
    }

    @Test
    public void testNextTokenWhenNoTokensLeft() throws IOException {
        JsonParser parser1 = createMockParser();
        JsonParser parser2 = createMockParser();
        JsonParser sequence = new JsonParserSequence(new JsonParser[] { parser1, parser2 });
        assertEquals(2, sequence.containedParsersCount());
        assertSame(parser1, sequence.delegate);
        JsonToken token1 = sequence.nextToken();
        assertEquals(JsonToken.START_OBJECT, token1.getId());
        assertFalse(parser1.isClosed());
        assertFalse(parser2.isClosed());

        JsonToken token2 = sequence.nextToken();
        assertEquals(JsonToken.START_ARRAY, token2.getId());
        assertFalse(parser1.isClosed());
        assertFalse(parser2.isClosed());

        JsonToken token3 = sequence.nextToken();
        assertNull(token3);
        assertTrue(parser1.isClosed());
        assertTrue(parser2.isClosed());

        sequence = new JsonParserSequence(new JsonParser[] { parser1, parser2 });
        assertEquals(2, sequence.containedParsersCount());
        assertSame(parser1, sequence.delegate);
        JsonToken token4 = sequence.nextToken();
        assertNull(token4);
        assertTrue(parser1.isClosed());
        assertTrue(parser2.isClosed());
    }

    private JsonParser createMockParser() {
        return new JsonTestParser() {
            private int _nextToken = JsonToken.START_OBJECT.getId();

            @Override
            public JsonToken nextToken() throws IOException, JsonParseException {
                JsonToken token = JsonToken.values()[_nextToken++];
                if (token == null) {
                    close();
                }
                return token;
            }
        };
    }
}