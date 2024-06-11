package com.fasterxml.jackson.databind.node; // or your testing package if different

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TreeTraversingParser;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Test;

public class TreeTraversingParserTest
{
    @Test
    public void testEmptyJsonArray() throws Exception
    {
        JsonParser parser = new TreeTraversingParser(new JsonFactory(new ObjectMapper()).readValue("[]", JsonNode.class));
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertNull(parser.nextToken()); // end of array
        parser.close();
    }

    @Test
    public void testEmptyJsonObject() throws Exception
    {
        JsonParser parser = new TreeTraversingParser(new JsonFactory(new ObjectMapper()).readValue("{}", JsonNode.class));
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertNull(parser.nextToken()); // end of object
        parser.close();
    }

    @Test
    public void testJsonArray() throws Exception
    {
        String json = "[1, 2, 3, \"four\", null, true, false]";
        JsonParser parser = new TreeTraversingParser(new JsonFactory(new ObjectMapper()).readValue(json, JsonNode.class));

        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(1, parser.getIntValue());

        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(2, parser.getIntValue());

        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("four", parser.getText());

        assertEquals(JsonToken.VALUE_NULL, parser.nextToken());

        assertEquals(JsonToken.VALUE_BOOLEAN_TRUE, parser.nextToken());

        assertEquals(JsonToken.VALUE_BOOLEAN_FALSE, parser.nextToken());

        assertEquals(JsonToken.END_ARRAY, parser.nextToken());

        parser.close();
    }

    @Test
    public void testJsonObject() throws Exception
    {
        String json = "{\"key1\": 1, \"key2\": \"two\", \"key3\": null, \"key4\": true, \"key5\": false}";
        JsonParser parser = new TreeTraversingParser(new JsonFactory(new ObjectMapper()).readValue(json, JsonNode.class));

        assertEquals(JsonToken.START_OBJECT, parser.nextToken());

        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("key1", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(1, parser.getIntValue());

        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("key2", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("two", parser.getText());

        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("key3", parser.getCurrentName());
        assertNull(parser.nextToken());

        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("key4", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_BOOLEAN_TRUE, parser.nextToken());

        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("key5", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_BOOLEAN_FALSE, parser.nextToken());

        assertEquals(JsonToken.END_OBJECT, parser.nextToken());

        parser.close();
    }

    @Test(expected = JsonParseException.class)
    public void testMalformedJson() throws Exception
    {
        JsonParser parser = new TreeTraversingParser(new JsonFactory(new ObjectMapper()).readValue("{", JsonNode.class));
        parser.nextToken();
        parser.nextToken(); // expecting ":" instead of next token here
        parser.close();
    }
}