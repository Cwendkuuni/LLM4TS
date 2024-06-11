package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Before;
import org.junit.Test;

public class TreeTraversingParserTest {
    private static final JsonNodeFactory FACTORY = JsonNodeFactory.instance;

    private TreeTraversingParser parser;

    @Before
    public void setup() {
        JsonNode node = FACTORY.objectNode();
        parser = new TreeTraversingParser(node);
    }

    @Test
    public void testConstructor() {
        JsonNode node = FACTORY.objectNode();

        parser = new TreeTraversingParser(node);
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());

        parser = new TreeTraversingParser(node, null);
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());

        ObjectCodec codec = new ObjectMapper().writer().getCodec();
        parser = new TreeTraversingParser(node, codec);
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
    }

    @Test
    public void testSetCodec() {
        ObjectCodec codec = new ObjectMapper().writer().getCodec();
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
        parser.skipChildren();
    }

    @Test
    public void testIsClosed() {
        assertFalse(parser.isClosed());
    }

    @Test
    public void testGetCurrentName() {
        assertNull(parser.getCurrentName());
    }

    @Test
    public void testOverrideCurrentName() throws Exception {
        parser.overrideCurrentName("name");
        assertEquals("name", parser.getCurrentName());
    }

    @Test
    public void testGetParsingContext() {
        assertNull(parser.getParsingContext());
    }

    @Test
    public void testGetTokenLocation() {
        assertEquals(JsonLocation.NA, parser.getTokenLocation());
    }

    @Test
    public void testGetCurrentLocation() {
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
        assertEquals(0.0f, parser.getFloatValue(), 0.0f);
    }

    @Test
    public void testGetLongValue() throws Exception {
        assertEquals(0L, parser.getLongValue());
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
        assertNull(parser.getBinaryValue(Base64Variants.MIME));
    }

    @Test
    public void testReadBinaryValue() throws Exception {
        assertEquals(0, parser.readBinaryValue(Base64Variants.MIME, null));
    }

    @Test
    public void testHandleEOF() throws Exception {
        try {
            parser._handleEOF();
            fail("Expected JsonParseException");
        } catch (JsonParseException e) {
            // expected
        }
    }
}