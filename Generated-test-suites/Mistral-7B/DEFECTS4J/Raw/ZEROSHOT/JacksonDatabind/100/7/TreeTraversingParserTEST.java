package com.example.junit;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

import org.junit.Test;

public class TreeTraversingParserTest {

    @Test
    public void testSimpleJsonObject() throws IOException {
        String json = "{\"name\": \"John\", \"age\": 30, \"city\": \"New York\"}";
        JsonNode jsonNode = new ObjectMapper().readTree(json);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        assertEquals("John", parser.nextToken().getName());
        assertEquals("name", parser.getCurrentName());
        assertEquals("name", parser.getText());
        parser.skipChildren();
        assertEquals("New York", parser.nextToken().getName());
        assertEquals("city", parser.getCurrentName());
        assertEquals("New York", parser.getText());
        parser.skipChildren();
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertNull(parser.nextToken());
    }

    @Test
    public void testSimpleJsonArray() throws IOException {
        String json = "[1, 2.5, \"three\", 4, true]";
        JsonNode jsonNode = new ObjectMapper().readTree(json);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        parser.skipChildren();
        assertEquals(1, parser.nextIntValue());
        parser.skipChildren();
        assertEquals(2.5, parser.nextDoubleValue(), 0.0);
        parser.skipChildren();
        assertEquals("three", parser.nextTextValue());
        parser.skipChildren();
        assertEquals(4, parser.nextIntValue());
        parser.skipChildren();
        assertEquals(true, parser.nextBooleanValue());
        parser.skipChildren();
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        assertNull(parser.nextToken());
    }

    @Test(expected = JsonParseException.class)
    public void testMissingName() throws IOException {
        String json = "[1, 2.5, 3, 4, true]"; // missing "name" field in object
        JsonNode jsonNode = new ObjectMapper().readTree(json);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        parser.nextToken(); // skip [
        parser.nextToken(); // skip START_ARRAY
        parser.nextIntValue(); // 1
        parser.nextToken(); // skip COMMA
        parser.nextDoubleValue(); // 2.5
        parser.nextToken(); // skip COMMA
        parser.nextTextValue(); // "three"
        parser.nextToken(); // skip COMMA
        parser.nextIntValue(); // 4
        parser.nextToken(); // skip COMMA
        parser.nextBooleanValue(); // true
        parser.nextToken(); // skip END_ARRAY
        parser.nextToken(); // should throw exception
    }

    @Test
    public void testCurrentLocation() throws IOException {
        String json = "{\"name\": \"John\", \"age\": 30, \"city\": \"New York\", \"email\": \"john@example.com\"}";
        JsonNode jsonNode = new ObjectMapper().readTree(json);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        assertEquals(JsonLocation.NA, parser.getTokenLocation());
        assertEquals(JsonLocation.NA, parser.getCurrentLocation());

        parser.nextToken();
        assertEquals("name", parser.getCurrentName());
        assertEquals("name", parser.getText());
        assertEquals("name", parser.getCurrentLocation().getPath());

        parser.skipChildren();
        assertEquals("city", parser.getCurrentName());
        assertEquals("city", parser.getText());
        assertEquals("city", parser.getCurrentLocation().getPath());

        parser.skipChildren();
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertEquals(JsonLocation.NA, parser.getCurrentLocation());
    }

    @Test
    public void testNullJson() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertNull(parser.nextToken());
        assertNull(parser.getCurrentName());
        assertNull(parser.getText());
    }
}