package com.example.test.jackson;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;

public class TreeTraversingParserTest {

    @Test
    public void testConstructorWithJsonNode() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("{\"name\":\"John\", \"age\":30}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals("name", parser.getCurrentName());
        assertEquals("John", parser.getText());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("age", parser.getCurrentName());
        assertEquals("30", parser.getText());
        assertEquals(JsonToken.VALUE_INT, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());

        parser.close();
        assertNull(parser.nextToken());
    }

    @Test(expected = IOException.class)
    public void testNextTokenAfterClose() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("{\"name\":\"John\", \"age\":30}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        parser.close();
        parser.nextToken();
    }

    @Test
    public void testSkipChildren() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("{\"name\":{\"first\":\"John\", \"last\":\"Doe\"}, \"age\":30}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        parser.nextToken(); // START_OBJECT
        parser.nextToken(); // FIELD_NAME: name
        parser.nextToken(); // START_OBJECT
        parser.nextToken(); // FIELD_NAME: first
        parser.nextToken(); // VALUE_STRING: "John"
        parser.nextToken(); // END_OBJECT
        parser.nextToken(); // FIELD_NAME: last
        parser.nextToken(); // VALUE_STRING: "Doe"
        parser.nextToken(); // END_OBJECT
        parser.nextToken(); // FIELD_NAME: age
        parser.nextToken(); // VALUE_INT: 30
        parser.nextToken(); // END_OBJECT

        parser.skipChildren();

        assertEquals(JsonToken.START_OBJECT, parser.nextToken()); // "name"
        assertEquals(JsonToken.END_OBJECT, parser.nextToken()); // end of "name" object
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken()); // "age"
        assertEquals(JsonToken.VALUE_INT, parser.nextToken()); // "30"
        assertEquals(JsonToken.END_OBJECT, parser.nextToken()); // end of root object
    }

    @Test(expected = JsonParseException.class)
    public void testNextTokenAfterEndObject() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("{\"name\":{\"first\":\"John\", \"last\":\"Doe\"}, \"age\":30}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        parser.nextToken(); // START_OBJECT
        parser.nextToken(); // FIELD_NAME: name
        parser.nextToken(); // START_OBJECT
        parser.nextToken(); // FIELD_NAME: first
        parser.nextToken(); // VALUE_STRING: "John"
        parser.nextToken(); // END_OBJECT
        parser.nextToken(); // FIELD_NAME: last
        parser.nextToken(); // VALUE_STRING: "Doe"
        parser.nextToken(); // END_OBJECT
        parser.nextToken(); // FIELD_NAME: age
        parser.nextToken(); // VALUE_INT: 30
        parser.nextToken(); // END_OBJECT
        parser.nextToken(); // END_OBJECT

        parser.nextToken();
    }

    @Test
    public void testGetText() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("{\"name\":{\"first\":\"John\", \"last\":\"Doe\"}, \"age\":30}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        parser.nextToken(); // START_OBJECT
        parser.nextToken(); // FIELD_NAME: name
        parser.nextToken(); // START_OBJECT
        parser.nextToken(); // FIELD_NAME: first
        parser.nextToken(); // VALUE_STRING: "John"
        assertEquals("John", parser.getText());

        parser.nextToken(); // END_OBJECT
        parser.nextToken(); // FIELD_NAME: last
        parser.nextToken(); // VALUE_STRING: "Doe"
        assertEquals("Doe", parser.getText());

        parser.nextToken(); // END_OBJECT
        parser.nextToken(); // FIELD_NAME: age
        parser.nextToken(); // VALUE_INT: 30
        assertEquals("30", parser.getText());

        parser.close();
        assertNull(parser.getText());
    }
}