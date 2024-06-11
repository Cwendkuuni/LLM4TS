package com.fasterxml.jackson.databind.node;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;

/**
 * Tests for {@link TreeTraversingParser}.
 */
public class TreeTraversingParserTest {

    @Test
    public void testParsingSimpleJsonArray() throws Exception {
        JsonNode rootNode = Json.parse("[1, 2, 3]");
        TreeTraversingParser parser = new TreeTraversingParser(rootNode);

        // Test parsing the whole JSON array
        JsonToken token = parser.nextToken();
        Assert.assertEquals(JsonToken.START_ARRAY, token);

        // Test parsing each element in the JSON array
        for (int i = 0; i < 3; i++) {
            token = parser.nextToken();
            if (i == 0) {
                Assert.assertEquals(JsonToken.VALUE_INT, token);
                Assert.assertEquals(1, parser.getIntValue());
            } else if (i == 1) {
                Assert.assertEquals(JsonToken.VALUE_INT, token);
                Assert.assertEquals(2, parser.getIntValue());
            } else {
                Assert.assertEquals(JsonToken.VALUE_INT, token);
                Assert.assertEquals(3, parser.getIntValue());
                Assert.assertEquals(JsonToken.END_ARRAY, parser.nextToken());
            }
        }

        // Test closing the parser
        parser.close();
        Assert.assertEquals(JsonToken.NONE, parser.nextToken());
    }

    @Test
    public void testParsingSimpleJsonObject() throws Exception {
        JsonNode rootNode = Json.parse("{\"name\": \"John\", \"age\": 30}");
        TreeTraversingParser parser = new TreeTraversingParser(rootNode);

        // Test parsing the whole JSON object
        JsonToken token = parser.nextToken();
        Assert.assertEquals(JsonToken.START_OBJECT, token);

        // Test parsing each field in the JSON object
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.FIELD_NAME, token);
        Assert.assertEquals("name", parser.getCurrentName());
        parser.nextToken(); // VALUE_STRING
        Assert.assertEquals("John", parser.getText());

        token = parser.nextToken();
        Assert.assertEquals(JsonToken.FIELD_NAME, token);
        Assert.assertEquals("age", parser.getCurrentName());
        parser.nextToken(); // VALUE_NUMBER_INT
        Assert.assertEquals(30, parser.getIntValue());

        token = parser.nextToken();
        Assert.assertEquals(JsonToken.END_OBJECT, token);

