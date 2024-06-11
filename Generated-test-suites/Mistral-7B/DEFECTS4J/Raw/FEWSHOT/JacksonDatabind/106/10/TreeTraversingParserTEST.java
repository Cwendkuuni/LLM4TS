package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.ParserMinimalBase;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Test class for TreeTraversingParser
 */
public class TreeTraversingParserTest {

    @Test
    public void testEmpty() throws IOException {
        JsonNode jsonNode = null;
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        assertEquals(JsonToken.END_DOCUMENT, parser.nextToken());
        assertTrue(parser.isClosed());
    }

    @Test
    public void testArray() throws IOException {
        String json = "[1,2,3]";
        JsonNode jsonNode = JsonFactory.instance.readTree(json);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        // Start of array
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());

        // Array elements
        for (int i = 0; i < 3; i++) {
            assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
            int value = parser.getIntValue();
            assertEquals(i + 1, value);
        }

        // End of array
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());

        assertTrue(parser.isClosed());
    }

    @Test
    public void testObject() throws IOException {
        String json = "{\"name\":\"John\",\"age\":30,\"city\":\"New York\"}";
        JsonNode jsonNode = JsonFactory.instance.readTree(json);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        // Start of object
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());

        // Object keys and values
        assertEquals("name", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("John", parser.getText());
        assertEquals("age", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(30, parser.getIntValue());
        assertEquals("city", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("New York", parser.getText());

        // End of object
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());

        assertTrue(parser.isClosed());
    }

    @Test
    public void testValue() throws IOException {
        String json = "3.14";
        JsonNode jsonNode = JsonFactory.instance.readTree(json);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        // Value
        assertEquals(JsonToken.VALUE_NUMBER_FLOAT, parser.nextToken());
        assertEquals(3.14f, parser.getFloatValue(), 0.01f);

        assertTrue(parser.isClosed());
    }

    @Test
    public void testBinary() throws IOException {
        String json = "{\"data\":\"RnJvbSBzcGFjZSg=\"}";
        JsonNode jsonNode = JsonFactory.instance.readTree(json);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        // Binary data
        assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, parser.nextToken());
        byte[] binaryData = parser.getBinaryValue(Base64Variant.BASE64_STANDARD);
        assertEquals(10, binaryData.length);
        assertEquals((byte) 0x61, binaryData[0]);
        assertEquals((byte) 0x73, binaryData[1]);
        assertEquals((byte) 0x73, binaryData[2]);
        assertEquals((byte) 0x68, binaryData[3]);
        assertEquals((byte) 0x65, binaryData[4]);
        assertEquals((byte) 0x61, binaryData[5]);
        assertEquals((byte) 0x74, binaryData[6]);
        assertEquals((byte) 0x65, binaryData[7]);
        assertEquals((byte) 0x73, binaryData[8]);
        assertEquals((byte) 0x74, binaryData[9]);

        assertTrue(parser.isClosed());
    }

    @Test(expected = JsonParseException.class)
    public void testUnexpectedToken() throws IOException {
        String json = "[\"abc\",1]";
        JsonNode jsonNode = JsonFactory.instance.readTree(json);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        // Read array elements
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("abc", parser.getText());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(1, parser.getIntValue());

        // Unexpected token
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        fail("Unexpected token: FIELD_NAME");
    }

    @Test
    public void testClose() throws IOException {
        String json = "[1,2,3]";
        JsonNode jsonNode = JsonFactory.instance.readTree(json);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        // Read array elements
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        for (int i = 0; i < 3; i++) {
            assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
            int value = parser.getIntValue();
            assertEquals(i + 1, value);
        }

        // Close parser
        parser.close();

        // Read end of array
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());

        assertTrue(parser.isClosed());
    }
}