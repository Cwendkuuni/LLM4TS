package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.ParserMinimalBase;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonStreamContext;
import com.fasterxml.jackson.databind.node.TextNode;

import org.junit.Test;

public class TreeTraversingParserTest {

    @Test
    public void testEmptyParser() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        parser.nextToken();
        assertNull(parser.getCurrentName());
        assertEquals(JsonToken.END_OBJECT, parser.getCurrentToken());
        parser.close();
    }

    @Test
    public void testValueNodeParser() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(new TextNode("foo"));
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken().getTokenId());
        assertEquals("foo", parser.getText());
        parser.close();
    }

    @Test
    public void testArrayNodeParser() throws IOException {
        JsonNode arrayNode = new TextNode("bar");;
        TreeTraversingParser parser = new TreeTraversingParser(arrayNode);
        assertEquals(JsonToken.START_ARRAY, parser.nextToken().getTokenId());
        assertNull(parser.getText());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken().getTokenId());
        assertEquals("bar", parser.getText());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken().getTokenId());
        parser.close();
    }

    @Test
    public void testObjectNodeParser() throws IOException {
        JsonNode objectNode = new ObjectNode(ObjectMap.emptyObjectMap());
        TreeTraversingParser parser = new TreeTraversingParser(objectNode);
        assertEquals(JsonToken.START_OBJECT, parser.nextToken().getTokenId());
        assertNull(parser.getText());
        assertEquals("", parser.getCurrentName());
        parser.nextToken();
        assertEquals("key", parser.getCurrentName());
        assertNull(parser.getText());
        assertEquals(JsonToken.VALUE_NULL, parser.nextToken().getTokenId());
        parser.nextToken();
        assertEquals("value", parser.getCurrentName());
        assertEquals("value", parser.getText());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken().getTokenId());
        parser.close();
    }

    @Test
    public void testNumericNodeParser() throws IOException {
        JsonNode numericNode = new NumberNode(new BigDecimal("123.45"));
        TreeTraversingParser parser = new TreeTraversingParser(numericNode);
        assertEquals(JsonToken.VALUE_NUMBER_FLOAT, parser.nextToken().getTokenId());
        assertEquals("123.45", parser.getText());
        parser.close();
    }

    @Test
    public void testBinaryNodeParser() throws IOException {
        byte[] binaryData = new byte[] { 0x01, 0x02, 0x03 };
        JsonNode binaryNode = new BinaryNode(binaryData, Base64Variant.STANDARD);
        TreeTraversingParser parser = new TreeTraversingParser(binaryNode);
        assertEquals(JsonToken.VALUE_BINARY, parser.nextToken().getTokenId());
        assertArrayEquals(binaryData, parser.getBinaryValue(Base64Variant.STANDARD));
        parser.close();
    }

    @Test
    public void testNestedNodesParser() throws IOException {
        JsonNode rootNode = new ObjectNode(ObjectMap.emptyObjectMap());
        JsonNode arrayNode = new ArrayNode(JsonNodeType.ARRAY);
        JsonNode objectNode = new ObjectNode(ObjectMap.emptyObjectMap());
        JsonNode nestedArrayNode = new ArrayNode(JsonNodeType.ARRAY);
        JsonNode nestedObjectNode = new ObjectNode(ObjectMap.emptyObjectMap());
        rootNode.set("array", arrayNode);
        arrayNode.add(new TextNode("item1"));
        arrayNode.add(objectNode);
        objectNode.set("object", nestedObjectNode);
        nestedObjectNode.set("foo", new TextNode("bar"));
        nestedObjectNode.set("array", nestedArrayNode);
        nestedArrayNode.add(new TextNode("item1"));
        nestedArrayNode.add(new NumberNode(new BigInteger("123")));

        TreeTraversingParser parser = new TreeTraversingParser(rootNode);
        assertEquals(JsonToken.START_OBJECT, parser.nextToken().getTokenId());
        assertEquals("array", parser.getCurrentName());
        assertEquals(JsonToken.START_ARRAY, parser.nextToken().getTokenId());
        assertEquals("item1", parser.getText());
        parser.nextToken();
        assertEquals("object", parser.getCurrentName());
        assertEquals(JsonToken.START_OBJECT, parser.nextToken().getTokenId());
        assertEquals("foo", parser.getCurrentName());
        assertEquals("bar", parser.getText());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken().getTokenId());
        assertEquals("array", parser.getCurrentName());
        assertEquals(JsonToken.START_ARRAY, parser.nextToken().getTokenId());
        assertEquals("item1", parser.getText());
        assertEquals(JsonToken.VALUE_NUMBER_INTEGER, parser.nextToken().getTokenId());
        assertEquals("123", parser.getText());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken().getTokenId());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken().getTokenId());
        parser.close();
    }

    @Test
    public void testSkipChildren() throws IOException {
        JsonNode arrayNode = new ArrayNode(JsonNodeType.ARRAY);
        TreeTraversingParser parser = new TreeTraversingParser(arrayNode);
        parser.nextToken();
        parser.nextToken();
        parser.skipChildren();
        parser.nextToken();
        parser.nextToken();
        parser.close();
    }

    @Test
    public void testIsClosed() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(new TextNode("foo"));
        assertFalse(parser.isClosed());
        parser.close();
        assertTrue(parser.isClosed());
    }

}