package com.example.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Before;
import org.junit.Test;

public class TreeTraversingParserTest {

    private ObjectMapper objectMapper;
    private JsonNode rootNode;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        rootNode = objectMapper.valueToTree(new int[]{1, 2, 3});
    }

    @Test
    public void testTreeTraversingParser() {
        // Basic functionality test
        TreeTraversingParser parser = new TreeTraversingParser(rootNode);
        TreeTraversingParser expectedParser = new TreeTraversingParser(rootNode, null);
        assertEquals(expectedParser, parser);

        // Test nextToken()
        JsonToken nextToken = parser.nextToken();
        assertEquals(JsonToken.START_ARRAY, nextToken);

        nextToken = parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, nextToken);
        assertEquals(1, parser.getIntValue());

        nextToken = parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, nextToken);
        assertEquals(2, parser.getIntValue());

        nextToken = parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, nextToken);
        assertEquals(3, parser.getIntValue());

        nextToken = parser.nextToken();
        assertEquals(JsonToken.END_ARRAY, nextToken);

        // Test setCodec() and getCodec()
        ObjectCodec newCodec = new ObjectMapper().getFactory().getCodec();
        parser.setCodec(newCodec);
        assertEquals(newCodec, parser.getCodec());

        // Test nextValue() (Default works well, no need to test)

        // Test skipChildren()
        parser.skipChildren();
        nextToken = parser.nextToken();
        assertEquals(JsonToken.END_ARRAY, nextToken);

        // Test isClosed()
        parser.close();
        assertTrue(parser.isClosed());

        // Test getCurrentName()
        parser = new TreeTraversingParser(objectMapper.valueToTree("{\"name\": \"John\"}"));
        assertEquals("name", parser.getCurrentName());

        // Test overrideCurrentName()
        parser.overrideCurrentName("newName");
        assertEquals("newName", parser.getCurrentName());

        // Test getParsingContext()
        assertEquals(parser.getParsingContext(), parser.getParsingContext());

        // Test getTokenLocation() and getCurrentLocation()
        assertEquals(JsonLocation.NA, parser.getTokenLocation());
        assertEquals(JsonLocation.NA, parser.getCurrentLocation());

        // Test getText()
        assertEquals("1", parser.getText());
        assertEquals("1", parser.getText().substring(0, 1));
        assertEquals(1, parser.getTextLength());
        assertEquals(0, parser.getTextOffset());

        // Test getTextCharacters()
        char[] textCharacters = parser.getTextCharacters();
        assertEquals(1, textCharacters.length);
        assertEquals('1', textCharacters[0]);

        // Test getNumberType()
        assertEquals(JsonType.INTEGER, parser.getNumberType());

        // Test getBigIntegerValue()
        assertEquals(BigInteger.valueOf(1), parser.getBigIntegerValue());

        // Test getDecimalValue()
        assertEquals(new BigDecimal("1"), parser.getDecimalValue());

        // Test getDoubleValue()
        assertEquals(1.0, parser.getDoubleValue(), 0.0);

        // Test getFloatValue()
        assertEquals(1.0f, parser.getFloatValue(), 0.0f);

        // Test getLongValue()
        assertEquals(1, parser.getLongValue());

        // Test getIntValue()
        assertEquals(1, parser.getIntValue());

        // Test getNumberValue()
        assertEquals(1, parser.getNumberValue().intValue());

        // Test getEmbeddedObject()
        assertEquals(3, ((ArrayNode) parser.getEmbeddedObject()).size());

        // Test isNaN()
        parser = new TreeTraversingParser(objectMapper.valueToTree(new Double("NaN")));
        assertTrue(parser.isNaN());

        // Test getBinaryValue(Base64Variant)
        byte[] binaryData = parser.getBinaryValue(Base64Variants.DEFAULT);
        assertNotNull(binaryData);

        // Test readBinaryValue(Base64Variant, OutputStream)
        byte[] outputData = new byte[binaryData.length];
        parser.readBinaryValue(Base64Variants.DEFAULT, new ByteArrayOutputStream(outputData.length));
        assertArrayEquals(binaryData, outputData);

        // Test internal methods (not public)
        JsonNode internalCurrentNode = parser.currentNode();
        assertEquals(1, internalCurrentNode.asInt());

        JsonNode internalCurrentNumericNode = parser.currentNumericNode();
        assertEquals(1, internalCurrentNumericNode.asInt());
    }

    @Test(expected = JsonParseException.class)
    public void testInvalidNextToken() throws JsonParseException {
        TreeTraversingParser parser = new TreeTraversingParser(rootNode);
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken(); // This should throw an exception
    }
}