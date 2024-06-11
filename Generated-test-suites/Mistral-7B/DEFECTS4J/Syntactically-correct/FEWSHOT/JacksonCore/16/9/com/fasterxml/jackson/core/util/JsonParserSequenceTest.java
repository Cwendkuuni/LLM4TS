package com.fasterxml.jackson.core.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

public class JsonParserSequenceTest {

    @Test
    public void testCreateFlattened() throws IOException {
        JsonParser parser1 = new MockParser();
        JsonParser parser2 = new MockParser();
        JsonParser parser3 = new MockParser();

        JsonParserSequence sequence = JsonParserSequence.createFlattened(parser1, parser2);
        JsonParser[] expectedParsers = new JsonParser[] { parser1, parser2 };
        assertEquals(expectedParsers.length, sequence.containedParsersCount());
        assertFalse(sequence instanceof JsonParserSequence);

        sequence = JsonParserSequence.createFlattened(parser1, parser2, parser3);
        expectedParsers = new JsonParser[] { parser1, parser2, parser3 };
        assertEquals(expectedParsers.length, sequence.containedParsersCount());
        assertFalse(sequence instanceof JsonParserSequence);

        // Test with JsonParserSequence as a parsing component
        JsonParserSequence parserSequence1 = new JsonParserSequence(new JsonParser[] { parser1, parser2 });
        sequence = JsonParserSequence.createFlattened(parser1, parserSequence1);
        expectedParsers = new JsonParser[] { parser1, parser2, parser1, parser2 };
        assertEquals(expectedParsers.length, sequence.containedParsersCount());
        assertFalse(sequence instanceof JsonParserSequence);

        // Test with null as a parsing component
        JsonParser parserNull = null;
        sequence = JsonParserSequence.createFlattened(parser1, parserNull);
        assertEquals(1, sequence.containedParsersCount());
        assertTrue(sequence instanceof JsonParserSequence);
    }

    @Test
    public void testSwitchToNext() throws IOException {
        JsonParser parser1 = new MockParser();
        JsonParser parser2 = new MockParser();
        JsonParser parser3 = new MockParser();

        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[] { parser1, parser2, parser3 });

        assertEquals(parser1, sequence.delegate);
        assertTrue(sequence.switchToNext());
        assertEquals(parser2, sequence.delegate);
        assertTrue(sequence.switchToNext());
        assertEquals(parser3, sequence.delegate);
        assertFalse(sequence.switchToNext());
    }

    @Test
    public void testClose() throws IOException {
        JsonParser parser1 = new MockParser();
        JsonParser parser2 = new MockParser();

        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[] { parser1, parser2 });

        sequence.delegate.close();
        assertTrue(sequence.switchToNext());
        sequence.delegate.close();
        assertTrue(sequence.switchToNext());
        assertFalse(sequence.switchToNext());
    }

    @Test
    public void testNextToken() throws IOException {
        JsonParser parser1 = new MockParser();
        JsonParser parser2 = new MockParser();

        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[] { parser1, parser2 });

        JsonToken token = sequence.nextToken();
        assertEquals(JsonToken.START_OBJECT, token);
        assertFalse(sequence.switchToNext());
        token = parser1.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        assertTrue(sequence.switchToNext());
        token = parser2.nextToken();
        assertEquals(JsonToken.START_ARRAY, token);
        assertFalse(sequence.switchToNext());
        token = parser1.nextToken();
        assertEquals(JsonToken.END_OBJECT, token);
        assertFalse(sequence.switchToNext());
        token = parser2.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token);
        assertTrue(sequence.switchToNext());
        assertNull(sequence.nextToken());
    }

    @Test
    public void testNextTokenFailure() throws IOException {
        JsonParser parser1 = new MockParser();
        JsonParser parser2 = new MockParser();

        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[] { parser1, parser2 });

        JsonToken token = sequence.nextToken();
        assertEquals(JsonToken.START_OBJECT, token);
        assertFalse(sequence.switchToNext());

        // Test exception when no parser left
        try {
            sequence.nextToken();
            fail("Expected JsonParseException");
        } catch (JsonParseException e) {
            // Expected
        }

        // Test exception when parser fails
        parser1 = new MockParserWithException();
        sequence = new JsonParserSequence(new JsonParser[] { parser1, parser2 });

        token = sequence.nextToken();
        assertEquals(JsonToken.START_OBJECT, token);
        assertFalse(sequence.switchToNext());

        try {
            sequence.nextToken();
            fail("Expected JsonParseException");
        } catch (JsonParseException e) {
            // Expected
        }

        // Test exception when current parser fails but next parser succeeds
        parser2 = new MockParserWithException();
        sequence = new JsonParserSequence(new JsonParser[] { parser1, parser2 });

        token = sequence.nextToken();
        assertEquals(JsonToken.START_OBJECT, token);
        assertFalse(sequence.switchToNext());

        try {
            sequence.nextToken();
            fail("Expected JsonParseException");
        } catch (JsonParseException e) {
            // Expected
        }

        token = parser2.nextToken();
        assertEquals(JsonToken.START_ARRAY, token);
        assertTrue(sequence.switchToNext());
        token = parser1.nextToken();
        assertEquals(JsonToken.END_OBJECT, token);
        assertFalse(sequence.switchToNext());
        token = parser2.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token);
        assertFalse(sequence.switchToNext());
        assertNull(sequence.nextToken());
    }
}

class MockParser implements JsonParser {
    @Override
    public void close() throws IOException {
    }

    @Override
    public JsonToken nextToken() throws IOException, JsonParseException {
        return JsonToken.START_OBJECT;
    }
}

class MockParserWithException implements JsonParser {
    @Override
    public void close() throws IOException {
    }

    @Override
    public JsonToken nextToken() throws IOException, JsonParseException {
        throw new JsonParseException("Test exception");
    }
}