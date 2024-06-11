package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TreeTraversingParserTest {

    private ObjectMapper objectMapper;
    private JsonNode jsonNode;

    @Before
    public void setup() throws Exception {
        objectMapper = new ObjectMapper();
        jsonNode = objectMapper.readTree("{\"name\": \"Alice\", \"age\": 30, \"hobbies\": [\"reading\", \"swimming\"]}");
    }

    @Test
    public void testParseStartObject() throws Exception {
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        JsonToken token = parser.nextToken();
        Assert.assertEquals(JsonToken.START_OBJECT, token);

        token = parser.nextToken();
        Assert.assertEquals(JsonToken.FIELD_NAME, token);
        Assert.assertEquals("name", parser.getCurrentName());

        token = parser.nextToken();
        Assert.assertEquals(JsonToken.VALUE_STRING, token);
        Assert.assertEquals("Alice", parser.getText());

        token = parser.nextToken();
        Assert.assertEquals(JsonToken.FIELD_NAME, token);
        Assert.assertEquals("age", parser.getCurrentName());

        token = parser.nextToken();
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, token);
        Assert.assertEquals(30, parser.getIntValue());

        token = parser.nextToken();
        Assert.assertEquals(JsonToken.FIELD_NAME, token);
        Assert.assertEquals("hobbies", parser.getCurrentName());

        token = parser.nextToken();
        Assert.assertEquals(JsonToken.START_ARRAY, token);

        token = parser.nextToken();
        Assert.assertEquals(JsonToken.VALUE_STRING, token);
        Assert.assertEquals("reading", parser.getText());

        token = parser.nextToken();
        Assert.assertEquals(JsonToken.VALUE_STRING, token);
        Assert.assertEquals("swimming", parser.getText());

        token = parser.nextToken();
        Assert.assertEquals(JsonToken.END_ARRAY, token);

        token = parser.nextToken();
        Assert.assertEquals(JsonToken.END_OBJECT, token);

        parser.close();
    }

    @Test(expected = IOException.class)
    public void testParseNullNode() throws Exception {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        parser.nextToken();
    }

    @Test(expected = JsonParseException.class)
    public void testParseInvalidJson() throws Exception {
        JsonNode jsonNode = objectMapper.readTree("{\"name\": \"Alice\", \"age\": 30, \"hobbies\": [\"reading\", \"swimming\"}{");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken();
    }
}