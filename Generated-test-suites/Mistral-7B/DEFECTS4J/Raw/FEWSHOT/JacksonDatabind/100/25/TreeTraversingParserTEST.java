package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.ParserMinimalBase;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;

public class TreeTraversingParserTest {

    @Test
    public void testEmptyParser() {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        parser.nextToken();
        assertTrue(parser.isClosed());
    }

    @Test
    public void testRootParser() {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree("{}");
        TreeTraversingParser parser = new TreeTraversingParser(rootNode);
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals("", parser.getText());
        assertTrue(parser.nextToken() instanceof JsonToken.FIELD_NAME);
        assertEquals("", parser.getText());
        assertTrue(parser.nextToken() instanceof JsonToken.END_OBJECT);
        assertTrue(parser.isClosed());
    }

    @Test
    public void testArrayParser() {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode arrayNode = mapper.readTree("[1, 2, 3]");
        TreeTraversingParser parser = new TreeTraversingParser(arrayNode);
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals("1", parser.getText());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken().getValueAsInt());
        assertEquals("2", parser.getText());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken().getValueAsInt());
        assertEquals("3", parser.getText());
        assertTrue(parser.nextToken() instanceof JsonToken.END_ARRAY);
        assertTrue(parser.isClosed());
    }

    @Test
    public void testObjectParser() {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode objectNode = mapper.readTree("{\"a\": 1, \"b\": 2}");
        TreeTraversingParser parser = new TreeTraversingParser(objectNode);
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals("a", parser.getText());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken().getValueAsInt(-1));
        assertEquals("1", parser.getText());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken().getValueAsInt());
        assertEquals("b", parser.getText());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken().getValueAsInt(-1));
        assertEquals("2", parser.getText());
        assertTrue(parser.nextToken() instanceof JsonToken.END_OBJECT);
        assertTrue(parser.isClosed());
    }

    @Test(expected = JsonParseException.class)
    public void testMissingRootParser() {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        parser.nextToken();
        parser.nextToken();
        fail();
    }

    @Test(expected = JsonParseException.class)
    public void testMissingChildrenParser() {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode emptyNode = mapper.readTree("{}");
        TreeTraversingParser parser = new TreeTraversingParser(emptyNode);
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        fail();
    }

    @Test(expected = JsonParseException.class)
    public void testNullParser() {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        parser.nextToken();
        fail();
    }
}