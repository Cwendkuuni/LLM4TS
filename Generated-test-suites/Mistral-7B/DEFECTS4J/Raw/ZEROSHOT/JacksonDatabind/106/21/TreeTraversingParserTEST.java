package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link TreeTraversingParser}.
 */
public class TreeTraversingParserTest {

    private JsonParser parser;
    private JsonNode node;

    @Before
    public void setUp() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = "{\"name\":\"John\",\"age\":30,\"hobbies\":[\"football\",\"reading\"]}";
        node = objectMapper.readTree(json);
        parser = new TreeTraversingParser(node);
    }

    @Test
    public void testNextToken() throws Exception {
        JsonToken token;

        token = parser.nextToken();
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
        assertEquals("hobbies", parser.getCurrentName());

        token = parser.nextToken();
        assertEquals(JsonToken.START_ARRAY, token);

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token);
        assertEquals("football", parser.getText());

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token);
        assertEquals("reading", parser.getText());

        token = parser.nextToken();
        assertEquals(JsonToken.END_ARRAY, token);

        token = parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, token);

        token = parser.nextToken();
        assertNull(token);
    }

    @Test
    public void testSkipChildren() throws Exception {
        JsonToken token;

        token = parser.nextToken();
        assertEquals(JsonToken.START_OBJECT, token);

        parser.skipChildren();

        token = parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, token);

        token = parser.nextToken();
        assertNull(token);
    }

    @Test
    public void testIsClosed() throws Exception {
        assertEquals(false, parser.isClosed());

        parser.close();

        assertEquals(true, parser.isClosed());
    }
}