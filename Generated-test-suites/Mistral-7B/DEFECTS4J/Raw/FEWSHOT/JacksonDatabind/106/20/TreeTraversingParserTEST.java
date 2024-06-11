package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;

public class TreeTraversingParserTest {

    @Test
    public void testEmptyTree() throws Exception {
        JsonParser parser = new TreeTraversingParser(createEmptyJsonNode());
        assertEquals(JsonToken.END_DOCUMENT, parser.nextToken());
        parser.close();
    }

    @Test
    public void testSimpleValueNode() throws Exception {
        JsonParser parser = new TreeTraversingParser(createJsonNode("123"));
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(123, parser.getIntValue());
        parser.close();
    }

    @Test
    public void testObjectNode() throws Exception {
        JsonParser parser = new TreeTraversingParser(createJsonNode("{\"name\":\"John\",\"age\":30}"));
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        String name = parser.getCurrentName();
        assertEquals("name", name);
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("John", parser.getText());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        name = parser.getCurrentName();
        assertEquals("age", name);
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(30, parser.getIntValue());
        parser.close();
    }

    @Test
    public void testArrayNode() throws Exception {
        JsonParser parser = new TreeTraversingParser(createJsonNode("[1,2,3]"));
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(1, parser.getIntValue());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(2, parser.getIntValue());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(3, parser.getIntValue());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        parser.close();
    }

    @Test
    public void testSkippingChildren() throws Exception {
        JsonParser parser = new TreeTraversingParser(createJsonNode("{\"name\":{\"first\":\"John\",\"last\":\"Doe\"}}"));
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("name", parser.getCurrentName());
        parser.skipChildren();
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        parser.close();
    }

    @Test(expected = JsonParseException.class)
    public void testClosedParser() throws Exception {
        JsonParser parser = new TreeTraversingParser(createJsonNode("{}"));
        parser.close();
        parser.nextToken();
    }

    @Test(expected = JsonParseException.class)
    public void testParsingBeyondEnd() throws Exception {
        JsonParser parser = new TreeTraversingParser(createEmptyJsonNode());
        parser.nextToken();
        parser.nextToken();
    }

    @Test
    public void testGetText() throws Exception {
        JsonParser parser = new TreeTraversingParser(createJsonNode("{\"name\":\"John\",\"age\":30}"));
        assertEquals("John", parser.getText());
        parser.nextToken();
        assertEquals("30", parser.getText());
        parser.close();
    }

    @Test
    public void testGetTextLength() throws Exception {
        JsonParser parser = new TreeTraversingParser(createJsonNode("{\"name\":\"John\",\"age\":30}"));
        assertEquals(4, parser.getTextLength());
        parser.nextToken();
        assertEquals(3, parser.getTextLength());
        parser.close();
    }

    @Test
    public void testGetTextCharacters() throws Exception {
        JsonParser parser = new TreeTraversingParser(createJsonNode("{\"name\":\"John\",\"age\":30}"));
        char[] text = parser.getTextCharacters();
        assertEquals('J', text[0]);
        assertEquals('o', text[1]);
        assertEquals('h', text[2]);
        assertEquals('n', text[3]);
        parser.nextToken();
        char[] text2 = parser.getTextCharacters();
        assertEquals('3', text2[0]);
        assertEquals('0', text2[1]);
        parser.close();
    }

    @Test
    public void testGetCurrentName() throws Exception {
        JsonParser parser = new TreeTraversingParser(createJsonNode("{\"name\":\"John\",\"age\":30}"));
        assertEquals("name", parser.getCurrentName());
        parser.nextToken();
        assertEquals("age", parser.getCurrentName());
        parser.close();
    }

    @Test
    public void testOverrideCurrentName() throws Exception {
        JsonParser parser = new TreeTraversingParser(createJsonNode("{\"name\":\"John\",\"age\":30}"));
        parser.overrideCurrentName("newName");
        assertEquals("newName", parser.getCurrentName());
        parser.nextToken();
        assertEquals("age", parser.getCurrentName());
        parser.close();
    }

    @Test
    public void testGetParsingContext() throws Exception {
        JsonParser parser = new TreeTraversingParser(createJsonNode("{\"name\":\"John\",\"age\":30}"));
        assertEquals(parser, parser.getParsingContext());
        parser.close();
    }

