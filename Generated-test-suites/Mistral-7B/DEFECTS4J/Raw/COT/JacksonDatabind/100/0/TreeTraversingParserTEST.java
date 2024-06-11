package com.example.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TreeTraversingParser;

import org.junit.Test;

public class TreeTraversingParserTest {

    

    @Test
    public void testEmptyParser() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertNull(parser.nextToken());
        parser.close();
    }

    @Test
    public void testParserWithJsonNode() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        JsonNode jsonNode = jsonFactory.readValue("{\"name\":\"John\", \"age\":30}", JsonNode.class);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.START_OBJECT, token);
        assertEquals("name", parser.getCurrentName());
        token = parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        assertEquals("John", parser.getText());
        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token);
        token = parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        assertEquals("age", parser.getCurrentName());
        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, token);
        assertEquals("30", parser.getText());
        token = parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, token);
        parser.close();
    }

    @Test(expected = IOException.class)
    public void testParserWithNullJsonNode() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        parser.nextToken();
    }

    @Test
    public void testParserWithEmptyArrayJsonNode() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        JsonNode jsonNode = jsonFactory.readValue("[]", JsonNode.class);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.START_ARRAY, token);
        parser.skipChildren();
        token = parser.nextToken();
        assertEquals(JsonToken.END_ARRAY, token);
        parser.close();
    }

    @Test
    public void testParserWithEmptyObjectJsonNode() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        JsonNode jsonNode = jsonFactory.readValue("{}", JsonNode.class);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.START_OBJECT, token);
        parser.skipChildren();
        token = parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, token);
        parser.close();
    }

    @Test
    public void testParserWithInvalidJsonNode() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        JsonNode jsonNode = jsonFactory.readValue("{\"name\":[\"John\"}", JsonNode.class);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.START_OBJECT, token);
        parser.skipChildren();
        assertEquals(0, parser.nextToken().getColumnNumber()); // should throw an exception
        parser.close();
    }

    @Test
    public void testParserWithClosedParser() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        JsonNode jsonNode = jsonFactory.readValue("{\"name\":\"John\", \"age\":30}", JsonNode.class);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.close();
        assertNull(parser.nextToken());
    }

    @Test
    public void testParserWithClosedParserAndNextToken() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        JsonNode jsonNode = jsonFactory.readValue("{\"name\":\"John\", \"age\":30}", JsonNode.class);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken(); // consume the START_OBJECT token
        parser.close();
        assertNull(parser.nextToken());
    }

    @Test
    public void testParserWithStartContainerFlag() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        JsonNode jsonNode = jsonFactory.readValue("[1, 2, 3]", JsonNode.class);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken(); // consume the START_ARRAY token
        parser.nextToken(); // consume the first START_ARRAY token for the first element
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, token);
        parser.skipChildren();
        parser.nextToken(); // consume the END_ARRAY token for the first element
        parser.nextToken(); // consume the next START_ARRAY token for the second element
        parser.nextToken(); // consume the VALUE_NUMBER_INT token for the second element
        parser.skipChildren();
        parser.nextToken(); // consume the END_ARRAY token for the second element
        parser.nextToken(); // consume the next VALUE_NUMBER_INT token for the third element
        parser.skipChildren();
        parser.nextToken(); // consume the END_ARRAY token
        parser.close();
    }

    @Test
    public void testParserWithEmptyParserAndNextToken() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        parser.nextToken(); // consume the null token
        parser.close();
        assertNull(parser.nextToken());
    }

    @Test
    public void testParserWithTextValue() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        JsonNode jsonNode = jsonFactory.readValue("{\"name\":\"John\"}", JsonNode.class);
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
        parser.close();
    }

    @Test
    public void testParserWithNumericValue() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        JsonNode jsonNode = jsonFactory.readValue("{\"name\":1}", JsonNode.class);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.START_OBJECT, token);
        token = parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        assertEquals("name", parser.getCurrentName());
        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, token);
        assertEquals("1", parser.getText());
        token = parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        parser.close();
    }

    @Test
    public void testParserWithEmbeddedObject() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"name\":{\"first\":\"John\", \"last\":\"Doe\"}}";
        JsonNode jsonNode = mapper.readTree(json);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.START_OBJECT, token);
        token = parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        assertEquals("name", parser.getCurrentName());
        token = parser.nextToken();
        assertEquals(JsonToken.START_OBJECT, token);
        token = parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        assertEquals("first", parser.getCurrentName());
        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token);
        assertEquals("John", parser.getText());
        token = parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        assertEquals("last", parser.getCurrentName());
        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token);
        assertEquals("Doe", parser.getText());
        token = parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, token);
        token = parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, token);
        parser.close();
    }

    
}