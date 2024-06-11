package com.fasterxml.jackson.databind.node;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TreeTraversingParserTest {

    private TreeTraversingParser parser;

    @Before
    public void setup() {
        JsonNode node = new JsonNode();
        parser = new TreeTraversingParser(node);
    }

    @After
    public void teardown() {
        parser = null;
    }

    @Test
    public void testSetCodec() {
        ObjectCodec codec = new ObjectCodec();
        parser.setCodec(codec);
        assertEquals(codec, parser.getCodec());
    }

    @Test
    public void testVersion() {
        Version version = com.fasterxml.jackson.databind.cfg.PackageVersion.VERSION;
        assertEquals(version, parser.version());
    }

    @Test
    public void testClose() {
        assertFalse(parser.isClosed());
        parser.close();
        assertTrue(parser.isClosed());
    }

    @Test
    public void testSkipChildren() {
        assertEquals(parser, parser.skipChildren());
    }

    @Test
    public void testIsClosed() {
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
        assertArrayEquals(new char[] {}, parser.getTextCharacters());
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
        assertEquals(0.0f, parser.getFloatValue(), 0.0f);
    }

    @Test
    public void testGetLongValue() throws IOException, JsonParseException {
        assertEquals(0L, parser.getLongValue());
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
        assertNull(parser.getBinaryValue(null));
    }

    @Test
    public void testReadBinaryValue() throws IOException, JsonParseException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        assertEquals(0, parser.readBinaryValue(null, out));
        assertArrayEquals(new byte[] {}, out.toByteArray());
    }

    @Test
    public void testCurrentNode() {
        assertNull(parser.currentNode());
    }

    @Test(expected = JsonParseException.class)
    public void testCurrentNumericNode() throws JsonParseException {
        parser.currentNumericNode();
    }

    @Test(expected = JsonParseException.class)
    public void testHandleEOF() throws JsonParseException {
        parser._handleEOF();
    }

}