package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.ser.std.StdPOJOLazySerializer;

public class TreeTraversingParserTest {
    private JsonNode rootNode;
    private ObjectCodec objectCodec;
    private TreeTraversingParser parser;

    @org.junit.Before
    public void setUp() throws Exception {
        objectCodec = mock(ObjectCodec.class);
        rootNode = createTestNode();
        parser = new TreeTraversingParser(rootNode, objectCodec);
    }

    @org.junit.Test
    public void testEmptyParser() throws Exception {
        assertNull(parser.nextToken());
        parser.close();
        assertNull(parser.nextToken());
    }

    @org.junit.Test
    public void testParserWithRootNode() throws Exception {
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.START_OBJECT, token);
        token = parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        assertEquals("field1", token.asString());
        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token);
        assertEquals("value1", token.asString());
        token = parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        assertEquals("field2", token.asString());
        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, token);
        assertEquals(42, token.asInt());
        token = parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, token);

        parser.close();

        token = parser.nextToken();
        assertNull(token);
    }

    @org.junit.Test
    public void testParserWithArrayNode() throws Exception {
        rootNode = createTestArrayNode();
        parser = new TreeTraversingParser(rootNode, objectCodec);

        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.START_ARRAY, token);
        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, token);
        assertEquals(1, token.asInt());
        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, token);
        assertEquals(2, token.asInt());
        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token);
        assertEquals("three", token.asString());
        token = parser.nextToken();
        assertEquals(JsonToken.END_ARRAY, token);

        parser.close();

        token = parser.nextToken();
        assertNull(token);
    }

    @org.junit.Test
    public void testParserWithBinaryNode() throws Exception {
        rootNode = createTestBinaryNode();
        parser = new TreeTraversingParser(rootNode, objectCodec);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        parser.readBinaryValue(null, outputStream);
        byte[] bytes = outputStream.toByteArray();
        assertEquals("test".getBytes(), bytes);

        parser.close();

        token = parser.nextToken();
        assertNull(token);
    }

    @org.junit.Test
    public void testParserWithPojoNode() throws Exception {
        POJONode pojoNode = new POJONode(new StdPOJOLazySerializer(String.class), "test");
        rootNode = new ObjectNode(objectCodec.treeAsToken(pojoNode));
        parser = new TreeTraversingParser(rootNode, objectCodec);

        assertEquals("test", parser.getText());
        parser.close();

        token = parser.nextToken();
        assertNull(token);
    }

    @org.junit.Test
    public void testParserWithClose() throws Exception {
        parser = new TreeTraversingParser(rootNode, objectCodec);
        parser.close();
        token = parser.nextToken();
        assertNull(token);
    }

    @org.junit.Test
    public void testParserWithNoChildren() throws Exception {
        rootNode = new ObjectNode(objectCodec.treeAsToken(new ObjectNode("name", "John Doe")));
        parser = new TreeTraversingParser(rootNode, objectCodec);

        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.START_OBJECT, token);
        token = parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        assertEquals("name", token.asString());
        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token);
        assertEquals("John Doe", token.asString());
        token = parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, token);

        parser.close();

        token = parser.nextToken();
        assertNull(token);
    }

    @org.junit.Test
    public void testParserWithNullNode() throws Exception {
        rootNode = null;
        parser = new TreeTraversingParser(rootNode, objectCodec);
        assertNull(parser.nextToken());
        parser.close();
        assertNull(parser.nextToken());
    }

    @org.junit.Test
    public void testParserWithRecursiveContainer() throws Exception {
        JsonNode root = new ObjectNode(objectCodec.treeAsToken(new ObjectNode("root", new ObjectNode("child", new ObjectNode(new ObjectNode())))));
        parser = new TreeTraversingParser(root, objectCodec);

        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("root", parser.getText());
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("child", parser.getText());
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("", parser.getText());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());

        parser.close();

        token = parser.nextToken();
        assertNull(token);
    }

    private JsonNode createTestNode() {
        return new ObjectNode(objectCodec.treeAsToken(new ObjectNode("field1", new TextNode("value1"))));
    }

    private JsonNode createTestArrayNode() {
        return new ArrayNode(new JsonFactory(), new TextNode("one"), new IntNode(1), new TextNode("three"));
    }

    private JsonNode createTestBinaryNode() {
        return new BinaryNode(new TextNode("test"));
    }
}