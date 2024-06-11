package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.ParserMinimalBase;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonParser;
import com.fasterxml.jackson.databind.node.BaseTestNodes;
import com.fasterxml.jackson.databind.node.TextNode;

public class TreeTraversingParserTest {

    private static JsonNode createTestNode(JsonNodeType type, Object value) {
        if (type.isValueNodeType()) {
            return new TextNode(value.toString());
        } else if (type.isContainerNodeType()) {
            if (type == JsonNodeType.ARRAY) {
                return new TextNode("[]");
            } else { // JSON object
                return new BaseTestNodes.ObjectNodeBuilder()
                        .add("key", new TextNode("value"))
                        .build();
            }
        }
        throw new IllegalArgumentException("Unsupported node type: " + type);
    }

    @Test
    public void testEmptyParser() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertEquals(JsonToken.END_DOCUMENT, parser.nextToken());
        assertTrue(parser.isClosed());
    }

    @Test
    public void testValueNode() throws IOException {
        JsonNode node = createTestNode(JsonNodeType.VALUE, "hello");
        TreeTraversingParser parser = new TreeTraversingParser(node);
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("hello", parser.getText());
        assertEquals("hello", parser.getText().trim());
        assertEquals("hello", parser.getTextCharacters()[0]);
        assertEquals('h', parser.getTextCharacters()[0]);
        assertEquals(1, parser.getTextLength());
        assertEquals(0, parser.getTextOffset());
        assertFalse(parser.hasTextCharacters());
        assertNull(parser.getEmbeddedObject());
        assertFalse(parser.isNaN());
        assertTrue(parser.isClosed());
    }

    @Test
    public void testArrayNode() throws IOException {
        JsonNode node = createTestNode(JsonNodeType.ARRAY, null);
        TreeTraversingParser parser = new TreeTraversingParser(node);
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertFalse(parser.isClosed());

        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("hello", parser.getText());
        assertEquals("hello", parser.getText().trim());
        assertEquals("hello", parser.getTextCharacters()[0]);
        assertEquals('h', parser.getTextCharacters()[0]);
        assertEquals(1, parser.getTextLength());
        assertEquals(0, parser.getTextOffset());
        assertFalse(parser.hasTextCharacters());
        assertNull(parser.getEmbeddedObject());
        assertFalse(parser.isNaN());

        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        assertTrue(parser.isClosed());
    }

    @Test
    public void testObjectNode() throws IOException {
        JsonNode node = createTestNode(JsonNodeType.OBJECT, null);
        TreeTraversingParser parser = new TreeTraversingParser(node);
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertFalse(parser.isClosed());

        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("key", parser.getCurrentName());
        assertEquals("key", parser.getText());
        assertEquals("key", parser.getText().trim());
        assertEquals("key", parser.getTextCharacters()[0]);
        assertEquals('k', parser.getTextCharacters()[0]);
        assertEquals(3, parser.getTextLength());
        assertEquals(0, parser.getTextOffset());
        assertFalse(parser.hasTextCharacters());

        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("value", parser.getText());
        assertEquals("value", parser.getText().trim());
        assertEquals("value", parser.getTextCharacters()[0]);
        assertEquals('v', parser.getTextCharacters()[0]);
        assertEquals(5, parser.getTextLength());
        assertEquals(3, parser.getTextOffset());
        assertFalse(parser.hasTextCharacters());

        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertTrue(parser.isClosed());
    }

    @Test
    public void testComplexTree() throws IOException {
        String json = "{\"key1\": 1, \"key2\": [1, 2, 3], \"key3\": {}}";
        JsonParser parser = new JsonFactory().createParser(json);
        TreeTraversingParser treeParser = new TreeTraversingParser(parser.readValueAsTree());

        assertEquals(JsonToken.START_OBJECT,
                treeParser.nextToken()); // Start object
        assertEquals("key1", treeParser.getCurrentName());
        assertEquals("key1", treeParser.getText());
        assertEquals(JsonToken.VALUE_NUMBER_INT, treeParser.nextToken());
        assertEquals(1, treeParser.getIntValue());

        assertEquals(JsonToken.FIELD_NAME, treeParser.nextToken());
        assertEquals("key2", treeParser.getCurrentName());
        assertEquals("key2", treeParser.getText());
        assertEquals(JsonToken.START_ARRAY, treeParser.nextToken());

        assertEquals(JsonToken.VALUE_NUMBER_INT, treeParser.nextToken());
        assertEquals(1, treeParser.getIntValue());

        assertEquals(JsonToken.VALUE_NUMBER_INT, treeParser.nextToken());
        assertEquals(2, treeParser.getIntValue());

        assertEquals(JsonToken.VALUE_NUMBER_INT, treeParser.nextToken());
        assertEquals(3, treeParser.getIntValue());

        assertEquals(JsonToken.END_ARRAY, treeParser.nextToken());

        assertEquals(JsonToken.FIELD_NAME, treeParser.nextToken());
        assertEquals("key3", treeParser.getCurrentName());
        assertEquals("key3", treeParser.getText());
        assertEquals(JsonToken.START_OBJECT, treeParser.nextToken());
        assertEquals(JsonToken.END_OBJECT, treeParser.nextToken());

        assertEquals(JsonToken.END_OBJECT, treeParser.nextToken()); // End object
        assertTrue(treeParser.isClosed());
    }

    @Test
    public void testTraverseObject() throws IOException {
        String json = "{\"key1\": 1, \"key2\": [1, 2, 3], \"key3\": {}}";
        JsonParser parser = new JsonFactory().createParser(json);
        TreeTraversingParser treeParser = new TreeTraversingParser(parser.readValueAsTree());

        treeParser.traverse(new TreeTraversingParser.TreeTraverser() {
            private int count = 0;

            @Override
            public void onFieldName(String name) {
                assertEquals("key" + ++count, name);
            }

            @Override
            public void onValueString(String value) {
                assertNotNull(value);
            }

            @Override
            public void onValueInteger(int value) {
                assertEquals(count, value);
            }

            @Override
            public void onValueNumber(Number value) {
                assertEquals(count, value.intValue());
            }

            @Override
            public void onValueArray() {
                fail();
            }

            @Override
            public void onValueObject() {
                fail();
            }

            @Override
            public void onValueNumberInt() {
                fail();
            }

            @Override
            public void onValueNumberLong() {
                fail();
            }

            @Override
            public void onValueNumberFloat() {
                fail();
            }

            @Override
            public void onValueNumberDouble() {
                fail();
            }

            @Override
            public void onValueText() {
                fail();
            }

            @Override
            public void onValueBinary() {
                fail();
            }

            @Override
            public void onValueNull() {
                fail();
            }

            @Override
            public void onValueBoolean(boolean value) {
                fail();
            }

            @Override
            public void onValueDecimal(BigDecimal value) {
                fail();
            }

            @Override
            public void onValueBigInteger(BigInteger value) {
                fail();
            }

            @Override
            public void onValuePointer() {
                fail();
            }
        });

        assertEquals(4, treeParser.getParsingContext().pathSize());
        assertTrue(treeParser.isClosed());
    }

    @Test
    public void testTraverseArray() throws IOException {
        String json = "[null, 1, [2, 3], 4]";
        JsonParser parser = new JsonFactory().createParser(json);
        TreeTraversingParser treeParser = new TreeTraversingParser(parser.readValueAsTree());

        treeParser.traverse(new TreeTraversingParser.TreeTraverser() {
            private int count = 0;

            @Override
            public void onValueNull() {
                assertEquals(0, count++);
            }

            @Override
            public void onValueInteger(int value) {
                assertEquals(count++, value);
            }

            @Override
            public void onValueArray() {
                assertEquals(3, count++);
            }

            @Override
            public void onFieldName(String name) {
                fail();
            }

            @Override
            public void onValueString(String value) {
                fail();
            }

            @Override
            public void onValueObject() {
                fail();
            }

            @Override
            public void onValueNumberInt() {
                fail();
            }

            @Override
            public void onValueNumberLong() {
                fail();
            }

            @Override
            public void onValueNumberFloat() {
                fail();
            }

            @Override
            public void onValueNumberDouble() {
                fail();
            }

            @Override
            public void onValueText() {
                fail();
            }

            @Override
            public void onValueBinary() {
                fail();
            }

            @Override
            public void onValuePointer() {
                fail();
            }

            @Override
            public void onValueDecimal(BigDecimal value) {
                fail();
            }

            @Override
            public void onValueBigInteger(BigInteger value) {
                fail();
            }

            @Override
            public void onValueBoolean(boolean value) {
                fail();
            }
        });

        assertEquals(5, treeParser.getParsingContext().pathSize());
        assertTrue(treeParser.isClosed());
    }

    private void _throwInternal() {
        throw new RuntimeException("Internal error in parser");
    }
}