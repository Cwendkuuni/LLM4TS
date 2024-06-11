package com.fasterxml.jackson.databind.node;

import org.junit.*;
import static org.junit.Assert.*;

public class TreeTraversingParserTest {

    private TreeTraversingParser parser;

    @Before
    public void setUp() {
        JsonNode n = createJsonNode(); // Create a sample JsonNode for testing
        parser = new TreeTraversingParser(n);
    }

    @Test
    public void testNextToken_StartArray_ReturnsStartArrayToken() {
        try {
            assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testNextToken_StartObject_ReturnsStartObjectToken() {
        try {
            assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testNextToken_ValueNode_ReturnsValueToken() {
        try {
            assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testSkipChildren_StartObject_SkipsChildrenAndReturnsEndObjectToken() {
        try {
            parser.nextToken(); // Start object token
            assertEquals(JsonToken.END_OBJECT, parser.skipChildren());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testGetText_ValueString_ReturnsTextValue() {
        try {
            parser.nextToken(); // Value string token
            assertEquals("TestValue", parser.getText());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testGetIntValue_ValueInt_ReturnsIntValue() {
        try {
            parser.nextToken(); // Value number int token
            assertEquals(123, parser.getIntValue());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testGetBigIntegerValue_ValueInt_ReturnsBigIntegerValue() {
        try {
            parser.nextToken(); // Value number int token
            assertEquals(BigInteger.valueOf(123), parser.getBigIntegerValue());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testGetDecimalValue_ValueFloat_ReturnsDecimalValue() {
        try {
            parser.nextToken(); // Value number float token
            assertEquals(BigDecimal.valueOf(3.14), parser.getDecimalValue());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testGetDoubleValue_ValueFloat_ReturnsDoubleValue() {
        try {
            parser.nextToken(); // Value number float token
            assertEquals(3.14, parser.getDoubleValue(), 0.01);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testGetFloatValue_ValueFloat_ReturnsFloatValue() {
        try {
            parser.nextToken(); // Value number float token
            assertEquals(3.14f, parser.getFloatValue(), 0.01);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @After
    public void tearDown() {
        try {
            parser.close();
        } catch (Exception e) {
            fail("Exception thrown while closing parser: " + e.getMessage());
        }
    }

    private JsonNode createJsonNode() {
        return new ObjectNode(null);
    }
}