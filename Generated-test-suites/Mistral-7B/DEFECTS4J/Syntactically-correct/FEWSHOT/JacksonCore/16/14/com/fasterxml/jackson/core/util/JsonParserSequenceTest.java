package com.fasterxml.jackson.core.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class JsonParserSequenceTest {

    private JsonParser parser1;
    private JsonParser parser2;
    private JsonParser parser3;
    private JsonParserSequence seq;

    @Before
    public void setUp() throws IOException {
        parser1 = mockJsonParser("{ \"name\": \"John\" }");
        parser2 = mockJsonParser("{ \"age\": 30 }");
        parser3 = mockJsonParser("{ \"city\": \"New York\" }");

        seq = JsonParserSequence.createFlattened(parser1, parser2, parser3);
    }

    @Test
    public void testCreateFlattened() throws IOException {
        JsonParser expectedSeq = new JsonParserSequence(new JsonParser[] { parser1, parser2, parser3 });
        assertEquals(expectedSeq.getClass(), seq.getClass());
        assertEquals(parser1, seq.getCurrentParser());
        assertSame(parser1, seq.getDelegate());
    }

    @Test
    public void testNextToken() throws IOException {
        JsonToken token = seq.nextToken();
        assertEquals(JsonToken.START_OBJECT, token.id());
        assertEquals("{\"name\":\"John\"}", seq.getText());
        assertSame(parser1, seq.getCurrentParser());

        token = seq.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token.id());
        assertEquals("name", token.getText().toString());
        assertSame(parser1, seq.getCurrentParser());

        token = seq.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token.id());
        assertEquals("John", token.getText().toString());
        assertSame(parser1, seq.getCurrentParser());

        token = seq.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token.id());
        assertEquals("age", token.getText().toString());
        assertSame(parser2, seq.getCurrentParser());

        token = seq.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, token.id());
        assertEquals(30, token.getIntValue());
        assertSame(parser2, seq.getCurrentParser());

        token = seq.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token.id());
        assertEquals("city", token.getText().toString());
        assertSame(parser3, seq.getCurrentParser());

        token = seq.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token.id());
        assertEquals("New York", token.getText().toString());
        assertSame(parser3, seq.getCurrentParser());

        token = seq.nextToken();
        assertEquals(JsonToken.END_OBJECT, token.id());
        assertNull(seq.getText());
        assertSame(parser3, seq.getCurrentParser());
    }

    @Test
    public void testSwitchToNextParser() throws IOException {
        JsonToken token = seq.nextToken();
        assertEquals(JsonToken.START_OBJECT, token.id());
        assertSame(parser1, seq.getCurrentParser());

        seq.switchToNext();
        token = seq.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token.id());
        assertEquals("age", token.getText().toString());
        assertSame(parser2, seq.getCurrentParser());

        seq.switchToNext();
        token = seq.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, token.id());
        assertEquals(30, token.getIntValue());
        assertSame(parser2, seq.getCurrentParser());

        seq.switchToNext();
        token = seq.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token.id());
        assertEquals("city", token.getText().toString());
        assertSame(parser3, seq.getCurrentParser());

        seq.switchToNext();
        token = seq.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token.id());
        assertEquals("New York", token.getText().toString());
        assertSame(parser3, seq.getCurrentParser());

        seq.switchToNext();
        token = seq.nextToken();
        assertEquals(JsonToken.END_OBJECT, token.id());
        assertNull(seq.getText());
        assertSame(parser1, seq.getCurrentParser());
    }

    @Test
    public void testContainedParsersCount() {
        assertEquals(3, seq.containedParsersCount());
    }

    private JsonParser mockJsonParser(String json) throws IOException {
        return new JsonParser() {
            @Override
            public JsonToken nextToken() throws IOException, JsonParseException {
                JsonReader reader = new JsonReader(new StringReader(json));
                JsonToken token = reader.nextToken();
                reader.close();
                return token;
            }
        };
    }
}