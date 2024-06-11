package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

public class TreeTraversingParserTest {

    @Test
    public void testBasicParsing() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"name\":\"John\",\"age\":30,\"hobbies\":[\"reading\",\"swimming\"]}";
        JsonNode rootNode = mapper.readTree(json);
        TreeTraversingParser parser = new TreeTraversingParser(rootNode);

        // Test getting next token
        JsonToken token = parser.nextToken();
        Assert.assertEquals(JsonToken.START_OBJECT, token);

        // Test getting next token after object start
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.FIELD_NAME, token);
        Assert.assertEquals("name", parser.getText());

        // Test getting next token after field name
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.VALUE_STRING, token);
        Assert.assertEquals("John", parser.getText());

        // Test getting next token after string value
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.FIELD_NAME, token);
        Assert.assertEquals("age", parser.getText());

        // Test getting next token after field name
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, token);
        Assert.assertEquals(30, parser.getIntValue());

        // Test getting next token after integer value
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.FIELD_NAME, token);
        Assert.assertEquals("hobbies", parser.getText());

        // Test getting next token after field name
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.START_ARRAY, token);

        // Test getting next token after array start
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.VALUE_STRING, token);
        Assert.assertEquals("reading", parser.getText());

        // Test getting next token after string value
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.VALUE_STRING, token);
        Assert.assertEquals("swimming", parser.getText());

        // Test getting next token after last value in array
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.END_ARRAY, token);

        // Test getting next token after array end
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.END_OBJECT, token);

        // Test closing parser
        parser.close();
        token = parser.nextToken();
        Assert.assertNull(token);
    }

    @Test
    public void testEmptyParsing() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{}";
        JsonNode rootNode = mapper.readTree(json);
        TreeTraversingParser parser = new TreeTraversingParser(rootNode);

        // Test getting next token
        JsonToken token = parser.nextToken();
        Assert.assertEquals(JsonToken.START_OBJECT, token);

        // Test getting next token after object start
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.END_OBJECT, token);

        // Test closing parser
        parser.close();
        token = parser.nextToken();
        Assert.assertNull(token);
    }

    @Test(expected = JsonParseException.class)
    public void testInvalidJsonParsing() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{name:John,age:30}"; // missing comma between fields
        JsonNode rootNode = mapper.readTree(json);
        TreeTraversingParser parser = new TreeTraversingParser(rootNode);

        parser.nextToken();
        parser.nextToken();

        // This should throw an exception since there is a missing comma in the JSON
    }
}