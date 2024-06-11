package com.fasterxml.jackson.core.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class JsonParserSequenceTest {

    private JsonParser firstParser;
    private JsonParser secondParser;
    private JsonParser sequenceParser;

    @Before
    public void setup() throws IOException {
        firstParser = new JsonTestParser();
        secondParser = new JsonTestParser();
        sequenceParser = JsonParserSequence.createFlattened(firstParser, secondParser);
    }

    @Test
    public void testContainedParsersCount() {
        assertEquals(2, sequenceParser.containedParsersCount());
    }

    @Test
    public void testNextTokenWhenFirstParserHasToken() throws IOException {
        JsonToken token1 = sequenceParser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token1.getId());
        JsonToken token2 = sequenceParser.nextToken();
        assertEquals(JsonToken.END_ARRAY, token2.getId());
    }

    @Test
    public void testNextTokenWhenFirstParserDoesNotHaveToken() throws IOException {
        sequenceParser.nextToken(); // consume first token from first parser
        JsonToken token2 = sequenceParser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, token2.getId());
    }

    @Test
    public void testNextTokenWhenBothParsersHaveTokens() throws IOException {
        sequenceParser.nextToken(); // consume first token from first parser
        sequenceParser.nextToken(); // consume first token from second parser
        JsonToken token1 = sequenceParser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token1.getId());
        JsonToken token2 = sequenceParser.nextToken();
        assertEquals(JsonToken.END_ARRAY, token2.getId());
    }

    @Test
    public void testSwitchToNextParserWhenBothParsersHaveTokens() throws IOException {
        sequenceParser.nextToken(); // consume first token from first parser
        sequenceParser.nextToken(); // consume first token from second parser
        sequenceParser.nextToken(); // consume second token from second parser
        assertTrue(sequenceParser.switchToNext());
        JsonToken token1 = sequenceParser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, token1.getId());
    }

    @Test
    public void testSwitchToNextParserWhenNoTokensLeft() throws IOException {
        sequenceParser.nextToken(); // consume first token from first parser
        sequenceParser.nextToken(); // consume first token from second parser
        sequenceParser.nextToken(); // consume second token from second parser
        assertFalse(sequenceParser.switchToNext());
        assertNull(sequenceParser.nextToken());
    }

    @Test(expected = IllegalStateException.class)
    public void testSwitchToNextParserBeforeFirstToken() throws IOException {
        sequenceParser.nextToken();
        sequenceParser.switchToNext();
    }

    @Test
    public void testClose() throws IOException {
        sequenceParser.nextToken(); // consume first token from first parser
        sequenceParser.nextToken(); // consume first token from second parser
        sequenceParser.nextToken(); // consume second token from second parser
        sequenceParser.close();
        assertFalse(sequenceParser.isOpen());
    }
}

class JsonTestParser extends JsonParser {

    public JsonTestParser() throws IOException {
        this(new StandardParser());
    }

    public JsonTestParser(JsonFactory jsonFactory) throws IOException {
        super(jsonFactory);
    }

    @Override
    protected void _parseNextToken() throws IOException, JsonParseException {
        if (nextToken == null) {
            nextToken = new JsonToken();
        }
        nextToken.setRawValue("tokenValue");
    }
}