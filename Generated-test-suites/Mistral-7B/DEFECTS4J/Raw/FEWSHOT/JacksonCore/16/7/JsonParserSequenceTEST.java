package com.fasterxml.jackson.core.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class JsonParserSequenceTest {

    @Test
    public void testCreateFlattened() throws IOException {
        JsonParser first = mockParser(JsonToken.START_ARRAY);
        JsonParser second = mockParser(JsonToken.VALUE_STRING);
        JsonParser third = mockParser(JsonToken.END_ARRAY);

        JsonParser sequence = JsonParserSequence.createFlattened(first, second, third);
        JsonToken token = sequence.nextToken();
        assertEquals(JsonToken.START_ARRAY, token.getId());

        token = sequence.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token.getId());

        token = sequence.nextToken();
        assertEquals(JsonToken.END_ARRAY, token.getId());

        // verify that all parsers were used
        verify(first).nextToken();
        verify(second).nextToken();
        verify(third).nextToken();
        verifyNoMoreInteractions(first);
        verifyNoMoreInteractions(second);
        verifyNoMoreInteractions(third);
    }

    @Test
    public void testCreateFlattenedEmpty() {
        JsonParser first = mockParser(JsonToken.START_ARRAY);
        JsonParser second = mockParser(JsonToken.START_ARRAY);

        JsonParser sequence = JsonParserSequence.createFlattened(first, second);
        JsonToken token = sequence.nextToken();
        assertEquals(JsonToken.START_ARRAY, token.getId());

        // verify that both parsers were used
        verify(first).nextToken();
        verify(second).nextToken();
        verifyNoMoreInteractions(first);
        verifyNoMoreInteractions(second);
    }

    @Test
    public void testCreateFlattenedRecursive() {
        JsonParser first = createMockParserSequence(JsonToken.START_ARRAY, JsonToken.VALUE_STRING, JsonToken.END_ARRAY);
        JsonParser second = createMockParserSequence(JsonToken.START_OBJECT, JsonToken.FIELD_NAME, JsonToken.VALUE_STRING, JsonToken.END_OBJECT);

        JsonParser sequence = JsonParserSequence.createFlattened(first, second);
        JsonToken token = sequence.nextToken();
        assertEquals(JsonToken.START_ARRAY, token.getId());

        token = sequence.nextToken();
        assertEquals(JsonToken.START_OBJECT, token.getId());

        token = sequence.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token.getId());

        token = sequence.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token.getId());

        token = sequence.nextToken();
        assertEquals(JsonToken.END_OBJECT, token.getId());

        token = sequence.nextToken();
        assertEquals(JsonToken.END_ARRAY, token.getId());

        // verify that both sequences were used
        verifySequenceInteractions(first);
        verifySequenceInteractions(second);
    }

    @Test
    public void testSwitchToNext() throws IOException {
        JsonParser first = mockParser(JsonToken.START_ARRAY);
        JsonParser second = mockParser(JsonToken.START_OBJECT);

        JsonParser sequence = new JsonParserSequence(new JsonParser[] { first, second });
        JsonToken token = sequence.nextToken();
        assertEquals(JsonToken.START_ARRAY, token.getId());

        token = sequence.nextToken();
        assertEquals(JsonToken.START_OBJECT, token.getId());

        // switch to next parser and verify that it was used
        assertTrue(sequence.switchToNext());
        assertEquals(JsonToken.NULL, sequence.nextToken().getId());

        // verify that first parser was not used again
        assertFalse(sequence.switchToNext());
        assertNull(sequence.nextToken());

        // verify that second parser was used once more
        assertTrue(sequence.switchToNext());
        assertEquals(JsonToken.END_OBJECT, sequence.nextToken().getId());

        // verify that no more parsers are left
        assertFalse(sequence.switchToNext());
        assertNull(sequence.nextToken());
    }

    @Test
    public void testContainedParsersCount() {
        JsonParser first = mockParser(JsonToken.START_ARRAY);
        JsonParser second = mockParser(JsonToken.START_OBJECT);
        JsonParser sequence = new JsonParserSequence(new JsonParser[] { first, second });

        int count = sequence.containedParsersCount();
        assertEquals(2, count);
    }

    private JsonParser mockParser(JsonToken token) {
        JsonParser parser = mock(JsonParser.class);
        when(parser.nextToken()).thenReturn(token);
        return parser;
    }

    private JsonParserSequence createMockParserSequence(JsonToken... tokens) {
        JsonParser[] parsers = new JsonParser[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            parsers[i] = mockParser(tokens[i]);
        }
        return new JsonParserSequence(parsers);
    }

    private void verifySequenceInteractions(JsonParser parser) throws IOException {
        verify(parser).nextToken();
        verify(parser).close();
    }
}