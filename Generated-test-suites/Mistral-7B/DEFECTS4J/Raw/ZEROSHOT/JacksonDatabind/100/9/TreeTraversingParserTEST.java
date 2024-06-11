package com.example.junit;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.*;

import org.junit.Test;

public class TreeTraversingParserTest {

    @Test
    public void testTreeTraversingParser() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("{'name': 'John Doe', 'age': 30, 'cities': ['New York', 'Los Angeles']}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        // Test nextToken()
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals("name", parser.getCurrentName());
        assertEquals("John Doe", parser.getText());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("age", parser.getCurrentName());
        assertEquals("30", parser.getText());
        assertEquals(JsonToken.VALUE_INT, parser.nextToken());
        assertEquals(JsonToken.COMMA, parser.nextToken());
        assertEquals("cities", parser.getCurrentName());
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals("New York", parser.getText());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("Los Angeles", parser.getText());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertEquals(null, parser.nextToken());

        // Test skipChildren()
        parser = new TreeTraversingParser(jsonNode);
        parser.nextToken(); // "name"
        parser.nextToken(); // "John Doe"
        parser.nextToken(); // "age"
        parser.nextToken(); // "30"
        parser.nextToken(); // ","
        parser.nextToken(); // "cities"
        parser.nextToken(); // "[New York, Los Angeles]"
        parser.nextToken(); // "New York"
        parser.nextToken(); // "New York"
        parser.skipChildren();
        assertEquals("Los Angeles", parser.getText());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());

        // Test isClosed()
        parser = new TreeTraversingParser(jsonNode);
        assertFalse(parser.isClosed());
        parser.nextToken(); // "name"
        parser.nextToken(); // "John Doe"
        parser.nextToken(); // "age"
        parser.nextToken(); // "30"
        parser.nextToken(); // ","
        parser.nextToken(); // "cities"
        parser.nextToken(); // "[New York, Los Angeles]"
        parser.nextToken(); // "New York"
        parser.nextToken(); // "New York"
        parser.skipChildren();
        parser.nextToken(); // "Los Angeles"
        parser.nextToken(); // "Los Angeles"
        parser.nextToken(); // "]"
        parser.nextToken(); // "]"
        assertTrue(parser.isClosed());

        // Test getCurrentName() and overrideCurrentName()
        parser = new TreeTraversingParser(jsonNode);
        assertEquals(null, parser.getCurrentName());
        parser.nextToken(); // "name"
        assertEquals("name", parser.getCurrentName());
        parser.overrideCurrentName("title");
        assertEquals("title", parser.getCurrentName());
        assertEquals("age", parser.nextToken().getCurrentName());

        // Test getText()
        parser = new TreeTraversingParser(jsonNode);
        parser.nextToken(); // "name"
        assertEquals("John Doe", parser.getText());
        parser.nextToken(); // "age"
        assertEquals("30", parser.getText());
        parser.nextToken(); // ","
        parser.nextToken(); // "cities"
        parser.nextToken(); // "[New York, Los Angeles]"
        parser.nextToken(); // "New York"
        parser.nextToken(); // "New York"
        parser.skipChildren();
        parser.nextToken(); // "Los Angeles"
        parser.nextToken(); // "Los Angeles"
        parser.nextToken(); // "]"
        parser.nextToken(); // "]"
        assertEquals(null, parser.getText());

        // Test getTextCharacters() and getTextLength()
        parser = new TreeTraversingParser(jsonNode);
        parser.nextToken(); // "name"
        assertEquals("John Doe".toCharArray(), parser.getTextCharacters());
        assertEquals(10, parser.getTextLength());

        // Test hasTextCharacters()
        parser = new TreeTraversingParser(jsonNode);
        parser.nextToken(); // "name"
        assertFalse(parser.hasTextCharacters());

        // Test getNumberType()
        parser = new TreeTraversingParser(jsonNode);
        parser.nextToken(); // "age"
        assertEquals(JsonToken.VALUE_INT, parser.getNumberType());

