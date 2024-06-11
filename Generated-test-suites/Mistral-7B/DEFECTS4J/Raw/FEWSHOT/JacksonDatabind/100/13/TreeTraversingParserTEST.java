package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import org.junit.Test;

public class TreeTraversingParserTest {

    @Test
    public void testConfig() {
        ObjectCodec objectCodec = mock(ObjectCodec.class);
        JsonNode jsonNode = mock(JsonNode.class);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode, objectCodec);
        assertEquals(objectCodec, parser.getCodec());
        assertNull(parser.getParsingContext());
    }

    @Test
    public void testLifecycle() throws IOException {
        JsonNode jsonNode = mock(JsonNode.class);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        assertFalse(parser.isClosed());
        parser.close();
        assertTrue(parser.isClosed());
    }

    @Test
    public void testNextToken() throws IOException, JsonParseException {
        JsonNode jsonNode = mock(JsonNode.class);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        JsonToken startArrayToken = JsonToken.START_ARRAY;
        JsonToken startObjectToken = JsonToken.START_OBJECT;
        JsonToken endArrayToken = JsonToken.END_ARRAY;
        JsonToken endObjectToken = JsonToken.END_OBJECT;
        JsonToken textToken = null;
        JsonToken endToken = null;
        NodeCursor nodeCursor = mock(NodeCursor.class);
        when(jsonNode.isArray()).thenReturn(true);
        doReturn(startArrayToken).when(nodeCursor).nextToken();
        doReturn(null).when(nodeCursor).hasChildren();
        doReturn(endArrayToken).when(nodeCursor).endToken();
        doReturn(nodeCursor).when(parser).getParsingContext();
        JsonToken token = parser.nextToken();
        assertEquals(startArrayToken, token);
        parser.skipChildren();
        token = parser.nextToken();
        assertEquals(endArrayToken, token);
        when(jsonNode.isObject()).thenReturn(true);
        doReturn(startObjectToken).when(nodeCursor).nextToken();
        doReturn(true).when(nodeCursor).hasChildren();
        doReturn(textToken).when(nodeCursor).nextToken();
        doReturn(endObjectToken).when(nodeCursor).endToken();
        parser = new TreeTraversingParser(jsonNode);
        token = parser.nextToken();
        assertEquals(startObjectToken, token);
        token = parser.nextToken();
        assertNull(token);
        token = parser.nextToken();
        assertEquals(endObjectToken, token);
    }

    @Test
    public void testPublicApiTraversal() throws IOException, JsonParseException {
        JsonNode jsonNode = mock(JsonNode.class);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        JsonToken startArrayToken = JsonToken.START_ARRAY;
        JsonToken startObjectToken = JsonToken.START_OBJECT;
        JsonToken textToken = JsonToken.VALUE_STRING;
        JsonToken numberIntToken = JsonToken.VALUE_NUMBER_INT;
        JsonToken numberFloatToken = JsonToken.VALUE_NUMBER_FLOAT;
        JsonToken endArrayToken = JsonToken.END_ARRAY;
        JsonToken endObjectToken = JsonToken.END_OBJECT;
        NodeCursor nodeCursor = mock(NodeCursor.class);
        when(jsonNode.isArray()).thenReturn(true);
        doReturn(startArrayToken).when(nodeCursor).nextToken();
        doReturn(null).when(nodeCursor).hasChildren();
        doReturn(endArrayToken).when(nodeCursor).endToken();
        doReturn(nodeCursor).when(parser).getParsingContext();
        parser = new TreeTraversingParser(jsonNode);
        JsonToken token = parser.nextToken();
        assertEquals(startArrayToken, token);
        parser.skipChildren();
        token = parser.nextToken();
        assertEquals(endArrayToken, token);
        when(jsonNode.isObject()).thenReturn(true);
        doReturn(startObjectToken).when(nodeCursor).nextToken();
        doReturn(true).when(nodeCursor).hasChildren();
        doReturn(textToken).when(nodeCursor).nextToken();
        doReturn(numberIntToken).when(nodeCursor).nextToken();
        doReturn(numberFloatToken).when(nodeCursor).nextToken();
        doReturn(endObjectToken).when(nodeCursor).endToken();
        parser = new TreeTraversingParser(jsonNode);
        token = parser.nextToken();
        assertEquals(startObjectToken, token);
        token = parser.nextToken();
        assertEquals(textToken.asString(), "");
        token = parser.nextToken();
        assertEquals(numberIntToken.asInt(), 0);
        token = parser.nextToken();
        assertEquals(numberFloatToken.asDouble(), 0.0);
        token = parser.nextToken();
        assertEquals(endObjectToken, token);
    }

    @Test
    public void testPublicApiTextContent() throws IOException, JsonParseException {
        JsonNode jsonNode = mock(JsonNode.class);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        String text = "Test";
        ValueNode valueNode = mock(ValueNode.class);
        when(jsonNode.isTextual()).thenReturn(true);
        when(jsonNode.textValue()).thenReturn(text);
        doReturn(valueNode).when(parser).currentNode();
        String result = parser.getText();
        assertEquals(text, result);
    }

    @Test
    public void testPublicApiNumberContent() throws IOException, JsonParseException {
        JsonNode jsonNode = mock(JsonNode.class);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        Number number = new BigInteger("42");
        NumericNode numericNode = mock(NumericNode.class);
        when(jsonNode.isNumber()).thenReturn(true);
        when(jsonNode.numberValue()).thenReturn(number);
        doReturn(numericNode).when(parser).currentNumericNode();
        BigInteger result = parser.getBigIntegerValue();
        assertEquals(number, result);
    }

    @Test
    public void testPublicApiBinaryContent() throws IOException, JsonParseException {
        JsonNode jsonNode = mock(JsonNode.class);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        byte[] binaryData = new byte[]{1, 2, 3};
        BinaryNode binaryNode = mock(BinaryNode.class);
        when(jsonNode.isBinary()).thenReturn(true);
        doReturn(binaryNode).when(parser).currentNode();
        when(binaryNode.binaryValue()).thenReturn(binaryData);
        byte[] result = parser.getBinaryValue(Base64Variant.STANDARD);
        assertArrayEquals(binaryData, result);
    }

    @Test
    public void testPublicApiSkipChildren() throws IOException, JsonParseException {
        JsonNode jsonNode = mock(JsonNode.class);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        JsonToken startArrayToken = JsonToken.START_ARRAY;
        JsonToken startObjectToken = JsonToken.START_OBJECT;
        JsonToken textToken = JsonToken.VALUE_STRING;
        JsonToken numberIntToken = JsonToken.VALUE_NUMBER_INT;
        JsonToken numberFloatToken = JsonToken.VALUE_NUMBER_FLOAT;
        JsonToken endArrayToken = JsonToken.END_ARRAY;
        JsonToken endObjectToken = JsonToken.END_OBJECT;
        NodeCursor nodeCursor = mock(NodeCursor.class);
        when(jsonNode.isArray()).thenReturn(true);
        doReturn(startArrayToken).when(nodeCursor).nextToken();
        doReturn(null).when(nodeCursor).hasChildren();
        doReturn(endArrayToken).when(nodeCursor).endToken();
        doReturn(nodeCursor).when(parser).getParsingContext();
        parser = new TreeTraversingParser(jsonNode);
        JsonToken token = parser.nextToken();
        assertEquals(startArrayToken, token);
        parser.skipChildren();
        token = parser.nextToken();
        assertEquals(endArrayToken, token);
        when(jsonNode.isObject()).thenReturn(true);
        doReturn(startObjectToken).when(nodeCursor).nextToken();
        doReturn(true).when(nodeCursor).hasChildren();
        doReturn(textToken).when(nodeCursor).nextToken();
        doReturn(numberIntToken).when(nodeCursor).nextToken();
        doReturn(numberFloatToken).when(nodeCursor).nextToken();
        doReturn(endObjectToken).when(nodeCursor).endToken();
        parser = new TreeTraversingParser(jsonNode);
        token = parser.nextToken();
        assertEquals(startObjectToken, token);
        parser.skipChildren();
        token = parser.nextToken();
        assertEquals(endObjectToken, token);
    }

    @Test
    public void testPublicApiIsNaN() throws IOException, JsonParseException {
        JsonNode jsonNode = mock(JsonNode.class);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        NumericNode numericNode = mock(NumericNode.class);
        when(jsonNode.isNumber()).thenReturn(true);
        doReturn(numericNode).when(parser).currentNumericNode();
        when(numericNode.isNaN()).thenReturn(true);
        assertTrue(parser.isNaN());
    }

    @Test
    public void testPublicApiBinaryRead() throws IOException, JsonParseException {
        JsonNode jsonNode = mock(JsonNode.class);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        OutputStream outputStream = mock(OutputStream.class);
        byte[] binaryData = new byte[]{1, 2, 3};
        BinaryNode binaryNode = mock(BinaryNode.class);
        when(jsonNode.isBinary()).thenReturn(true);
        doReturn(binaryNode).when(parser).currentNode();
        when(binaryNode.binaryValue()).thenReturn(binaryData);
        int result = parser.readBinaryValue(Base64Variant.STANDARD, outputStream);
        assertEquals(binaryData.length, result);
    }

    @Test
    public void testInternalMethods() throws IOException, JsonParseException {
        JsonNode jsonNode = mock(JsonNode.class);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        JsonNode currentNode = mock(JsonNode.class);
        when(jsonNode.isNumber()).thenReturn(true);
        doReturn(currentNode).when(parser).currentNode();
        NumericNode numericNode = mock(NumericNode.class);
        when(currentNode.isNumber()).thenReturn(true);
        when(currentNode).thenReturn(numericNode);
        parser.currentNumericNode();
        verify(numericNode).isNaN();
    }
}