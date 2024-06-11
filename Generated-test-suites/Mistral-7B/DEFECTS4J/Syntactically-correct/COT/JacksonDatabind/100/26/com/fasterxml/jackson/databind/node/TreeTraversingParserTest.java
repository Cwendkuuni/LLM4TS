package com.example.junit; // replace with your test package if necessary

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;

import org.junit.Test;

public class TreeTraversingParserTest {

    @Test
    public void testConstructorWithJsonNode() {
        JsonNode jsonNode = new ObjectMapper().createObjectNode();
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        assertNotNull(parser);
    }

    @Test
    public void testConstructorWithJsonNodeAndObjectCodec() {
        JsonNode jsonNode = new ObjectMapper().createObjectNode();
        ObjectMapper objectMapper = new ObjectMapper();
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode, objectMapper);
        assertNotNull(parser);
    }

    @Test
    public void testSetCodec() {
        JsonNode jsonNode = new ObjectMapper().createObjectNode();
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        ObjectMapper objectMapper = new ObjectMapper();
        parser.setCodec(objectMapper);
        assertEquals(objectMapper, parser.getCodec());
    }

    @Test
    public void testGetCodec() {
        JsonNode jsonNode = new ObjectMapper().createObjectNode();
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        ObjectMapper objectMapper = new ObjectMapper();
        parser.setCodec(objectMapper);
        assertEquals(objectMapper, parser.getCodec());
    }

    @Test(expected = IOException.class)
    public void testClose() throws IOException {
        JsonNode jsonNode = new ObjectMapper().createObjectNode();
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.close();
    }

    @Test
    public void testNextTokenIsStartArray() throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree("[]");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        JsonToken nextToken = parser.nextToken();
        assertEquals(JsonToken.START_ARRAY, nextToken);
    }

    @Test
    public void testNextTokenIsStartObject() throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree("{\"key\": \"value\"}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        JsonToken nextToken = parser.nextToken();
        assertEquals(JsonToken.START_OBJECT, nextToken);
    }

    @Test
    public void testNextTokenIsValue() throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree("\"value\"");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        JsonToken nextToken = parser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, nextToken);
    }

    @Test
    public void testNextTokenIsNull() throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree(null);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        JsonToken nextToken = parser.nextToken();
        assertNull(nextToken);
    }

    @Test
    public void testNextTokenIsEndArrayAfterArray() throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree("[]");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken(); // consume START_ARRAY
        JsonToken nextToken = parser.nextToken();
        assertEquals(JsonToken.END_ARRAY, nextToken);
    }

    @Test
    public void testNextTokenIsEndObjectAfterObject() throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree("{\"key\": \"value\"}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken(); // consume START_OBJECT
        parser.nextToken(); // consume KEY_NAME
        JsonToken nextToken = parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, nextToken);
    }

    @Test(expected = JsonParseException.class)
    public void testNextTokenAfterNull() throws IOException {
        JsonNode jsonNode = null;
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken();
        parser.nextToken();
    }

    @Test
    public void testGetText() throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree("\"value\"");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        String text = parser.getText();
        assertEquals("value", text);
    }

    @Test
    public void testGetTextCharacters() throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree("\"value\"");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        char[] characters = parser.getTextCharacters();
        assertEquals("value".toCharArray(), characters);
    }

    @Test
    public void testGetTextLength() throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree("\"value\"");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        int length = parser.getTextLength();
        assertEquals(6, length);
    }

    @Test
    public void testIsClosed() throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree("[]");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        assertFalse(parser.isClosed());
        parser.close();
        assertTrue(parser.isClosed());
    }

    @Test
    public void testGetCurrentName() throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree("{\"key\": \"value\"}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken(); // consume START_OBJECT
        parser.nextToken(); // consume KEY_NAME
        String name = parser.getCurrentName();
        assertEquals("key", name);
    }

    @Test
    public void testOverrideCurrentName() throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree("{\"key\": \"value\"}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken(); // consume START_OBJECT
        parser.nextToken(); // consume KEY_NAME
        parser.overrideCurrentName("newName");
        String name = parser.getCurrentName();
        assertEquals("newName", name);
    }

    @Test(expected = JsonParseException.class)
    public void testTextAccessAfterClosed() throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree("[]");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.close();
        parser.getText();
    }

    @Test
    public void testNextValueIsValueString() throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree("\"value\"");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        Object value = parser.nextValue();
        String valueString = (String) value;
        assertEquals("value", valueString);
    }

    @Test
    public void testNextValueIsNumber() throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree(42.0);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        Number value = parser.nextValue();
        assertEquals(42.0, value.doubleValue(), 0.0);
    }

    @Test
    public void testNextValueIsBoolean() throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree(true);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        Boolean value = parser.nextValue();
        assertTrue(value);
    }

    @Test
    public void testNextValueIsNull() throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree(null);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        Object value = parser.nextValue();
        assertNull(value);
    }

    @Test(expected = JsonParseException.class)
    public void testNextValueAfterNull() throws IOException {
        JsonNode jsonNode = null;
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken();
        parser.nextToken();
        parser.nextValue();
    }

    @Test
    public void testNextValueIsEmbeddedObject() throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree("{\"name\": {\"age\": 42}}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        Object value = parser.nextValue();
        assertInstanceOf(ObjectNode.class, value);
    }

    @Test
    public void testNextValueIsEmbeddedBinary() throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree("{\"binaryData\": \"RnJvbSBzdHlsZXM=\"}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        Object value = parser.nextValue();
        assertInstanceOf(BinaryNode.class, value);
    }

    @Test
    public void testSkipChildrenIsArray() throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree("[]");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken(); // consume START_ARRAY
        parser.skipChildren();
        JsonToken nextToken = parser.nextToken();
        assertEquals(JsonToken.END_ARRAY, nextToken);
    }

    @Test
    public void testSkipChildrenIsObject() throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree("{\"key\": \"value\"}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken(); // consume START_OBJECT
        parser.nextToken(); // consume KEY_NAME
        parser.skipChildren();
        JsonToken nextToken = parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, nextToken);
    }

    @Test
    public void testNextTokenIsEndArrayWithoutStartContainer() throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree("[]");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken(); // consume START_ARRAY
        JsonToken nextToken = parser.nextToken();
        assertEquals(JsonToken.END_ARRAY, nextToken);
    }

    @Test
    public void testNextTokenIsEndObjectWithoutStartContainer() throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree("{\"key\": \"value\"}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken(); // consume START_OBJECT
        parser.nextToken(); // consume KEY_NAME
        JsonToken nextToken = parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, nextToken);
    }

    @Test
    public void testNextTokenIsEndArrayWithStartContainer() throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree("[]");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken(); // consume START_ARRAY
        parser.nextToken(); // consume START_ARRAY
        JsonToken nextToken = parser.nextToken();
        assertEquals(JsonToken.END_ARRAY, nextToken);
    }

    @Test
    public void testNextTokenIsEndObjectWithStartContainer() throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree("{\"key\": \"value\"}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken(); // consume START_OBJECT
        parser.nextToken(); // consume KEY_NAME
        parser.nextToken(); // consume VALUE_STRING
        parser.nextToken(); // consume START_OBJECT
        JsonToken nextToken = parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, nextToken);
    }

    @Test
    public void testNextTokenIsNullAfterEndArray() throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree("[]");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken(); // consume START_ARRAY
        parser.nextToken(); // consume END_ARRAY
        JsonToken nextToken = parser.nextToken();
        assertNull(nextToken);
    }

    @Test
    public void testNextTokenIsNullAfterEndObject() throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree("{\"key\": \"value\"}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken(); // consume START_OBJECT
        parser.nextToken(); // consume KEY_NAME
        parser.nextToken(); // consume VALUE_STRING
        parser.nextToken(); // consume END_OBJECT
        JsonToken nextToken = parser.nextToken();
        assertNull(nextToken);
    }

    @Test
    public void testNextTokenIsEndArrayAfterValue() throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree("[]");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken(); // consume START_ARRAY
        parser.nextValue();
        JsonToken nextToken = parser.nextToken();
        assertEquals(JsonToken.END_ARRAY, nextToken);
    }

    @Test
    public void testNextTokenIsEndObjectAfterValue() throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree("{\"key\": \"value\"}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken(); // consume START_OBJECT
        parser.nextToken(); // consume KEY_NAME
        parser.nextValue();
        parser.nextToken(); // consume VALUE_STRING
        JsonToken nextToken = parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, nextToken);
    }

    @Test(expected = JsonParseException.class)
    public void testNextTokenAfterNullAfterEndArray() throws IOException {
        JsonNode jsonNode = null;
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken(); // consume START_ARRAY
        parser.nextToken(); // consume END_ARRAY
        parser.nextToken();
    }

    @Test(expected = JsonParseException.class)
    public void testNextTokenAfterNullAfterEndObject() throws IOException {
        JsonNode jsonNode = null;
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken(); // consume START_OBJECT
        parser.nextToken(); // consume KEY_NAME
        parser.nextToken(); // consume VALUE_STRING
        parser.nextToken(); // consume END_OBJECT
        parser.nextToken();
    }
}