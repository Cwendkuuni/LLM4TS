package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.OutputStream;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.ParserMinimalBase;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Test cases for {@link TreeTraversingParser}.
 */
@RunWith(MockitoJUnitRunner.class)
public class TreeTraversingParserTest {

    @Mock
    private NodeCursor nodeCursor;

    @Mock
    private ObjectCodec objectCodec;

    @InjectMocks
    private TreeTraversingParser parser;

    @Test
    public void testNewInstanceWithJsonNode() throws IOException {
        JsonNode jsonNode = mock(JsonNode.class);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        assertEquals(jsonNode, parser.getCurrentNode());
        assertNull(parser.getParsingContext());
        assertNull(parser.getCodec());
    }

    @Test
    public void testNewInstanceWithJsonNodeAndObjectCodec() throws IOException {
        JsonNode jsonNode = mock(JsonNode.class);
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        when(objectMapper.getFactory()).thenReturn(new DefaultSerializerProvider());
        ObjectCodec objectCodec = mock(ObjectCodec.class);
        when(objectMapper.getCodec()).thenReturn(objectCodec);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode, objectMapper);
        assertEquals(jsonNode, parser.getCurrentNode());
        assertEquals(objectCodec, parser.getCodec());
    }

    @Test
    public void testSetCodec() throws IOException {
        ObjectCodec objectCodec = mock(ObjectCodec.class);
        TreeTraversingParser parser = new TreeTraversingParser(mock(JsonNode.class));
        parser.setCodec(objectCodec);
        assertEquals(objectCodec, parser.getCodec());
    }

    @Test
    public void testGetCodec() throws IOException {
        ObjectCodec objectCodec = mock(ObjectCodec.class);
        TreeTraversingParser parser = new TreeTraversingParser(mock(JsonNode.class), objectCodec);
        assertEquals(objectCodec, parser.getCodec());
    }

    @Test
    public void testClose() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(mock(JsonNode.class));
        parser.close();
        assertNull(parser.getCurrentNode());
        assertNull(parser.getParsingContext());
        assertNull(parser.getCodec());
    }

    @Test
    public void testNextToken() throws IOException, JsonParseException {
        JsonToken expectedToken = JsonToken.START_OBJECT;
        when(nodeCursor.nextToken()).thenReturn(expectedToken);

        TreeTraversingParser parser = new TreeTraversingParser(nodeCursor, objectCodec);
        JsonToken actualToken = parser.nextToken();
        assertEquals(expectedToken, actualToken);
    }

    @Test
    public void testNextTokenReturnsEndMarkerAfterNullNode() throws IOException, JsonParseException {
        when(nodeCursor.currentNode()).thenReturn(null);

        TreeTraversingParser parser = new TreeTraversingParser(nodeCursor, objectCodec);
        JsonToken actualToken = parser.nextToken();
        assertNull(actualToken);
    }

    @Test
    public void testNextTokenReturnsEndMarkerAfterLastNode() throws IOException, JsonParseException {
        when(nodeCursor.nextToken()).thenReturn(JsonToken.END_ARRAY);
        when(nodeCursor.nextToken()).thenReturn(null);

        TreeTraversingParser parser = new TreeTraversingParser(nodeCursor, objectCodec);
        JsonToken actualToken = parser.nextToken();
        assertNull(actualToken);
    }

    @Test
    public void testNextTokenReturnsStartContainerAfterContainerNode() throws IOException, JsonParseException {
        when(nodeCursor.currentNode()).thenReturn(mock(JsonNode.class));
        when(nodeCursor.currentNode().isArray()).thenReturn(true);

        TreeTraversingParser parser = new TreeTraversingParser(nodeCursor, objectCodec);
        JsonToken actualToken = parser.nextToken();
        assertEquals(JsonToken.START_ARRAY, actualToken);
    }

    @Test(expected = JsonParseException.class)
    public void testNextTokenThrowsJsonParseExceptionAfterClosedParser() throws IOException, JsonParseException {
        TreeTraversingParser parser = new TreeTraversingParser(mock(JsonNode.class));
        parser.close();
        parser.nextToken();
    }

    @Test
    public void testSkipChildren() throws IOException, JsonParseException {
        JsonNode expectedNode = mock(JsonNode.class);
        when(nodeCursor.nextToken()).thenReturn(JsonToken.START_OBJECT);
        when(nodeCursor.currentNode()).thenReturn(expectedNode);
        when(expectedNode.iterateChildren()).thenReturn(nodeCursor);

        TreeTraversingParser parser = new TreeTraversingParser(nodeCursor, objectCodec);
        parser.skipChildren();
        assertFalse(parser.isStartContainer());
    }

    @Test
    public void testIsClosed() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(mock(JsonNode.class));
        assertFalse(parser.isClosed());
        parser.close();
        assertTrue(parser.isClosed());
    }

    @Test
    public void testGetText() throws IOException, JsonParseException {
        JsonNode jsonNode = mock(JsonNode.class);
        when(jsonNode.textValue()).thenReturn("Hello World");

        TreeTraversingParser parser = new TreeTraversingParser(jsonNode, objectCodec);
        String actualText = parser.getText();
        assertEquals("Hello World", actualText);
    }

    @Test
    public void testGetTextCharacters() throws IOException, JsonParseException {
        JsonNode jsonNode = mock(JsonNode.class);
        when(jsonNode.textValue()).thenReturn("Hello World");

        TreeTraversingParser parser = new TreeTraversingParser(jsonNode, objectCodec);
        char[] actualTextCharacters = parser.getTextCharacters();
        assertArrayEquals("Hello World".toCharArray(), actualTextCharacters);
    }

    @Test
    public void testGetTextLength() throws IOException, JsonParseException {
        JsonNode jsonNode = mock(JsonNode.class);
        when(jsonNode.textValue()).thenReturn("Hello World");

        TreeTraversingParser parser = new TreeTraversingParser(jsonNode, objectCodec);
        int actualTextLength = parser.getTextLength();
        assertEquals(11, actualTextLength);
    }

    @Test
    public void testGetTextOffset() throws IOException, JsonParseException {
        JsonNode jsonNode = mock(JsonNode.class);
        when(jsonNode.textValue()).thenReturn("Hello World");

        TreeTraversingParser parser = new TreeTraversingParser(jsonNode, objectCodec);
        int actualTextOffset = parser.getTextOffset();
        assertEquals(0, actualTextOffset);
    }

    @Test
    public void testHasTextCharacters() throws IOException, JsonParseException {
        JsonNode jsonNode = mock(JsonNode.class);
        when(jsonNode.textValue()).thenReturn("Hello World");

        TreeTraversingParser parser = new TreeTraversingParser(jsonNode, objectCodec);
        assertFalse(parser.hasTextCharacters());
    }

    @Test
    public void testGetNumberType() throws IOException, JsonParseException {
        JsonNode jsonNode = mock(JsonNode.class);
        when(jsonNode.numberType()).thenReturn(NumberType.INT);

        TreeTraversingParser parser = new TreeTraversingParser(jsonNode, objectCodec);
        NumberType actualNumberType = parser.getNumberType();
        assertEquals(NumberType.INT, actualNumberType);
    }

    @Test
    public void testGetBigIntegerValue() throws IOException, JsonParseException {
        BigInteger bigInteger = BigInteger.valueOf(12345);
        JsonNode jsonNode = mock(JsonNode.class);
        when(jsonNode.bigIntegerValue()).thenReturn(bigInteger);

        TreeTraversingParser parser = new TreeTraversingParser(jsonNode, objectCodec);
        BigInteger actualBigInteger = parser.getBigIntegerValue();
        assertEquals(bigInteger, actualBigInteger);
    }

    @Test
    public void testGetDecimalValue() throws IOException, JsonParseException {
        BigDecimal bigDecimal = new BigDecimal("12345.6789");
        JsonNode jsonNode = mock(JsonNode.class);
        when(jsonNode.decimalValue()).thenReturn(bigDecimal);

        TreeTraversingParser parser = new TreeTraversingParser(jsonNode, objectCodec);
        BigDecimal actualBigDecimal = parser.getDecimalValue();
        assertEquals(bigDecimal, actualBigDecimal);
    }

    @Test
    public void testGetDoubleValue() throws IOException, JsonParseException {
        double doubleValue = 12345.6789;
        JsonNode jsonNode = mock(JsonNode.class);
        when(jsonNode.doubleValue()).thenReturn(doubleValue);

        TreeTraversingParser parser = new TreeTraversingParser(jsonNode, objectCodec);
        double actualDouble = parser.getDoubleValue();
        assertEquals(doubleValue, actualDouble, 0.001);
    }

    @Test
    public void testGetFloatValue() throws IOException, JsonParseException {
        float floatValue = (float) 12345.6789;
        JsonNode jsonNode = mock(JsonNode.class);
        when(jsonNode.doubleValue()).thenReturn(12345.6789);

        TreeTraversingParser parser = new TreeTraversingParser(jsonNode, objectCodec);
        float actualFloat = parser.getFloatValue();
        assertEquals(floatValue, actualFloat, 0.001f);
    }

    @Test
    public void testGetLongValue() throws IOException, JsonParseException {
        long longValue = 12345;
        JsonNode jsonNode = mock(JsonNode.class);
        when(jsonNode.longValue()).thenReturn(longValue);

        TreeTraversingParser parser = new TreeTraversingParser(jsonNode, objectCodec);
        long actualLong = parser.getLongValue();
        assertEquals(longValue, actualLong);
    }

    @Test
    public void testGetIntValue() throws IOException, JsonParseException {
        int intValue = 12345;
        JsonNode jsonNode = mock(JsonNode.class);
        when(jsonNode.intValue()).thenReturn(intValue);

        TreeTraversingParser parser = new TreeTraversingParser(jsonNode, objectCodec);
        int actualInt = parser.getIntValue();
        assertEquals(intValue, actualInt);
    }

    @Test
    public void testGetNumberValue() throws IOException, JsonParseException {
        JsonNode jsonNode = mock(JsonNode.class);
        when(jsonNode.numberValue()).thenReturn(12345);

        TreeTraversingParser parser = new TreeTraversingParser(jsonNode, objectCodec);
        Number actualNumber = parser.getNumberValue();
        assertEquals(12345, actualNumber.intValue());
    }

    @Test
    public void testGetEmbeddedObject() throws IOException, JsonParseException {
        Object embeddedObject = new Object();
        JsonNode jsonNode = mock(JsonNode.class);
        when(jsonNode.getPojo()).thenReturn(embeddedObject);

        TreeTraversingParser parser = new TreeTraversingParser(jsonNode, objectCodec);
        Object actualEmbeddedObject = parser.getEmbeddedObject();
        assertEquals(embeddedObject, actualEmbeddedObject);
    }

    @Test
    public void testIsNaN() throws IOException, JsonParseException {
        JsonNode jsonNode = mock(JsonNode.class);
        when(jsonNode.isNaN()).thenReturn(true);

        TreeTraversingParser parser = new TreeTraversingParser(jsonNode, objectCodec);
        assertTrue(parser.isNaN());
    }

    @Test
    public void testGetBinaryValue() throws IOException, JsonParseException {
        byte[] binaryData = new byte[] { 0, 1, 2, 3, 4, 5 };
        JsonNode jsonNode = mock(JsonNode.class);
        when(jsonNode.binaryValue()).thenReturn(binaryData);

        TreeTraversingParser parser = new TreeTraversingParser(jsonNode, objectCodec);
        byte[] actualBinaryData = parser.getBinaryValue(null);
        assertArrayEquals(binaryData, actualBinaryData);
    }

    @Test
    public void testReadBinaryValue() throws IOException, JsonParseException {
        byte[] binaryData = new byte[] { 0, 1, 2, 3, 4, 5 };
        OutputStream outputStream = mock(OutputStream.class);

        JsonNode jsonNode = mock(JsonNode.class);
        when(jsonNode.binaryValue()).thenReturn(binaryData);

        TreeTraversingParser parser = new TreeTraversingParser(jsonNode, objectCodec);
        int actualBytesRead = parser.readBinaryValue(null, outputStream);
        assertEquals(binaryData.length, actualBytesRead);
    }

    @Test
    public void testCurrentNode() throws IOException, JsonParseException {
        JsonNode jsonNode = mock(JsonNode.class);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode, objectCodec);
        assertEquals(jsonNode, parser.getCurrentNode());
    }

    @Test
    public void testCurrentNumericNode() throws IOException, JsonParseException {
        JsonNode jsonNode = mock(JsonNode.class);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode, objectCodec);
        JsonNode numericNode = mock(JsonNode.class);
        when(jsonNode.asNumber()).thenReturn(numericNode);
        assertEquals(numericNode, parser.currentNumericNode());
    }

    @Test(expected = JsonParseException.class)
    public void test_handleEOF() throws IOException, JsonParseException {
        TreeTraversingParser parser = new TreeTraversingParser(mock(JsonNode.class));
        parser._handleEOF();
    }
}