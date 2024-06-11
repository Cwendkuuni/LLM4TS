package com.fasterxml.jackson.databind.node;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class TreeTraversingParserTest {

    private TreeTraversingParser treeTraversingParser;

    @Before
    public void setup() {
        JsonNode jsonNode = new ObjectNode(JsonNodeFactory.instance);
        treeTraversingParser = new TreeTraversingParser(jsonNode);
    }

    @Test
    public void testSetCodec() {
        ObjectCodec objectCodec = new ObjectMapper();
        treeTraversingParser.setCodec(objectCodec);
        Assert.assertEquals(objectCodec, treeTraversingParser.getCodec());
    }

    @Test
    public void testVersion() {
        Version version = PackageVersion.VERSION;
        Assert.assertEquals(version, treeTraversingParser.version());
    }

    @Test
    public void testClose() throws IOException {
        treeTraversingParser.close();
        Assert.assertTrue(treeTraversingParser.isClosed());
    }

    @Test
    public void testNextToken() throws IOException, JsonParseException {
        Assert.assertNotNull(treeTraversingParser.nextToken());
    }

    @Test
    public void testSkipChildren() throws IOException, JsonParseException {
        Assert.assertNotNull(treeTraversingParser.skipChildren());
    }

    @Test
    public void testGetCurrentName() {
        Assert.assertNull(treeTraversingParser.getCurrentName());
    }

    @Test
    public void testOverrideCurrentName() {
        String name = "name";
        treeTraversingParser.overrideCurrentName(name);
        Assert.assertEquals(name, treeTraversingParser.getCurrentName());
    }

    @Test
    public void testGetParsingContext() {
        Assert.assertNotNull(treeTraversingParser.getParsingContext());
    }

    @Test
    public void testGetTokenLocation() {
        Assert.assertNotNull(treeTraversingParser.getTokenLocation());
    }

    @Test
    public void testGetCurrentLocation() {
        Assert.assertNotNull(treeTraversingParser.getCurrentLocation());
    }

    @Test
    public void testGetText() throws IOException {
        Assert.assertNull(treeTraversingParser.getText());
    }

    @Test
    public void testGetTextCharacters() throws IOException, JsonParseException {
        Assert.assertNotNull(treeTraversingParser.getTextCharacters());
    }

    @Test
    public void testGetTextLength() throws IOException, JsonParseException {
        Assert.assertEquals(0, treeTraversingParser.getTextLength());
    }

    @Test
    public void testGetTextOffset() throws IOException, JsonParseException {
        Assert.assertEquals(0, treeTraversingParser.getTextOffset());
    }

    @Test
    public void testHasTextCharacters() {
        Assert.assertFalse(treeTraversingParser.hasTextCharacters());
    }

    @Test
    public void testGetNumberType() throws IOException, JsonParseException {
        Assert.assertNull(treeTraversingParser.getNumberType());
    }

    @Test
    public void testGetBigIntegerValue() throws IOException, JsonParseException {
        Assert.assertNull(treeTraversingParser.getBigIntegerValue());
    }

    @Test
    public void testGetDecimalValue() throws IOException, JsonParseException {
        Assert.assertNull(treeTraversingParser.getDecimalValue());
    }

    @Test
    public void testGetDoubleValue() throws IOException, JsonParseException {
        Assert.assertEquals(0.0, treeTraversingParser.getDoubleValue(), 0.01);
    }

    @Test
    public void testGetFloatValue() throws IOException, JsonParseException {
        Assert.assertEquals(0.0f, treeTraversingParser.getFloatValue(), 0.01);
    }

    @Test
    public void testGetLongValue() throws IOException, JsonParseException {
        Assert.assertEquals(0L, treeTraversingParser.getLongValue());
    }

    @Test
    public void testGetIntValue() throws IOException, JsonParseException {
        Assert.assertEquals(0, treeTraversingParser.getIntValue());
    }

    @Test
    public void testGetNumberValue() throws IOException, JsonParseException {
        Assert.assertNull(treeTraversingParser.getNumberValue());
    }

    @Test
    public void testGetEmbeddedObject() {
        Assert.assertNull(treeTraversingParser.getEmbeddedObject());
    }

    @Test
    public void testIsNaN() {
        Assert.assertFalse(treeTraversingParser.isNaN());
    }

    @Test
    public void testGetBinaryValue() throws IOException, JsonParseException {
        Assert.assertNull(treeTraversingParser.getBinaryValue(null));
    }

    @Test
    public void testReadBinaryValue() throws IOException, JsonParseException {
        Assert.assertEquals(0, treeTraversingParser.readBinaryValue(null, null));
    }

}