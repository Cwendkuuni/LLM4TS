package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;

import org.junit.Test;

public class TreeTraversingParserTest {

    @Test
    public void testEmptyTree() {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        JsonToken token;

        token = parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, token);
        assertTrue(parser.isClosed());
    }

    @Test
    public void testSimpleValueNode() {
        TreeTraversingParser parser = new TreeTraversingParser(Json.parse("{\"value\": 42}"));
        JsonToken token;

        token = parser.nextToken();
        assertEquals(JsonToken.START_OBJECT, token);

        token = parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        assertEquals("value", parser.getCurrentName());

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, token);
        assertEquals(42, parser.getIntValue());

        token = parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, token);
        assertTrue(parser.isClosed());
    }

    @Test
    public void testSimpleArrayNode() {
        TreeTraversingParser parser = new TreeTraversingParser(Json.parse("[1, 2.5, \"three\"]"));
        JsonToken token;

        token = parser.nextToken();
        assertEquals(JsonToken.START_ARRAY, token);

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, token);
        assertEquals(1, parser.getIntValue());

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_FLOAT, token);
        assertEquals(2.5, parser.getDoubleValue(), 0.001);

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token);
        assertEquals("three", parser.getText());

        token = parser.nextToken();
        assertEquals(JsonToken.END_ARRAY, token);
        assertTrue(parser.isClosed());
    }

    @Test
    public void testComplexNode() {
        TreeTraversingParser parser = new TreeTraversingParser(Json.parse("{\"key\": [{\"nestedKey\": 1}, {\"nestedKey\": 2}]}"));
        JsonToken token;

        token = parser.nextToken();
        assertEquals(JsonToken.START_OBJECT, token);

        token = parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        assertEquals("key", parser.getCurrentName());

        token = parser.nextToken();
        assertEquals(JsonToken.START_ARRAY, token);

        token = parser.nextToken();
        assertEquals(JsonToken.START_OBJECT, token);

        token = parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        assertEquals("nestedKey", parser.getCurrentName());

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, token);
        assertEquals(1, parser.getIntValue());

        token = parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, token);

        token = parser.nextToken();
        assertEquals(JsonToken.START_OBJECT, token);

        token = parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        assertEquals("nestedKey", parser.getCurrentName());

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, token);
        assertEquals(2, parser.getIntValue());

        token = parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, token);

        token = parser.nextToken();
        assertEquals(JsonToken.END_ARRAY, token);
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertTrue(parser.isClosed());
    }

    @Test
    public void testSkipChildren() {
        TreeTraversingParser parser = new TreeTraversingParser(Json.parse("{\"key\": [{\"nestedKey\": 1}, {\"nestedKey\": 2}]}"));
        parser.skipChildren();

        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.START_OBJECT, token);

        token = parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        assertEquals("key", parser.getCurrentName());

        token = parser.nextToken();
        assertEquals(JsonToken.END_ARRAY, token);
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertTrue(parser.isClosed());
    }
}