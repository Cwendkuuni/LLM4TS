package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonStreamContext;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectCodec;
import com.fasterxml.jackson.databind.cfg.PackageVersion;

public class TreeTraversingParserTest {

    private TreeTraversingParser parser;

    @Before
    public void setUp() {
        JsonNode jsonNode = new JsonNode();
        parser = new TreeTraversingParser(jsonNode);
    }

    @After
    public void tearDown() {
        try {
            parser.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSetCodec() {
        ObjectCodec codec = new ObjectCodec();
        parser.setCodec(codec);
        assertEquals(codec, parser.getCodec());
    }

    @Test
    public void testVersion() {
        assertEquals(PackageVersion.VERSION, parser.version());
    }

    @Test
    public void testClose() {
        assertFalse(parser.isClosed());
        try {
            parser.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertTrue(parser.isClosed());
    }

    @Test
    public void testNextToken() throws IOException, JsonParseException {
        assertNull(parser.nextToken());
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
    }

    @Test
    public void testSkipChildren() throws IOException, JsonParseException {
        assertEquals(parser, parser.skipChildren());
        assertEquals(JsonToken.END_OBJECT, parser.getCurrentToken());
    }

    @Test
    public void testIsClosed() throws IOException, JsonParseException {
        assertFalse(parser.isClosed());
        parser.close();
        assertTrue(parser.isClosed());
    }

    @Test
    public void testGetCurrentName() {
        assertNull(parser.getCurrentName());
    }

    @Test
    public void testOverrideCurrentName() {
        String name = "name";
        parser.overrideCurrentName(name);
        assertEquals(name, parser.getCurrentName());
    }

    @Test
    public void testGetParsingContext() {
        assertNull(parser.getParsingContext());
    }

    @Test
    public void testGetTokenLocation() {
        assertNull(parser.getTokenLocation());
    }

    @Test
    public void testGetCurrentLocation() {
        assertNull(parser.getCurrentLocation());
    }

    @Test
    public void testGetText() {
        assertNull(parser.getText());
    }

    @Test
    public void testGetTextCharacters() throws IOException, JsonParseException {
        assertNull(parser.getTextCharacters());
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
    public void testGetNumberType() throws IOException, JsonParseException {
        assertNull(parser.getNumberType());
    }

    @Test
    public void testGetBigIntegerValue() throws IOException, JsonParseException {
        assertNull(parser.getBigIntegerValue());
    }

    @Test
    public void testGetDecimalValue() throws IOException, JsonParseException {
        assertNull(parser.getDecimalValue());
    }

    @Test
    public void testGetDoubleValue() throws IOException, JsonParseException {
        assertEquals(0.0, parser.getDoubleValue(), 0.0);
    }

    @Test
    public void testGetFloatValue() throws IOException, JsonParseException {
        assertEquals(0.0, parser.getFloatValue(), 0.0);
    }

    @Test
    public void testGetLongValue() throws IOException, JsonParseException {
        assertEquals(0, parser.getLongValue());
    }

    @Test
    public void testGetIntValue() throws IOException, JsonParseException {
        assertEquals(0, parser.getIntValue());
    }

    @Test
    public void testGetNumberValue() throws IOException, JsonParseException {
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

    @Test
    public void testHandleEOF() throws JsonParseException {
        try {
            parser._handleEOF();
        } catch (JsonParseException e) {
            e.printStackTrace();
        }
    }
}