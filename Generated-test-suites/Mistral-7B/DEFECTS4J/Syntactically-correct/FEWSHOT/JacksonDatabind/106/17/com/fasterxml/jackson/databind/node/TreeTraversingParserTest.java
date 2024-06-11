package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.ParserMinimalBase;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonStreamContext;
import com.fasterxml.jackson.databind.node.BaseTest;

/**
 * Unit tests for {@link TreeTraversingParser} class.
 */
public class TreeTraversingParserTest extends BaseTest {

    /**
     * Test constructor with root JsonNode.
     */
    @Test
    public void testTreeTraversingParserRoot() throws Exception {
        JsonNode jsonNode = createObjectNode();
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        parser.skipChildren();
        String name = parser.getCurrentName();
        assertEquals("root", name);
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("name", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("John Doe", parser.getText());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("age", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(25, parser.getIntValue());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        parser.close();
    }

    /**
     * Test constructor with array JsonNode.
     */
    @Test
    public void testTreeTraversingParserArray() throws Exception {
        JsonNode jsonNode = createArrayNode(new TextNode("Hello"));
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        parser.skipChildren();
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("Hello", parser.getText());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        parser.close();
    }

    /**
     * Test constructor with nested JsonNodes.
     */
    @Test
    public void testTreeTraversingParserNested() throws Exception {
        JsonNode jsonNode = createObjectNode();
        JsonNode objNode = createObjectNode();
        JsonNode arrayNode = createArrayNode(new TextNode("Hello"), new NumberNode(10));
        JsonNode textNode = createTextNode("World");

        jsonNode.set("obj", objNode);
        objNode.set("name", createTextNode("John"));
        objNode.set("numbers", arrayNode);

        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        parser.skipChildren();
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("obj", parser.getCurrentName());
        parser.skipChildren();
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        parser.skipChildren();
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("name", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("John", parser.getText());
        parser.skipChildren();
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("numbers", parser.getCurrentName());
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        parser.skipChildren();
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("Hello", parser.getText());
        parser.skipChildren();
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(10, parser.getIntValue());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        parser.close();
    }

    /**
     * Test closing parser.
     */
    @Test
    public void testTreeTraversingParserClose() throws Exception {
        JsonNode jsonNode = createObjectNode();
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        parser.close();
        assertEquals(JsonToken.NULL, parser.nextToken());
    }

    /**
     * Test handling empty JsonNode.
     */
    @Test
    public void testTreeTraversingParserEmpty() throws Exception {
        JsonNode jsonNode = createObjectNode();
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        parser.skipChildren();
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        parser.close();
    }

    /**
     * Test handling null JsonNode.
     */
    @Test(expected = NullPointerException.class)
    public void testTreeTraversingParserNull() throws Exception {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        parser.nextToken();
    }

    /**
     * Test handling recursion into children.
     */
    @Test
    public void testTreeTraversingParserRecursion() throws Exception {
        JsonNode jsonNode = createObjectNode();
        JsonNode arrayNode = createArrayNode();
        JsonNode objNode = createObjectNode();
        JsonNode textNode = createTextNode("Recursion");

        jsonNode.set("array", arrayNode);
        arrayNode.set(0, objNode);
        objNode.set("name", textNode);

        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        parser.skipChildren();
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("array", parser.getCurrentName());
        parser.skipChildren();
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        parser.skipChildren();
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        parser.skipChildren();
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("name", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("Recursion", parser.getText());
        parser.skipChildren();
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        parser.skipChildren();
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        parser.skipChildren();
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        parser.close();
    }
}