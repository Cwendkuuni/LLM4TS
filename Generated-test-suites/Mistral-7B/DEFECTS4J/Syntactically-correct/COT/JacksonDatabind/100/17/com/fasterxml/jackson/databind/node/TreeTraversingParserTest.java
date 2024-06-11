package com.example.junit;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;

import org.junit.Test;

/**
 * JUnit test cases for TreeTraversingParser class
 */
public class TreeTraversingParserTest {

    @Test
    public void testConstructorWithJsonNode() {
        JsonNode jsonNode = new ObjectMapper().readTree("{\"name\":\"John\",\"age\":30}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        assertSame(jsonNode, parser.getCurrentNode());
    }

    @Test
    public void testConstructorWithJsonNodeAndObjectCodec() {
        ObjectMapper mapper = new ObjectMapper();
        TreeTraversingParser parser = new TreeTraversingParser(mapper.readTree("{\"name\":\"John\",\"age\":30}"), mapper.getFactory());
        assertSame(mapper, parser.getCodec());
    }

    @Test
    public void testNextToken() throws IOException, JsonParseException {
        ObjectMapper mapper = new ObjectMapper();
        TreeTraversingParser parser = new TreeTraversingParser(mapper.readTree("{\"name\":\"John\",\"age\":30}"), mapper.getFactory());
        JsonToken token;

        token = parser.nextToken();
        assertEquals(JsonToken.START_OBJECT, token);

        token = parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        assertEquals("name", parser.getText());

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token);
        assertEquals("John", parser.getText());

        token = parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        assertEquals("age", parser.getText());

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, token);
        assertEquals(30, parser.getIntValue());

        token = parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, token);
    }

    @Test
    public void testSkipChildren() throws IOException, JsonParseException {
        ObjectMapper mapper = new ObjectMapper();
        TreeTraversingParser parser = new TreeTraversingParser(mapper.readTree("{\"name\":{\"first\":\"John\",\"last\":\"Doe\"}}"), mapper.getFactory());

        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();

        parser.skipChildren();

        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, token);
    }

    @Test(expected = IOException.class)
    public void testCloseParser() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TreeTraversingParser parser = new TreeTraversingParser(mapper.readTree("{\"name\":\"John\",\"age\":30}"), mapper.getFactory());
        parser.close();
        parser.nextToken();
    }

    @Test(expected = JsonParseException.class)
    public void testInvalidToken() throws IOException, JsonParseException {
        ObjectMapper mapper = new ObjectMapper();
        TreeTraversingParser parser = new TreeTraversingParser(mapper.readTree("{\"name\":\"John\",\"age\":30}"), mapper.getFactory());
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken(); // invalid token
    }
}