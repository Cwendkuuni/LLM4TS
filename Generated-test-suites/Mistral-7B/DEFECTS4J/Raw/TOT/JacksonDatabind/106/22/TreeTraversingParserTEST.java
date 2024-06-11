package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.BaseJsonNode;
import com.fasterxml.jackson.databind.node.TextNode;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

class TreeTraversingParserTest {

    private ObjectMapper mapper;
    private JsonNode inputNode;

    @BeforeAll
    static void setupClass() {
        // Ensure JSON processing is initialized
        new ObjectMapper();
    }

    @BeforeEach
    void setup() throws JsonProcessingException {
        mapper = new ObjectMapper();
        String inputJson = "{\"array\":[1,\"two\",3],\"object\":{\"key\":\"value\"}}";
        inputNode = mapper.readTree(inputJson);
    }

    @Test
    void testTreeTraversingParser_Array() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(inputNode.path("array"));

        // Test nextToken()
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.START_ARRAY, token);

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_INT, token);
        assertEquals(1, parser.getIntValue());

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token);
        assertEquals("two", parser.getText());

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_INT, token);
        assertEquals(3, parser.getIntValue());

        token = parser.nextToken();
        assertEquals(JsonToken.END_ARRAY, token);

        // Test skipChildren()
        parser.skipChildren();
        token = parser.nextToken();
        assertEquals(JsonToken.END_ARRAY, token);

        // Test isClosed()
        parser.close();
        assertTrue(parser.isClosed());

        // Test nextToken() after close
        token = parser.nextToken();
        assertNull(token);
    }

    @Test
    void testTreeTraversingParser_Object() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(inputNode);

        // Test nextToken()
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.START_OBJECT, token);

        token = parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        assertEquals("array", parser.getCurrentName());

        token = parser.nextToken();
        assertEquals(JsonToken.START_ARRAY, token);

        // Test test nextToken() inside array
        JsonToken innerToken = parser.nextToken();
        assertEquals(JsonToken.VALUE_INT, innerToken);
        assertEquals(1, parser.getIntValue());

        innerToken = parser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, innerToken);
        assertEquals("two", parser.getText());

        innerToken = parser.nextToken();
        assertEquals(JsonToken.VALUE_INT, innerToken);
        assertEquals(3, parser.getIntValue());

        token = parser.nextToken();
        assertEquals(JsonToken.END_ARRAY, token);

        // Test nextToken() outside array
        innerToken = parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, innerToken);
        assertEquals("object", parser.getCurrentName());

        token = parser.nextToken();
        assertEquals(JsonToken.START_OBJECT, token);

        // Test test nextToken() inside object
        JsonNode innerObject = parser.nextNode();
        assertEquals("key", innerObject.getName());

        innerToken = parser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, innerToken);
        assertEquals("value", parser.getText());

        token = parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, token);

        token = parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, token);

        // Test skipChildren()
        parser.skipChildren();
        token = parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, token);

        // Test isClosed()
        parser.close();
        assertTrue(parser.isClosed());

        // Test nextToken() after close
        token = parser.nextToken();
        assertNull(token);
    }

    @Test
    void testTreeTraversingParser_ValueNode() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(inputNode.path("/array/1"));

        // Test nextToken()
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.VALUE_INT, token);
        assertEquals(1, parser.getIntValue());

        // Test nextToken() after value node
        token = parser.nextToken();
        assertEquals(JsonToken.END_ARRAY, token);

        // Test isClosed()
        parser.close();
        assertTrue(parser.isClosed());

        // Test nextToken() after close
        token = parser.nextToken();
        assertNull(token);
    }

    @Test
    void testTreeTraversingParser_NullInput() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        JsonToken token = parser.nextToken();
        assertNull(token);
    }

    @Test
    void testTreeTraversingParser_EmptyNode() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(new TextNode(""));
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, token);
    }

    @Test
    void testTreeTraversingParser_BinaryValue() throws IOException {
        // Create an input node with a binary value
        JsonNode inputNodeBinary = mapper.readTree("{\"binary\":[0x01,0x02,0x03]}");
        TreeTraversingParser parser = new TreeTraversingParser(inputNodeBinary.path("binary"));

        // Test nextToken()
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.START_ARRAY, token);

        byte[] binaryValue = parser.getBinaryValue(Base64Variant.STANDARD);
        assertNotNull(binaryValue);
        assertEquals(3, binaryValue.length);

        token = parser.nextToken();
        assertEquals(JsonToken.END_ARRAY, token);

        // Test skipChildren()
        parser.skipChildren();
        token = parser.nextToken();
        assertEquals(JsonToken.END_ARRAY, token);

        // Test isClosed()
        parser.close();
        assertTrue(parser.isClosed());

        // Test nextToken() after close
        token = parser.nextToken();
        assertNull(token);
    }

    @Test
    void testTreeTraversingParser_NullBinaryValue() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(new TextNode(null));
        byte[] binaryValue = parser.getBinaryValue(Base64Variant.STANDARD);
        assertNull(binaryValue);
    }

    @Test
    void testTreeTraversingParser_InvalidTokenType() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(new TextNode("InvalidToken"));

        // Test nextToken()
        JsonToken token = parser.nextToken();
        assertNull(token);
    }

    @Test
    void testTreeTraversingParser_NegativeNumber() throws IOException {
        JsonNode inputNodeNegative = mapper.readTree("{-1}");
        TreeTraversingParser parser = new TreeTraversingParser(inputNodeNegative);

        // Test nextToken()
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, token);
        assertEquals(-1, parser.getIntValue());

        // Test nextToken() after value node
        token = parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, token);

        // Test isClosed()
        parser.close();
        assertTrue(parser.isClosed());

        // Test nextToken() after close
        token = parser.nextToken();
        assertNull(token);
    }

    @Test
    void testTreeTraversingParser_FloatNumber() throws IOException {
        JsonNode inputNodeFloat = mapper.readTree("{3.1415}");
        TreeTraversingParser parser = new TreeTraversingParser(inputNodeFloat);

        // Test nextToken()
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_FLOAT, token);
        assertEquals(3.1415, parser.getDoubleValue(), 0.001);

        // Test nextToken() after value node
        token = parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, token);

        // Test isClosed()
        parser.close();
        assertTrue(parser.isClosed());

        // Test nextToken() after close
        token = parser.nextToken();
        assertNull(token);
    }

    @Test
    void testTreeTraversingParser_BigDecimalNumber() throws IOException {
        JsonNode inputNodeBigDecimal = mapper.readTree("{1234567890.12345678901234567890}");
        TreeTraversingParser parser = new TreeTraversingParser(inputNodeBigDecimal);

        // Test nextToken()
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_FLOAT, token);
        BigDecimal expected = new BigDecimal("1234567890.12345678901234567890");
        BigDecimal actual = parser.getDecimalValue();
        assertEquals(expected, actual);

        // Test nextToken() after value node
        token = parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, token);

        // Test isClosed()
        parser.close();
        assertTrue(parser.isClosed());

        // Test nextToken() after close
        token = parser.nextToken();
        assertNull(token);
    }

    @Test
    void testTreeTraversingParser_BigIntegerNumber() throws IOException {
        JsonNode inputNodeBigInteger = mapper.readTree("{12345678901234567890}");
        TreeTraversingParser parser = new TreeTraversingParser(inputNodeBigInteger);

        // Test nextToken()
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, token);
        BigInteger expected = new BigInteger("12345678901234567890");
        BigInteger actual = parser.getBigIntegerValue();
        assertEquals(expected, actual);

        // Test nextToken() after value node
        token = parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, token);

        // Test isClosed()
        parser.close();
        assertTrue(parser.isClosed());

        // Test nextToken() after close
        token = parser.nextToken();
        assertNull(token);
    }

    @Test
    void testTreeTraversingParser_NaN() throws IOException {
        JsonNode inputNodeNaN = mapper.readTree("{NaN}");
        TreeTraversingParser parser = new TreeTraversingParser(inputNodeNaN);

        // Test nextToken()
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, token);
        assertFalse(parser.isNaN());

        // Test nextToken() after value node
        token = parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, token);

        // Test isClosed()
        parser.close();
        assertTrue(parser.isClosed());

        // Test nextToken() after close
        token = parser.nextToken();
        assertNull(token);

        // Test isNaN()
        parser = new TreeTraversingParser(new TextNode("NaN"));
        parser.nextToken();
        assertTrue(parser.isNaN());

        // Test isNaN() after close
        parser.close();
        assertTrue(parser.isClosed());
        assertTrue(parser.isNaN());
    }
}