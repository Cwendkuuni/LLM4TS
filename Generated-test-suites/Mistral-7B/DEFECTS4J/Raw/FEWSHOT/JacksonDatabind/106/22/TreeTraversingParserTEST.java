package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.ParserMinimalBase;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectCodec;

import org.junit.Test;

public class TreeTraversingParserTest {

    @Test
    public void testTreeTraversingParserEmpty() throws IOException {
        ObjectCodec objectCodec = null;
        TreeTraversingParser parser = new TreeTraversingParser(JsonNode.nullNode(), objectCodec);

        parser.close();

        JsonToken token = parser.nextToken();
        assertNull(token);
    }

    @Test
    public void testTreeTraversingParserArray() throws IOException {
        ObjectCodec objectCodec = null;
        JsonNode jsonNode = JsonNodeFactory.instance.numberNode(1);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode, objectCodec);

        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        parser.skipChildren();
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());

        parser.close();
    }

    @Test
    public void testTreeTraversingParserObject() throws IOException {
        ObjectCodec objectCodec = null;
        JsonNode jsonNode = JsonNodeFactory.instance.objectNode();
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode, objectCodec);

        assertEquals(JsonToken.START_OBJECT, parser.nextToken());

        parser.skipChildren();
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());

        parser.close();
    }

    @Test
    public void testTreeTraversingParserValue() throws IOException {
        ObjectCodec objectCodec = null;
        JsonNode jsonNode = JsonNodeFactory.instance.textNode("Hello World");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode, objectCodec);

        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("Hello World", parser.getText());

        parser.skipChildren();
        parser.close();
    }

    @Test
    public void testTreeTraversingParserNumeric() throws IOException {
        ObjectCodec objectCodec = null;
        JsonNode jsonNode = JsonNodeFactory.instance.numberNode(123);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode, objectCodec);

        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(123, parser.getIntValue());

        parser.skipChildren();
        parser.close();
    }

    @Test
    public void testTreeTraversingParserNested() throws IOException {
        ObjectCodec objectCodec = null;
        JsonNode jsonNode = JsonNodeFactory.instance.objectNode();
        JsonNode arrayNode = JsonNodeFactory.instance.arrayNode();
        JsonNode numberNode = JsonNodeFactory.instance.numberNode(123);

        jsonNode.set("key", arrayNode);
        arrayNode.add(numberNode);

        TreeTraversingParser parser = new TreeTraversingParser(jsonNode, objectCodec);

        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        parser.nextToken(); // skip "key"
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(123, parser.getIntValue());

        parser.skipChildren();
        parser.close();
    }

    @Test
    public void testTreeTraversingParserBinary() throws IOException {
        ObjectCodec objectCodec = null;
        JsonNode jsonNode = JsonNodeFactory.instance.binaryNode(new byte[] {1, 2, 3, 4, 5});
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode, objectCodec);

        byte[] binaryValue = parser.getBinaryValue(Base64Variant.STANDARD);

        assertArrayEquals(new byte[] {1, 2, 3, 4, 5}, binaryValue);

        parser.close();
    }

    @Test
    public void testTreeTraversingParserClosed() throws IOException {
        ObjectCodec objectCodec = null;
        JsonNode jsonNode = JsonNodeFactory.instance.textNode("Hello World");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode, objectCodec);

        parser.close();

        JsonToken token = parser.nextToken();
        assertNull(token);
    }

    @Test(expected = JsonParseException.class)
    public void testTreeTraversingParserInvalidType() throws IOException {
        ObjectCodec objectCodec = null;
        JsonNode jsonNode = JsonNodeFactory.instance.booleanNode(true);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode, objectCodec);

        parser.close();

        assertEquals(JsonToken.VALUE_FALSE, parser.nextToken()); // should be VALUE_BOOLEAN but is a bug

        parser.nextToken(); // should throw an error
    }
}