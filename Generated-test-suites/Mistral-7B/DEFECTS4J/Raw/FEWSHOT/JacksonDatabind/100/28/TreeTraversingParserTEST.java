package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;

public class TreeTraversingParserTest {

    @Test
    public void testEmptyParser() {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertNull(parser.nextToken());
        parser.close();
    }

    @Test
    public void testNullParser() {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        parser.close();
        assertNull(parser.nextToken());
    }

    @Test
    public void testParseEmptyArray() {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("[]");
        TreeTraversingParser parser = new TreeTraversingParser(node);
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.START_ARRAY, token);
        assertEquals(null, parser.nextToken());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        parser.close();
    }

    @Test
    public void testParseEmptyObject() {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("{}");
        TreeTraversingParser parser = new TreeTraversingParser(node);
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.START_OBJECT, token);
        assertEquals(null, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        parser.close();
    }

    @Test
    public void testParseSimpleArray() {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("[1, 2, 3]");
        TreeTraversingParser parser = new TreeTraversingParser(node);
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.START_ARRAY, token);
        assertEquals(JsonToken.INT, parser.nextToken().getValueAsInt());
        assertEquals(JsonToken.INT, parser.nextToken().getValueAsInt());
        assertEquals(JsonToken.INT, parser.nextToken().getValueAsInt());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        parser.close();
    }

    @Test
    public void testParseSimpleObject() {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("{\"name\": \"John\", \"age\": 30}");
        TreeTraversingParser parser = new TreeTraversingParser(node);
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.START_OBJECT, token);
        assertEquals("name", parser.nextValue().asText());
        assertEquals("John", parser.nextValue().asText());
        assertEquals("age", parser.nextValue().asText());
        assertEquals(JsonToken.INT, parser.nextToken().getValueAsInt());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        parser.close();
    }

    @Test
    public void testParseComplexArray() {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("[1, [2, 3], \"foo\"]");
        TreeTraversingParser parser = new TreeTraversingParser(node);
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.START_ARRAY, token);
        assertEquals(JsonToken.INT, parser.nextToken().getValueAsInt());
        token = parser.nextToken();
        assertEquals(JsonToken.START_ARRAY, token);
        assertEquals(JsonToken.INT, parser.nextToken().getValueAsInt());
        assertEquals(JsonToken.INT, parser.nextToken().getValueAsInt());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("foo", parser.nextValue().asText());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        parser.close();
    }

    @Test
    public void testParseComplexObject() {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("{\"name\": { \"first\": \"John\", \"last\": \"Doe\" }, \"age\": 30}");
        TreeTraversingParser parser = new TreeTraversingParser(node);
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.START_OBJECT, token);
        assertEquals("name", parser.nextValue().asText());
        JsonToken nameToken = parser.nextToken();
        assertEquals(JsonToken.START_OBJECT, nameToken);
        assertEquals("first", parser.nextValue().asText());
        assertEquals("John", parser.nextValue().asText());
        assertEquals("last", parser.nextValue().asText());
        assertEquals("Doe", parser.nextValue().asText());
        assertEquals(JsonToken.END_OBJECT, parser.nextValue().getValueAsInt());
        assertEquals("age", parser.nextToken().asText());
        assertEquals(JsonToken.INT, parser.nextToken().getValueAsInt());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        parser.close();
    }

    @Test
    public void testParseEmptyObjectWithCurrent() {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("{}");
        TreeTraversingParser parser = new TreeTraversingParser(node);
        parser.nextToken(); // consume START_OBJECT
        JsonNode currentNode = parser.nextToken();
        parser.close();
        assertEquals(JsonToken.END_OBJECT, currentNode.get(0).getValueAsInt());
    }

    @Test(expected = JsonParseException.class)
    public void testParseInvalidJson() throws JsonParseException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("{abc: 1}");
        TreeTraversingParser parser = new TreeTraversingParser(node);
        parser.nextToken();
        parser.nextToken(); // consume "abc:"
        parser.nextToken(); // expecting COLON, but got INT
    }
}