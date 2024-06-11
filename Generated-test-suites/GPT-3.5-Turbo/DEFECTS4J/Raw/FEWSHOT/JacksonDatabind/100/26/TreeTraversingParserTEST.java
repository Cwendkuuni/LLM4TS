
package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.ParserMinimalBase;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectCodec;
import com.fasterxml.jackson.databind.cfg.PackageVersion;
import com.fasterxml.jackson.databind.node.BinaryNode;
import com.fasterxml.jackson.databind.node.POJONode;

import org.junit.Before;
import org.junit.Test;

public class TreeTraversingParserTest {

    private ObjectMapper objectMapper;
    private JsonNode jsonNode;
    private ObjectCodec objectCodec;
    private TreeTraversingParser parser;
  
    @Before
    public void setUp() {
        objectMapper = mock(ObjectMapper.class);
        jsonNode = mock(JsonNode.class);
        objectCodec = mock(ObjectCodec.class);
        parser = new TreeTraversingParser(jsonNode, objectCodec);
    }

    @Test
    public void testSetCodec() {
        parser.setCodec(objectCodec);
        assertEquals(objectCodec, parser.getCodec());
    }

    @Test
    public void testAsTextBinaryNode() {
        BinaryNode binaryNode = mock(BinaryNode.class);
        when(jsonNode.binaryValue()).thenReturn(binaryNode);
        parser.getBinaryValue(Base64Variant.BASE64_DEFAULT);
        verify(jsonNode).binaryValue();
    }

    @Test
    public void testAsTextPojoNode() {
        POJONode pojoNode = mock(POJONode.class);
        when(jsonNode.isPojo()).thenReturn(true);
        when(jsonNode instanceof POJONode).thenReturn(true);
        when(jsonNode.isBinary()).thenReturn(false);
        when(jsonNode.asToken()).thenReturn(JsonToken.VALUE_EMBEDDED_OBJECT);
        when(jsonNode.getPojo()).thenReturn(pojoNode);
        parser.getEmbeddedObject();
        verify(jsonNode, times(1)).isPojo();
    }

    @Test
    public void testJsonStreamContext() {
        assertNotNull(parser.getParsingContext());
    }

    @Test
    public void testGetTokenLocation() {
        assertNotNull(parser.getTokenLocation());
    }

    @Test
    public void testGetCurrentLocation() {
        assertNotNull(parser.getCurrentLocation());
    }

    @Test
    public void testGetCurrentName() {
        assertNull(parser.getCurrentName());
    }

    @Test
    public void testOverrideCurrentName() {
        parser.overrideCurrentName("name");
        verify(jsonNode, times(1)).overrideCurrentName(anyString());
    }

    @Test
    public void testNextToken() throws IOException, JsonParseException {
        assertNull(parser.nextToken());
    }

    @Test
    public void testNextValue() throws IOException, JsonParseException {
        assertNull(parser.nextValue());
    }

    @Test
    public void testGetTokenType() {
        assertNull(parser.currentToken());
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
    public void testGetIntValue() throws IOException, JsonParseException {
        assertNull(parser.getIntValue());
    }

    @Test
    public void testGetLongValue() throws IOException, JsonParseException {
        assertNull(parser.getLongValue());
    }

    @Test
    public void testGetFloatValue() throws IOException, JsonParseException {
        assertNull(parser.getFloatValue());
    }

    @Test
    public void testGetDoubleValue() throws IOException, JsonParseException {
        assertNull(parser.getDoubleValue());
    }

    @Test
    public void testGetBigDecimalValue() throws IOException, JsonParseException {
        assertNull(parser.getBigDecimalValue());
    }

    @Test
    public void testGetBigIntegerValue() throws IOException, JsonParseException {
        assertNull(parser.getBigIntegerValue());
    }

    @Test
    public void testGetNumberType() throws IOException, JsonParseException {
        assertNull(parser.getNumberType());
    }

    @Test
    public void testGetNumberValue() throws IOException, JsonParseException {
        assertNull(parser.getNumberValue());
    }

    @Test
    public void testGetEmbeddedObjectNullNode() {
        JsonNode nullNode = mock(JsonNode.class);
        when(jsonNode.isPojo()).thenReturn(false);
        when(jsonNode.isBinary()).thenReturn(false);
        when(jsonNode.asToken()).thenReturn(JsonToken.VALUE_EMBEDDED_OBJECT);
        when(jsonNode.getPojo()).thenReturn(nullNode);
        parser.getEmbeddedObject();
        verify(jsonNode, times(1)).isPojo();
    }

    @Test
    public void testIsNaN() {
        assertFalse(parser.isNaN());
    }

    @Test
    public void testClose() throws IOException {
        parser.close();
        assertTrue(parser.isClosed());
    }

    @Test
    public void testSkipChildren() throws IOException, JsonParseException {
        parser.skipChildren();
        assertFalse(parser.isClosed());
    }
}

