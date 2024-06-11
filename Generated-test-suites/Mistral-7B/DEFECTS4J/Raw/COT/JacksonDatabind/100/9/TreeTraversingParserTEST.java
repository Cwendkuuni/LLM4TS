package com.example.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectCodec;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

import org.junit.Test;

public class TreeTraversingParserTest {

    @Test
    public void testEmptyParser() throws Exception {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertNull(parser.nextToken());
        parser.close();
    }

    @Test
    public void testParserWithNullNode() throws Exception {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertNull(parser.nextToken());
        parser.close();
    }

    @Test
    public void testParserWithArrayNode() throws Exception {
        ObjectCodec objectCodec = null;
        JsonNode jsonNode = new ArrayNode(objectCodec);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode, objectCodec);
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.START_ARRAY, token);
        parser.skipChildren();
        token = parser.nextToken();
        assertEquals(JsonToken.END_ARRAY, token);
        parser.close();
    }

    @Test
    public void testParserWithObjectNode() throws Exception {
        ObjectCodec objectCodec = null;
        JsonNode jsonNode = new ObjectNode(objectCodec);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode, objectCodec);
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.START_OBJECT, token);
        parser.skipChildren();
        token = parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, token);
        parser.close();
    }

    @Test
    public void testParserWithTextNode() throws Exception {
        ObjectCodec objectCodec = null;
        JsonNode jsonNode = new TextNode("Test Text");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode, objectCodec);
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token);
        assertEquals("Test Text", parser.getText());
        parser.close();
    }

    @Test(expected = JsonParseException.class)
    public void testParserWithInvalidToken() throws Exception {
        ObjectCodec objectCodec = null;
        JsonNode jsonNode = new ObjectNode(objectCodec);
        JsonNode invalidNode = new ObjectNode(objectCodec).set("invalid", 123);
        TreeTraversingParser parser = new TreeTraversingParser(invalidNode, objectCodec);
        parser.nextToken();
        parser.nextToken(); // should be throwing an exception
    }

    @Test
    public void testParserWithClosedState() throws Exception {
        ObjectCodec objectCodec = null;
        JsonNode jsonNode = new ObjectNode(objectCodec);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode, objectCodec);
        parser.close();
        assertNull(parser.nextToken());
    }
}