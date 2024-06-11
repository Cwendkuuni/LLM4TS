package com.fasterxml.jackson.databind.node;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TreeTraversingParserTest {

    private TreeTraversingParser parser;

    @Before
    public void setup() {
        JsonNode jsonNode = // create a JSON node for testing
        parser = new TreeTraversingParser(jsonNode);
    }

    @Test
    public void testNextToken() throws IOException {
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertNull(parser.nextToken());
    }

    @Test
    public void testSkipChildren() throws IOException {
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertTrue(parser.skipChildren() instanceof TreeTraversingParser);
        assertEquals(JsonToken.END_OBJECT, parser.getCurrentToken());
    }

    @Test
    public void testIsClosed() throws IOException {
        parser.close();
        assertTrue(parser.isClosed());
    }

    @Test
    public void testGetCurrentName() throws IOException {
        assertEquals("field", parser.getCurrentName());
        parser.nextToken();
        assertEquals("123", parser.getCurrentName());
    }

    @Test
    public void testText() throws IOException {
        assertEquals("field", parser.getText());
        parser.nextToken();
        assertEquals("123", parser.getText());
    }

    @Test
    public void testNumberType() throws IOException {
        assertEquals(NumberType.INT, parser.getNumberType());
        parser.nextToken();
        assertEquals(NumberType.INT, parser.getNumberType());
    }

    @Test
    public void testBigIntegerValue() throws IOException {
        assertEquals(new BigInteger("123"), parser.getBigIntegerValue());
        parser.nextToken();
        assertNull(parser.getBigIntegerValue());
    }

    @Test
    public void testDecimalValue() throws IOException {
        assertEquals(new BigDecimal("123"), parser.getDecimalValue());
        parser.nextToken();
        assertNull(parser.getDecimalValue());
    }

    @Test
    public void testDoubleValue() throws IOException {
        assertEquals(123.0, parser.getDoubleValue(), 0);
        parser.nextToken();
        assertEquals(0.0, parser.getDoubleValue(), 0);
    }

    @Test
    public void testFloatValue() throws IOException {
        assertEquals(123.0f, parser.getFloatValue(), 0);
        parser.nextToken();
        assertEquals(0.0f, parser.getFloatValue(), 0);
    }

    @Test
    public void testLongValue() throws IOException {
        assertEquals(123L, parser.getLongValue());
        parser.nextToken();
        assertEquals(0L, parser.getLongValue());
    }

    @Test
    public void testIntValue() throws IOException {
        assertEquals(123, parser.getIntValue());
        parser.nextToken();
        assertEquals(0, parser.getIntValue());
    }

    @Test
    public void testNumberValue() throws IOException {
        assertEquals(123, parser.getNumberValue());
        parser.nextToken();
        assertNull(parser.getNumberValue());
    }

    @Test
    public void testGetEmbeddedObject() throws IOException {
        assertNull(parser.getEmbeddedObject());
        parser.nextToken();
        assertNull(parser.getEmbeddedObject());
    }

    @Test
    public void testIsNaN() throws IOException {
        assertFalse(parser.isNaN());
        parser.nextToken();
        assertTrue(parser.isNaN());
    }

    @Test
    public void testGetBinaryValue() throws IOException {
        assertNull(parser.getBinaryValue(null));
        parser.nextToken();
        assertNull(parser.getBinaryValue(null));
    }

    @Test
    public void testReadBinaryValue() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        assertEquals(0, parser.readBinaryValue(null, outputStream));
        parser.nextToken();
        assertEquals(0, parser.readBinaryValue(null, outputStream));
    }

}