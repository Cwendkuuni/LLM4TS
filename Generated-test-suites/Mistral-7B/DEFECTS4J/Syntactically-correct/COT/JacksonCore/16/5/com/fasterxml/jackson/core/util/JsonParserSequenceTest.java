package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.base.ParserMinimalBase;

/**
 * Test class for JsonParserSequence.
 */
public class JsonParserSequenceTest {

    private JsonParser parser1;
    private JsonParser parser2;
    private JsonParser parser3;
    private JsonParser sequence;

    @Before
    public void setUp() throws IOException {
        parser1 = new ParserMinimalBase() {};
        parser2 = new ParserMinimalBase() {};
        parser3 = new ParserMinimalBase() {};
        sequence = JsonParserSequence.createFlattened(parser1, parser2, parser3);
    }

    @Test
    public void testConstructor() {
        assertEquals(3, sequence.containedParsersCount());
    }

    @Test
    public void testNextToken_FirstParser() throws IOException {
        JsonToken token = sequence.nextToken();
        assertEquals(JsonToken.START_ARRAY, token.getId());
        sequence.close();
    }

    @Test
    public void testNextToken_SecondParser() throws IOException {
        sequence.nextToken();
        JsonToken token = sequence.nextToken();
        assertEquals(JsonToken.START_OBJECT, token.getId());
        sequence.close();
    }

    @Test
    public void testNextToken_ThirdParser() throws IOException {
        sequence.nextToken();
        sequence.nextToken();
        JsonToken token = sequence.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token.getId());
        sequence.close();
    }

    @Test(expected = IOException.class)
    public void testNextToken_EndOfStream() throws IOException {
        sequence.nextToken();
        sequence.nextToken();
        sequence.nextToken();
        sequence.nextToken();
        sequence.nextToken();
    }

    @Test(expected = JsonParseException.class)
    public void testNextToken_ParsingException() throws IOException {
        JsonParserExceptionParser parserException = new JsonParserExceptionParser();
        JsonParser sequenceWithException = JsonParserSequence.createFlattened(parser1, parserException);
        sequenceWithException.nextToken();
    }

    @Test
    public void testClose() throws IOException {
        sequence.nextToken();
        sequence.nextToken();
        sequence.nextToken();
        sequence.nextToken();
        sequence.close();
        assertFalse(sequence.isOpen());
    }

    @Test
    public void testSwitchToNextParser() throws IOException {
        sequence.nextToken();
        sequence.nextToken();
        assertTrue(sequence.switchToNext());
        JsonToken token = sequence.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token.getId());
        assertFalse(sequence.switchToNext());
        sequence.close();
    }

    @Test
    public void testAddFlattenedActiveParsers() throws IOException {
        JsonParser parserA = new ParserMinimalBase() {};
        JsonParser parserB = new ParserMinimalBase() {};
        JsonParser parserC = new ParserMinimalBase() {};
        JsonParser parserD = new ParserMinimalBase() {};

        JsonParser sequenceWithParsers = new JsonParserSequence(new JsonParser[] { parserA, parserB, parserC, parserD });
        List<JsonParser> activeParsers = Arrays.asList(new JsonParser[0]);
        sequenceWithParsers.addFlattenedActiveParsers(activeParsers);

        assertEquals(4, activeParsers.size());
        assertEquals(parserB, activeParsers.get(1));
        assertEquals(parserC, activeParsers.get(2));
        assertEquals(parserD, activeParsers.get(3));
    }
}