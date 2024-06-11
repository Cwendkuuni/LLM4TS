package com.example.test; // replace with your test package name

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TreeTraversingParser;

import org.junit.Before;
import org.junit.Test;

public class TreeTraversingParserTest {

    private ObjectMapper mapper;
    private JsonNode jsonNode;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        jsonNode = mapper.readTree("{\"name\":\"John\", \"age\":30, \"cars\":[\"Ford\", \"BMW\", \"Fiat\"]}");
    }

    

    // Test constructor
    @Test
    public void testTreeTraversingParserConstruction() {
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        assertNotNull(parser);
    }

    // Test nextToken method
    @Test
    public void testNextToken() throws Exception {
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
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
        assertEquals(JsonToken.VALUE_NUMBER_INT, token);
        assertEquals(30, parser.getIntValue());

        token = parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        assertEquals("cars", parser.getCurrentName());

        token = parser.nextToken();
        assertEquals(JsonToken.START_ARRAY, token);

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token);
        assertEquals("Ford", parser.getText());

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token);
        assertEquals("BMW", parser.getText());

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token);
        assertEquals("Fiat", parser.getText());

        token = parser.nextToken();
        assertEquals(JsonToken.END_ARRAY, token);

        token = parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, token);

        token = parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, token);
    }

    // Test skipChildren method
    @Test
    public void testSkipChildren() throws Exception {
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken(); // skip object start
        parser.nextToken(); // skip field name
        parser.nextToken(); // skip string value

        parser.skipChildren();

        parser.nextToken(); // should be next field, not the next String value in the array
        assertEquals("age", parser.getCurrentName());

        parser.nextToken(); // string value for age
        assertEquals("30", parser.getText());

        parser.skipChildren();

        parser.nextToken(); // should be next field, not the next String value in the array
        assertEquals("cars", parser.getCurrentName());

        parser.nextToken(); // array start
        parser.nextToken(); // string value for Ford
        parser.nextToken(); // string value for BMW
        parser.nextToken(); // string value for Fiat
        parser.nextToken(); // array end

        parser.nextToken(); // end object
        parser.nextToken(); // end object
        parser.nextToken(); // end of input
    }

    // Test isClosed method
    @Test
    public void testIsClosed() throws Exception {
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        assertFalse(parser.isClosed());

        parser.close();
        assertTrue(parser.isClosed());
    }

    // Exception handling tests
    @Test(expected = IOException.class)
    public void testIOExceptionWhenClosed() throws Exception {
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.close();
        parser.nextToken();
    }

    @Test(expected = JsonParseException.class)
    public void testJsonParseExceptionWhenNullJsonNode() {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        parser.nextToken();
    }

    @Test(expected = JsonParseException.class)
    public void testJsonParseExceptionWhenInvalidJson() {
        TreeTraversingParser parser = new TreeTraversingParser("{Invalid JSON}");
        parser.nextToken();
    }

    
}