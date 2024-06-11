package com.fasterxml.jackson.databind.node;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonToken;

import static org.junit.Assert.*;

public class TreeTraversingParserTest {

    private JsonNode jsonNode;
    private TreeTraversingParser parser;

    @Before
    public void setUp() {
        jsonNode = new ObjectNode(JsonNodeFactory.instance);
        parser = new TreeTraversingParser(jsonNode);
    }

    @Test
    public void testNextToken() throws Exception {
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
    }

    @Test
    public void testSkipChildren() throws Exception {
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
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
        parser.nextToken();
        assertEquals(null, parser.getCurrentName());
        parser.nextToken();
        assertEquals("field", parser.getCurrentName());
    }

    @Test
    public void testOverrideCurrentName() throws Exception {
        parser.nextToken();
        parser.nextToken();
        parser.overrideCurrentName("newField");
        assertEquals("newField", parser.getCurrentName());
    }

    @Test
    public void testGetParsingContext() throws Exception {
        assertNotNull(parser.getParsingContext());
    }

    @Test
    public void testGetTokenLocation() throws Exception {
        assertNotNull(parser.getTokenLocation());
    }

    @Test
    public void testGetCurrentLocation() throws Exception {
        assertNotNull(parser.getCurrentLocation());
    }

    @Test
    public void testGetText() throws Exception {
        parser.nextToken();
        assertEquals(null, parser.getText());
        parser.nextToken();
        assertEquals("value", parser.getText());
    }

    @Test
    public void testHasTextCharacters() throws Exception {
        assertFalse(parser.hasTextCharacters());
    }

    @Test
    public void testGetNumberType() throws Exception {
        parser.nextToken();
        assertEquals(null, parser.getNumberType());
        parser.nextToken();
        assertEquals(JsonParser.NumberType.INT, parser.getNumberType());
    }

    @Test
    public void testGetBigIntegerValue() throws Exception {
        parser.nextToken();
        assertNull(parser.getBigIntegerValue());
    }

    @Test
    public void testGetDecimalValue() throws Exception {
        parser.nextToken();
        assertNull(parser.getDecimalValue());
    }

    @Test
    public void testGetDoubleValue() throws Exception {
        parser.nextToken();
        assertEquals(0.0, parser.getDoubleValue(), 0.01);
    }

    @Test
    public void testGetFloatValue() throws Exception {
        parser.nextToken();
        assertEquals(0.0, parser.getFloatValue(), 0.01);
    }

    @Test
    public void testGetIntValue() throws Exception {
        parser.nextToken();
        assertEquals(0, parser.getIntValue());
    }

    @Test
    public void testGetLongValue() throws Exception {
        parser.nextToken();
        assertEquals(0, parser.getLongValue());
    }

    @Test
    public void testGetNumberValue() throws Exception {
        parser.nextToken();
        assertNull(parser.getNumberValue());
    }

    @Test
    public void testGetEmbeddedObject() throws Exception {
        parser.nextToken();
        assertNull(parser.getEmbeddedObject());
    }

    @Test
    public void testIsNaN() throws Exception {
        parser.nextToken();
        assertFalse(parser.isNaN());
    }

    @Test
    public void testGetBinaryValue() throws Exception {
        parser.nextToken();
        assertNull(parser.getBinaryValue());
    }

    @Test
    public void testReadBinaryValue() throws Exception {
        parser.nextToken();
        assertEquals(0, parser.readBinaryValue(null, null));
    }

    @Test
    public void testHandleEOF() throws Exception {
        try {
            parser._handleEOF();
            fail("Expected JsonParseException");
        } catch (JsonParseException e) {
            assertEquals("Unexpected end-of-input", e.getMessage());
        }
    }
}