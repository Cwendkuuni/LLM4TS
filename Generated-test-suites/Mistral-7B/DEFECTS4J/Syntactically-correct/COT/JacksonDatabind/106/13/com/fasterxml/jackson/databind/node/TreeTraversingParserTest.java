package com.example.junit;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;

import org.junit.Test;

public class TreeTraversingParserTest {

    @Test
    public void testConstructorWithJsonNode() throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree("{\"name\": \"John\", \"age\": 30}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals("name", parser.getCurrentName());
        assertEquals("John", parser.getText());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("age", parser.getCurrentName());
        assertEquals("30", parser.getText());
        assertEquals(JsonToken.VALUE_INT, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertEquals(null, parser.nextToken());
        assertTrue(parser.isClosed());
    }

    @Test
    public void testConstructorWithJsonNodeAndObjectCodec() throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree("{\"name\": \"John\", \"age\": 30}");
        ObjectMapper mapper = new ObjectMapper();
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode, mapper.getFactory().getObjectCodec());

        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals("name", parser.getCurrentName());
        assertEquals("John", parser.getText());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("age", parser.getCurrentName());
        assertEquals("30", parser.getText());
        assertEquals(JsonToken.VALUE_INT, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertEquals(null, parser.nextToken());
        assertTrue(parser.isClosed());
    }

    @Test(expected = JsonParseException.class)
    public void testNextTokenAfterClose() throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree("{\"name\": \"John\", \"age\": 30}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken(); // consume START_OBJECT
        parser.close();
        parser.nextToken(); // should throw exception
    }

    @Test
    public void testSkipChildren() throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree("{\"name\": \"John\", \"age\": 30, \"city\": {\"name\": \"New York\"}}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        parser.skipChildren();
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("city", parser.getCurrentName());
        parser.skipChildren();
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        parser.skipChildren();
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("name", parser.getCurrentName());
        parser.skipChildren();
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("New York", parser.getText());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
    }

    @Test
    public void testTextAccessors() throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree("{\"name\": \"John\", \"age\": 30, \"city\": \"New York\"}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        assertEquals("John", parser.getText());
        assertEquals("John", parser.getText(0, jsonNode.size()));
        assertEquals("30", parser.getText(2, jsonNode.size()));
        assertEquals("New York", parser.getText(4, jsonNode.size()));

        parser.close();
        assertEquals(null, parser.getText());
    }

    @Test
    public void testNumberAccessors() throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree("{\"age\": 30, \"price\": 4.5}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        assertEquals(30, parser.getIntValue());
        assertEquals(30.0, parser.getDoubleValue(), 0.0);
        assertEquals(4.5, parser.getFloatValue(), 0.0f);
        assertEquals(new BigInteger("30"), parser.getBigIntegerValue());
        assertEquals(new BigDecimal("30"), parser.getDecimalValue());

        parser.close();
        assertEquals(null, parser.getIntValue());
        assertEquals(Double.NaN, parser.getDoubleValue(), 0.0);
        assertEquals(Float.NaN, parser.getFloatValue(), 0.0f);
    }

    @Test
    public void testBinaryAccessors() throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree("{\"image\": \"Rm9udCAoCiAgICA8Y2lyY2xlIGN4eXQtdGl0bGUgZG9jdW1lbnQgZnV0dXJlIGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGggdG8gcmVhbGx5IGZvdWwgcGhpbmdzIHdoZXJlIGZvdWwgdGhpbmdzIHN0cm9rZT4=");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        byte[] binaryData = parser.getBinaryValue(Base64Variants.STANDARD);
        assertEquals(16, binaryData.length);

        parser.close();
        assertEquals(null, parser.getBinaryValue(Base64Variants.STANDARD));
    }

    @Test
    public void testClose() throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree("{\"name\": \"John\", \"age\": 30, \"city\": \"New York\"}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        parser.nextToken(); // consume START_OBJECT
        parser.close();

        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        parser.nextToken(); // consume END_OBJECT

        parser.close();
        assertEquals(null, parser.nextToken());
    }
}