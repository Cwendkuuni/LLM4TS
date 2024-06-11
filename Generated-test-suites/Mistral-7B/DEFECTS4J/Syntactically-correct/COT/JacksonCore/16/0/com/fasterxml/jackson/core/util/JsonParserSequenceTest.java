package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.*;

/**
 * JsonParserSequenceTest
 */
public class JsonParserSequenceTest {
    private JsonParser[] _parsers;
    private JsonParserSequence _parserSequence;

    @Before
    public void setUp() throws IOException {
        JsonParser parser1 = new JsonFactory().createParser("{ \"key1\": \"value1\"}");
        JsonParser parser2 = new JsonFactory().createParser("{ \"key2\": \"value2\"}");
        _parsers = new JsonParser[] { parser1, parser2 };
        _parserSequence = new JsonParserSequence(_parsers);
    }

    

    @Test
    public void testConstructor() {
        assertNotNull(_parserSequence);
        assertEquals(2, _parserSequence.containedParsersCount());
    }

    @Test
    public void testNextToken_FirstParser() throws IOException {
        JsonToken token = _parserSequence.nextToken();
        assertNotNull(token);
        assertEquals(JsonToken.START_OBJECT, token.getId());
    }

    @Test
    public void testNextToken_SecondParser() throws IOException {
        JsonToken token = _parserSequence.nextToken();
        assertNotNull(token);
        assertEquals(JsonToken.START_OBJECT, token.getId());
    }

    @Test
    public void testNextToken_SwitchParsers() throws IOException {
        JsonToken token1 = _parserSequence.nextToken();
        JsonToken token2 = _parserSequence.nextToken();
        JsonToken token3 = _parserSequence.nextToken();

        assertNotNull(token1);
        assertNotNull(token2);
        assertNotNull(token3);

        assertEquals(JsonToken.START_OBJECT, token1.getId());
        assertEquals(JsonToken.START_OBJECT, token2.getId());
        assertEquals(JsonToken.END_OBJECT, token3.getId());
    }

    @Test(expected = IOException.class)
    public void testNextToken_NoMoreParsers() throws IOException {
        JsonToken token = _parserSequence.nextToken();
        assertNull(token);
    }

    @Test(expected = JsonParseException.class)
    public void testNextToken_JsonParseException() throws IOException, JsonParseException {
        JsonParser parserWithError = new JsonFactory().createParser("{ \"key\": null }"); // Intentionally creating a parser with an error
        _parserSequence = new JsonParserSequence(new JsonParser[] { _parserSequence.delegate, parserWithError });
        _parserSequence.nextToken(); // Consume the first token
        _parserSequence.nextToken(); // Consume the first errorless token
        _parserSequence.nextToken(); // This should throw JsonParseException
    }

    @Test
    public void testSwitchToNextParser() throws IOException {
        boolean switched = _parserSequence.switchToNext();
        assertTrue(switched);
        assertEquals(_parserSequence.delegate, _parsers[1]);
    }

    @Test
    public void testSwitchToNextParser_NoMoreParsers() {
        boolean switched = _parserSequence.switchToNext();
        assertFalse(switched);
    }

    @Test
    public void testCreateFlattened() throws IOException {
        JsonParser parser1 = new JsonFactory().createParser("{ \"key1\": \"value1\"}");
        JsonParser parser2 = new JsonFactory().createParser("{ \"key2\": \"value2\"}");
        JsonParser parser3 = new JsonFactory().createParser("{ \"key3\": \"value3\"}");
        JsonParser parser4 = new JsonFactory().createParser("{ \"key4\": \"value4\"}");

        JsonParserSequence seq1 = new JsonParserSequence(new JsonParser[] { parser1, parser2 });
        JsonParserSequence seq2 = new JsonParserSequence(new JsonParser[] { parser3, parser4 });

        JsonParserSequence flattenedParserSequence = JsonParserSequence.createFlattened(seq1, seq2);

        assertEquals(4, flattenedParserSequence.containedParsersCount());
        assertNotSame(parser1, flattenedParserSequence.delegate);
        assertSame(parser3, flattenedParserSequence.delegate);
    }

    
}