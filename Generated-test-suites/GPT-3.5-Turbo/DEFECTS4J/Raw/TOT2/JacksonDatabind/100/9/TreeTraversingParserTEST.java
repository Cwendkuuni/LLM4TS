package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;
import org.junit.Test;

public class TreeTraversingParserTest {

    @Test
    public void testConstructor() {
        JsonNode node = new ArrayNode(JsonNodeFactory.instance);
        TreeTraversingParser parser = new TreeTraversingParser(node);
        assertNotNull(parser);
        assertEquals(node, parser.currentNode());
    }

    @Test
    public void testSetCodec() {
        JsonNode node = new ObjectNode(JsonNodeFactory.instance);
        ObjectCodec codec = new ObjectMapper();
        TreeTraversingParser parser = new TreeTraversingParser(node);
        parser.setCodec(codec);
        assertEquals(codec, parser.getCodec());
    }

    @Test
    public void testVersion() {
        JsonNode node = new TextNode("test");
        TreeTraversingParser parser = new TreeTraversingParser(node);
        assertEquals(PackageVersion.VERSION, parser.version());
    }

    @Test
    public void testClose() {
        JsonNode node = TextNode.valueOf("test");
        TreeTraversingParser parser = new TreeTraversingParser(node);
        assertFalse(parser.isClosed());
        parser.close();
        assertTrue(parser.isClosed());
    }

    @Test
    public void testNextToken() {
        JsonNode node = new ObjectNode(JsonNodeFactory.instance);
        TreeTraversingParser parser = new TreeTraversingParser(node);
        try {
            assertNull(parser.getCurrentName());
            assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        } catch (IOException | JsonParseException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testSkipChildren() {
        JsonNode node = new ArrayNode(JsonNodeFactory.instance);
        TreeTraversingParser parser = new TreeTraversingParser(node);
        try {
            parser.nextToken();
            assertEquals(JsonToken.START_ARRAY, parser.getCurrentToken());
            assertEquals(parser, parser.skipChildren());
            assertEquals(JsonToken.END_ARRAY, parser.getCurrentToken());
        } catch (IOException | JsonParseException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testText() {
        JsonNode node = new TextNode("test");
        TreeTraversingParser parser = new TreeTraversingParser(node);
        assertEquals("test", parser.getText());
    }

    @Test
    public void testNumberValue() {
        JsonNode node = new IntNode(42);
        TreeTraversingParser parser = new TreeTraversingParser(node);
        try {
            assertEquals(NumberType.INT, parser.getNumberType());
            assertEquals(BigInteger.valueOf(42), parser.getBigIntegerValue());
            assertEquals(BigDecimal.valueOf(42), parser.getDecimalValue());
            assertEquals(42L, parser.getLongValue());
            assertEquals(42, parser.getIntValue());
            assertEquals(42, parser.getNumberValue());
        } catch (IOException | JsonParseException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testBinaryValue() {
        byte[] data = "test".getBytes();
        JsonNode node = BinaryNode.valueOf(data);
        TreeTraversingParser parser = new TreeTraversingParser(node);
        assertEquals(data, parser.getBinaryValue());
    }

}