package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonParserSequenceTest {

    @Test
    public void testCreateFlattened() {
        JsonParser parser1 = mock(JsonParser.class);
        JsonParser parser2 = mock(JsonParser.class);
        JsonParser parser3 = mock(JsonParser.class);

        JsonParserSequence seq1 = new JsonParserSequence(new JsonParser[] { parser1, parser2 });
        JsonParserSequence seq2 = new JsonParserSequence(new JsonParser[] { parser2, parser3 });

        JsonParser expectedSeq = new JsonParserSequence(new JsonParser[] { parser1, parser2, parser3 });
        JsonParser actualSeq = JsonParserSequence.createFlattened(parser1, parser2);

        assertEquals(expectedSeq, actualSeq);
        verify(parser1, times(1)).delegate = any(JsonParser.class);
        verify(parser2, times(1)).delegate = any(JsonParser.class);

        JsonParser parser4 = mock(JsonParser.class);
        JsonParser expectedSeq2 = new JsonParserSequence(new JsonParser[] { parser1, parser2, parser3, parser4 });
        JsonParser actualSeq2 = JsonParserSequence.createFlattened(parser1, parser2, parser3, parser4);

        assertEquals(expectedSeq2, actualSeq2);
        verify(parser1, times(1)).delegate = any(JsonParser.class);
        verify(parser2, times(1)).delegate = any(JsonParser.class);
        verify(parser3, times(1)).delegate = any(JsonParser.class);
        verify(parser4, times(1)).delegate = any(JsonParser.class);
    }

    @Test
    public void testNextToken() throws IOException, JsonParseException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser1 = mapper.getFactory().createParser("[]");
        JsonParser parser2 = mapper.getFactory().createParser("{\"foo\":1}");

        JsonParserSequence seq = new JsonParserSequence(new JsonParser[] { parser1, parser2 });

        JsonToken token1 = JsonToken.START_ARRAY;
        JsonToken token2 = JsonToken.VALUE_STRING;
        JsonToken tokenExpected = null;

        // Test parser1
        tokenExpected = token1;
        assertEquals(tokenExpected, seq.nextToken());

        // Test parser2
        tokenExpected = token2;
        assertEquals(tokenExpected, seq.nextToken());

        // Test parser1 again
        tokenExpected = JsonToken.END_ARRAY;
        assertEquals(tokenExpected, seq.nextToken());

        // Test end of sequence
        assertNull(seq.nextToken());
    }

    @Test
    public void testContainedParsersCount() {
        JsonParser parser1 = mock(JsonParser.class);
        JsonParser parser2 = mock(JsonParser.class);

        JsonParserSequence seq = new JsonParserSequence(new JsonParser[] { parser1, parser2 });

        int expected = 2;
        int actual = seq.containedParsersCount();

        assertEquals(expected, actual);
    }
}