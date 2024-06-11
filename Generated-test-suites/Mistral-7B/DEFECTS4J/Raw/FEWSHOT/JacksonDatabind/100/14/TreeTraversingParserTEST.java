package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonToken;
import org.junit.Test;

public class TreeTraversingParserTest {

    @Test
    public void testConfiguration() {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertNull(parser._objectCodec);
        assertNull(parser._nodeCursor);
        assertEquals(JsonToken.START_OBJECT, parser._nextToken);
        parser = new TreeTraversingParser(new Object(), new ObjectCodec());
        assertNotNull(parser._objectCodec);
        assertNotNull(parser._nodeCursor);
        assertEquals(JsonToken.START_NULL, parser._nextToken);
    }

    @Test
    public void testCloseableImplementation() throws IOException, JsonParseException {
        TreeTraversingParser parser = new TreeTraversingParser(new Object(), new ObjectCodec());
        assertFalse(parser.isClosed());
        parser.close();
        assertTrue(parser.isClosed());
    }

    @Test
    public void testPublicApiTraversal() throws IOException, JsonParseException {
        TreeTraversingParser parser = new TreeTraversingParser(new ObjectNode(new ObjectMapper().readTree("[]")), new ObjectCodec());
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        String name = parser.nextValue().asText();
        assertEquals("0", name);
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(0, parser.getIntValue());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        assertEquals(JsonToken.END_OF_STREAM, parser.nextToken());
        parser.close();
    }

    @Test
    public void testPublicApiTokenAccessors() throws IOException, JsonParseException {
        TreeTraversingParser parser = new TreeTraversingParser(new ObjectNode(new ObjectMapper().readTree("{'name': 'John Doe'}")), new ObjectCodec());
        assertEquals("name", parser.getCurrentName());
        assertEquals("John Doe", parser.getText());
        assertEquals(JsonToken.FIELD_NAME, parser.getCurrentToken());
        assertEquals("John Doe", parser.getText());
        parser.close();
    }

    @Test
    public void testPublicApiAccessToTextualContent() throws IOException, JsonParseException {
        TreeTraversingParser parser = new TreeTraversingParser(new ObjectNode(new ObjectMapper().readTree("{'name': 'John Doe'}")), new ObjectCodec());
        assertEquals("John Doe", parser.getText());
        assertEquals("John Doe", parser.getText());
        assertEquals("John Doe", parser.getText());
        parser.close();
    }

    @Test
    public void testPublicApiTypedNonTextAccess() throws IOException, JsonParseException {
        TreeTraversingParser parser = new TreeTraversingParser(new ObjectNode(new ObjectMapper().readTree("{'name': 123}")), new ObjectCodec());
        assertEquals(JsonType.NUMBER, parser.getNumberType());
        assertEquals(123L, parser.getLongValue());
        parser.close();
    }

    @Test
    public void testPublicApiTypedBinaryAccess() throws IOException, JsonParseException {
        TreeTraversingParser parser = new TreeTraversingParser(new ObjectNode(new ObjectMapper().readTree("{'name': {'data': [1,2,3]}}")), new ObjectCodec());
        byte[] binaryData = parser.getBinaryValue(Base64Variants.STANDARD);
        assertArrayEquals(new byte[]{1, 2, 3}, binaryData);
        parser.close();
    }

    @Test
    public void testInternalMethods() throws IOException, JsonParseException {
        TreeTraversingParser parser = new TreeTraversingParser(new ObjectNode(new ObjectMapper().readTree("{'name': {'data': [1,2,3]}}")), new ObjectCodec());
        JsonNode currentNode = parser.currentNode();
        assertNotNull(currentNode);
        parser.close();
    }
}