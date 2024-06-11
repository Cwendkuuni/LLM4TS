package com.fasterxml.jackson.core.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class JsonParserSequenceTest {

    @Test
    public void testCreateFlattened() {
        JsonParser parser1 = mock(JsonParser.class);
        JsonParser parser2 = mock(JsonParser.class);
        JsonParser sequence = JsonParserSequence.createFlattened(parser1, parser2);
        assertEquals(2, sequence.containedParsersCount());
    }

    @Test
    public void testNextToken_activeParserHasToken() {
        JsonParser parser1 = mock(JsonParser.class);
        when(parser1.nextToken()).thenReturn(JsonToken.START_OBJECT);

        JsonParser parser2 = mock(JsonParser.class);
        when(parser2.nextToken()).thenReturn(JsonToken.VALUE_STRING);

        JsonParser sequence = new JsonParserSequence(new JsonParser[] { parser1, parser2 });
        JsonToken token = sequence.nextToken();
        assertEquals(JsonToken.START_OBJECT, token);
    }

    @Test
    public void testNextToken_activeParserHasNoToken() {
        JsonParser parser1 = mock(JsonParser.class);
        JsonParser parser2 = mock(JsonParser.class);

        when(parser1.nextToken()).thenReturn(null);

        JsonParser sequence = new JsonParserSequence(new JsonParser[] { parser1, parser2 });
        JsonToken token = sequence.nextToken();
        assertNull(token);

        assertTrue(sequence.switchToNext());
        token = sequence.nextToken();
        assertNotNull(token);
    }

    @Test
    public void testNextToken_allParsersHaveNoToken() {
        JsonParser parser1 = mock(JsonParser.class);
        JsonParser parser2 = mock(JsonParser.class);

        when(parser1.nextToken()).thenReturn(null);
        when(parser2.nextToken()).thenReturn(null);

        JsonParser sequence = new JsonParserSequence(new JsonParser[] { parser1, parser2 });
        JsonToken token = sequence.nextToken();
        assertNull(token);

        assertFalse(sequence.switchToNext());
        token = sequence.nextToken();
        assertNull(token);
    }

    @Test
    public void testClose() throws IOException {
        JsonParser parser1 = mock(JsonParser.class);
        JsonParser parser2 = mock(JsonParser.class);

        when(parser1.close()).thenThrow(IOException.class);

        JsonParser sequence = new JsonParserSequence(new JsonParser[] { parser1, parser2 });
        sequence.close();

        assertTrue(parser1.isClosed());
        assertTrue(parser2.isClosed());
    }
}