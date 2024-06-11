package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;

public class TreeTraversingParserTest {
    
    private TreeTraversingParser parser;
    
    @Before
    public void setUp() {
        JsonNode node = new JsonNode();
        parser = new TreeTraversingParser(node);
    }
    
    @Test
    public void testNextToken() throws Exception {
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertEquals(null, parser.nextToken());
    }
    
    @Test
    public void testSkipChildren() throws Exception {
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());

        parser.skipChildren();

        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
    }
    
    @Test
    public void testIsClosed() throws Exception {
        assertFalse(parser.isClosed());
        parser.close();
        assertTrue(parser.isClosed());
    }
    
    @Test
    public void testGetCurrentName() throws Exception {
        assertEquals(null, parser.getCurrentName());
        parser.nextToken();
        assertEquals("field1", parser.getCurrentName());
    }
    
    @Test
    public void testOverrideCurrentName() throws Exception {
        assertEquals(null, parser.getCurrentName());
        parser.nextToken();
        assertEquals("field1", parser.getCurrentName());
        parser.overrideCurrentName("newName");
        assertEquals("newName", parser.getCurrentName());
    }
    
    @Test
    public void testGetParsingContext() throws Exception {
        assertNotNull(parser.getParsingContext());
    }
    
    @Test
    public void testGetTokenLocation() throws Exception {
        assertEquals(JsonLocation.NA, parser.getTokenLocation());
    }
    
    @Test
    public void testGetCurrentLocation() throws Exception {
        assertEquals(JsonLocation.NA, parser.getCurrentLocation());
    }
    
    @Test
    public void testGetText() throws Exception {
        assertEquals(null, parser.getText());
        parser.nextToken();
        assertEquals("value1", parser.getText());
        parser.nextToken();
        assertEquals("123", parser.getText());
        parser.nextToken();
        assertEquals(null, parser.getText());
    }
    
    @Test
    public void testGetTextCharacters() throws Exception {
        assertArrayEquals(null, parser.getTextCharacters());
        parser.nextToken();
        assertArrayEquals("value1".toCharArray(), parser.getTextCharacters());
    }
    
    @Test
    public void testGetTextLength() throws Exception {
        assertEquals(-1, parser.getTextLength());
        parser.nextToken();
        assertEquals(6, parser.getTextLength());
    }
    
    @Test
    public void testGetTextOffset() throws Exception {
        assertEquals(-1, parser.getTextOffset());
        parser.nextToken();
        assertEquals(0, parser.getTextOffset());
    }
    
    @Test
    public void testHasTextCharacters() throws Exception {
        assertFalse(parser.hasTextCharacters());
    }
    
    @Test
    public void testGetNumberType() throws Exception {
        assertEquals(null, parser.getNumberType());
        parser.nextToken();
        assertEquals(NumberType.INTEGER, parser.getNumberType());
    }
    
    @Test
    public void testGetBigIntegerValue() throws Exception {
        assertNull(parser.getBigIntegerValue());
        parser.nextToken();
        assertEquals(new BigInteger("123"), parser.getBigIntegerValue());
    }
    
    @Test
    public void testGetDecimalValue() throws Exception {
        assertNull(parser.getDecimalValue());
        parser.nextToken();
        assertEquals(new BigDecimal("123"), parser.getDecimalValue());
    }
    
    @Test
    public void testGetDoubleValue() throws Exception {
        assertEquals(0.0, parser.getDoubleValue(), 0.001);
        parser.nextToken();
        assertEquals(123.0, parser.getDoubleValue(), 0.001);
    }
    
    @Test
    public void testGetFloatValue() throws Exception {
        assertEquals(0.0, parser.getFloatValue(), 0.001);
        parser.nextToken();
        assertEquals(123.0, parser.getFloatValue(), 0.001);
    }
    
    @Test
    public void testGetIntValue() throws Exception {
        assertEquals(0, parser.getIntValue());
        parser.nextToken();
        assertEquals(123, parser.getIntValue());
    }
    
    @Test
    public void testGetLongValue() throws Exception {
        assertEquals(0, parser.getLongValue());
        parser.nextToken();
        assertEquals(123, parser.getLongValue());
    }
    
    @Test
    public void testGetNumberValue() throws Exception {
        assertNull(parser.getNumberValue());
        parser.nextToken();
        assertEquals(new BigInteger("123"), parser.getNumberValue());
    }
    
    @Test
    public void testGetEmbeddedObject() throws Exception {
        assertNull(parser.getEmbeddedObject());
        parser.nextToken();
        assertEquals("value1", parser.getEmbeddedObject());
    }
    
    @Test
    public void testIsNaN() throws Exception {
        assertFalse(parser.isNaN());
        parser.nextToken();
        assertFalse(parser.isNaN());
    }
    
    @Test
    public void testGetBinaryValue() throws Exception {
        assertNull(parser.getBinaryValue(null));
        parser.nextToken();
        assertNull(parser.getBinaryValue(null));
    }
    
    @Test
    public void testReadBinaryValue() throws Exception {
        assertEquals(0, parser.readBinaryValue(null, null));
        parser.nextToken();
        assertEquals(0, parser.readBinaryValue(null, null));
    }
    
}