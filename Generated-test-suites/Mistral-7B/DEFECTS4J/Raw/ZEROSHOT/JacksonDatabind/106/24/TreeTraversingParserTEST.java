package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.*;

/**
 * Comprehensive test for the TreeTraversingParser class.
 */
public class TreeTraversingParserTest {

    @Test
    public void testTreeTraversingParser() throws IOException {
        String jsonString = "{\"name\":\"John\",\"age\":30,\"city\":\"New York\",\"hobbies\":[\"reading\",\"swimming\"]}";
        ObjectCodec objectCodec = new ObjectMapper().getFactory().getDefaultInstance();
        JsonNode jsonNode = objectMapper.readTree(jsonString);

        TreeTraversingParser parser = new TreeTraversingParser(jsonNode, objectCodec);

        // Test nextToken()
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
        assertEquals("city", parser.getCurrentName());

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token);
        assertEquals("New York", parser.getText());

        token = parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        assertEquals("hobbies", parser.getCurrentName());

        token = parser.nextToken();
        assertEquals(JsonToken.START_ARRAY, token);

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token);
        assertEquals("reading", parser.getText());

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token);
        assertEquals("swimming", parser.getText());

        token = parser.nextToken();
        assertEquals(JsonToken.END_ARRAY, token);

        token = parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, token);

        // Test closed state
        parser.close();
        token = parser.nextToken();
        assertNull(token);

        // Test skipChildren()
        parser = new TreeTraversingParser(jsonNode, objectCodec);
        parser.nextToken(); // object start
        parser.nextToken(); // field "name"
        parser.nextToken(); // string "John"
        parser.nextToken(); // field "age"
        parser.nextToken(); // int 30
        parser.nextToken(); // field "city"
        parser.nextToken(); // string "New York"
        parser.nextToken(); // field "hobbies"
        parser.nextToken(); // array start
        parser.skipChildren();
        parser.nextToken(); // string "swimming"
        parser.nextToken(); // end array
        parser.nextToken(); // end object

        parser.close();
        token = parser.nextToken();
        assertNull(token);
    }

    @Test
    public void testTreeTraversingParserNullInput() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(null, null);
        JsonToken token = parser.nextToken();
        assertNull(token);
    }

    @Test(expected = JsonParseException.class)
    public void testTreeTraversingParserInvalidJsonInput() throws IOException {
        String jsonString = "{\"name\":\"John\"age:30}"; // missing comma
        ObjectCodec objectCodec = new ObjectMapper().getFactory().getDefaultInstance();
        JsonNode jsonNode = objectMapper.readTree(jsonString);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode, objectCodec);
        parser.nextToken(); // object start
        parser.nextToken(); // field "name"
        parser.nextToken(); // string "John"
        parser.nextToken(); // invalid token "age:"
        // Should throw JsonParseException
    }

    private ObjectMapper objectMapper = new ObjectMapper();

}