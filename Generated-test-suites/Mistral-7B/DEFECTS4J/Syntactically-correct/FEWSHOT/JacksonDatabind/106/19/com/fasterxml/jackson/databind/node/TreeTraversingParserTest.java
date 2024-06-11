package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;

import org.junit.Test;

public class TreeTraversingParserTest {

    @Test
    public void testEmptyParser() {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertEquals(JsonToken.END_DOCUMENT, parser.nextToken());
        assertNull(parser.getCurrentName());
        assertNull(parser.getText());
        parser.close();
    }

    @Test
    public void testParserWithNullNode() {
        TreeTraversingParser parser = new TreeTraversingParser(null, null);
        assertEquals(JsonToken.START_NULL, parser.nextToken());
        assertNull(parser.getCurrentName());
        assertNull(parser.getText());
        parser.close();
    }

    @Test
    public void testParserWithArrayNode() {
        JsonNode jsonNode = new ArrayNode();
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode, null);
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertNull(parser.getCurrentName());
        assertEquals("[]", parser.getText());
        parser.skipChildren();
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        assertNull(parser.getText());
        parser.close();
    }

    @Test
    public void testParserWithObjectNode() {
        JsonNode jsonNode = new ObjectNode(new ObjectMapper().getFactory());
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode, null);
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertNull(parser.getCurrentName());
        assertEquals("{\n}", parser.getText());
        parser.skipChildren();
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertNull(parser.getText());
        parser.close();
    }

    @Test
    public void testParserWithTextNode() {
        JsonNode jsonNode = new TextNode("Hello World");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode, null);
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("Hello World", parser.getText());
        assertEquals("Hello World", parser.getCurrentName());
        assertNull(parser.getBigIntegerValue());
        assertNull(parser.getBigDecimalValue());
        assertNull(parser.getIntValue());
        assertNull(parser.getLongValue());
        assertNull(parser.getNumberValue());
        assertNull(parser.getEmbeddedObject());
        parser.close();
    }

    @Test
    public void testParserWithNumberNode() {
        JsonNode jsonNode = new IntNode(42);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode, null);
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals("42", parser.getText());
        assertEquals("42", parser.getCurrentName());
        assertEquals(42, parser.getIntValue());
        assertEquals(42L, parser.getLongValue());
        assertEquals(42, parser.getNumberValue().intValue());
        assertNull(parser.getBigIntegerValue());
        assertNull(parser.getBigDecimalValue());
        assertNull(parser.getEmbeddedObject());
        parser.close();
    }

    @Test
    public void testParserWithBooleanNode() {
        JsonNode jsonNode = new BooleanNode(true);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode, null);
        assertEquals(JsonToken.VALUE_TRUE, parser.nextToken());
        assertEquals("true", parser.getText());
        assertEquals("true", parser.getCurrentName());
        assertEquals(true, parser.getBooleanValue());
        assertNull(parser.getIntValue());
        assertNull(parser.getLongValue());
        assertNull(parser.getNumberValue());
        assertNull(parser.getEmbeddedObject());
        parser.close();
    }

    @Test
    public void testParserWithNullNodeAndChildren() {
        JsonNode jsonNode = new ObjectNode(new ObjectMapper().getFactory());
        jsonNode.put("name", new TextNode("John"));
        jsonNode.put("age", new IntNode(30));
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode, null);
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals("{\"name\":", parser.getText());
        assertEquals("name", parser.getCurrentName());
        parser.skipChildren();
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("age", parser.getCurrentName());
        assertEquals("30", parser.getText());
        assertEquals("age", parser.getCurrentName());
        parser.skipChildren();
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertNull(parser.getText());
        parser.close();
    }

    @Test
    public void testParserWithNumberNodeAndChildren() {
        JsonNode jsonNode = new ObjectNode(new ObjectMapper().getFactory());
        jsonNode.put("num1", new IntNode(1));
        jsonNode.put("num2", new IntNode(2));
        jsonNode.put("sum", new IntNode(3));
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode, null);
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals("{\"num1\":", parser.getText());
        assertEquals("num1", parser.getCurrentName());
        parser.skipChildren();
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("num2", parser.getCurrentName());
        assertEquals("2", parser.getText());
        assertEquals("num2", parser.getCurrentName());
        parser.skipChildren();
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("sum", parser.getCurrentName());
        assertEquals("3", parser.getText());
        assertEquals("sum", parser.getCurrentName());
        parser.skipChildren();
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertNull(parser.getText());
        parser.close();
    }

    @Test
    public void testParserWithBinaryNode() {
        JsonNode jsonNode = new BinaryNode(new Base64Variant(), new TextNode("Hello World").binaryValue());
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode, null);
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, parser.nextToken());
        byte[] binaryData = parser.getBinaryValue(Base64Variant.DEFAULT);
        assertNotNull(binaryData);
        assertEquals("Hello World", new String(binaryData, "UTF-8"));
        parser.close();
    }
}