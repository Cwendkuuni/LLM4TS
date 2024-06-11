package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.fasterxml.jackson.core.Base64Variants;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonStreamContext;
import com.fasterxml.jackson.core.io.SerializableString;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.POJONode;
import com.fasterxml.jackson.databind.node.TextNode;

public class TreeTraversingParserTest {

    @Test
    public void testEmptyParser() {
        TreeTraversingParser parser = new TreeTraversingParser(JsonNode.NULL);
        assertEquals(JsonToken.START_NULL, parser.nextToken());
        parser.close();
    }

    @Test
    public void testValueNodeParser() {
        JsonNode rootNode = new TextNode("Hello World");
        TreeTraversingParser parser = new TreeTraversingParser(rootNode);

        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("Hello World", parser.getText());

        parser.close();
    }

    @Test
    public void testArrayNodeParser() throws IOException {
        JsonNode rootNode = new TextNode("['1', '2', '3']");
        TreeTraversingParser parser = new TreeTraversingParser(rootNode);

        assertEquals(JsonToken.START_ARRAY, parser.nextToken());

        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals("1", parser.getText());
        assertEquals(1, parser.getIntValue());

        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("2", parser.getText());

        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals("3", parser.getText());

        assertEquals(JsonToken.END_ARRAY, parser.nextToken());

        parser.close();
    }

    @Test
    public void testObjectNodeParser() throws IOException {
        JsonNode rootNode = new ObjectNode(JsonNodeFactory.instance);
        rootNode.put("name", new TextNode("John Doe"));
        rootNode.put("age", new NumberNode(30));
        rootNode.put("city", new TextNode("New York"));

        TreeTraversingParser parser = new TreeTraversingParser(rootNode);

        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals("name", parser.getCurrentName());
        assertEquals("John Doe", parser.getText());

        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("age", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(30, parser.getIntValue());

        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("city", parser.getCurrentName());
        assertEquals("New York", parser.getText());

        assertEquals(JsonToken.END_OBJECT, parser.nextToken());

        parser.close();
    }

    @Test
    public void testBinaryValueParser() throws IOException {
        JsonNode rootNode = new TextNode("\"Rm9udCAoKTs=\"".getBytes());
        TreeTraversingParser parser = new TreeTraversingParser(rootNode);

        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, parser.nextToken());
        assertEquals("Rm9udCAoKTs=\n", parser.getText());

        byte[] binaryData = parser.getBinaryValue(Base64Variants.STANDARD);
        assertEquals(new String(binaryData), "Hello World");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int length = parser.readBinaryValue(Base64Variants.STANDARD, outputStream);
        assertEquals(11, length);
        assertEquals("Hello World", new String(outputStream.toByteArray()));

        parser.close();
    }

    @Test
    public void testSkippingChildrenParser() throws IOException {
        JsonNode rootNode = new ArrayNode(JsonNodeFactory.instance);
        rootNode.add(new TextNode("Child 1"));
        rootNode.add(new TextNode("Child 2"));
        rootNode.add(new ObjectNode(JsonNodeFactory.instance));
        rootNode.get(2).put("Name", new TextNode("Sub-Child"));

        TreeTraversingParser parser = new TreeTraversingParser(rootNode);

        parser.nextToken(); // Start Object
        parser.nextToken(); // Field Name: "Name"
        parser.nextToken(); // Value String: "Sub-Child"
        parser.skipChildren(); // Skip Object

        assertEquals(JsonToken.StartArray, parser.nextToken()); // Start Array
        parser.nextToken(); // Value String: "Child 1"
        parser.nextToken(); // Value String: "Child 2"
        parser.nextToken(); // End Array

        parser.close();
    }

    @Test
    public void testClosedParser() throws IOException {
        JsonNode rootNode = new ObjectNode(JsonNodeFactory.instance);
        TreeTraversingParser parser = new TreeTraversingParser(rootNode);
        parser.close();

        assertEquals(JsonToken.CLOSED, parser.nextToken());
    }
}