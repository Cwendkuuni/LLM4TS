package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.core.JsonBuffer;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TextNode;

public class TreeTraversingParserTest {
    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testEmpty() throws IOException {
        JsonNode node = mapper.readTree("{}");
        TreeTraversingParser parser = new TreeTraversingParser(node);
        parser.nextToken();
        assertNull(parser.getText());
        parser.close();
    }

    @Test
    public void testString() throws IOException {
        JsonNode node = mapper.readTree("{\"foo\": \"bar\"}");
        TreeTraversingParser parser = new TreeTraversingParser(node);
        assertEquals("bar", parser.getText());
        parser.nextToken();
        assertNull(parser.getText());
        parser.close();
    }

    @Test
    public void testNumberInt() throws IOException {
        JsonNode node = mapper.readTree("{1}");
        TreeTraversingParser parser = new TreeTraversingParser(node);
        assertEquals("1", parser.getText());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken().getValue());
        assertEquals(Integer.valueOf(1), parser.getNumberValue());
        parser.nextToken();
        assertNull(parser.getText());
        parser.close();
    }

    @Test
    public void testNumberLong() throws IOException {
        JsonNode node = mapper.readTree("{123}");
        TreeTraversingParser parser = new TreeTraversingParser(node);
        assertEquals("123", parser.getText());
        assertEquals(JsonToken.VALUE_NUMBER_LONG, parser.nextToken().getValue());
        assertEquals(Long.valueOf(123), parser.getNumberValue());
        parser.nextToken();
        assertNull(parser.getText());
        parser.close();
    }

    @Test
    public void testNumberFloat() throws IOException {
        JsonNode node = mapper.readTree("{3.14}");
        TreeTraversingParser parser = new TreeTraversingParser(node);
        assertEquals("3.14", parser.getText());
        assertEquals(JsonToken.VALUE_NUMBER_FLOAT, parser.nextToken().getValue());
        assertEquals(Float.valueOf((float) 3.14), parser.getNumberValue());
        parser.nextToken();
        assertNull(parser.getText());
        parser.close();
    }

    @Test
    public void testNumberDouble() throws IOException {
        JsonNode node = mapper.readTree("{3.14159265}");
        TreeTraversingParser parser = new TreeTraversingParser(node);
        assertEquals("3.14159265", parser.getText());
        assertEquals(JsonToken.VALUE_NUMBER_FLOAT, parser.nextToken().getValue());
        assertEquals(Double.valueOf(3.14159265), parser.getNumberValue());
        parser.nextToken();
        assertNull(parser.getText());
        parser.close();
    }

    @Test
    public void testNumberDecimal() throws IOException {
        JsonNode node = mapper.readTree("{1234567890.123456}");
        TreeTraversingParser parser = new TreeTraversingParser(node);
        assertEquals("1234567890.123456", parser.getText());
        assertEquals(JsonToken.VALUE_NUMBER_FLOAT, parser.nextToken().getValue());
        assertEquals(new BigDecimal("1234567890.123456"), parser.getDecimalValue());
        parser.nextToken();
        assertNull(parser.getText());
        parser.close();
    }

    @Test
    public void testNumberBigInteger() throws IOException {
        JsonNode node = mapper.readTree("{123456789012345678901234567890}");
        TreeTraversingParser parser = new TreeTraversingParser(node);
        assertEquals("123456789012345678901234567890", parser.getText());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken().getValue());
        assertEquals(new BigInteger("123456789012345678901234567890"), parser.getBigIntegerValue());
        parser.nextToken();
        assertNull(parser.getText());
        parser.close();
    }

    @Test
    public void testNull() throws IOException {
        JsonNode node = mapper.readTree("{null}");
        TreeTraversingParser parser = new TreeTraversingParser(node);
        parser.nextToken();
        assertEquals(JsonToken.VALUE_NULL, parser.nextToken().getValue());
        parser.nextToken();
        assertNull(parser.getText());
        parser.close();
    }

    @Test
    public void testBooleanTrue() throws IOException {
        JsonNode node = mapper.readTree("{true}");
        TreeTraversingParser parser = new TreeTraversingParser(node);
        parser.nextToken();
        assertEquals(JsonToken.VALUE_BOOLEAN, parser.nextToken().getValue());
        assertTrue(parser.getBooleanValue());
        parser.nextToken();
        assertNull(parser.getText());
        parser.close();
    }

    @Test
    public void testBooleanFalse() throws IOException {
        JsonNode node = mapper.readTree("{false}");
        TreeTraversingParser parser = new TreeTraversingParser(node);
        parser.nextToken();
        assertEquals(JsonToken.VALUE_BOOLEAN, parser.nextToken().getValue());
        assertFalse(parser.getBooleanValue());
        parser.nextToken();
        assertNull(parser.getText());
        parser.close();
    }

    @Test
    public void testArray() throws IOException {
        JsonNode node = mapper.readTree("[1, 2, 3]");
        TreeTraversingParser parser = new TreeTraversingParser(node);
        assertEquals("[1, 2, 3]", parser.getText());
        assertEquals(JsonToken.START_ARRAY, parser.nextToken().getValue());
        parser.nextToken();
        assertEquals(Integer.valueOf(1), parser.getNumberValue());
        parser.nextToken();
        assertEquals(Integer.valueOf(2), parser.getNumberValue());
        parser.nextToken();
        assertEquals(Integer.valueOf(3), parser.getNumberValue());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken().getValue());
        parser.nextToken();
        assertNull(parser.getText());
        parser.close();
    }

    @Test
    public void testObject() throws IOException {
        JsonNode node = mapper.readTree("{\"foo\": 1, \"bar\": 2}");
        TreeTraversingParser parser = new TreeTraversingParser(node);
        assertEquals("{\"foo\": 1, \"bar\": 2}", parser.getText());
        assertEquals(JsonToken.START_OBJECT, parser.nextToken().getValue());
        parser.nextToken();
        assertEquals("foo", parser.getCurrentName());
        assertEquals(Integer.valueOf(1), parser.getNumberValue());
        parser.nextToken();
        assertEquals("bar", parser.getCurrentName());
        assertEquals(Integer.valueOf(2), parser.getNumberValue());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken().getValue());
        parser.nextToken();
        assertNull(parser.getText());
        parser.close();
    }

    @Test
    public void testArrayAndObject() throws IOException {
        JsonNode node = mapper.readTree("[1, {\"foo\": 1, \"bar\": 2}]");
        TreeTraversingParser parser = new TreeTraversingParser(node);
        assertEquals("[1, {\"foo\": 1, \"bar\": 2}]", parser.getText());
        assertEquals(JsonToken.START_ARRAY, parser.nextToken().getValue());
        parser.nextToken();
        assertEquals(Integer.valueOf(1), parser.getNumberValue());
        parser.nextToken();
        assertEquals(JsonToken.START_OBJECT, parser.nextToken().getValue());
        parser.nextToken();
        assertEquals("foo", parser.getCurrentName());
        assertEquals(Integer.valueOf(1), parser.getNumberValue());
        parser.nextToken();
        assertEquals("bar", parser.getCurrentName());
        assertEquals(Integer.valueOf(2), parser.getNumberValue());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken().getValue());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken().getValue());
        parser.nextToken();
        assertNull(parser.getText());
        parser.close();
    }

    @Test
    public void testBinary() throws IOException {
        JsonNode node = mapper.readTree("{\"foo\": \"RnJhZGllLCBmaWxlICgkCiAgICA8Y2lyY2xlLXdpZHRoOj4KICAgICAgICA8dGVjaG5vbG9neSRpbWVvdXQoJ2Jhc2U2ZXllZCBmb3Igc291cmNlcyBvZiBiZSArIGZpbGw9ImEiIGN5bnQtdG8pIHsK\"]");
        TreeTraversingParser parser = new TreeTraversingParser(node);
        byte[] data = parser.getBinaryValue(Base64Variant.BASE64_STANDARD);
        assertEquals("foo", parser.getText());
        assertEquals(JsonNode.VALUE_TEXT_STRING, parser.nextToken().getValue());
        assertEquals("RnJhZGllLCBmaWxlICgkCiAgICA8Y2lyY2xlLXdpZHRoOj4KICAgICAgICA8dGVjaG5vbG9neSRpbWVvdXQoJ2Jhc2U2ZXllZCBmb3Igc291cmNlcyBvZiBiZSArIGZpbGw9ImEiIGN5bnQtdG8pIHsK", parser.getText());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken().getValue());
        assertEquals(TextNode.class, parser.getCurrentNode().getClass());
        assertEquals(data.length, parser.readBinaryValue(Base64Variant.BASE64_STANDARD, new JsonBuffer(1024)).length);
        parser.nextToken();
        assertNull(parser.getText());
        parser.close();
    }
}