package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectCodec;
import com.fasterxml.jackson.databind.node.BinaryNode;
import com.fasterxml.jackson.databind.node.POJONode;

public class TreeTraversingParserTest {

    private JsonNode testNode = null;
    private ObjectCodec testCodec = null;
    private TreeTraversingParser testParser = null;

    @Test
    public void testSetCodec() {
        testParser = new TreeTraversingParser(testNode);
        testParser.setCodec(testCodec);
        assertEquals(testCodec, testParser.getCodec());
    }

    @Test
    public void testVersion() {
        testParser = new TreeTraversingParser(testNode);
        assertEquals(com.fasterxml.jackson.databind.cfg.PackageVersion.VERSION, testParser.version());
    }

    @Test
    public void testClose() throws Exception {
        testParser = new TreeTraversingParser(testNode);
        testParser.close();
        assertEquals(true, testParser.isClosed());
    }

    @Test
    public void testNextToken() throws Exception {
        testParser = new TreeTraversingParser(testNode);
        assertEquals(null, testParser.nextToken());
    }

    @Test
    public void testSkipChildren() throws Exception {
        testParser = new TreeTraversingParser(testNode);
        assertEquals(testParser, testParser.skipChildren());
    }

    @Test
    public void testIsClosed() {
        testParser = new TreeTraversingParser(testNode);
        assertEquals(false, testParser.isClosed());
    }

    @Test
    public void testGetCurrentName() throws Exception {
        testParser = new TreeTraversingParser(testNode);
        assertEquals(null, testParser.getCurrentName());
    }

    @Test
    public void testOverrideCurrentName() throws Exception {
        testParser = new TreeTraversingParser(testNode);
        testParser.overrideCurrentName("");
		assertEquals("", testParser.getCurrentName());
    }

    @Test
    public void testGetParsingContext() throws Exception {
        testParser = new TreeTraversingParser(testNode);
        assertEquals(testParser, testParser.getParsingContext());
    }

    @Test
    public void testGetTokenLocation() throws Exception {
        testParser = new TreeTraversingParser(testNode);
        assertEquals(JsonLocation.NA, testParser.getTokenLocation());
    }

    @Test
    public void testGetCurrentLocation() throws Exception {
        testParser = new TreeTraversingParser(testNode);
        assertEquals(JsonLocation.NA, testParser.getCurrentLocation());
    }

    @Test
    public void testGetText() throws Exception {
        testParser = new TreeTraversingParser(testNode);
        assertEquals(null, testParser.getText());
    }

    @Test
    public void testGetTextCharacters() throws Exception {
        testParser = new TreeTraversingParser(testNode);
        assertEquals(null, testParser.getTextCharacters());
    }

    @Test
    public void testGetTextLength() throws Exception {
        testParser = new TreeTraversingParser(testNode);
        assertEquals(0, testParser.getTextLength());
    }

    @Test
    public void testGetTextOffset() throws Exception {
        testParser = new TreeTraversingParser(testNode);
        assertEquals(0, testParser.getTextOffset());
    }

    @Test
    public void testHasTextCharacters() throws Exception {
        testParser = new TreeTraversingParser(testNode);
        assertEquals(false, testParser.hasTextCharacters());
    }

    @Test
    public void testGetNumberType() throws Exception {
        testParser = new TreeTraversingParser(testNode);
        assertEquals(null, testParser.getNumberType());
    }

    @Test(expected = JsonParseException.class)
    public void testGetBigIntegerValue() throws Exception {
        testParser = new TreeTraversingParser(testNode);
        testParser.getBigIntegerValue();
    }

    @Test(expected = JsonParseException.class)
    public void testGetDecimalValue() throws Exception {
        testParser = new TreeTraversingParser(testNode);
        testParser.getDecimalValue();
    }

    @Test(expected = JsonParseException.class)
    public void testGetDoubleValue() throws Exception {
        testParser = new TreeTraversingParser(testNode);
        testParser.getDoubleValue();
    }

    @Test(expected = JsonParseException.class)
    public void testGetFloatValue() throws Exception {
        testParser = new TreeTraversingParser(testNode);
        testParser.getFloatValue();
    }

    @Test(expected = JsonParseException.class)
    public void testGetIntValue() throws Exception {
        testParser = new TreeTraversingParser(testNode);
        testParser.getIntValue();
    }

    @Test(expected = JsonParseException.class)
    public void testGetLongValue() throws Exception {
        testParser = new TreeTraversingParser(testNode);
        testParser.getLongValue();
    }

    @Test
    public void testGetNumberValue() throws Exception {
        testParser = new TreeTraversingParser(testNode);
        assertEquals(null, testParser.getNumberValue());
    }

    @Test
    public void testGetEmbeddedObject() throws Exception {
        testParser = new TreeTraversingParser(testNode);
        assertEquals(null, testParser.getEmbeddedObject());
    }

    @Test
    public void testIsNaN() throws Exception {
        testParser = new TreeTraversingParser(testNode);
        assertEquals(false, testParser.isNaN());
    }

    @Test
    public void testGetBinaryValue() throws Exception {
        testParser = new TreeTraversingParser(testNode);
        assertNull(testParser.getBinaryValue(null));
    }

    @Test
    public void testReadBinaryValue() throws Exception {
        testParser = new TreeTraversingParser(testNode);
        assertEquals(0, testParser.readBinaryValue(null, null));
    }
}