        // Test getBigIntegerValue() and getDecimalValue()
        parser = new TreeTraversingParser(jsonNode);
        parser.nextToken(); // "age"
        assertEquals(new BigInteger("30"), parser.getBigIntegerValue());
        parser.nextToken(); // ","
        parser.nextToken(); // "cities"
        JsonNode citiesNode = parser.nextToken();
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        BigDecimal newYorkDecimalValue = parser.getDecimalValue();
        assertEquals(new BigDecimal("40.7128"), newYorkDecimalValue);
        parser.nextToken(); // "New York"
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        parser.nextToken(); // "Los Angeles"
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        parser.nextToken(); // "]"
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertEquals(null, parser.getBigIntegerValue());
        assertEquals(null, parser.getDecimalValue());

        // Test getDoubleValue(), getFloatValue(), getLongValue() and getIntValue()
        parser = new TreeTraversingParser(jsonNode);
        parser.nextToken(); // "age"
        assertEquals(30.0, parser.getDoubleValue(), 0.0);
        assertEquals(30.0f, parser.getFloatValue(), 0.0f);
        assertEquals(30L, parser.getLongValue());
        assertEquals(30, parser.getIntValue());

        // Test getNumberValue()
        parser = new TreeTraversingParser(jsonNode);
        parser.nextToken(); // "age"
        assertEquals(new Number(30), parser.getNumberValue());

        // Test getEmbeddedObject()
        parser = new TreeTraversingParser(jsonNode);
        parser.nextToken(); // "name"
        assertEquals("John Doe", parser.getText());
        parser.nextToken(); // "age"
        assertEquals(30, parser.getIntValue());
        parser.nextToken(); // ","
        parser.nextToken(); // "cities"
        parser.nextToken(); // "[New York, Los Angeles]"
        Object embeddedObject = parser.getEmbeddedObject();
        assertNotNull(embeddedObject);
        assertEquals("[New York, Los Angeles]", embeddedObject.toString());

        // Test isNaN()
        parser = new TreeTraversingParser(jsonNode);
        parser.nextToken(); // "age"
        parser.nextToken(); // "30"
        assertFalse(parser.isNaN());
        parser.nextToken(); // ","
        parser.nextToken(); // "cities"
        parser.nextToken(); // "[New York, Los Angeles]"
        parser.nextToken(); // "New York"
        parser.nextToken(); // "New York"
        parser.skipChildren();
        parser.nextToken(); // "Los Angeles"
        parser.nextToken(); // "Los Angeles"
        parser.nextToken(); // "]"
        parser.nextToken(); // "]"
        parser.nextToken(); // "null"
        assertTrue(parser.isNaN());

        // Test getBinaryValue() and readBinaryValue()
        parser = new TreeTraversingParser(jsonNode);
        parser.nextToken(); // "cities"
        parser.nextToken(); // "[New York, Los Angeles]"
        byte[] binaryData = parser.getBinaryValue(Base64Variant.BASE64_STANDARD);
        assertNull(binaryData);
        parser.nextToken(); // "New York"
        parser.nextToken(); // "New York"
        parser.skipChildren();
        parser.nextToken(); // "Los Angeles"
        parser.nextToken(); // "Los Angeles"
        parser.nextToken(); // "]"
        parser.nextToken(); // "]"
        byte[] binaryDataBase64 = parser.getCurrentNode().binaryValue();
        byte[] expectedBinaryData = new byte[] { 0x4e, 0x65, 0x63, 0x74, 0x52, 0x65, 0x61, 0x75, 0x65, 0x52, 0x65, 0x61, 0x75, 0x49, 0x64, 0x2c, 0x4c, 0x69, 0x6e, 0x65, 0x67, 0x74, 0x68, 0x2c, 0x4c, 0x61, 0x6e, 0x67, 0x74, 0x68, 0x2c, 0x43, 0x68, 0x61, 0x72, 0x73, 0x65, 0x74, 0x54, 0x79, 0x70, 0x65, 0x31, 0x32, 0x33, 0x34 };
        assertArrayEquals(expectedBinaryData, binaryDataBase64);

        parser.nextToken(); // "null"
        byte[] binaryDataNull = parser.getBinaryValue(Base64Variant.BASE64_STANDARD);
        assertNull(binaryDataNull);

        parser.close();
    }
}