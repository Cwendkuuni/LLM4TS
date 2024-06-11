package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TextNode;

import org.junit.*;

public class TreeTraversingParserTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @BeforeClass
    public static void setup() {
        // Set up ObjectMapper for test
    }

    @AfterClass
    public static void tearDown() {
        // Clean up ObjectMapper for test
    }

    @Before
    public void resetParser() {
        _parser = null;
    }

    private TreeTraversingParser _parser;

    @Test
    public void testEmptyArray() throws Exception {
        JsonNode jsonNode = MAPPER.readTree("[]");
        _parser = new TreeTraversingParser(jsonNode);

        assertEquals(JsonToken.START_ARRAY, _parser.nextToken());
        assertEquals(JsonToken.END_ARRAY, _parser.nextToken());
        assertTrue(_parser.isClosed());
    }

    @Test
    public void testEmptyObject() throws Exception {
        JsonNode jsonNode = MAPPER.readTree("{}");
        _parser = new TreeTraversingParser(jsonNode);

        assertEquals(JsonToken.START_OBJECT, _parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, _parser.nextToken());
        assertTrue(_parser.isClosed());
    }

    @Test
    public void testSimpleObject() throws Exception {
        JsonNode jsonNode = MAPPER.readTree("{\"name\": \"John\", \"age\": 30}");
        _parser = new TreeTraversingParser(jsonNode);

        assertEquals(JsonToken.START_OBJECT, _parser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, _parser.nextToken());
        assertEquals("name", _parser.getCurrentName());
        assertEquals(JsonToken.VALUE_STRING, _parser.nextToken());
        assertEquals("John", _parser.getText());
        assertEquals(JsonToken.FIELD_NAME, _parser.nextToken());
        assertEquals("age", _parser.getCurrentName());
        assertEquals(JsonToken.VALUE_NUMBER_INT, _parser.nextToken());
        assertEquals(30, _parser.getIntValue());
        assertEquals(JsonToken.END_OBJECT, _parser.nextToken());
        assertTrue(_parser.isClosed());
    }

    @Test
    public void testSimpleArray() throws Exception {
        JsonNode jsonNode = MAPPER.readTree("[1, 2.5, \"Hello\", true]");
        _parser = new TreeTraversingParser(jsonNode);

        assertEquals(JsonToken.START_ARRAY, _parser.nextToken());
        assertEquals(JsonToken.VALUE_NUMBER_INT, _parser.nextToken());
        assertEquals(1, _parser.getIntValue());
        assertEquals(JsonToken.VALUE_NUMBER_FLOAT, _parser.nextToken());
        assertEquals(2.5, _parser.getFloatValue(), 0.01f);
        assertEquals(JsonToken.VALUE_STRING, _parser.nextToken());
        assertEquals("Hello", _parser.getText());
        assertEquals(JsonToken.VALUE_BOOLEAN, _parser.nextToken());
        assertTrue(_parser.nextValue().isTrue());
        assertEquals(JsonToken.END_ARRAY, _parser.nextToken());
        assertTrue(_parser.isClosed());
    }

    @Test
    public void testNestedObjects() throws Exception {
        JsonNode jsonNode = MAPPER.readTree("{\"name\": \"John\", \"age\": 30, \"family\": {\"mother\": \"Mary\", \"father\": \"John\"}}");
        _parser = new TreeTraversingParser(jsonNode);

        assertEquals(JsonToken.START_OBJECT, _parser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, _parser.nextToken());
        assertEquals("name", _parser.getCurrentName());
        assertEquals(JsonToken.VALUE_STRING, _parser.nextToken());
        assertEquals("John", _parser.getText());
        assertEquals(JsonToken.FIELD_NAME, _parser.nextToken());
        assertEquals("age", _parser.getCurrentName());
        assertEquals(JsonToken.VALUE_NUMBER_INT, _parser.nextToken());
        assertEquals(30, _parser.getIntValue());
        assertEquals(JsonToken.FIELD_NAME, _parser.nextToken());
        assertEquals("family", _parser.getCurrentName());
        assertEquals(JsonToken.START_OBJECT, _parser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, _parser.nextToken());
        assertEquals("mother", _parser.getCurrentName());
        assertEquals(JsonToken.VALUE_STRING, _parser.nextToken());
        assertEquals("Mary", _parser.getText());
        assertEquals(JsonToken.FIELD_NAME, _parser.nextToken());
        assertEquals("father", _parser.getCurrentName());
        assertEquals(JsonToken.VALUE_STRING, _parser.nextToken());
        assertEquals("John", _parser.getText());
        assertEquals(JsonToken.END_OBJECT, _parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, _parser.nextToken());
        assertTrue(_parser.isClosed());
    }

    @Test
    public void testNestedArrays() throws Exception {
        JsonNode jsonNode = MAPPER.readTree("[1, [2, 3], 4, [5, \"Hello\", false]]");
        _parser = new TreeTraversingParser(jsonNode);

        assertEquals(JsonToken.START_ARRAY, _parser.nextToken());
        assertEquals(JsonToken.VALUE_NUMBER_INT, _parser.nextToken());
        assertEquals(1, _parser.getIntValue());
        assertEquals(JsonToken.START_ARRAY, _parser.nextToken());
        assertEquals(JsonToken.VALUE_NUMBER_INT, _parser.nextToken());
        assertEquals(2, _parser.getIntValue());
        assertEquals(JsonToken.VALUE_NUMBER_INT, _parser.nextToken());
        assertEquals(3, _parser.getIntValue());
        assertEquals(JsonToken.END_ARRAY, _parser.nextToken());
        assertEquals(JsonToken.VALUE_NUMBER_INT, _parser.nextToken());
        assertEquals(4, _parser.getIntValue());
        assertEquals(JsonToken.START_ARRAY, _parser.nextToken());
        assertEquals(JsonToken.VALUE_NUMBER_INT, _parser.nextToken());
        assertEquals(5, _parser.getIntValue());
        assertEquals(JsonToken.VALUE_STRING, _parser.nextToken());
        assertEquals("Hello", _parser.getText());
        assertEquals(JsonToken.VALUE_BOOLEAN, _parser.nextToken());
        assertFalse(_parser.nextValue().isTrue());
        assertEquals(JsonToken.END_ARRAY, _parser.nextToken());
        assertEquals(JsonToken.END_ARRAY, _parser.nextToken());
        assertTrue(_parser.isClosed());
    }

    @Test
    public void testStringValue() throws Exception {
        JsonNode jsonNode = MAPPER.readTree("{\"name\": \"John\", \"message\": \"Hello World\"}");
        _parser = new TreeTraversingParser(jsonNode);

        assertEquals(JsonToken.START_OBJECT, _parser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, _parser.nextToken());
        assertEquals("name", _parser.getCurrentName());
        assertEquals(JsonToken.VALUE_STRING, _parser.nextToken());
        assertEquals("John", _parser.getText());
        assertEquals(JsonToken.FIELD_NAME, _parser.nextToken());
        assertEquals("message", _parser.getCurrentName());
        assertEquals(JsonToken.VALUE_STRING, _parser.nextToken());
        assertEquals("Hello World", _parser.getText());
        assertEquals(JsonToken.END_OBJECT, _parser.nextToken());
        assertTrue(_parser.isClosed());
    }

    @Test
    public void testIntegerValue() throws Exception {
        JsonNode jsonNode = MAPPER.readTree("{\"age\": 30}");
        _parser = new TreeTraversingParser(jsonNode);

        assertEquals(JsonToken.START_OBJECT, _parser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, _parser.nextToken());
        assertEquals("age", _parser.getCurrentName());
        assertEquals(JsonToken.VALUE_NUMBER_INT, _parser.nextToken());
        assertEquals(30, _parser.getIntValue());
        assertEquals(JsonToken.END_OBJECT, _parser.nextToken());
        assertTrue(_parser.isClosed());
    }

    @Test
    public void testDoubleValue() throws Exception {
        JsonNode jsonNode = MAPPER.readTree("{\"weight\": 123.45}");
        _parser = new TreeTraversingParser(jsonNode);

        assertEquals(JsonToken.START_OBJECT, _parser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, _parser.nextToken());
        assertEquals("weight", _parser.getCurrentName());
        assertEquals(JsonToken.VALUE_NUMBER_FLOAT, _parser.nextToken());
        assertEquals(123.45, _parser.getDoubleValue(), 0.01);
        assertEquals(JsonToken.END_OBJECT, _parser.nextToken());
        assertTrue(_parser.isClosed());
    }

    @Test
    public void testBooleanValue() throws Exception {
        JsonNode jsonNode = MAPPER.readTree("{\"isStudent\": true, \"isMarried\": false}");
        _parser = new TreeTraversingParser(jsonNode);

        assertEquals(JsonToken.START_OBJECT, _parser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, _parser.nextToken());
        assertEquals("isStudent", _parser.getCurrentName());
        assertEquals(JsonToken.VALUE_BOOLEAN, _parser.nextToken());
        assertTrue(_parser.nextValue().isTrue());
        assertEquals(JsonToken.FIELD_NAME, _parser.nextToken());
        assertEquals("isMarried", _parser.getCurrentName());
        assertEquals(JsonToken.VALUE_BOOLEAN, _parser.nextToken());
        assertFalse(_parser.nextValue().isTrue());
        assertEquals(JsonToken.END_OBJECT, _parser.nextToken());
        assertTrue(_parser.isClosed());
    }

    @Test
    public void testNullValue() throws Exception {
        JsonNode jsonNode = MAPPER.readTree("{\"isStudent\": null, \"isMarried\": false}");
        _parser = new TreeTraversingParser(jsonNode);

        assertEquals(JsonToken.START_OBJECT, _parser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, _parser.nextToken());
        assertEquals("isStudent", _parser.getCurrentName());
        assertEquals(JsonToken.VALUE_NULL, _parser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, _parser.nextToken());
        assertEquals("isMarried", _parser.getCurrentName());
        assertEquals(JsonToken.VALUE_BOOLEAN, _parser.nextToken());
        assertFalse(_parser.nextValue().isTrue());
        assertEquals(JsonToken.END_OBJECT, _parser.nextToken());
        assertTrue(_parser.isClosed());
    }

    @Test
    public void testArrayValue() throws Exception {
        JsonNode jsonNode = MAPPER.readTree("[1, 2, 3]");
        _parser = new TreeTraversingParser(jsonNode);

        assertEquals(JsonToken.START_ARRAY, _parser.nextToken());
        assertEquals(JsonToken.VALUE_NUMBER_INT, _parser.nextToken());
        assertEquals(1, _parser.getIntValue());
        assertEquals(JsonToken.VALUE_NUMBER_INT, _parser.nextToken());
        assertEquals(2, _parser.getIntValue());
        assertEquals(JsonToken.VALUE_NUMBER_INT, _parser.nextToken());
        assertEquals(3, _parser.getIntValue());
        assertEquals(JsonToken.END_ARRAY, _parser.nextToken());
        assertTrue(_parser.isClosed());
    }

    @Test
    public void testObjectValue() throws Exception {
        JsonNode jsonNode = MAPPER.readTree("{\"name\": \"John\", \"age\": 30}");
        _parser = new TreeTraversingParser(jsonNode);

        assertEquals(JsonToken.START_OBJECT, _parser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, _parser.nextToken());
        assertEquals("name", _parser.getCurrentName());
        assertEquals(JsonToken.VALUE_STRING, _parser.nextToken());
        assertEquals("John", _parser.getText());
        assertEquals(JsonToken.FIELD_NAME, _parser.nextToken());
        assertEquals("age", _parser.getCurrentName());
        assertEquals(JsonToken.VALUE_NUMBER_INT, _parser.nextToken());
        assertEquals(30, _parser.getIntValue());
        assertEquals(JsonToken.END_OBJECT, _parser.nextToken());
        assertTrue(_parser.isClosed());
    }

    @Test
    public void testBinaryValue() throws Exception {
        JsonNode jsonNode = MAPPER.readTree("{\"data\": {\"type\": \"base64\", \"value\": \"VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY2hhcmFjdGl2ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=\"}}");
        _parser = new TreeTraversingParser(jsonNode);

        assertEquals(JsonToken.START_OBJECT, _parser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, _parser.nextToken());
        assertEquals("data", _parser.getCurrentName());
        assertEquals(JsonToken.VALUE_OBJECT, _parser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, _parser.nextToken());
        assertEquals("type", _parser.getCurrentName());
        assertEquals(JsonToken.VALUE_STRING, _parser.nextToken());
        assertEquals("base64", _parser.getText());
        assertEquals(JsonToken.FIELD_NAME, _parser.nextToken());
        assertEquals("value", _parser.getCurrentName());
        assertEquals(JsonToken.VALUE_BINARY, _parser.nextToken());
        byte[] binaryData = _parser.getBinaryValue(Base64Variants.STANDARD);
        assertEquals("Hello World", new String(binaryData, "UTF-8"));
        assertEquals(JsonToken.END_OBJECT, _parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, _parser.nextToken());
        assertTrue(_parser.isClosed());
    }

    @Test(expected = JsonParseException.class)
    public void testInvalidJson() throws Exception {
        JsonNode jsonNode = MAPPER.readTree("{name: 123}");
        _parser = new TreeTraversingParser(jsonNode);
        _parser.nextToken();
        _parser.nextToken();
        _parser.nextToken();
        _parser.nextToken();
        _parser.nextToken();
        _parser.nextToken();
        _parser.nextToken();
    }
}