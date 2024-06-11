package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

public class TreeTraversingParserTest {

    @Test
    public void testTreeTraversingParser_Array() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[1,2,3,4]";
        JsonNode rootNode = mapper.readTree(json);
        TreeTraversingParser parser = new TreeTraversingParser(rootNode);

        // Test initial state
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertFalse(parser.isClosed());
        assertNull(parser.getCurrentName());

        // Test traversing array elements
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(1, parser.getIntValue());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(2, parser.getIntValue());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(3, parser.getIntValue());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(4, parser.getIntValue());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        assertTrue(parser.isClosed());

        // Test skipping children
        parser.reset();
        parser.nextToken();
        parser.skipChildren();
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        assertTrue(parser.isClosed());

        // Test accessing textual content
        parser.reset();
        parser.nextToken();
        assertEquals("1", parser.getText());
        parser.nextToken();
        assertEquals("2", parser.getText());
        parser.nextToken();
        assertEquals("3", parser.getText());
        parser.nextToken();
        assertEquals("4", parser.getText());
        parser.nextToken();
        assertNull(parser.getText());

        // Test typed non-text access
        parser.reset();
        parser.nextToken();
        assertEquals(1, parser.getIntValue());
        parser.nextToken();
        assertEquals(2, parser.getIntValue());
        parser.nextToken();
        assertEquals(3, parser.getIntValue());
        parser.nextToken();
        assertEquals(4, parser.getIntValue());
        parser.nextToken();
        assertNull(parser.getIntValue());

        // Test binary access (not supported in this test)
        parser.reset();
        parser.nextToken();
        assertNull(parser.getBinaryValue(null));

        // Test traversing and accessing nested array
        JsonNode nestedArrayNode = rootNode.get(0);
        TreeTraversingParser nestedParser = new TreeTraversingParser(nestedArrayNode);
        assertEquals(JsonToken.START_ARRAY, nestedParser.nextToken());
        assertEquals(JsonToken.VALUE_NUMBER_INT, nestedParser.nextToken());
        assertEquals(1, nestedParser.getIntValue());
        assertEquals(JsonToken.VALUE_NUMBER_INT, nestedParser.nextToken());
        assertEquals(2, nestedParser.getIntValue());
        assertEquals(JsonToken.END_ARRAY, nestedParser.nextToken());
        assertTrue(nestedParser.isClosed());
    }

    @Test
    public void testTreeTraversingParser_Object() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"name\":\"John\",\"age\":30,\"city\":\"New York\"}";
        JsonNode rootNode = mapper.readTree(json);
        TreeTraversingParser parser = new TreeTraversingParser(rootNode);

        // Test initial state
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertFalse(parser.isClosed());
        assertNull(parser.getCurrentName());

        // Test traversing object fields
        assertEquals("name", parser.getCurrentName());
        assertEquals("John", parser.getText());
        assertEquals("name", parser.getCurrentName());
        assertFalse(parser.isClosed());

        assertEquals("age", parser.nextToken().getCurrentName());
        assertEquals(30, parser.getIntValue());
        assertEquals("age", parser.getCurrentName());
        assertFalse(parser.isClosed());

        assertEquals("city", parser.nextToken().getCurrentName());
        assertEquals("New York", parser.getText());
        assertEquals("city", parser.getCurrentName());
        assertFalse(parser.isClosed());

        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertTrue(parser.isClosed());

        // Test skipping children
        parser.reset();
        parser.nextToken();
        parser.nextToken();
        parser.skipChildren();
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertTrue(parser.isClosed());

        // Test accessing textual content
        parser.reset();
        parser.nextToken();
        parser.nextToken();
        assertEquals("New York", parser.getText());
        parser.nextToken();
        assertNull(parser.getText());

        // Test typed non-text access
        parser.reset();
        parser.nextToken();
        parser.nextToken();
        assertEquals("New York", parser.getText());
        parser.nextToken();
        assertEquals("city", parser.getCurrentName());
        assertEquals(30, parser.getIntValue());
        parser.nextToken();
        assertEquals("age", parser.getCurrentName());
        assertEquals(30, parser.getIntValue());
        parser.nextToken();
        assertNull(parser.getIntValue());

        // Test binary access (not supported in this test)
        parser.reset();
        parser.nextToken();
        parser.nextToken();
        assertNull(parser.getBinaryValue(null));

        // Test traversing and accessing nested object
        JsonNode nestedObjectNode = rootNode.get("city");
        TreeTraversingParser nestedParser = new TreeTraversingParser(nestedObjectNode);
        assertEquals(JsonToken.VALUE_STRING, nestedParser.nextToken());
        assertEquals("New York", nestedParser.getText());
        assertEquals(JsonToken.END_OBJECT, nestedParser.nextToken());
        assertTrue(nestedParser.isClosed());
    }
}