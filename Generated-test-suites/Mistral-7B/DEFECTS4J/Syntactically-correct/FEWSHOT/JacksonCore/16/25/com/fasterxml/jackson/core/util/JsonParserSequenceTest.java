package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Arrays;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.test.base.JSONTestBase;

public class JsonParserSequenceTest extends JSONTestBase {

    @Test
    public void testCreateFlattened() throws IOException {
        JsonParser parser1 = mock(JsonParser.class);
        JsonParser parser2 = mock(JsonParser.class);
        JsonParserSequence sequence = JsonParserSequence.createFlattened(parser1, parser2);
        assertEquals(2, sequence.containedParsersCount());
        verify(parser1, never()).addFlattenedActiveParsers(any());
        verify(parser2, never()).addFlattenedActiveParsers(any());
        JsonParser activeParser = sequence.delegate;
        assertEquals(parser1, activeParser);

        JsonToken token = sequence.nextToken();
        assertNotNull(token);
        activeParser = sequence.delegate;
        verify(parser1, times(1)).nextToken();
        verifyNoMoreInteractions(parser2);
        token = sequence.nextToken();
        assertNotNull(token);
        activeParser = sequence.delegate;
        verify(parser2, times(1)).nextToken();
        verifyNoMoreInteractions(parser1);
    }

    @Test
    public void testSwitchToNext() throws IOException {
        JsonParser parser1 = mock(JsonParser.class);
        JsonParser parser2 = mock(JsonParser.class);
        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[] { parser1, parser2 });
        assertEquals(2, sequence.containedParsersCount());

        JsonToken token1 = sequence.nextToken();
        assertNotNull(token1);
        JsonParser activeParser1 = sequence.delegate;
        assertEquals(parser1, activeParser1);
        verify(parser1, times(1)).nextToken();

        JsonToken token2 = sequence.nextToken();
        assertNotNull(token2);
        JsonParser activeParser2 = sequence.delegate;
        assertEquals(parser2, activeParser2);
        verify(parser2, times(1)).nextToken();

        sequence.close();

        token1 = sequence.nextToken();
        assertNull(token1);
        activeParser1 = sequence.delegate;
        assertEquals(parser1, activeParser1);
        verify(parser1, times(2)).close();

