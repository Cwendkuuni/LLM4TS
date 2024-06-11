package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.BaseJsonNode;
import com.fasterxml.jackson.databind.node.JsonPointer;
import com.fasterxml.jackson.databind.node.TextNode;

import org.junit.Test;

public class TreeTraversingParserTest {

    @Test
    public void testEmpty() throws Exception {
        JsonParser parser = new TreeTraversingParser(new TextNode(""));
        assertEquals(JsonToken.END_DOCUMENT, parser.nextToken());
        parser.close();
    }

    @Test
    public void testSimpleValue() throws Exception {
        JsonParser parser = new TreeTraversingParser(new TextNode("Hello"));
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("Hello", parser.getText());
        parser.close();
    }

    @Test
    public void testSimpleObject() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("{\"name\": \"John\", \"age\": 30}");
        JsonParser parser = new TreeTraversingParser(node);

        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals("name", parser.getCurrentName());
        assertEquals("John", parser.getText());
        parser.nextToken(); // nextKey: "age"
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(30, parser.getIntValue());
        parser.close();
    }

    @Test
    public void testSimpleArray() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("[1, 2, 3]");
        JsonParser parser = new TreeTraversingParser(node);

        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals(1, parser.getIntValue());
        parser.nextToken(); // nextValue: 2
        assertEquals(2, parser.getIntValue());
        parser.nextToken(); // nextValue: 3
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        parser.close();
    }

    @Test
    public void testNestedObjects() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("{\"name\":{\"first\":\"John\",\"last\":\"Doe\"},\"age\":30}");
        JsonParser parser = new TreeTraversingParser(node);

        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals("name", parser.getCurrentName());
        parser.nextToken(); // nextKey: "first"
        assertEquals("John", parser.getText());
        parser.nextToken(); // nextKey: "last"
        assertEquals("Doe", parser.getText());
        parser.nextToken(); // nextKey: "age"
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(30, parser.getIntValue());
        parser.close();
    }

    @Test
    public void testNestedArrays() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("{ \"a\": [1, [2, 3], 4]], \"b\": [5, 6]}");
        JsonParser parser = new TreeTraversingParser(node);

        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals("a", parser.getCurrentName());
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals(1, parser.getIntValue());
        parser.nextToken(); // nextValue: [2, 3]
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals(2, parser.getIntValue());
        parser.nextToken(); // nextValue: 3
        parser.nextToken(); // nextValue: 4
        parser.nextToken(); // nextKey: "b"
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals(5, parser.getIntValue());
        parser.nextToken(); // nextValue: 6
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        parser.close();
    }

    @Test
    public void testNullValue() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("{ \"a\": null }");
        JsonParser parser = new TreeTraversingParser(node);

        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals("a", parser.getCurrentName());
        assertEquals(null, parser.getEmbeddedObject());
        parser.nextToken(); // nextKey: null
        parser.close();
    }

    @Test
    public void testComplexObjectWithBinaries() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("{ \"name\": \"John\", \"friends\": [{\"name\": \"Alice\", \"photo\": \"data:image/jpeg;base64,..." +
                "\"}], \"age\": 30 }");
        JsonParser parser = new TreeTraversingParser(node);

        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals("name", parser.getCurrentName());
        assertEquals("John", parser.getText());
        parser.nextToken(); // nextKey: "friends"
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals("name", parser.getCurrentName());
        assertEquals("Alice", parser.getText());
        JsonNode photo = parser.getEmbeddedObject();
        assertTrue(photo instanceof Base64Binary);
        parser.nextToken(); // nextKey: null
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(30, parser.getIntValue());
        parser.close();
    }

    @Test(expected = JsonParseException.class)
    public void testReadBeyondEnd() throws Exception {
        JsonParser parser = new TreeTraversingParser(new TextNode(""));
        parser.nextToken();
        parser.nextToken(); // this should fail
    }

    @Test(expected = JsonParseException.class)
    public void testNullParser() throws Exception {
        JsonParser parser = null;
        parser.nextToken();
    }

    @Test(expected = JsonParseException.class)
    public void testMalformedJSON() throws Exception {
        JsonParser parser = new TreeTraversingParser(new TextNode("{ age: 30"));
        parser.nextToken();
        parser.nextToken(); // this should fail
    }

    private JsonParser _constructError(String message) throws JsonParseException {
        JsonParser parser = new TreeTraversingParser(new TextNode(""));
        parser.parseError(new ParseContext(new JsonLocation(0, 0), JsonToken.START_DOCUMENT), message);
        return parser;
    }
}