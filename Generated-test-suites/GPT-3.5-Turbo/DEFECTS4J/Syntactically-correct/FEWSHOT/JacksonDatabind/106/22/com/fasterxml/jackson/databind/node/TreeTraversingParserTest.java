package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

public class TreeTraversingParserTest {

    @Test
    public void testSetCodec() {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        ObjectCodec codec = new ObjectCodec();
        parser.setCodec(codec);
        assertEquals(codec, parser.getCodec());
    }

    @Test
    public void testVersion() {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        Version version = new Version(1, 2, 3, null, null, null);
        assertEquals(version, parser.version());
    }

    @Test
    public void testClose() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertFalse(parser.isClosed());
        parser.close();
        assertTrue(parser.isClosed());
    }

    @Test
    public void testNextToken() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertNull(parser.nextToken());
    }

    @Test
    public void testSkipChildren() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertNull(parser.skipChildren());
    }

    @Test
    public void testIsClosed() {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertFalse(parser.isClosed());
        parser.close();
        assertTrue(parser.isClosed());
    }

    @Test
    public void testGetCurrentName() {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertNull(parser.getCurrentName());
    }

    @Test
    public void testOverrideCurrentName() {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        parser.overrideCurrentName("name");
        assertEquals("name", parser.getCurrentName());
    }

    @Test
    public void testGetParsingContext() {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertNull(parser.getParsingContext());
    }

    @Test
    public void testGetTokenLocation() {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertEquals(JsonLocation.NA, parser.getTokenLocation());
    }

    @Test
    public void testGetCurrentLocation() {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertEquals(JsonLocation.NA, parser.getCurrentLocation());
    }

    @Test
    public void testGetIntValue() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertEquals(0, parser.getIntValue());
    }

    @Test
    public void testGetLongValue() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertEquals(0L, parser.getLongValue());
    }

    @Test
    public void testGetNumberValue() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertNull(parser.getNumberValue());
    }

    @Test
    public void testGetEmbeddedObject() {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertNull(parser.getEmbeddedObject());
    }

    @Test
    public void testIsNaN() {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertFalse(parser.isNaN());
    }

    @Test
    public void testGetBinaryValue() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertNull(parser.getBinaryValue(Base64Variant.getDefaultVariant()));
    }

    @Test
    public void testReadBinaryValue() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertEquals(0, parser.readBinaryValue(Base64Variant.getDefaultVariant(), null));
    }
}