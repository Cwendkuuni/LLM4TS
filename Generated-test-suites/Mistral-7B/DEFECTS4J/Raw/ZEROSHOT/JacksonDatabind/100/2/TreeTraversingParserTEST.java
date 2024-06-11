package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for {@link TreeTraversingParser}.
 */
public class TreeTraversingParserTest {

    private ObjectMapper mapper;
    private JsonNode rootNode;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        String json = "{\"name\": \"John\", \"age\": 30, \"hobbies\": [\"reading\", \"swimming\"]}";
        rootNode = mapper.readTree(json);
    }

    @Test
    public void testParsing() throws Exception {
        TreeTraversingParser parser = new TreeTraversingParser(rootNode);

        // Test nextToken
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.START_OBJECT, token);

        token = parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        assertEquals("name", parser.getCurrentName());

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token);
        assertEquals("John", parser.getText());

        token = parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        assertEquals("age", parser.getCurrentName());

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, token);
        assertEquals(30, parser.getIntValue());

        token = parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        assertEquals("hobbies", parser.getCurrentName());

        token = parser.nextToken();
        assertEquals(JsonToken.START_ARRAY, token);

        // Test skipping children
        parser.skipChildren();

        token = parser.nextToken();
        assertEquals(JsonToken.END_ARRAY, token);

        // Test isClosed
        parser.close();
        assertTrue(parser.isClosed());

        // Test nextToken after closing
        parser.nextToken();
        assertNull(parser.getCurrentNode());
    }

    @Test(expected = IOException.class)
    public void testEmptyNode() throws Exception {
        JsonNode emptyNode = mapper.valueFromObject(new Object());
        TreeTraversingParser parser = new TreeTraversingParser(emptyNode);
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
    }
}