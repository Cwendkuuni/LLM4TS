package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TextNode;

import org.junit.Test;

public class TreeTraversingParserTest {

    @Test
    public void testEmptyParser() throws IOException {
        JsonParser parser = new TreeTraversingParser(null);
        assertNull(parser.nextToken());
        parser.close();
    }

    @Test
    public void testValueParser() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = new TreeTraversingParser(mapper.readTree("123"));
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(123, parser.getIntValue());
        parser.close();
    }

    @Test
    public void testArrayParser() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = new TreeTraversingParser(mapper.readTree("[1, 2, 3]"));
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(1, parser.getIntValue());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(2, parser.getIntValue());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(3, parser.getIntValue());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        parser.close();
    }

    @Test
    public void testObjectParser() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = new TreeTraversingParser(mapper.readTree("{\"name\": \"John\", \"age\": 30}"));
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals("name", parser.getCurrentName());
        assertEquals("John", parser.getText());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("age", parser.getCurrentName());
        assertEquals("30", parser.getText());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        parser.close();
    }

    @Test
    public void testSkippingChildren() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = new TreeTraversingParser(mapper.readTree("[1, 2, { \"name\": \"John\" }]"));
        parser.nextToken(); // [
        parser.nextToken(); // START_ARRAY
        parser.nextToken(); // VALUE_NUMBER_INT
        parser.nextToken(); // VALUE_NUMBER_INT
        parser.nextToken(); // START_OBJECT
        parser.nextToken(); // FIELD_NAME
        parser.nextToken(); // "name"
        parser.nextToken(); // VALUE_STRING
        parser.nextToken(); // END_OBJECT
        parser.nextToken(); // END_ARRAY
        parser.skipChildren();
        assertNull(parser.nextToken()); // null for end marker
        parser.close();
    }
}