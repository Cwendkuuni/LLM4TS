package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;

import org.junit.Test;

public class TreeTraversingParserTest {

    @Test
    public void testEmptyParser() throws Exception {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        assertTrue(parser.isClosed());
    }

    @Test
    public void testParserForObject() throws Exception {
        String json = "{\"name\": \"John\", \"age\": 30}";
        TreeTraversingParser parser = new TreeTraversingParser(JsonParser.parseString(json));
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals("name", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("John", parser.getText());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("age", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(30, parser.getIntValue());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertTrue(parser.isClosed());
    }

    @Test
    public void testParserForArray() throws Exception {
        String json = "[1, 2.5, \"three\"]";
        TreeTraversingParser parser = new TreeTraversingParser(JsonParser.parseString(json));
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals(1, parser.getIntValue());
        assertEquals(JsonToken.VALUE_NUMBER_FLOAT, parser.nextToken());
        assertEquals(2.5, parser.getDoubleValue(), 0.01);
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("three", parser.getText());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        assertTrue(parser.isClosed());
    }

    @Test
    public void testParserForBinary() throws Exception {
        String json = "{\"name\": \"John\", \"data\": [{\"id\": 1, \"data\": \"QWw==\"}]}";
        TreeTraversingParser parser = new TreeTraversingParser(JsonParser.parseString(json));
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals("name", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("John", parser.getText());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("data", parser.getCurrentName());
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals(1, parser.getIntValue());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("data", parser.getCurrentName());
        byte[] binaryData = parser.getBinaryValue(Base64Variants.STANDARD);
        assertArrayEquals(new byte[]{"Q", "W", "w"}, binaryData);
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertTrue(parser.isClosed());
    }

    @Test(expected = JsonParseException.class)
    public void testParserForInvalidJson() throws Exception {
        String json = "{name: 1}"; // missing quotes in name
        TreeTraversingParser parser = new TreeTraversingParser(JsonParser.parseString(json));
        parser.nextToken(); // name
        parser.nextToken(); // missing colon
        parser.nextToken(); // number 1
        parser.nextToken(); // missing comma or end of object
    }
}