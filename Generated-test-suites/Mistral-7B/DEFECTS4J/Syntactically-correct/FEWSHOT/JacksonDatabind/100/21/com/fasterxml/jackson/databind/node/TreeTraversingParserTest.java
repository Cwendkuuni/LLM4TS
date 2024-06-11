package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonToken;
import org.junit.Test;

public class TreeTraversingParserTest {

    @Test
    public void testEmptyParser() {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertEquals(JsonToken.END_DOCUMENT, parser.nextToken());
    }

    @Test
    public void testRootParser() {
        TreeTraversingParser parser = new TreeTraversingParser(Json.parse("{}"));
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
    }

    @Test
    public void testArrayParser() {
        TreeTraversingParser parser = new TreeTraversingParser(Json.parse("[]"));
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
    }

    @Test
    public void testValueParser() {
        TreeTraversingParser parser = new TreeTraversingParser(Json.parse("\"foo\""));
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("foo", parser.getText());
    }

    @Test
    public void testValueNumberParser() {
        TreeTraversingParser parser = new TreeTraversingParser(Json.parse("42"));
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(42, parser.getIntValue());
    }

    @Test
    public void testValueNumberFloatParser() {
        TreeTraversingParser parser = new TreeTraversingParser(Json.parse("3.14"));
        assertEquals(JsonToken.VALUE_NUMBER_FLOAT, parser.nextToken());
        assertEquals(3.14f, parser.getFloatValue());
    }

    @Test
    public void testValueNullParser() {
        TreeTraversingParser parser = new TreeTraversingParser(Json.parse("null"));
        assertEquals(JsonToken.VALUE_NULL, parser.nextToken());
        assertNull(parser.getText());
    }

    @Test
    public void testValueBooleanParser() {
        TreeTraversingParser parser = new TreeTraversingParser(Json.parse("true"));
        assertEquals(JsonToken.VALUE_BOOL, parser.nextToken());
        assertTrue(parser.getBooleanValue());

        parser = new TreeTraversingParser(Json.parse("false"));
        assertEquals(JsonToken.VALUE_BOOL, parser.nextToken());
        assertFalse(parser.getBooleanValue());
    }

    @Test
    public void testValueObjectParser() {
        TreeTraversingParser parser = new TreeTraversingParser(Json.parse("{\"foo\":42}"));
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals("foo", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(42, parser.getIntValue());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
    }

    @Test
    public void testValueArrayParser() {
        TreeTraversingParser parser = new TreeTraversingParser(Json.parse("[1,2,3]"));
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(1, parser.getIntValue());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(2, parser.getIntValue());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(3, parser.getIntValue());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
    }

    @Test
    public void testValueStringParser() {
        TreeTraversingParser parser = new TreeTraversingParser(Json.parse("\"foo bar baz\""));
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("foo", parser.getText());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("bar", parser.getText());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("baz", parser.getText());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
    }

    @Test
    public void testValueNestedObjectParser() {
        TreeTraversingParser parser = new TreeTraversingParser(Json.parse("{\"foo\":{\"bar\":1}}"));
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals("foo", parser.getCurrentName());
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals("bar", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(1, parser.getIntValue());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
    }

    @Test
    public void testValueNestedArrayParser() {
        TreeTraversingParser parser = new TreeTraversingParser(Json.parse("{[1,2,3]}"));
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(1, parser.getIntValue());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(2, parser.getIntValue());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(3, parser.getIntValue());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
    }

    @Test
    public void testValueBinaryParser() {
        TreeTraversingParser parser = new TreeTraversingParser(Json.parse("\"data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAAD/2wBDAAMC..."));
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("data", parser.getCurrentName());
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("image", parser.getCurrentName());
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("jpeg", parser.getCurrentName());
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        String base64Data = parser.getText();
        byte[] decodedData = Base64.decodeBase64(base64Data.substring(base64Data.indexOf(",") + 1));
        assertEquals(decodedData.length, parser.getBinaryValue(null).length);
        assertEquals(decodedData, parser.getBinaryValue(null));
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
    }

    @Test
    public void testContainerParser() {
        TreeTraversingParser parser = new TreeTraversingParser(Json.parse("[1,2,3]"));
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        parser.skipChildren();
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());

        parser = new TreeTraversingParser(Json.parse("{\"foo\":{\"bar\":1}}"));
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        parser.skipChildren();
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
    }
}