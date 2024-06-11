package com.fasterxml.jackson.databind.node;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

public class TreeTraversingParserTest {

    private TreeTraversingParser parser;

    @Before
    public void setUp() {
        JsonNode rootNode = createTestJson();
        parser = new TreeTraversingParser(rootNode);
    }

    @Test
    public void testSetCodec() {
        ObjectCodec codec = new ObjectCodec();
        parser.setCodec(codec);
        Assert.assertEquals(codec, parser.getCodec());
    }

    @Test
    public void testVersion() {
        Version version = PackageVersion.VERSION;
        Assert.assertEquals(version, parser.version());
    }

    @Test(expected = IOException.class)
    public void testClose() throws IOException {
        parser.close();
        parser.nextToken();
    }

    @Test
    public void testNextToken() throws IOException {
        JsonToken token = parser.nextToken();
        Assert.assertEquals(JsonToken.START_OBJECT, token);
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.FIELD_NAME, token);
        // Additional assertions, follow the same pattern for other tokens
    }

    @Test
    public void testSkipChildren() throws IOException {
        parser.nextToken(); // Start object
        parser.nextToken(); // Field name
        parser.nextToken(); // Value string
        parser.nextToken(); // Field name
        parser.skipChildren();
        parser.nextToken(); // Next field name
        // Additional assertions
    }

    @Test
    public void testIsClosed() throws IOException {
        Assert.assertFalse(parser.isClosed());
        parser.close();
        Assert.assertTrue(parser.isClosed());
    }

    @Test
    public void testGetCurrentName() {
        parser.nextToken(); // Start object
        parser.nextToken(); // Field name
        String currentName = parser.getCurrentName();
        Assert.assertEquals("field1", currentName);
    }

    @Test
    public void testOverrideCurrentName() {
        parser.nextToken(); // Start object
        parser.nextToken(); // Field name
        parser.overrideCurrentName("newField");
        String currentName = parser.getCurrentName();
        Assert.assertEquals("newField", currentName);
    }

    @Test
    public void testGetParsingContext() {
        JsonStreamContext context = parser.getParsingContext();
        Assert.assertNotNull(context);
        Assert.assertTrue(context.inRoot());
    }

    @Test
    public void testGetTokenLocation() {
        JsonLocation location = parser.getTokenLocation();
        Assert.assertEquals(JsonLocation.NA, location);
    }

    @Test
    public void testGetCurrentLocation() {
        JsonLocation location = parser.getCurrentLocation();
        Assert.assertEquals(JsonLocation.NA, location);
    }

    @Test
    public void testGetText() throws IOException {
        parser.nextToken(); // Start object
        parser.nextToken(); // Field name
        String text = parser.getText();
        Assert.assertEquals("value1", text);
        parser.nextToken(); // Next field name
        text = parser.getText();
        Assert.assertEquals("value2", text);
    }

    @Test
    public void testGetTextCharacters() throws IOException {
        parser.nextToken(); // Start object
        parser.nextToken(); // Field name
        char[] textChars = parser.getTextCharacters();
        String text = new String(textChars);
        Assert.assertEquals("value1", text);
    }

    @Test
    public void testGetTextLength() throws IOException {
        parser.nextToken(); // Start object
        parser.nextToken(); // Field name
        int textLength = parser.getTextLength();
        Assert.assertEquals(6, textLength);
    }

    @Test
    public void testGetTextOffset() throws IOException {
        parser.nextToken(); // Start object
        parser.nextToken(); // Field name
        int textOffset = parser.getTextOffset();
        Assert.assertEquals(0, textOffset);
    }

    @Test
    public void testHasTextCharacters() {
        boolean hasTextCharacters = parser.hasTextCharacters();
        Assert.assertFalse(hasTextCharacters);
    }

    @Test
    public void testGetNumberType() throws IOException {
        parser.nextToken(); // Start object
        parser.nextToken(); // Field name
        NumberType numberType = parser.getNumberType();
        Assert.assertEquals(NumberType.INT, numberType);
    }

    @Test
    public void testGetBigIntegerValue() throws IOException {
        parser.nextToken(); // Start object
        parser.nextToken(); // Field name
        BigInteger bigIntegerValue = parser.getBigIntegerValue();
        Assert.assertEquals(new BigInteger("123"), bigIntegerValue);
    }

    @Test
    public void testGetDecimalValue() throws IOException {
        parser.nextToken(); // Start object
        parser.nextToken(); // Field name
        BigDecimal decimalValue = parser.getDecimalValue();
        Assert.assertEquals(new BigDecimal("123.45"), decimalValue);
    }

    @Test
    public void testGetDoubleValue() throws IOException {
        parser.nextToken(); // Start object
        parser.nextToken(); // Field name
        double doubleValue = parser.getDoubleValue();
        Assert.assertEquals(123.45, doubleValue, 0.001);
    }

    @Test
    public void testGetFloatValue() throws IOException {
        parser.nextToken(); // Start object
        parser.nextToken(); // Field name
        float floatValue = parser.getFloatValue();
        Assert.assertEquals(123.45f, floatValue, 0.001f);
    }

    @Test
    public void testGetIntValue() throws IOException {
        parser.nextToken(); // Start object
        parser.nextToken(); // Field name
        int intValue = parser.getIntValue();
        Assert.assertEquals(123, intValue);
    }

    @Test
    public void testGetLongValue() throws IOException {
        parser.nextToken(); // Start object
        parser.nextToken(); // Field name
        long longValue = parser.getLongValue();
        Assert.assertEquals(123, longValue);
    }

    @Test
    public void testGetNumberValue() throws IOException {
        parser.nextToken(); // Start object
        parser.nextToken(); // Field name
        Number numberValue = parser.getNumberValue();
        Assert.assertEquals((Number) 123, numberValue);
    }

    @Test
    public void testGetEmbeddedObject() throws IOException {
        parser.nextToken(); // Start object
        parser.nextToken(); // Field name
        Object embeddedObject = parser.getEmbeddedObject();
        Assert.assertNull(embeddedObject);
    }

    @Test
    public void testIsNaN() throws IOException {
        parser.nextToken(); // Start object
        parser.nextToken(); // Field name
        boolean isNaN = parser.isNaN();
        Assert.assertFalse(isNaN);
    }

    @Test
    public void testGetBinaryValue() throws IOException {
        parser.nextToken(); // Start object
        parser.nextToken(); // Field name
        byte[] binaryValue = parser.getBinaryValue();
        Assert.assertEquals(new byte[]{0x0}, binaryValue);
    }

    @Test
    public void testReadBinaryValue() throws IOException {
        parser.nextToken(); // Start object
        parser.nextToken(); // Field name
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int length = parser.readBinaryValue(out);
        byte[] binaryValue = out.toByteArray();
        Assert.assertEquals(1, length);
        Assert.assertEquals(new byte[]{0x0}, binaryValue);
    }

    private JsonNode createTestJson() {
        ObjectNode rootNode = new ObjectNode(JsonNodeFactory.instance);
        rootNode.put("field1", "value1");

        ObjectNode childNode = new ObjectNode(JsonNodeFactory.instance);
        childNode.put("childField1", "childValue1");
        childNode.put("childField2", 123);

        rootNode.put("field2", childNode);

        return rootNode;
    }

}