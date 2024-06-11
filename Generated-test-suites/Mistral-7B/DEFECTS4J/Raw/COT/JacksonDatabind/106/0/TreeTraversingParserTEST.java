package com.example.junit;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;

public class TreeTraversingParserTest {

    @Test
    public void testConstructorWithJsonNode() {
        JsonNode jsonNode = new ObjectMapper().valueToTree(new int[0]);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        parser.close();
    }

    @Test
    public void testConstructorWithJsonNodeAndObjectCodec() {
        JsonNode jsonNode = new ObjectMapper().valueToTree(new int[0]);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode, null);
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        parser.close();
    }

    @Test
    public void testSetCodec() {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.valueToTree(new int[0]);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        ObjectCodec objectCodec = objectMapper.getFactory().getCodec();
        parser.setCodec(objectCodec);
        assertEquals(objectCodec, parser.getCodec());
        parser.close();
    }

    @Test
    public void testGetCodec() {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.valueToTree(new int[0]);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        ObjectCodec objectCodec = objectMapper.getFactory().getCodec();
        parser.setCodec(objectCodec);
        assertEquals(objectCodec, parser.getCodec());
        parser.close();
    }

    @Test(expected = IOException.class)
    public void testCloseTwice() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.valueToTree(new int[0]);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.close();
        parser.close();
    }

    @Test
    public void testNextTokenNormalFlow() throws IOException, JsonParseException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.valueToTree(new int[0]);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        JsonToken jsonToken = parser.nextToken();
        assertEquals(JsonToken.START_ARRAY, jsonToken);
        parser.close();
    }

    @Test
    public void testNextTokenAfterEndOfStream() throws IOException, JsonParseException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.valueToTree(new int[0]);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        assertNull(parser.nextToken());
        parser.close();
    }

    @Test
    public void testNextTokenAfterStartContainer() throws IOException, JsonParseException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.valueToTree(new int[0]);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.setStartContainer(true);
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        ObjectNode objectNode = (ObjectNode) parser.getCurrentNode();
        assertEquals("0", objectNode.get(0).asText());
        parser.close();
    }

    @Test(expected = JsonParseException.class)
    public void testNextTokenAfterEndOfStreamWithStartContainer() throws IOException, JsonParseException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.valueToTree(new int[0]);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.setStartContainer(true);
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        assertNull(parser.nextToken());
    }

    @Test
    public void testSkipChildren() throws IOException, JsonParseException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.valueToTree(new int[0]);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.skipChildren();
        JsonToken jsonToken = parser.nextToken();
        parser.close();
        assertEquals(JsonToken.START_ARRAY, jsonToken);
    }

    @Test
    public void testIsClosed() throws IOException, JsonParseException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.valueToTree(new int[0]);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        assertFalse(parser.isClosed());
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.close();
        assertTrue(parser.isClosed());
    }

    @Test
    public void testgetText() throws IOException, JsonParseException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.valueToTree("{\"name\": \"John Doe\", \"age\": 30}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken();
        parser.nextToken();
        assertEquals("name", parser.getText());
        parser.nextToken();
        assertEquals("John Doe", parser.getText());
        parser.nextToken();
        parser.nextToken();
        assertEquals("age", parser.getText());
        parser.nextToken();
        assertEquals("30", parser.getText());
        parser.close();
    }

    @Test
    public void testgetTextCharacters() throws IOException, JsonParseException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.valueToTree("{\"name\": \"John Doe\", \"age\": 30}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken();
        parser.nextToken();
        char[] nameCharacters = parser.getTextCharacters();
        assertEquals("name", new String(nameCharacters));
        parser.nextToken();
        char[] doeCharacters = parser.getTextCharacters();
        assertEquals("Doe", new String(doeCharacters));
        parser.nextToken();
        parser.nextToken();
        char[] ageCharacters = parser.getTextCharacters();
        assertEquals("age", new String(ageCharacters));
        parser.nextToken();
        char[] thirtyCharacters = parser.getTextCharacters();
        assertEquals("30", new String(thirtyCharacters));
        parser.close();
    }

    @Test
    public void testgetTextLength() throws IOException, JsonParseException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.valueToTree("{\"name\": \"John Doe\", \"age\": 30}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken();
        parser.nextToken();
        int nameLength = parser.getTextLength();
        assertEquals(5, nameLength);
        parser.nextToken();
        int doeLength = parser.getTextLength();
        assertEquals(3, doeLength);
        parser.nextToken();
        parser.nextToken();
        int ageLength = parser.getTextLength();
        assertEquals(3, ageLength);
        parser.nextToken();
        int thirtyLength = parser.getTextLength();
        assertEquals(2, thirtyLength);
        parser.close();
    }

    @Test
    public void testgetTextOffset() throws IOException, JsonParseException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.valueToTree("{\"name\": \"John Doe\", \"age\": 30}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken();
        parser.nextToken();
        int nameOffset = parser.getTextOffset();
        assertEquals(0, nameOffset);
        parser.nextToken();
        int doeOffset = parser.getTextOffset();
        assertEquals(5, doeOffset);
        parser.nextToken();
        parser.nextToken();
        int ageOffset = parser.getTextOffset();
        assertEquals(8, ageOffset);
        parser.nextToken();
        int thirtyOffset = parser.getTextOffset();
        assertEquals(11, thirtyOffset);
        parser.close();
    }

    @Test
    public void testHasTextCharacters() {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertFalse(parser.hasTextCharacters());
        parser.close();
    }

    @Test
    public void testGetNumberType() throws IOException, JsonParseException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.valueToTree(new BigInteger("12345"));
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken();
        parser.nextToken();
        NumberType numberType = parser.getNumberType();
        assertEquals(JsonParser.NumberType.LONG, numberType);
        parser.close();
    }

    @Test
    public void testGetBigIntegerValue() throws IOException, JsonParseException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.valueToTree(new BigInteger("12345"));
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken();
        parser.nextToken();
        BigInteger bigInteger = parser.getBigIntegerValue();
        assertEquals(new BigInteger("12345"), bigInteger);
        parser.close();
    }

    @Test
    public void testGetDecimalValue() throws IOException, JsonParseException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.valueToTree(new BigDecimal("123.45"));
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken();
        parser.nextToken();
        BigDecimal decimal = parser.getDecimalValue();
        assertEquals(new BigDecimal("123.45"), decimal);
        parser.close();
    }

    @Test
    public void testGetDoubleValue() throws IOException, JsonParseException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.valueToTree(new Double(123.45));
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken();
        parser.nextToken();
        double doubleValue = parser.getDoubleValue();
        assertEquals(123.45, doubleValue, 0.0);
        parser.close();
    }

    @Test
    public void testGetFloatValue() throws IOException, JsonParseException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.valueToTree(new Float(123.45F));
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken();
        parser.nextToken();
        float floatValue = parser.getFloatValue();
        assertEquals(123.45f, floatValue, 0.0f);
        parser.close();
    }

    @Test
    public void testGetIntValue() throws IOException, JsonParseException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.valueToTree(new Integer(123));
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken();
        parser.nextToken();
        int intValue = parser.getIntValue();
        assertEquals(123, intValue);
        parser.close();
    }

    @Test
    public void testGetLongValue() throws IOException, JsonParseException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.valueToTree(new Long(12345));
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken();
        parser.nextToken();
        long longValue = parser.getLongValue();
        assertEquals(12345, longValue);
        parser.close();
    }

    @Test
    public void testGetNumberValue() throws IOException, JsonParseException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.valueToTree(new BigDecimal("123.45"));
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken();
        parser.nextToken();
        Number numberValue = parser.getNumberValue();
        assertEquals(new BigDecimal("123.45"), numberValue);
        parser.close();
    }

    @Test
    public void testGetEmbeddedObject() throws IOException, JsonParseException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.valueToTree(new Object());
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken();
        parser.nextToken();
        Object embeddedObject = parser.getEmbeddedObject();
        assertNotNull(embeddedObject);
        parser.close();
    }

    @Test
    public void testIsNaN() throws IOException, JsonParseException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.valueToTree(new Double.NaN());
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken();
        parser.nextToken();
        assertTrue(parser.isNaN());
        parser.close();
    }

    @Test
    public void testGetBinaryValue() throws IOException, JsonParseException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.valueToTree(new byte[0]);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken();
        parser.nextToken();
        byte[] binaryValue = parser.getBinaryValue(Base64Variant.STANDARD);
        assertArrayEquals(new byte[0], binaryValue);
        parser.close();
    }

    @Test
    public void testReadBinaryValue() throws IOException, JsonParseException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.valueToTree(new byte[0]);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken();
        parser.nextToken();
        byte[] expected = new byte[0];
        OutputStream outputStream = new ByteArrayOutputStream();
        int bytesRead = parser.readBinaryValue(Base64Variant.STANDARD, outputStream);
        byte[] actual = outputStream.toByteArray();
        assertEquals(bytesRead, expected.length);
        assertArrayEquals(expected, actual);
        parser.close();
    }
}