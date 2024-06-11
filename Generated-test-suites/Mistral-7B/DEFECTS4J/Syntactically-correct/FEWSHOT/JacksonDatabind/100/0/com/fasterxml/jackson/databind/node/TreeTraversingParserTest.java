package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.POJONode;
import com.fasterxml.jackson.databind.node.TextNode;

import org.junit.Test;

public class TreeTraversingParserTest {

    @Test
    public void testGetText() throws Exception {
        JsonNode node = new TextNode("foo");
        JsonParser parser = new TreeTraversingParser(node);
        assertEquals("foo", parser.getText());
        parser.nextToken(); // consume "foo"
        assertNull(parser.getText());
    }

    @Test
    public void testGetTextCharacters() throws Exception {
        JsonNode node = new TextNode("foo");
        JsonParser parser = new TreeTraversingParser(node);
        char[] chars = parser.getTextCharacters();
        assertEquals("foo".toCharArray(), chars);
        parser.nextToken(); // consume "foo"
        assertNull(parser.getTextCharacters());
    }

    @Test
    public void testGetTextLength() throws Exception {
        JsonNode node = new TextNode("foo");
        JsonParser parser = new TreeTraversingParser(node);
        assertEquals(3, parser.getTextLength());
        parser.nextToken(); // consume "foo"
        assertEquals(0, parser.getTextLength());
    }

    @Test
    public void testGetTextOffset() throws Exception {
        JsonNode node = new TextNode("foo");
        JsonParser parser = new TreeTraversingParser(node);
        assertEquals(0, parser.getTextOffset());
        parser.nextToken(); // consume "foo"
        assertEquals(0, parser.getTextOffset());
    }

    @Test
    public void testHasTextCharacters() throws Exception {
        JsonNode node = new TextNode("foo");
        JsonParser parser = new TreeTraversingParser(node);
        assertFalse(parser.hasTextCharacters());
        parser.nextToken(); // consume "foo"
        assertFalse(parser.hasTextCharacters());
    }

    @Test
    public void testGetNumberType() throws Exception {
        JsonNode node = new NumberNode(123);
        JsonParser parser = new TreeTraversingParser(node);
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.getNumberType());

