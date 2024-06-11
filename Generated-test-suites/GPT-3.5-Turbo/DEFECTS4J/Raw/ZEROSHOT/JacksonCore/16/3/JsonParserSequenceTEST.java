
package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class JsonParserSequenceTest {

    @Test
    public void testCreateFlattened() throws IOException {
        JsonParser parser1 = mock(JsonParser.class);
        JsonParser parser2 = mock(JsonParser.class);

        JsonParserSequence sequence = JsonParserSequence.createFlattened(parser1, parser2);

        verify(parser1).close();
        verify(parser2).close();

        assertEquals(2, sequence.containedParsersCount());
        assertArrayEquals(new JsonParser[]{parser1, parser2}, sequence._parsers);
    }

    @Test
    public void testContainedParsersCount() {
        JsonParser parser1 = mock(JsonParser.class);
        JsonParser parser2 = mock(JsonParser.class);
        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[]{parser1, parser2});

        assertEquals(2, sequence.containedParsersCount());
    }

    @Test
    public void testClose() throws IOException {
        JsonParser parser1 = mock(JsonParser.class);
        JsonParser parser2 = mock(JsonParser.class);
        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[]{parser1, parser2});

        sequence.close();

        verify(parser1).close();
        verify(parser2).close();
    }

    @Test
    public void testNextToken() throws IOException, JsonParseException {
        JsonParser parser1 = mock(JsonParser.class);
        JsonParser parser2 = mock(JsonParser.class);
        JsonToken token1 = mock(JsonToken.class);
        JsonToken token2 = mock(JsonToken.class);
        when(parser1.nextToken()).thenReturn(null);
        when(parser2.nextToken()).thenReturn(token2);
        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[]{parser1, parser2});

        JsonToken actualToken = sequence.nextToken();

        verify(parser1).nextToken();
        verify(parser2).nextToken();
        assertEquals(token2, actualToken);
    }

    @Test
    public void testNextTokenNoToken() throws IOException, JsonParseException {
        JsonParser parser1 = mock(JsonParser.class);
        JsonParser parser2 = mock(JsonParser.class);
        when(parser1.nextToken()).thenReturn(null);
        when(parser2.nextToken()).thenReturn(null);
        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[]{parser1, parser2});

        JsonToken actualToken = sequence.nextToken();

        verify(parser1).nextToken();
        verify(parser2).nextToken();
        assertNull(actualToken);
    }

    @Test
    public void testSwitchToNextSucceeds() {
        JsonParser parser1 = mock(JsonParser.class);
        JsonParser parser2 = mock(JsonParser.class);
        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[]{parser1, parser2});

        boolean result = sequence.switchToNext();

        assertEquals(parser2, sequence.delegate);
        assertTrue(result);
    }

    @Test
    public void testSwitchToNextFails() {
        JsonParser parser1 = mock(JsonParser.class);
        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[]{parser1});

        boolean result = sequence.switchToNext();

        assertEquals(parser1, sequence.delegate);
        assertFalse(result);
    }

    @Test
    public void testAddFlattenedActiveParsers() {
        JsonParser parser1 = mock(JsonParser.class);
        JsonParser parser2 = mock(JsonParser.class);
        JsonParser parser3 = mock(JsonParser.class);
        JsonParser parser4 = mock(JsonParser.class);
        JsonParserSequence subSequence = spy(new JsonParserSequence(new JsonParser[]{parser3, parser4}));
        when(subSequence.containedParsersCount()).thenReturn(2);
        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[]{parser1, parser2, subSequence});

        List<JsonParser> result = new ArrayList<>();
        sequence.addFlattenedActiveParsers(result);

        assertEquals(4, result.size());
        assertArrayEquals(new JsonParser[]{parser1, parser2, parser3, parser4}, result.toArray());
        verify(subSequence, times(1)).addFlattenedActiveParsers(anyList());
    }
}
