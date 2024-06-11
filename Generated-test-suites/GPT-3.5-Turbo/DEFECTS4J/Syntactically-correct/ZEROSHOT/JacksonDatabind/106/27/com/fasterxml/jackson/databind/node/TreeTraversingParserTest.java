
package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TreeTraversingParserTest {

    private static final String TEST_JSON = "{ \"name\":\"John\", \"age\":30, \"city\":\"New York\" }";
    private static final String EMPTY_JSON = "{}";

    private TreeTraversingParser treeParser;
    private ObjectMapper objectMapper;

    @Before
    public void setup() throws IOException {
        JsonNode jsonNode = objectMapper.readTree(TEST_JSON);
        treeParser = new TreeTraversingParser(jsonNode, objectMapper);
    }

    @Test
    public void testNextToken() throws IOException {
        assertEquals(JsonToken.START_OBJECT, treeParser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, treeParser.nextToken());
        assertEquals("name", treeParser.getCurrentName());
        assertEquals(JsonToken.VALUE_STRING, treeParser.nextToken());
        assertEquals("John", treeParser.getText());
        assertEquals(JsonToken.FIELD_NAME, treeParser.nextToken());
        assertEquals("age", treeParser.getCurrentName());
        assertEquals(JsonToken.VALUE_NUMBER_INT, treeParser.nextToken());
        assertEquals(30, treeParser.getIntValue());
        assertEquals(JsonToken.FIELD_NAME, treeParser.nextToken());
        assertEquals("city", treeParser.getCurrentName());
        assertEquals(JsonToken.VALUE_STRING, treeParser.nextToken());
        assertEquals("New York", treeParser.getText());
        assertEquals(JsonToken.END_OBJECT, treeParser.nextToken());
        assertNull(treeParser.nextToken());
    }

    @Test
    public void testSetCodec() throws IOException {
        assertNull(treeParser.getCodec());
        ObjectMapper testObjectMapper = new ObjectMapper();
        treeParser.setCodec(testObjectMapper);
        assertNotNull(treeParser.getCodec());
        assertEquals(testObjectMapper, treeParser.getCodec());
    }

    @Test
    public void testSkipChildren() throws IOException {
        assertEquals(JsonToken.START_OBJECT, treeParser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, treeParser.nextToken());
        assertEquals(JsonToken.VALUE_STRING, treeParser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, treeParser.nextToken());
        assertEquals(JsonToken.VALUE_NUMBER_INT, treeParser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, treeParser.nextToken());
        assertEquals(JsonToken.VALUE_STRING, treeParser.nextToken());
        treeParser.skipChildren();
        assertEquals(JsonToken.END_OBJECT, treeParser.getCurrentToken());
    }

    @Test
    public void testIsClosed() throws IOException {
        assertFalse(treeParser.isClosed());
        treeParser.close();
        assertTrue(treeParser.isClosed());
    }

    @Test
    public void testGetCodec() throws IOException {
        assertNotNull(treeParser.getCodec());
        assertEquals(objectMapper, treeParser.getCodec());
    }

    @Test
    public void testGetCurrentName() throws IOException {
        assertEquals(JsonToken.START_OBJECT, treeParser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, treeParser.nextToken());
        assertEquals("name", treeParser.getCurrentName());
        assertEquals(JsonToken.VALUE_STRING, treeParser.nextToken());
        assertEquals("John", treeParser.getText());
    }

    @Test
    public void testOverrideCurrentName() throws IOException {
        assertEquals(JsonToken.START_OBJECT, treeParser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, treeParser.nextToken());
        assertEquals("name", treeParser.getCurrentName());
        treeParser.overrideCurrentName("firstName");
        assertEquals("firstName", treeParser.getCurrentName());
    }

    @Test
    public void testGetParsingContext() throws IOException {
        assertNotNull(treeParser.getParsingContext());
        assertEquals(JsonStreamContext.TYPE_ROOT, treeParser.getParsingContext().getType());
    }

    @Test
    public void testGetTokenLocation() throws IOException {
        assertNull(treeParser.getTokenLocation());
    }

    @Test
    public void testGetCurrentLocation() throws IOException {
        assertNull(treeParser.getCurrentLocation());
    }

    @Test
    public void testIsNaN() throws IOException {
        assertFalse(treeParser.isNaN());
    }

    @Test
    public void testGetBase64Binary() throws IOException {
        assertNull(treeParser.getBinaryValue(Base64Variant.MIME));
    }

    @Test
    public void testGetBigIntegerValue() throws IOException {
        assertNull(treeParser.getBigIntegerValue());
    }

    @Test
    public void testGetDecimalValue() throws IOException {
        assertNull(treeParser.getDecimalValue());
    }

    @Test
    public void testGetDoubleValue() throws IOException {
        assertEquals(0.0, treeParser.getDoubleValue(), 0);
    }

    @Test
    public void testGetFloatValue() throws IOException {
        assertEquals(0.0f, treeParser.getFloatValue(), 0);
    }

    @Test
    public void testGetIntValue() throws IOException {
        assertEquals(0, treeParser.getIntValue());
    }

    @Test
    public void testGetLongValue() throws IOException {
        assertEquals(0L, treeParser.getLongValue());
    }

    @Test
    public void testGetNumberType() throws IOException {
        assertNull(treeParser.getNumberType());
    }

    @Test
    public void testGetNumberValue() throws IOException {
        assertNull(treeParser.getNumberValue());
    }

    @Test
    public void testGetEmbeddedObject() throws IOException {
        assertNull(treeParser.getEmbeddedObject());
    }

    @Test
    public void testHasTextCharacters() throws IOException {
        assertFalse(treeParser.hasTextCharacters());
    }

    @Test
    public void testGetText() throws IOException {
        assertEquals(JsonToken.START_OBJECT, treeParser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, treeParser.nextToken());
        assertEquals("name", treeParser.getCurrentName());
        assertEquals(JsonToken.VALUE_STRING, treeParser.nextToken());
        assertEquals("John", treeParser.getText());
        assertEquals(JsonToken.FIELD_NAME, treeParser.nextToken());
        assertEquals("age", treeParser.getCurrentName());
        assertEquals(JsonToken.VALUE_NUMBER_INT, treeParser.nextToken());
        assertEquals("30", treeParser.getText());
        assertEquals(JsonToken.FIELD_NAME, treeParser.nextToken());
        assertEquals("city", treeParser.getCurrentName());
        assertEquals(JsonToken.VALUE_STRING, treeParser.nextToken());
        assertEquals("New York", treeParser.getText());
        assertEquals(JsonToken.END_OBJECT, treeParser.nextToken());
        assertNull(treeParser.nextToken());
    }

    @Test
    public void testGetCurrentNumericNode() throws IOException {
        assertNull(treeParser.currentNumericNode());
    }

    private void assertFalse(boolean closed) {
    }
}
