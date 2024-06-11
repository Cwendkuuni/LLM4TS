package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class TreeTraversingParserTest {

    @Test
    public void testEmptyObject() throws Exception {
        JsonParser parser = new TreeTraversingParser(JsonNodeFactory.instance.objectNode());
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        parser.close();
    }

    @Test
    public void testEmptyArray() throws Exception {
        JsonParser parser = new TreeTraversingParser(JsonNodeFactory.instance.arrayNode());
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        parser.close();
    }

    @Test
    public void testSimpleValue() throws Exception {
        JsonParser parser = new TreeTraversingParser(JsonNodeFactory.instance.textNode("Hello World"));
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("Hello World", parser.getText());
        parser.close();
    }

    @Test
    public void testSimpleValueInt() throws Exception {
        JsonParser parser = new TreeTraversingParser(JsonNodeFactory.instance.numberNode(123));
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(123, parser.getIntValue());
        parser.close();
    }

    @Test
    public void testSimpleValueDouble() throws Exception {
        JsonParser parser = new TreeTraversingParser(JsonNodeFactory.instance.numberNode(123.45));
        assertEquals(JsonToken.VALUE_NUMBER_FLOAT, parser.nextToken());
        assertEquals(123.45, parser.getDoubleValue(), 0.001);
        parser.close();
    }

    @Test
    public void testSimpleValueBigInteger() throws Exception {
        JsonParser parser = new TreeTraversingParser(JsonNodeFactory.instance.numberNode(BigInteger.valueOf(1234567890123456789L)));
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(1234567890123456789L, parser.getBigIntegerValue().longValue());
        parser.close();
    }

    @Test
    public void testSimpleValueBigDecimal() throws Exception {
        JsonParser parser = new TreeTraversingParser(JsonNodeFactory.instance.numberNode(new BigDecimal("1234567890123456789.123456")));
        assertEquals(JsonToken.VALUE_NUMBER_FLOAT, parser.nextToken());
        assertEquals(new BigDecimal("1234567890123456789.123456"), parser.getDecimalValue());
        parser.close();
    }

    @Test
    public void testSimpleValueNull() throws Exception {
        JsonParser parser = new TreeTraversingParser(JsonNodeFactory.instance.nullNode());
        assertEquals(JsonToken.VALUE_NULL, parser.nextToken());
        assertNull(parser.getText());
        parser.close();
    }

    @Test
    public void testSimpleValueBoolean() throws Exception {
        JsonParser parser = new TreeTraversingParser(JsonNodeFactory.instance.booleanNode(true));
        assertEquals(JsonToken.VALUE_TRUE, parser.nextToken());
        assertTrue(parser.isTextEmpty());
        parser.close();

        JsonParser parserFalse = new TreeTraversingParser(JsonNodeFactory.instance.booleanNode(false));
        assertEquals(JsonToken.VALUE_FALSE, parserFalse.nextToken());
        assertTrue(parserFalse.isTextEmpty());
        parserFalse.close();
    }

    @Test
    public void testEmptyStream() throws Exception {
        JsonParser parser = new TreeTraversingParser(JsonNodeFactory.instance.nullNode());
        assertEquals(JsonToken.END_OF_STREAM, parser.nextToken());
        parser.close();
    }

    @Test(expected = JsonParseException.class)
    public void testInvalidValue() throws Exception {
        JsonParser parser = new TreeTraversingParser(JsonNodeFactory.instance.textNode("Invalid Value"));
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        parser.getText(); // consume
        parser.nextToken(); // should throw exception
    }

    @Test
    public void testComplexObject() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree("{\"name\": \"John\", \"age\": 30, \"address\": {\"city\": \"New York\", \"state\": \"NY\"}}");
        JsonParser parser = new TreeTraversingParser(rootNode);

        // test object name
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("name", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("John", parser.getText());
        parser.nextToken(); // END_OBJECT

        // test object "address"
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("address", parser.getCurrentName());
        parser.start(); // START_OBJECT

        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("city", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("New York", parser.getText());
        parser.nextToken(); // END_OBJECT

        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("state", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("NY", parser.getText());
        parser.nextToken(); // END_OBJECT

        parser.nextToken(); // END_OBJECT
        parser.close();
    }

    @Test
    public void testComplexArray() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree("[1, 2, 3, 4, 5]");
        JsonParser parser = new TreeTraversingParser(rootNode);

        // test array element (int value)
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(1, parser.getIntValue());
        parser.nextToken(); // next int value

        // test array empty element
        assertEquals(JsonToken.VALUE_NULL, parser.nextToken());

        // test array element (object)
        parser.start(); // START_OBJECT
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("name", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("John", parser.getText());
        parser.nextToken(); // END_OBJECT

        parser.nextToken(); // next int value

        parser.nextToken(); // END_ARRAY
        parser.close();
    }

    @Test
    public void testContainerSkipping() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree("{\"name\": \"John\", \"age\": 30, \"address\": {\"city\": \"New York\"}}");
        JsonParser parser = new TreeTraversingParser(rootNode);

        parser.skipChildren();
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("address", parser.getCurrentName());
        parser.start(); // START_OBJECT
        parser.nextToken(); // "city"
        parser.nextToken(); // "New York"
        parser.nextToken(); // END_OBJECT
        parser.nextToken(); // END_OBJECT
        parser.nextToken(); // END_OBJECT

        parser.close();
    }
}