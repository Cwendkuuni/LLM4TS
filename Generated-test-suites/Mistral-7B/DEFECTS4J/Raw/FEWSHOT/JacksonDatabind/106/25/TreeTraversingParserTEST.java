package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;

import org.junit.Test;

public class TreeTraversingParserTest {

    @Test
    public void testEmptyParser() {
        JsonParser parser = new TreeTraversingParser(null);
        assertNull(parser.nextToken());
        parser.close();
    }

    @Test
    public void testParserWithRootNode() {
        JsonParser parser = new TreeTraversingParser(Json.parse("{}"));
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertNull(parser.nextToken());
        parser.close();
    }

    @Test
    public void testParserWithArrayNode() {
        JsonParser parser = new TreeTraversingParser(Json.parse("[1, 2, 3]"));
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals(JsonToken.VALUE_INT, parser.nextToken());
        assertEquals(1, parser.getIntValue());
        assertEquals(JsonToken.VALUE_INT, parser.nextToken());
        assertEquals(2, parser.getIntValue());
        assertEquals(JsonToken.VALUE_INT, parser.nextToken());
        assertEquals(3, parser.getIntValue());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        assertNull(parser.nextToken());
        parser.close();
    }

    @Test
    public void testParserWithObjectNode() {
        JsonParser parser = new TreeTraversingParser(Json.parse("{\"a\":1, \"b\":2}"));
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("a", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_INT, parser.nextToken());
        assertEquals(1, parser.getIntValue());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("b", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_INT, parser.nextToken());
        assertEquals(2, parser.getIntValue());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertNull(parser.nextToken());
        parser.close();
    }

    @Test
    public void testParserWithMixedNodes() {
        JsonParser parser = new TreeTraversingParser(Json.parse("{1: [\"a\", 2], \"b\": 3}"));
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("1", parser.getCurrentName());
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("a", parser.getText());
        assertEquals(JsonToken.VALUE_INT, parser.nextToken());
        assertEquals(2, parser.getIntValue());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("b", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_INT, parser.nextToken());
        assertEquals(3, parser.getIntValue());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertNull(parser.nextToken());
        parser.close();
    }

    @Test
    public void testParserWithNullNode() {
        JsonParser parser = new TreeTraversingParser(Json.parse("{null: null}"));
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("null", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_NULL, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertNull(parser.nextToken());
        parser.close();
    }

    @Test
    public void testParserWithBooleanNode() {
        JsonParser parser = new TreeTraversingParser(Json.parse("{true: true, false: false}"));
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("true", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_BOOLEAN, parser.nextToken());
        assertTrue(parser.getBooleanValue());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("false", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_BOOLEAN, parser.nextToken());
        assertFalse(parser.getBooleanValue());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertNull(parser.nextToken());
        parser.close();
    }

    @Test
    public void testParserWithNumberNode() {
        JsonParser parser = new TreeTraversingParser(Json.parse("{1.0: 1.0, -1.0: -1.0, 3.14: 3.14}"));
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("1.0", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_NUMBER_FLOAT, parser.nextToken());
        assertEquals(1.0, parser.getDoubleValue(), 0.0);
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("
ef{-1.0", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_NUMBER_FLOAT, parser.nextToken());
        assertEquals(-1.0, parser.getDoubleValue(), 0.0);
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("3.14", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_NUMBER_FLOAT, parser.nextToken());
        assertEquals(3.14, parser.getDoubleValue(), 0.0);
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertNull(parser.nextToken());
        parser.close();
    }

    @Test
    public void testParserWithStringNode() {
        JsonParser parser = new TreeTraversingParser(Json.parse("{\"foo\": \"bar\"}"));
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("foo", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("bar", parser.getText());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertNull(parser.nextToken());
        parser.close();
    }

    @Test
    public void testParserWithBinaryNode() {
        JsonParser parser = new TreeTraversingParser(Json.parse("{\"binary\": [0xFF, 0x01, 0x02]}"));
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("binary", parser.getCurrentName());
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        byte[] data = parser.getBinaryValue(Base64Variants.STANDARD);
        assertEquals(3, data.length);
        assertEquals((byte) 0xFF, data[0]);
        assertEquals((byte) 0x01, data[1]);
        assertEquals((byte) 0x02, data[2]);
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertNull(parser.nextToken());
        parser.close();
    }
}