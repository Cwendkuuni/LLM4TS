package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParser.NumberType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.POJONode;
import com.fasterxml.jackson.databind.node.TextNode;

import org.junit.Test;

public class TreeTraversingParserTest {

    @Test
    public void testGetText() throws Exception {
        JsonNode node = TextNode.valueOf("foo");
        TreeTraversingParser parser = new TreeTraversingParser(node);
        assertEquals("foo", parser.getText());
    }

    @Test
    public void testGetTextCharacters() throws Exception {
        JsonNode node = TextNode.valueOf("foo");
        TreeTraversingParser parser = new TreeTraversingParser(node);
        assertArrayEquals(new char[]{'f', 'o', 'o'}, parser.getTextCharacters());
    }

    @Test
    public void testGetTextLength() throws Exception {
        JsonNode node = TextNode.valueOf("foo");
        TreeTraversingParser parser = new TreeTraversingParser(node);
        assertEquals(3, parser.getTextLength());
    }

    @Test
    public void testGetTextOffset() throws Exception {
        JsonNode node = TextNode.valueOf("foo");
        TreeTraversingParser parser = new TreeTraversingParser(node);
        assertEquals(0, parser.getTextOffset());
    }

    @Test
    public void testHasTextCharacters() throws Exception {
        JsonNode node = TextNode.valueOf("foo");
        TreeTraversingParser parser = new TreeTraversingParser(node);
        assertFalse(parser.hasTextCharacters());
    }

    @Test
    public void testGetNumberType() throws Exception {
        JsonNode node = NumberNode.value(123);
        TreeTraversingParser parser = new TreeTraversingParser(node);
        assertEquals(NumberType.INT, parser.getNumberType());
    }

    @Test
    public void testGetBigIntegerValue() throws Exception {
        JsonNode node = NumberNode.value(BigInteger.valueOf(12345));
        TreeTraversingParser parser = new TreeTraversingParser(node);
        assertEquals(BigInteger.valueOf(12345), parser.getBigIntegerValue());
    }

    @Test
    public void testGetDecimalValue() throws Exception {
        JsonNode node = NumberNode.value(new BigDecimal("123.45"));
        TreeTraversingParser parser = new TreeTraversingParser(node);
        assertEquals(new BigDecimal("123.45"), parser.getDecimalValue());
    }

    @Test
    public void testGetDoubleValue() throws Exception {
        JsonNode node = NumberNode.value(123.45);
        TreeTraversingParser parser = new TreeTraversingParser(node);
        assertEquals(123.45, parser.getDoubleValue(), 0.001);
    }

    @Test
    public void testGetFloatValue() throws Exception {
        JsonNode node = NumberNode.value((float) 123.45);
        TreeTraversingParser parser = new TreeTraversingParser(node);
        assertEquals(123.45f, parser.getFloatValue(), 0.001f);
    }

    @Test
    public void testGetLongValue() throws Exception {
        JsonNode node = NumberNode.value(12345L);
        TreeTraversingParser parser = new TreeTraversingParser(node);
        assertEquals(12345L, parser.getLongValue());
    }

    @Test
    public void testGetIntValue() throws Exception {
        JsonNode node = NumberNode.value(123);
        TreeTraversingParser parser = new TreeTraversingParser(node);
        assertEquals(123, parser.getIntValue());
    }

    @Test
    public void testGetNumberValue() throws Exception {
        JsonNode node = NumberNode.value(123.45);
        TreeTraversingParser parser = new TreeTraversingParser(node);
        assertEquals(123.45, parser.getNumberValue().doubleValue(), 0.001);
    }

    @Test
    public void testGetEmbeddedObject() throws Exception {
        JsonNode root = TextNode.valueOf("{\"name\":\"John\", \"age\":30}");
        TreeTraversingParser parser = new TreeTraversingParser(root);
        JsonNode objNode = parser.nextToken();
        assertEquals(JsonToken.START_OBJECT, objNode.asToken().getTokenId());
        POJONode obj = parser.nextValue();
        assertEquals("John", obj.get("name").asText());
        assertEquals(30, obj.get("age").asInt());
        assertNull(parser.getEmbeddedObject());
    }

    @Test
    public void testIsNaN() throws Exception {
        JsonNode node = NumberNode.value(Double.NaN);
        TreeTraversingParser parser = new TreeTraversingParser(node);
        assertTrue(parser.isNaN());
    }

