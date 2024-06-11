package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.ParserMinimalBase;
import org.junit.Test;

public class TreeTraversingParserTest {
    @Test
    public void testSetCodec() {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        parser.setCodec(null);
        assertNull(parser.getCodec());
    }

    @Test
    public void testVersion() {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertEquals(com.fasterxml.jackson.databind.cfg.PackageVersion.VERSION, parser.version());
    }

    @Test
    public void testClose() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        parser.close();
        assertTrue(parser.isClosed());
    }

    @Test
    public void testNextToken() throws IOException, JsonParseException {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertNull(parser.nextToken());
    }

    @Test
    public void testSkipChildren() throws IOException, JsonParseException {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertNull(parser.skipChildren());
    }
    
    @Test
    public void testIsClosed() {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertFalse(parser.isClosed());
    }

    @Test
    public void testGetCurrentName() {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertNull(parser.getCurrentName());
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
    public void testGetText() {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertNull(parser.getText());
    }

    @Test
    public void testGetTextCharacters() throws IOException, JsonParseException {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertNull(parser.getTextCharacters());
    }

    @Test
    public void testGetTextLength() throws IOException, JsonParseException {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertEquals(0, parser.getTextLength());
    }

    @Test
    public void testGetTextOffset() throws IOException, JsonParseException {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertEquals(0, parser.getTextOffset());
    }

    @Test
    public void testHasTextCharacters() {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertFalse(parser.hasTextCharacters());
    }

    @Test
    public void testGetNumberType() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertNull(parser.getNumberType());
    }

    @Test
    public void testGetBigIntegerValue() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertNull(parser.getBigIntegerValue());
    }

    @Test
    public void testGetDecimalValue() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertNull(parser.getDecimalValue());
    }

    @Test
    public void testGetDoubleValue() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertEquals(0.0, parser.getDoubleValue(), 0.0);
    }

    @Test
    public void testGetFloatValue() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertEquals(0.0f, parser.getFloatValue(), 0.0f);
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
    public void testGetBinaryValue() throws IOException, JsonParseException {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertNull(parser.getBinaryValue(null));
    }

    @Test
    public void testReadBinaryValue() throws IOException, JsonParseException {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertEquals(0, parser.readBinaryValue(null, null));
    }
}