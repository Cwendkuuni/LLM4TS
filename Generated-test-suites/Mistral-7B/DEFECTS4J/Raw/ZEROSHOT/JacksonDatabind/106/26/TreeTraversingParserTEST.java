package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;

/**
 * Test class for TreeTraversingParser class
 */
public class TreeTraversingParserTest {

    /**
     * Test constructor with JsonNode argument
     *
     * @throws IOException
     */
    @Test
    public void testConstructorWithJsonNode() throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree("{\"name\":\"John\",\"age\":30,\"cities\":[\"New York\",\"Los Angeles\"]}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        // Check initial state
        assertNull(parser._nextToken);
        assertNotNull(parser._nodeCursor);

        // Test nextToken method
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.START_OBJECT, token);

        token = parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        assertEquals("name", parser.getCurrentName());

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token);
        assertEquals("John", parser.getText());

        token = parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        assertEquals("age", parser.getCurrentName());

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, token);
        assertEquals(30, parser.getIntValue());

        token = parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        assertEquals("cities", parser.getCurrentName());

        token = parser.nextToken();
        assertEquals(JsonToken.START_ARRAY, token);

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token);
        assertEquals("New York", parser.getText());

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token);
        assertEquals("Los Angeles", parser.getText());

        token = parser.nextToken();
        assertEquals(JsonToken.END_ARRAY, token);

        token = parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, token);

        // Test skipChildren method
        parser.skipChildren();
        assertNull(parser._nextToken);

        // Test isClosed method
        parser.close();
        assertTrue(parser.isClosed());

        // Test nextToken method after close
        token = parser.nextToken();
        assertNull(token);
    }

    /**
     * Test constructor with JsonNode and ObjectCodec arguments
     *
     * @throws IOException
     */
    @Test
    public void testConstructorWithJsonNodeAndObjectCodec() throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree("{\"name\":\"John\",\"age\":30,\"cities\":[\"New York\",\"Los Angeles\"]}");
        ObjectCodec objectCodec = new ObjectMapper().getFactory().getCodec();
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode, objectCodec);

        // Check initial state
        assertNull(parser._nextToken);
        assertNotNull(parser._nodeCursor);

        // Test nextToken method
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.START_OBJECT, token);

        token = parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        assertEquals("name", parser.getCurrentName());

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token);
        assertEquals("John", parser.getText());

        token = parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        assertEquals("age", parser.getCurrentName());

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, token);
        assertEquals(30, parser.getIntValue());

        token = parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        assertEquals("cities", parser.getCurrentName());

        token = parser.nextToken();
        assertEquals(JsonToken.START_ARRAY, token);

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token);
        assertEquals("New York", parser.getText());

        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token);
        assertEquals("Los Angeles", parser.getText());

        token = parser.nextToken();
        assertEquals(JsonToken.END_ARRAY, token);

        token = parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, token);

        // Test skipChildren method
        parser.skipChildren();
        assertNull(parser._nextToken);

        // Test isClosed method
        parser.close();
        assertTrue(parser.isClosed());

        // Test nextToken method after close
        token = parser.nextToken();
        assertNull(token);
    }

    /**
     * Test setCodec method
     *
     * @throws IOException
     */
    @Test
    public void testSetCodecMethod() throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree("{\"name\":\"John\",\"age\":30,\"cities\":[\"New York\",\"Los Angeles\"]}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        ObjectCodec objectCodec = new ObjectMapper().getFactory().getCodec();

        parser.setCodec(objectCodec);
        assertEquals(objectCodec, parser.getCodec());
    }

    /**
     * Test getCodec method
     *
     * @throws IOException
     */
    @Test
    public void testGetCodecMethod() throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree("{\"name\":\"John\",\"age\":30,\"cities\":[\"New York\",\"Los Angeles\"]}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        ObjectCodec objectCodec = new ObjectMapper().getFactory().getCodec();
        parser.setCodec(objectCodec);

        assertEquals(objectCodec, parser.getCodec());
    }

    /**
     * Test version method
     *
     * @throws IOException
     */
    @Test
    public void testVersionMethod() throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree("{\"name\":\"John\",\"age\":30,\"cities\":[\"New York\",\"Los Angeles\"]}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        assertEquals(com.fasterxml.jackson.databind.cfg.PackageVersion.VERSION, parser.version());
    }

    /**
     * Test close method
     *
     * @throws IOException
     */
    @Test
    public void testCloseMethod() throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree("{\"name\":\"John\",\"age\":30,\"cities\":[\"New York\",\"Los Angeles\"]}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        parser.close();
        assertNull(parser._nextToken);
        assertNull(parser._nodeCursor);
    }
}