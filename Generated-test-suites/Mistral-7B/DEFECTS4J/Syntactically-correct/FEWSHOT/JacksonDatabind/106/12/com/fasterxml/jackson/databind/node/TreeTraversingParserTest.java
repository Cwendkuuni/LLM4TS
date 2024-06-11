package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;

import org.junit.Test;

public class TreeTraversingParserTest {

    @Test
    public void testConfiguration() {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertNull(parser._objectCodec);
        assertNull(parser._nodeCursor);

        ObjectCodec codec = new ObjectMapper().getFactory().findValueSerializer(JsonType.FIELD_NAME);
        parser = new TreeTraversingParser(null, codec);
        assertEquals(codec, parser._objectCodec);
        assertNull(parser._nodeCursor);

        JsonNode jsonNode = new ObjectMapper().readTree("{'a': 1, 'b': [1, 2, 3]}");
        parser = new TreeTraversingParser(jsonNode);
        assertEquals(codec, parser._objectCodec);
        assertNotNull(parser._nodeCursor);
    }

    @Test
    public void testLifeCycle() throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree("{'a': 1, 'b': [1, 2, 3]}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        assertEquals(JsonToken.START_OBJECT, parser.nextToken());

        parser.close();
        assertEquals(JsonToken.NULL, parser.nextToken());
    }

    @Test
    public void testPublicApiTraversal() throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree("{'a': 1, 'b': [1, 2, 3]}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals("a", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(1, parser.getIntValue());

        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("b", parser.getCurrentName());
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());

        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(1, parser.getIntValue());

        parser.skipChildren();

        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(2, parser.getIntValue());

        parser.skipChildren();

        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(3, parser.getIntValue());

        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        assertNull(parser.nextToken());
    }

    @Test
    public void testPublicApiTokenAccessors() throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree("{'a': 1, 'b': [1, 2, 3]}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        assertEquals("a", parser.getCurrentName());
        parser.overrideCurrentName("c");
        assertEquals("c", parser.getCurrentName());

        assertEquals(JsonLocation.NA, parser.getParsingContext());
        assertEquals(JsonLocation.NA, parser.getTokenLocation());
        assertEquals(JsonLocation.NA, parser.getCurrentLocation());
    }

    @Test
    public void testPublicApiTextAccess() throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree("{'a': 'foo', 'b': [{'c': 'bar'}]}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        assertEquals("foo", parser.getText());
        assertEquals("bar", parser.nextToken().getValueAsString());
        assertEquals("bar", parser.getText());

        parser.close();
        assertNull(parser.getText());
    }

    @Test
    public void testPublicApiTypedNonTextAccess() throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree("{'a': 1, 'b': 3.14}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(1, parser.getIntValue());

        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("b", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_NUMBER_FLOAT, parser.nextToken());
        assertEquals(3.14, parser.getDoubleValue(), 0.0);

        parser.close();
        assertNull(parser.getCurrentName());
    }

    @Test
    public void testPublicApiBinaryAccess() throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree("{'a': 'data:image/png;base64,iVBORw0KGg...'}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);

        byte[] binaryData = parser.getBinaryValue(Base64Variants.STANDARD);
        assertNotNull(binaryData);
        assertEquals(binaryData.length, 1024); // Adjust this value based on the actual binary data size

        parser.close();
        assertNull(parser.getBinaryValue(Base64Variants.STANDARD));
    }
}