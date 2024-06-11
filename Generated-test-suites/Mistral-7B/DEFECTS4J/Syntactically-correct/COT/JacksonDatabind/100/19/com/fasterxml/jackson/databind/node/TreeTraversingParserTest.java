package com.example.junit;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;

public class TreeTraversingParserTest {

    @Test
    public void testConstructorWithJsonNode() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("{\"name\": \"John\", \"age\": 30}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        assertNotNull(parser);
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals("name", parser.getCurrentName());
        assertEquals("John", parser.getText());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("age", parser.getCurrentName());
        assertEquals("30", parser.getText());
        assertEquals(JsonToken.VALUE_INT, parser.nextToken());
        assertNull(parser.nextToken());
        parser.close();
    }

    @Test
    public void testConstructorWithJsonNodeAndCodec() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("{\"name\": \"John\", \"age\": 30}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode, mapper.getFactory());

        assertNotNull(parser);
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals("name", parser.getCurrentName());
        assertEquals("John", parser.getText());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("age", parser.getCurrentName());
        assertEquals("30", parser.getText());
        assertEquals(JsonToken.VALUE_INT, parser.nextToken());
        assertNull(parser.nextToken());
        parser.close();
    }

    @Test(expected = IOException.class)
    public void testNextTokenAfterClose() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("{\"name\": \"John\", \"age\": 30}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.close();
        parser.nextToken();
    }

    @Test
    public void testSkipChildren() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("{\"name\": {\"first\": \"John\", \"last\": \"Doe\"}, \"age\": 30}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        assertNotNull(parser);
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        parser.skipChildren();
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("name", parser.getCurrentName());
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        parser.skipChildren();
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("first", parser.getCurrentName());
        assertEquals("John", parser.getText());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("last", parser.getCurrentName());
        assertEquals("Doe", parser.getText());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("age", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_INT, parser.nextToken());
        assertEquals("30", parser.getText());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertNull(parser.nextToken());
    }
}