package com.example.junit;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;

import org.junit.Test;

public class TreeTraversingParserTest {

    @Test
    public void testConstructorWithJsonNode() {
        JsonNode jsonNode = new ObjectMapper().createObjectNode();
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        assertNull(parser.getNextToken());
        assertEquals(JsonToken.START_OBJECT, parser.getNextToken());
        parser.close();
    }

    @Test
    public void testConstructorWithJsonNodeAndCodec() {
        JsonNode jsonNode = new ObjectMapper().createObjectNode();
        ObjectMapper mapper = new ObjectMapper();
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode, mapper.getObjectCodec());
        assertNull(parser.getNextToken());
        assertEquals(JsonToken.START_OBJECT, parser.getNextToken());
        parser.close();
    }

    @Test
    public void testSetCodec() {
        ObjectMapper mapper = new ObjectMapper();
        TreeTraversingParser parser = new TreeTraversingParser(new Object());
        ObjectCodec oldCodec = parser.getCodec();
        parser.setCodec(mapper.getObjectCodec());
        assertEquals(mapper.getObjectCodec(), parser.getCodec());
        parser.close();
        assertEquals(oldCodec, parser.getCodec()); // should return old codec since parser is closed
    }

    @Test
    public void testVersion() {
        TreeTraversingParser parser = new TreeTraversingParser(new Object());
        assertEquals(com.fasterxml.jackson.databind.cfg.PackageVersion.VERSION, parser.version());
    }

    @Test(expected = IllegalStateException.class)
    public void testCloseTwice() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(new Object());
        parser.close();
        parser.close();
    }

    @Test
    public void testNextToken() throws IOException {
        JsonNode jsonNode = new ObjectMapper().createObjectNode();
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken(); // consume start object token
        parser.nextToken(); // consume key1 token
        parser.nextToken(); // consume value1 token
        parser.nextToken(); // consume comma token
        parser.nextToken(); // consume key2 token
        parser.nextToken(); // consume value2 token
        parser.nextToken(); // consume comma token
        parser.nextToken(); // consume key3 token
        parser.nextToken(); // consume value3 token
        parser.nextToken(); // consume end object token
        parser.nextToken(); // should return null
        parser.close();
    }

    @Test
    public void testSkipChildren() throws IOException {
        JsonNode jsonNode = new ObjectMapper().createObjectNode();
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken(); // consume start object token
        parser.nextToken(); // consume key1 token
        parser.nextToken(); // consume value1 token
        parser.nextToken(); // consume comma token
        parser.nextToken(); // consume key2 token
        parser.nextToken(); // consume value2 token
        parser.nextToken(); // consume comma token
        parser.nextToken(); // consume key3 token
        parser.nextToken(); // consume value3 token
        parser.nextToken(); // consume end object token
        parser.skipChildren();
        parser.nextToken(); // should return null
        parser.close();
    }

    @Test
    public void testIsClosed() {
        TreeTraversingParser parser = new TreeTraversingParser(new Object());
        assertFalse(parser.isClosed());
        parser.close();
        assertTrue(parser.isClosed());
    }

    @Test
    public void testGetText() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("{\"key1\":\"value1\",\"key2\":\"value2\"}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken(); // consume start object token
        parser.nextToken(); // consume key1 token
        assertEquals("value1", parser.getText());
        parser.nextToken(); // consume value1 token
        parser.nextToken(); // consume comma token
        parser.nextToken(); // consume key2 token
        assertEquals("value2", parser.getText());
        parser.nextToken(); // consume value2 token
        parser.nextToken(); // consume end object token
        parser.nextToken(); // should return null
        parser.close();
    }

    @Test
    public void testGetTextCharacters() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("{\"key1\":\"value1\",\"key2\":\"value2\"}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken(); // consume start object token
        parser.nextToken(); // consume key1 token
        char[] textCharacters = parser.getTextCharacters();
        assertEquals("value1", new String(textCharacters));
        parser.nextToken(); // consume value1 token
        parser.nextToken(); // consume comma token
        parser.nextToken(); // consume key2 token
        textCharacters = parser.getTextCharacters();
        assertEquals("value2", new String(textCharacters));
        parser.nextToken(); // consume value2 token
        parser.nextToken(); // consume end object token
        parser.nextToken(); // should return null
        parser.close();
    }

    @Test
    public void testGetTextLength() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("{\"key1\":\"value1\",\"key2\":\"value2\"}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken(); // consume start object token
        parser.nextToken(); // consume key1 token
        assertEquals(6, parser.getTextLength());
        parser.nextToken(); // consume value1 token
        assertEquals(6, parser.getTextLength());
        parser.nextToken(); // consume comma token
        parser.nextToken(); // consume key2 token
        assertEquals(6, parser.getTextLength());
        parser.nextToken(); // consume value2 token
        parser.nextToken(); // consume end object token
        parser.nextToken(); // should return null
        parser.close();
    }

    @Test
    public void testGetTextOffset() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("{\"key1\":\"value1\",\"key2\":\"value2\"}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken(); // consume start object token
        parser.nextToken(); // consume key1 token
        assertEquals(0, parser.getTextOffset());
        parser.nextToken(); // consume value1 token
        assertEquals(0, parser.getTextOffset());
        parser.nextToken(); // consume comma token
        parser.nextToken(); // consume key2 token
        assertEquals(0, parser.getTextOffset());
        parser.nextToken(); // consume value2 token
        parser.nextToken(); // consume end object token
        parser.nextToken(); // should return null
        parser.close();
    }

    @Test
    public void testHasTextCharacters() {
        TreeTraversingParser parser = new TreeTraversingParser(new Object());
        assertFalse(parser.hasTextCharacters());
    }

    @Test
    public void testGetNumberType() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("{\"key1\":1,\"key2\":3.14}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken(); // consume start object token
        parser.nextToken(); // consume key1 token
        assertEquals(BigDecimal.TYPE, parser.getNumberType());
        parser.nextToken(); // consume value1 token
        parser.nextToken(); // consume comma token
        parser.nextToken(); // consume key2 token
        assertEquals(NumberType.FLOAT, parser.getNumberType());
        parser.nextToken(); // consume value2 token
        parser.nextToken(); // consume end object token
        parser.nextToken(); // should return null
        parser.close();
    }

    @Test
    public void testGetBigIntegerValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("{\"key1\":12345}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken(); // consume start object token
        parser.nextToken(); // consume key1 token
        BigInteger value = parser.getBigIntegerValue();
        assertEquals(new BigInteger("12345"), value);
        parser.nextToken(); // consume value1 token
        parser.nextToken(); // consume comma token
        parser.nextToken(); // consume end object token
        parser.nextToken(); // should return null
        parser.close();
    }

    @Test
    public void testGetDecimalValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("{\"key1\":123.45}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken(); // consume start object token
        parser.nextToken(); // consume key1 token
        BigDecimal value = parser.getDecimalValue();
        assertEquals(new BigDecimal("123.45"), value);
        parser.nextToken(); // consume value1 token
        parser.nextToken(); // consume comma token
        parser.nextToken(); // consume end object token
        parser.nextToken(); // should return null
        parser.close();
    }

    @Test
    public void testGetDoubleValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("{\"key1\":123.45}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken(); // consume start object token
        parser.nextToken(); // consume key1 token
        double value = parser.getDoubleValue();
        assertEquals(123.45, value, 0.001);
        parser.nextToken(); // consume value1 token
        parser.nextToken(); // consume comma token
        parser.nextToken(); // consume end object token
        parser.nextToken(); // should return null
        parser.close();
    }

    @Test
    public void testGetFloatValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("{\"key1\":123.45f}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken(); // consume start object token
        parser.nextToken(); // consume key1 token
        float value = parser.getFloatValue();
        assertEquals(123.45f, value, 0.001f);
        parser.nextToken(); // consume value1 token
        parser.nextToken(); // consume comma token
        parser.nextToken(); // consume end object token
        parser.nextToken(); // should return null
        parser.close();
    }

    @Test
    public void testGetIntValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("{\"key1\":123}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken(); // consume start object token
        parser.nextToken(); // consume key1 token
        int value = parser.getIntValue();
        assertEquals(123, value);
        parser.nextToken(); // consume value1 token
        parser.nextToken(); // consume comma token
        parser.nextToken(); // consume end object token
        parser.nextToken(); // should return null
        parser.close();
    }

    @Test
    public void testGetLongValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("{\"key1\":1234567890123L}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken(); // consume start object token
        parser.nextToken(); // consume key1 token
        long value = parser.getLongValue();
        assertEquals(1234567890123L, value);
        parser.nextToken(); // consume value1 token
        parser.nextToken(); // consume comma token
        parser.nextToken(); // consume end object token
        parser.nextToken(); // should return null
        parser.close();
    }

    @Test
    public void testGetNumberValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("{\"key1\":123.45}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken(); // consume start object token
        parser.nextToken(); // consume key1 token
        Number value = parser.getNumberValue();
        assertEquals(123.45, value.doubleValue(), 0.001);
        parser.nextToken(); // consume value1 token
        parser.nextToken(); // consume comma token
        parser.nextToken(); // consume end object token
        parser.nextToken(); // should return null
        parser.close();
    }

    @Test
    public void testGetEmbeddedObject() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.createObjectNode();
        jsonNode.put("key1", mapper.createBinaryNode(new byte[]{1, 2, 3}));
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken(); // consume start object token
        parser.nextToken(); // consume key1 token
        Object value = parser.getEmbeddedObject();
        assertArrayEquals(new byte[]{1, 2, 3}, ((byte[]) value));
        parser.nextToken(); // consume value1 token
        parser.nextToken(); // consume end object token
        parser.nextToken(); // should return null
        parser.close();
    }

    @Test(expected = IOException.class)
    public void testGetEmbeddedObjectNull() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.createObjectNode();
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken(); // consume start object token
        parser.nextToken(); // consume key1 token
        parser.nextToken(); // consume value1 token
        parser.nextToken(); // consume end object token
        parser.nextToken(); // should throw IOException
        parser.getEmbeddedObject();
    }

    @Test(expected = JsonParseException.class)
    public void testGetEmbeddedObjectNotBinary() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.createObjectNode();
        jsonNode.put("key1", mapper.createObjectNode());
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        parser.nextToken(); // consume start object token
        parser.nextToken(); // consume key1 token
        parser.nextToken(); // consume value1 token
        parser.nextToken(); // consume end object token
        parser.nextToken(); // should throw JsonParseException
        parser.getEmbeddedObject();
    }
}