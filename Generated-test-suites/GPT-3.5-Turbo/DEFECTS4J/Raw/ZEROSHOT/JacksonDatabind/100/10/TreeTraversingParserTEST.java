package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;
import org.junit.Test;

public class TreeTraversingParserTest {

    @Test
    public void testGetCodec() {
        // Arrange
        ObjectCodec codec = new ObjectCodec();

        // Act
        TreeTraversingParser parser = new TreeTraversingParser(null, codec);
        ObjectCodec result = parser.getCodec();

        // Assert
        assertEquals(codec, result);
    }

    @Test
    public void testVersion() {
        // Arrange
        Version version = new Version();

        // Act
        TreeTraversingParser parser = new TreeTraversingParser(null);
        Version result = parser.version();

        // Assert
        assertEquals(version, result);
    }

    @Test
    public void testSetCodec() {
        // Arrange
        ObjectCodec codec = new ObjectCodec();

        // Act
        TreeTraversingParser parser = new TreeTraversingParser(null);
        parser.setCodec(codec);
        ObjectCodec result = parser.getCodec();

        // Assert
        assertEquals(codec, result);
    }

    @Test
    public void testClose() throws IOException {
        // Arrange
        TreeTraversingParser parser = new TreeTraversingParser(null);

        // Act
        parser.close();
        boolean result = parser.isClosed();

        // Assert
        assertTrue(result);
    }

    @Test
    public void testNextToken() throws IOException, JsonParseException {
        // Arrange
        TreeTraversingParser parser = new TreeTraversingParser(null);

        // Act
        JsonToken result = parser.nextToken();

        // Assert
        assertNull(result);
    }

    @Test
    public void testSkipChildren() throws IOException, JsonParseException {
        // Arrange
        TreeTraversingParser parser = new TreeTraversingParser(null);

        // Act
        JsonParser result = parser.skipChildren();

        // Assert
        assertEquals(parser, result);
    }

    @Test
    public void testIsClosed() {
        // Arrange
        TreeTraversingParser parser = new TreeTraversingParser(null);

        // Act
        boolean result = parser.isClosed();

        // Assert
        assertFalse(result);
    }

    @Test
    public void testGetCurrentName() {
        // Arrange
        String currentName = "currentName";
        TreeTraversingParser parser = new TreeTraversingParser(null);
        parser.overrideCurrentName(currentName);

        // Act
        String result = parser.getCurrentName();

        // Assert
        assertEquals(currentName, result);
    }

    @Test
    public void testGetParsingContext() {
        // Arrange
        NodeCursor cursor = new NodeCursor();
        TreeTraversingParser parser = new TreeTraversingParser(null);
        parser.setNodeCursor(cursor);

        // Act
        JsonStreamContext result = parser.getParsingContext();

        // Assert
        assertEquals(cursor, result);
    }

    @Test
    public void testGetTokenLocation() {
        // Arrange
        JsonLocation location = new JsonLocation();
        TreeTraversingParser parser = new TreeTraversingParser(null);
        parser.setTokenLocation(location);

        // Act
        JsonLocation result = parser.getTokenLocation();

        // Assert
        assertEquals(location, result);
    }

    @Test
    public void testGetCurrentLocation() {
        // Arrange
        JsonLocation location = new JsonLocation();
        TreeTraversingParser parser = new TreeTraversingParser(null);
        parser.setCurrentLocation(location);

        // Act
        JsonLocation result = parser.getCurrentLocation();

        // Assert
        assertEquals(location, result);
    }

    @Test
    public void testGetText() {
        // Arrange
        String text = "text";
        JsonNode node = new JsonNode();
        node.setTextValue(text);
        TreeTraversingParser parser = new TreeTraversingParser(node);

        // Act
        String result = parser.getText();

        // Assert
        assertEquals(text, result);
    }

    @Test
    public void testGetTextLength()  {
        // Arrange
        String text = "text";
        JsonNode node = new JsonNode();
        node.setTextValue(text);
        TreeTraversingParser parser = new TreeTraversingParser(node);

        // Act
        int result = parser.getTextLength();

        // Assert
        assertEquals(text.length(), result);
    }

    @Test
    public void testGetIntValue() throws IOException, JsonParseException {
        // Arrange
        int expectedValue = 1;
        JsonNode node = new JsonNode();
        node.setIntValue(expectedValue);
        TreeTraversingParser parser = new TreeTraversingParser(node);

        // Act
        int result = parser.getIntValue();

        // Assert
        assertEquals(expectedValue, result);
    }

    @Test
    public void testIsNaN() {
        // Arrange
        TreeTraversingParser parser = new TreeTraversingParser(null);

        // Act
        boolean result = parser.isNaN();

        // Assert
        assertFalse(result);
    }

    @Test
    public void testGetBinaryValue() throws IOException, JsonParseException {
        // Arrange
        byte[] expectedValue = new byte[]{1, 2, 3};
        JsonNode node = new JsonNode();
        node.setBinaryValue(expectedValue);
        TreeTraversingParser parser = new TreeTraversingParser(node);

        // Act
        byte[] result = parser.getBinaryValue(null);

        // Assert
        assertArrayEquals(expectedValue, result);
    }

    @Test
    public void testReadBinaryValue() throws IOException, JsonParseException {
        // Arrange
        byte[] expectedValue = new byte[]{1, 2, 3};
        JsonNode node = new JsonNode();
        node.setBinaryValue(expectedValue);
        TreeTraversingParser parser = new TreeTraversingParser(node);

        // Act
        byte[] result = new byte[expectedValue.length];
        parser.readBinaryValue(null, result, 0, expectedValue.length);

        // Assert
        assertArrayEquals(expectedValue, result);
    }
}