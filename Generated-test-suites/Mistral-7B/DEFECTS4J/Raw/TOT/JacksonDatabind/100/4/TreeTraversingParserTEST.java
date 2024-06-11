package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class TreeTraversingParserTest {

    private JsonParser parser;
    private JsonNode rootNode;

    @BeforeEach
    public void setup() throws IOException {
        JsonFactory jsonFactory = new ObjectMapper().getFactory();
        rootNode = jsonFactory.readTree("{\"name\":\"John Doe\",\"age\":30,\"skills\":[\"Java\",\"Python\",\"JavaScript\"]}");
        parser = new TreeTraversingParser(rootNode);
    }

    @Test
    public void testNextToken_RootNode() throws IOException {
        JsonToken token = parser.nextToken();
        Assertions.assertEquals(JsonToken.START_OBJECT, token);

        token = parser.nextToken();
        Assertions.assertEquals(JsonToken.FIELD_NAME, token);
        Assertions.assertEquals("name", token.asString());

        token = parser.nextToken();
        Assertions.assertEquals(JsonToken.VALUE_STRING, token);
        Assertions.assertEquals("John Doe", token.asString());

        token = parser.nextToken();
        Assertions.assertEquals(JsonToken.FIELD_NAME, token);
        Assertions.assertEquals("age", token.asString());

        token = parser.nextToken();
        Assertions.assertEquals(JsonToken.VALUE_INT, token);
        Assertions.assertEquals(30, token.intValue());

        token = parser.nextToken();
        Assertions.assertEquals(JsonToken.FIELD_NAME, token);
        Assertions.assertEquals("skills", token.asString());

        token = parser.nextToken();
        Assertions.assertEquals(JsonToken.START_ARRAY, token);

        token = parser.nextToken();
        Assertions.assertEquals(JsonToken.VALUE_STRING, token);
        Assertions.assertEquals("Java", token.asString());

        token = parser.nextToken();
        Assertions.assertEquals(JsonToken.VALUE_STRING, token);
        Assertions.assertEquals("Python", token.asString());

        token = parser.nextToken();
        Assertions.assertEquals(JsonToken.VALUE_STRING, token);
        Assertions.assertEquals("JavaScript", token.asString());

        token = parser.nextToken();
        Assertions.assertEquals(JsonToken.END_ARRAY, token);

        token = parser.nextToken();
        Assertions.assertEquals(JsonToken.END_OBJECT, token);

        Assertions.assertTrue(parser.isClosed());
    }

    @Test
    public void testNextToken_EmptyArrayNode() throws IOException {
        rootNode = jsonFactory.readTree("[]");
        parser = new TreeTraversingParser(rootNode);

        JsonToken token = parser.nextToken();
        Assertions.assertEquals(JsonToken.START_ARRAY, token);

        token = parser.nextToken();
        Assertions.assertEquals(JsonToken.END_ARRAY, token);

        token = parser.nextToken();
        Assertions.assertEquals(JsonToken.END_OBJECT, token);

        Assertions.assertTrue(parser.isClosed());
    }

    @Test
    public void testNextToken_EmptyObjectNode() throws IOException {
        rootNode = jsonFactory.readTree("{}");
        parser = new TreeTraversingParser(rootNode);

        JsonToken token = parser.nextToken();
        Assertions.assertEquals(JsonToken.START_OBJECT, token);

        token = parser.nextToken();
        Assertions.assertEquals(JsonToken.END_OBJECT, token);

        Assertions.assertTrue(parser.isClosed());
    }

    @Test
    public void testNextToken_NullNode() throws IOException {
        rootNode = null;
        parser = new TreeTraversingParser(rootNode);

        JsonToken token = parser.nextToken();
        Assertions.assertNull(token);

        Assertions.assertTrue(parser.isClosed());
    }

    @Test
    public void testNextToken_ErrorNode() throws IOException {
        rootNode = jsonFactory.readTree("{\"name\":\"John Doe\"}");
        parser = new TreeTraversingParser(rootNode);

        parser.nextToken(); // consume "START_OBJECT"

        parser.setCodec(null);

        JsonToken token = parser.nextToken();
        Assertions.assertEquals(JsonToken.INVALID_TOKEN, token);

        Assertions.assertTrue(parser.isClosed());
    }

    @Test
    public void testNextToken_SkipChildren() throws IOException {
        rootNode = jsonFactory.readTree("{\"name\":\"John Doe\",\"age\":30,\"skills\":[\"Java\",\"Python\",\"JavaScript\"]}");
        parser = new TreeTraversingParser(rootNode);

        parser.nextToken(); // consume "START_OBJECT"
        parser.nextToken(); // consume "name"
        parser.nextToken(); // consume "John Doe"

        parser.skipChildren();

        JsonToken token = parser.nextToken();
        Assertions.assertEquals(JsonToken.FIELD_NAME, token);
        Assertions.assertEquals("age", token.asString());

        token = parser.nextToken();
        Assertions.assertEquals(JsonToken.VALUE_INT, token);
        Assertions.assertEquals(30, token.intValue());

        token = parser.nextToken();
        Assertions.assertEquals(JsonToken.FIELD_NAME, token);
        Assertions.assertEquals("skills", token.asString());

        token = parser.nextToken();
        Assertions.assertEquals(JsonToken.START_ARRAY, token);

        parser.skipChildren();

        token = parser.nextToken();
        Assertions.assertEquals(JsonToken.END_ARRAY, token);

        token = parser.nextToken();
        Assertions.assertEquals(JsonToken.END_OBJECT, token);

        Assertions.assertTrue(parser.isClosed());
    }

    @Test
    public void testNextToken_ReadBinaryValue() throws IOException {
        JsonNode binaryNode = jsonFactory.readTree("{\"binary\":\"RnJvbSBzbyBmYWxzLg==\"}");
        parser = new TreeTraversingParser(binaryNode);

        byte[] binaryData = parser.getBinaryValue(Base64Variants.BASE64_STANDARD);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int bytesRead = parser.readBinaryValue(Base64Variants.BASE64_STANDARD, outputStream);

        Assertions.assertArrayEquals(binaryData, outputStream.toByteArray());
        Assertions.assertEquals(binaryData.length, bytesRead);

        Assertions.assertTrue(parser.isClosed());
    }
}