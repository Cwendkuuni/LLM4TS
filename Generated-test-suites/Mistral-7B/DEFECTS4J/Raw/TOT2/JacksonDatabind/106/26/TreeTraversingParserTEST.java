package com.fasterxml.jackson.databind.node.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TreeTraversingParser;

import org.junit.Test;

public class TreeTraversingParserTest {

    @Test
    public void testEmptyJsonObject() throws Exception {
        JsonFactory factory = new JsonFactory();
        TreeTraversingParser parser = new TreeTraversingParser(factory.parse("{}"));
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        parser.close();
    }

    @Test
    public void testEmptyJsonArray() throws Exception {
        JsonFactory factory = new JsonFactory();
        TreeTraversingParser parser = new TreeTraversingParser(factory.parse("[]"));
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        parser.close();
    }

    @Test
    public void testSimpleJsonObject() throws Exception {
        JsonFactory factory = new JsonFactory();
        TreeTraversingParser parser = new TreeTraversingParser(factory.parse("{\"name\":\"John\", \"age\":30}"));
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());

        assertEquals("name", parser.getCurrentName());
        assertEquals("John", parser.getText());
        parser.nextToken(); // next name: "age"

        assertEquals("age", parser.getCurrentName());
        assertEquals("30", parser.getText());
        parser.nextToken(); // next value: null

        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        parser.close();
    }

    @Test
    public void testSimpleJsonArray() throws Exception {
        JsonFactory factory = new JsonFactory();
        TreeTraversingParser parser = new TreeTraversingParser(factory.parse("[1, true, \"hello\"]"));
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());

        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(1, parser.getIntValue());
        parser.nextToken(); // next value: true

        assertEquals(JsonToken.VALUE_BOOLEAN, parser.nextToken());
        assertTrue(parser.nextValue().booleanValue());
        parser.nextToken(); // next value: "hello"

        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("hello", parser.getText());
        parser.nextToken(); // next value: null

        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        parser.close();
    }

    @Test(expected = IOException.class)
    public void testEmptyStream() throws Exception {
        JsonFactory factory = new JsonFactory();
        TreeTraversingParser parser = new TreeTraversingParser(factory.getEmptyParser());
        parser.nextToken(); // should throw IOException
        fail("Expected IOException, but did not receive one");
    }

    @Test
    public void testCloseParser() throws Exception {
        JsonFactory factory = new JsonFactory();
        TreeTraversingParser parser = new TreeTraversingParser(factory.parse("{}"));
        parser.close();

        try {
            parser.nextToken();
            fail("Expected IOException, but did not receive one");
        } catch (IOException e) {
            // expected
        }
    }

    @Test(expected = JsonParseException.class)
    public void testInvalidJson() throws Exception {
        JsonFactory factory = new JsonFactory();
        TreeTraversingParser parser = new TreeTraversingParser(factory.parse("{ \"name\":\"John\" }")); // missing closing brace
        parser.nextToken();

        try {
            parser.nextToken();
            fail("Expected JsonParseException, but did not receive one");
        } catch (JsonParseException e) {
            // expected
        }
    }

    @Test(expected = JsonParseException.class)
    public void testNullJson() throws Exception {
        JsonFactory factory = new JsonFactory();
        TreeTraversingParser parser = new TreeTraversingParser(factory.getNullParser());
        parser.nextToken(); // should throw JsonParseException
        fail("Expected JsonParseException, but did not receive one");
    }
}