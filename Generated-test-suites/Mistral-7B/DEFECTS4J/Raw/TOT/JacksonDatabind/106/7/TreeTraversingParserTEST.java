package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TreeTraversingParserTest {

    @Test
    public void testEmptyArrayParsing() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree("[]");
        TreeTraversingParser parser = new TreeTraversingParser(rootNode);
        JsonToken token;

        // Empty array, no children
        parser.nextToken();
        token = parser.nextToken();
        assertEquals(JsonToken.END_ARRAY, token);
        parser.close();

        // Empty array, with children (should be an error)
        rootNode = mapper.readTree("[null]");
        parser = new TreeTraversingParser(rootNode);
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        assertEquals(JsonToken.VALUE_NULL, parser.getCurrentToken());
        parser._throwInternal(); // force error
    }

    @Test
    public void testArrayParsing() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree("[1,2,3,4,5,6]");
        TreeTraversingParser parser = new TreeTraversingParser(rootNode);

        // Start of array
        parser.nextToken();
        assertEquals(JsonToken.START_ARRAY, parser.getCurrentToken());

        // Array elements
        for (int i = 0; i < 6; i++) {
            parser.nextToken();
            assertEquals(JsonToken.VALUE_NUMBER_INT, parser.getCurrentToken());
            assertEquals(i + 1, parser.getIntValue());
        }

        // End of array
        parser.nextToken();
        assertEquals(JsonToken.END_ARRAY, parser.getCurrentToken());

        parser.close();
    }

    @Test
    public void testObjectParsing() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree("{\"name\":\"John\",\"age\":30,\"address\":{\"city\":\"New York\",\"street\":\"1st Ave\"}}");
        TreeTraversingParser parser = new TreeTraversingParser(rootNode);

        // Start of object
        parser.nextToken();
        assertEquals(JsonToken.START_OBJECT, parser.getCurrentToken());

        // Object properties
        parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, parser.getCurrentToken());
        assertEquals("name", parser.getCurrentName());
        parser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, parser.getCurrentToken());
        assertEquals("John", parser.getText());
        parser.nextToken();

        parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, parser.getCurrentToken());
        assertEquals("age", parser.getCurrentName());
        parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.getCurrentToken());
        assertEquals(30, parser.getIntValue());

        parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, parser.getCurrentToken());
        assertEquals("address", parser.getCurrentName());
        parser.nextToken();
        assertEquals(JsonToken.START_OBJECT, parser.getCurrentToken());

        parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, parser.getCurrentToken());
        assertEquals("city", parser.getCurrentName());
        parser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, parser.getCurrentToken());
        assertEquals("New York", parser.getText());
        parser.nextToken();

        parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, parser.getCurrentToken());
        assertEquals("street", parser.getCurrentName());
        parser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, parser.getCurrentToken());
        assertEquals("1st Ave", parser.getText());
        parser.nextToken();

        parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, parser.getCurrentToken());
        parser.nextToken();

        // End of object
        parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, parser.getCurrentToken());

        parser.close();
    }

    @Test
    public void testValueParsing() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree("{\"value\":42}");
        TreeTraversingParser parser = new TreeTraversingParser(rootNode);

        // Start of object
        parser.nextToken();
        assertEquals(JsonToken.START_OBJECT, parser.getCurrentToken());

        // Object property
        parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, parser.getCurrentToken());
        assertEquals("value", parser.getCurrentName());
        parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.getCurrentToken());
        assertEquals(42, parser.getIntValue());

        parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, parser.getCurrentToken());

        parser.close();
    }

    @Test
    public void testNullParsing() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree("{

    }"); // empty object with null value
        TreeTraversingParser parser = new TreeTraversingParser(rootNode);

        // Start of object
        parser.nextToken();
        assertEquals(JsonToken.START_OBJECT, parser.getCurrentToken());

        // Null value
        parser.nextToken();
        parser.nextToken();
        assertEquals(JsonToken.VALUE_NULL, parser.getCurrentToken());

        parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, parser.getCurrentToken());

        parser.close();
    }

    @Test(expected = JsonParseException.class)
    public void testInvalidParsing() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree("{\"invalid\":\"text\"}");
        TreeTraversingParser parser = new TreeTraversingParser(rootNode);

        // Start of object
        parser.nextToken();
        assertEquals(JsonToken.START_OBJECT, parser.getCurrentToken());

        // Invalid value (string instead of number)
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        assertEquals("Text representation of JsonToken.VALUE_NUMBER_INT does not match expected value.", parser._constructErrorMessage());
        parser._throwInternal();
    }

    @Test
    public void testClosedParser() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree("[]");
        TreeTraversingParser parser = new TreeTraversingParser(rootNode);
        parser.close();

        // Closed parser, no more tokens
        parser.nextToken();
        assertNull(parser.getCurrentToken());
    }

    @Test
    public void testEmptyParser() {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertNull(parser.getCurrentToken());
        parser.close();
    }
}