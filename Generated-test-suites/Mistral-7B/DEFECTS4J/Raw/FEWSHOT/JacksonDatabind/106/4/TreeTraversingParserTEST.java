package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.ParserMinimalBase;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.NodeCursor;
import com.fasterxml.jackson.databind.ObjectCodec;
import com.fasterxml.jackson.databind.cfg.PackageVersion;
import org.junit.Test;

public class TreeTraversingParserTest {

    @Test
    public void testEmptyParser() {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertNull(parser.nextToken());
        parser.close();
    }

    @Test
    public void testArrayParser() throws IOException {
        ObjectCodec codec = createObjectCodec();
        JsonNode jsonNode = createJsonArray();
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode, codec);

        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        parser.close();
    }

    @Test
    public void testObjectParser() throws IOException {
        ObjectCodec codec = createObjectCodec();
        JsonNode jsonNode = createJsonObject();
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode, codec);

        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals("name1", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("value1", parser.getText());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("name2", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(123, parser.getIntValue());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        parser.close();
    }

    @Test
    public void testValueNodeParser() throws IOException {
        ObjectCodec codec = createObjectCodec();
        JsonNode jsonNode = createJsonValue("42");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode, codec);

        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(42, parser.getIntValue());
        parser.close();
    }

    @Test
    public void testSkipChildren() throws IOException {
        ObjectCodec codec = createObjectCodec();
        JsonNode jsonNode = createJsonObject();
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode, codec);

        parser.nextToken(); // START_OBJECT
        parser.nextToken(); // name1
        parser.nextToken(); // value1
        parser.nextToken(); // name2
        parser.nextToken(); // value2
        parser.nextToken(); // END_OBJECT

        parser.skipChildren();
        assertEquals(JsonToken.START_OBJECT, parser.nextToken()); // checking for correct state
    }

    @Test(expected = JsonParseException.class)
    public void testClosedParser() throws IOException {
        ObjectCodec codec = createObjectCodec();
        JsonNode jsonNode = createJsonObject();
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode, codec);
        parser.close();
        parser.nextToken();
    }

    @Test
    public void testIsClosed() {
        ObjectCodec codec = createObjectCodec();
        JsonNode jsonNode = createJsonObject();
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode, codec);
        assertFalse(parser.isClosed());
        parser.close();
        assertTrue(parser.isClosed());
    }

    private ObjectCodec createObjectCodec() {
        return new ObjectMapper().getFactory().getCodec();
    }

    private JsonNode createJsonArray() {
        return new ObjectMapper().readValue("[1, 2, 3]", JsonNode.class);
    }

    private JsonNode createJsonObject() {
        return new ObjectMapper().readValue("{\"name1\":\"value1\",\"name2\":123}", JsonNode.class);
    }

    private JsonNode createJsonValue(String value) {
        return new ObjectMapper().readValue(value, JsonNode.class);
    }
}