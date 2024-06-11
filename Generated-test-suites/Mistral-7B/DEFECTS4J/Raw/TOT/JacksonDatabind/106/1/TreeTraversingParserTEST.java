package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import static org.junit.Assert.*;

public class TreeTraversingParserTest {

    @Test
    public void testSimpleObjectParsing() throws JsonProcessingException {
        TreeTraversingParser parser = new TreeTraversingParser(createTestObject());
        JsonToken token;

        // Check initial state
        assertFalse(parser.isClosed());
        assertNull(parser.nextToken());

        // Parse object
        token = parser.nextToken();
        assertEquals(JsonToken.START_OBJECT, token);

        token = parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        assertEquals("name", token.asString());

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token);
        assertEquals("John Doe", token.asString());

        token = parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        assertEquals("age", token.asString());

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, token);
        assertEquals(30, token.getIntValue());

        token = parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, token);

        // Check end state
        assertTrue(parser.isClosed());
        assertNull(parser.nextToken());
    }

    @Test
    public void testSimpleArrayParsing() throws JsonProcessingException {
        TreeTraversingParser parser = new TreeTraversingParser(createTestArray());
        JsonToken token;

        // Check initial state
        assertFalse(parser.isClosed());
        assertNull(parser.nextToken());

        // Parse array
        token = parser.nextToken();
        assertEquals(JsonToken.START_ARRAY, token);

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, token);
        assertEquals(1, token.getIntValue());

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, token);
        assertEquals(2, token.getIntValue());

        token = parser.nextToken();
        assertEquals(JsonToken.END_ARRAY, token);

        // Check end state
        assertTrue(parser.isClosed());
        assertNull(parser.nextToken());
    }

    @Test
    public void testEmptyParsing() throws JsonProcessingException {
        TreeTraversingParser parser = new TreeTraversingParser(createTestEmpty());
        JsonToken token;

        // Check initial state
        assertFalse(parser.isClosed());
        assertNull(parser.nextToken());

        // Parse empty object
        token = parser.nextToken();
        assertEquals(JsonToken.START_OBJECT, token);
        token = parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, token);

        // Check end state
        assertTrue(parser.isClosed());
        assertNull(parser.nextToken());
    }

    @Test(expected = JsonParseException.class)
    public void testInvalidParsing() throws JsonProcessingException {
        TreeTraversingParser parser = new TreeTraversingParser(createTestInvalid());
        JsonToken token;

        // Parse invalid JSON
        token = parser.nextToken();
        assertEquals(JsonToken.START_OBJECT, token);

        token = parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        assertEquals("name", token.asString());

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token);
        assertEquals("John", token.asString());

        token = parser.nextToken(); // should be "Doe" but is not present
        assertNull(token);

        // Check error state
        assertTrue(parser.isClosed());
        assertNull(parser.nextToken());
    }

    private JsonNode createTestObject() {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree("{\"name\":\"John Doe\",\"age\":30}");
    }

    private JsonNode createTestArray() {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree("[1,2]");
    }

    private JsonNode createTestEmpty() {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree("{}");
    }

    private JsonNode createTestInvalid() {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree("{\"name\":\"John Doe\"age\":30}");
    }
}