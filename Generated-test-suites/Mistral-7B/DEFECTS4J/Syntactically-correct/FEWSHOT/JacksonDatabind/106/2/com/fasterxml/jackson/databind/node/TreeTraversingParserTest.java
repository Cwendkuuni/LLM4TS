package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import org.junit.Test;

public class TreeTraversingParserTest {

    @Test
    public void testCurrentNode() throws IOException {
        JsonNode node = new ObjectNode(new ObjectMapper().getFactory());
        TreeTraversingParser parser = new TreeTraversingParser(node);

        assertEquals(node, parser.currentNode());
    }

    @Test
    public void testCurrentNumericNode() throws IOException {
        JsonNode node = new NumberNode(123, JsonToken.VALUE_NUMBER_INT);
        TreeTraversingParser parser = new TreeTraversingParser(node);

        assertEquals(node, parser.currentNode());
        assertEquals(node, parser.currentNumericNode());
    }

    @Test(expected = JsonParseException.class)
    public void testCurrentNumericNodeNonNumeric() throws IOException {
        JsonNode node = new TextNode("foo", JsonToken.VALUE_STRING);
        TreeTraversingParser parser = new TreeTraversingParser(node);

        parser.currentNumericNode();
    }

    @Test
    public void testGetText() throws IOException {
        JsonNode node = new TextNode("foo", JsonToken.VALUE_STRING);
        TreeTraversingParser parser = new TreeTraversingParser(node);

        assertEquals("foo", parser.getText());
    }

    @Test
    public void testGetTextCharacters() throws IOException {
        JsonNode node = new TextNode("foo", JsonToken.VALUE_STRING);
        TreeTraversingParser parser = new TreeTraversingParser(node);

        char[] expected = "foo".toCharArray();
        assertArrayEquals(expected, parser.getTextCharacters());
    }

    @Test
    public void testGetTextLength() throws IOException {
        JsonNode node = new TextNode("foo", JsonToken.VALUE_STRING);
        TreeTraversingParser parser = new TreeTraversingParser(node);

        assertEquals(3, parser.getTextLength());
    }

    @Test
    public void testGetTextOffset() throws IOException {
        JsonNode node = new TextNode("foo", JsonToken.VALUE_STRING);
        TreeTraversingParser parser = new TreeTraversingParser(node);

        assertEquals(0, parser.getTextOffset());
    }

    @Test
    public void testHasTextCharacters() throws IOException {
        JsonNode node = new TextNode("foo", JsonToken.VALUE_STRING);
        TreeTraversingParser parser = new TreeTraversingParser(node);

        assertFalse(parser.hasTextCharacters());
    }

    @Test
    public void testGetNumberType() throws IOException {
        JsonNode node = new NumberNode(123, JsonToken.VALUE_NUMBER_INT);
        TreeTraversingParser parser = new TreeTraversingParser(node);

        assertEquals(NumberType.INT, parser.getNumberType());
    }

    @Test
    public void testGetBigIntegerValue() throws IOException {
        JsonNode node = new NumberNode(BigInteger.valueOf(123), JsonToken.VALUE_NUMBER_INT);
        TreeTraversingParser parser = new TreeTraversingParser(node);

        assertEquals(BigInteger.valueOf(123), parser.getBigIntegerValue());
    }

    @Test
    public void testGetDecimalValue() throws IOException {
        JsonNode node = new NumberNode(new BigDecimal("123.45"), JsonToken.VALUE_NUMBER_FLOAT);
        TreeTraversingParser parser = new TreeTraversingParser(node);

        assertEquals(new BigDecimal("123.45"), parser.getDecimalValue());
    }

    @Test
    public void testGetDoubleValue() throws IOException {
        JsonNode node = new NumberNode(123.45, JsonToken.VALUE_NUMBER_FLOAT);
        TreeTraversingParser parser = new TreeTraversingParser(node);

        assertEquals(123.45, parser.getDoubleValue(), 0.001);
    }

    @Test
    public void testGetFloatValue() throws IOException {
        JsonNode node = new NumberNode(123.45f, JsonToken.VALUE_NUMBER_FLOAT);
        TreeTraversingParser parser = new TreeTraversingParser(node);

        assertEquals(123.45f, parser.getFloatValue(), 0.001f);
    }

    @Test
    public void testGetIntValue() throws IOException {
        JsonNode node = new NumberNode(123, JsonToken.VALUE_NUMBER_INT);
        TreeTraversingParser parser = new TreeTraversingParser(node);

        assertEquals(123, parser.getIntValue());
    }

    @Test
    public void testGetLongValue() throws IOException {
        JsonNode node = new NumberNode(123L, JsonToken.VALUE_NUMBER_LONG);
        TreeTraversingParser parser = new TreeTraversingParser(node);

        assertEquals(123, parser.getLongValue());
    }

    @Test
    public void testGetNumberValue() throws IOException {
        JsonNode node = new NumberNode(123, JsonToken.VALUE_NUMBER_INT);
        TreeTraversingParser parser = new TreeTraversingParser(node);

        assertEquals(123, parser.getNumberValue().longValue());
    }

    @Test
    public void testGetEmbeddedObject() throws IOException {
        JsonNode objectNode = new ObjectNode(new ObjectMapper().getFactory());
        JsonNode arrayNode = new ArrayNode(new ObjectMapper().getFactory());
        objectNode.set("array", arrayNode);

        TreeTraversingParser parser = new TreeTraversingParser(objectNode);

        assertEquals(arrayNode, parser.getEmbeddedObject());
    }

    @Test
    public void testIsNaN() throws IOException {
        JsonNode node = new NumberNode(Double.NaN, JsonToken.VALUE_NUMBER_FLOAT);
        TreeTraversingParser parser = new TreeTraversingParser(node);

        assertTrue(parser.isNaN());
    }

    @Test
    public void testGetBinaryValue() throws IOException {
        byte[] data = "foo".getBytes();
        JsonNode textNode = new TextNode(new String(data), JsonToken.VALUE_STRING);

        TreeTraversingParser parser = new TreeTraversingParser(textNode);

        byte[] result = parser.getBinaryValue(Base64Variant.DEFAULT);

        assertArrayEquals(data, result);
    }

    @Test
    public void testReadBinaryValue() throws IOException {
        byte[] data = "foo".getBytes();
        JsonNode textNode = new TextNode(new String(data), JsonToken.VALUE_STRING);

        OutputStream out = new ByteArrayOutputStream();
        TreeTraversingParser parser = new TreeTraversingParser(textNode);

        int length = parser.readBinaryValue(Base64Variant.DEFAULT, out);

        assertEquals(data.length, length);
        assertArrayEquals(data, out.toByteArray());
    }
}