        node = new NumberNode(123.45);
        parser = new TreeTraversingParser(node);
        assertEquals(JsonToken.VALUE_NUMBER_FLOAT, parser.getNumberType());
    }

    @Test
    public void testGetBigIntegerValue() throws Exception {
        JsonNode node = new NumberNode(BigInteger.valueOf(123));
        JsonParser parser = new TreeTraversingParser(node);
        assertEquals(BigInteger.valueOf(123), parser.getBigIntegerValue());

        node = new NumberNode(BigInteger.valueOf("-123"));
        parser = new TreeTraversingParser(node);
        assertEquals(BigInteger.valueOf(-123), parser.getBigIntegerValue());
    }

    @Test
    public void testGetDecimalValue() throws Exception {
        JsonNode node = new NumberNode(new BigDecimal("123.45"));
        JsonParser parser = new TreeTraversingParser(node);
        assertEquals(new BigDecimal("123.45"), parser.getDecimalValue());
    }

    @Test
    public void testGetDoubleValue() throws Exception {
        JsonNode node = new NumberNode(123.45);
        JsonParser parser = new TreeTraversingParser(node);
        assertEquals(123.45, parser.getDoubleValue(), 0.0);

        node = new NumberNode(Double.NaN);
        parser = new TreeTraversingParser(node);
        assertTrue(parser.isNaN());
    }

    @Test
    public void testGetFloatValue() throws Exception {
        JsonNode node = new NumberNode(123.45f);
        JsonParser parser = new TreeTraversingParser(node);
        assertEquals(123.45f, parser.getFloatValue(), 0.0f);

        node = new NumberNode(Float.NaN);
        parser = new TreeTraversingParser(node);
        assertTrue(parser.isNaN());
    }

    @Test
    public void testGetLongValue() throws Exception {
        JsonNode node = new NumberNode(123L);
        JsonParser parser = new TreeTraversingParser(node);
        assertEquals(123L, parser.getLongValue());

        node = new NumberNode(Long.MIN_VALUE);
        parser = new TreeTraversingParser(node);
        assertEquals(Long.MIN_VALUE, parser.getLongValue());

        node = new NumberNode(Long.MAX_VALUE);
        parser = new TreeTraversingParser(node);
        assertEquals(Long.MAX_VALUE, parser.getLongValue());
    }

    @Test
    public void testGetIntValue() throws Exception {
        JsonNode node = new NumberNode(123);
        JsonParser parser = new TreeTraversingParser(node);
        assertEquals(123, parser.getIntValue());

        node = new NumberNode(Integer.MIN_VALUE);
        parser = new TreeTraversingParser(node);
        assertEquals(Integer.MIN_VALUE, parser.getIntValue());

        node = new NumberNode(Integer.MAX_VALUE);
        parser = new TreeTraversingParser(node);
        assertEquals(Integer.MAX_VALUE, parser.getIntValue());
    }

    @Test
    public void testGetNumberValue() throws Exception {
        JsonNode node = new NumberNode(123);
        JsonParser parser = new TreeTraversingParser(node);
        assertEquals(123, parser.getNumberValue().longValue());

        node = new NumberNode(123.45);
        parser = new TreeTraversingParser(node);
        assertEquals(123.45, parser.getNumberValue().doubleValue(), 0.0);
    }

    @Test
    public void testGetEmbeddedObject() throws Exception {
        JsonNode node = new ObjectNode(new Object() {{
            put("foo", new TextNode("bar"));
        }});
        JsonParser parser = new TreeTraversingParser(node);

        assertEquals("bar", parser.getEmbeddedObject().toString());
        parser.nextToken(); // consume "bar"
        assertNull(parser.getEmbeddedObject());
    }

    @Test
    public void testIsNaN() throws Exception {
        JsonNode node = new NumberNode(Double.NaN);
        JsonParser parser = new TreeTraversingParser(node);
        assertTrue(parser.isNaN());

        node = new TextNode("NaN");
        parser = new TreeTraversingParser(node);
        assertFalse(parser.isNaN());
    }

    @Test
    public void testGetBinaryValue() throws Exception {
        JsonNode node = new BinaryNode(new byte[]{1, 2, 3});
        JsonParser parser = new TreeTraversingParser(node);
        byte[] result = parser.getBinaryValue(null);
        assertSame(result, new byte[]{1, 2, 3});
    }

    @Test
    public void testReadBinaryValue() throws Exception {
        JsonNode node = new BinaryNode(new byte[]{1, 2, 3});
        JsonParser parser = new TreeTraversingParser(node);
        OutputStream out = new ByteArrayOutputStream();
        int length = parser.readBinaryValue(null, out);
        byte[] result = out.toByteArray();
        assertSame(result, new byte[]{1, 2, 3});
        assertEquals(3, length);
    }

    @Test
    public void testNextToken() throws Exception {
        JsonNode node = new ObjectNode(new Object() {{
            put("foo", new ArrayNode(new TextNode("1"), new TextNode("2")));
            put("bar", new ObjectNode(new Object() {{
                put("baz", new TextNode("qux"));
            }}));
        }});
        JsonParser parser = new TreeTraversingParser(node);

        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals("foo", parser.getText());

        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals("1", parser.getText());

        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("2", parser.getText());

        assertEquals(JsonToken.END_ARRAY, parser.nextToken());

        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("bar", parser.getText());

        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals("baz", parser.getText());

        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("qux", parser.getText());

        assertEquals(JsonToken.END_OBJECT, parser.nextToken());

        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertNull(parser.getText());

        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
    }

    @Test
    public void testSkipChildren() throws Exception {
        JsonNode node = new ObjectNode(new Object() {{
            put("foo", new ArrayNode(new TextNode("1"), new TextNode("2")));
            put("bar", new ObjectNode(new Object() {{
                put("baz", new TextNode("qux"));
            }}));
        }});
        JsonParser parser = new TreeTraversingParser(node);

        parser.skipChildren();
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("bar", parser.getText());

        parser.skipChildren();
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("baz", parser.getText());

        parser.skipChildren();
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("qux", parser.getText());

        parser.skipChildren();
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());

        parser.skipChildren();
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertNull(parser.getText());

        parser.skipChildren();
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
    }
}