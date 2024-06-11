package com.example.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;

public class TreeTraversingParserTest {

    @Test
    public void testConstructorWithJsonNode() {
        JsonNode jsonNode = new ObjectMapper().readTree("{\"name\": \"John\", \"age\": 30}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        assertNotNull(parser);
    }

    @Test
    public void testConstructorWithJsonNodeAndObjectCodec() {
        JsonNode jsonNode = new ObjectMapper().readTree("{\"name\": \"John\", \"age\": 30}");
        ObjectMapper mapper = new ObjectMapper();
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode, mapper.getObjectCodec());
        assertNotNull(parser);
    }

    @Test(expected = IOException.class)
    public void testNextToken_ClosedParser() throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree("{\"name\": \"John\", \"age\": 30}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.close();
        parser.nextToken();
    }

    @Test
    public void testNextToken_EmptyArray() throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree("[]");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        assertEquals(JsonToken.START_ARRAY, parser.nextToken().getClass());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken().getClass());
    }

    @Test
    public void testNextToken_EmptyObject() throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree("{}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        assertEquals(JsonToken.START_OBJECT, parser.nextToken().getClass());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken().getClass());
    }

    @Test
    public void testNextToken_ValueNode() throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree("\"Hello World\"");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken().getClass());
        assertEquals("Hello World", parser.getText());
    }

    @Test
    public void testNextToken_IntValue() throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree(42);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken().getClass());
        assertEquals(42, parser.getIntValue());
    }

    @Test
    public void testNextToken_FloatValue() throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree(3.14);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        assertEquals(JsonToken.VALUE_NUMBER_FLOAT, parser.nextToken().getClass());
        assertEquals(3.14, parser.getFloatValue(), 0.01);
    }

    @Test
    public void testNextToken_StringValue() throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree("\"Hello World\"");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken().getClass());
        assertEquals("Hello World", parser.getText());
    }

    @Test
    public void testNextToken_StartArray() throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree("[1, 2, 3]");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        assertEquals(JsonToken.START_ARRAY, parser.nextToken().getClass());
    }

    @Test
    public void testNextToken_StartObject() throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree("{\"name\": \"John\", \"age\": 30}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        assertEquals(JsonToken.START_OBJECT, parser.nextToken().getClass());
    }

    @Test
    public void testNextToken_EndArray() throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree("[1, 2, 3]");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        // consume first two elements
        parser.nextToken();
        parser.nextToken();
        assertEquals(JsonToken.END_ARRAY, parser.nextToken().getClass());
    }

    @Test
    public void testNextToken_EndObject() throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree("{\"name\": \"John\", \"age\": 30}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        // consume all keys and values
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, parser.nextToken().getClass());
    }

    @Test(expected = JsonParseException.class)
    public void testNextToken_NullValue() throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree(null);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken();
    }

    @Test
    public void testNextToken_MissingRootValue() throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree(new ByteArrayInputStream(new byte[0]));
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        assertEquals(JsonToken.END_DOCUMENT, parser.nextToken().getClass());
    }
}