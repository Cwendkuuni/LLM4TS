package com.example.junit;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

import org.junit.Test;

public class TreeTraversingParserTest {

    @Test
    public void testConstructorWithJsonNode() {
        JsonNode jsonNode = createTestJsonNode();
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        // Test other methods here
        parser.close();
    }

    @Test
    public void testSetCodec() {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree("{\"name\": \"John\", \"age\": 30}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        ObjectCodec codec = objectMapper.getFactory().getCodec();
        parser.setCodec(codec);
        // Test other methods here
        parser.close();
    }

    @Test
    public void testNextToken() throws IOException, JsonParseException {
        JsonNode jsonNode = createTestJsonNode();
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        assertNextToken(parser, JsonToken.START_OBJECT);
        // Test other tokens here
        parser.close();
    }

    @Test
    public void testSkipChildren() throws IOException, JsonParseException {
        JsonNode jsonNode = createTestJsonNode();
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken(); // START_OBJECT
        parser.skipChildren();
        assertNextToken(parser, JsonToken.END_OBJECT);
        parser.close();
    }

    @Test(expected = JsonParseException.class)
    public void testIsClosedWhenClosed() throws IOException, JsonParseException {
        JsonNode jsonNode = createTestJsonNode();
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.close();
        parser.isClosed();
    }

    @Test(expected = JsonParseException.class)
    public void testIsClosedWhenNotClosed() throws IOException, JsonParseException {
        JsonNode jsonNode = createTestJsonNode();
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken(); // START_OBJECT
        parser.isClosed();
    }

    @Test
    public void testGetText() throws IOException, JsonParseException {
        JsonNode jsonNode = createTestJsonNode();
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken(); // "name"
        String text = parser.getText();
        assertEquals("name", text);
        parser.nextToken(); // "John"
        text = parser.getText();
        assertEquals("John", text);
        parser.close();
    }

    @Test
    public void testGetTextCharacters() throws IOException, JsonParseException {
        JsonNode jsonNode = createTestJsonNode();
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken(); // "name"
        char[] textChars = parser.getTextCharacters();
        assertEquals("name", new String(textChars));
        parser.nextToken(); // "John"
        textChars = parser.getTextCharacters();
        assertEquals("John", new String(textChars));
        parser.close();
    }

    @Test
    public void testGetTextLength() throws IOException, JsonParseException {
        JsonNode jsonNode = createTestJsonNode();
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken(); // "name"
        int length = parser.getTextLength();
        assertEquals(5, length);
        parser.nextToken(); // "John"
        length = parser.getTextLength();
        assertEquals(5, length);
        parser.close();
    }

    @Test
    public void testGetTextOffset() throws IOException, JsonParseException {
        JsonNode jsonNode = createTestJsonNode();
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken(); // "name"
        int offset = parser.getTextOffset();
        assertEquals(0, offset);
        parser.nextToken(); // "John"
        offset = parser.getTextOffset();
        assertEquals(0, offset);
        parser.close();
    }

    @Test
    public void testHasTextCharacters() throws IOException, JsonParseException {
        JsonNode jsonNode = createTestJsonNode();
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken(); // "name"
        assertFalse(parser.hasTextCharacters());
        parser.nextToken(); // "John"
        assertTrue(parser.hasTextCharacters());
        parser.close();
    }

    @Test
    public void testGetNumberType() throws IOException, JsonParseException {
        JsonNode jsonNode = createTestJsonNode();
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken(); // 123
        NumberType numberType = parser.getNumberType();
        assertEquals(NumberType.INT, numberType);
        parser.nextToken(); // 45.6
        numberType = parser.getNumberType();
        assertEquals(NumberType.FLOAT, numberType);
        parser.close();
    }

    @Test
    public void testGetBigIntegerValue() throws IOException, JsonParseException {
        JsonNode jsonNode = createTestJsonNode();
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken(); // 123
        BigInteger bigInteger = parser.getBigIntegerValue();
        assertEquals(new BigInteger("123"), bigInteger);
        parser.nextToken(); // 45678901234567890123456789012345
        bigInteger = parser.getBigIntegerValue();
        assertEquals(new BigInteger("45678901234567890123456789012345"), bigInteger);
        parser.close();
    }

    @Test
    public void testGetDecimalValue() throws IOException, JsonParseException {
        JsonNode jsonNode = createTestJsonNode();
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken(); // 123.45
        BigDecimal decimal = parser.getDecimalValue();
        assertEquals(new BigDecimal("123.45"), decimal);
        parser.nextToken(); // -123.45
        decimal = parser.getDecimalValue();
        assertEquals(new BigDecimal("-123.45"), decimal);
        parser.close();
    }

    @Test
    public void testGetDoubleValue() throws IOException, JsonParseException {
        JsonNode jsonNode = createTestJsonNode();
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken(); // 123.45
        double doubleValue = parser.getDoubleValue();
        assertEquals(123.45, doubleValue, 0.001);
        parser.nextToken(); // -123.45
        doubleValue = parser.getDoubleValue();
        assertEquals(-123.45, doubleValue, 0.001);
        parser.close();
    }

    @Test
    public void testGetFloatValue() throws IOException, JsonParseException {
        JsonNode jsonNode = createTestJsonNode();
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken(); // 123.45
        float floatValue = parser.getFloatValue();
        assertEquals(123.45f, floatValue, 0.001f);
        parser.nextToken(); // -123.45
        floatValue = parser.getFloatValue();
        assertEquals(-123.45f, floatValue, 0.001f);
        parser.close();
    }

    @Test
    public void testGetIntValue() throws IOException, JsonParseException {
        JsonNode jsonNode = createTestJsonNode();
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken(); // 123
        int intValue = parser.getIntValue();
        assertEquals(123, intValue);
        parser.nextToken(); // -123
        intValue = parser.getIntValue();
        assertEquals(-123, intValue);
        parser.close();
    }

    @Test
    public void testGetLongValue() throws IOException, JsonParseException {
        JsonNode jsonNode = createTestJsonNode();
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken(); // 1234567890123456789012345678901234567890
        long longValue = parser.getLongValue();
        assertEquals(1234567890123456789012345678901234567890L, longValue);
        parser.nextToken(); // -1234567890123456789012345678901234567890
        longValue = parser.getLongValue();
        assertEquals(-1234567890123456789012345678901234567890L, longValue);
        parser.close();
    }

    @Test
    public void testGetNumberValue() throws IOException, JsonParseException {
        JsonNode jsonNode = createTestJsonNode();
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken(); // 123
        Number numberValue = parser.getNumberValue();
        assertEquals(123, numberValue.intValue());
        parser.nextToken(); // 45.6
        numberValue = parser.getNumberValue();
        assertEquals(45.6, numberValue.doubleValue(), 0.001);
        parser.close();
    }

    @Test
    public void testGetEmbeddedObject() throws IOException, JsonParseException {
        JsonNode jsonNode = createTestJsonNode();
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken(); // {"name": "John", "age": 30}
        Object object = parser.getEmbeddedObject();
        assertEquals(30, ((ObjectNode) object).get("age").asInt());
        parser.nextToken(); // {"name": "Jane", "age": 25}
        object = parser.getEmbeddedObject();
        assertEquals(25, ((ObjectNode) object).get("age").asInt());
        parser.close();
    }

    @Test
    public void testIsNaN() throws IOException, JsonParseException {
        JsonNode jsonNode = createTestJsonNode();
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken(); // NaN
        assertTrue(parser.isNaN());
        parser.nextToken(); // 123
        assertFalse(parser.isNaN());
        parser.close();
    }

    @Test
    public void testGetBinaryValue() throws IOException, JsonParseException {
        JsonNode jsonNode = createTestJsonNodeWithBinaryData();
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        byte[] binaryValue = parser.getBinaryValue(Base64Variant.STANDARD);
        assertArrayEquals(new byte[] { 72, 69, 70, 73, 68, 65, 74, 73, 72 }, binaryValue);
        parser.close();
    }

    @Test
    public void testReadBinaryValue() throws IOException, JsonParseException {
        JsonNode jsonNode = createTestJsonNodeWithBinaryData();
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        byte[] buffer = new byte[10];
        int length = parser.readBinaryValue(Base64Variant.STANDARD, new ByteArrayOutputStream(buffer));
        assertArrayEquals(new byte[] { 72, 69, 70, 73, 68, 65, 74, 73, 72 }, buffer, 0, length);
        parser.close();
    }

    private JsonNode createTestJsonNode() {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree("{\"name\": \"name\", \"value\": 123}");
    }

    private JsonNode createTestJsonNodeWithBinaryData() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode root = objectMapper.createObjectNode();
        root.put("binaryData", objectMapper.readTree("{ \"binary\": \"VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGZvdWwgdGhpbmdzIHJlc3BvbnNl\"}"));
        return root;
    }

    private void assertNextToken(TreeTraversingParser parser, JsonToken expectedToken) throws IOException, JsonParseException {
        JsonToken actualToken = parser.nextToken();
        assertEquals(expectedToken, actualToken);
    }
}