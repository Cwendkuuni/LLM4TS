package com.example.junit; // replace with your package name

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Test;

public class TreeTraversingParserTest {

    

    @Test
    public void testEmptyParser() {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertNull(parser.nextToken());
        parser.close();
    }

    @Test
    public void testParserWithEmptyArray() {
        String json = "[]";
        TreeTraversingParser parser = new TreeTraversingParser(parseJson(json));
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertTrue(parser.nextToken() == JsonToken.END_ARRAY);
        parser.close();
    }

    @Test
    public void testParserWithEmptyObject() {
        String json = "{}";
        TreeTraversingParser parser = new TreeTraversingParser(parseJson(json));
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertTrue(parser.nextToken() == JsonToken.END_OBJECT);
        parser.close();
    }

    @Test
    public void testParserWithValueNode() {
        String json = "42";
        TreeTraversingParser parser = new TreeTraversingParser(parseJson(json));
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(42, parser.getIntValue());
        parser.close();
    }

    @Test
    public void testParserWithStringValue() {
        String json = "\"Hello World\"";
        TreeTraversingParser parser = new TreeTraversingParser(parseJson(json));
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("Hello World", parser.getText());
        parser.close();
    }

    @Test
    public void testParserWithEmptyContainer() {
        String json = "{ }";
        TreeTraversingParser parser = new TreeTraversingParser(parseJson(json));
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        parser.skipChildren();
        assertTrue(parser.nextToken() == JsonToken.END_OBJECT);
        parser.close();
    }

    @Test
    public void testParserWithContainer() {
        String json = "[ 1, 2, 3 ]";
        TreeTraversingParser parser = new TreeTraversingParser(parseJson(json));
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals(1, parser.getIntValue());
        parser.nextToken(); // skip next token (ValueInt 1)
        assertEquals(2, parser.getIntValue());
        parser.nextToken(); // skip next token (ValueInt 2)
        assertEquals(3, parser.getIntValue());
        parser.nextToken(); // skip next token (ValueInt 3)
        assertTrue(parser.nextToken() == JsonToken.END_ARRAY);
        parser.close();
    }

    @Test(expected = JsonParseException.class)
    public void testParserWithInvalidJson() {
        String json = "{\"abcd\": [1, 2]"; // missing comma between property and array
        TreeTraversingParser parser = new TreeTraversingParser(parseJson(json));
        parser.nextToken(); // JsonToken.START_OBJECT
        parser.nextToken(); // JsonToken.FIELD_NAME, "abcd"
        parser.nextToken(); // JsonToken.START_ARRAY
        // expectation is that an JsonParseException is thrown here
        parser.nextToken(); // JsonToken.VALUE_NUMBER_INT, 1
        parser.close();
    }

    @Test(expected = JsonParseException.class)
    public void testParserWithNullNode() {
        String json = "null";
        TreeTraversingParser parser = new TreeTraversingParser(parseJson(json));
        parser.nextToken(); // JsonToken.VALUE_NULL
        parser.close();
    }

    private static JsonNode parseJson(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(jsonString);
    }

    
}