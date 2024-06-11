package com.example.junit; // replace with your package name if needed

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectCodec;

import org.junit.Test;

public class TreeTraversingParserTest {

    

    @Test
    public void testConstructorWithJsonNode() throws Exception {
        JsonNode jsonNode = createJsonNode("{}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.START_OBJECT, token);
    }

    @Test
    public void testConstructorWithJsonNodeAndObjectCodec() throws Exception {
        JsonNode jsonNode = createJsonNode("{}");
        ObjectCodec objectCodec = createObjectCodec();
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode, objectCodec);
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.START_OBJECT, token);
    }

    @Test
    public void testNextToken_ReturnsExpectedToken() throws Exception {
        JsonNode jsonNode = createJsonNode("{\"name\": \"John\", \"age\": 30}");
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
        assertEquals(JsonToken.END_OBJECT, token);
    }

    @Test
    public void testSkipChildren() throws Exception {
        JsonNode jsonNode = createJsonNode("{\"name\": {\"first\": \"John\", \"last\": \"Doe\"}}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        parser.nextToken(); // START_OBJECT
        parser.nextToken(); // FIELD_NAME, "name"
        parser.nextToken(); // START_OBJECT

        parser.skipChildren();

        parser.nextToken(); // END_OBJECT
        parser.nextToken(); // END_OBJECT

        assertEquals(JsonToken.END_OBJECT, parser.nextToken().getStatusCode());
    }

    @Test(expected = IOException.class)
    public void testNextToken_WhenParserIsClosed() throws Exception {
        JsonNode jsonNode = createJsonNode("{}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.close();
        parser.nextToken();
    }

    @Test(expected = JsonParseException.class)
    public void testNextToken_WhenParserHasNoMoreTokens() throws Exception {
        JsonNode jsonNode = createJsonNode("{}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        JsonToken token = parser.nextToken();
        parser.nextToken(); // consume the next token, START_OBJECT

        parser.nextToken(); // should throw an exception
    }

    @Test
    public void testGetText() throws Exception {
        JsonNode jsonNode = createJsonNode("{\"name\": \"John\", \"age\": 30}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        assertEquals("John", parser.getText());
        assertEquals("30", parser.getText());
        assertEquals("{\"name\":", parser.getText());
        assertEquals("}", parser.getText());
        assertEquals("", parser.getText());
        assertEquals(null, parser.getText());
    }

    @Test
    public void testGetTextCharacters() throws Exception {
        JsonNode jsonNode = createJsonNode("{\"name\": \"John\", \"age\": 30}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        char[] textChars = parser.getTextCharacters();
        assertEquals("John", new String(textChars, 0, textChars.length));
    }

    @Test
    public void testGetTextLength() throws Exception {
        JsonNode jsonNode = createJsonNode("{\"name\": \"John\", \"age\": 30}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        assertEquals(4, parser.getTextLength());
        assertEquals(5, parser.getTextLength());
        assertEquals(0, parser.getTextLength());
        assertEquals(10, parser.getTextLength());
    }

    @Test
    public void testGetTextOffset() throws Exception {
        JsonNode jsonNode = createJsonNode("{\"name\": \"John\", \"age\": 30}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        assertEquals(0, parser.getTextOffset());
    }

    @Test
    public void testHasTextCharacters() throws Exception {
        JsonNode jsonNode = createJsonNode("{\"name\": \"John\", \"age\": 30}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        assertFalse(parser.hasTextCharacters());
    }

    // Add more test cases for other methods as needed

    

    private JsonNode createJsonNode(String jsonString) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(jsonString);
    }

    private ObjectCodec createObjectCodec() {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.getObjectFactory().getJsonParser().getCodec();
    }
}