package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.JsonToken;

import org.junit.Test;

public class TreeTraversingParserTest {

    @Test
    public void testEmptyParser() throws Exception {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertNull(parser.nextToken());
        parser.close();
    }

    @Test
    public void testValueParser() throws Exception {
        JsonNode jsonNode = new ObjectNode(JSON.parse("{\"value\": 42}"));
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        assertTrue(parser.nextToken().isNumber());
        assertEquals(42.0, parser.nextDoubleValue(), 0.0);
        parser.close();
    }

    @Test
    public void testArrayParser() throws Exception {
        JsonNode jsonNode = new ArrayNode(JSON.parse("[]"));
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        assertTrue(parser.nextToken().isStartArray());
        parser.skipChildren();
        assertNull(parser.nextToken());
        parser.close();
    }

    @Test
    public void testObjectParser() throws Exception {
        JsonNode jsonNode = new ObjectNode(JSON.parse("{\"key1\": 1, \"key2\": \"value2\"}"));
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        assertEquals("key1", parser.nextFieldName());
        assertEquals(1, parser.nextIntValue());

        assertEquals("key2", parser.nextFieldName());
        assertEquals("value2", parser.nextText());

        parser.close();
    }

    @Test
    public void testNestedObjectParser() throws Exception {
        JsonNode jsonNode = new ObjectNode(JSON.parse("{\"key1\": { \"key2\": 1, \"key3\": \"value3\" }, \"key4\": [4, 5]}"));
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        assertEquals("key1", parser.nextFieldName());
        assertNotNull(parser.nextToken());
        assertEquals("key2", parser.nextFieldName());
        assertEquals(1, parser.nextIntValue());

        parser.skipChildren();

        assertEquals("key3", parser.nextFieldName());
        assertEquals("value3", parser.nextText());

        parser.skipChildren();

        assertEquals("key4", parser.nextFieldName());
        assertNotNull(parser.nextToken());
        assertEquals(4, parser.nextIntValue());

        parser.skipChildren();

        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        parser.close();
    }

    @Test
    public void testNestedArrayParser() throws Exception {
        JsonNode jsonNode = new ObjectNode(JSON.parse("{\"key1\": [1, [2, 3]], \"key2\": 4}"));
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        assertEquals("key1", parser.nextFieldName());
        assertNotNull(parser.nextToken());
        assertEquals(1, parser.nextIntValue());

        parser.skipChildren();

        assertTrue(parser.nextToken().isStartArray());
        assertNotNull(parser.nextToken());
        assertEquals(2, parser.nextIntValue());

        parser.skipChildren();

        assertEquals(3, parser.nextIntValue());

        parser.skipChildren();

        parser.skipChildren(); // empty array

        assertEquals(JsonToken.END_ARRAY, parser.nextToken());

        parser.skipChildren();

        assertEquals(4, parser.nextIntValue());

        parser.close();
    }
}