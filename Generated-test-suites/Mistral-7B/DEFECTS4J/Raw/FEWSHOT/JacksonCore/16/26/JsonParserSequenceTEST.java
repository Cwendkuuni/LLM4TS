package com.fasterxml.jackson.core.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.IOException;
import java.util.Arrays;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.ParserMinimalBase;
import com.fasterxml.jackson.databind.JsonMappingException;

public class JsonParserSequenceTest {

    @Test
    public void testCreateFlattened() throws IOException {
        JsonParser parser1 = new ParserMinimalBase() {
            @Override
            public JsonToken nextToken() throws IOException, JsonParseException {
                return JsonToken.START_OBJECT;
            }
        };
        JsonParser parser2 = new ParserMinimalBase() {
            @Override
            public JsonToken nextToken() throws IOException, JsonParseException {
                return JsonToken.FIELD_NAME;
            }
        };
        JsonParser parser3 = new ParserMinimalBase() {
            @Override
            public JsonToken nextToken() throws IOException, JsonParseException {
                return JsonToken.VALUE_STRING;
            }
        };

        JsonParser sequence = JsonParserSequence.createFlattened(parser1, parser2, parser3);
        assertNotNull(sequence);
        assertEquals(3, sequence.containedParsersCount());

        JsonToken token = sequence.nextToken();
        assertSame(JsonToken.START_OBJECT, token);

        token = sequence.nextToken();
        assertSame(JsonToken.FIELD_NAME, token);

        token = sequence.nextToken();
        assertSame(JsonToken.VALUE_STRING, token);

        assertNull(sequence.nextToken());
    }

    @Test
    public void testSwitchToNext() throws IOException {
        JsonParser parser1 = new ParserMinimalBase() {
            @Override
            public JsonToken nextToken() throws IOException, JsonParseException {
                return JsonToken.START_ARRAY;
            }
        };
        JsonParser parser2 = new ParserMinimalBase() {
            @Override
            public JsonToken nextToken() throws IOException, JsonParseException {
                return JsonToken.VALUE_NUMBER_INT;
            }
        };

        JsonParser sequence = new JsonParserSequence(new JsonParser[] { parser1, parser2 });
        assertEquals(JsonToken.START_ARRAY, sequence.nextToken());
        assertNull(sequence.nextToken());

        assertTrue(sequence.switchToNext());
        assertEquals(JsonToken.VALUE_NUMBER_INT, sequence.nextToken());
        assertNull(sequence.nextToken());

        assertTrue(sequence.switchToNext());
        assertEquals(JsonToken.END_ARRAY, sequence.nextToken());
        assertNull(sequence.nextToken());

        assertFalse(sequence.switchToNext());
    }

    @Test
    public void testClose() throws IOException {
        JsonParser parser1 = new ParserMinimalBase() {
            @Override
            public JsonToken nextToken() throws IOException, JsonParseException {
                throw new IOException("Test error");
            }
        };
        JsonParser parser2 = new ParserMinimalBase() {
            @Override
            public JsonToken nextToken() throws IOException, JsonParseException {
                return JsonToken.VALUE_STRING;
            }
        };

        JsonParser sequence = new JsonParserSequence(new JsonParser[] { parser1, parser2 });

        sequence.close();

        try {
            sequence.nextToken();
            fail("Expected IOException");
        } catch (IOException e) {}

        assertSame(parser2, sequence.delegate);
    }
}