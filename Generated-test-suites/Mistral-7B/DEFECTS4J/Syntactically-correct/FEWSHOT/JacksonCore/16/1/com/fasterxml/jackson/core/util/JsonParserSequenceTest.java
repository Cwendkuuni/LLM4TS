package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;

import com.fasterxml.jackson.core.*;

public class JsonParserSequenceTest {

    @Test
    public void testCreateFlattened() throws IOException {
        JsonParser parser1 = mock(JsonParser.class);
        JsonParser parser2 = mock(JsonParser.class);
        JsonParser sequence = JsonParserSequence.createFlattened(parser1, parser2);
        assertEquals(2, sequence.containedParsersCount());

        verify(parser1, never()).addFlattenedActiveParsers(anyList());
        verify(parser2, never()).addFlattenedActiveParsers(anyList());
    }

    @Test
    public void testCreateFlattenedNestedSequences() throws IOException {
        JsonParser parser1 = mock(JsonParser.class);
        JsonParserSequence parserSequence1 = mock(JsonParserSequence.class);
        JsonParser parser2 = mock(JsonParser.class);
        JsonParser parser3 = mock(JsonParser.class);
        JsonParser sequence = JsonParserSequence.createFlattened(parser1, parserSequence1, parser2, parser3);
        assertEquals(4, sequence.containedParsersCount());

        verify(parser1, never()).addFlattenedActiveParsers(anyList());
        verify(parserSequence1).addFlattenedActiveParsers(anyList());
        verify(parser2, never()).addFlattenedActiveParsers(anyList());
        verify(parser3, never()).addFlattenedActiveParsers(anyList());
        verify(parserSequence1).addAll(Arrays.asList(parser2, parser3));
    }

    @Test
    public void testClose() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[] { parser });
        sequence.close();
        verify(parser).close();
    }

    @Test
    public void testNextToken() throws IOException, JsonParseException {
        JsonParser parser1 = mock(JsonParser.class);
        JsonParser parser2 = mock(JsonParser.class);
        JsonParser sequence = new JsonParserSequence(new JsonParser[] { parser1, parser2 });

        when(parser1.nextToken()).thenReturn(JsonToken.START_OBJECT);
        when(parser2.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        JsonToken token = sequence.nextToken();
        assertNotNull(token);
        assertEquals(JsonToken.VALUE_STRING, token);

        verify(parser1).nextToken();
        verify(parser2).nextToken();

        JsonToken nullToken = sequence.nextToken();
        assertNull(nullToken);

        verify(parser1, times(2)).nextToken();
        verify(parser2, times(1)).nextToken();
    }

    @Test
    public void testSwitchToNext() throws IOException, JsonParseException {
        JsonParser parser1 = mock(JsonParser.class);
        JsonParser parser2 = mock(JsonParser.class);
        JsonParser sequence = new JsonParserSequence(new JsonParser[] { parser1, parser2 });

        when(parser1.nextToken()).thenReturn(JsonToken.START_OBJECT);
        when(parser2.nextToken()).thenReturn(JsonToken.VALUE_STRING);

        JsonToken token1 = sequence.nextToken();
        assertNotNull(token1);
        assertEquals(JsonToken.VALUE_STRING, token1);

        JsonToken token2 = sequence.nextToken();
        assertNotNull(token2);
        verify(parser1).nextToken();
        verify(parser2).nextToken();

        assertTrue(sequence.switchToNext());

        JsonToken token3 = sequence.nextToken();
        assertNotNull(token3);
        verify(parser2).nextToken();
        verify(parser1, never()).nextToken();
    }

    @Test
    public void testSwitchToNextWhenNoMoreParsers() {
        JsonParser parser1 = mock(JsonParser.class);
        JsonParser parser2 = mock(JsonParser.class);
        JsonParser sequence = new JsonParserSequence(new JsonParser[] { parser1, parser2 });

        sequence.switchToNext();

        verify(parser1, times(1)).nextToken();
        verify(parser2, never()).nextToken();
    }
}