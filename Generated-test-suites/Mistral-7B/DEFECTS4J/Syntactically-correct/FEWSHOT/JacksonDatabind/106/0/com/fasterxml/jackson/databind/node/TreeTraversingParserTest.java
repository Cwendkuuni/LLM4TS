package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Test;

public class TreeTraversingParserTest {

    @Test
    public void testEmpty() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(JsonNode.valueOf(new Object()));
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        parser.close();
    }

    @Test
    public void testArray() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(JsonNode.valueOf(new int[]{1, 2, 3}));
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals(JsonToken.INT, parser.nextToken().getValueAsInt());
        assertEquals(JsonToken.INT, parser.nextToken().getValueAsInt());
        assertEquals(JsonToken.INT, parser.nextToken().getValueAsInt());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        parser.close();
    }

    @Test
    public void testObject() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(JsonNode.valueOf("{\"key\":1,\"key2\":[1,2,3]}"));
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("key", parser.nextValue().asText());
        assertEquals(JsonToken.INT, parser.nextToken().getValueAsInt());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("key2", parser.nextValue().asText());
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals(JsonToken.INT, parser.nextToken().getValueAsInt());
        assertEquals(JsonToken.INT, parser.nextToken().getValueAsInt());
        assertEquals(JsonToken.INT, parser.nextToken().getValueAsInt());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        parser.close();
    }

    @Test
    public void testRecursiveObject() throws IOException {
        JsonNode root = JsonNode.valueOf("{\"key\":{\"key2\":1,\"key3\":[1,2,3]}}");
        TreeTraversingParser parser = new TreeTraversingParser(root);
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("key", parser.nextValue().asText());
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("key2", parser.nextValue().asText());
        assertEquals(JsonToken.INT, parser.nextToken().getValueAsInt());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("key3", parser.nextToken().asText());
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals(JsonToken.INT, parser.nextToken().getValueAsInt());
        assertEquals(JsonToken.INT, parser.nextToken().getValueAsInt());
        assertEquals(JsonToken.INT, parser.nextToken().getValueAsInt());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        parser.close();
    }

    @Test
    public void testRecursiveArray() throws IOException {
        JsonNode root = JsonNode.valueOf("[1,2,{\"key\":3,\"key2\":[4,5,6]}]");
        TreeTraversingParser parser = new TreeTraversingParser(root);
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals(JsonToken.INT, parser.nextToken().getValueAsInt());
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("key", parser.nextValue().asText());
        assertEquals(JsonToken.INT, parser.nextToken().getValueAsInt());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("key2", parser.nextToken().asText());
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals(JsonToken.INT, parser.nextToken().getValueAsInt());
        assertEquals(JsonToken.INT, parser.nextToken().getValueAsInt());
        assertEquals(JsonToken.INT, parser.nextToken().getValueAsInt());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        parser.close();
    }

    @Test
    public void testTextValue() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(JsonNode.valueOf("\"foo\""));
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("foo", parser.getText());
        parser.close();
    }

    @Test
    public void testTextValueWithNull() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(JsonNode.valueOf(null));
        assertEquals(JsonToken.NULL, parser.nextToken());
        parser.close();
    }

    @Test
    public void testIntValue() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(JsonNode.valueOf(42));
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(42, parser.getIntValue());
        parser.close();
    }

    @Test
    public void testLongValue() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(JsonNode.valueOf(42L));
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(42, parser.getIntValue());
        assertEquals(42L, parser.getLongValue());
        parser.close();
    }

    @Test
    public void testDecimalValue() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(JsonNode.valueOf(3.14));
        assertEquals(JsonToken.VALUE_NUMBER_FLOAT, parser.nextToken());
        assertEquals(3.14, parser.getDoubleValue(), 0.001);
        assertEquals(3.14, parser.getDecimalValue().doubleValue(), 0.001);
        parser.close();
    }

    @Test
    public void testBinaryValue() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(JsonNode.valueOf("iQogIHJlZW1zLg=="));
        byte[] data = parser.getBinaryValue(Base64Variant.STANDARD);
        assertArrayEquals(new byte[]{0, 104, 111, 121, 32, 105, 110, 32, 119, 105, 116, 104, 32, 105, 101, 110}, data);
        parser.close();
    }

    @Test(expected = JsonParseException.class)
    public void testInvalidJson() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(JsonNode.valueOf("{"));
        parser.nextToken();
        parser.nextToken(); // this will fail as there is no field name
        parser.close();
    }
}