package com.example.junit;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Test;

public class TreeTraversingParserTest {

    @Test
    public void testConstructorWithJsonNode() {
        JsonNode jsonNode = new ObjectMapper(new JsonFactory())
                .readTree("{\"name\": \"John\", \"age\": 30}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        assertNotNull(parser);
    }

    @Test
    public void testConstructorWithJsonNodeAndCodec() {
        JsonNode jsonNode = new ObjectMapper(new JsonFactory())
                .readTree("{\"name\": \"John\", \"age\": 30}");
        ObjectMapper mapper = new ObjectMapper(new JsonFactory());
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode, mapper);
        assertNotNull(parser);
    }

    @Test
    public void testNextToken_RootNode() throws IOException {
        JsonNode jsonNode = new ObjectMapper(new JsonFactory())
                .readTree("{\"name\": \"John\", \"age\": 30}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.START_OBJECT, token);
    }

    @Test
    public void testNextToken_ArrayNode() throws IOException {
        JsonNode jsonNode = new ObjectMapper(new JsonFactory())
                .readTree("[1, 2, 3]");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.START_ARRAY, token);
    }

    @Test
    public void testNextToken_ValueNode() throws IOException {
        JsonNode jsonNode = new ObjectMapper(new JsonFactory())
                .readTree("{\"name\": \"John\", \"age\": 30}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        assertEquals("name", parser.getText());

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token);
        assertEquals("John", parser.getText());

        token = parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        assertEquals("age", parser.getText());

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, token);
        assertEquals(30, parser.getIntValue());

