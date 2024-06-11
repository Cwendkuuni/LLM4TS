package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.ParserMinimalBase;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Test cases for {@link TreeTraversingParser} class.
 */
public class TreeTraversingParserTest {

    @Test
    public void testEmptyParser() throws Exception {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, token);
        parser.close();
    }

    @Test
    public void testParserWithObject() throws Exception {
        String json = "{\"name\":\"John\",\"age\":30,\"city\":\"New York\"}";
        TreeTraversingParser parser = new TreeTraversingParser(JsonParseUtils.parseMemoized(json));

        // Check initial state
        assertNull(parser.getCurrentName());
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());

        // Test field values
        assertEquals("name", parser.getCurrentName());
        assertEquals("John", parser.getText());
        assertEquals("name", parser.getCurrentName());
        parser.nextToken(); // move to next field

        assertEquals("age", parser.getCurrentName());
        assertEquals("30", parser.getText());
        assertEquals("age", parser.getCurrentName());
        parser.nextToken(); // move to next field

        assertEquals("city", parser.getCurrentName());
        assertEquals("New York", parser.getText());
        assertEquals("city", parser.getCurrentName());
        parser.nextToken(); // move to end of object

        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        parser.close();
    }

    @Test
    public void testParserWithArray() throws Exception {
        String json = "[1, 2.5, \"three\", null, true]";
        TreeTraversingParser parser = new TreeTraversingParser(JsonParseUtils.parseMemoized(json));

        // Check initial state
        assertNull(parser.getCurrentName());
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());

        // Test array elements
        parser.nextToken(); // move to first element
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken().getValueAsInt());
        assertEquals(1, parser.nextIntValue());
        parser.nextToken(); // move to next element

        parser.nextToken(); // move to next element
        assertEquals(JsonToken.VALUE_NUMBER_FLOAT, parser.nextToken().getValueAsInt());
        assertEquals(2.5, parser.nextFloatValue());
        parser.nextToken(); // move to next element

        assertEquals(JsonToken.VALUE_STRING, parser.nextToken().getValueAsInt());
        assertEquals("three", parser.getText());
        parser.nextToken(); // move to next element

        assertNull(parser.nextToken()); // move to null value

        assertEquals(JsonToken.VALUE_TRUE, parser.nextToken());
        assertEquals(true, parser.nextBooleanValue());
        parser.nextToken(); // move to end of array

        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        parser.close();
    }

    @Test
    public void testParserWithComplexJson() throws Exception {
        String json = "{\"name\":{\"first\":\"John\", \"last\":\"Doe\"}, \"age\":30, \"city\":\"New York\", \"hobbies\":[\"reading\",\"swimming\",\"cooking\"]}";
        TreeTraversingParser parser = new TreeTraversingParser(JsonParseUtils.parseMemoized(json));

        // Check initial state
        assertNull(parser.getCurrentName());
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());

        // Test nested object
        assertEquals("name", parser.getCurrentName());
        parser.nextToken(); // move to first field
        assertEquals("first", parser.getCurrentName());
        assertEquals("John", parser.getText());
        parser.nextToken(); // move to next field
        assertEquals("last", parser.getCurrentName());
        assertEquals("Doe", parser.getText());
        parser.nextToken(); // move to end of object

        // Test other fields
        assertEquals("age", parser.getCurrentName());
        assertEquals("30", parser.getText());
        parser.nextToken(); // move to city field
        assertEquals("city", parser.getCurrentName());
        assertEquals("New York", parser.getText());
        parser.nextToken(); // move to hobbies field
        assertEquals("hobbies", parser.getCurrentName());
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());

        // Test array elements
        parser.nextToken(); // move to first element
        assertEquals("reading", parser.getText());
        parser.nextToken(); // move to next element
        assertEquals("swimming", parser.getText());
        parser.nextToken(); // move to next element
        assertEquals("cooking", parser.getText());
        parser.nextToken(); // move to end of array

        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        parser.close();
    }
}