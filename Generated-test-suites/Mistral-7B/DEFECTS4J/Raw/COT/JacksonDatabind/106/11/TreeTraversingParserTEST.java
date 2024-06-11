package com.fasterxml.jackson.databind.node; // make sure you have the proper import statements

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.BaseJsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.BinaryNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

/**
 * JUnit test cases for TreeTraversingParser class
 */
public class TreeTraversingParserTest {

    

    @Test
    public void testEmptyParser() throws Exception {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertNull(parser.nextToken());
        parser.close();
    }

    @Test
    public void testParserWithNullJsonNode() throws Exception {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertNull(parser.nextToken());
        parser.close();
    }

    @Test
    public void testParserWithEmptyArray() throws Exception {
        JsonNode jsonNode = createEmptyArray();
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        JsonToken token = JsonToken.START_ARRAY;
        assertEquals(token, parser.nextToken());
        token = JsonToken.END_ARRAY;
        assertEquals(token, parser.nextToken());
        parser.close();
    }

    @Test
    public void testParserWithEmptyObject() throws Exception {
        JsonNode jsonNode = createEmptyObject();
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        JsonToken token = JsonToken.START_OBJECT;
        assertEquals(token, parser.nextToken());
        token = JsonToken.END_OBJECT;
        assertEquals(token, parser.nextToken());
        parser.close();
    }

    @Test
    public void testParserWithValueNode() throws Exception {
        JsonNode jsonNode = createBooleanValueNode(true);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        JsonToken token = JsonToken.VALUE_BOOLEAN;
        assertEquals(token, parser.nextToken());
        token = JsonToken.END_OBJECT;
        parser.close();
    }

    @Test
    public void testParserWithArrayNode() throws Exception {
        JsonNode jsonNode = createArrayNode();
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        JsonToken token = JsonToken.START_ARRAY;
        assertEquals(token, parser.nextToken());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("element1", parser.getText());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(1, parser.getIntValue());
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, parser.nextToken());
        ObjectNode objectNode = (ObjectNode) parser.getEmbeddedObject();
        assertEquals("key1", objectNode.get("key1").getText());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        parser.close();
    }

    @Test
    public void testParserWithObjectNode() throws Exception {
        JsonNode jsonNode = createObjectNode();
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        JsonToken token = JsonToken.START_OBJECT;
        assertEquals(token, parser.nextToken());
        assertEquals("field1", parser.getCurrentName());
        assertEquals("value1", parser.getText());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("field2", parser.getCurrentName());
        assertEquals("value2", parser.getText());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertNull(parser.getCurrentName());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        parser.close();
    }

    @Test(expected = JsonParseException.class)
    public void testParserWithInvalidJsonNode() throws Exception {
        JsonNode jsonNode = createInvalidJsonNode();
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken();
        parser.close();
    }

    @Test
    public void testParserWithClosedParser() throws Exception {
        TreeTraversingParser parser = new TreeTraversingParser(createEmptyArray());
        parser.close();
        assertFalse(parser.isOpen());
        parser.nextToken(); // should throw an exception
    }

    @Test
    public void testParserWithSkippedChildren() throws Exception {
        JsonNode jsonNode = createArrayNode();
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken(); // START_ARRAY
        parser.skipChildren();
        parser.nextToken(); // END_ARRAY
    }

    @Test
    public void testParserWithParentNode() throws Exception {
        JsonNode jsonNode = createObjectNode();
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken(); // START_OBJECT
        JsonToken token = parser.nextToken();
        assertEquals("field1", parser.getCurrentName());
        parser.nextToken(); // VALUE_STRING
        assertEquals("value1", parser.getText());
        parser.nextToken(); // END_OBJECT
        parser.nextToken(); // START_OBJECT
        token = parser.nextToken();
        assertEquals("field2", parser.getCurrentName());
        parser.nextToken(); // VALUE_STRING
        assertEquals("value2", parser.getText());
        parser.nextToken(); // END_OBJECT
        parser.close();
    }

    

    private JsonNode createEmptyArray() {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree("[]");
    }

    private JsonNode createEmptyObject() {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree("{}");
    }

    private JsonNode createBooleanValueNode(boolean value) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.valueToTree(value);
    }

    private JsonNode createArrayNode() {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode arrayNode = mapper.createArrayNode();
        arrayNode.add("element1");
        arrayNode.add(1);
        arrayNode.add(createObjectNode());
        return arrayNode;
    }

    private JsonNode createObjectNode() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("key1", "value1");
        objectNode.put("key2", createObjectNode());
        return objectNode;
    }

    private JsonNode createInvalidJsonNode() {
        JsonFactory factory = new JsonFactory();
        return factory.createParser("{ this is not valid json }");
    }
}