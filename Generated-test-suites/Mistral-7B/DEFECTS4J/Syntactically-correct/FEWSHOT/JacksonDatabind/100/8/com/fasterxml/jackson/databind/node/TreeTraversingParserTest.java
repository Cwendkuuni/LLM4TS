package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonToken;
import org.junit.Test;

public class TreeTraversingParserTest {

    @Test
    public void testEmptyParser() {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        assertTrue(parser.isClosed());
    }

    @Test
    public void testArrayParser() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(new ArrayNode(new ObjectNode(true)));
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        assertTrue(parser.isClosed());
    }

    @Test
    public void testObjectParser() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(new ObjectNode(new ObjectNode(true)));
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        parser.nextFieldName(); // skip "fieldName"
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(1, parser.getIntValue());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("fieldName", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("value", parser.getText());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertTrue(parser.isClosed());
    }

    @Test
    public void testValueParser() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(new TextNode("value"));
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("value", parser.getText());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        assertTrue(parser.isClosed());
    }

    @Test
    public void testSkipChildrenParser() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(new ObjectNode(new ObjectNode(true)));
        parser.nextToken(); // skip START_OBJECT
        parser.nextFieldName(); // skip "fieldName"
        parser.nextToken(); // skip VALUE_NUMBER_INT
        parser.skipChildren();
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("fieldName", parser.getCurrentName());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertTrue(parser.isClosed());
    }
}