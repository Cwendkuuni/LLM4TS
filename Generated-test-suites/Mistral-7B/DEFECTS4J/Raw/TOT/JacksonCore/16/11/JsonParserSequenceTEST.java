package com.fasterxml.jackson.core.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.core.BaseTest;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.util.JsonParserSequence;

public class JsonParserSequenceTest extends BaseTest {

    @Test
    public void testNextTokenWithSingleParser() throws IOException {
        JsonParser parser = new JsonFactory().createParser(getClass().getResourceAsStream("/test.json"));
        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[] { parser });
        JsonToken token = sequence.nextToken();
        assertEquals(JsonToken.START_OBJECT, token);

        token = sequence.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        assertEquals("name", token.asString());

        token = sequence.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token);
        assertEquals("John Doe", token.asText());

        token = sequence.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        assertEquals("age", token.asString());

        token = sequence.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, token);
        assertEquals(42, token.asInt());

        token = sequence.nextToken();
        assertEquals(JsonToken.END_OBJECT, token);
        assertFalse(sequence.hasNext());
    }

    @Test
    public void testNextTokenWithTwoParsers() throws IOException {
        JsonParser parser1 = new JsonFactory().createParser(getClass().getResourceAsStream("/test1.json"));
        JsonParser parser2 = new JsonFactory().createParser(getClass().getResourceAsStream("/test2.json"));
        JsonParserSequence sequence = JsonParserSequence.createFlattened(parser1, parser2);

        JsonToken token = sequence.nextToken();
        assertEquals(JsonToken.START_ARRAY, token);

        token = sequence.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, token);
        assertEquals(1, token.asInt());

        token = sequence.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token);
        assertEquals("apple", token.asText());

        token = sequence.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token);
        assertEquals("banana", token.asText());

        token = sequence.nextToken();
        assertEquals(JsonToken.END_ARRAY, token);
        assertFalse(sequence.hasNext());
    }

    @Test
    public void testNextTokenWithEmptyParser() throws IOException {
        JsonParser emptyParser = new JsonFactory().createParser("");
        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[] { emptyParser });
        JsonToken token = sequence.nextToken();
        assertNull(token);
        assertFalse(sequence.hasNext());
    }

    @Test(expected = IOException.class)
    public void testNextTokenWithNullParser() throws IOException {
        JsonParser nullParser = null;
        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[] { nullParser });
        sequence.nextToken();
    }

    @Test
    public void testContainedParsersCount() throws IOException {
        JsonParser parser1 = new JsonFactory().createParser(getClass().getResourceAsStream("/test1.json"));
        JsonParser parser2 = new JsonFactory().createParser(getClass().getResourceAsStream("/test2.json"));
        JsonParserSequence sequence = JsonParserSequence.createFlattened(parser1, parser2);
        assertEquals(2, sequence.containedParsersCount());
    }
}