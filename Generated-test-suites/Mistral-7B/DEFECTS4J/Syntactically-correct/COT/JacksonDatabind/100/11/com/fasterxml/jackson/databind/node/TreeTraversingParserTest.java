package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TreeTraversingParser;

import org.junit.Test;

/**
 * Unit tests for {@link TreeTraversingParser} class
 */
public class TreeTraversingParserTest {

    @Test
    public void testTreeTraversingParser_EmptyJsonNode() throws Exception {
        JsonNode jsonNode = JsonFactory.instance.createObjectNode();
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        // Test basic functionality
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertTrue(parser.isClosed());

        // Test handling of empty nodes
        parser = new TreeTraversingParser(JsonFactory.instance.createNullNode());
        assertEquals(JsonToken.NULL, parser.nextToken());
        assertTrue(parser.isClosed());

        parser = new TreeTraversingParser(JsonFactory.instance.createValueNode(""));
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("", parser.getText());
        assertTrue(parser.isClosed());
    }

    @Test
    public void testTreeTraversingParser_SimpleJsonNode() throws Exception {
        String jsonString = "{\"name\":\"John\", \"age\":30}";
        JsonNode jsonNode = new ObjectMapper().readTree(jsonString);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        // Test basic functionality
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals("name", parser.getCurrentName());
        assertEquals("John", parser.getText());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("age", parser.getCurrentName());
        assertEquals("30", parser.getText());
        assertEquals(JsonToken.VALUE_INT, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertTrue(parser.isClosed());
    }

    @Test
    public void testTreeTraversingParser_EmptyArrayJsonNode() throws Exception {
        JsonNode jsonNode = JsonFactory.instance.createArrayNode();
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        // Test basic functionality
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertTrue(parser.isClosed());
    }

    @Test
    public void testTreeTraversingParser_ArrayJsonNode() throws Exception {
        String jsonString = "[1, 2, 3]";
        JsonNode jsonNode = new ObjectMapper().readTree(jsonString);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        // Test basic functionality
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals(JsonToken.VALUE_INT, parser.nextToken());
        assertEquals(1, parser.getIntValue());
        assertEquals(JsonToken.VALUE_INT, parser.nextToken());
        assertEquals(2, parser.getIntValue());
        assertEquals(JsonToken.VALUE_INT, parser.nextToken());
        assertEquals(3, parser.getIntValue());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        assertTrue(parser.isClosed());
    }

    @Test(expected = JsonParseException.class)
    public void testTreeTraversingParser_NullNode() throws Exception {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        parser.nextToken();
    }

    @Test(expected = JsonParseException.class)
    public void testTreeTraversingParser_InvalidJsonNode() throws Exception {
        String jsonString = "{'name':\"John\"}"; // missing colon in value
        JsonNode jsonNode = new ObjectMapper().readTree(jsonString);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken();
    }
}