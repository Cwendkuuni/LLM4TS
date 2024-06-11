package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.Assert.*;

public class TreeTraversingParserTest {

    private ObjectMapper objectMapper;
    private TreeTraversingParser parser;
    private JsonNode rootNode;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
        rootNode = objectMapper.createObjectNode();
        parser = new TreeTraversingParser(rootNode);
    }

    @Test
    public void testSetAndGetCodec() throws IOException {
        ObjectCodec codec = new ObjectCodec();
        parser.setCodec(codec);
        assertEquals(codec, parser.getCodec());
    }

    @Test
    public void testVersion() {
        Version version = PackageVersion.VERSION;
        assertEquals(version, parser.version());
    }

    @Test
    public void testClose() throws IOException {
        assertFalse(parser.isClosed());
        parser.close();
        assertTrue(parser.isClosed());
    }

    @Test
    public void testNextToken() throws IOException {
        assertEquals(null, parser.nextToken());
        assertFalse(parser.isClosed());
    }

    @Test
    public void testSkipChildren() throws IOException {
        parser.skipChildren();
        assertFalse(parser.isClosed());
    }

    @Test
    public void testIsClosed() throws IOException {
        assertFalse(parser.isClosed());
        parser.close();
        assertTrue(parser.isClosed());
    }

    @Test
    public void testGetCurrentName() throws IOException {
        assertNull(parser.getCurrentName());
    }

    @Test
    public void testOverrideCurrentName() throws IOException {
        parser.overrideCurrentName("test");
        assertNull(parser.getCurrentName());
    }

    @Test
    public void testGetParsingContext() {
        assertNotNull(parser.getParsingContext());
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
    public void testGetText() throws IOException {
        assertNull(parser.getText());
    }

    @Test
    public void testGetTextCharacters() throws IOException {
        assertNull(parser.getTextCharacters());
    }

    @Test
    public void testGetTextLength() throws IOException {
        assertEquals(0, parser.getTextLength());
    }

    @Test
    public void testGetTextOffset() throws IOException {
        assertEquals(0, parser.getTextOffset());
    }

    @Test
    public void testHasTextCharacters() {
        assertFalse(parser.hasTextCharacters());
    }

    @Test
    public void testGetNumberType() throws IOException {
        assertNull(parser.getNumberType());
    }

    @Test
    public void testGetBigIntegerValue() throws IOException {
        assertNull(parser.getBigIntegerValue());
    }

    @Test
    public void testGetDecimalValue() throws IOException {
        assertNull(parser.getDecimalValue());
    }

    @Test
    public void testGetDoubleValue() throws IOException {
        assertEquals(0.0, parser.getDoubleValue(), 0.0001);
    }

    @Test
    public void testGetFloatValue() throws IOException {
        assertEquals(0.0f, parser.getFloatValue(), 0.0001);
    }

    @Test
    public void testGetIntValue() throws IOException {
        assertEquals(0, parser.getIntValue());
    }

    @Test
    public void testGetLongValue() throws IOException {
        assertEquals(0L, parser.getLongValue());
    }

    @Test
    public void testGetNumberValue() throws IOException {
        assertNull(parser.getNumberValue());
    }

    @Test
    public void testGetEmbeddedObject() throws IOException {
        assertNull(parser.getEmbeddedObject());
    }

    @Test
    public void testIsNaN() throws IOException {
        assertFalse(parser.isNaN());
    }

    @Test
    public void testGetBinaryValue() throws IOException {
        assertNull(parser.getBinaryValue(Base64Variant.getDefaultVariant()));
    }

    @Test
    public void testReadBinaryValue() throws IOException {
        assertEquals(0, parser.readBinaryValue(Base64Variant.getDefaultVariant(), null));
    }

    @Test
    public void testHandleEOF() throws IOException {
        try {
            parser._handleEOF();
        } catch (JsonParseException e) {
            assertEquals("Unexpected end-of-input", e.getMessage());
            return;
        }
        fail("Expected JsonParseException to be thrown");
    }
}