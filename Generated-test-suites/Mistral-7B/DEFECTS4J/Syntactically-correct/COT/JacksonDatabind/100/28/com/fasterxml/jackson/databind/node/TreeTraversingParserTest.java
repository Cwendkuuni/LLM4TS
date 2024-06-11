package com.example.junit; // replace with your package name

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.ObjectCodec;
import com.fasterxml.jackson.databind.json.JsonFactory;
import com.fasterxml.jackson.databind.node.*;

import org.junit.Test;

public class TreeTraversingParserTest {

    @Test
    public void testTreeTraversingParser() throws Exception {
        ObjectCodec objectCodec = new JsonFactory().getCodec();
        JsonNode jsonNode = objectCodec.readTree("{\"name\": \"John\", \"age\": 30, \"city\": \"New York\"}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode, objectCodec);

        // Basic functionality test
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.START_OBJECT, token);
        assertEquals("name", parser.getCurrentName());
        assertEquals("John", parser.getText());
        token = parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        assertEquals("age", parser.getCurrentName());
        assertEquals("30", parser.getText());
        token = parser.nextToken();
        assertEquals(JsonToken.COMMA, token);
        assertEquals("city", parser.getCurrentName());
        assertEquals("New York", parser.getText());
        token = parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, token);

        // Test edge cases and exceptions
        
        // Test empty JSON object
        parser = new TreeTraversingParser(objectCodec.createParser("{}"));
        token = parser.nextToken();
        assertEquals(JsonToken.START_OBJECT, token);
        token = parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, token);
        parser.close();

        // Test empty JSON array
        parser = new TreeTraversingParser(objectCodec.createParser("[]"));
        token = parser.nextToken();
        assertEquals(JsonToken.START_ARRAY, token);
        token = parser.nextToken();
        assertEquals(JsonToken.END_ARRAY, token);
        parser.close();

        // Test null JSON value
        parser = new TreeTraversingParser(objectCodec.createParser("null"));
        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_NULL, token);
        assertEquals(null, parser.getText());
        parser.close();

        // Test JSON number
        parser = new TreeTraversingParser(objectCodec.createParser("123"));
        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_INT, token);
        assertEquals("123", parser.getText());
        assertEquals(123, parser.getIntValue());
        parser.close();

        // Test JSON string
        parser = new TreeTraversingParser(objectCodec.createParser("\"Hello World\""));
        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token);
        assertEquals("Hello World", parser.getText());
        parser.close();

        // Test JSON boolean
        parser = new TreeTraversingParser(objectCodec.createParser("true"));
        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_TRUE, token);
        assertTrue(parser.getBooleanValue());
        parser.close();

        // Test JSON array with different types
        parser = new TreeTraversingParser(objectCodec.createParser("[1, \"Hello\", 3.14]"));
        token = parser.nextToken();
        assertEquals(JsonToken.START_ARRAY, token);
        assertEquals(1, parser.getIntValue());
        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token);
        assertEquals("Hello", parser.getText());
        assertEquals(3.14, parser.getDoubleValue(), 0.01);
        token = parser.nextToken();
        assertEquals(JsonToken.END_ARRAY, token);
        parser.close();

        // Test JSON object with different types
        parser = new TreeTraversingParser(objectCodec.createParser("{\"name\": \"John\", \"age\": 30, \"isStudent\": true}"));
        token = parser.nextToken();
        assertEquals(JsonToken.START_OBJECT, token);
        assertEquals("name", parser.getCurrentName());
        assertEquals("John", parser.getText());
        token = parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        assertEquals("age", parser.getCurrentName());
        assertEquals("30", parser.getText());
        token = parser.nextToken();
        assertEquals(JsonToken.COMMA, token);
        assertEquals("isStudent", parser.getCurrentName());
        assertTrue(parser.getBooleanValue());
        token = parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, token);
        parser.close();
    }

    @Test(expected = IOException.class)
    public void testUnexpectedTokenException() throws Exception {
        JsonFactory factory = new JsonFactory();
        TreeTraversingParser parser = new TreeTraversingParser(factory.createParser("{"));
        parser.nextToken(); // consume START_OBJECT
        parser.nextToken(); // consume FIELD_NAME
        parser.nextToken(); // consume VALUE_STRING
        parser.nextToken(); // consume COMMA
        parser.nextToken(); // consume unexpected VALUE_NUMBER_INT
        parser.close();
    }

    @Test(expected = JsonParseException.class)
    public void testInvalidJsonException() throws Exception {
        JsonFactory factory = new JsonFactory();
        TreeTraversingParser parser = new TreeTraversingParser(factory.createParser("{name: 1"));
        parser.nextToken(); // consume START_OBJECT
        parser.nextToken(); // consume FIELD_NAME
        parser.nextToken(); // consume VALUE_NUMBER_INT
        parser.close();
    }

    @Test(expected = NullPointerException.class)
    public void testNullPointerException() throws Exception {
        JsonFactory factory = new JsonFactory();
        TreeTraversingParser parser = new TreeTraversingParser(null);
        parser.nextToken();
        parser.close();
    }
}