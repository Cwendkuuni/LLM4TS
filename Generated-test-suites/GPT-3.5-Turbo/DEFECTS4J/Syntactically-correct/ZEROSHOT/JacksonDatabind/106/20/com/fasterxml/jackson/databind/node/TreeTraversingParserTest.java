package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Before;
import org.junit.Test;

public class TreeTraversingParserTest {

    private TreeTraversingParser parser;
    private ObjectCodec codec;

    @Before
    public void setUp() {
        JsonNode node = new ObjectNode(null, null);
        parser = new TreeTraversingParser(node, codec);
    }

    @Test
    public void testSetCodec() {
        ObjectCodec c = new ObjectMapper().codec();
        parser.setCodec(c);
        assertEquals(c, parser.getCodec());
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
    public void testNextToken() throws IOException, JsonParseException {
        assertNull(parser.nextToken());
    }

    @Test
    public void testSkipChildren() throws IOException, JsonParseException {
        assertNull(parser.skipChildren());
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
    public void testOverrideCurrentName() {
        String name = "test";
        parser.overrideCurrentName(name);
        assertEquals(name, parser.getCurrentName());
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
    public void testGetText() {
        assertNull(parser.getText());
    }

    @Test
    public void testGetTextCharacters() throws IOException, JsonParseException {
        assertArrayEquals(null, parser.getTextCharacters());
    }

    @Test
    public void testGetTextLength() throws IOException, JsonParseException {
        assertEquals(0, parser.getTextLength());
    }

    @Test
    public void testGetTextOffset() throws IOException, JsonParseException {
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
        assertEquals(0.0, parser.getDoubleValue(), 0.0);
    }

    @Test
    public void testGetFloatValue() throws IOException {
        assertEquals(0.0f, parser.getFloatValue(), 0.0f);
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
    public void testGetEmbeddedObject() {
        assertNull(parser.getEmbeddedObject());
    }

    @Test
    public void testIsNaN() {
        assertFalse(parser.isNaN());
    }

    @Test
    public void testGetBinaryValue() throws IOException, JsonParseException {
        assertNull(parser.getBinaryValue(Base64Variant.getDefaultVariant()));
    }

    @Test
    public void testReadBinaryValue() throws IOException, JsonParseException {
        assertEquals(0, parser.readBinaryValue(Base64Variant.getDefaultVariant(), null));
    }
}