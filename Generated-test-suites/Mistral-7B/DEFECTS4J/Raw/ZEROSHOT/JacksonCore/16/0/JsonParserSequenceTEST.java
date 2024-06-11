package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonParserSequenceTest {

    @Test
    public void testJsonParserSequence() throws IOException {
        // Test creation of JsonParserSequence with single parser
        JsonParser parser1 = new JsonFactory().createParser("[]");
        JsonParserSequence sequence1 = new JsonParserSequence(new JsonParser[] { parser1 });
        assertEquals(JsonToken.START_ARRAY, sequence1.nextToken());
        assertNull(sequence1.nextToken());
        sequence1.close();

        // Test creation of JsonParserSequence with two parsers
        JsonParser parser2 = new JsonFactory().createParser("{}");
        JsonParserSequence sequence2 = new JsonParserSequence(new JsonParser[] { parser1, parser2 });
        assertEquals(JsonToken.START_ARRAY, sequence1.nextToken());
        assertEquals(JsonToken.START_OBJECT, sequence2.nextToken());
        assertNull(sequence2.nextToken());
        sequence2.close();
        assertEquals(JsonToken.END_OBJECT, sequence1.nextToken());
        assertNull(sequence1.nextToken());
        sequence1.close();

        // Test creation of JsonParserSequence with two parsers flattened
        JsonParserSequence sequence3 = JsonParserSequence.createFlattened(parser1, parser2);
        assertEquals(JsonToken.START_ARRAY, sequence3.nextToken());
        assertEquals(JsonToken.START_OBJECT, sequence3.nextToken());
        assertNull(sequence3.nextToken());
        sequence3.close();

        // Test switching between parsers in JsonParserSequence
        JsonParser parser3 = new JsonFactory().createParser("\"hello\"");
        JsonParser parser4 = new JsonFactory().createParser("42");
        JsonParserSequence sequence4 = new JsonParserSequence(new JsonParser[] { parser1, parser2, parser3, parser4 });
        assertEquals(JsonToken.START_ARRAY, sequence4.nextToken());
        assertEquals(JsonToken.START_OBJECT, sequence4.nextToken());
        assertEquals(JsonToken.FIELD_NAME, sequence4.nextToken());
        assertEquals("hello", sequence4.getText());
        assertEquals(JsonToken.VALUE_STRING, sequence4.nextToken());
        assertEquals(JsonToken.START_ARRAY, sequence4.nextToken());
        assertEquals(JsonToken.VALUE_NUMBER_INT, sequence4.nextToken());
        assertEquals(42, sequence4.getIntValue());
        assertNull(sequence4.nextToken());
        sequence4.close();

        // Test containedParsersCount() method
        JsonParserSequence sequence5 = new JsonParserSequence(new JsonParser[] { parser1, parser2, parser3, parser4 });
        assertEquals(4, sequence5.containedParsersCount());
    }
}