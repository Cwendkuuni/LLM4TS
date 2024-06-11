package com.fasterxml.jackson.core.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.Base64Variants;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class JsonParserSequenceTest {

    private JsonParser[] _parsers;
    private JsonParserSequence _parserSequence;

    @Before
    public void setup() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        _parsers = new JsonParser[3];
        _parsers[0] = jsonFactory.createParser("{}");
        _parsers[1] = jsonFactory.createParser("[]");
        _parsers[2] = jsonFactory.createParser("false");
        _parserSequence = JsonParserSequence.createFlattened(_parsers[0], _parsers[1]);
    }

    @Test
    public void testCreateFlattened() {
        JsonParser parserSequence = JsonParserSequence.createFlattened(_parsers[0], _parsers[1]);
        assertEquals(_parsers.length, parserSequence.containedParsersCount());
        assertArrayEquals(Arrays.copyOfRange(_parsers, 1, _parsers.length), parserSequence._parsers);
    }

    @Test
    public void testNextToken() throws IOException {
        JsonToken token = _parserSequence.nextToken();
        assertEquals(JsonToken.START_OBJECT, token);

        token = _parserSequence.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);

        token = _parserSequence.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token);
        assertEquals("", token.getValueAsString());

        token = _parserSequence.nextToken();
        assertEquals(JsonToken.END_OBJECT, token);

        token = _parserSequence.nextToken();
        assertEquals(JsonToken.START_ARRAY, token);

        token = _parserSequence.nextToken();
        assertEquals(JsonToken.END_ARRAY, token);

        token = _parserSequence.nextToken();
        assertEquals(JsonToken.VALUE_FALSE, token);

        token = _parserSequence.nextToken();
        assertNull(token);
    }

    @Test
    public void testClose() throws IOException {
        _parserSequence.close();
        assertEquals(0, _parsers[0].getCurrentPosition());
        assertEquals(0, _parsers[1].getCurrentPosition());
        assertEquals(0, _parsers[2].getCurrentPosition());
    }

    @Test
    public void testSwitchToNext() {
        JsonParser parser = _parserSequence.delegate;
        assertSame(_parsers[0], parser);
        assertTrue(_parserSequence.switchToNext());
        parser = _parserSequence.delegate;
        assertSame(_parsers[1], parser);
        assertTrue(_parserSequence.switchToNext());
        parser = _parserSequence.delegate;
        assertSame(_parsers[2], parser);
        assertFalse(_parserSequence.switchToNext());
    }

    @Test
    public void testContainedParsersCount() {
        assertEquals(_parsers.length, _parserSequence.containedParsersCount());
    }

    @Test
    public void testCreateFlattenedWithJsonParserSequence() throws IOException {
        JsonParser parser1 = new JsonFactory().createParser("{\"foo\":1}");
        JsonParser parser2 = JsonParserSequence.createFlattened(parser1, new JsonParserSequence(new JsonParser[0]));
        assertEquals(1, parser2.containedParsersCount());
        assertSame(parser1, parser2.delegate);
    }

    @Test
    public void testCreateFlattenedWithEmptySequence() {
        JsonParser parser1 = new JsonFactory().createParser("{}");
        JsonParser parser2 = JsonParserSequence.createFlattened(parser1, new JsonParserSequence(new JsonParser[0]));
        assertEquals(1, parser2.containedParsersCount());
        assertSame(parser1, parser2.delegate);
    }

    @Test
    public void testCreateFlattenedWithNull() {
        JsonParser parser1 = new JsonFactory().createParser("{}");
        JsonParser parser2 = JsonParserSequence.createFlattened(parser1, null);
        assertEquals(1, parser2.containedParsersCount());
        assertSame(parser1, parser2.delegate);
    }

    @Test
    public void testCreateFlattenedWithJsonParserSequenceWithParsers() {
        JsonParser parser1 = new JsonFactory().createParser("{\"foo\":1}");
        JsonParser parser2 = new JsonFactory().createParser("{\"bar\":2}");
        JsonParser parser3 = new JsonFactory().createParser("{\"baz\":3}");
        JsonParserSequence parserSequence = new JsonParserSequence(new JsonParser[] { new JsonParserSequence(parser1, parser2), parser3 });
        JsonParser flattenedParser = JsonParserSequence.createFlattened(parser1, parserSequence);
        assertEquals(3, flattenedParser.containedParsersCount());
        assertTrue(Arrays.asList(_parsers[0], _parsers[1], _parsers[2]).containsAll(Arrays.asList(parser1, parser2, parser3)));
    }
}