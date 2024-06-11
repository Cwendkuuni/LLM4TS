package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.ParserMinimalBase;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonParser;
import com.fasterxml.jackson.databind.node.*;

import org.junit.Test;

public class TreeTraversingParserTest {

    @Test
    public void testEmptyParser() {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        JsonToken token;
        assertFalse(parser.isClosed());
        token = parser.nextToken();
        assertNull(token);
        parser.close();
        assertTrue(parser.isClosed());
    }

    @Test
    public void testParserWithJsonNodeArray() {
        TreeTraversingParser parser = new TreeTraversingParser(new ArrayNode(new ObjectNode(true)));
        JsonToken token;

        assertFalse(parser.isClosed());
        token = parser.nextToken();
        assertEquals(JsonToken.START_ARRAY, token);

        token = parser.nextToken();
        assertEquals(JsonToken.START_OBJECT, token);

        parser.skipChildren();

        token = parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, token);

        token = parser.nextToken();
        assertEquals(JsonToken.END_ARRAY, token);

        parser.close();
        assertTrue(parser.isClosed());
    }

    @Test
    public void testParserWithJsonNodeObject() {
        TreeTraversingParser parser = new TreeTraversingParser(new ObjectNode(new Object()));
        JsonToken token;

        assertFalse(parser.isClosed());
        token = parser.nextToken();
        assertEquals(JsonToken.START_OBJECT, token);

        token = parser.nextToken();
        assertEquals("field1", token.asString());
        assertEquals("value1", parser.getText());

        parser.skipChildren();

        token = parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        assertEquals("field2", token.asString());
        assertEquals("value2", parser.getText());

        token = parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, token);

        parser.close();
        assertTrue(parser.isClosed());
    }

    @Test
    public void testParserWithJsonNodeValue() {
        TreeTraversingParser parser = new TreeTraversingParser(new TextNode("test"));
        JsonToken token;

        assertFalse(parser.isClosed());
        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token);
        assertEquals("test", parser.getText());

        parser.close();
        assertTrue(parser.isClosed());
    }

    @Test
    public void testParserWithNullNode() {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        JsonToken token;
        parser.close();
        assertTrue(parser.isClosed());
    }
    
    @Test (expected = JsonParseException.class)
    public void testParserWithInvalidNode() {
        TreeTraversingParser parser = new TreeTraversingParser(new Object()); // Object instead of JsonNode
        JsonToken token;
        token = parser.nextToken();
        fail("Should not reach this point");
    }
}