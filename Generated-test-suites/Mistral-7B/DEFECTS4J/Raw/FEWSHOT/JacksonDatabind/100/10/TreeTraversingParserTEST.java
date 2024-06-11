package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;

public class TreeTraversingParserTest {

    @Test
    public void testEmptyParser() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertEquals(JsonToken.END_OF_STREAM, parser.nextToken());
    }

    @Test
    public void testParserWithNullNode() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        parser.setCodec(null);
        assertEquals(JsonToken.END_OF_STREAM, parser.nextToken());
    }

    @Test
    public void testParserWithRootNode() throws IOException {
        String json = "{}";
        TreeTraversingParser parser = new TreeTraversingParser(Json.parse(json));
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertEquals(JsonToken.END_OF_STREAM, parser.nextToken());
    }

    @Test
    public void testParserWithArrayNode() throws IOException {
        String json = "[1,2,3]";
        TreeTraversingParser parser = new TreeTraversingParser(Json.parse(json));
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(1, parser.getIntValue());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(2, parser.getIntValue());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(3, parser.getIntValue());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        assertEquals(JsonToken.END_OF_STREAM, parser.nextToken());
    }

    @Test
    public void testParserWithObjectNode() throws IOException {
        String json = "{\"name\":\"John\",\"age\":30}";
        TreeTraversingParser parser = new TreeTraversingParser(Json.parse(json));
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("name", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("John", parser.getText());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("age", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(30, parser.getIntValue());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertEquals(JsonToken.END_OF_STREAM, parser.nextToken());
    }

    @Test
    public void testParserWithNestedNodes() throws IOException {
        String json = "{\"name\":\"John\",\"age\":30,\"children\":[{\"name\":\"Alice\",\"age\":10},{\"name\":\"Bob\",\"age\":20}]}";
        TreeTraversingParser parser = new TreeTraversingParser(Json.parse(json));
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("name", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("John", parser.getText());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("age", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(30, parser.getIntValue());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("children", parser.getCurrentName());
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        // first child
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("name", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("Alice", parser.getText());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("age", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(10, parser.getIntValue());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        // second child
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("name", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("Bob", parser.getText());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("age", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(20, parser.getIntValue());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        assertEquals(JsonToken.END_OF_STREAM, parser.nextToken());
    }

    @Test
    public void testParserWithClosedNode() throws IOException {
        JsonNode node = Json.parse("{}");
        TreeTraversingParser parser = new TreeTraversingParser(node);
        parser.close();
        assertEquals(JsonToken.END_OF_STREAM, parser.nextToken());
        assertEquals(JsonToken.END_OF_STREAM, parser.nextToken());
    }
}