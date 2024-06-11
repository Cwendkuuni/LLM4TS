package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;

import org.junit.Test;

public class TreeTraversingParserTest {

    @Test
    public void testEmptyParser() {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertTrue(parser.isClosed());
    }

    @Test
    public void testArrayParser() throws IOException {
        JsonNode jsonNode = new ArrayNode();
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertNull(parser.nextValue());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        assertTrue(parser.isClosed());
    }

    @Test
    public void testObjectParser() throws IOException {
        JsonNode jsonNode = new ObjectNode(JsonFactory.getInstance());
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertNull(parser.nextValue());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("test", parser.getCurrentName());
        assertNull(parser.nextValue());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertTrue(parser.isClosed());
    }

    @Test
    public void testValueParser() throws IOException {
        JsonNode jsonNode = new TextNode("test");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("test", parser.getText());
        assertNull(parser.nextValue());
        assertTrue(parser.isClosed());
    }

    @Test
    public void testNumericParser() throws IOException {
        JsonNode jsonNode = new NumberNode(123);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(123, parser.getIntValue());
        assertNull(parser.nextValue());
        assertTrue(parser.isClosed());
    }

    @Test
    public void testSkipChildrenParser() throws IOException {
        JsonNode jsonNode = new ObjectNode(JsonFactory.getInstance());
        JsonNode childNode = new TextNode("test");
        JsonNode grandChildNode = new NumberNode(456);
        jsonNode.set("test", childNode);
        childNode.set("test2", grandChildNode);

        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals("test", parser.getCurrentName());
        parser.nextValue();

        parser.skipChildren();

        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("test2", parser.getCurrentName());
        assertEquals(456, parser.getIntValue());
        assertNull(parser.nextValue());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertTrue(parser.isClosed());
    }
}