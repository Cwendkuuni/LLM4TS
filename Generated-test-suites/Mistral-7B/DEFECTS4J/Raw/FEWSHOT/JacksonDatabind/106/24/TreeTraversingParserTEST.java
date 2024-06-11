package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;

import org.junit.Test;

public class TreeTraversingParserTest {

    @Test
    public void testEmptyTree() throws Exception {
        JsonParser parser = new TreeTraversingParser(JsonNode.NULL);
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        parser.close();
    }

    @Test
    public void testStringValue() throws Exception {
        String json = "\"foo\"";
        JsonParser parser = new TreeTraversingParser(JsonNode.parse(json));
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("foo", parser.getText());
        parser.close();
    }

    @Test
    public void testIntValue() throws Exception {
        String json = "42";
        JsonParser parser = new TreeTraversingParser(JsonNode.parse(json));
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(42, parser.getIntValue());
        parser.close();
    }

    @Test
    public void testFloatValue() throws Exception {
        String json = "3.14159";
        JsonParser parser = new TreeTraversingParser(JsonNode.parse(json));
        assertEquals(JsonToken.VALUE_NUMBER_FLOAT, parser.nextToken());
        assertEquals(3.14159f, parser.getFloatValue(), 0.0f);
        parser.close();
    }

    @Test
    public void testArray() throws Exception {
        String json = "[1, 2, 3]";
        JsonParser parser = new TreeTraversingParser(JsonNode.parse(json));
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(1, parser.getIntValue());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(2, parser.getIntValue());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(3, parser.getIntValue());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        parser.close();
    }

    @Test
    public void testObject() throws Exception {
        String json = "{\"name\":\"Alice\", \"age\":30}";
        JsonParser parser = new TreeTraversingParser(JsonNode.parse(json));
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals("name", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("Alice", parser.getText());
        assertEquals("age", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(30, parser.getIntValue());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        parser.close();
    }

    @Test
    public void testNestedObject() throws Exception {
        String json = "{\"name\":{\"first\":\"Alice\", \"last\":\"Smith\"}}";
        JsonParser parser = new TreeTraversingParser(JsonNode.parse(json));
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals("name", parser.getCurrentName());
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals("first", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("Alice", parser.getText());
        assertEquals("last", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("Smith", parser.getText());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        parser.close();
    }

    @Test
    public void testNullValue() throws Exception {
        String json = "null";
        JsonParser parser = new TreeTraversingParser(JsonNode.parse(json));
        assertEquals(JsonToken.VALUE_NULL, parser.nextToken());
        parser.close();
    }

    @Test(expected = JsonParseException.class)
    public void testInvalidJson() throws Exception {
        String json = "42\"foo";
        JsonParser parser = new TreeTraversingParser(JsonNode.parse(json));
        parser.nextToken();
        fail("Expected JsonParseException");
    }

    @Test
    public void testSkipChildren() throws Exception {
        String json = "[1, 2, 3]";
        JsonParser parser = new TreeTraversingParser(JsonNode.parse(json));
        parser.nextToken(); // START_ARRAY
        parser.nextToken(); // VALUE_NUMBER_INT (1)
        parser.skipChildren();
        parser.nextToken(); // VALUE_NUMBER_INT (2)
        parser.skipChildren();
        parser.nextToken(); // VALUE_NUMBER_INT (3)
        parser.skipChildren();
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        parser.close();
    }

    @Test(expected = JsonParseException.class)
    public void testNonNumberToken() throws Exception {
        String json = "\"foo\"";
        JsonParser parser = new TreeTraversingParser(JsonNode.parse(json));
        parser.nextIntValue(); // throws JsonParseException
        parser.close();
    }

    @Test
    public void testCurrentLocation() throws Exception {
        String json = "{\"location\":{\"lat\":45.23, \"lon\":-75.32}}";
        JsonParser parser = new TreeTraversingParser(JsonNode.parse(json));
        assertEquals(JsonLocation.NA, parser.getCurrentLocation());
        parser.nextToken(); // START_OBJECT
        parser.nextToken(); // "location"
        parser.nextToken(); // START_OBJECT
        parser.nextToken(); // "lat"
        parser.nextToken(); // VALUE_NUMBER_FLOAT
        assertEquals(JsonLocation.createLocation(45.23, -75.32, null), parser.getCurrentLocation());
        parser.close();
    }

    @Test(expected = JsonParseException.class)
    public void testNullCurrentLocation() throws Exception {
        JsonParser parser = new TreeTraversingParser(null);
        parser.nextToken(); // throws JsonParseException
        parser.close();
    }

    @Test
    public void testReadBinaryValue() throws Exception {
        String json = "\"data:image/jpeg;base64,/9j/4AAQSkZJRgABAQ...";
        JsonParser parser = new TreeTraversingParser(JsonNode.parse(json));
        parser.nextToken(); // VALUE_STRING
        byte[] data = parser.getBinaryValue(Base64Variant.STANDARD);
        assertNotNull(data);
        assertEquals(data.length, 4096);
        parser.close();
    }
}