package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;

import org.junit.Test;

public class TreeTraversingParserTest {

    @Test
    public void testGetText() throws Exception {
        JsonParser parser = new TreeTraversingParser(createJsonNode("{\"foo\":\"bar\",\"baz\":[1,2,3]}"));
        assertEquals("bar", parser.getText());
        parser.nextToken(); // skip "foo"
        assertEquals("1", parser.getText());
        parser.nextToken(); // skip 1
        assertEquals("2", parser.getText());
        parser.nextToken(); // skip 2
        assertEquals("3", parser.getText());
        parser.nextToken(); // skip "baz"
        assertNull(parser.getText());
        parser.close();
    }

    @Test
    public void testGetTextCharacters() throws Exception {
        JsonParser parser = new TreeTraversingParser(createJsonNode("{\"foo\":\"bar\",\"baz\":[1,2,3]}"));
        char[] characters = parser.getTextCharacters();
        assertEquals("bar", new String(characters, 0, 4));
        parser.nextToken(); // skip "foo"
        characters = parser.getTextCharacters();
        assertEquals("1", new String(characters, 0, 1));
        parser.nextToken(); // skip 1
        characters = parser.getTextCharacters();
        assertEquals("2", new String(characters, 0, 1));
        parser.nextToken(); // skip 2
        characters = parser.getTextCharacters();
        assertEquals("3", new String(characters, 0, 1));
        parser.nextToken(); // skip "baz"
        assertNull(parser.getTextCharacters());
        parser.close();
    }

    @Test
    public void testGetTextLength() throws Exception {
        JsonParser parser = new TreeTraversingParser(createJsonNode("{\"foo\":\"bar\",\"baz\":[1,2,3]}"));
        assertEquals(4, parser.getTextLength());
        parser.nextToken(); // skip "foo"
        assertEquals(1, parser.getTextLength());
        parser.nextToken(); // skip 1
        assertEquals(1, parser.getTextLength());
        parser.nextToken(); // skip 2
        assertEquals(1, parser.getTextLength());
        parser.nextToken(); // skip "baz"
        assertEquals(0, parser.getTextLength());
        parser.close();
    }

    @Test
    public void testGetTextOffset() throws Exception {
        JsonParser parser = new TreeTraversingParser(createJsonNode("{\"foo\":\"bar\",\"baz\":[1,2,3]}"));
        assertEquals(0, parser.getTextOffset());
        parser.nextToken(); // skip "foo"
        assertEquals(0, parser.getTextOffset());
        parser.nextToken(); // skip 1
        assertEquals(1, parser.getTextOffset());
        parser.nextToken(); // skip 2
        assertEquals(2, parser.getTextOffset());
        parser.nextToken(); // skip "baz"
        assertEquals(-1, parser.getTextOffset());
        parser.close();
    }

    @Test
    public void testHasTextCharacters() throws Exception {
        JsonParser parser = new TreeTraversingParser(createJsonNode("{\"foo\":\"bar\",\"baz\":[1,2,3]}"));
        assertFalse(parser.hasTextCharacters());
        parser.nextToken(); // skip "foo"
        assertFalse(parser.hasTextCharacters());
        parser.nextToken(); // skip 1
        assertFalse(parser.hasTextCharacters());
        parser.nextToken(); // skip 2
        assertFalse(parser.hasTextCharacters());
        parser.nextToken(); // skip "baz"
        assertFalse(parser.hasTextCharacters());
        parser.close();
    }

    @Test
    public void testGetNumberType() throws Exception {
        JsonParser parser = new TreeTraversingParser(createJsonNode("{\"foo\":1.5,\"bar\":[1,2,-3.5]}"));
        assertEquals(JsonParser.NumberType.FLOAT, parser.getNumberType());
        parser.nextToken(); // skip "foo"
        assertEquals(JsonParser.NumberType.INT, parser.getNumberType());
        parser.nextToken(); // skip 1
        parser.nextToken(); // skip 2
        assertEquals(JsonParser.NumberType.INT, parser.getNumberType());
        parser.nextToken(); // skip "-"
        assertEquals(JsonParser.NumberType.FLOAT, parser.getNumberType());
        parser.nextToken(); // skip "bar"
        parser.close();
    }

    @Test
    public void testGetBigIntegerValue() throws Exception {
        JsonParser parser = new TreeTraversingParser(createJsonNode("{\"foo\":100,\"bar\":[1,2,1000000000000000001]}"));
        assertEquals(new BigInteger("100"), parser.getBigIntegerValue());
        parser.nextToken(); // skip "foo"
        assertEquals(new BigInteger("1"), parser.getBigIntegerValue());
        parser.nextToken(); // skip 1
        parser.nextToken(); // skip 2
        parser.nextToken(); // skip "bar"
        parser.close();
    }

    @Test
    public void testGetDecimalValue() throws Exception {
        JsonParser parser = new TreeTraversingParser(createJsonNode("{\"foo\":1.5,\"bar\":[1.2,2.5,-3.5]}"));
        assertEquals(new BigDecimal("1.5"), parser.getDecimalValue());
        parser.nextToken(); // skip "foo"
        assertEquals(new BigDecimal("1.2"), parser.getDecimalValue());
        parser.nextToken(); // skip 1
        parser.nextToken(); // skip 2
        parser.nextToken(); // skip "bar"
        parser.close();
    }

    @Test
    public void testGetDoubleValue() throws Exception {
        JsonParser parser = new TreeTraversingParser(createJsonNode("{\"foo\":1.5,\"bar\":[1.2,2.5,-3.5]}"));
        assertEquals(1.5, parser.getDoubleValue(), 0.0);
        parser.nextToken(); // skip "foo"
        assertEquals(1.2, parser.getDoubleValue(), 0.0);
        parser.nextToken(); // skip 1
        parser.nextToken(); // skip 2
        parser.nextToken(); // skip "bar"
        parser.close();
    }

