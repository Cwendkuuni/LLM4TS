
package com.fasterxml.jackson.core.util;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonParserSequenceTest {

    private JsonParserSequence parserSequence;

    @Before
    public void setup() {
        JsonParser[] parsers = new JsonParser[2];
        parsers[0] = new FakeJsonParser();
        parsers[1] = new FakeJsonParser();
        parserSequence = new JsonParserSequence(parsers);
    }

    @Test
    public void containedParsersCount_ReturnsCorrectCount() throws Exception {
        assertEquals(2, parserSequence.containedParsersCount());
    }

    @Test
    public void nextToken_ReturnsNextTokenFromEachParserInSequence() throws Exception {
        assertEquals(JsonToken.START_OBJECT, parserSequence.nextToken());
        assertEquals(JsonToken.FIELD_NAME, parserSequence.nextToken());
        assertEquals(JsonToken.VALUE_STRING, parserSequence.nextToken());
        assertEquals(JsonToken.END_OBJECT, parserSequence.nextToken());
    }

    @Test
    public void nextToken_ReturnsNullWhenNoMoreTokensAvailable() throws Exception {
        // Move to the end of the first parser
        parserSequence.nextToken();
        parserSequence.nextToken();
        parserSequence.nextToken();
        parserSequence.nextToken();
        // Move to the end of the second parser
        parserSequence.nextToken();
        parserSequence.nextToken();
        parserSequence.nextToken();
        parserSequence.nextToken();
        // All tokens consumed, expect null
        assertNull(parserSequence.nextToken());
    }

    @Test
    public void close_ClosesAllParsersInSequence() throws Exception {
        parserSequence.close();

        FakeJsonParser firstParser = (FakeJsonParser) parserSequence._parsers[0];
        assertTrue(firstParser.isClosed());

        FakeJsonParser secondParser = (FakeJsonParser) parserSequence._parsers[1];
        assertTrue(secondParser.isClosed());
    }
  
    // Additional test cases for createFlattened method

    @Test
    public void createFlattened_ReturnsSingleParser_WhenBothParsersAreNotSequences() throws Exception {
        JsonParser first = new FakeJsonParser();
        JsonParser second = new FakeJsonParser();

        JsonParserSequence flattenedParser = JsonParserSequence.createFlattened(first, second);

        assertEquals(2, flattenedParser.containedParsersCount());
    }

    @Test
    public void createFlattened_ReturnsFlatSequence_WhenFirstParserIsASequence() throws Exception {
        JsonParser first = new FakeJsonParser();
        JsonParser second = new FakeJsonParser();
        JsonParserSequence innerSequence = new JsonParserSequence(new JsonParser[] { first, second });

        JsonParserSequence flattenedParser = JsonParserSequence.createFlattened(innerSequence, second);

        assertEquals(3, flattenedParser.containedParsersCount());
    }

    @Test
    public void createFlattened_ReturnsFlatSequence_WhenSecondParserIsASequence() throws Exception {
        JsonParser first = new FakeJsonParser();
        JsonParser second = new FakeJsonParser();
        JsonParserSequence innerSequence = new JsonParserSequence(new JsonParser[] { first, second });

        JsonParserSequence flattenedParser = JsonParserSequence.createFlattened(first, innerSequence);

        assertEquals(3, flattenedParser.containedParsersCount());
    }

    @Test
    public void createFlattened_ReturnsFlatSequence_WhenBothParsersAreSequences() throws Exception {
        JsonParser first = new FakeJsonParser();
        JsonParser second = new FakeJsonParser();
        JsonParserSequence innerSequence1 = new JsonParserSequence(new JsonParser[] { first, second });

        JsonParser third = new FakeJsonParser();
        JsonParser fourth = new FakeJsonParser();
        JsonParserSequence innerSequence2 = new JsonParserSequence(new JsonParser[] { third, fourth });

        JsonParserSequence flattenedParser = JsonParserSequence.createFlattened(innerSequence1, innerSequence2);

        assertEquals(4, flattenedParser.containedParsersCount());
    }
  
}

class FakeJsonParser extends JsonParser {

    private boolean closed = false;

    @Override
    public void close() {
        closed = true;
    }

    @Override
    public JsonToken nextToken() {
        // Return fake tokens for testing purposes
        return null;
    }

    public boolean isClosed() {
        return closed;
    }
}
