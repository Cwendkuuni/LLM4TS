package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.NodeCursor;
import com.fasterxml.jackson.databind.ObjectCodec;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.BinaryNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.node.TreeTraversingParser;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

public class TreeTraversingParserTest {

    private TreeTraversingParser parser;

    @BeforeMethod
    public void setUp() {
        ObjectCodec objectCodec = new JsonFactory().getCodec();
        JsonNode jsonNode = new ObjectNode(objectCodec);
        parser = new TreeTraversingParser(jsonNode, objectCodec);
    }

    @Test
    public void testTraversingParser_EmptyObject() throws IOException {
        JsonToken token = parser.nextToken();
        Assert.assertEquals(token, JsonToken.START_OBJECT);
        Assert.assertEquals(parser.nextToken(), JsonToken.FIELD_NAME);
        Assert.assertEquals(parser.getCurrentName(), "class");
        Assert.assertEquals(parser.nextToken(), JsonToken.VALUE_NULL);
        Assert.assertEquals(parser.nextToken(), JsonToken.END_OBJECT);
        Assert.assertTrue(parser.isClosed());
    }

    @Test
    public void testTraversingParser_EmptyArray() throws IOException {
        parser.nextToken(); // START_ARRAY
        Assert.assertEquals(parser.nextToken(), JsonToken.END_ARRAY);
        Assert.assertTrue(parser.isClosed());
    }

    @Test
    public void testTraversingParser_StringValue() throws IOException {
        ObjectNode rootNode = new ObjectNode(parser.getCodec());
        rootNode.put("name", new TextNode("John Doe"));
        parser = new TreeTraversingParser(rootNode, parser.getCodec());

        Assert.assertEquals(parser.nextToken(), JsonToken.FIELD_NAME);
        Assert.assertEquals(parser.getCurrentName(), "name");
        Assert.assertEquals(parser.nextToken(), JsonToken.VALUE_STRING);
        Assert.assertEquals(parser.getText(), "John Doe");
        Assert.assertEquals(parser.nextToken(), JsonToken.COMMA);
        Assert.assertEquals(parser.nextToken(), JsonToken.END_OBJECT);
        Assert.assertTrue(parser.isClosed());
    }

    @Test
    public void testTraversingParser_NumberValue() throws IOException {
        ObjectNode rootNode = new ObjectNode(parser.getCodec());
        rootNode.put("age", new NumberNode(42));
        parser = new TreeTraversingParser(rootNode, parser.getCodec());

        Assert.assertEquals(parser.nextToken(), JsonToken.FIELD_NAME);
        Assert.assertEquals(parser.getCurrentName(), "age");
        Assert.assertEquals(parser.getNumberType(), NumberType.INTEGER);
        Assert.assertEquals(parser.getLongValue(), 42);
        Assert.assertEquals(parser.nextToken(), JsonToken.COMMA);
        Assert.assertEquals(parser.nextToken(), JsonToken.END_OBJECT);
        Assert.assertTrue(parser.isClosed());
    }

    @Test
    public void testTraversingParser_NestedArray() throws IOException {
        ObjectNode rootNode = new ObjectNode(parser.getCodec());
        ArrayNode arrayNode = new ArrayNode(parser.getCodec());
        arrayNode.add(new TextNode("Test"));
        arrayNode.add(new NumberNode(42));
        rootNode.set("array", arrayNode);
        parser = new TreeTraversingParser(rootNode, parser.getCodec());

        Assert.assertEquals(parser.nextToken(), JsonToken.FIELD_NAME);
        Assert.assertEquals(parser.getCurrentName(), "array");
        Assert.assertEquals(parser.nextToken(), JsonToken.START_ARRAY);

        Assert.assertEquals(parser.nextToken(), JsonToken.VALUE_STRING);
        Assert.assertEquals(parser.getText(), "Test");

        Assert.assertEquals(parser.nextToken(), JsonToken.VALUE_NUMBER_INT);
        Assert.assertEquals(parser.getIntValue(), 42);

        Assert.assertEquals(parser.nextToken(), JsonToken.END_ARRAY);
        Assert.assertEquals(parser.nextToken(), JsonToken.END_OBJECT);
        Assert.assertTrue(parser.isClosed());
    }

