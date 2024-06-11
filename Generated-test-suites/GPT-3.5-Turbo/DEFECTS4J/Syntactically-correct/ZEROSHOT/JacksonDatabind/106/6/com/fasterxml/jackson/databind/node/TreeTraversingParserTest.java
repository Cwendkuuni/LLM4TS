package com.fasterxml.jackson.databind.node;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class TreeTraversingParserTest {

    private TreeTraversingParser parser;
    private JsonNode jsonNode;

    @Before
    public void setup() {
        jsonNode = createJsonNode();
        parser = new TreeTraversingParser(jsonNode);
    }

    @Test
    public void testSetCodec() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        parser.setCodec(objectMapper);
        Assert.assertEquals(objectMapper, parser.getCodec());
    }

    @Test
    public void testVersion() {
        Assert.assertEquals(com.fasterxml.jackson.databind.cfg.PackageVersion.VERSION, parser.version());
    }
    
    @Test
    public void testClose() throws IOException {
        Assert.assertFalse(parser.isClosed());

        parser.close();

        Assert.assertTrue(parser.isClosed());
    }

    @Test
    public void testNextToken() throws IOException {
        JsonToken expectedToken = JsonToken.START_ARRAY;

        Assert.assertEquals(expectedToken, parser.nextToken());
    }

    @Test
    public void testSkipChildren() throws IOException {
        Assert.assertEquals(parser, parser.skipChildren());
    }

    @Test
    public void testIsClosed() throws IOException {
        Assert.assertFalse(parser.isClosed());

        parser.close();

        Assert.assertTrue(parser.isClosed());
    }

    @Test
    public void testGetCurrentName() {
        JsonToken token = JsonToken.FIELD_NAME;
        String expectedCurrentName = "fieldName";

        Assert.assertEquals(token, parser.nextToken());
        Assert.assertEquals(expectedCurrentName, parser.getCurrentName());
    }

    @Test
    public void testOverrideCurrentName() throws IOException {
        String expectedCurrentName = "newFieldName";

        parser.nextToken();
        parser.overrideCurrentName(expectedCurrentName);

        Assert.assertEquals(expectedCurrentName, parser.getCurrentName());
    }

    @Test
    public void testGetParsingContext() throws IOException {
        JsonStreamContext expectedParsingContext = parser.getParsingContext();

        Assert.assertEquals(expectedParsingContext, parser.getParsingContext());
    }

    @Test
    public void testGetTokenLocation() throws IOException {
        JsonLocation expectedLocation = JsonLocation.NA;

        Assert.assertEquals(expectedLocation, parser.getTokenLocation());
    }

    @Test
    public void testGetCurrentLocation() throws IOException {
        JsonLocation expectedLocation = JsonLocation.NA;

        Assert.assertEquals(expectedLocation, parser.getCurrentLocation());
    }

    @Test
    public void testGetText() throws IOException {
        String expectedText = "fieldName";

        parser.nextToken();

        Assert.assertEquals(expectedText, parser.getText());
    }

    @Test
    public void testGetTextCharacters() throws IOException {
        String expectedText = "fieldName";

        parser.nextToken();

        Assert.assertArrayEquals(expectedText.toCharArray(), parser.getTextCharacters());
    }

    @Test
    public void testGetTextLength() throws IOException {
        String expectedText = "fieldName";

        parser.nextToken();

        Assert.assertEquals(expectedText.length(), parser.getTextLength());
    }

    @Test
    public void testGetTextOffset() throws IOException {
        String expectedText = "fieldName";

        parser.nextToken();

        Assert.assertEquals(0, parser.getTextOffset());
    }

    @Test
    public void testHasTextCharacters() throws IOException {
        parser.nextToken();

        Assert.assertFalse(parser.hasTextCharacters());
    }

    @Test
    public void testGetNumberType() throws IOException {
        parser.nextToken();

        Assert.assertNull(parser.getNumberType());

        parser.nextToken();

        Assert.assertEquals(NumberType.BIG_DECIMAL, parser.getNumberType());
    }

    @Test
    public void testGetBigIntegerValue() throws IOException {
        parser.nextToken();

        Assert.assertNull(parser.getBigIntegerValue());

        parser.nextToken();

        Assert.assertEquals(BigInteger.TEN, parser.getBigIntegerValue());
    }

    @Test
    public void testGetDecimalValue() throws IOException {
        parser.nextToken();

        Assert.assertNull(parser.getDecimalValue());

        parser.nextToken();

        Assert.assertEquals(new BigDecimal("0.123"), parser.getDecimalValue());
    }

    @Test
    public void testGetDoubleValue() throws IOException {
        parser.nextToken();

        Assert.assertEquals(0D, parser.getDoubleValue(), 0);

        parser.nextToken();

        Assert.assertEquals(0.123D, parser.getDoubleValue(), 0);
    }

    @Test
    public void testGetFloatValue() throws IOException {
        parser.nextToken();

        Assert.assertEquals(0F, parser.getFloatValue(), 0);

        parser.nextToken();

        Assert.assertEquals(0.123F, parser.getFloatValue(), 0);
    }

    @Test
    public void testGetIntValue() throws IOException {
        parser.nextToken();

        Assert.assertEquals(0, parser.getIntValue());

        parser.nextToken();

        Assert.assertEquals(1, parser.getIntValue());
    }

    @Test
    public void testGetLongValue() throws IOException {
        parser.nextToken();

        Assert.assertEquals(0L, parser.getLongValue());

        parser.nextToken();

        Assert.assertEquals(1L, parser.getLongValue());
    }

    @Test
    public void testGetNumberValue() throws IOException {
        parser.nextToken();

        Assert.assertNull(parser.getNumberValue());

        parser.nextToken();

        Assert.assertEquals(new BigDecimal("0.123"), parser.getNumberValue());
    }

    @Test
    public void testGetEmbeddedObject() throws IOException {
        parser.nextToken();

        Assert.assertNull(parser.getEmbeddedObject());

        parser.nextToken();

        JsonNode expectedEmbeddedObject = jsonNode.get(0);

        Object embeddedObject = parser.getEmbeddedObject();

        Assert.assertTrue(embeddedObject instanceof POJONode);
        Assert.assertEquals(expectedEmbeddedObject, ((POJONode) embeddedObject).getPojo());
    }

    @Test
    public void testIsNaN() throws IOException {
        parser.nextToken();

        Assert.assertFalse(parser.isNaN());

        parser.nextToken();

        Assert.assertTrue(parser.isNaN());
    }

    @Test
    public void testGetBinaryValue() throws IOException {
        parser.nextToken();

        Assert.assertNull(parser.getBinaryValue(Base64Variants.MIME));

        parser.nextToken();

        String expectedBinaryValue = "binaryValue";

        Assert.assertArrayEquals(expectedBinaryValue.getBytes(), parser.getBinaryValue(Base64Variants.MIME));
    }

    @Test
    public void testReadBinaryValue() throws IOException {
        parser.nextToken();

        Assert.assertEquals(0, parser.readBinaryValue(Base64Variants.MIME, null));

        parser.nextToken();

        String expectedBinaryValue = "binaryValue";

        OutputStream outputStream = System.out;

        Assert.assertEquals(expectedBinaryValue.length(), parser.readBinaryValue(Base64Variants.MIME, outputStream));
    }

    @After
    public void tearDown() {
        parser = null;
        jsonNode = null;
    }

    private JsonNode createJsonNode() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        JsonNode jsonNode = objectMapper.createObjectNode();
        ((ObjectNode) jsonNode).put("fieldName", "fieldValue");
        ((ObjectNode) jsonNode).put("numberNode", 0.123);
        ((ObjectNode) jsonNode).put("embeddedObjectNode", objectMapper.createObjectNode().put("subField", "subValue"));
        ((ObjectNode) jsonNode).put("arrayNode", objectMapper.createArrayNode().add(0).add(1));

        return jsonNode;
    }
}