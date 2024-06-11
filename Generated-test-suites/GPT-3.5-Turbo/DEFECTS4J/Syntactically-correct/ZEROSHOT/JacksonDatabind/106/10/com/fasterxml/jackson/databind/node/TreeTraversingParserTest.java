package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectCodec;
import com.fasterxml.jackson.databind.cfg.PackageVersion;
import com.fasterxml.jackson.databind.node.NodeCursor;

public class TreeTraversingParserTest {

    private ObjectMapper mapper;
    private JsonNode jsonNode;
    private TreeTraversingParser parser;

    @Before
    public void setup() throws IOException {
        mapper = new ObjectMapper();
        jsonNode = mapper.readTree("{\"name\":\"John\",\"age\":30,\"city\":\"New York\"}");
        parser = new TreeTraversingParser(jsonNode);
        parser.setCodec(mapper);
    }

    @Test
    public void testGetCodec() {
        ObjectCodec codec = parser.getCodec();
        assertEquals(mapper, codec);
    }

    @Test
    public void testGetText() throws IOException {
        parser.nextToken();
        String text = parser.getText();
        assertEquals("", text);
    }

    @Test
    public void testGetIntValue() throws IOException {
        parser.nextToken();
        parser.nextToken();
        int intValue = parser.getIntValue();
        assertEquals(30, intValue);
    }

    @Test
    public void testLongValue() throws IOException {
        parser.nextToken();
        parser.nextToken();
        long longValue = parser.getLongValue();
        assertEquals(30, longValue);
    }

    @Test
    public void testGetNumberValue() throws IOException {
        parser.nextToken();
        parser.nextToken();
        Number numberValue = parser.getNumberValue();
        assertEquals(30, numberValue);
    }

    @Test
    public void testGetBinaryValue() throws IOException {
        parser.nextToken();
        parser.nextToken();
        byte[] binaryValue = parser.getBinaryValue(null);
        assertNull(binaryValue);
    }

    @Test
    public void testGetBigIntegerValue() throws IOException {
        parser.nextToken();
        parser.nextToken();
        BigInteger bigIntegerValue = parser.getBigIntegerValue();
        assertNull(bigIntegerValue);
    }

    @Test
    public void testGetDecimalValue() throws IOException {
        parser.nextToken();
        parser.nextToken();
        BigDecimal decimalValue = parser.getDecimalValue();
        assertNull(decimalValue);
    }

    @Test
    public void testGetObjectCodec() {
        ObjectCodec objectCodec = parser.getCodec();
        assertEquals(mapper, objectCodec);
    }

    @Test
    public void testNextToken() throws IOException {
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.START_OBJECT, token);
    }

    @Test
    public void testSkipChildren() throws IOException {
        parser.nextToken();
        parser.skipChildren();
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, token);
    }

    @Test
    public void testIsClosed() throws IOException {
        parser.close();
        assertTrue(parser.isClosed());
    }

    @Test
    public void testGetCurrentName() throws IOException {
        parser.nextToken();
        parser.nextToken();
        String currentName = parser.getCurrentName();
        assertEquals("age", currentName);
    }

    @Test
    public void testOverrideCurrentName() throws IOException {
        parser.nextToken();
        parser.nextToken();
        parser.overrideCurrentName("name");
        String currentName = parser.getCurrentName();
        assertEquals("name", currentName);
    }

    @Test
    public void testGetParsingContext() throws IOException {
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.START_OBJECT, token);
        assertNotNull(parser.getParsingContext());
    }

    @Test
    public void testGetTokenLocation() {
        assertEquals(JsonParser.NA, parser.getTokenLocation());
    }

    @Test
    public void testGetCurrentLocation() {
        assertEquals(JsonParser.NA, parser.getCurrentLocation());
    }

    @Test
    public void testHasTextCharacters() {
        assertFalse(parser.hasTextCharacters());
    }

    @Test
    public void testGetNumberType() throws IOException {
        parser.nextToken();
        parser.nextToken();
        NumberType numberType = parser.getNumberType();
        assertEquals(BigInteger.class, numberType);
    }

    @Test
    public void testGetEmbeddedObject() throws IOException {
        parser.nextToken();
        parser.nextToken();
        Object embeddedObject = parser.getEmbeddedObject();
        assertNull(embeddedObject);
    }

    @Test
    public void testIsNaN() throws IOException {
        parser.nextToken();
        parser.nextToken();
        assertFalse(parser.isNaN());
    }

    @Test
    public void testGetBinaryValueVariant() throws IOException {
        parser.nextToken();
        parser.nextToken();
        byte[] binaryValueVariant = parser.getBinaryValue(null);
        assertNull(binaryValueVariant);
    }

    @Test(expected = JsonParseException.class)
    public void testHandleEOF() throws IOException {
        parser._handleEOF();
    }
}