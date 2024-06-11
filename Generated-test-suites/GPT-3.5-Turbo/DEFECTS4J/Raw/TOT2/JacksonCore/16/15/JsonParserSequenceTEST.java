package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class JsonParserSequenceTest {

    @Test
    public void testJsonParserSequenceCreation() {
        // Test creating a JsonParserSequence with two parsers
        JsonParser parser1 = new JsonFactory().createParser("[1, 2, 3]");
        JsonParser parser2 = new JsonFactory().createParser("{ \"key\": \"value\" }");
        JsonParserSequence sequence = JsonParserSequence.createFlattened(parser1, parser2);

        assertNotNull(sequence);
        assertEquals(2, sequence.containedParsersCount());
    }

    @Test
    public void testNextToken() throws Exception {
        // Test nextToken method of JsonParserSequence
        JsonParser parser1 = new JsonFactory().createParser("[1, 2, 3]");
        JsonParser parser2 = new JsonFactory().createParser("{ \"key\": \"value\" }");
        JsonParserSequence sequence = JsonParserSequence.createFlattened(parser1, parser2);

        assertEquals(JsonToken.START_ARRAY, sequence.nextToken());
        assertEquals(JsonToken.VALUE_NUMBER_INT, sequence.nextToken());
        assertEquals(JsonToken.END_ARRAY, sequence.nextToken());
        assertEquals(JsonToken.START_OBJECT, sequence.nextToken());
        assertEquals(JsonToken.FIELD_NAME, sequence.nextToken());
        assertEquals(JsonToken.VALUE_STRING, sequence.nextToken());
        assertEquals(JsonToken.END_OBJECT, sequence.nextToken());
        assertNull(sequence.nextToken());
    }

    @Test
    public void testClose() throws Exception {
        // Test close method of JsonParserSequence
        JsonParser parser1 = new JsonFactory().createParser("[1, 2, 3]");
        JsonParser parser2 = new JsonFactory().createParser("{ \"key\": \"value\" }");
        JsonParserSequence sequence = JsonParserSequence.createFlattened(parser1, parser2);

        sequence.close();

        assertTrue(parser1.isClosed());
        assertTrue(parser2.isClosed());
    }

    @Test
    public void testSwitchToNext() throws Exception {
        // Test switchToNext method of JsonParserSequence
        JsonParser parser1 = new JsonFactory().createParser("[1, 2, 3]");
        JsonParser parser2 = new JsonFactory().createParser("{ \"key\": \"value\" }");
        JsonParserSequence sequence = JsonParserSequence.createFlattened(parser1, parser2);

        assertTrue(sequence.switchToNext());
        assertEquals(parser2, sequence.delegate);
        assertFalse(sequence.switchToNext());
    }
}