    @Test
    public void testGetBinaryValue() throws Exception {
        JsonNode node = BinaryNode.instance(null, new byte[]{0x61, 0x62, 0x63});
        TreeTraversingParser parser = new TreeTraversingParser(node);
        byte[] result = parser.getBinaryValue(Base64Variant.STANDARD);
        assertArrayEquals(new byte[]{'a', 'b', 'c'}, result);
    }

    @Test
    public void testReadBinaryValue() throws Exception {
        JsonNode node = BinaryNode.instance(null, new byte[]{0x61, 0x62, 0x63});
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        TreeTraversingParser parser = new TreeTraversingParser(node);
        int length = parser.readBinaryValue(Base64Variant.STANDARD, output);
        assertEquals(3, length);
        assertArrayEquals(new byte[]{'a', 'b', 'c'}, output.toByteArray());
    }

    @Test
    public void testNextToken() throws Exception {
        JsonNode root = TextNode.valueOf("{\"name\":\"John\", \"age\":30}");
        TreeTraversingParser parser = new TreeTraversingParser(root);
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.START_OBJECT, token.getTokenId());
        parser.nextValue(); // name
        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token.getTokenId());
        assertEquals("John", parser.getText());
        token = parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token.getTokenId());
        assertEquals("age", parser.getText());
        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_INT, token.getTokenId());
        assertEquals(30, parser.getIntValue());
        token = parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, token.getTokenId());
        assertNull(parser.nextToken());
    }

    @Test
    public void testSkipChildren() throws Exception {
        JsonNode root = TextNode.valueOf("{\"name\":\"John\", \"age\":30, \"hobbies\":[\"reading\", \"painting\"]}");
        TreeTraversingParser parser = new TreeTraversingParser(root);
        parser.nextToken(); // START_OBJECT
        parser.nextValue(); // name
        parser.nextToken(); // FIELD_NAME: "name"
        parser.nextValue(); // "John"
        parser.nextToken(); // FIELD_NAME: "age"
        parser.nextValue(); // 30
        parser.nextToken(); // END_OBJECT
        parser.nextToken(); // START_ARRAY
        parser.nextValue(); // "reading"
        parser.nextToken(); // STRING
        parser.skipChildren();
        parser.nextToken(); // "painting"
        parser.nextToken(); // STRING
        parser.nextToken(); // END_ARRAY
        assertNull(parser.nextToken());
    }

    @Test
    public void testIterateThroughArray() throws Exception {
        JsonNode root = TextNode.valueOf("[1, 2, 3]");
        TreeTraversingParser parser = new TreeTraversingParser(root);
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.START_ARRAY, token.getTokenId());
        for (int i = 0; i < 3; i++) {
            JsonToken numberToken = parser.nextToken();
            assertEquals(JsonToken.VALUE_NUMBER_INT, numberToken.getTokenId());
            assertEquals(i + 1, parser.getIntValue());
        }
        token = parser.nextToken();
        assertEquals(JsonToken.END_ARRAY, token.getTokenId());
        assertNull(parser.nextToken());
    }

    @Test
    public void testIterateThroughObject() throws Exception {
        JsonNode root = TextNode.valueOf("{\"name\":\"John\", \"age\":30, \"city\":\"New York\"}");
        TreeTraversingParser parser = new TreeTraversingParser(root);
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.START_OBJECT, token.getTokenId());
        for (String fieldName : new String[]{"name", "age", "city"}) {
            JsonToken fieldNameToken = parser.nextToken();
            assertEquals(JsonToken.FIELD_NAME, fieldNameToken.getTokenId());
            assertEquals(fieldName, parser.getText());
            JsonToken valueToken = parser.nextToken();
            assertEquals(JsonToken.VALUE_STRING, valueToken.getTokenId());
            assertEquals(parser.getText(), parser.getText()); // Check if value is not changed
            parser.nextToken(); // END_OBJECT
        }
        assertNull(parser.nextToken());
    }

    @Test
    public void testClose() throws Exception {
        JsonNode root = TextNode.valueOf("{\"name\":\"John\", \"age\":30, \"city\":\"New York\"}");
        TreeTraversingParser parser = new TreeTraversingParser(root);
        parser.nextToken();
        parser.nextValue();
        parser.nextToken();
        parser.nextValue();
        parser.nextToken();
        parser.nextValue();
        parser.nextToken();
        parser.close();
        JsonToken token = parser.nextToken();
        assertNull(token);
    }
}