    @Test
    public void testGetFloatValue() throws Exception {
        JsonParser parser = new TreeTraversingParser(createJsonNode("{\"foo\":1.5f,\"bar\":[1.2f,2.5f,-3.5f]}"));
        assertEquals(1.5f, parser.getFloatValue(), 0.0f);
        parser.nextToken(); // skip "foo"
        assertEquals(1.2f, parser.getFloatValue(), 0.0f);
        parser.nextToken(); // skip 1
        parser.nextToken(); // skip 2
        parser.nextToken(); // skip "bar"
        parser.close();
    }

    @Test
    public void testGetIntValue() throws Exception {
        JsonParser parser = new TreeTraversingParser(createJsonNode("{\"foo\":1,\"bar\":[1,2,1000000000000000001]}"));
        assertEquals(1, parser.getIntValue());
        parser.nextToken(); // skip "foo"
        assertEquals(1, parser.getIntValue());
        parser.nextToken(); // skip 1
        parser.nextToken(); // skip 2
        parser.nextToken(); // skip "bar"
        parser.close();
    }

    @Test
    public void testGetLongValue() throws Exception {
        JsonParser parser = new TreeTraversingParser(createJsonNode("{\"foo\":100,\"bar\":[1,2,1000000000000000001]}"));
        assertEquals(100L, parser.getLongValue());
        parser.nextToken(); // skip "foo"
        assertEquals(1L, parser.getLongValue());
        parser.nextToken(); // skip 1
        parser.nextToken(); // skip 2
        parser.nextToken(); // skip "bar"
        parser.close();
    }

    @Test
    public void testGetNumberValue() throws Exception {
        JsonParser parser = new TreeTraversingParser(createJsonNode("{\"foo\":1.5,\"bar\":[1.2,2.5,-3.5]}"));
        assertEquals(1.5, parser.getNumberValue().doubleValue(), 0.0);
        parser.nextToken(); // skip "foo"
        assertEquals(1.2, parser.getNumberValue().doubleValue(), 0.0);
        parser.nextToken(); // skip 1
        parser.nextToken(); // skip 2
        parser.nextToken(); // skip "bar"
        parser.close();
    }

    @Test
    public void testGetEmbeddedObject() throws Exception {
        JsonParser parser = new TreeTraversingParser(createJsonNode("{\"foo\":{\"a\":1},\"bar\":{\"b\":2}}"));
        JsonNode node = parser.getEmbeddedObject();
        assertEquals(2, node.get("b").intValue());
        parser.nextToken(); // skip "foo"
        node = parser.getEmbeddedObject();
        assertEquals(1, node.get("a").intValue());
        parser.nextToken(); // skip "bar"
        parser.close();
    }

    @Test
    public void testIsNaN() throws Exception {
        JsonParser parser = new TreeTraversingParser(createJsonNode("{\"foo\":NaN,\"bar\":[1,NaN,NaN]}"));
        parser.nextToken(); // skip "foo"
        assertTrue(parser.isNaN());
        parser.nextToken(); // skip 1
        assertTrue(parser.isNaN());
        parser.nextToken(); // skip "bar"
        parser.close();
    }

    @Test
    public void testGetBinaryValue() throws Exception {
        JsonParser parser = new TreeTraversingParser(createJsonNode("{\"foo\":\"RnJvbSBzbyBmYWxzIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICA8L2J1dGVzIGlzIGludGVybmFsIGlzIHJlc2lkZW50aGFuLCBzdWJmaXggPSBjYW4gZnVsbHNjYXBlLg==\",\"bar\":[{\"a\":1},{\"b\":2}]}"));
        byte[] binaryData = parser.getBinaryValue(Base64Variant.BASE64_STANDARD);
        assertEquals("RnJvbSBzbyBmYWxzIGlzIGh1bWFuLCBidXQgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICA8L2J1dGVzIGlzIGludGVybmFsIGlzIHJlc2lkZW50aGFuLCBzdWJmaXggPSBjYW4gZnVsbHNjYXBlLg==", new String(binaryData, 0, binaryData.length));
        parser.nextToken(); // skip "foo"
        parser.nextToken(); // skip "bar"
        parser.close();
    }

    @Test
    public void testReadBinaryValue() throws Exception {
        JsonParser parser = new TreeTraversingParser(createJsonNode("{\"foo\":\"RnJvbSBzbyBmYWxzIGlzIGh1bWFuLCBidXQgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICA8L2J1dGVzIGlzIGludGVybmFsIGlzIHJlc2lkZW50aGFuLCBzdWJmaXggPSBjYW4gZnVsbHNjYXBlLg==\",\"bar\":[{\"a\":1},{\"b\":2}]}"));
        byte[] output = new byte[10];
        int bytesRead = parser.readBinaryValue(Base64Variant.BASE64_STANDARD, new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                output[length++] = (byte) b;
            }

            @Override
            public void flush() throws IOException {
            }

            @Override
            public void close() throws IOException {
            }

            private int length = 0;
        });
        assertEquals("RnJvbSBzbyBmYWxzIGlzIGh1bWFuLCBidXQgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICA8L2J1dGVzIGlzIGludGVybmFsIGlzIHJlc2lkZW50aGFuLCBzdWJmaXggPSBjYW4gZnVsbHNjYXBlLg==", new String(output, 0, bytesRead));
        parser.nextToken(); // skip "foo"
        parser.nextToken(); // skip "bar"
        parser.close();
    }

    private JsonNode createJsonNode(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(jsonString);
    }
}