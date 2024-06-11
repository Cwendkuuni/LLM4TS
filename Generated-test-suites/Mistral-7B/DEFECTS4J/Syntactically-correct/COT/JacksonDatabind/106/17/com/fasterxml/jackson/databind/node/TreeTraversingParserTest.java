package com.example.junit; // replace with your test package if needed

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;

import org.junit.Test;

public class TreeTraversingParserTest {

    @Test
    public void testConstructorWithJsonNode() {
        JsonNode jsonNode = new ObjectMapper().readTree("{'name': 'John Doe'}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        assertSame(jsonNode, parser.getParsingContext().getCurrentNode());
    }

    @Test
    public void testConstructorWithJsonNodeAndCodec() {
        ObjectMapper objectMapper = new ObjectMapper();
        TreeTraversingParser parser = new TreeTraversingParser(new ObjectNode(objectMapper.getFactory()), objectMapper);
        assertNotNull(parser.getCodec());
    }

    @Test
    public void testNextTokenReturnsNullWhenEndOfStreamReached() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree("{'name': 'John Doe'}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        JsonToken token = parser.nextToken();
        assertNotNull(token);
        token = parser.nextToken();
        assertNull(token);
    }

    @Test(expected = JsonParseException.class)
    public void testNextTokenThrowsExceptionWhenParserIsClosed() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree("{'name': 'John Doe'}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.close();
        parser.nextToken();
    }

    @Test
    public void testNextTokenReturnsExpectedTokenForSimpleJsonNode() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree("{'name': 'John Doe'}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        JsonToken expectedToken = JsonToken.FIELD_NAME;
        JsonToken token = parser.nextToken();
        assertEquals(expectedToken, token);

        expectedToken = JsonToken.VALUE_STRING;
        token = parser.nextToken();
        assertEquals(expectedToken, token);

        expectedToken = JsonToken.END_OBJECT;
        token = parser.nextToken();
        assertEquals(expectedToken, token);
    }

    @Test
    public void testSkipChildren() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree("{'name': {'age': 25}, 'hobbies': ['swimming', 'reading']}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        parser.nextToken();
        parser.nextToken();

        parser.skipChildren();

        JsonToken expectedToken = JsonToken.START_ARRAY;
        JsonToken token = parser.nextToken();
        assertEquals(expectedToken, token);

        expectedToken = JsonToken.VALUE_STRING;
        token = parser.nextToken();
        assertEquals(expectedToken, token);

        expectedToken = JsonToken.VALUE_STRING;
        token = parser.nextToken();
        assertEquals(expectedToken, token);

        expectedToken = JsonToken.END_ARRAY;
        token = parser.nextToken();
        assertEquals(expectedToken, token);
    }

    @Test
    public void testGetStringValue() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree("{'name': 'John Doe'}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        parser.nextToken();
        parser.nextToken();

        String value = parser.getText();
        assertEquals("Doe", value);
    }

    @Test(expected = JsonParseException.class)
    public void testGetTextThrowsExceptionWhenParserIsClosed() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree("{'name': 'John Doe'}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.close();
        parser.getText();
    }

    @Test
    public void testGetNumberValue() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree("{'age': 25}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        parser.nextToken();
        parser.nextToken();

        int value = parser.getIntValue();
        assertEquals(25, value);
    }

    @Test(expected = JsonParseException.class)
    public void testGetIntValueThrowsExceptionWhenParserIsClosed() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree("{'age': 25}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.close();
        parser.getIntValue();
    }

    @Test
    public void testGetBigIntegerValue() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree("{'id': 123456789012345678901234567890}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        parser.nextToken();
        parser.nextToken();

        BigInteger value = parser.getBigIntegerValue();
        assertEquals(new BigInteger("123456789012345678901234567890"), value);
    }

    @Test(expected = JsonParseException.class)
    public void testGetBigIntegerValueThrowsExceptionWhenParserIsClosed() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree("{'id': 123456789012345678901234567890}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.close();
        parser.getBigIntegerValue();
    }

    @Test
    public void testGetDecimalValue() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree("{'price': 123.45}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        parser.nextToken();
        parser.nextToken();

        BigDecimal value = parser.getDecimalValue();
        assertEquals(new BigDecimal("123.45"), value);
    }

    @Test(expected = JsonParseException.class)
    public void testGetDecimalValueThrowsExceptionWhenParserIsClosed() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree("{'price': 123.45}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.close();
        parser.getDecimalValue();
    }

    @Test
    public void testGetDoubleValue() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree("{'price': 123.45}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        parser.nextToken();
        parser.nextToken();

        double value = parser.getDoubleValue();
        assertEquals(123.45, value, 0.0);
    }

    @Test(expected = JsonParseException.class)
    public void testGetDoubleValueThrowsExceptionWhenParserIsClosed() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree("{'price': 123.45}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.close();
        parser.getDoubleValue();
    }

    @Test
    public void testCloseParser() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree("{'name': 'John Doe'}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.close();

        JsonToken token = parser.nextToken();
        assertNull(token);
    }
}