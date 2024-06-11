package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.ParserMinimalBase;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;

public class TreeTraversingParserTest {

    @Test
    public void testTreeTraversingParser_Null() throws Exception {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertNull(parser.nextToken());
        parser.close();
    }

    @Test
    public void testTreeTraversingParser_Array() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("[]");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        parser.skipChildren();
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        parser.close();
    }

    @Test
    public void testTreeTraversingParser_Object() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("{\"name\": \"John\", \"age\": 30}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        parser.skipChildren();
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("name", parser.getCurrentName());
        parser.skipChildren();
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("John", parser.getText());
        parser.skipChildren();
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("age", parser.getCurrentName());
        parser.skipChildren();
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(30, parser.getIntValue());
        parser.skipChildren();
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        parser.close();
    }

    @Test
    public void testTreeTraversingParser_ValueNode() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("42");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(42, parser.getIntValue());
        parser.skipChildren();
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        parser.close();
    }

    @Test(expected = JsonParseException.class)
    public void testTreeTraversingParser_Error() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("{\"name\": [\"John\"]}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        parser.skipChildren();
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("name", parser.getCurrentName());
        parser.skipChildren();
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        parser.skipChildren();
        parser.nextToken(); // should throw an exception
    }
}