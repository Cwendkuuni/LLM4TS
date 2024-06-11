package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TextNode;

/**
 * Test class for TreeTraversingParser.java
 */
public class TreeTraversingParserTest {

    @Test
    public void testTreeTraversingParser() throws IOException, JsonParseException {
        String json = "{\"a\":1,\"b\":[1,2],\"c\":3.14,\"d\":{\"e\":true,\"f\":[\"apple\",\"banana\"]}}";
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(json);

        TreeTraversingParser parser = new TreeTraversingParser(rootNode);

        // Test basic functionality
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals("a", parser.getCurrentName());
        assertEquals(1, parser.getIntValue());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("b", parser.getCurrentName());
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals(1, parser.getIntValue());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(2, parser.getIntValue());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("c", parser.getCurrentName());
        assertEquals(3.14, parser.getDoubleValue(), 0.01);
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("d", parser.getCurrentName());
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals("e", parser.getCurrentName());
        assertEquals(true, parser.getBooleanValue());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("f", parser.getCurrentName());
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals("apple", parser.getText());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("banana", parser.getText());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());

        // Test handling of empty objects and arrays
        JsonNode emptyObject = mapper.readTree("{}");
        TreeTraversingParser emptyObjectParser = new TreeTraversingParser(emptyObject);
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());

        JsonNode emptyArray = mapper.readTree("[]");
        TreeTraversingParser emptyArrayParser = new TreeTraversingParser(emptyArray);
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());

        // Test handling of null values
        JsonNode nullNode = mapper.readTree("{\"a\":null}");
        TreeTraversingParser nullNodeParser = new TreeTraversingParser(nullNode);
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals("a", parser.getCurrentName());
        assertNull(parser.getText());
        assertEquals(JsonToken.VALUE_NULL, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());

        // Test handling of text values
        JsonNode textNode = mapper.readTree("{\"a\":\"Hello, world!\"}");
        TreeTraversingParser textNodeParser = new TreeTraversingParser(textNode);
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals("a", parser.getCurrentName());
        assertEquals("Hello, world!", parser.getText());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());

        // Test handling of binary values
        JsonNode binaryNode = mapper.readTree("{\"a\":\"R\\u00E5sume\\u00E5r\\u00E5\"}");
        TreeTraversingParser binaryNodeParser = new TreeTraversingParser(binaryNode);
        byte[] binaryData = binaryNodeParser.getBinaryValue(Base64Variant.STANDARD);
        String decodedText = new String(binaryData, "UTF-8");
        assertEquals("Resumer\u00E4", decodedText);

        // Test handling of embedded objects
        JsonNode embeddedObject = mapper.readTree("{\"a\":{{\"b\":1, \"c\":2}}}");
        TreeTraversingParser embeddedObjectParser = new TreeTraversingParser(embeddedObject);
        JsonNode embeddedNode = embeddedObjectParser.getEmbeddedObject();
        assertEquals(1, embeddedNode.get("b").getIntValue());
        assertEquals(2, embeddedNode.get("c").getIntValue());

        // Test handling of missing fields
        JsonNode missingFieldNode = mapper.readTree("{\"a\":1,\"b\":[1,2],\"c\":3.14}");
        TreeTraversingParser missingFieldParser = new TreeTraversingParser(missingFieldParser, null);
        assertFalse(missingFieldParser.getCurrentName().equals("d"));
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());

        // Test handling of nested objects and arrays
        JsonNode nestedNode = mapper.readTree(
                "{\"a\":{\"b\":1,\"c\":[1,2,3]},\"d\":[{\"e\":1},{\"e\":2}]}");
        TreeTraversingParser nestedParser = new TreeTraversingParser(nestedNode);
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals("a", parser.getCurrentName());
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals("b", parser.getCurrentName());
        assertEquals(1, parser.getIntValue());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("c", parser.getCurrentName());
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals(1, parser.getIntValue());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(2, parser.getIntValue());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(3, parser.getIntValue());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        JsonNode nestedObject1 = parser.getEmbeddedObject();
        assertEquals(1, nestedObject1.get("e").getIntValue());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        JsonNode nestedObject2 = parser.getEmbeddedObject();
        assertEquals(2, nestedObject2.get("e").getIntValue());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());

        // Test handling of empty containers
        JsonNode emptyContainerArray = mapper.readTree("[]");
        TreeTraversingParser emptyContainerArrayParser = new TreeTraversingParser(emptyContainerArray);
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());

        JsonNode emptyContainerObject = mapper.readTree("{}");
        TreeTraversingParser emptyContainerObjectParser = new TreeTraversingParser(emptyContainerObject);
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());

        // Test handling of null containers
        JsonNode nullContainerArray = mapper.readTree("null");
        TreeTraversingParser nullContainerArrayParser = new TreeTraversingParser(nullContainerArray);
        assertEquals(JsonToken.START_NULL, parser.nextToken());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());

        JsonNode nullContainerObject = mapper.readTree("null");
        TreeTraversingParser nullContainerObjectParser = new TreeTraversingParser(nullContainerObject);
        assertEquals(JsonToken.START_NULL, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());

        // Test handling of recursive data structures
        JsonNode recursiveNode = mapper.readTree("{\"a\":{\"b\":[1,2,3],\"c\":{\"d\":4,\"e\":[5,6,7],\"f\":{\"g\":[8,9,10]}}}}");
        TreeTraversingParser recursiveParser = new TreeTraversingParser(recursiveNode);
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals("a", parser.getCurrentName());
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals("b", parser.getCurrentName());
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals(1, parser.getIntValue());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(2, parser.getIntValue());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(3, parser.getIntValue());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        assertEquals("c", parser.getCurrentName());
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals("d", parser.getCurrentName());
        assertEquals(4, parser.getIntValue());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("e", parser.getCurrentName());
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals(5, parser.getIntValue());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(6, parser.getIntValue());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(7, parser.getIntValue());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        assertEquals("f", parser.getCurrentName());
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals("g", parser.getCurrentName());
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals(8, parser.getIntValue());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(9, parser.getIntValue());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(10, parser.getIntValue());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());

        // Test handling of unsupported token types
        JsonNode unsupportedNode = mapper.readTree("{\"a\":null,\"b\":true,\"c\":[\"apple\",\"banana\"],\"d\":123}");
        TreeTraversingParser unsupportedParser = new TreeTraversingParser(unsupportedNode);
        try {
            unsupportedParser.getIntValue();
            fail("Expected JsonParseException when trying to get int value from boolean or string token");
        } catch (JsonParseException e) {
            // Expected
        }

        try {
            unsupportedParser.getBooleanValue();
            fail("Expected JsonParseException when trying to get boolean value from int or string token");
        } catch (JsonParseException e) {
            // Expected
        }

        try {
            unsupportedParser.getTextValue();
            fail("Expected JsonParseException when trying to get text value from int or boolean token");
        } catch (JsonParseException e) {
            // Expected
        }
    }

    // Helper class for testing NodeCursor functionality
    private static class NodeCursorTestHelper {
        static String currentName(NodeCursor cursor) {
            if (cursor instanceof NodeCursor.RootCursor) {
                return null;
            } else if (cursor instanceof NodeCursor.ArrayCursor) {
                NodeCursor.ArrayCursor arrayCursor = (NodeCursor.ArrayCursor) cursor;
                return arrayCursor.getCurrentName();
            } else if (cursor instanceof NodeCursor.ObjectCursor) {
                NodeCursor.ObjectCursor objectCursor = (NodeCursor.ObjectCursor) cursor;
                return objectCursor.getCurrentName();
            }
            throw new IllegalStateException();
        }

        static JsonNode currentNode(NodeCursor cursor) {
            if (cursor instanceof NodeCursor.RootCursor) {
                return null;
            } else if (cursor instanceof NodeCursor.ArrayCursor) {
                NodeCursor.ArrayCursor arrayCursor = (NodeCursor.ArrayCursor) cursor;
                return arrayCursor.currentNode();
            } else if (cursor instanceof NodeCursor.ObjectCursor) {
                NodeCursor.ObjectCursor objectCursor = (NodeCursor.ObjectCursor) cursor;
                return objectCursor.currentNode();
            }
            throw new IllegalStateException();
        }
    }

    private static class NodeCursorTestHelper$ObjectCursor extends NodeCursor.ObjectCursor {
        NodeCursorTestHelper$ObjectCursor(JsonNode node, JsonNode parent) {
            super(node, parent);
        }

        @Override
        public String getCurrentName() {
            return NodeCursorTestHelper.currentName(this);
        }

        @Override
        public JsonNode currentNode() {
            return NodeCursorTestHelper.currentNode(this);
        }
    }

    private static class NodeCursorTestHelper$ArrayCursor extends NodeCursor.ArrayCursor {
        NodeCursorTestHelper$ArrayCursor(JsonNode node, JsonNode parent) {
            super(node, parent);
        }

        @Override
        public String getCurrentName() {
            return NodeCursorTestHelper.currentName(this);
        }

        @Override
        public JsonNode currentNode() {
            return NodeCursorTestHelper.currentNode(this);
        }

        @Override
        public NodeCursor iterateChildren() {
            return new NodeCursorTestHelper$ArrayCursor(getArray(), getParent());
        }
    }
}