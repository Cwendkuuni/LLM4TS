package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;
import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.*;

public class JsonParserSequenceTest {

    @Test
    public void testCreateFlattened() throws IOException {
        JsonParser parser1 = mock(JsonParser.class);
        JsonParser parser2 = mock(JsonParser.class);
        JsonParser parser3 = mock(JsonParser.class);

        JsonParserSequence seq = JsonParserSequence.createFlattened(parser1, JsonParserSequence.createFlattened(parser2, parser3));

        assertEquals(3, seq.containedParsersCount());
        assertEquals(parser1, seq.delegate);

        List<JsonParser> parsers = new ArrayList<>();
        JsonParserSequence seq1 = JsonParserSequence.createFlattened(parser1, parser2);
        seq1.addFlattenedActiveParsers(parsers);
        assertEquals(2, parsers.size());
        assertEquals(parser2, parsers.get(0));
        assertEquals(parser1, parsers.get(1));

        JsonParserSequence seq2 = JsonParserSequence.createFlattened(parser2, parser3);
        seq1.addFlattenedActiveParsers(parsers);
        assertEquals(3, parsers.size());
        assertEquals(parser3, parsers.get(2));

        JsonParserSequence seq3 = JsonParserSequence.createFlattened(parser1, seq2);
        seq3.addFlattenedActiveParsers(parsers);
        assertEquals(4, parsers.size());
        assertEquals(parser3, parsers.get(3));
    }

    @Test
    public void testNextToken() throws IOException {
        JsonParser parser1 = mock(JsonParser.class);
        JsonParser parser2 = mock(JsonParser.class);

        when(parser1.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(parser2.nextToken()).thenReturn(JsonToken.VALUE_NUMBER_INT);

        JsonParserSequence seq = new JsonParserSequence(new JsonParser[] { parser1, parser2 });

        JsonToken token = seq.nextToken();
        assertEquals(JsonToken.START_ARRAY, token);

        token = seq.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, token);

        token = seq.nextToken();
        assertNull(token);

        token = seq.nextToken();
        assertNull(token);

        assertTrue(parser1.isClosed());
        assertTrue(parser2.isClosed());
    }

    @Test(expected = JsonProcessingException.class)
    public void testNextToken_IOExceptionThrown() throws IOException {
        JsonParser parser1 = mock(JsonParser.class);
        JsonParser parser2 = mock(JsonParser.class);

        when(parser1.nextToken()).thenThrow(new IOException());
        when(parser2.nextToken()).thenReturn(JsonToken.VALUE_STRING);

        JsonParserSequence seq = new JsonParserSequence(new JsonParser[] { parser1, parser2 });

        seq.nextToken();
        seq.nextToken();

        parser1.close();
        parser2.close();
    }

    @Test(expected = JsonParseException.class)
    public void testNextToken_JsonParseExceptionThrown() throws IOException {
        JsonParser parser1 = mock(JsonParser.class);
        JsonParser parser2 = mock(JsonParser.class);

        when(parser1.nextToken()).thenThrow(new JsonParseException());
        when(parser2.nextToken()).thenReturn(JsonToken.VALUE_NUMBER_INT);

        JsonParserSequence seq = new JsonParserSequence(new JsonParser[] { parser1, parser2 });

        seq.nextToken();
        seq.nextToken();

        parser1.close();
        parser2.close();
    }

    @Test
    public void testClose() throws IOException {
        JsonParser parser1 = mock(JsonParser.class);
        JsonParser parser2 = mock(JsonParser.class);

        when(parser1.close()).thenReturn(true);
        when(parser2.close()).thenReturn(true);

        JsonParserSequence seq = new JsonParserSequence(new JsonParser[] { parser1, parser2 });
        seq.close();

        assertTrue(parser1.isClosed());
        assertTrue(parser2.isClosed());
    }
}