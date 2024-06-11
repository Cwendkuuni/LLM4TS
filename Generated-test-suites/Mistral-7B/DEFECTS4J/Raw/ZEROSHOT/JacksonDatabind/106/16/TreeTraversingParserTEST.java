package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.ParserMinimalBase;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for TreeTraversingParser
 */
public class TreeTraversingParserTest {

    private ObjectMapper objectMapper;
    private TreeTraversingParser parser;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        String jsonString = "{\"name\":\"John\",\"age\":30,\"hobbies\":[\"reading\",\"swimming\"]}";
        JsonNode jsonNode = objectMapper.readTree(jsonString);
        parser = new TreeTraversingParser(jsonNode);
    }

    @Test
    public void testNextToken() throws Exception {
        // Test nextToken with START_OBJECT
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.START_OBJECT, token);

        // Test nextToken with FIELD_NAME
        token = parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        assertEquals("name", parser.getCurrentName());

        // Test nextToken with VALUE_STRING
        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token);
        assertEquals("John", parser.getText());

        // Test nextToken with COMMA
        token = parser.nextToken();
        assertEquals(JsonToken.COMMA, token);

        // Test nextToken with START_ARRAY
        token = parser.nextToken();
        assertEquals(JsonToken.START_ARRAY, token);

        // Test nextToken with VALUE_STRING inside an array
        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token);
        assertEquals("reading", parser.getText());

        // Test nextToken with COMMA inside an array
        token = parser.nextToken();
        assertEquals(JsonToken.COMMA, token);

        // Test nextToken with END_ARRAY
        token = parser.nextToken();
        assertEquals(JsonToken.END_ARRAY, token);

        // Test nextToken with END_OBJECT
        token = parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, token);

        // Test nextToken after closing the parser
        parser.close();
        token = parser.nextToken();
        assertNull(token);
    }

    @Test
    public void testSkipChildren() throws Exception {
        // Test skipChildren with START_OBJECT
        parser.skipChildren();
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());

        // Test skipChildren with START_ARRAY
        parser.nextToken(); // move to the array
        parser.skipChildren();
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
    }

    @Test
    public void testIsClosed() throws Exception {
        // Test isClosed when parser is open
        assertFalse(parser.isClosed());

        // Test isClosed after closing the parser
        parser.close();
        assertTrue(parser.isClosed());
    }

    @Test
    public void testGetText() throws Exception {
        // Test getText with START_OBJECT
        parser.nextToken();
        assertNull(parser.getText());

        // Test getText with FIELD_NAME
        parser.nextToken();
        assertEquals("name", parser.getText());

        // Test getText with VALUE_STRING
        parser.nextToken();
        assertEquals("John", parser.getText());

        // Test getText with VALUE_NUMBER_INT
        parser.nextToken();
        assertEquals("30", parser.getText());

        // Test getText with VALUE_NUMBER_FLOAT
        parser.nextToken();
        assertEquals("30.0", parser.getText());

        // Test getText with START_ARRAY
        parser.nextToken();
        assertNull(parser.getText());

        // Test getText with VALUE_STRING inside an array
        parser.nextToken();
        assertEquals("reading", parser.getText());

        // Test getText with END_OBJECT
        parser.nextToken();
        assertNull(parser.getText());

        // Test getText after closing the parser
        parser.close();
        assertNull(parser.getText());
    }

    @Test
    public void testGetTextCharacters() throws Exception {
        // Test getTextCharacters with START_OBJECT
        parser.nextToken();
        assertNull(parser.getTextCharacters());

        // Test getTextCharacters with FIELD_NAME
        parser.nextToken();
        assertArrayEquals(new char[]{"n", "a", "m", "e"}, parser.getTextCharacters());

        // Test getTextCharacters with VALUE_STRING
        parser.nextToken();
        assertArrayEquals(new char[]{'J', 'o', 'h', 'n'}, parser.getTextCharacters());

        // Test getTextCharacters with VALUE_NUMBER_INT
        parser.nextToken();
        assertArrayEquals(new char[]{'3', '0'}, parser.getTextCharacters());

        // Test getTextCharacters with VALUE_NUMBER_FLOAT
        parser.nextToken();
        assertArrayEquals(new char[]{'3', '0', '.','0'}, parser.getTextCharacters());

        // Test getTextCharacters with START_ARRAY
        parser.nextToken();
        assertNull(parser.getTextCharacters());

        // Test getTextCharacters with VALUE_STRING inside an array
        parser.nextToken();
        assertArrayEquals(new char[]{'r', 'e', 'a', 'd', 'i', 'n', 'g'}, parser.getTextCharacters());

        // Test getTextCharacters with END_OBJECT
        parser.nextToken();
        assertNull(parser.getTextCharacters());

        // Test getTextCharacters after closing the parser
        parser.close();
        assertNull(parser.getTextCharacters());
    }

    @Test
    public void testGetTextLength() throws Exception {
        // Test getTextLength with START_OBJECT
        parser.nextToken();
        assertEquals(0, parser.getTextLength());

        // Test getTextLength with FIELD_NAME
        parser.nextToken();
        assertEquals(4, parser.getTextLength());

        // Test getTextLength with VALUE_STRING
        parser.nextToken();
        assertEquals(5, parser.getTextLength());

        // Test getTextLength with VALUE_NUMBER_INT
        parser.nextToken();
        assertEquals(2, parser.getTextLength());

        // Test getTextLength with VALUE_NUMBER_FLOAT
        parser.nextToken();
        assertEquals(5, parser.getTextLength());

        // Test getTextLength with START_ARRAY
        parser.nextToken();
        assertEquals(0, parser.getTextLength());

        // Test getTextLength with VALUE_STRING inside an array
        parser.nextToken();
        assertEquals(6, parser.getTextLength());

        // Test getTextLength with END_OBJECT
        parser.nextToken();
        assertEquals(0, parser.getTextLength());

        // Test getTextLength after closing the parser
        parser.close();
        assertEquals(0, parser.getTextLength());
    }

    @Test
    public void testGetTextOffset() throws Exception {
        // Test getTextOffset with START_OBJECT
        parser.nextToken();
        assertEquals(-1, parser.getTextOffset());

        // Test getTextOffset with FIELD_NAME
        parser.nextToken();
        assertEquals(-1, parser.getTextOffset());

        // Test getTextOffset with VALUE_STRING
        parser.nextToken();
        assertEquals(0, parser.getTextOffset());

        // Test getTextOffset with VALUE_NUMBER_INT
        parser.nextToken();
        assertEquals(0, parser.getTextOffset());

        // Test getTextOffset with VALUE_NUMBER_FLOAT
        parser.nextToken();
        assertEquals(0, parser.getTextOffset());

        // Test getTextOffset with START_ARRAY
        parser.nextToken();
        assertEquals(-1, parser.getTextOffset());

        // Test getTextOffset with VALUE_STRING inside an array
        parser.nextToken();
        assertEquals(0, parser.getTextOffset());

        // Test getTextOffset with END_OBJECT
        parser.nextToken();
        assertEquals(-1, parser.getTextOffset());

        // Test getTextOffset after closing the parser
        parser.close();
        assertEquals(-1, parser.getTextOffset());
    }

    @Test
    public void testHasTextCharacters() throws Exception {
        // Test hasTextCharacters with START_OBJECT
        parser.nextToken();
        assertFalse(parser.hasTextCharacters());

        // Test hasTextCharacters with FIELD_NAME
        parser.nextToken();
        assertFalse(parser.hasTextCharacters());

        // Test hasTextCharacters with VALUE_STRING
        parser.nextToken();
        assertTrue(parser.hasTextCharacters());

        // Test hasTextCharacters with VALUE_NUMBER_INT
        parser.nextToken();
        assertFalse(parser.hasTextCharacters());

        // Test hasTextCharacters with VALUE_NUMBER_FLOAT
        parser.nextToken();
        assertFalse(parser.hasTextCharacters());

        // Test hasTextCharacters with START_ARRAY
        parser.nextToken();
        assertFalse(parser.hasTextCharacters());

        // Test hasTextCharacters with VALUE_STRING inside an array
        parser.nextToken();
        assertTrue(parser.hasTextCharacters());

        // Test hasTextCharacters with END_OBJECT
        parser.nextToken();
        assertFalse(parser.hasTextCharacters());

        // Test hasTextCharacters after closing the parser
        parser.close();
        assertFalse(parser.hasTextCharacters());
    }
}