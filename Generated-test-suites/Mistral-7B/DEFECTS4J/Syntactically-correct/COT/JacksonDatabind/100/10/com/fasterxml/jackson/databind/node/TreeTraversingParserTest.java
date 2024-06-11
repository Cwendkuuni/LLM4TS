package com.example.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;

public class TreeTraversingParserTest {

    @Test
    public void testEmptyTreeParser() throws Exception {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        JsonToken token = parser.nextToken();
        assertNull(token);
    }

    @Test
    public void testArrayParser() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("[1, 2, 3]");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.START_ARRAY, token);

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_INT, token);
        assertEquals(1, parser.getIntValue());

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_INT, token);
        assertEquals(2, parser.getIntValue());

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_INT, token);
        assertEquals(3, parser.getIntValue());

        token = parser.nextToken();
        assertEquals(JsonToken.END_ARRAY, token);

        parser.close();
    }

    @Test
    public void testObjectParser() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("{\"name\": \"John\", \"age\": 30}");
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
        assertEquals(JsonToken.VALUE_INT, token);
        assertEquals(30, parser.getIntValue());

        token = parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, token);

        parser.close();
    }

    @Test(expected = IOException.class)
    public void testClosedParser() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("[1, 2, 3]");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.close();
        parser.nextToken();
    }

    @Test(expected = JsonParseException.class)
    public void testParserWithNullJsonNode() throws Exception {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        parser.nextToken();
    }

    @Test(expected = JsonParseException.class)
    public void testParserWithEmptyJsonNode() throws Exception {
        TreeTraversingParser parser = new TreeTraversingParser(JsonNode.NULL);
        parser.nextToken();
    }
}