        // Test closing the parser
        parser.close();
        Assert.assertEquals(JsonToken.NONE, parser.nextToken());
    }

    @Test
    public void testParsingEmptyJsonArrayAndObject() throws Exception {
        JsonNode rootNodeArray = Json.parse("[]");
        TreeTraversingParser parserArray = new TreeTraversingParser(rootNodeArray);

        JsonToken tokenArray = parserArray.nextToken();
        Assert.assertEquals(JsonToken.START_ARRAY, tokenArray);
        Assert.assertEquals(JsonToken.END_ARRAY, parserArray.nextToken());
        parserArray.close();

        JsonNode rootNodeObject = Json.parse("{}");
        TreeTraversingParser parserObject = new TreeTraversingParser(rootNodeObject);
        tokenObject = parserObject.nextToken();
        Assert.assertEquals(JsonToken.START_OBJECT, tokenObject);
        Assert.assertEquals(JsonToken.END_OBJECT, parserObject.nextToken());
        parserObject.close();
    }

    @Test(expected = JsonParseException.class)
    public void testParsingMalformedJsonArray() throws Exception {
        JsonNode rootNode = Json.parse("[1, 2, 3]");
        TreeTraversingParser parser = new TreeTraversingParser(rootNode);

        // Test parsing the whole JSON array
        JsonToken token = parser.nextToken();
        Assert.assertEquals(JsonToken.START_ARRAY, token);

        // Test parsing the first element in the JSON array, which is valid
        Assert.assertEquals(JsonToken.VALUE_INT, parser.nextToken());
        Assert.assertEquals(1, parser.getIntValue());

        // Test parsing the second element in the JSON array, which contains a syntax error
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.VALUE_STRING, token);
        parser.nextToken(); // expected VALUE_STRING, but found a syntax error instead
    }

    @Test(expected = JsonParseException.class)
    public void testParsingMalformedJsonObject() throws Exception {
        JsonNode rootNode = Json.parse("{\"name\": \"John\", age: 30}"); // missing quote around "age"
        TreeTraversingParser parser = new TreeTraversingParser(rootNode);

        // Test parsing the whole JSON object
        JsonToken token = parser.nextToken();
        Assert.assertEquals(JsonToken.START_OBJECT, token);

        // Test parsing the first field in the JSON object, which is valid
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.FIELD_NAME, token);
        Assert.assertEquals("name", parser.getCurrentName());
        parser.nextToken(); // VALUE_STRING
        Assert.assertEquals("John", parser.getText());

        // Test parsing the second field in the JSON object, which contains a syntax error
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.FIELD_NAME, token);
        Assert.assertEquals("age", parser.getCurrentName());
        parser.nextToken(); // expected VALUE_NUMBER_INT, but found a syntax error instead
    }

    @Test
    public void testParsingEmptyJsonArrayAndObjectWithNullValues() throws Exception {
        JsonNode rootNodeArray = Json.parse("[null, null]");
        TreeTraversingParser parserArray = new TreeTraversingParser(rootNodeArray);

        JsonToken tokenArray = parserArray.nextToken();
        Assert.assertEquals(JsonToken.START_ARRAY, tokenArray);

        // Test parsing both null values
        Assert.assertEquals(JsonToken.VALUE_NULL, parserArray.nextToken());
        Assert.assertNull(parserArray.getText());

        Assert.assertEquals(JsonToken.VALUE_NULL, parserArray.nextToken());
        Assert.assertNull(parserArray.getText());

        Assert.assertEquals(JsonToken.END_ARRAY, parserArray.nextToken());
        parserArray.close();

        JsonNode rootNodeObject = Json.parse("{null: null}");
        TreeTraversingParser parserObject = new TreeTraversingParser(rootNodeObject);
        tokenObject = parserObject.nextToken();
        Assert.assertEquals(JsonToken.START_OBJECT, tokenObject);

        // Test parsing both null values
        tokenObject = parserObject.nextToken();
        Assert.assertEquals(JsonToken.FIELD_NAME, tokenObject);
        Assert.assertEquals("", parserObject.getCurrentName());
        parserObject.nextToken(); // VALUE_NULL
        Assert.assertNull(parserObject.getText());

        tokenObject = parserObject.nextToken();
        Assert.assertEquals(JsonToken.END_OBJECT, tokenObject);
        parserObject.close();
    }

    @Test
    public void testParsingNestedJsonArrayAndObject() throws Exception {
        JsonNode rootNode = Json.parse("{name: {age: 20, hobbies: [\"reading\", \"sleeping\"]}}");
        TreeTraversingParser parser = new TreeTraversingParser(rootNode);

        // Test parsing the whole JSON object
        JsonToken token = parser.nextToken();
        Assert.assertEquals(JsonToken.START_OBJECT, token);

        // Test parsing the "name" field
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.FIELD_NAME, token);
        Assert.assertEquals("name", parser.getCurrentName());
        parser.nextToken(); // START_OBJECT

        // Test parsing the "age" field
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.FIELD_NAME, token);
        Assert.assertEquals("age", parser.getCurrentName());
        parser.nextToken(); // VALUE_NUMBER_INT
        Assert.assertEquals(20, parser.getIntValue());

        // Test parsing the "hobbies" field
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.FIELD_NAME, token);
        Assert.assertEquals("hobbies", parser.getCurrentName());
        parser.nextToken(); // START_ARRAY

        // Test parsing each hobby in the JSON array
        Assert.assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        Assert.assertEquals("reading", parser.getText());

        Assert.assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        Assert.assertEquals("sleeping", parser.getText());

        Assert.assertEquals(JsonToken.END_ARRAY, parser.nextToken());

        // Test closing the parser
        parser.close();
        Assert.assertEquals(JsonToken.NONE, parser.nextToken());
    }
}