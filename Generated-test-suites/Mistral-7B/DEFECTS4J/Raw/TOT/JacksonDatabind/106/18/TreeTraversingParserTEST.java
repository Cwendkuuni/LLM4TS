package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.TextNode;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class TreeTraversingParserTest {

    @Test
    public void testParseJsonArray() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[1, 2, 3]";
        JsonNode node = mapper.readTree(json);
        TreeTraversingParser parser = new TreeTraversingParser(node);

        Assert.assertEquals(JsonToken.START_ARRAY, parser.nextToken());

        for (int i = 0; i < 3; i++) {
            Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
            Assert.assertEquals(i + 1, parser.getIntValue());
        }

        Assert.assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        Assert.assertNull(parser.nextToken());
    }

    @Test
    public void testParseJsonObject() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"a\": 1, \"b\": 2}";
        JsonNode node = mapper.readTree(json);
        TreeTraversingParser parser = new TreeTraversingParser(node);

        Assert.assertEquals(JsonToken.START_OBJECT, parser.nextToken());

        parser.skipChildren();
        Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        Assert.assertEquals("a", parser.getCurrentName());
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        Assert.assertEquals(1, parser.getIntValue());

        parser.skipChildren();
        Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        Assert.assertEquals("b", parser.getCurrentName());
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        Assert.assertEquals(2, parser.getIntValue());

        Assert.assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        Assert.assertNull(parser.nextToken());
    }

    @Test
    public void testParseEmptyJson() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{}";
        JsonNode node = mapper.readTree(json);
        TreeTraversingParser parser = new TreeTraversingParser(node);

        Assert.assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        Assert.assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        Assert.assertNull(parser.nextToken());
    }

    @Test(expected = JsonParseException.class)
    public void testInvalidJson() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "\"not valid json\"";
        JsonNode node = mapper.readTree(json);
        TreeTraversingParser parser = new TreeTraversingParser(node);

        parser.nextToken(); // consume opening [ or {

        parser.close();
    }

    @Test(expected = JsonParseException.class)
    public void testNegativeNumber() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "-1";
        JsonNode node = mapper.readTree(json);
        TreeTraversingParser parser = new TreeTraversingParser(node);

        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        Assert.assertEquals(-1, parser.getIntValue());

        parser.close();

        parser = new TreeTraversingParser(node);

        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        Assert.assertEquals(-1, parser.getIntValue());

        parser.nextToken(); // consume closing ] or }

        parser.nextToken(); // should be null

        JsonNodeFactory factory = mapper.getNodeFactory();
        JsonNode node2 = factory.numberNode(-2.5);
        TreeTraversingParser parser2 = new TreeTraversingParser(node2);

        Assert.assertEquals(JsonToken.VALUE_NUMBER_FLOAT, parser2.nextToken());
        Assert.assertEquals(-2.5, parser2.getDoubleValue(), 0.001);

        parser2.close();
    }

    @Test
    public void testParseBinaryData() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "\"RG9jdW1lbnQ=\"".getBytes();
        JsonNode node = mapper.readTree(json);
        TreeTraversingParser parser = new TreeTraversingParser(node);

        Assert.assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        String binaryData = parser.getText();
        Assert.assertEquals("data", binaryData);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int read = parser.readBinaryValue(Base64Variant.BASE64_STANDARD, outputStream);
        Assert.assertEquals(10, read);
        Assert.assertEquals("data".getBytes(), outputStream.toByteArray());
    }

    @Test
    public void testParseEmptyBinaryData() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "\"\"";
        JsonNode node = mapper.readTree(json);
        TreeTraversingParser parser = new TreeTraversingParser(node);

        Assert.assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        Assert.assertEquals("", parser.getText());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int read = parser.readBinaryValue(Base64Variant.BASE64_STANDARD, outputStream);
        Assert.assertEquals(0, read);
    }

    @Test(expected = IOException.class)
    public void testParseInvalidBinaryData() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "\"RG9jdW1lbnQ==\""; // missing = at the end
        JsonNode node = mapper.readTree(json);
        TreeTraversingParser parser = new TreeTraversingParser(node);

        parser.nextToken(); // consume opening [ or {

        parser.close();
    }
}