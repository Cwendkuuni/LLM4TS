package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import org.junit.Assert;
import org.junit.Test;

public class TreeTraversingParserTest {

    @Test
    public void testArrayParse() throws JsonProcessingException {
        String json = "[1, 2, 3, 4]";
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.readerForTree(JsonNode.class);
        JsonNode jsonNode = reader.readTree(json);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        // Test traversing an array
        Assert.assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        for (int i = 0; i < 4; i++) {
            Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
            int value = parser.getIntValue();
            Assert.assertEquals(i + 1, value);
            Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
            Assert.assertNull(parser.getText()); // no value for field name
            Assert.assertEquals(JsonToken.END_ARRAY, parser.nextToken());
            Assert.assertNull(parser.nextToken()); // end of stream
        }
    }

    @Test
    public void testObjectParse() throws JsonProcessingException {
        String json = "{\"name\": \"John\", \"age\": 30, \"hobbies\": [\"reading\", \"coding\"]}";
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.readerForTree(JsonNode.class);
        JsonNode jsonNode = reader.readTree(json);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        // Test traversing an object
        Assert.assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        Assert.assertEquals("name", parser.getCurrentName());
        Assert.assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        Assert.assertEquals("John", parser.getText());
        Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        Assert.assertEquals("age", parser.getCurrentName());
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        int age = parser.getIntValue();
        Assert.assertEquals(30, age);
        Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        Assert.assertEquals("hobbies", parser.getCurrentName());
        Assert.assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        for (int i = 0; i < 2; i++) {
            Assert.assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
            String hobby = parser.getText();
            if (i == 0) {
                Assert.assertEquals("reading", hobby);
            } else {
                Assert.assertEquals("coding", hobby);
            }
            Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        }
        Assert.assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        Assert.assertEquals(JsonToken.END_OBJECT, parser.nextToken()); // end of stream
    }

    @Test
    public void testEmptyParse() throws JsonProcessingException {
        String json = "{}";
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.readerForTree(JsonNode.class);
        JsonNode jsonNode = reader.readTree(json);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        // Test traversing an empty object
        Assert.assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        Assert.assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        Assert.assertEquals(JsonToken.END_OBJECT, parser.nextToken()); // end of stream
    }

    @Test(expected = JsonParseException.class)
    public void testInvalidJsonParse() throws JsonProcessingException {
        String json = "{name: John, age: 30, hobbies: [\"reading\", \"coding\"}]"; // missing comma
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.readerForTree(JsonNode.class);
        JsonNode jsonNode = reader.readTree(json);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        // Test traversing an invalid JSON should throw an exception
        Assert.assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        Assert.assertEquals("name", parser.getCurrentName());
        Assert.assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        Assert.assertEquals("John", parser.getText());
        Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        Assert.assertEquals("age", parser.getCurrentName());
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        int age = parser.getIntValue();
        Assert.assertEquals(30, age);
        Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        Assert.assertEquals("hobbies", parser.getCurrentName());
        Assert.assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        String hobby = parser.nextToken().asText();
        Assert.assertEquals("reading", hobby);
        Assert.assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        hobby = parser.nextToken().asText();
        Assert.assertEquals("coding", hobby);
        Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken()); // missing comma before the end
        // The next line should throw an exception
        Assert.fail();
    }
}