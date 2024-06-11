package com.example.junit; // replace with your test package

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TreeTraversingParserTest {

    private TreeTraversingParser parser;
    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
    }

    @Test
    public void testTreeTraversingParser_EmptyJsonNode() throws JsonParseException {
        parser = new TreeTraversingParser(mapper.readTree("{}"));
        Assert.assertNull(parser.nextToken());
        parser.close();
    }

    @Test
    public void testTreeTraversingParser_StringValueNode() throws JsonParseException {
        parser = new TreeTraversingParser(mapper.readTree("{\"str\":\"Hello World\"}"));
        Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        Assert.assertEquals("str", parser.getCurrentName());
        Assert.assertEquals("Hello World", parser.getText());
        Assert.assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        parser.close();
    }

    @Test
    public void testTreeTraversingParser_NumberValueNode() throws JsonParseException {
        parser = new TreeTraversingParser(mapper.readTree("{123:1, 3.14:2}"));
        Assert.assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        Assert.assertEquals("123", parser.getCurrentName());
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        Assert.assertEquals(123, parser.getIntValue());
        Assert.assertEquals(JsonToken.COMMA, parser.nextToken());
        Assert.assertEquals("3.14", parser.getCurrentName());
        Assert.assertEquals(JsonToken.VALUE_NUMBER_FLOAT, parser.nextToken());
        Assert.assertEquals(3.14, parser.getDoubleValue(), 0.001);
        Assert.assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        parser.close();
    }

    @Test
    public void testTreeTraversingParser_ArrayValueNode() throws JsonParseException {
        parser = new TreeTraversingParser(mapper.readTree("[1, 2, 3]"));
        Assert.assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        Assert.assertEquals(1, parser.getIntValue());
        Assert.assertEquals(JsonToken.COMMA, parser.nextToken());
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        Assert.assertEquals(2, parser.getIntValue());
        Assert.assertEquals(JsonToken.COMMA, parser.nextToken());
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        Assert.assertEquals(3, parser.getIntValue());
        Assert.assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        parser.close();
    }

    @Test
    public void testTreeTraversingParser_ComplexJsonNode() throws JsonParseException {
        parser = new TreeTraversingParser(mapper.readTree("{" +
                "\"str\": \"Hello World\"," +
                "\"num\": 123.45," +
                "\"arr\": [1, 2, 3]," +
                "\"obj\": {" +
                "\"str1\": \"Hello\"," +
                "\"num1\": 45.67," +
                "\"arr1\": [\"apple\", \"banana\"]" +
                "}, " +
                "\"null\": null" +
                "}"));

        Assert.assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        Assert.assertEquals("str", parser.getCurrentName());
        Assert.assertEquals("Hello World", parser.getText());
        Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        Assert.assertEquals("num", parser.getCurrentName());
        Assert.assertEquals(JsonToken.VALUE_NUMBER_FLOAT, parser.nextToken());
        Assert.assertEquals(123.45, parser.getDoubleValue(), 0.001);
        Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        Assert.assertEquals("arr", parser.getCurrentName());
        Assert.assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        Assert.assertEquals(1, parser.getIntValue());
        Assert.assertEquals(JsonToken.COMMA, parser.nextToken());
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        Assert.assertEquals(2, parser.getIntValue());
        Assert.assertEquals(JsonToken.COMMA, parser.nextToken());
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        Assert.assertEquals(3, parser.getIntValue());
        Assert.assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        Assert.assertEquals("obj", parser.getCurrentName());
        Assert.assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        Assert.assertEquals("str1", parser.getCurrentName());
        Assert.assertEquals("Hello", parser.getText());
        Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        Assert.assertEquals("num1", parser.getCurrentName());
        Assert.assertEquals(JsonToken.VALUE_NUMBER_FLOAT, parser.nextToken());
        Assert.assertEquals(45.67, parser.getDoubleValue(), 0.001);
        Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        Assert.assertEquals("arr1", parser.getCurrentName());
        Assert.assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        Assert.assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        Assert.assertEquals("apple", parser.getText());
        Assert.assertEquals(JsonToken.COMMA, parser.nextToken());
        Assert.assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        Assert.assertEquals("banana", parser.getText());
        Assert.assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        Assert.assertEquals("null", parser.getCurrentName());
        Assert.assertNull(parser.nextValue());
        Assert.assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        parser.close();
    }

    @Test
    public void testTreeTraversingParser_ClosedBeforeEnd() throws JsonParseException {
        parser = new TreeTraversingParser(mapper.readTree("[]"));
        parser.close();
        Assert.assertThrows(IOException.class, () -> parser.nextToken());
    }

    @Test
    public void testTreeTraversingParser_NullNode() throws JsonParseException {
        parser = new TreeTraversingParser(mapper.readTree(null));
        Assert.assertNull(parser.nextToken());
        parser.close();
    }

    @Test
    public void testTreeTraversingParser_InvalidJsonNode() throws JsonParseException {
        parser = new TreeTraversingParser(mapper.readTree("{invalid:1}"));
        Assert.assertThrows(JsonParseException.class, () -> parser.nextToken());
        parser.close();
    }
}