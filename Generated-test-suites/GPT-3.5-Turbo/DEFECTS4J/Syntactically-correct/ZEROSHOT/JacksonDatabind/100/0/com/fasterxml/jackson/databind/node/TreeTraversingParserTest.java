
package com.fasterxml.jackson.databind.node;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TreeTraversingParserTest {
    private TreeTraversingParser parser;

    @Before
    public void setUp() {
        JsonNode node = new ObjectNode(JsonNodeFactory.instance);
        parser = new TreeTraversingParser(node);
    }

    @Test
    public void testSetCodec() {
        ObjectCodec codec = new ObjectMapper().getCodec();
        parser.setCodec(codec);
        assertEquals(codec, parser.getCodec());
    }

    @Test
    public void testVersion() {
        assertEquals(com.fasterxml.jackson.databind.cfg.PackageVersion.VERSION, parser.version());
    }

    @Test
    public void testClose() throws Exception {
        assertFalse(parser.isClosed());
        parser.close();
        assertTrue(parser.isClosed());
    }

    @Test
    public void testNextToken() throws Exception {
        assertNull(parser.nextToken());
    }

    @Test
    public void testSkipChildren() throws Exception {
        assertSame(parser, parser.skipChildren());
    }

    @Test
    public void testIsClosed() throws Exception {
        assertFalse(parser.isClosed());
        parser.close();
        assertTrue(parser.isClosed());
    }

    @Test
    public void testGetCurrentName() throws Exception {
        assertNull(parser.getCurrentName());
    }

    @Test
    public void testOverrideCurrentName() throws Exception {
        parser.overrideCurrentName("name");
        assertNull(parser.getCurrentName());
    }

    @Test
    public void testGetParsingContext() throws Exception {
        assertNull(parser.getParsingContext());
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
        assertNull(parser.getText());
    }

    @Test
    public void testGetTextCharacters() throws Exception {
        assertNull(parser.getTextCharacters());
    }

    @Test
    public void testGetTextLength() throws Exception {
        assertEquals(0, parser.getTextLength());
    }

    @Test
    public void testGetTextOffset() throws Exception {
        assertEquals(0, parser.getTextOffset());
    }

    @Test
    public void testHasTextCharacters() throws Exception {
        assertFalse(parser.hasTextCharacters());
    }

    @Test
    public void testGetNumberType() throws Exception {
        assertNull(parser.getNumberType());
    }

    @Test
    public void testGetBigIntegerValue() throws Exception {
        assertNull(parser.getBigIntegerValue());
    }

    @Test
    public void testGetDecimalValue() throws Exception {
        assertNull(parser.getDecimalValue());
    }

    @Test
    public void testGetDoubleValue() throws Exception {
        assertEquals(0.0, parser.getDoubleValue(), 0.0);
    }

    @Test
    public void testGetFloatValue() throws Exception {
        assertEquals(0.0, parser.getFloatValue(), 0.0);
    }

    @Test
    public void testGetLongValue() throws Exception {
        assertEquals(0, parser.getLongValue());
    }

    @Test
    public void testGetIntValue() throws Exception {
        assertEquals(0, parser.getIntValue());
    }

    @Test
    public void testGetNumberValue() throws Exception {
        assertNull(parser.getNumberValue());
    }

    @Test
    public void testGetEmbeddedObject() throws Exception {
        assertNull(parser.getEmbeddedObject());
    }

    @Test
    public void testIsNaN() throws Exception {
        assertFalse(parser.isNaN());
    }

    @Test
    public void testGetBinaryValue() throws Exception {
        assertNull(parser.getBinaryValue(Base64Variants.MIME_NO_LINEFEEDS));
    }

    @Test
    public void testReadBinaryValue() throws Exception {
        assertEquals(0, parser.readBinaryValue(Base64Variants.MIME_NO_LINEFEEDS, null));
    }
}
