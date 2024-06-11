package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TextNode;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TreeTraversingParserTest {

    private ObjectMapper mapper;
    private JsonNode testNode;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        String jsonString = "{\"name\":\"John\",\"age\":30,\"hobbies\":[\"reading\",\"swimming\"]}";
        try {
            testNode = mapper.readTree(jsonString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @After
    public void teardown() {
        testNode = null;
    }

    @Test
    public void testTreeTraversingParser() {
        TreeTraversingParser parser = new TreeTraversingParser(testNode);

        // Testing basic functionalities
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("name", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("John", parser.getText());

        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("age", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(30, parser.getIntValue());

        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("hobbies", parser.getCurrentName());
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());

        // Testing skipChildren()
        parser.skipChildren();
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("reading", parser.getText());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("swimming", parser.getText());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());

        // Testing isClosed()
        parser.close();
        assertTrue(parser.isClosed());

        // Testing empty container
        TreeTraversingParser emptyParser = new TreeTraversingParser(new TextNode(""));
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertTrue(parser.isClosed());
    }
}