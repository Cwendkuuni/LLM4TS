package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.TextNode;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class TreeTraversingParserTest {

    private JsonNode rootNode;
    private JsonNode arrayNode;
    private JsonNode objectNode;
    private TreeTraversingParser parser;
    private ObjectMapper mapper;

    @Before
    public void setUp() throws Exception {
        mapper = new ObjectMapper();
        rootNode = mapper.readTree("{\"foo\": 1, \"bar\": [1, 2, 3]}");
        arrayNode = mapper.readTree("[1, 2, 3]");
        objectNode = mapper.readTree("{\"foo\": 1, \"bar\": {}}");
        parser = new TreeTraversingParser(rootNode);
    }

    @After
    public void tearDown() throws Exception {
        parser.close();
    }

    @Test
    public void testNextToken_Root() throws Exception {
        JsonToken nextToken = parser.nextToken();
        assertEquals(JsonToken.START_OBJECT, nextToken);
        assertSame(rootNode, parser.getCurrentNode());

        nextToken = parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, nextToken);
        assertSame("foo", parser.getCurrentName());

        nextToken = parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, nextToken);
        assertEquals(1, parser.getIntValue());

        nextToken = parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, nextToken);
        assertSame("bar", parser.getCurrentName());

        nextToken = parser.nextToken();
        assertEquals(JsonToken.START_ARRAY, nextToken);

        for (int i = 0; i < 3; i++) {
            nextToken = parser.nextToken();
            assertEquals(JsonToken.VALUE_NUMBER_INT, nextToken);
            assertEquals(i + 1, parser.getIntValue());
        }

        nextToken = parser.nextToken();
        assertEquals(JsonToken.END_ARRAY, nextToken);

        nextToken = parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, nextToken);
        assertNull(parser.getCurrentNode());
    }

    @Test
    public void testNextToken_Array() throws Exception {
        parser = new TreeTraversingParser(arrayNode);

        JsonToken nextToken = parser.nextToken();
        assertEquals(JsonToken.START_ARRAY, nextToken);

        for (int i = 0; i < 3; i++) {
            nextToken = parser.nextToken();
            assertEquals(JsonToken.VALUE_NUMBER_INT, nextToken);
            assertEquals(i + 1, parser.getIntValue());
        }

        nextToken = parser.nextToken();
        assertEquals(JsonToken.END_ARRAY, nextToken);

        nextToken = parser.nextToken();
        assertNull(parser.getCurrentNode());
    }

    @Test
    public void testNextToken_Object() throws Exception {
        parser = new TreeTraversingParser(objectNode);

        JsonToken nextToken = parser.nextToken();
        assertEquals(JsonToken.START_OBJECT, nextToken);

        nextToken = parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, nextToken);
        assertSame("foo", parser.getCurrentName());

        nextToken = parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, nextToken);
        assertEquals(1, parser.getIntValue());

        nextToken = parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, nextToken);
        assertSame("bar", parser.getCurrentName());

        nextToken = parser.nextToken();
        assertEquals(JsonToken.START_OBJECT, nextToken);

        nextToken = parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, nextToken);

        nextToken = parser.nextToken();
        assertNull(parser.getCurrentNode());
    }

    @Test(expected = JsonParseException.class)
    public void testNextToken_Closed() throws Exception {
        parser.close();
        parser.nextToken();
    }

    @Test(expected = JsonParseException.class)
    public void testNextToken_Null() throws Exception {
        parser = new TreeTraversingParser(null);
        parser.nextToken();
    }

    @Test
    public void testNextValue_Object() throws Exception {
        parser = new TreeTraversingParser(objectNode);

        // ObjectNode does not have a value, use nextToken instead
        // testNextValue_Value() tests nextValue for value nodes
    }

    @Test
    public void testNextValue_Value() throws Exception {
        parser = new TreeTraversingParser(new TextNode("Hello World"));

        assertEquals("Hello World", parser.getText());
    }

    @Test
    public void testNextValue_NumberInt() throws Exception {
        parser = new TreeTraversingParser(JsonNodeFactory.numberNode(1));
        assertEquals(1, parser.getIntValue());
    }

    @Test
    public void testNextValue_NumberFloat() throws Exception {
        parser = new TreeTraversingParser(JsonNodeFactory.numberNode(1.5));
        assertEquals(1.5, parser.getFloatValue(), 0.001f);
    }

    @Test
    public void testNextValue_NumberLong() throws Exception {
        parser = new TreeTraversingParser(JsonNodeFactory.numberNode(1L));
        assertEquals(1, parser.getLongValue());
    }

    @Test
    public void testNextValue_NumberDecimal() throws Exception {
        parser = new TreeTraversingParser(JsonNodeFactory.numberNode(new BigDecimal("1.2345")));
        assertEquals("1.2345", parser.getText());
    }

    @Test(expected = JsonParseException.class)
    public void testNextValue_Null() throws Exception {
        parser = new TreeTraversingParser(null);
        parser.nextValue();
    }
}