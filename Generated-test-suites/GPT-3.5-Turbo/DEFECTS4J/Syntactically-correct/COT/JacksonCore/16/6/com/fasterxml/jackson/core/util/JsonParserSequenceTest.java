
package com.fasterxml.jackson.core.util;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.util.JsonParserSequence;
import com.fasterxml.jackson.core.util.JsonParserDelegate;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class JsonParserSequenceTest {

    private JsonParserSequence sequence;

    @Before
    public void setUp() {
        JsonParserDelegate delegate1 = new JsonParserDelegate() {
            @Override
            public JsonToken nextToken() throws IOException, JsonParseException {
                return null;
            }

            @Override
            public void close() throws IOException {
            }
        };

        JsonParserDelegate delegate2 = new JsonParserDelegate() {
            @Override
            public JsonToken nextToken() throws IOException, JsonParseException {
                return JsonToken.START_OBJECT;
            }

            @Override
            public void close() throws IOException {
            }
        };

        JsonParserDelegate delegate3 = new JsonParserDelegate() {
            @Override
            public JsonToken nextToken() throws IOException, JsonParseException {
                return JsonToken.END_OBJECT;
            }

            @Override
            public void close() throws IOException {
            }
        };

        JsonParserDelegate delegate4 = new JsonParserDelegate() {
            @Override
            public JsonToken nextToken() throws IOException, JsonParseException {
                return JsonToken.START_ARRAY;
            }

            @Override
            public void close() throws IOException {
            }
        };

        JsonParserDelegate delegate5 = new JsonParserDelegate() {
            @Override
            public JsonToken nextToken() throws IOException, JsonParseException {
                return JsonToken.END_ARRAY;
            }

            @Override
            public void close() throws IOException {
            }
        };

        JsonParserDelegate delegate6 = new JsonParserDelegate() {
            @Override
            public JsonToken nextToken() throws IOException, JsonParseException {
                throw new JsonParseException(null, "");
            }

            @Override
            public void close() throws IOException {
            }
        };

        sequence = new JsonParserSequence(new JsonParserDelegate[]{delegate1, delegate2, delegate3, delegate4, delegate5, delegate6});
    }

    @Test
    public void testNextToken() throws IOException, JsonParseException {
        assertNull(sequence.nextToken());
        assertEquals(JsonToken.START_OBJECT, sequence.nextToken());
        assertEquals(JsonToken.END_OBJECT, sequence.nextToken());
        assertEquals(JsonToken.START_ARRAY, sequence.nextToken());
        assertEquals(JsonToken.END_ARRAY, sequence.nextToken());

        try {
            sequence.nextToken();
            fail("Expected JsonParseException");
        } catch (JsonParseException e) {
            // expected
        }
    }

    @Test
    public void testClose() throws IOException {
        sequence.close();
        // test that close is called on all underlying parsers
        assertTrue(sequence.delegateClosed);
        assertTrue(((JsonParserDelegate) sequence.delegate).delegateClosed);
    }

    @Test
    public void testContainedParsersCount() {
        assertEquals(6, sequence.containedParsersCount());
    }
}
