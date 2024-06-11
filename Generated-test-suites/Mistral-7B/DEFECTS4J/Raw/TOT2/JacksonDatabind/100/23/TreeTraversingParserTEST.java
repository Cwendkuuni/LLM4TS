package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

public class TreeTraversingParserTest {

    @Test
    public void testEmptyTree() throws Exception {
        JsonParser parser = new TreeTraversingParser(JsonFactory.instance.createParser("[]"));
        Assert.assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        parser.close();
    }

    @Test
    public void testSimpleObject() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree("{\"name\": \"John\", \"age\": 30}");
        JsonParser parser = new TreeTraversingParser(rootNode);

        Assert.assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        Assert.assertEquals("name", parser.getCurrentName());
        Assert.assertEquals("John", parser.getText());
        Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        Assert.assertEquals("age", parser.getCurrentName());
        Assert.assertEquals("30", parser.getText());
        Assert.assertEquals(JsonToken.END_OBJECT, parser.nextToken());

        parser.close();
    }

    @Test
    public void testSimpleArray() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree("[1, 2, 3]");
        JsonParser parser = new TreeTraversingParser(rootNode);

        Assert.assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        Assert.assertEquals(1, parser.nextIntValue());
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        Assert.assertEquals(2, parser.nextIntValue());
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        Assert.assertEquals(3, parser.nextIntValue());
        Assert.assertEquals(JsonToken.END_ARRAY, parser.nextToken());

        parser.close();
    }

    @Test
    public void testNullValue() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree("{\"name\": null}");
        JsonParser parser = new TreeTraversingParser(rootNode);

        Assert.assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        Assert.assertEquals("name", parser.getCurrentName());
        Assert.assertEquals(null, parser.getText());
        Assert.assertEquals(JsonToken.END_OBJECT, parser.nextToken());

        parser.close();
    }

    @Test(expected = JsonParseException.class)
    public void testInvalidJson() throws Exception {
        JsonParser parser = new TreeTraversingParser(JsonFactory.instance.createParser("{a: 1}"));
        parser.nextToken(); // consume "{"
        parser.nextToken(); // consume "a"
        parser.nextToken(); // consume ":"
        parser.nextToken(); // consume 1
        parser.nextToken(); // consume ","

        // this should throw an exception
        parser.nextToken();
    }

    @Test(expected = JsonParseException.ClassNotFoundException.class)
    public void testUnknownClass() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree("{\"foo\": new Xyz()}");
        JsonParser parser = new TreeTraversingParser(rootNode);

        parser.nextToken(); // consume "{"
        parser.nextToken(); // consume "foo"
        parser.nextToken(); // consume ":"
        parser.nextToken(); // consume "new"
        parser.nextToken(); // consume "Xyz"

        // this should throw an exception
        parser.getEmbeddedObject();
    }
}