package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.BaseTestUtils;

import org.junit.Test;

public class TreeTraversingParserTest {

    @Test
    public void testEmptyNode() throws Exception {
        JsonParser parser = new TreeTraversingParser(null);
        JsonToken token = parser.nextToken();
        assertNull(token);
    }

    @Test
    public void testRootNode() throws Exception {
        String json = "{}";
        JsonParser parser = new TreeTraversingParser(BaseTestUtils.parseMemoized(json));
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.START_OBJECT, token);
        token = parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, token);
        parser.close();
    }

    @Test
    public void testArrayNode() throws Exception {
        String json = "[1,2,3]";
        JsonParser parser = new TreeTraversingParser(BaseTestUtils.parseMemoized(json));
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.START_ARRAY, token);
        token = parser.nextToken();
        assertEquals(JsonToken.INT, token);
        assertEquals(1, parser.getIntValue());
        token = parser.nextToken();
        assertEquals(JsonToken.INT, token);
        assertEquals(2, parser.getIntValue());
        token = parser.nextToken();
        assertEquals(JsonToken.INT, token);
        assertEquals(3, parser.getIntValue());
        token = parser.nextToken();
        assertEquals(JsonToken.END_ARRAY, token);
        parser.close();
    }

    @Test
    public void testObjectNode() throws Exception {
        String json = "{\"foo\":1,\"bar\":2}";
        JsonParser parser = new TreeTraversingParser(BaseTestUtils.parseMemoized(json));
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.START_OBJECT, token);
        token = parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        assertEquals("foo", parser.getCurrentName());
        token = parser.nextToken();
        assertEquals(JsonToken.INT, token);
        assertEquals(1, parser.getIntValue());
        token = parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, token);
        assertEquals("bar", parser.getCurrentName());
        token = parser.nextToken();
        assertEquals(JsonToken.INT, token);
        assertEquals(2, parser.getIntValue());
        token = parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, token);
        parser.close();
    }

    @Test
    public void testNextToken() throws Exception {
        String json = "[1,\"two\",3.0]";
        JsonParser parser = new TreeTraversingParser(BaseTestUtils.parseMemoized(json));
        JsonToken token = parser.nextToken();
        assertEquals(JsonToken.START_ARRAY, token);
        token = parser.nextToken();
        assertEquals(JsonToken.INT, token);
        assertEquals(1, parser.getIntValue());
        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_STRING, token);
        assertEquals("two", parser.getText());
        token = parser.nextToken();
        assertEquals(JsonToken.VALUE_NUMBER_FLOAT, token);
        assertEquals(3.0, parser.getDoubleValue(), 0.0);
        token = parser.nextToken();
        assertEquals(JsonToken.END_ARRAY, token);
        parser.close();
    }

    @Test
    public void testNextValue() throws Exception {
        String json = "[1,\"two\",3.0]";
        JsonParser parser = new TreeTraversingParser(BaseTestUtils.parseMemoized(json));
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        assertEquals(3.0, parser.getDoubleValue(), 0.0);
        parser.close();
    }

    @Test
    public void testSkipChildren() throws Exception {
        String json = "{\"foo\":[1,2],\"bar\":[3,4]}";
        JsonParser parser = new TreeTraversingParser(BaseTestUtils.parseMemoized(json));
        parser.nextToken();
        parser.nextToken();
        parser.skipChildren();
        parser.nextToken();
        parser.nextToken();
        parser.skipChildren();
        parser.close();
    }

    @Test(expected = JsonParseException.class)
    public void testNullNode() throws Exception {
        JsonParser parser = new TreeTraversingParser(null);
        parser.nextToken();
        parser.close();
    }

    @Test
    public void testCloseParser() throws Exception {
        String json = "{\"foo\":1}";
        JsonParser parser = new TreeTraversingParser(BaseTestUtils.parseMemoized(json));
        parser.nextToken();
        parser.nextToken();
        parser.close();
        parser.nextToken(); // should throw error
    }

    @Test
    public void testTextAccessors() throws Exception {
        String json = "{\"foo\":\"bar\"}";
        JsonParser parser = new TreeTraversingParser(BaseTestUtils.parseMemoized(json));
        parser.nextToken();
        parser.nextToken();
        assertEquals("bar", parser.getText());
        assertEquals("bar".toCharArray(), parser.getTextCharacters());
        assertEquals(4, parser.getTextLength());
        assertEquals(0, parser.getTextOffset());
        parser.close();
    }

    @Test
    public void testNumericAccessors() throws Exception {
        String json = "[1,2.5,3]";
        JsonParser parser = new TreeTraversingParser(BaseTestUtils.parseMemoized(json));
        parser.nextToken();
        parser.nextToken();
        assertEquals(1, parser.getIntValue());
        assertEquals(1L, parser.getLongValue());
        assertEquals(1.0, parser.getDoubleValue(), 0.0);
        parser.nextToken();
        parser.nextToken();
        assertEquals(2.5, parser.getDoubleValue(), 0.0);
        parser.nextToken();
        parser.nextToken();
        assertEquals(3, parser.getIntValue());
        assertEquals(3L, parser.getLongValue());
        assertEquals(3.0, parser.getDoubleValue(), 0.0);
        parser.close();
    }

    @Test
    public void testBinaryAccessors() throws Exception {
        String json = "{\"foo\":\"data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAAD/2wBDAAMCAAMDAmEAAAQ...";
        JsonParser parser = new TreeTraversingParser(BaseTestUtils.parseMemoized(json));
        parser.nextToken();
        parser.nextToken();
        byte[] binaryData = parser.getBinaryValue(JsonParser.Base64Variant.BASE64);
        assertNotNull(binaryData);
        parser.close();
    }

    @Test
    public void testEarlyCloseParser() throws Exception {
        String json = "[1,2]";
        JsonParser parser = new TreeTraversingParser(BaseTestUtils.parseMemoized(json));
        parser.nextToken();
        parser.nextToken();
        parser.close();
    }
}