        token2 = sequence.nextToken();
        assertNull(token2);
        activeParser2 = sequence.delegate;
        assertEquals(parser2, activeParser2);
        verify(parser2, times(2)).close();
    }

    @Test
    public void testCreateFlattenedWithNestedSequences() throws IOException {
        JsonParser parser1 = mock(JsonParser.class);
        JsonParser parser2 = mock(JsonParser.class);
        JsonParser parser3 = mock(JsonParser.class);
        JsonParserSequence parserSequence1 = new JsonParserSequence(new JsonParser[] { parser1, parser2 });
        JsonParserSequence parserSequence2 = new JsonParserSequence(new JsonParser[] { parser3 });

        JsonParserSequence sequence = JsonParserSequence.createFlattened(parserSequence1, parserSequence2);
        assertEquals(3, sequence.containedParsersCount());
        verify(parser1, never()).addFlattenedActiveParsers(any());
        verify(parser2, never()).addFlattenedActiveParsers(any());
        verify(parser3, never()).addFlattenedActiveParsers(any());
        JsonParser activeParser = sequence.delegate;
        assertEquals(parser1, activeParser);

        JsonToken token = sequence.nextToken();
        assertNotNull(token);
        activeParser = sequence.delegate;
        verify(parser1, times(1)).nextToken();
        JsonParser activeParser1 = parserSequence1.delegate;
        assertEquals(parser1, activeParser1);
        verify(parser2, never()).nextToken();
        verify(parser3, never()).nextToken();

        token = sequence.nextToken();
        assertNotNull(token);
        activeParser = sequence.delegate;
        verify(parser2, times(1)).nextToken();
        activeParser1 = parserSequence1.delegate;
        assertEquals(parser2, activeParser1);
        verify(parser1, times(1)).nextToken();
        verify(parser3, never()).nextToken();

        token = sequence.nextToken();
        assertNotNull(token);
        activeParser = sequence.delegate;
        verify(parser3, times(1)).nextToken();
        activeParser1 = parserSequence1.delegate;
        assertEquals(parser3, activeParser1.delegate);
        verify(parser1, times(2)).nextToken();
        verify(parser2, times(1)).nextToken();

        sequence.close();

        token = sequence.nextToken();
        assertNull(token);
        activeParser = sequence.delegate;
        assertEquals(parser1, activeParser);
        verify(parser1, times(3)).close();

        token = sequence.nextToken();
        assertNull(token);
        activeParser1 = parserSequence1.delegate;
        assertEquals(parser1, activeParser1);
        verify(parser1, times(3)).close();

        token = sequence.nextToken();
        assertNull(token);
        activeParser2 = parserSequence2.delegate;
        assertEquals(parser3, activeParser2);
        verify(parser3, times(1)).close();
    }

    @Test
    public void testCreateFlattenedWithSingleParser() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        JsonParserSequence sequence = JsonParserSequence.createFlattened(parser, null);
        assertEquals(1, sequence.containedParsersCount());
        verify(parser, times(1)).nextToken();
        verify(parser, times(1)).close();
    }

    @Test
    public void testCreateFlattenedWithNullParsers() {
        assertNull(JsonParserSequence.createFlattened(null, null));
    }

    @Test
    public void testCreateFlattenedWithNullParser() {
        JsonParser parser1 = mock(JsonParser.class);
        JsonParser nullParser = null;
        JsonParserSequence sequence = JsonParserSequence.createFlattened(parser1, nullParser);
        assertEquals(1, sequence.containedParsersCount());
        verify(parser1, times(1)).nextToken();
        verify(parser1, times(1)).close();
        verify(nullParser, never()).nextToken();
        verify(nullParser, never()).close();
    }

    @Test
    public void testCreateFlattenedWithEmptyArray() {
        JsonParser parser = mock(JsonParser.class);
        JsonParserSequence sequence = JsonParserSequence.createFlattened(parser, new JsonParser[0]);
        assertEquals(1, sequence.containedParsersCount());
        verify(parser, times(1)).nextToken();
        verify(parser, times(1)).close();
    }

    @Test
    public void testCreateFlattenedWithSingleSequenceParser() throws IOException {
        JsonParser parser1 = mock(JsonParser.class);
        JsonParser parser2 = mock(JsonParser.class);
        JsonParserSequence sequence1 = new JsonParserSequence(new JsonParser[] { parser1 });
        JsonParser sequenceParser = sequence1;
        JsonParserSequence sequence = JsonParserSequence.createFlattened(sequenceParser, parser2);
        assertEquals(2, sequence.containedParsersCount());
        verify(parser1, times(1)).nextToken();
        verify(parser1, times(1)).close();
        verify(parser2, times(0)).nextToken();
        verify(parser2, times(0)).close();
        JsonParser activeParser = sequence.delegate;
        assertEquals(parser1, activeParser);

        JsonToken token = sequence.nextToken();
        assertNotNull(token);
        activeParser = sequence.delegate;
        verify(parser1, times(1)).nextToken();

        token = sequence.nextToken();
        assertNotNull(token);
        activeParser = sequence.delegate;
        verify(parser2, times(1)).nextToken();

        sequence.close();

        token = sequence.nextToken();
        assertNull(token);
        activeParser = sequence.delegate;
        assertEquals(parser1, activeParser);
        verify(parser1, times(2)).close();

        token = sequence.nextToken();
        assertNull(token);
        activeParser = sequence.delegate;
        assertEquals(parser2, activeParser);
        verify(parser2, times(1)).close();
    }

    @Test
    public void testToString() {
        JsonParser parser1 = mock(JsonParser.class);
        JsonParser parser2 = mock(JsonParser.class);
        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[] { parser1, parser2 });
        String expected = "JsonParserSequence[parsers=[JsonParser@" +
                hashCode(parser1) + ", JsonParser@" + hashCode(parser2) + "]]";
        assertEquals(expected, sequence.toString());
    }

    private static int hashCode(Object object) {
        return object.hashCode();
    }
}