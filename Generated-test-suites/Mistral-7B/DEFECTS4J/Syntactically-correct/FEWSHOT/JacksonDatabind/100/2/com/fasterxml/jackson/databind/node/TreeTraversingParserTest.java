package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;

public class TreeTraversingParserTest {

    @Test
    public void testEmptyParser() {
        JsonParser parser = new TreeTraversingParser(null);
        assertEquals(JsonToken.END_DOCUMENT, parser.nextToken());
        assertEquals(JsonToken.END_DOCUMENT, parser.nextToken());
        assertNull(parser.getText());
        parser.close();
    }

    @Test
    public void testSimpleValueParser() {
        JsonParser parser = new TreeTraversingParser(Json.value("42"));
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals("42", parser.getText());
        assertEquals(42L, parser.getIntValue());
        assertEquals(42, parser.getNumberValue().intValue());
        assertNull(parser.getDecimalValue());
        assertNull(parser.getBigIntegerValue());
        assertNull(parser.getBigDecimalValue());
        assertNull(parser.getDoubleValue());
        assertNull(parser.getFloatValue());
        assertEquals(42L, parser.getLongValue());
        assertEquals(42, parser.getIntValue());
        assertEquals(JsonToken.END_DOCUMENT, parser.nextToken());
        parser.close();
    }

    @Test
    public void testNormalParser() {
        JsonParser parser = new TreeTraversingParser(Json.value("{\"foo\": 1,\"bar\":[42,[\"qux\",null]]}"));
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals("foo", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals("1", parser.getText());
        assertEquals(1, parser.getIntValue());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("bar", parser.getCurrentName());
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals("42", parser.getText());
        assertEquals(42L, parser.getIntValue());
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("qux", parser.getText());
        assertEquals(JsonToken.VALUE_NULL, parser.nextToken());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertEquals(JsonToken.END_DOCUMENT, parser.nextToken());
        parser.close();
    }

    @Test
    public void testNestedParser() {
        JsonParser parser = new TreeTraversingParser(Json.value("{\"foo\":{\"bar\":1,\"baz\":[{\"qux\":42}]}}"));
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals("foo", parser.getCurrentName());
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals("bar", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals("1", parser.getText());
        assertEquals(1, parser.getIntValue());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("baz", parser.getCurrentName());
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals("qux", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals("42", parser.getText());
        assertEquals(42L, parser.getIntValue());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertEquals(JsonToken.END_DOCUMENT, parser.nextToken());
        parser.close();
    }

    @Test
    public void testErrorParser() {
        JsonParser parser = new TreeTraversingParser(Json.value("{42"));
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals("42", parser.getCurrentName());
        JsonParseException e = assertThrows(JsonParseException.class, () -> parser.nextToken());
        assertSame(parser, e.getParser());
        assertEquals("Unexpected character. Was expecting START_OBJECT but found NUMBER_INT", e.getMessage());
        parser.close();
    }

    @Test
    public void testNullParser() {
        JsonParser parser = new TreeTraversingParser(Json.value(null));
        assertEquals(JsonToken.NULL, parser.nextToken());
        assertEquals(null, parser.getText());
        assertNull(parser.getIntValue());
        assertNull(parser.getNumberValue());
        assertNull(parser.getDecimalValue());
        assertNull(parser.getBigIntegerValue());
        assertNull(parser.getBigDecimalValue());
        assertNull(parser.getDoubleValue());
        assertNull(parser.getFloatValue());
        assertNull(parser.getLongValue());
        parser.close();
    }

    @Test
    public void testBinaryParser() throws IOException {
        JsonParser parser = new TreeTraversingParser(Json.value(JsonNode.value(new byte[] { 0x1, 0x2, 0x3 })));
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, parser.nextToken());
        byte[] value = parser.getBinaryValue(JsonEncoding.Base64Std);
        assertEquals(3, value.length);
        assertEquals(0x1, value[0]);
        assertEquals(0x2, value[1]);
        assertEquals(0x3, value[2]);
        assertEquals(new BigInteger(new String(value, StandardCharsets.UTF_8)), parser.getBigIntegerValue());
        assertEquals(new BigDecimal(new String(value, StandardCharsets.UTF_8)), parser.getDecimalValue());
        assertEquals(new Double(new String(value, StandardCharsets.UTF_8)), parser.getDoubleValue());
        assertEquals(new Float(new String(value, StandardCharsets.UTF_8)), parser.getFloatValue());
        assertEquals(new Long(new String(value, StandardCharsets.UTF_8)), parser.getLongValue());
        assertEquals(new Integer(new String(value, StandardCharsets.UTF_8)), parser.getIntValue());
        assertEquals(new Number(new String(value, StandardCharsets.UTF_8)), parser.getNumberValue());
        assertEquals(value, parser.getEmbeddedObject());
        assertEquals(3, parser.readBinaryValue(JsonEncoding.Base64Std, new ByteArrayOutputStream()).size());
        parser.close();
    }

    @Test(expected = JsonParseException.class)
    public void testErrorBinaryParser() throws IOException {
        JsonParser parser = new TreeTraversingParser(Json.value(JsonNode.value(new byte[] { 0x1, 0x2, 0x3 })));
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, parser.nextToken());
        byte[] value = parser.getBinaryValue(JsonEncoding.Base64Malformed);
        parser.close();
    }
}