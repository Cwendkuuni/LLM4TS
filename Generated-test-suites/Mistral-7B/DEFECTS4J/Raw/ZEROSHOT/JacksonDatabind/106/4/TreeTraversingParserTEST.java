package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser.NumberType;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.AbstractJsonNode;
import com.fasterxml.jackson.databind.node.NdCollection;
import com.fasterxml.jackson.databind.node.NdObject;
import com.fasterxml.jackson.databind.node.NdValue;
import com.fasterxml.jackson.databind.node.TextNode;

public class TreeTraversingParserTest {

    @Test
    public void testTreeTraversingParser() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser parser = jsonFactory.getParser(new Object[] { new AbstractJsonNode[] { objectMapper.createObjectNode(), objectMapper.createArrayNode() } });
        TreeTraversingParser treeTraversingParser = new TreeTraversingParser((JsonNode) objectMapper.createObjectNode());

        // Test configuration
        assertEquals(objectMapper.getCodec(), treeTraversingParser.getCodec());
        assertNull(treeTraversingParser._nodeCursor);
        assertNull(treeTraversingParser._nextToken);
        assertFalse(treeTraversingParser._startContainer);
        assertFalse(treeTraversingParser._closed);

        // Test lifecycle
        treeTraversingParser.close();
        assertTrue(treeTraversingParser._closed);

        JsonParser parser2 = new TreeTraversingParser((JsonNode) objectMapper.createArrayNode(), objectMapper.getObjectCodec());
        assertNotNull(parser2._nodeCursor);

        // Test public API, traversal
        assertEquals(JsonToken.START_OBJECT, treeTraversingParser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, treeTraversingParser.nextToken());
        assertEquals("field1", treeTraversingParser.getText());
        assertEquals(JsonToken.VALUE_STRING, treeTraversingParser.nextToken());
        assertEquals("value1", treeTraversingParser.getText());
        assertEquals(JsonToken.END_OBJECT, treeTraversingParser.nextToken());

        treeTraversingParser = new TreeTraversingParser((JsonNode) objectMapper.createObjectNode(), objectMapper.getObjectCodec());
        assertEquals(JsonToken.START_OBJECT, treeTraversingParser.nextToken());
        assertEquals("field1", treeTraversingParser.getText());
        assertEquals(JsonToken.VALUE_STRING, treeTraversingParser.nextToken());
        assertEquals("value1", treeTraversingParser.getText());
        assertEquals(JsonToken.FIELD_NAME, treeTraversingParser.nextToken());
        assertEquals("field2", treeTraversingParser.getText());
        assertEquals(JsonToken.VALUE_NULL, treeTraversingParser.nextToken());
        assertEquals(JsonToken.END_OBJECT, treeTraversingParser.nextToken());

        treeTraversingParser = new TreeTraversingParser((JsonNode) objectMapper.createArrayNode(), objectMapper.getObjectCodec());
        assertEquals(JsonToken.START_ARRAY, treeTraversingParser.nextToken());
        assertEquals(JsonToken.START_OBJECT, treeTraversingParser.nextToken());
        assertEquals("field1", treeTraversingParser.getText());
        assertEquals("value1", treeTraversingParser.getText());
        assertEquals(JsonToken.FIELD_NAME, treeTraversingParser.nextToken());
        assertEquals("field2", treeTraversingParser.getText());
        assertEquals("value2", treeTraversingParser.getText());
        assertEquals(JsonToken.END_OBJECT, treeTraversingParser.nextToken());
        assertEquals(JsonToken.START_OBJECT, treeTraversingParser.nextToken());
        assertEquals("field1", treeTraversingParser.getText());
        assertEquals("value3", treeTraversingParser.getText());
        assertEquals(JsonToken.FIELD_NAME, treeTraversingParser.nextToken());
        assertEquals("field2", treeTraversingParser.getText());
        assertEquals("value4", treeTraversingParser.getText());
        assertEquals(JsonToken.END_OBJECT, treeTraversingParser.nextToken());
        assertEquals(JsonToken.END_ARRAY, treeTraversingParser.nextToken());

