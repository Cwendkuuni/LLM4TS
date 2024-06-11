package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.TextNode;

import org.junit.Assert;
import org.junit.Test;

import static com.fasterxml.jackson.databind.node.JsonNodeType.*;

public class TreeTraversingParserTest {

    @Test
    public void testParseEmpty() throws Exception {
        JsonFactory factory = new JsonFactory();
        TreeTraversingParser parser = new TreeTraversingParser(null, factory);

        parser.nextToken(); // consume START_OBJECT
        Assert.assertNull(parser.getText());
        parser.close();
    }

    @Test
    public void testParseNull() throws Exception {
        JsonFactory factory = new JsonFactory();
        TreeTraversingParser parser = new TreeTraversingParser(null, factory);

        parser.nextToken(); // consume START_NULL
        Assert.assertNull(parser.getText());
        parser.close();
    }

    @Test
    public void testParseBooleanTrue() throws Exception {
        JsonFactory factory = new JsonFactory();
        TreeTraversingParser parser = new TreeTraversingParser(new TextNode("true"), factory);

        Assert.assertEquals(Boolean.TRUE, parser.nextValue().asBoolean());
        Assert.assertEquals("true", parser.getText());
        parser.close();
    }

    @Test
    public void testParseBooleanFalse() throws Exception {
        JsonFactory factory = new JsonFactory();
        TreeTraversingParser parser = new TreeTraversingParser(new TextNode("false"), factory);

        Assert.assertEquals(Boolean.FALSE, parser.nextValue().asBoolean());
        Assert.assertEquals("false", parser.getText());
        parser.close();
    }

    @Test
    public void testParseNumberInt() throws Exception {
        JsonFactory factory = new JsonFactory();
        TreeTraversingParser parser = new TreeTraversingParser(new TextNode("123"), factory);

        Assert.assertEquals(123, parser.nextValue().asInt());
        Assert.assertEquals("123", parser.getText());
        parser.close();
    }

    @Test
    public void testParseNumberLong() throws Exception {
        JsonFactory factory = new JsonFactory();
        TreeTraversingParser parser = new TreeTraversingParser(new TextNode("45678901234567890"), factory);

        Assert.assertEquals(45678901234567890L, parser.nextValue().asLong());
        Assert.assertEquals("45678901234567890", parser.getText());
        parser.close();
    }

    @Test
    public void testParseNumberFloat() throws Exception {
        JsonFactory factory = new JsonFactory();
        TreeTraversingParser parser = new TreeTraversingParser(new TextNode("3.14159"), factory);

        Assert.assertEquals(3.14159f, parser.nextValue().asFloat(), 0.0f);
        Assert.assertEquals("3.14159", parser.getText());
        parser.close();
    }

    @Test
    public void testParseNumberDouble() throws Exception {
        JsonFactory factory = new JsonFactory();
        TreeTraversingParser parser = new TreeTraversingParser(new TextNode("3.14159265358979323846"), factory);

        Assert.assertEquals(3.14159265358979323846d, parser.nextValue().asDouble(), 0.0d);
        Assert.assertEquals("3.14159265358979323846", parser.getText());
        parser.close();
    }

    @Test
    public void testParseNumberDecimal() throws Exception {
        JsonFactory factory = new JsonFactory();
        TreeTraversingParser parser = new TreeTraversingParser(new TextNode("3.14159265358979323846"), factory);

        Assert.assertEquals(new BigDecimal("3.14159265358979323846"), parser.nextValue().decimalValue());
        Assert.assertEquals("3.14159265358979323846", parser.getText());
        parser.close();
    }

    @Test
    public void testParseString() throws Exception {
        JsonFactory factory = new JsonFactory();
        TreeTraversingParser parser = new TreeTraversingParser(new TextNode("\"abc\tdef\nghi\""), factory);

        Assert.assertEquals("abc	def\nghi", parser.getText());
        Assert.assertEquals("abc\tdef\nghi", parser.nextValue().asText());
        parser.close();
    }

    @Test
    public void testParseArray() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree("[1, 2, 3]");
        TreeTraversingParser parser = new TreeTraversingParser(root, null);

        Assert.assertEquals(START_ARRAY, parser.nextToken().getValue());
        Assert.assertEquals(int.class, parser.nextValue().getClass());
        Assert.assertEquals(1, parser.nextValue().asInt());
        Assert.assertEquals(VALUE_NUMBER_INT, parser.nextToken().getValue());
        Assert.assertEquals(2, parser.nextValue().asInt());
        Assert.assertEquals(VALUE_NUMBER_INT, parser.nextToken().getValue());
        Assert.assertEquals(3, parser.nextValue().asInt());
        Assert.assertEquals(END_ARRAY, parser.nextToken().getValue());
        parser.close();
    }

    @Test
    public void testParseObject() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree("{\"name\":\"John\", \"age\":30}");
        TreeTraversingParser parser = new TreeTraversingParser(root, null);

        Assert.assertEquals(START_OBJECT, parser.nextToken().getValue());
        Assert.assertEquals("name", parser.getCurrentName());
        Assert.assertEquals("John", parser.nextValue().asText());
        Assert.assertEquals("age", parser.getCurrentName());
        Assert.assertEquals(30, parser.nextValue().asInt());
        Assert.assertEquals(END_OBJECT, parser.nextToken().getValue());
        parser.close();
    }

    @Test
    public void testParseBinaryBase64() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree("{\"data\":\"RnJpbGx5\nRnJpbGx5\nRnJpbGx5\n\"}");
        TreeTraversingParser parser = new TreeTraversingParser(root, null);

        Assert.assertEquals(START_OBJECT, parser.nextToken().getValue());
        Assert.assertEquals("data", parser.getCurrentName());
        byte[] binaryData = parser.nextValue().getBinaryValue(Base64Variants.STANDARD);
        Assert.assertNotNull(binaryData);
        Assert.assertEquals(12, binaryData.length);
        parser.close();
    }

    @Test(expected = JsonParseException.class)
    public void testParseInvalidJson() throws Exception {
        JsonFactory factory = new JsonFactory();
        TreeTraversingParser parser = new TreeTraversingParser(new TextNode("{"), factory);

        parser.nextToken();
        parser.nextToken(); // consume START_OBJECT
        parser.nextToken(); // consume KEY_NAME
        parser.nextToken(); // consume COLON
        parser.nextToken(); // consume INVALID_TOKEN
        parser.nextToken(); // consume COMMA
        parser.nextToken(); // consume VALUE_NULL
        parser.nextToken(); // consume END_OBJECT
        parser.close();
    }
}