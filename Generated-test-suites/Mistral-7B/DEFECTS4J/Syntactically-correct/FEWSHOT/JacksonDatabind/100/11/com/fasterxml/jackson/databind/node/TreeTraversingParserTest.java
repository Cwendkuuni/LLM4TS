package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import org.junit.Test;

public class TreeTraversingParserTest {

    @Test
    public void testEmptyParser() {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertEquals(JsonToken.END_DOCUMENT, parser.nextToken());
        assertNull(parser.nextToken());
        parser.close();
    }

    @Test
    public void testSingleValueParser() {
        JsonNode testNode = new ObjectNode(JSONFactory.getInstance());
        TreeTraversingParser parser = new TreeTraversingParser(testNode);
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        String value = parser.getText();
        assertEquals("", value);
        parser.close();
    }

    @Test
    public void testArrayParser() {
        JsonNode testNode = new ArrayNode(JSONFactory.getInstance());
        TreeTraversingParser parser = new TreeTraversingParser(testNode);
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        parser.skipChildren(); // skip empty array
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        parser.close();
    }

    @Test
    public void testObjectParser() {
        JsonNode testNode = new ObjectNode(JSONFactory.getInstance());
        testNode.put("field1", new IntNode(1));
        testNode.put("field2", new TextNode("test"));
        TreeTraversingParser parser = new TreeTraversingParser(testNode);

        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals("field1", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(1, parser.getIntValue());
        parser.skipChildren(); // skip empty object

        assertEquals("field2", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        String value = parser.getText();
        assertEquals("test", value);
        parser.skipChildren(); // skip empty object

        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        parser.close();
    }

    @Test(expected = JsonParseException.class)
    public void testMalformedJsonParser() {
        JsonNode testNode = new ObjectNode(JSONFactory.getInstance());
        testNode.put("field1", new TextNode("{"));
        TreeTraversingParser parser = new TreeTraversingParser(testNode);
        parser.nextToken();
        parser.nextToken(); // skip "{"
        parser.nextToken(); // expect START_OBJECT but gets START_ARRAY instead
    }
}