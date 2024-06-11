package com.fasterxml.jackson.databind.node;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Base64;

import static org.junit.Assert.*;

public class TreeTraversingParserTest {

    private TreeTraversingParser parser;

    @Before
    public void setup() {
        JsonNode jsonNode = TestUtils.createJsonTree();
        parser = new TreeTraversingParser(jsonNode);
    }

    @Test
    public void testNextToken() throws Exception {
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(JsonToken.VALUE_NUMBER_FLOAT, parser.nextToken());
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, parser.nextToken());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertNull(parser.nextToken());
    }

    @Test
    public void testSkipChildren() throws Exception {
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        parser.skipChildren();
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        parser.skipChildren();
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        parser.skipChildren();
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertNull(parser.nextToken());
    }

    @Test
    public void testIsClosed() throws Exception {
        assertFalse(parser.isClosed());
        parser.close();
        assertTrue(parser.isClosed());
    }

    @Test
    public void testGetCurrentName() throws Exception {
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals("name", parser.getCurrentName());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("age", parser.getCurrentName());
        parser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertNull(parser.getCurrentName());
    }

    @Test
    public void testOverrideCurrentName() throws Exception {
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals("name", parser.getCurrentName());
        parser.overrideCurrentName("fullName");
        assertEquals("fullName", parser.getCurrentName());
    }

    @Test
    public void testGetParsingContext() throws Exception {
        assertEquals(JsonStreamContext.Type.OBJECT, parser.getParsingContext().getType());
        assertEquals(0, parser.getParsingContext().getCurrentIndex());
        assertEquals("name", parser.getParsingContext().getCurrentName());
        assertEquals(JsonStreamContext.Type.OBJECT, parser.getParsingContext().getParent().getType());
        assertEquals(0, parser.getParsingContext().getParent().getCurrentIndex());
        assertNull(parser.getParsingContext().getParent().getCurrentName());
    }

    @Test
    public void testGetTokenLocation() throws Exception {
        assertNull(parser.getTokenLocation());
    }

    @Test
    public void testGetCurrentLocation() throws Exception {
        assertNull(parser.getCurrentLocation());
    }

    @Test
    public void testGetText() throws Exception {
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals("", parser.getText());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("name", parser.getText());
        parser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("John Doe", parser.getText());
    }

    @Test
    public void testGetNumberType() throws Exception {
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(JsonParser.NumberType.INT, parser.getNumberType());
        parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_FLOAT, parser.nextToken());
        assertEquals(JsonParser.NumberType.DOUBLE, parser.getNumberType());
    }

    @Test
    public void testGetBigIntegerValue() throws Exception {
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(new BigInteger("10"), parser.getBigIntegerValue());
    }

    @Test
    public void testGetDecimalValue() throws Exception {
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_FLOAT, parser.nextToken());
        assertEquals(new BigDecimal("3.14"), parser.getDecimalValue());
    }

    @Test
    public void testGetDoubleValue() throws Exception {
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_FLOAT, parser.nextToken());
        assertEquals(3.14, parser.getDoubleValue(), 0.001);
    }

    @Test
    public void testGetFloatValue() throws Exception {
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_FLOAT, parser.nextToken());
        assertEquals(3.14, parser.getFloatValue(), 0.001);
    }

    @Test
    public void testGetIntValue() throws Exception {
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(10, parser.getIntValue());
    }

    @Test
    public void testGetLongValue() throws Exception {
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(10L, parser.getLongValue());
    }

    @Test
    public void testGetNumberValue() throws Exception {
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(new BigInteger("10"), parser.getNumberValue());
    }

    @Test
    public void testGetEmbeddedObject() throws Exception {
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        parser.nextToken();
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, parser.nextToken());
        assertEquals("embedded", parser.getEmbeddedObject());
    }

    @Test
    public void testIsNaN() throws Exception {
        assertFalse(parser.isNaN());
        parser.nextToken();
        assertTrue(parser.isNaN());
    }

    @Test
    public void testGetBinaryValue() throws Exception {
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        byte[] expected = Base64.getDecoder().decode("SGVsbG8=");
        assertArrayEquals(expected, parser.getBinaryValue(null));
    }

    @Test
    public void testReadBinaryValue() throws Exception {
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        byte[] expected = Base64.getDecoder().decode("SGVsbG8=");
        byte[] buffer = new byte[expected.length];
        int length = parser.readBinaryValue(null, buffer, 0, buffer.length);
        assertEquals(expected.length, length);
        assertArrayEquals(expected, buffer);
    }
}