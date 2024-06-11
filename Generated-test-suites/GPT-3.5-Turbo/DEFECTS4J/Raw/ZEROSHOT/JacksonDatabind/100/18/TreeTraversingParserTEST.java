package com.fasterxml.jackson.databind.node;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.Assert.*;

public class TreeTraversingParserTest {

    private TreeTraversingParser parser;

    @Before
    public void setUp() {
        parser = new TreeTraversingParser(null);
    }

    @Test
    public void setCodec() {
        assertNull(parser.getCodec());
        parser.setCodec(null);
        assertNull(parser.getCodec());
    }

    @Test
    public void getCodec() {
        assertNull(parser.getCodec());
    }

    @Test
    public void version() {
        assertNotNull(parser.version());
    }

    @Test
    public void close() throws IOException {
        assertFalse(parser.isClosed());
        parser.close();
        assertTrue(parser.isClosed());
    }

    @Test
    public void nextToken() throws IOException {
        assertNull(parser.nextToken());
    }

    @Test
    public void skipChildren() throws IOException {
        assertNull(parser.skipChildren());
    }

    @Test
    public void isClosed() {
        assertFalse(parser.isClosed());
    }

    @Test
    public void getCurrentName() {
        assertNull(parser.getCurrentName());
    }

    @Test
    public void overrideCurrentName() {
        parser.overrideCurrentName(null);
    }

    @Test
    public void getParsingContext() {
        assertNull(parser.getParsingContext());
    }

    @Test
    public void getTokenLocation() {
        assertNotNull(parser.getTokenLocation());
    }

    @Test
    public void getCurrentLocation() {
        assertNotNull(parser.getCurrentLocation());
    }

    @Test
    public void getText() throws IOException {
        assertNull(parser.getText());
    }

    @Test
    public void getTextCharacters() throws IOException {
        assertNull(parser.getTextCharacters());
    }

    @Test
    public void getTextLength() throws IOException {
        assertEquals(0, parser.getTextLength());
    }

    @Test
    public void getTextOffset() throws IOException {
        assertEquals(0, parser.getTextOffset());
    }

    @Test
    public void hasTextCharacters() {
        assertFalse(parser.hasTextCharacters());
    }

    @Test
    public void getNumberType() throws IOException {
        assertNull(parser.getNumberType());
    }

    @Test
    public void getBigIntegerValue() throws IOException {
        assertNull(parser.getBigIntegerValue());
    }

    @Test
    public void getDecimalValue() throws IOException {
        assertNull(parser.getDecimalValue());
    }

    @Test
    public void getDoubleValue() throws IOException {
        assertEquals(0.0, parser.getDoubleValue(), 0);
    }

    @Test
    public void getFloatValue() throws IOException {
        assertEquals(0.0f, parser.getFloatValue(), 0);
    }

    @Test
    public void getLongValue() throws IOException {
        assertEquals(0L, parser.getLongValue());
    }

    @Test
    public void getIntValue() throws IOException {
        assertEquals(0, parser.getIntValue());
    }

    @Test
    public void getNumberValue() throws IOException {
        assertNull(parser.getNumberValue());
    }

    @Test
    public void getEmbeddedObject() {
        assertNull(parser.getEmbeddedObject());
    }

    @Test
    public void isNaN() {
        assertFalse(parser.isNaN());
    }

    @Test
    public void getBinaryValue() throws IOException {
        assertNull(parser.getBinaryValue(null));
    }

    @Test
    public void readBinaryValue() throws IOException {
        assertEquals(0, parser.readBinaryValue(null, null));
    }

    @Test
    public void _handleEOF() throws IOException {
        parser._handleEOF();
    }
}