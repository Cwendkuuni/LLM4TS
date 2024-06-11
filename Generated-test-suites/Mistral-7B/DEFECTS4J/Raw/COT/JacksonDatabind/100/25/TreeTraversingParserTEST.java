package com.example.junit;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectCodec;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.BinaryNode;
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.NumberNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

import org.junit.Test;

public class TreeTraversingParserTest {

    @Test
    public void testEmptyParser() throws Exception {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertEquals(JsonToken.END_DOCUMENT, parser.nextToken());
        assertTrue(parser.isClosed());
    }

    @Test
    public void testValueParser() throws Exception {
        ObjectCodec objectCodec = null;
        JsonNode jsonNode = new ObjectNode(objectCodec);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.START_OBJECT, token);

        token = parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        assertEquals("", parser.getCurrentName());

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token);
        assertEquals("", parser.getText());

        token = parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, token);

        assertTrue(parser.isClosed());
    }

    @Test
    public void testArrayParser() throws Exception {
        ObjectCodec objectCodec = null;
        JsonNode jsonNode = new ArrayNode(objectCodec);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.START_ARRAY, token);

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, token);
        assertEquals(1, parser.getIntValue());

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_FLOAT, token);
        assertEquals(2.5, parser.getFloatValue(), 0.01);

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token);
        assertEquals("hello", parser.getText());

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_NULL, token);

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_BOOLEAN, token);
        assertEquals(true, parser.getBooleanValue());

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, token);
        ObjectNode embeddedObject = (ObjectNode) parser.getEmbeddedObject();
        assertEquals(JsonToken.START_OBJECT, embeddedObject.nextToken());
        assertEquals("key", embeddedObject.getCurrentName());
        assertEquals("value", embeddedObject.getText());
        assertEquals(JsonToken.END_OBJECT, embeddedObject.nextToken());

        token = parser.nextToken();
        assertEquals(JsonToken.END_ARRAY, token);

        assertTrue(parser.isClosed());
    }

    @Test(expected = IOException.class)
    public void testNullParser() throws Exception {
        ObjectCodec objectCodec = null;
        TreeTraversingParser parser = new TreeTraversingParser(null);
        parser.nextToken();
    }

    @Test(expected = JsonParseException.class)
    public void testInvalidJsonParser() throws Exception {
        ObjectCodec objectCodec = null;
        JsonNode jsonNode = new ObjectNode(objectCodec);
        jsonNode.put("key", new TextNode("invalid json"));
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken();
    }

    @Test
    public void testCloseParser() throws Exception {
        ObjectCodec objectCodec = null;
        JsonNode jsonNode = new ObjectNode(objectCodec);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken();
        parser.close();
        assertEquals(JsonToken.END_DOCUMENT, parser.nextToken());
        assertTrue(parser.isClosed());
    }

    @Test(expected = JsonParseException.class)
    public void testParseBeyondEndOfJson() throws Exception {
        ObjectCodec objectCodec = null;
        JsonNode jsonNode = new ObjectNode(objectCodec);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken();
        parser.nextToken(); // consume start object
        parser.nextToken(); // consume field name
        parser.nextToken(); // consume value
        parser.nextToken(); // consume end object
        parser.nextToken(); // consume empty array
        parser.nextToken(); // consume end array
        parser.nextToken(); // consume end document
        parser.nextToken(); // consume beyond end document
    }

    @Test
    public void testParseBinaryData() throws Exception {
        ObjectCodec objectCodec = null;
        JsonNode jsonNode = new ObjectNode(objectCodec).put("binaryData", new BinaryNode(new TextNode("dGVzdA=="), Base64Variants.STANDARD));
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        byte[] binaryData = parser.getBinaryValue(Base64Variants.STANDARD);
        assertArrayEquals(new byte[] { 32, 101, 115, 116, 32, 83, 111, 117, 115 }, binaryData);
    }

    @Test
    public void testParseNestedObject() throws Exception {
        ObjectCodec objectCodec = null;
        JsonNode jsonNode = new ObjectNode(objectCodec).put("obj", new ObjectNode(objectCodec)
                .put("key1", new NumberNode(1))
                .put("key2", new TextNode("value2"))
                .put("key3", new BooleanNode(true))
        );
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals("obj", parser.getCurrentName());

        parser.nextToken(); // consume start object of nested object
        assertEquals("key1", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken().getValueType());
        assertEquals(1, parser.getIntValue());

        parser.nextToken(); // consume field name of next nested object
        assertEquals("key2", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken().getValueType());
        assertEquals("value2", parser.getText());

        parser.nextToken(); // consume end object of nested object
        assertEquals("key3", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_BOOLEAN, parser.nextToken().getValueType());
        assertEquals(true, parser.getBooleanValue());

        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertTrue(parser.isClosed());
    }

    @Test
    public void testParseNestedArray() throws Exception {
        ObjectCodec objectCodec = null;
        JsonNode jsonNode = new ObjectNode(objectCodec).put("arr", new ArrayNode(objectCodec)
                .add(new TextNode("item1"))
                .add(new NumberNode(2))
                .add(new ArrayNode(objectCodec)
                        .add(new TextNode("item3"))
                        .add(new NumberNode(3))
                )
        );
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals("arr", parser.getCurrentName());

        parser.nextToken(); // consume start array of nested array
        assertEquals("item1", parser.getText());

        parser.nextToken(); // consume value number
        assertEquals(2, parser.getIntValue());

        parser.nextToken(); // consume start array of nested array
        assertEquals("item3", parser.getText());

        parser.nextToken(); // consume value number
        assertEquals(3, parser.getIntValue());

        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertTrue(parser.isClosed());
    }
}