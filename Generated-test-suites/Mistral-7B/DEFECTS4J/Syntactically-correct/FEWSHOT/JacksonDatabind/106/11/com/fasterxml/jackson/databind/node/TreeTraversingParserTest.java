package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.databind.JsonNode;

import org.junit.Test;

public class TreeTraversingParserTest {

    @Test
    public void testCurrentName() throws IOException {
        JsonNode node = Json.parse("{\"name\": \"John\", \"age\": 30}");
        TreeTraversingParser parser = new TreeTraversingParser(node);
        assertEquals("name", parser.getCurrentName());
        parser.nextToken();
        assertEquals("age", parser.getCurrentName());
        parser.nextToken(); // end of object
        assertNull(parser.getCurrentName());
    }

    @Test
    public void testText() throws IOException {
        JsonNode node = Json.parse("{\"name\": \"John\", \"age\": 30}");
        TreeTraversingParser parser = new TreeTraversingParser(node);
        assertEquals("John", parser.getText());
        parser.nextToken();
        assertEquals("30", parser.getText());
        parser.nextToken(); // end of object
        assertNull(parser.getText());
    }

    @Test
    public void testGetIntValue() throws IOException {
        JsonNode node = Json.parse("{\"age\": 30}");
        TreeTraversingParser parser = new TreeTraversingParser(node);
        parser.nextToken(); // start object
        parser.nextToken(); // name "age"
        parser.nextToken(); // int value 30
        assertEquals(30, parser.getIntValue());
        parser.nextToken(); // end of int value
        parser.nextToken(); // end of object
        assertNull(parser.getIntValue());
    }

    @Test
    public void testGetLongValue() throws IOException {
        JsonNode node = Json.parse("{\"id\": 1234567890}");
        TreeTraversingParser parser = new TreeTraversingParser(node);
        parser.nextToken(); // start object
        parser.nextToken(); // name "id"
        parser.nextToken(); // long value 1234567890
        assertEquals(1234567890L, parser.getLongValue());
        parser.nextToken(); // end of long value
        parser.nextToken(); // end of object
        assertNull(parser.getLongValue());
    }

    @Test
    public void testGetDecimalValue() throws IOException {
        JsonNode node = Json.parse("{\"price\": 4.99}");
        TreeTraversingParser parser = new TreeTraversingParser(node);
        parser.nextToken(); // start object
        parser.nextToken(); // name "price"
        parser.nextToken(); // double value 4.99
        assertEquals(new BigDecimal("4.99"), parser.getDecimalValue());
        parser.nextToken(); // end of double value
        parser.nextToken(); // end of object
        assertNull(parser.getDecimalValue());
    }

    @Test
    public void testGetBigIntegerValue() throws IOException {
        JsonNode node = Json.parse("{\"id\": 12345678901234567890}");
        TreeTraversingParser parser = new TreeTraversingParser(node);
        parser.nextToken(); // start object
        parser.nextToken(); // name "id"
        parser.nextToken(); // big integer value 12345678901234567890
        assertEquals(new BigInteger("12345678901234567890"), parser.getBigIntegerValue());
        parser.nextToken(); // end of big integer value
        parser.nextToken(); // end of object
        assertNull(parser.getBigIntegerValue());
    }

    @Test
    public void testGetNumberValue() throws IOException {
        JsonNode node = Json.parse("{\"price\": 4.99}");
        TreeTraversingParser parser = new TreeTraversingParser(node);
        parser.nextToken(); // start object
        parser.nextToken(); // name "price"
        parser.nextToken(); // double value 4.99
        assertEquals(4.99, parser.getNumberValue().doubleValue(), 0.01);
        parser.nextToken(); // end of double value
        parser.nextToken(); // end of object
        assertNull(parser.getNumberValue());
    }

    @Test
    public void testGetNumberType() throws IOException {
        JsonNode node = Json.parse("{\"price\": 4.99, \"quantity\": 3}");
        TreeTraversingParser parser = new TreeTraversingParser(node);
        parser.nextToken(); // start object
        parser.nextToken(); // name "price"
        assertEquals(JsonParser.NumberType.FLOAT, parser.getNumberType());
        parser.nextToken(); // int value 3
        parser.nextToken(); // name "quantity"
        assertEquals(JsonParser.NumberType.INT, parser.getNumberType());
        parser.nextToken(); // end of object
        assertNull(parser.getNumberType());
    }

    @Test
    public void testClose() throws IOException {
        JsonNode node = Json.parse("{\"name\": \"John\", \"age\": 30}");
        TreeTraversingParser parser = new TreeTraversingParser(node);
        parser.nextToken(); // start object
        parser.nextToken(); // name "name"
        parser.nextToken(); // string value "John"
        parser.nextToken(); // end of string value
        parser.nextToken(); // name "age"
        parser.nextToken(); // int value 30
        parser.nextToken(); // end of int value
        parser.nextToken(); // end of object
        parser.close();
        assertNull(parser.getText());
        assertNull(parser.getCurrentName());
        assertNull(parser.getIntValue());
        assertNull(parser.getLongValue());
        assertNull(parser.getDecimalValue());
        assertNull(parser.getBigIntegerValue());
        assertNull(parser.getNumberValue());
        assertNull(parser.getNumberType());
    }
}