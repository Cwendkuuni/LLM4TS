package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.TestBase;

public class TreeTraversingParserTest extends TestBase {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testEmptyParser() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, parser.getCurrentToken());
        parser.close();
    }

    @Test
    public void testParserWithRootNode() throws IOException {
        JsonNode rootNode = MAPPER.readTree("{'a':1, 'b':[2, 3]}");
        TreeTraversingParser parser = new TreeTraversingParser(rootNode);

        // check initial state
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("a", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(1, parser.getIntValue());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("b", parser.getCurrentName());
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());

        // check array contents
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(2, parser.getIntValue());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(3, parser.getIntValue());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());

        // check end of object
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        parser.close();
    }

    @Test
    public void testParserWithNullNode() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, parser.getCurrentToken());
        parser.close();
    }

    @Test
    public void testParserWithObjectNode() throws IOException {
        JsonNode node = MAPPER.readTree("{'name':'John', 'age':42, 'hobbies':['reading', 'gardening']}");
        TreeTraversingParser parser = new TreeTraversingParser(node, new ObjectMapper.DefaultTyping());

        // check initial state
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("name", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("John", parser.getText());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("age", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(42, parser.getIntValue());

        // check array
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("hobbies", parser.getCurrentName());
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());

        // check first element
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("reading", parser.getText());

        // check second element
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("gardening", parser.getText());

        // check end of array
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());

        // check end of object
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        parser.close();
    }

    @Test
    public void testParserWithNumberNode() throws IOException {
        JsonNode node = MAPPER.readTree("{123}");
        TreeTraversingParser parser = new TreeTraversingParser(node, new ObjectMapper.DefaultTyping());

        // check initial state
        assertEquals(JsonToken.START_NUMBER, parser.nextToken());
        assertEquals(123L, parser.getLongValue());
        assertEquals(JsonToken.END_NUMBER, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        parser.close();
    }

    @Test
    public void testParserWithBinaryNode() throws IOException {
        JsonNode node = MAPPER.readTree("{'image': {'data': [0x00, 0x01, 0x02, ...]}}");
        TreeTraversingParser parser = new TreeTraversingParser(node, new ObjectMapper.DefaultTyping());

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("image", parser.getCurrentName());
        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("data", parser.getCurrentName());
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());

        // read binary data
        int length = parser.readBinaryValue(Base64Variants.standard, output);
        assertEquals(4, length);

        byte[] binaryData = output.toByteArray();
        assertArrayEquals(new byte[] {0x00, 0x01, 0x02, 0x03}, binaryData);

        // check end of array
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        parser.close();
    }

    @Test
    public void testParserWithNullBinaryNode() throws IOException {
        JsonNode node = MAPPER.readTree("{'image': null}");
        TreeTraversingParser parser = new TreeTraversingParser(node, new ObjectMapper.DefaultTyping());

        // check initial state
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("image", parser.getCurrentName());
        assertEquals(JsonToken.NULL, parser.nextToken());

        // check end of object
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        parser.close();
    }

    @Test(expected = JsonMappingException.class)
    public void testParserWithMalformedNode() throws IOException {
        JsonNode node = MAPPER.readTree("{'name':[1, 2]}");
        TreeTraversingParser parser = new TreeTraversingParser(node, new ObjectMapper.DefaultTyping());

        // check initial state
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("name", parser.getCurrentName());
        assertEquals(JsonToken.START_ARRAY, parser.nextToken());

        // should throw an exception here
        parser.nextToken();
    }

    @Test
    public void testParserWithSkippedNode() throws IOException {
        JsonNode node = MAPPER.readTree("{'name':1, 'age':{'value':30}}");
        TreeTraversingParser parser = new TreeTraversingParser(node, new ObjectMapper.DefaultTyping());

        // check initial state
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("name", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(1, parser.getIntValue());

        // skip object "age"
        parser.skipChildren();

        // check end of object
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        parser.close();
    }

    @Test
    public void testParserWithClosedNode() throws IOException {
        JsonNode node = MAPPER.readTree("{'name':1, 'age':{'value':30}}");
        TreeTraversingParser parser = new TreeTraversingParser(node, new ObjectMapper.DefaultTyping());

        // check initial state
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("name", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(1, parser.getIntValue());

        // close "name" node
        parser.close();

        // check current state
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("age", parser.getCurrentName());
        parser.skipChildren();

        // check end of object
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        parser.close();
    }
}