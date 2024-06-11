package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;

import org.junit.Test;

public class TreeTraversingParserTest {

    @Test
    public void testEmpty() throws Exception {
        TreeTraversingParser parser = new TreeTraversingParser(Json.valueFromObject(new Object()));
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        parser.close();
    }

    @Test
    public void testSingleInt() throws Exception {
        String json = "1";
        TreeTraversingParser parser = new TreeTraversingParser(Json.parse(json));
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("1", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(1, parser.getIntValue());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        parser.close();
    }

    @Test
    public void testSingleString() throws Exception {
        String json = "\"foo\"";
        TreeTraversingParser parser = new TreeTraversingParser(Json.parse(json));
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("foo", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("foo", parser.getText());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        parser.close();
    }

    @Test
    public void testSingleObject() throws Exception {
        String json = "{\"foo\":1}";
        TreeTraversingParser parser = new TreeTraversingParser(Json.parse(json));
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("foo", parser.getCurrentName());
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("1", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(1, parser.getIntValue());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        parser.close();
    }

    @Test
    public void testArray() throws Exception {
        String json = "[1, \"foo\", 3.14]";
        TreeTraversingParser parser = new TreeTraversingParser(Json.parse(json));
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(1, parser.getIntValue());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("foo", parser.getText());
        assertEquals(JsonToken.VALUE_NUMBER_FLOAT, parser.nextToken());
        assertEquals(3.14f, parser.getFloatValue(), 0.001f);
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        parser.close();
    }

    @Test
    public void testNestedObjects() throws Exception {
        String json = "{\"foo\":{\"bar\":1, \"baz\":[\"qux\", 2.5]}}";
        TreeTraversingParser parser = new TreeTraversingParser(Json.parse(json));
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("foo", parser.getCurrentName());
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("bar", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(1, parser.getIntValue());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("baz", parser.getCurrentName());
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("qux", parser.getText());
        assertEquals(JsonToken.VALUE_NUMBER_FLOAT, parser.nextToken());
        assertEquals(2.5f, parser.getFloatValue(), 0.001f);
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        parser.close();
    }

    @Test
    public void testBinaryData() throws Exception {
        String json = "{\"data\": \"data:application/octet-stream;base64,AAECAwQFBgZmb2xsb3dpymoBicAgICAgICAgICAgICA8cmVuPg==\"}";
        TreeTraversingParser parser = new TreeTraversingParser(Json.parse(json), ObjectMapper.getDefaultInstance());
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("data", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        byte[] data = parser.getBinaryValue(Base64Variants.STANDARD);
        assertNotNull(data);
        assertEquals("data", parser.getText());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        parser.close();
    }
}