        token = parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, token);
    }

    @Test(expected = JsonParseException.class)
    public void testNextToken_ClosedParser() throws IOException {
        JsonNode jsonNode = new ObjectMapper(new JsonFactory())
                .readTree("{\"name\": \"John\", \"age\": 30}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.close();
        parser.nextToken();
    }

    @Test
    public void testSkipChildren() throws IOException {
        JsonNode jsonNode = new ObjectMapper(new JsonFactory())
                .readTree("{\"name\": \"John\", \"age\": 30, \"hobbies\": [\"football\", \"reading\"]}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.skipChildren();

        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.END_ARRAY, token);
    }

    @Test
    public void testGetText() throws IOException {
        JsonNode jsonNode = new ObjectMapper(new JsonFactory())
                .readTree("{\"name\": \"John\", \"age\": 30}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        parser.nextToken();
        parser.nextToken();
        assertEquals("John", parser.getText());

        parser.nextToken();
        assertEquals("age", parser.getText());

        parser.nextToken();
        parser.nextToken();
        assertEquals("30", parser.getText());

        parser.nextToken();
        assertEquals("", parser.getText());
    }

    @Test
    public void testGetTextCharacters() throws IOException {
        JsonNode jsonNode = new ObjectMapper(new JsonFactory())
                .readTree("{\"name\": \"John\", \"age\": 30}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        parser.nextToken();
        parser.nextToken();
        char[] characters = parser.getTextCharacters();
        assertEquals("John", new String(characters, 0, characters.length));

        parser.nextToken();
        assertEquals("age", parser.getText());

        parser.nextToken();
        parser.nextToken();
        characters = parser.getTextCharacters();
        assertEquals("30", new String(characters, 0, characters.length));

        parser.nextToken();
        assertEquals("", parser.getText());
    }

    @Test
    public void testGetTextLength() throws IOException {
        JsonNode jsonNode = new ObjectMapper(new JsonFactory())
                .readTree("{\"name\": \"John\", \"age\": 30}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        parser.nextToken();
        parser.nextToken();
        assertEquals(4, parser.getTextLength());

        parser.nextToken();
        assertEquals(4, parser.getTextLength());

        parser.nextToken();
        parser.nextToken();
        assertEquals(3, parser.getTextLength());

        parser.nextToken();
        assertEquals(0, parser.getTextLength());
    }

    @Test
    public void testHasTextCharacters() {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertFalse(parser.hasTextCharacters());
    }

    @Test
    public void testGetNumberType() throws IOException {
        JsonNode jsonNode = new ObjectMapper(new JsonFactory())
                .readTree("{\"name\": \"John\", \"age\": 30.5}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        parser.nextToken();
        parser.nextToken();
        assertEquals(JsonType.STRING, parser.getNumberType());

        parser.nextToken();
        parser.nextToken();
        assertEquals(JsonType.NUMBER, parser.getNumberType());
    }

    @Test
    public void testGetBigIntegerValue() throws IOException {
        JsonNode jsonNode = new ObjectMapper(new JsonFactory())
                .readTree("{\"name\": \"John\", \"age\": 3000000000000000000}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        parser.nextToken();
        parser.nextToken();
        assertEquals(BigInteger.ZERO, parser.getBigIntegerValue());

        parser.nextToken();
        parser.nextToken();
        BigInteger bigInteger = parser.getBigIntegerValue();
        assertEquals(3000000000000000000L, bigInteger.longValueExact());
    }

    @Test
    public void testGetDecimalValue() throws IOException {
        JsonNode jsonNode = new ObjectMapper(new JsonFactory())
                .readTree("{\"name\": \"John\", \"age\": 30.5}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        parser.nextToken();
        parser.nextToken();
        assertEquals(new BigDecimal("30.5"), parser.getDecimalValue());

        parser.nextToken();
        parser.nextToken();
        assertEquals(new BigDecimal("30"), parser.getDecimalValue());
    }

    @Test
    public void testGetDoubleValue() throws IOException {
        JsonNode jsonNode = new ObjectMapper(new JsonFactory())
                .readTree("{\"name\": \"John\", \"age\": 30.5}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        parser.nextToken();
        parser.nextToken();
        assertEquals(30.0, parser.getDoubleValue(), 0.001);

        parser.nextToken();
        parser.nextToken();
        assertEquals(30.5, parser.getDoubleValue(), 0.001);
    }

    @Test
    public void testGetFloatValue() throws IOException {
        JsonNode jsonNode = new ObjectMapper(new JsonFactory())
                .readTree("{\"name\": \"John\", \"age\": 30.5}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        parser.nextToken();
        parser.nextToken();
        assertEquals(30.0f, parser.getFloatValue(), 0.001f);

        parser.nextToken();
        parser.nextToken();
        assertEquals(30.5f, parser.getFloatValue(), 0.001f);
    }

    @Test
    public void testGetLongValue() throws IOException {
        JsonNode jsonNode = new ObjectMapper(new JsonFactory())
                .readTree("{\"name\": \"John\", \"age\": 3000000000000000000}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        parser.nextToken();
        parser.nextToken();
        assertEquals(0L, parser.getLongValue());

        parser.nextToken();
        parser.nextToken();
        assertEquals(3000000000000000000L, parser.getLongValue());
    }

    @Test
    public void testGetIntValue() throws IOException {
        JsonNode jsonNode = new ObjectMapper(new JsonFactory())
                .readTree("{\"name\": \"John\", \"age\": 30}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        parser.nextToken();
        parser.nextToken();
        assertEquals(0, parser.getIntValue());

        parser.nextToken();
        parser.nextToken();
        assertEquals(30, parser.getIntValue());
    }

    @Test
    public void testGetNumberValue() throws IOException {
        JsonNode jsonNode = new ObjectMapper(new JsonFactory())
                .readTree("{\"name\": \"John\", \"age\": 30.5}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        parser.nextToken();
        parser.nextToken();
        assertEquals(30.5, parser.getNumberValue().doubleValue(), 0.001);

        parser.nextToken();
        parser.nextToken();
        assertEquals(30, parser.getNumberValue().intValue());
    }

    @Test
    public void testGetEmbeddedObject() throws IOException {
        JsonNode jsonNode = new ObjectMapper(new JsonFactory())
                .readTree("{\"name\": \"John\", \"age\": 30, \"hobbies\": [{\"name\": \"football\"}, {\"name\": \"reading\"}]}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        Object hobby1 = parser.getEmbeddedObject();
        assertEquals("football", ((ArrayNode) hobby1).get(0).asText());

        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        Object hobby2 = parser.getEmbeddedObject();
        assertEquals("reading", ((ArrayNode) hobby2).get(0).asText());
    }

    @Test
    public void testIsNaN() throws IOException {
        JsonNode jsonNode = new ObjectMapper(new JsonFactory())
                .readTree("{\"name\": \"John\", \"age\": NaN}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken();
        parser.nextToken();
        assertTrue(parser.isNaN());
    }

    @Test
    public void testGetBinaryValue() throws IOException {
        JsonNode jsonNode = new ObjectMapper(new JsonFactory())
                .readTree("{\"name\": \"John\", \"image\": \"data:image/jpeg;base64,/9j/2wB\"}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        byte[] binaryData = parser.getBinaryValue(Base64Variants.STANDARD);
        assertNotNull(binaryData);
        // Here you should add the assertions checking the binary data
        // according to your requirements.

        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
    }

    @Test
    public void testReadBinaryValue() throws IOException {
        JsonNode jsonNode = new ObjectMapper(new JsonFactory())
                .readTree("{\"name\": \"John\", \"image\": \"data:image/jpeg;base64,/9j/2wB\"}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        byte[] binaryData = new byte[10];
        int size = parser.readBinaryValue(Base64Variants.STANDARD, new ByteArrayOutputStream(binaryData));
        assertEquals(10, size);
        // Here you should add the assertions checking the binary data
        // according to your requirements.

        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
    }

    @Test(expected = JsonParseException.class)
    public void testGetEmbeddedObject_null() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        parser.nextToken();
        parser.nextToken();
        parser.getEmbeddedObject();
    }

    @Test(expected = JsonParseException.class)
    public void testReadBinaryValue_null() throws IOException {
        JsonNode jsonNode = new ObjectMapper(new JsonFactory())
                .readTree("{\"name\": \"John\"}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        parser.nextToken();
        parser.nextToken();
        parser.readBinaryValue(Base64Variants.STANDARD, new ByteArrayOutputStream(new byte[1]));
    }
}