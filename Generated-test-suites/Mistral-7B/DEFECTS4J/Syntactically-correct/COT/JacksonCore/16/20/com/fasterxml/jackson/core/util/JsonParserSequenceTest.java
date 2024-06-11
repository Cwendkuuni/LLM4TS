package com.fasterxml.jackson.core.util; // make sure the test class is in the same package

import org.junit.Test;
import org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class JsonParserSequenceTest {

    @Test
    public void testJsonParserSequenceConstruction() throws IOException {
        JsonParser parser1 = createMockParser();
        JsonParser parser2 = createMockParser();

        JsonParserSequence parserSequence = new JsonParserSequence(new JsonParser[] { parser1, parser2 });

        assertEquals(2, parserSequence.containedParsersCount());
    }

    @Test
    public void testNextToken_firstParser() throws IOException {
        JsonParser parser1 = createMockParser();
        JsonParser parser2 = createMockParser();

        JsonParserSequence parserSequence = new JsonParserSequence(new JsonParser[] { parser1, parser2 });

        JsonToken token = parserSequence.nextToken();
        assertEquals(JsonToken.START_ARRAY, token);
        parser1.nextToken(); // consume first token of parser1

        token = parserSequence.nextToken();
        assertEquals(JsonToken.START_OBJECT, token); // now nextToken() returns token from parser2
    }

    @Test
    public void testNextToken_switchParsers() throws IOException {
        JsonParser parser1 = createMockParser();
        JsonParser parser2 = createMockParser();

        JsonParserSequence parserSequence = new JsonParserSequence(new JsonParser[] { parser1, parser2 });

        parserSequence.nextToken(); // consume START_ARRAY from parser1
        parserSequence.nextToken(); // consume STRING from parser1

        // switch to parser2 and get next token
        parserSequence.nextToken();
        assertEquals(JsonToken.START_OBJECT, parserSequence.nextToken());
    }

    @Test(expected = JsonParseException.class)
    public void testNextToken_allParsersExhausted() throws IOException {
        JsonParser parser1 = createMockParser();
        JsonParser parser2 = createMockParser();

        JsonParserSequence parserSequence = new JsonParserSequence(new JsonParser[] { parser1, parser2 });

        parserSequence.nextToken(); // consume START_ARRAY from parser1
        parserSequence.nextToken(); // consume STRING from parser1
        parserSequence.nextToken(); // consume null from parser1

        parserSequence.nextToken(); // should throw JsonParseException
    }

    @Test
    public void testCreateFlattened() throws IOException {
        JsonParser parser1 = createMockParser();
        JsonParser parser2 = createMockParser();
        JsonParser parser3 = createMockParser();

        JsonParserSequence seq1 = new JsonParserSequence(new JsonParser[] { parser1, parser2 });
        JsonParserSequence seq2 = new JsonParserSequence(new JsonParser[] { parser2, parser3 });

        JsonParserSequence flattenedSeq = JsonParserSequence.createFlattened(seq1, seq2);

        assertEquals(3, flattenedSeq.containedParsersCount());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateFlattened_invalidParser() throws IOException {
        JsonParser parser1 = createMockParser();
        Object invalidParser = new Object();

        JsonParserSequence seq1 = new JsonParserSequence(new JsonParser[] { parser1 });

        JsonParserSequence.createFlattened(seq1, invalidParser);
    }

    @Test
    public void testSwitchToNext_exhaustedParsers() throws IOException {
        JsonParser parser1 = createMockParser();
        JsonParser parser2 = createMockParser();

        JsonParserSequence parserSequence = new JsonParserSequence(new JsonParser[] { parser1, parser2 });

        parserSequence.nextToken();
        parserSequence.nextToken();

        // switch to parser2 but it's already exhausted
        parserSequence.nextToken();
        parserSequence.nextToken();

        // switch back to parser1, it should still have tokens
        parserSequence.nextToken();
        parserSequence.nextToken();
    }

    private JsonParser createMockParser() {
        return mock(JsonParser.class);
    }

    
    // Add more test cases here as needed
    
}