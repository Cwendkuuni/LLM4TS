package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.ParserMinimalBase;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectCodec;
import com.fasterxml.jackson.databind.node.BaseTest;

import org.junit.Test;

public class TreeTraversingParserTest extends BaseTest {

    @Test
    public void testTreeTraversingParserConstructorWithJsonNode() throws IOException {
        JsonNode node = createObjectNode();
        TreeTraversingParser parser = new TreeTraversingParser(node);
        assertEquals(node, parser.getCurrentNode());
    }

    @Test
    public void testTreeTraversingParserConstructorWithJsonNodeAndObjectCodec() throws IOException {
        JsonNode node = createObjectNode();
        ObjectCodec objectCodec = createObjectCodec();
        TreeTraversingParser parser = new TreeTraversingParser(node, objectCodec);
        assertEquals(node, parser.getCurrentNode());
    }

    @Test
    public void testSetCodec() throws IOException {
        JsonNode node = createObjectNode();
        ObjectCodec oldCodec = createObjectCodec();
        TreeTraversingParser parser = new TreeTraversingParser(node);
        ObjectCodec newCodec = createObjectCodec();
        parser.setCodec(newCodec);
        assertEquals(newCodec, parser.getCodec());
    }

    @Test
    public void testGetCodec() throws IOException {
        JsonNode node = createObjectNode();
        TreeTraversingParser parser = new TreeTraversingParser(node);
        ObjectCodec codec = parser.getCodec();
        assertNotNull(codec);
    }

    @Test
    public void testVersion() {
        TreeTraversingParser parser = new TreeTraversingParser(createObjectNode());
        assertEquals("2.11.2", parser.version().toString());
    }

    @Test
    public void testClose() throws IOException {
        JsonNode node = createObjectNode();
        TreeTraversingParser parser = new TreeTraversingParser(node);
        parser.close();
        assertNull(parser.getCurrentNode());
    }

