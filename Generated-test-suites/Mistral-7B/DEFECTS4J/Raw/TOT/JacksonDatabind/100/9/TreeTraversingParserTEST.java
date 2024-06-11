package com.fasterxml.jackson.databind.node;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.BaseJsonNode;
import com.fasterxml.jackson.databind.node.TextNode;

/**
 * Test cases for {@link TreeTraversingParser}.
 */
public class TreeTraversingParserTest {

    /**
     * Test empty JSON tree.
     */
    @Test
    public void testEmptyTree() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode emptyTree = mapper.readTree("");
        TreeTraversingParser parser = new TreeTraversingParser(emptyTree);
        JsonToken token = parser.nextToken();
        Assert.assertNull(token);
        parser.close();
    }

    /**
     * Test JSON array.
     */
    @Test
    public void testArray() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[1, 2, 3]";
        JsonNode tree = mapper.readTree(json);
        TreeTraversingParser parser = new TreeTraversingParser(tree);

        JsonToken token = parser.nextToken();
        Assert.assertEquals(JsonToken.START_ARRAY, token);

        token = parser.nextToken();
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, token);
        Assert.assertEquals(1, parser.getIntValue());
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, token);
        Assert.assertEquals(2, parser.getIntValue());
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, token);
        Assert.assertEquals(3, parser.getIntValue());
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.END_ARRAY, token);

        parser.close();
    }

    /**
     * Test JSON object.
     */
    @Test
    public void testObject() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"name\":\"John\", \"age\":30}";
        JsonNode tree = mapper.readTree(json);
        TreeTraversingParser parser = new TreeTraversingParser(tree);

        JsonToken token = parser.nextToken();
        Assert.assertEquals(JsonToken.START_OBJECT, token);

        token = parser.nextToken();
        Assert.assertEquals(JsonToken.FIELD_NAME, token);
        Assert.assertEquals("name", parser.getCurrentName());
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.VALUE_STRING, token);
        Assert.assertEquals("John", parser.getText());
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.FIELD_NAME, token);
        Assert.assertEquals("age", parser.getCurrentName());
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, token);
        Assert.assertEquals(30, parser.getIntValue());
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.END_OBJECT, token);

        parser.close();
    }

    /**
     * Test JSON tree with empty array and empty object.
     */
    @Test
    public void testEmptyArrayAndObject() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[{},{}]";
        JsonNode tree = mapper.readTree(json);
        TreeTraversingParser parser = new TreeTraversingParser(tree);

        JsonToken token = parser.nextToken();
        Assert.assertEquals(JsonToken.START_ARRAY, token);

        token = parser.nextToken();
        Assert.assertEquals(JsonToken.START_OBJECT, token);
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.END_OBJECT, token);
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.START_OBJECT, token);
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.END_OBJECT, token);
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.END_ARRAY, token);

        parser.close();
    }

    /**
     * Test JSON tree with non-numeric value and string value.
     */
    @Test
    public void testNonNumericAndStringValue() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"name\":\"John\", \"age\":true}";
        JsonNode tree = mapper.readTree(json);
        TreeTraversingParser parser = new TreeTraversingParser(tree);

        JsonToken token = parser.nextToken();
        Assert.assertEquals(JsonToken.START_OBJECT, token);

        token = parser.nextToken();
        Assert.assertEquals(JsonToken.FIELD_NAME, token);
        Assert.assertEquals("name", parser.getCurrentName());
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.VALUE_STRING, token);
        Assert.assertEquals("John", parser.getText());
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.FIELD_NAME, token);
        Assert.assertEquals("age", parser.getCurrentName());
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.VALUE_BOOLEAN, token);
        Assert.assertTrue(parser.getBooleanValue());
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.END_OBJECT, token);

        parser.close();
    }

    /**
     * Test JSON tree with binary value (base64).
     */
    @Test
    public void testBinaryValue() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"data\":\"RnJhbW1peGxleHBsYW5ldCgK\"}";
        JsonNode tree = mapper.readTree(json);
        TreeTraversingParser parser = new TreeTraversingParser(tree);

        JsonToken token = parser.nextToken();
        Assert.assertEquals(JsonToken.START_OBJECT, token);

        token = parser.nextToken();
        Assert.assertEquals(JsonToken.FIELD_NAME, token);
        Assert.assertEquals("data", parser.getCurrentName());
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.VALUE_STRING, token);
        byte[] binaryData = parser.getBinaryValue(Base64Variants.STANDARD);
        Assert.assertArrayEquals(new byte[]{0, 1, 2, 3}, binaryData);
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.END_OBJECT, token);

        parser.close();
    }

    /**
     * Test JSON tree with error.
     */
    @Test(expected = JsonParseException.class)
    public void testError() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"name\":\"John\"}";
        JsonNode tree = mapper.readTree(json);
        TreeTraversingParser parser = new TreeTraversingParser(tree);

        // should not reach here
        parser.nextToken();
        parser.nextToken(); // assuming "name" is a string
        parser.getIntValue();
    }
}