    @Test
    public void testGetTokenLocation() throws Exception {
        JsonParser parser = new TreeTraversingParser(createJsonNode("{\"name\":\"John\",\"age\":30}"));
        assertEquals(JsonLocation.NA, parser.getTokenLocation());
        parser.close();
    }

    @Test
    public void testGetCurrentLocation() throws Exception {
        JsonParser parser = new TreeTraversingParser(createJsonNode("{\"name\":\"John\",\"age\":30}"));
        assertEquals(JsonLocation.NA, parser.getCurrentLocation());
        parser.nextToken();
        assertEquals(JsonLocation.NA, parser.getCurrentLocation());
        parser.close();
    }

    @Test
    public void testGetNumberType() throws Exception {
        JsonParser parser = new TreeTraversingParser(createJsonNode("123.45"));
        assertEquals(NumberType.NUMBER, parser.getNumberType());
        parser.close();
    }

    @Test
    public void testGetBigIntegerValue() throws Exception {
        JsonParser parser = new TreeTraversingParser(createJsonNode("12345678901234567890"));
        assertEquals(new BigInteger("12345678901234567890"), parser.getBigIntegerValue());
        parser.close();
    }

    @Test
    public void testGetDecimalValue() throws Exception {
        JsonParser parser = new TreeTraversingParser(createJsonNode("123.45"));
        assertEquals(new BigDecimal("123.45"), parser.getDecimalValue());
        parser.close();
    }

    @Test
    public void testGetDoubleValue() throws Exception {
        JsonParser parser = new TreeTraversingParser(createJsonNode("123.45"));
        assertEquals(123.45, parser.getDoubleValue(), 0.001);
        parser.close();
    }

    @Test
    public void testGetFloatValue() throws Exception {
        JsonParser parser = new TreeTraversingParser(createJsonNode("123.45"));
        assertEquals((float) 123.45, parser.getFloatValue(), 0.001);
        parser.close();
    }

    @Test
    public void testGetIntValue() throws Exception {
        JsonParser parser = new TreeTraversingParser(createJsonNode("123"));
        assertEquals(123, parser.getIntValue());
        parser.close();
    }

    @Test
    public void testGetLongValue() throws Exception {
        JsonParser parser = new TreeTraversingParser(createJsonNode("12345678901234567890"));
        assertEquals(12345678901234567890L, parser.getLongValue());
        parser.close();
    }

    @Test
    public void testGetNumberValue() throws Exception {
        JsonParser parser = new TreeTraversingParser(createJsonNode("123.45"));
        assertEquals(new BigDecimal("123.45"), parser.getNumberValue());
        parser.close();
    }

    @Test
    public void testGetEmbeddedObject() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = new TreeTraversingParser(mapper.readTree("{\"name\":{\"first\":\"John\",\"last\":\"Doe\"}}"));
        JsonNode result = parser.getEmbeddedObject();
        assertEquals(mapper.readTree("{\"first\":\"John\",\"last\":\"Doe\"}"), result);
        parser.close();
    }

    @Test
    public void testIsNaN() throws Exception {
        JsonParser parser = new TreeTraversingParser(createJsonNode("NaN"));
        assertTrue(parser.isNaN());
        parser.close();
    }

    @Test
    public void testGetBinaryValue() throws Exception {
        JsonParser parser = new TreeTraversingParser(createJsonNode("{\"binary\":\"RnJvbSBzbyBmZWFyIHdvcmxkIQ==\"}"));
        byte[] result = parser.getBinaryValue(Base64Variant.STANDARD);
        assertEquals("JavaRocks!".getBytes(), result);
        parser.close();
    }

    @Test
    public void testReadBinaryValue() throws Exception {
        JsonParser parser = new TreeTraversingParser(createJsonNode("{\"binary\":\"RnJvbSBzbyBmZWFyIHdvcmxkIQ==\"}"));
        byte[] input = "JavaRocks!".getBytes();
        int length = parser.readBinaryValue(Base64Variant.STANDARD, new ByteArrayOutputStream());
        assertEquals(input.length, length);
        parser.close();
    }

    private JsonNode createJsonNode(String jsonText) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(jsonText);
    }

    private JsonNode createEmptyJsonNode() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree("[]");
    }
}