        // Test public API, token accessors
        assertEquals(null, treeTraversingParser.getCurrentName());
        treeTraversingParser.overrideCurrentName("newName");
        assertEquals("newName", treeTraversingParser.getCurrentName());
        assertNull(treeTraversingParser.getParsingContext());
        assertEquals(JsonLocation.NA, treeTraversingParser.getTokenLocation());
        assertEquals(JsonLocation.NA, treeTraversingParser.getCurrentLocation());

        // Test public API, textual content
        treeTraversingParser = new TreeTraversingParser((JsonNode) objectMapper.createObjectNode(), objectMapper.getObjectCodec());
        assertEquals(null, treeTraversingParser.getText());
        treeTraversingParser.nextToken();
        treeTraversingParser.nextToken();
        assertEquals("value1", treeTraversingParser.getText());

        // Test public API, typed non-text access
        treeTraversingParser = new TreeTraversingParser((JsonNode) objectMapper.createObjectNode(), objectMapper.getObjectCodec());
        assertEquals(JsonType. NULL, treeTraversingParser.getNumberType());
        treeTraversingParser.nextToken();
        treeTraversingParser.nextToken();
        assertEquals(JsonType.NUMBER, treeTraversingParser.getNumberType());
        assertEquals(NumberType.INT, treeTraversingParser.getNumberType());
        treeTraversingParser.nextToken();
        assertEquals(NumberType.NUMBER, treeTraversingParser.getNumberType());
        assertEquals(NumberType.FLOAT, treeTraversingParser.getNumberType());

        treeTraversingParser = new TreeTraversingParser((JsonNode) objectMapper.createObjectNode(), objectMapper.getObjectCodec());
        treeTraversingParser.nextToken();
        treeTraversingParser.nextToken();
        assertEquals(new BigInteger("123"), treeTraversingParser.getBigIntegerValue());
        treeTraversingParser.nextToken();
        treeTraversingParser.nextToken();
        assertEquals(new BigDecimal("456.789"), treeTraversingParser.getDecimalValue());
        treeTraversingParser.nextToken();
        treeTraversingParser.nextToken();
        assertEquals(456.789d, treeTraversingParser.getDoubleValue(), 0.001d);
        treeTraversingParser.nextToken();
        treeTraversingParser.nextToken();
        assertEquals(456.789f, treeTraversingParser.getFloatValue(), 0.001f);
        treeTraversingParser.nextToken();
        treeTraversingParser.nextToken();
        assertEquals(456, treeTraversingParser.getIntValue());
        treeTraversingParser.nextToken();
        treeTraversingParser.nextToken();
        assertEquals(456L, treeTraversingParser.getLongValue());
        treeTraversingParser.nextToken();
        treeTraversingParser.nextToken();
        assertEquals(new Number(456), treeTraversingParser.getNumberValue());

        // Test public API, typed binary (base64) access
        treeTraversingParser = new TreeTraversingParser((JsonNode) objectMapper.createObjectNode(), objectMapper.getObjectCodec());
        treeTraversingParser.nextToken();
        treeTraversingParser.nextToken();
        byte[] binaryData = treeTraversingParser.getBinaryValue(Base64Variant.STANDARD);
        assertNotNull(binaryData);
        assertEquals(16, binaryData.length);
        // Perform some random checks on the binary data
        for (int i = 0; i < binaryData.length; i++) {
            assertTrue(binaryData[i] >= 0 && binaryData[i] <= 127);
        }
        treeTraversingParser.nextToken();
        treeTraversingParser.nextToken();
        int numBytesRead = treeTraversingParser.readBinaryValue(Base64Variant.STANDARD, new ByteArrayOutputStream());
        assertEquals(binaryData.length, numBytesRead);