    @Test
    public void testTraversingParser_NestedObject() throws IOException {
        ObjectNode rootNode = new ObjectNode(parser.getCodec());
        ObjectNode nestedNode = new ObjectNode(parser.getCodec());
        nestedNode.put("name", new TextNode("Jane Doe"));
        nestedNode.put("age", new NumberNode(31));
        rootNode.set("person", nestedNode);
        parser = new TreeTraversingParser(rootNode, parser.getCodec());

        Assert.assertEquals(parser.nextToken(), JsonToken.FIELD_NAME);
        Assert.assertEquals(parser.getCurrentName(), "person");
        Assert.assertEquals(parser.nextToken(), JsonToken.START_OBJECT);

        Assert.assertEquals(parser.nextToken(), JsonToken.FIELD_NAME);
        Assert.assertEquals(parser.getCurrentName(), "name");
        Assert.assertEquals(parser.nextToken(), JsonToken.VALUE_STRING);
        Assert.assertEquals(parser.getText(), "Jane Doe");

        Assert.assertEquals(parser.nextToken(), JsonToken.FIELD_NAME);
        Assert.assertEquals(parser.getCurrentName(), "age");
        Assert.assertEquals(parser.getNumberType(), NumberType.INTEGER);
        Assert.assertEquals(parser.getIntValue(), 31);

        Assert.assertEquals(parser.nextToken(), JsonToken.END_OBJECT);
        Assert.assertEquals(parser.nextToken(), JsonToken.END_OBJECT);
        Assert.assertTrue(parser.isClosed());
    }

    @Test
    public void testTraversingParser_BinaryValue() throws IOException {
        ObjectNode rootNode = new ObjectNode(parser.getCodec());
        BinaryNode binaryNode = new BinaryNode(parser.getCodec(), new byte[]{1, 2, 3});
        rootNode.set("binary", binaryNode);
        parser = new TreeTraversingParser(rootNode, parser.getCodec());

        Assert.assertEquals(parser.nextToken(), JsonToken.FIELD_NAME);
        Assert.assertEquals(parser.getCurrentName(), "binary");
        Assert.assertEquals(parser.nextToken(), JsonToken.VALUE_EMBEDDED_OBJECT);
        byte[] binaryData = parser.getBinaryValue(Base64Variant.STANDARD);
        Assert.assertNotNull(binaryData);
        Assert.assertEquals(binaryData.length, 3);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int readBytes = parser.readBinaryValue(Base64Variant.STANDARD, outputStream);
        Assert.assertEquals(readBytes, binaryData.length);
        Assert.assertArrayEquals(binaryData, outputStream.toByteArray());

        Assert.assertEquals(parser.nextToken(), JsonToken.END_OBJECT);
        Assert.assertTrue(parser.isClosed());
    }

    @Test(expectedExceptions = JsonParseException.class)
    public void testTraversingParser_NullValue() throws IOException {
        ObjectNode rootNode = new ObjectNode(parser.getCodec());
        rootNode.put("age", null);
        parser = new TreeTraversingParser(rootNode, parser.getCodec());
        Assert.assertEquals(parser.nextToken(), JsonToken.FIELD_NAME);
        Assert.assertEquals(parser.getCurrentName(), "age");
        Assert.assertEquals(parser.nextToken(), JsonToken.VALUE_NULL);
        Assert.assertEquals(parser.nextToken(), JsonToken.COMMA);

        // This should throw JsonParseException
        Assert.assertEquals(parser.nextToken(), JsonToken.END_OBJECT);
    }

    @Test(expectedExceptions = JsonParseException.class)
    public void testTraversingParser_InvalidJson() throws IOException {
        ObjectNode rootNode = new ObjectNode(parser.getCodec());
        rootNode.put("name", "{\"age\": 42}");
        parser = new TreeTraversingParser(rootNode, parser.getCodec());

        Assert.assertEquals(parser.nextToken(), JsonToken.FIELD_NAME);
        Assert.assertEquals(parser.getCurrentName(), "name");
        Assert.assertEquals(parser.nextToken(), JsonToken.VALUE_STRING);
        Assert.assertEquals(parser.getText(), "{\"age\": 42}");

        // This should throw JsonParseException
        parser.nextToken();
    }
}