    @Test
    public void testNextToken() throws IOException {
        JsonNode node = createArrayNode();
        TreeTraversingParser parser = new TreeTraversingParser(node);
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.START_ARRAY, token);
    }

    @Test
    public void testNextTokenWithStartContainerFlag() throws IOException {
        JsonNode node = createArrayNode();
        TreeTraversingParser parser = new TreeTraversingParser(node);
        parser.nextToken(); // consume START_ARRAY
        parser.setStartContainer(true);
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.START_ARRAY, token);
    }

    @Test(expected = JsonParseException.class)
    public void testNextTokenAfterEndOfInput() throws IOException {
        JsonNode node = createObjectNode();
        TreeTraversingParser parser = new TreeTraversingParser(node);
        JsonToken token = parser.nextToken();
        parser.nextToken(); // consume START_OBJECT
        parser.nextToken(); // consume field name
        parser.nextToken(); // consume VALUE_STRING
        parser.nextToken(); // consume END_OBJECT
        parser.nextToken(); // should throw exception
    }

    @Test
    public void testSkipChildren() throws IOException {
        JsonNode node = createObjectNode();
        node.set("field1", createIntNode(1));
        node.set("field2", createIntNode(2));
        TreeTraversingParser parser = new TreeTraversingParser(node);
        parser.nextToken(); // consume START_OBJECT
        parser.nextToken(); // consume field name "field1"
        parser.nextToken(); // consume INT_VALUE 1
        parser.skipChildren();
        parser.nextToken(); // consume INT_VALUE 2
        parser.nextToken(); // consume END_OBJECT
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
    }

    @Test
    public void testIsClosed() {
        JsonNode node = createObjectNode();
        TreeTraversingParser parser = new TreeTraversingParser(node);
        assertFalse(parser.isClosed());
        parser.close();
        assertTrue(parser.isClosed());
    }

    @Test
    public void testGetText() throws IOException {
        JsonNode node = createTextNode("Hello World");
        TreeTraversingParser parser = new TreeTraversingParser(node);
        assertEquals("Hello World", parser.getText());
    }

    @Test
    public void testGetTextCharacters() throws IOException {
        JsonNode node = createTextNode("Hello World");
        TreeTraversingParser parser = new TreeTraversingParser(node);
        char[] characters = parser.getTextCharacters();
        assertEquals("Hello World", new String(characters));
    }

    @Test
    public void testGetTextLength() throws IOException {
        JsonNode node = createTextNode("Hello World");
        TreeTraversingParser parser = new TreeTraversingParser(node);
        assertEquals(13, parser.getTextLength());
    }

    @Test
    public void testGetTextOffset() throws IOException {
        JsonNode node = createTextNode("Hello World");
        TreeTraversingParser parser = new TreeTraversingParser(node);
        assertEquals(0, parser.getTextOffset());
    }

    @Test
    public void testHasTextCharacters() throws IOException {
        JsonNode node = createTextNode("Hello World");
        TreeTraversingParser parser = new TreeTraversingParser(node);
        assertFalse(parser.hasTextCharacters());
    }

    @Test
    public void testGetNumberType() throws IOException {
        JsonNode node = createNumberNode(123.45);
        TreeTraversingParser parser = new TreeTraversingParser(node);
        assertEquals(JsonType.NUMBER, parser.getNumberType());
    }

    @Test
    public void testGetBigIntegerValue() throws IOException {
        JsonNode node = createNumberNode(BigInteger.valueOf(123));
        TreeTraversingParser parser = new TreeTraversingParser(node);
        assertEquals(BigInteger.valueOf(123), parser.getBigIntegerValue());
    }

    @Test
    public void testGetDecimalValue() throws IOException {
        JsonNode node = createNumberNode(new BigDecimal("123.45"));
        TreeTraversingParser parser = new TreeTraversingParser(node);
        assertEquals(new BigDecimal("123.45"), parser.getDecimalValue());
    }

    @Test
    public void testGetDoubleValue() throws IOException {
        JsonNode node = createNumberNode(123.45);
        TreeTraversingParser parser = new TreeTraversingParser(node);
        assertEquals(123.45, parser.getDoubleValue(), 0.0);
    }

    @Test
    public void testGetFloatValue() throws IOException {
        JsonNode node = createNumberNode(123.45f);
        TreeTraversingParser parser = new TreeTraversingParser(node);
        assertEquals(123.45f, parser.getFloatValue(), 0.0f);
    }

    @Test
    public void testGetIntValue() throws IOException {
        JsonNode node = createNumberNode(123);
        TreeTraversingParser parser = new TreeTraversingParser(node);
        assertEquals(123, parser.getIntValue());
    }

    @Test
    public void testGetLongValue() throws IOException {
        JsonNode node = createNumberNode(123L);
        TreeTraversingParser parser = new TreeTraversingParser(node);
        assertEquals(123, parser.getLongValue());
    }

    @Test
    public void testGetNumberValue() throws IOException {
        JsonNode node = createNumberNode(123);
        TreeTraversingParser parser = new TreeTraversingParser(node);
        assertEquals(123, parser.getNumberValue().intValue());
    }

    @Test
    public void testGetEmbeddedObject() throws IOException {
        JsonNode node = createObjectNode();
        node.put("field1", createTextNode("Hello"));
        TreeTraversingParser parser = new TreeTraversingParser(node);
        parser.nextToken(); // consume START_OBJECT
        parser.nextToken(); // consume field name "field1"
        JsonNode field1 = parser.getEmbeddedObject();
        assertEquals("Hello", field1.textValue());
        parser.nextToken(); // consume END_OBJECT
    }

    @Test
    public void testIsNaN() throws IOException {
        JsonNode node = createNumberNode(Double.NaN);
        TreeTraversingParser parser = new TreeTraversingParser(node);
        assertTrue(parser.isNaN());
    }

    @Test
    public void testGetBinaryValue() throws IOException {
        JsonNode node = createBinaryNode("data:image/jpeg;base64,...");
        TreeTraversingParser parser = new TreeTraversingParser(node);
        byte[] data = parser.getBinaryValue(Base64Variants.STANDARD);
        assertNotNull(data);
        // check that binary data is base64 decoded correctly
        byte[] decodedData = org.apache.commons.codec.binary.Base64.decodeBase64(node.asText());
        assertArrayEquals(data, decodedData);
    }

    @Test
    public void testReadBinaryValue() throws IOException {
        JsonNode node = createBinaryNode("data:image/jpeg;base64,...");
        TreeTraversingParser parser = new TreeTraversingParser(node);
        byte[] data = new byte[node.textValue().length()];
        int length = parser.readBinaryValue(Base64Variants.STANDARD, new ByteArrayOutputStream(data.length));
        assertArrayEquals(node.textValue().getBytes(), data);
    }

    private static JsonNode createObjectNode() {
        return factory.objectNode();
    }

    private static JsonNode createArrayNode() {
        return factory.arrayNode();
    }

    private static JsonNode createTextNode(String text) {
        return factory.textNode(text);
    }

    private static JsonNode createNumberNode(Number number) {
        return factory.numberNode(number);
    }

    private static JsonNode createBinaryNode(String base64Data) {
        return factory.binaryNode(Base64Variants.STANDARD, Base64.decodeBase64(base64Data));
    }

    private static ObjectCodec createObjectCodec() {
        return factory.objectMapper().getFactory().createObjectCodec();
    }
}