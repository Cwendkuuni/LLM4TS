package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.POJONode;

import org.junit.Test;

public class TreeTraversingParserTest {

    @Test
    public void testEmpty() throws IOException, JsonParseException {
        JsonNode root = Json.value("[]");
        TreeTraversingParser parser = new TreeTraversingParser(root);
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        parser.close();
        assertFalse(parser.isClosed());
    }

    @Test
    public void testObject() throws IOException, JsonParseException {
        String json = "{\"foo\":1,\"bar\":[\"apple\",\"banana\"]}";
        JsonNode root = Json.parse(json);
        TreeTraversingParser parser = new TreeTraversingParser(root);
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals("foo", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(1, parser.getIntValue());
        parser.skipChildren();
        assertEquals("bar", parser.getCurrentName());
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals("apple", parser.getText());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("banana", parser.getText());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        parser.close();
        assertTrue(parser.isClosed());
    }

    @Test
    public void testNested() throws IOException, JsonParseException {
        String json = "{\"foo\":{ \"bar\":1, \"baz\":[1,2,3] }}";
        JsonNode root = Json.parse(json);
        TreeTraversingParser parser = new TreeTraversingParser(root);
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals("foo", parser.getCurrentName());
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals("bar", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(1, parser.getIntValue());
        parser.skipChildren();
        assertEquals("baz", parser.getCurrentName());
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals(1, parser.getIntValue());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(2, parser.getIntValue());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(3, parser.getIntValue());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        parser.close();
        assertTrue(parser.isClosed());
    }

    @Test
    public void testBinary() throws IOException {
        String json = "{\"foo\":\"abcdefghijklmnopqrstuvwxyz\"}";
        JsonNode root = Json.parse(json);
        TreeTraversingParser parser = new TreeTraversingParser(root);
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals("foo", parser.getCurrentName());
        byte[] data = parser.getBinaryValue(Base64Variants.Utf8);
        assertSame(json.getBytes("UTF-8"), data);
        parser.close();
    }

    @Test
    public void testNumeric() throws IOException, JsonParseException {
        String json = "{\"foo\":123e3,\"bar\":123.45}";
        JsonNode root = Json.parse(json);
        TreeTraversingParser parser = new TreeTraversingParser(root);
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals("foo", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_NUMBER_FLOAT, parser.nextToken());
        assertEquals(123.0e3, parser.getDoubleValue(), 0.0);
        parser.skipChildren();
        assertEquals("bar", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_NUMBER_FLOAT, parser.nextToken());
        assertEquals(123.45, parser.getDoubleValue(), 0.0);
        parser.close();
        assertTrue(parser.isClosed());
    }

    @Test
    public void testNull() throws IOException, JsonParseException {
        String json = "{\"foo\":null}";
        JsonNode root = Json.parse(json);
        TreeTraversingParser parser = new TreeTraversingParser(root);
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals("foo", parser.getCurrentName());
        assertNull(parser.getNumberValue());
        assertNull(parser.getText());
        parser.skipChildren();
        parser.close();
        assertTrue(parser.isClosed());
    }

    @Test
    public void testError() throws IOException, JsonParseException {
        String json = "{\"foo\":[1,\"apple\",3]}";
        JsonNode root = Json.parse(json);
        TreeTraversingParser parser = new TreeTraversingParser(root);
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals("foo", parser.getCurrentName());
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals(1, parser.getIntValue());
        parser.skipChildren();
        assertFalse(parser.isClosed());
        parser.getText(); // should throw an exception
        parser.close();
        assertTrue(parser.isClosed());
    }

    @Test
    public void testPojo() throws IOException {
        String json = "{\"foo\":{\"bar\":1,\"baz\":\"qux\"}}";
        JsonNode root = Json.parse(json);
        TreeTraversingParser parser = new TreeTraversingParser(root);
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals("foo", parser.getCurrentName());
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        POJONode pojo = (POJONode) parser.getEmbeddedObject();
        Object pojoValue = pojo.getPojo();
        ObjectMapper mapper = new ObjectMapper();
        assertEquals(Map.of("bar", 1, "baz", "qux"), mapper.convertValue(pojoValue, Map.class));
        parser.close();
        assertTrue(parser.isClosed());
    }
}