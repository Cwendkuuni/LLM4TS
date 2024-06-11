package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.ParserMinimalBase;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;

/**
 * Test class for TreeTraversingParser
 */
public class TreeTraversingParserTest {

    @Test
    public void testTreeTraversingParser() throws Exception {
        String json = "{\"name\":\"John\",\"age\":30,\"cities\":[\"New York\",\"Los Angeles\",\"Chicago\"]}";
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(json);

        TreeTraversingParser parser = new TreeTraversingParser(rootNode);

        // Test basic functionality
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals("name", parser.getCurrentName());
        assertEquals("John", parser.getText());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("age", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(30, parser.getIntValue());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("cities", parser.getCurrentName());
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());

        // Test traversing array elements
        assertEquals("New York", parser.nextToken().getValueAsString());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken().getType());
        assertEquals("Los Angeles", parser.nextToken().getValueAsString());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken().getType());
        assertEquals("Chicago", parser.nextToken().getValueAsString());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken().getType());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());

        // Test closing parser
        parser.close();
        assertEquals(JsonToken.NULL, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertNull(parser.nextToken());
    }

    @Test(expected = IllegalStateException.class)
    public void testClosedParser() throws Exception {
        String json = "{\"name\":\"John\",\"age\":30,\"cities\":[\"New York\",\"Los Angeles\",\"Chicago\"]}";
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(json);

        TreeTraversingParser parser = new TreeTraversingParser(rootNode);
        parser.close();
        parser.nextToken();
    }
}