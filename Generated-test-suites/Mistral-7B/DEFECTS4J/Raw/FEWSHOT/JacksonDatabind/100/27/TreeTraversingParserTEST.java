package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;

import org.junit.Test;

public class TreeTraversingParserTest {
    @Test
    public void testConfiguration() {
        TreeTraversingParser parser = new TreeTraversingParser(createJsonNode(new ObjectMapper(), "[1, 2, 3]"));
        assertEquals(parser.getClass(), TreeTraversingParser.class);
        assertNull(parser.getCodec());
        ObjectCodec codec = new ObjectMapper();
        parser = new TreeTraversingParser(createJsonNode(codec, "[1, 2, 3]"), codec);
        assertEquals(codec, parser.getCodec());

        parser = new TreeTraversingParser(createJsonNode(codec, "{\"name\": \"John\", \"age\": 30}"));
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals("name", parser.getCurrentName());
        assertEquals("John", parser.getText());
        parser.skipChildren();
        assertEquals("age", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(30, parser.getIntValue());
        parser.close();
    }

    @Test
    public void testCloseableImplementation() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(createJsonNode(new ObjectMapper(), "[1, 2, 3]"));
        parser.close();
        assertEquals(parser.isClosed(), true);
        try {
            parser.nextToken();
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }

        parser = new TreeTraversingParser(createJsonNode(new ObjectMapper(), "[1, 2, 3]"));
        parser.nextToken();
        parser.close();
        assertEquals(parser.isClosed(), true);
        assertNull(parser.nextToken());
    }

    @Test
    public void testPublicApiTraversal() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(createJsonNode(new ObjectMapper(), "[1, 2, 3]"));
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals(1, parser.getIntValue());
        parser.skipChildren();
        assertEquals(2, parser.getIntValue());
        parser.skipChildren();
        assertEquals(3, parser.getIntValue());
        parser.close();

        parser = new TreeTraversingParser(createJsonNode(new ObjectMapper(), "{\"name\": \"John\", \"age\": 30}"));
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals("name", parser.getCurrentName());
        assertEquals("John", parser.getText());
        parser.skipChildren();
        assertEquals("age", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(30, parser.getIntValue());
        parser.close();
    }

    @Test
    public void testPublicApiTokenAccessors() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(createJsonNode(new ObjectMapper(), "[1, 2, 3]"));
        assertEquals("", parser.getText());
        assertEquals(null, parser.getCurrentName());
        assertEquals(JsonLocation.NA, parser.getParsingContext());
        assertEquals(JsonLocation.NA, parser.getTokenLocation());

        parser.nextToken();
        assertEquals("[", parser.getText());
        assertEquals("array", parser.getCurrentName());
        assertEquals(parser.getParsingContext(), parser.getParsingContext()); // same instance
        assertEquals(parser.getTokenLocation(), parser.getTokenLocation()); // same instance

        parser.nextToken();
        assertEquals(1, parser.getIntValue());
        assertEquals("1", parser.getText());
        assertEquals("number", parser.getCurrentName());
        assertEquals(parser.getParsingContext(), parser.getParsingContext()); // same instance
        assertEquals(parser.getTokenLocation(), parser.getTokenLocation()); // same instance

        parser.skipChildren();

        parser.nextToken();
        assertEquals(2, parser.getIntValue());
        assertEquals("2", parser.getText());
        assertEquals("number", parser.getCurrentName());
        assertEquals(parser.getParsingContext(), parser.getParsingContext()); // same instance
        assertEquals(parser.getTokenLocation(), parser.getTokenLocation()); // same instance

        parser.skipChildren();

        parser.nextToken();
        assertEquals(3, parser.getIntValue());
        assertEquals("3", parser.getText());
        assertEquals("number", parser.getCurrentName());
        assertEquals(parser.getParsingContext(), parser.getParsingContext()); // same instance
        assertEquals(parser.getTokenLocation(), parser.getTokenLocation()); // same instance

        parser.close();

        parser = new TreeTraversingParser(createJsonNode(new ObjectMapper(), "{\"name\": \"John\", \"age\": 30}"));
        parser.nextToken();
        assertEquals("{\"name\": \"John\", \"age\": 30}", parser.getText());
        assertEquals("object", parser.getCurrentName());
        assertEquals(parser.getParsingContext(), parser.getParsingContext()); // same instance
        assertEquals(parser.getTokenLocation(), parser.getTokenLocation()); // same instance

        parser.nextToken();
        assertEquals("name", parser.getCurrentName());
        assertEquals("name", parser.getText());
        assertEquals("name", parser.getCurrentName());
        assertEquals(parser.getParsingContext(), parser.getParsingContext()); // same instance
        assertEquals(parser.getTokenLocation(), parser.getTokenLocation()); // same instance

        parser.skipChildren();

        parser.nextToken();
        assertEquals("age", parser.getCurrentName());
        assertEquals("age", parser.getText());
        assertEquals("age", parser.getCurrentName());
        assertEquals(parser.getParsingContext(), parser.getParsingContext()); // same instance
        assertEquals(parser.getTokenLocation(), parser.getTokenLocation()); // same instance

        parser.nextToken();
        assertEquals(30, parser.getIntValue());
        assertEquals("30", parser.getText());
        assertEquals("number", parser.getCurrentName());
        assertEquals(parser.getParsingContext(), parser.getParsingContext()); // same instance
        assertEquals(parser.getTokenLocation(), parser.getTokenLocation()); // same instance

        parser.close();
    }

    @Test
    public void testPublicApiTextualContent() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(createJsonNode(new ObjectMapper(), "[1, 2, 3]"));
        assertEquals("1", parser.getText());
        parser.nextToken();
        assertEquals("2", parser.getText());
        parser.nextToken();
        assertEquals("3", parser.getText());
        parser.close();

        parser = new TreeTraversingParser(createJsonNode(new ObjectMapper(), "{\"name\": \"John\", \"age\": 30}"));
        assertEquals("{\"name\": \"John\", \"age\": 30}", parser.getText());
        parser.nextToken();
        assertEquals("name", parser.getText());
        parser.nextToken();
        assertEquals("John", parser.getText());
        parser.skipChildren();
        parser.nextToken();
        assertEquals("age", parser.getText());
        parser.nextToken();
        assertEquals("30", parser.getText());
        parser.close();
    }

    @Test
    public void testPublicApiTypedNonTextAccess() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(createJsonNode(new ObjectMapper(), "[1, 2, 3]"));
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(1, parser.getIntValue());
        parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(2, parser.getIntValue());
        parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(3, parser.getIntValue());
        parser.close();

        parser = new TreeTraversingParser(createJsonNode(new ObjectMapper(), "{\"name\": \"John\", \"age\": 30}"));
        parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("name", parser.getText());
        parser.nextToken();
        assertEquals("John", parser.getText());
        parser.skipChildren();
        parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("age", parser.getText());
        parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(30, parser.getIntValue());
        parser.close();
    }

    @Test
    public void testPublicApiTypedBinaryAccess() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(createJsonNode(new ObjectMapper(), "{\"name\": \"John\", \"data\": \"VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwtdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g="));
        byte[] data = parser.getBinaryValue(Base64Variants.STANDARD);
        assertSame(new byte[] { 76, 76, 76, 76, 76 }, data);
        assertEquals(data.length, parser.readBinaryValue(Base64Variants.STANDARD, new ByteArrayOutputStream()));
        parser.close();
    }

    private static JsonNode createJsonNode(ObjectMapper mapper, String jsonString) throws IOException {
        return mapper.readTree(jsonString);
    }
}