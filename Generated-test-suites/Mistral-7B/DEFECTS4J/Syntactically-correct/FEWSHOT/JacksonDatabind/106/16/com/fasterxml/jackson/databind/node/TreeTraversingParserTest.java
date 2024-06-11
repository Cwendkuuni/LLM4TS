package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;

import org.junit.Test;

public class TreeTraversingParserTest {

    @Test
    public void testTreeTraversingParser() throws Exception {
        JsonNode jsonNode = Json.parse("[]");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        // Test initial state
        assertNull(parser.nextToken());
        assertEquals(JsonNode.class, parser.getClass());

        // Test empty array
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        assertNull(parser.nextToken());

        // Test empty object
        JsonNode jsonObject = Json.parse("{}");
        parser = new TreeTraversingParser(jsonObject);
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertNull(parser.nextToken());

        // Test value node
        jsonNode = Json.parse("123");
        parser = new TreeTraversingParser(jsonNode);
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(123, parser.getIntValue());
        assertEquals(123, parser.getLongValue());
        assertEquals(123L, parser.getNumberValue().longValue());
        assertEquals("123", parser.getText());
        assertEquals("123", parser.getText().toString());

        // Test string node
        jsonNode = Json.parse("\"Hello World\"");
        parser = new TreeTraversingParser(jsonNode);
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("Hello World", parser.getText());
        assertEquals("Hello World", parser.getText().toString());
        assertEquals("Hello World", parser.getTextCharacters());

        // Test boolean node
        jsonNode = Json.parse("true");
        parser = new TreeTraversingParser(jsonNode);
        assertEquals(JsonToken.VALUE_BOOLEAN, parser.nextToken());
        assertTrue(parser.getBooleanValue());
        assertFalse(parser.nextBoolean());

        // Test null node
        jsonNode = Json.parse("null");
        parser = new TreeTraversingParser(jsonNode);
        assertEquals(JsonToken.VALUE_NULL, parser.nextToken());
        assertNull(parser.getText());
        assertNull(parser.getText().toString());
        assertNull(parser.getTextCharacters());

        // Test array node
        jsonNode = Json.parse("[1, \"hello\", 3.141]");
        parser = new TreeTraversingParser(jsonNode);
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(1, parser.getIntValue());
        assertEquals(1, parser.getLongValue());
        assertEquals(1, parser.getNumberValue().intValue());
        assertEquals("1", parser.getText());
        assertEquals("1", parser.getText().toString());
        assertEquals(1, parser.getText().length());
        assertEquals(1, parser.getTextCharacters()[0]);
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("hello", parser.getText());
        assertEquals("hello", parser.getText().toString());
        assertEquals(5, parser.getText().length());
        assertEquals(5, parser.getTextCharacters().length);
        assertEquals(JsonToken.VALUE_NUMBER_FLOAT, parser.nextToken());
        assertEquals(3.141, parser.getDoubleValue(), 0.0001);
        assertEquals("3.141", parser.getText());
        assertEquals("3.141", parser.getText().toString());
        assertEquals(3, parser.getText().length());
        assertEquals(3.141, parser.getNumberValue().doubleValue(), 0.0001);
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());

        // Test nested array
        jsonNode = Json.parse("[1, [2, [3, 4], 5]]");
        parser = new TreeTraversingParser(jsonNode);
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(1, parser.getIntValue());
        assertEquals(1, parser.getLongValue());
        assertEquals(1, parser.getNumberValue().intValue());
        assertEquals("1", parser.getText());
        assertEquals("1", parser.getText().toString());
        assertEquals(1, parser.getText().length());
        assertEquals(1, parser.getTextCharacters()[0]);
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(2, parser.getIntValue());
        assertEquals(2, parser.getLongValue());
        assertEquals(2, parser.getNumberValue().intValue());
        assertEquals("2", parser.getText());
        assertEquals("2", parser.getText().toString());
        assertEquals(1, parser.getText().length());
        assertEquals(1, parser.getTextCharacters()[0]);
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(3, parser.getIntValue());
        assertEquals(3, parser.getLongValue());
        assertEquals(3, parser.getNumberValue().intValue());
        assertEquals("3", parser.getText());
        assertEquals("3", parser.getText().toString());
        assertEquals(1, parser.getText().length());
        assertEquals(1, parser.getTextCharacters()[0]);
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(4, parser.getIntValue());
        assertEquals(4, parser.getLongValue());
        assertEquals(4, parser.getNumberValue().intValue());
        assertEquals("4", parser.getText());
        assertEquals("4", parser.getText().toString());
        assertEquals(1, parser.getText().length());
        assertEquals(1, parser.getTextCharacters()[0]);
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(5, parser.getIntValue());
        assertEquals(5, parser.getLongValue());
        assertEquals(5, parser.getNumberValue().intValue());
        assertEquals("5", parser.getText());
        assertEquals("5", parser.getText().toString());
        assertEquals(1, parser.getText().length());
        assertEquals(1, parser.getTextCharacters()[0]);
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());

        // Test nested object
        jsonNode = Json.parse("{\"name\": \"John\", \"age\": 30, \"city\": {\"name\": \"New York\", \"country\": \"USA\"}}");
        parser = new TreeTraversingParser(jsonNode);
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals("name", parser.getCurrentName());
        assertEquals("John", parser.getText());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("age", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(30, parser.getIntValue());
        assertEquals(30, parser.getLongValue());
        assertEquals(30, parser.getNumberValue().intValue());
        assertEquals("30", parser.getText());
        assertEquals("30", parser.getText().toString());
        assertEquals(1, parser.getText().length());
        assertEquals(1, parser.getTextCharacters()[0]);
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("city", parser.getCurrentName());
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals("name", parser.getCurrentName());
        assertEquals("New York", parser.getText());
        assertEquals("New York", parser.getText().toString());
        assertEquals(7, parser.getText().length());
        assertEquals(7, parser.getTextCharacters().length);
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("country", parser.getCurrentName());
        assertEquals("USA", parser.getText());
        assertEquals("USA", parser.getText().toString());
        assertEquals(3, parser.getText().length());
        assertEquals(3, parser.getTextCharacters().length);
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());

        // Test skipChildren
        jsonNode = Json.parse("{\"name\": [1, 2, 3], \"age\": [\"John\", \"Doe\"]}");
        parser = new TreeTraversingParser(jsonNode);
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals("name", parser.getCurrentName());
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(1, parser.getIntValue());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(2, parser.getIntValue());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(3, parser.getIntValue());
        parser.skipChildren();
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("age", parser.getCurrentName());
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("John", parser.getText());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("Doe", parser.getText());
        parser.skipChildren();
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());

        // Test close
        parser.close();
        assertNull(parser.nextToken());
    }

    @Test
    public void testTreeTraversingParser_close() throws Exception {
        JsonNode jsonNode = Json.parse("[]");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        // Test closing parser before all tokens are read
        parser.close();
        assertNull(parser.nextToken());

        // Test re-opening parser after closing it
        parser = new TreeTraversingParser(jsonNode);
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        parser.close();
        assertNull(parser.nextToken());
        parser = new TreeTraversingParser(jsonNode);
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
    }
}