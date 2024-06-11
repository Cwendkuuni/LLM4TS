package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;

import org.junit.Test;

public class TreeTraversingParserTest {

    @Test
    public void testEmptyParser() {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertEquals(JsonToken.END_DOCUMENT, parser.nextToken());
        assertTrue(parser.isClosed());
    }

    @Test
    public void testArrayParser() throws Exception {
        TreeTraversingParser parser = new TreeTraversingParser(Json.parse("[]"));
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        assertTrue(parser.isClosed());
    }

    @Test
    public void testObjectParser() throws Exception {
        TreeTraversingParser parser = new TreeTraversingParser(Json.parse("{}"));
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertNull(parser.nextValue());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertTrue(parser.isClosed());
    }

    @Test
    public void testValueParser() throws Exception {
        TreeTraversingParser parser = new TreeTraversingParser(Json.parse("123"));
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(123, parser.getIntValue());
        assertTrue(parser.isClosed());
    }

    @Test
    public void testStringParser() throws Exception {
        TreeTraversingParser parser = new TreeTraversingParser(Json.parse("\"Hello World\""));
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("Hello World", parser.getText());
        assertTrue(parser.isClosed());
    }

    @Test
    public void testComplexParser() throws Exception {
        TreeTraversingParser parser = new TreeTraversingParser(Json.parse("{\"foo\": 1, \"bar\": [1, \"apples\"]}"));
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals("foo", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(1, parser.getIntValue());
        assertEquals("bar", parser.getCurrentName());
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(1, parser.getIntValue());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("apples", parser.getText());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertTrue(parser.isClosed());
    }

    @Test(expected = JsonParseException.class)
    public void testInvalidParser() throws Exception {
        TreeTraversingParser parser = new TreeTraversingParser(Json.parse("{\"foo\":"));
        parser.nextToken();
        parser.nextToken(); // should fail here
    }
}