
package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.Assert.*;
import org.junit.Test;

public class TreeTraversingParserTest {

    @Test
    public void testSetCodec() {
        ObjectCodec codec = new ObjectMapper().reader().getCodec();
        TreeTraversingParser parser = new TreeTraversingParser(null, null);
        parser.setCodec(codec);
        assertEquals(codec, parser.getCodec());
    }

    @Test
    public void testVersion() {
        Version version = com.fasterxml.jackson.databind.cfg.PackageVersion.VERSION;
        TreeTraversingParser parser = new TreeTraversingParser(null, null);
        assertEquals(version, parser.version());
    }

    @Test
    public void testClose() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(null, null);
        assertFalse(parser.isClosed());
        parser.close();
        assertTrue(parser.isClosed());
    }

    @Test
    public void testNextToken() throws IOException, JsonParseException {
        TreeTraversingParser parser = new TreeTraversingParser(null, null);
        JsonToken token = parser.nextToken();
        assertNull(token);
    }

    @Test
    public void testSkipChildren() throws IOException, JsonParseException {
        TreeTraversingParser parser = new TreeTraversingParser(null, null);
        JsonParser result = parser.skipChildren();
        assertEquals(parser, result);
    }

    @Test
    public void testGetNumberType() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(null, null);
        NumberType result = parser.getNumberType();
        assertNull(result);
    }

    @Test
    public void testGetBigIntegerValue() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(null, null);
        BigInteger result = parser.getBigIntegerValue();
        assertNull(result);
    }

    @Test
    public void testGetDecimalValue() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(null, null);
        BigDecimal result = parser.getDecimalValue();
        assertNull(result);
    }

    @Test
    public void testGetDoubleValue() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(null, null);
        double result = parser.getDoubleValue();
        assertEquals(0.0, result, 0.0);
    }

    @Test
    public void testGetFloatValue() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(null, null);
        float result = parser.getFloatValue();
        assertEquals(0.0f, result, 0.0f);
    }

    @Test
    public void testGetIntValue() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(null, null);
        int result = parser.getIntValue();
        assertEquals(0, result);
    }

    @Test
    public void testGetLongValue() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(null, null);
        long result = parser.getLongValue();
        assertEquals(0L, result);
    }

    @Test
    public void testGetNumberValue() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(null, null);
        Number result = parser.getNumberValue();
        assertNull(result);
    }

    @Test
    public void testGetEmbeddedObject() {
        TreeTraversingParser parser = new TreeTraversingParser(null, null);
        Object result = parser.getEmbeddedObject();
        assertNull(result);
    }

    @Test
    public void testIsNaN() {
        TreeTraversingParser parser = new TreeTraversingParser(null, null);
        assertFalse(parser.isNaN());
    }

    @Test
    public void testGetBinaryValue() throws IOException, JsonParseException {
        Base64Variant variant = Base64Variant.MIME_NO_LINEFEEDS;
        TreeTraversingParser parser = new TreeTraversingParser(null, null);
        byte[] result = parser.getBinaryValue(variant);
        assertNull(result);
    }

    @Test
    public void testReadBinaryValue() throws IOException, JsonParseException {
        Base64Variant variant = Base64Variant.MIME_NO_LINEFEEDS;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TreeTraversingParser parser = new TreeTraversingParser(null, null);
        int result = parser.readBinaryValue(variant, outputStream);
        assertEquals(0, result);
    }
}
