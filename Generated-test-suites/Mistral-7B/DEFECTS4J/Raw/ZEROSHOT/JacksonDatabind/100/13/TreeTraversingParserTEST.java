package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.ParserMinimalBase;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for {@link TreeTraversingParser}.
 */
public class TreeTraversingParserTest {

    private ObjectMapper objectMapper;

    @Before
    public void setUp() throws JsonProcessingException {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testParseArray() throws JsonProcessingException {
        String json = "[1, 2, 3, 4]";
        JsonNode rootNode = objectMapper.readTree(json);
        TreeTraversingParser parser = new TreeTraversingParser(rootNode);

        // Test nextTokens
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.START_ARRAY, token);
        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_INT, token);
        assertEquals(1, parser.getIntValue());
        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_INT, token);
        assertEquals(2, parser.getIntValue());
        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_INT, token);
        assertEquals(3, parser.getIntValue());
        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_INT, token);
        assertEquals(4, parser.getIntValue());
        token = parser.nextToken();
        assertEquals(JsonToken.END_ARRAY, token);

        // Test skipChildren
        parser.skipChildren();
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
    }

    @Test
    public void testParseObject() throws JsonProcessingException {
        String json = "{\"name\":\"John\", \"age\":30, \"city\":\"New York\"}";
        JsonNode rootNode = objectMapper.readTree(json);
        TreeTraversingParser parser = new TreeTraversingParser(rootNode);

        // Test nextTokens
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
        assertEquals(JsonToken.VALUE_INT, token);
        assertEquals(30, parser.getIntValue());
        token = parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        assertEquals("city", parser.getCurrentName());
        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token);
        assertEquals("New York", parser.getText());
        token = parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, token);

        // Test skipChildren
        parser.skipChildren();
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
    }

    @Test
    public void testParseSingleValue() throws JsonProcessingException {
        String json = "42";
        JsonNode rootNode = objectMapper.readTree(json);
        TreeTraversingParser parser = new TreeTraversingParser(rootNode);

        // Test nextTokens
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.VALUE_INT, token);
        assertEquals(42, parser.getIntValue());
        token = parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, token);
        // Test skipChildren
        parser.skipChildren();
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
    }

    @Test
    public void testParseEmpty() throws JsonProcessingException {
        String json = "{}";
        JsonNode rootNode = objectMapper.readTree(json);
        TreeTraversingParser parser = new TreeTraversingParser(rootNode);

        // Test nextTokens
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.START_OBJECT, token);
        token = parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, token);
        // Test skipChildren
        parser.skipChildren();
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
    }

    @Test(expected = JsonParseException.class)
    public void testParseInvalidJson() throws JsonProcessingException {
        String json = "[{";
        JsonNode rootNode = objectMapper.readTree(json);
        TreeTraversingParser parser = new TreeTraversingParser(rootNode);
        parser.nextToken();
    }
}