        // Test internal methods
        treeTraversingParser = new TreeTraversingParser((JsonNode) objectMapper.createObjectNode(), objectMapper.getObjectCodec());
        assertNull(treeTraversingParser.currentNode());
        treeTraversingParser.nextToken();
        treeTraversingParser.nextToken();
        AbstractJsonNode currentNode = treeTraversingParser.currentNode();
        assertNotNull(currentNode);
        assertEquals(JsonNodeType.OBJECT, currentNode.getNodeType());
        NdObject nodeObject = (NdObject) currentNode;
        assertEquals("field1", nodeObject.get("field1").asText());
        assertEquals("value1", nodeObject.get("value1").asText());
        assertNull(nodeObject.get("nonExistentField"));
        assertEquals(NdCollection.EMPTY, nodeObject.get("field2"));
        assertEquals(NdValue.NULL, nodeObject.get("field3"));
        treeTraversingParser.nextToken();
        treeTraversingParser.nextToken();
        currentNode = treeTraversingParser.currentNode();
        assertNotNull(currentNode);
        assertEquals(JsonNodeType.ARRAY, currentNode.getNodeType());
        NdCollection nodeCollection = (NdCollection) currentNode;
        assertEquals(2, nodeCollection.size());
        assertEquals("field1", nodeCollection.get(0).get("field1").asText());
        assertEquals("value2", nodeCollection.get(0).get("value1").asText());
        assertEquals("field2", nodeCollection.get(1).get("field1").asText());
        assertEquals("value4", nodeCollection.get(1).get("value1").asText());

        // Test closing parser
        treeTraversingParser.close();
        assertNull(treeTraversingParser._nodeCursor);
        assertNull(treeTraversingParser._currToken);

        // Test exception cases
        treeTraversingParser = new TreeTraversingParser((JsonNode) objectMapper.createObjectNode(), objectMapper.getObjectCodec());
        treeTraversingParser.nextToken();
        treeTraversingParser.nextToken();
        try {
            treeTraversingParser.getText();
            fail("Expected JsonParseException for non-text node.");
        } catch (JsonParseException e) {
            // Expected
        }
        treeTraversingParser.nextToken();
        try {
            treeTraversingParser.getIntValue();
            fail("Expected JsonParseException for non-numeric node.");
        } catch (JsonParseException e) {
            // Expected
        }
        treeTraversingParser.nextToken();
        try {
            treeTraversingParser.getDecimalValue();
            fail("Expected JsonParseException for non-numeric node.");
        } catch (JsonParseException e) {
            // Expected
        }
        treeTraversingParser.nextToken();
        try {
            treeTraversingParser.getBigIntegerValue();
            fail("Expected JsonParseException for non-numeric node.");
        } catch (JsonParseException e) {
            // Expected
        }
        treeTraversingParser.nextToken();
        try {
            treeTraversingParser.getBigDecimalValue();
            fail("Expected JsonParseException for non-numeric node.");
        } catch (JsonParseException e) {
            // Expected
        }
        treeTraversingParser.nextToken();
        try {
            treeTraversingParser.getNumberValue();
            fail("Expected JsonParseException for non-numeric node.");
        } catch (JsonParseException e) {
            // Expected
        }
        treeTraversingParser.nextToken();
        try {
            treeTraversingParser.getBinaryValue(null);
            fail("Expected JsonParseException for null variant.");
        } catch (JsonParseException e) {
            // Expected
        }
        treeTraversingParser.nextToken();
        try {
            treeTraversingParser.getBinaryValue(Base64Variant.STANDARD, null);
            fail("Expected JsonParseException for null output stream.");
        } catch (JsonParseException e) {
            // Expected
        }
        treeTraversingParser.nextToken();
        try {
            treeTraversingParser.readBinaryValue(null, new ByteArrayOutputStream());
            fail("Expected JsonParseException for null variant.");
        } catch (JsonParseException e) {
            // Expected
        }
        treeTraversingParser.nextToken();
        try {
            treeTraversingParser.readBinaryValue(Base64Variant.STANDARD, null);
            fail("Expected JsonParseException for null output stream.");
        } catch (JsonParseException e) {
            // Expected
        }

        // Test version
        treeTraversingParser = new TreeTraversingParser((JsonNode) objectMapper.createObjectNode(), objectMapper.getObjectCodec());
        Version version = treeTraversingParser.version();
        assertEquals(3.2, version.getMajorVersion());
        assertEquals(10, version.getMinorVersion());
        assertEquals(6, version.getSubVersion());
        assertEquals(7, version.getFeatures());
    }
}