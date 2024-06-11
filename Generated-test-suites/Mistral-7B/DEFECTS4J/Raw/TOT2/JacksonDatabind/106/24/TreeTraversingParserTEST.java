package com.fasterxml.jackson.databind.node; // adjust package if necessary

import com.fasterxml.jackson.core.io.SerializationTest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TextNode;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TreeTraversingParserTest {

    @Test
    public void testSimpleObject() throws Exception {
        JsonNode jsonNode = new ObjectMapper().readTree("{\"name\": \"John\", \"age\": 30}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        assertEquals("John", parser.getText());
        assertEquals(30, parser.getIntValue());
        assertFalse(parser.isClosed());

        parser.close();
        assertNull(parser.getText());
        assertNull(parser.getIntValue());
        assertTrue(parser.isClosed());
    }

    @Test
    public void testSimpleArray() throws Exception {
        JsonNode jsonNode = new ObjectMapper().readTree("[1, 2, 3]");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        assertEquals(1, parser.getIntValue());
        assertEquals(2, parser.getIntValue());
        assertEquals(3, parser.getIntValue());
        assertFalse(parser.isClosed());

        parser.close();
        assertNull(parser.getText());
        assertNull(parser.getIntValue());
        assertTrue(parser.isClosed());
    }

    @Test
    public void testEmpty() throws Exception {
        JsonNode jsonNode = new ObjectMapper().readTree("{}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        assertNull(parser.getText());
        assertNull(parser.getIntValue());
        assertTrue(parser.isClosed());
    }

    @Test
    public void testTextNode() throws Exception {
        JsonNode jsonNode = new ObjectMapper().readTree("{\"greeting\": \"Hello, world!\"}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        assertEquals("Hello, world!", parser.getText());
        assertEquals("Hello, world!", parser.getText(null)); // null as Locale
        assertNull(parser.getIntValue());
        assertFalse(parser.isClosed());

        parser.close();
        assertNull(parser.getText());
        assertNull(parser.getText(null));
        assertNull(parser.getIntValue());
        assertTrue(parser.isClosed());
    }

    @Test
    public void testNumericNode() throws Exception {
        JsonNode jsonNode = new ObjectMapper().readTree("{ \"number\": 42 }");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        assertEquals(42, parser.getIntValue());
        assertEquals(42, parser.getLongValue());
        assertEquals(42.0, parser.getDoubleValue(), 0.0);
        assertEquals(42.0, parser.getFloatValue(), 0.0f);
        assertFalse(parser.isClosed());

        parser.close();
        assertNull(parser.getText());
        assertNull(parser.getIntValue());
        assertNull(parser.getLongValue());
        assertNull(parser.getDoubleValue());
        assertNull(parser.getFloatValue());
        assertTrue(parser.isClosed());
    }

    @Test
    public void testEmtyArray() throws Exception {
        JsonNode jsonNode = new ObjectMapper().readTree("[]");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        assertNull(parser.getText());
        assertNull(parser.getIntValue());
        assertTrue(parser.isClosed());
    }

    @Test
    public void testNullNode() throws Exception {
        JsonNode jsonNode = new ObjectMapper().readTree("null");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        assertNull(parser.getText());
        assertNull(parser.getIntValue());
        assertTrue(parser.isClosed());
    }

    @Test(expected = IOException.class)
    public void testInvalidJson() throws Exception {
        JsonNode jsonNode = new ObjectMapper().readTree("invalidJson");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken();
    }

    @Test
    public void testClosedParser() throws Exception {
        JsonNode jsonNode = new ObjectMapper().readTree("{ \"name\": \"John\", \"age\": 30 }");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.close();

        assertNull(parser.getText());
        assertNull(parser.getIntValue());
        assertTrue(parser.isClosed());
        assertNull(parser.nextToken());
    }

    @Test
    public void testEmptyContainer() throws Exception {
        JsonNode jsonNode = new ObjectMapper().readTree("{ \"array\": [] }");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        assertFalse(parser.isClosed());
        assertNull(parser.getText());
        assertNull(parser.getIntValue());

        parser.skipChildren();
        assertTrue(parser.isClosed());
        assertNull(parser.getText());
        assertNull(parser.getIntValue());
    }

    @Test
    public void testComplexObject() throws Exception {
        JsonNode jsonNode = new ObjectMapper().readTree("{\"person\": {\"name\": \"John\", \"age\": 30, \"children\": [{\"name\": \"Mike\", \"age\": 25}, {\"name\": \"Emma\", \"age\": 23}]}}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        assertEquals("John", parser.getText());
        assertEquals(30, parser.getIntValue());

        parser.skipChildren();
        parser.getText(); // access children
        parser.skipChildren();

        assertEquals("Mike", parser.getText());
        assertEquals(25, parser.getIntValue());

        parser.skipChildren();
        assertEquals("Emma", parser.getText());
        assertEquals(23, parser.getIntValue());

        parser.close();
        assertNull(parser.getText());
        assertNull(parser.getIntValue());
        assertTrue(parser.isClosed());
    }

    @Test
    public void testComplexArray() throws Exception {
        JsonNode jsonNode = new ObjectMapper().readTree("[1, 2, 3, [7, 8, [9, {\"name\": \"John\"}]]]");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        assertEquals(1, parser.getIntValue());
        assertEquals(2, parser.getIntValue());
        assertEquals(3, parser.getIntValue());

        parser.skipChildren();
        assertEquals(7, parser.getIntValue());
        assertEquals(8, parser.getIntValue());

        parser.skipChildren();
        assertEquals(9, parser.getIntValue());
        assertEquals("John", parser.getText());

        parser.close();
        assertNull(parser.getText());
        assertNull(parser.getIntValue());
        assertTrue(parser.isClosed());
    }
}