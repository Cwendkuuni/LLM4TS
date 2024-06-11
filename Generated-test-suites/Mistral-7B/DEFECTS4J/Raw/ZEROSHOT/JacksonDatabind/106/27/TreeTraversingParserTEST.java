package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.BaseTest;

public class TreeTraversingParserTest extends BaseTest {

    @Test
    public void testTreeTraversingParser_Array() throws IOException {
        JsonNode jsonNode = mapper.valueToTree(new int[] { 1, 2, 3 });
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        // Test nextToken
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals(JsonToken.VALUE_INT, parser.nextToken());
        assertEquals(1, parser.getIntValue());
        assertEquals(JsonToken.VALUE_INT, parser.nextToken());
        assertEquals(2, parser.getIntValue());
        assertEquals(JsonToken.VALUE_INT, parser.nextToken());
        assertEquals(3, parser.getIntValue());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());

        // Test isClosed
        assertEquals(false, parser.isClosed());
        parser.close();
        assertEquals(true, parser.isClosed());

        // Test nextToken after close
        parser = new TreeTraversingParser(jsonNode);
        parser.close();
        assertEquals(JsonToken.NULL, parser.nextToken());

        // Test skipChildren
        parser = new TreeTraversingParser(jsonNode);
        parser.nextToken(); // START_ARRAY
        parser.skipChildren();
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());

        // Test getText
        assertEquals("1", parser.getText());
        assertEquals("2", parser.nextToken().getText());
        assertEquals("3", parser.nextToken().getText());
        assertEquals(null, parser.nextToken().getText());

        // Test getTextCharacters and getTextLength
        char[] text = parser.getTextCharacters();
        assertEquals("1", new String(text, 0, text.length));
        assertEquals(1, parser.getTextLength());

        // Test getTextOffset
        parser.nextToken(); // VALUE_INT
        assertEquals(0, parser.getTextOffset());
    }

    @Test
    public void testTreeTraversingParser_Object() throws IOException {
        JsonNode jsonNode = mapper.valueToTree("{\"name\":\"John\",\"age\":30,\"city\":\"New York\"}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        // Test nextToken
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("name", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("John", parser.getText());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("age", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_INT, parser.nextToken());
        assertEquals(30, parser.getIntValue());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("city", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("New York", parser.getText());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());

        // Test isClosed
        assertEquals(false, parser.isClosed());
        parser.close();
        assertEquals(true, parser.isClosed());

        // Test nextToken after close
        parser = new TreeTraversingParser(jsonNode);
        parser.close();
        assertEquals(JsonToken.NULL, parser.nextToken());

        // Test skipChildren
        parser = new TreeTraversingParser(jsonNode);
        parser.nextToken(); // START_OBJECT
        parser.skipChildren();
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());

        // Test getText
        assertEquals("John", parser.getText());
        assertEquals("30", parser.nextToken().getText());
        assertEquals("New York", parser.nextToken().getText());
        assertEquals(null, parser.nextToken().getText());

        // Test getTextCharacters and getTextLength
        char[] text = parser.getTextCharacters();
        assertEquals("John", new String(text, 0, text.length));
        assertEquals(5, parser.getTextLength());

        // Test getTextOffset
        parser.nextToken(); // FIELD_NAME
        assertEquals(0, parser.getTextOffset());
    }

    @Test
    public void testTreeTraversingParser_ValueNode() throws IOException {
        JsonNode jsonNode = mapper.valueToTree(123);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        // Test nextToken
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(123, parser.getIntValue());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());

        // Test isClosed
        assertEquals(false, parser.isClosed());
        parser.close();
        assertEquals(true, parser.isClosed());

        // Test nextToken after close
        parser = new TreeTraversingParser(jsonNode);
        parser.close();
        assertEquals(JsonToken.NULL, parser.nextToken());

        // Test skipChildren
        parser = new TreeTraversingParser(jsonNode);
        parser.nextToken(); // VALUE_NUMBER_INT
        parser.skipChildren();
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());

        // Test getText
        assertEquals("123", parser.getText());
        assertEquals("", parser.nextToken().getText());
        assertEquals(null, parser.nextToken().getText());

        // Test getTextCharacters and getTextLength
        char[] text = parser.getTextCharacters();
        assertEquals("123", new String(text, 0, text.length));
        assertEquals(3, parser.getTextLength());

        // Test getTextOffset
        parser.nextToken(); // VALUE_NUMBER_INT
        assertEquals(0, parser.getTextOffset());
    }

    @Test
    public void testTreeTraversingParser_Null() throws IOException {
        JsonNode jsonNode = null;
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        // Test nextToken
        assertEquals(JsonToken.START_NULL, parser.nextToken());
        parser.close();

        // Test nextToken after close
        parser = new TreeTraversingParser(jsonNode);
        parser.close();
        assertEquals(JsonToken.NULL, parser.nextToken());

        // Test skipChildren
        parser = new TreeTraversingParser(jsonNode);
        parser.nextToken(); // START_NULL
        parser.skipChildren();
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());

        // Test getText
        assertEquals("", parser.getText());
        assertEquals("", parser.nextToken().getText());
        assertEquals(null, parser.nextToken().getText());

        // Test getTextCharacters and getTextLength
        char[] text = parser.getTextCharacters();
        assertEquals("", new String(text, 0, text.length));
        assertEquals(0, parser.getTextLength());

        // Test getTextOffset
        parser.nextToken(); // START_NULL
        assertEquals(0, parser.getTextOffset());
    }

    @Test(expected = JsonProcessingException.class)
    public void testTreeTraversingParser_UnknownToken() throws IOException {
        JsonNode jsonNode = mapper.valueToTree("abcd");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken(); // VALUE_STRING
        parser.nextToken(); // expected START_ARRAY or START_OBJECT but got UNDEFINED
    }
}