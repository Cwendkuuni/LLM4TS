package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser.NumberType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TextNode;

public class TreeTraversingParserTest {

    @Test
    public void testBasic() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("{\"name\":\"John\",\"age\":30,\"cities\":[\"New York\",\"Los Angeles\",\"Chicago\"]}");
        TreeTraversingParser parser = new TreeTraversingParser(node);

        assertEquals("name", parser.nextToken().getName());
        assertEquals("John", parser.nextText());

        assertEquals("age", parser.nextToken().getName());
        assertEquals(NumberType.INT, parser.nextNumberType());
        assertEquals(30, parser.nextIntValue());

        assertEquals("cities", parser.nextToken().getName());
        assertEquals(JsonToken.START_ARRAY, parser.nextToken().getVal());

        // test streaming an element
        assertEquals("New York", parser.nextText());

        // test skipping an element
        parser.skipChildren();

        // test streaming the next element
        assertEquals("Los Angeles", parser.nextText());

        // test closing the parser
        parser.close();
        assertFalse(parser.isOpen());
    }

    @Test
    public void testBasicNull() throws IOException {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        parser.close();
    }

    @Test
    public void testBasicEmpty() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("{}");
        TreeTraversingParser parser = new TreeTraversingParser(node);
        parser.close();
    }

    @Test
    public void testBasicValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("42");
        TreeTraversingParser parser = new TreeTraversingParser(node);
        assertTrue(parser.nextValue().isNumber());
        assertEquals(NumberType.INT, parser.nextNumberType());
        assertEquals(42, parser.nextIntValue());
        parser.close();
    }

    @Test
    public void testBasicValueString() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("\"Hello World\"");
        TreeTraversingParser parser = new TreeTraversingParser(node);
        assertEquals("Hello World", parser.nextText());
        parser.close();
    }

    @Test
    public void testBasicValueBoolean() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("true");
        TreeTraversingParser parser = new TreeTraversingParser(node);
        assertTrue(parser.nextValue().isBoolean());
        assertTrue(parser.nextBooleanValue());
        parser.close();
    }

    @Test
    public void testBasicValueNull() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("null");
        TreeTraversingParser parser = new TreeTraversingParser(node);
        assertNull(parser.nextValue());
        parser.close();
    }

    @Test
    public void testBasicValueNumber() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("42.123");
        TreeTraversingParser parser = new TreeTraversingParser(node);
        assertTrue(parser.nextValue().isNumber());
        assertEquals(NumberType.FLOAT, parser.nextNumberType());
        assertEquals(42.123, parser.nextFloatValue(), 0.01);
        parser.close();
    }

    @Test
    public void testBasicValueNumberInt() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("42");
        TreeTraversingParser parser = new TreeTraversingParser(node);
        assertTrue(parser.nextValue().isNumber());
        assertEquals(NumberType.INT, parser.nextNumberType());
        assertEquals(42, parser.nextIntValue());
        parser.close();
    }

    @Test
    public void testBasicValueNumberLong() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("42000000000000001");
        TreeTraversingParser parser = new TreeTraversingParser(node);
        assertTrue(parser.nextValue().isNumber());
        assertEquals(NumberType.LONG, parser.nextNumberType());
        assertEquals(42000000000000001L, parser.nextLongValue());
        parser.close();
    }

    @Test
    public void testBasicValueBigDecimal() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("42.123E-10");
        TreeTraversingParser parser = new TreeTraversingParser(node);
        assertTrue(parser.nextValue().isNumber());
        assertEquals(NumberType.DECIMAL, parser.nextNumberType());
        BigDecimal expected = new BigDecimal("42.123E-10");
        BigDecimal actual = parser.nextDecimalValue();
        assertEquals(expected, actual);
        parser.close();
    }

    @Test
    public void testBasicValueBigInteger() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("42000000000000001");
        TreeTraversingParser parser = new TreeTraversingParser(node);
        assertTrue(parser.nextValue().isNumber());
        assertEquals(NumberType.BIG_INTEGER, parser.nextNumberType());
        BigInteger expected = new BigInteger("42000000000000001");
        BigInteger actual = parser.nextBigIntegerValue();
        assertEquals(expected, actual);
        parser.close();
    }

    @Test
    public void testBasicValueBinary() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("{\"data\":[0x12,0x34,0x56]}");
        TreeTraversingParser parser = new TreeTraversingParser(node);
        byte[] binary = parser.nextBinaryValue(null);
        assertEquals(3, binary.length);
        assertEquals((byte) 0x12, binary[0]);
        assertEquals((byte) 0x34, binary[1]);
        assertEquals((byte) 0x56, binary[2]);
        parser.close();
    }

    @Test
    public void testBasicValueBinaryRead() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("{\"data\":[0x12,0x34,0x56]}");
        TreeTraversingParser parser = new TreeTraversingParser(node);
        int length = parser.readBinaryValue(null, new byte[3]);
        assertEquals(3, length);
        assertEquals((byte) 0x12, parser.getBinaryValue(new byte[3])[0]);
        assertEquals((byte) 0x34, parser.getBinaryValue(new byte[3])[1]);
        assertEquals((byte) 0x56, parser.getBinaryValue(new byte[3])[2]);
        parser.close();
    }

    @Test
    public void testBasicValueEmbeddedObject() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("{\"name\":{\"title\":\"Mr\",\"first\":\"John\",\"last\":\"Doe\"}}");
        TreeTraversingParser parser = new TreeTraversingParser(node);
        Object obj = parser.nextEmbeddedObject();
        assertEquals("Mr", obj.getClass().getMethod("title").invoke(obj));
        assertEquals("John", obj.getClass().getMethod("first").invoke(obj));
        assertEquals("Doe", obj.getClass().getMethod("last").invoke(obj));
        parser.close();
    }

    @Test
    public void testBasicValueNullEmbeddedObject() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("{null}");
        TreeTraversingParser parser = new TreeTraversingParser(node);
        assertNull(parser.nextEmbeddedObject());
        parser.close();
    }

    @Test
    public void testBasicValueBinaryEmbeddedObject() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("{\"binary\":{\"type\":\"image/jpeg\",\"data\":[0x12,0x34,0x56]}}");
        TreeTraversingParser parser = new TreeTraversingParser(node);
        byte[] binary = parser.nextEmbeddedObject().getClass().getMethod("binaryValue").invoke(parser.nextEmbeddedObject());
        assertEquals(3, binary.length);
        assertEquals((byte) 0x12, binary[0]);
        assertEquals((byte) 0x34, binary[1]);
        assertEquals((byte) 0x56, binary[2]);
        parser.close();
    }

    @Test
    public void testBasicValueNullBinaryEmbeddedObject() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("{\"binary\":null}");
        TreeTraversingParser parser = new TreeTraversingParser(node);
        assertNull(parser.nextEmbeddedObject());
        parser.close();
    }

    @Test
    public void testBasicValueTextEmbeddedObject() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("{\"text\":{\"title\":\"Hello World\"}}");
        TreeTraversingParser parser = new TreeTraversingParser(node);
        TextNode textNode = parser.nextEmbeddedObject().getClass().getMethod("asText").invoke(parser.nextEmbeddedObject());
        assertEquals("Hello World", textNode.getText());
        parser.close();
    }

    @Test
    public void testBasicValueArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("[1,2,3]");
        TreeTraversingParser parser = new TreeTraversingParser(node);
        assertEquals(JsonToken.START_ARRAY, parser.nextToken().getVal());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken().getVal());
        assertEquals(1, parser.nextIntValue());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken().getVal());
        assertEquals(2, parser.nextIntValue());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken().getVal());
        assertEquals(3, parser.nextIntValue());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken().getVal());
        parser.close();
    }

    @Test
    public void testBasicValueObject() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("{\"name\":\"John\",\"age\":30,\"cities\":[\"New York\",\"Los Angeles\",\"Chicago\"]}");
        TreeTraversingParser parser = new TreeTraversingParser(node);
        assertEquals("name", parser.nextToken().getName());
        assertEquals("John", parser.nextText());
        assertEquals("age", parser.nextToken().getName());
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken().getVal());
        assertEquals(30, parser.nextIntValue());
        assertEquals("cities", parser.nextToken().getName());
        assertEquals(JsonToken.START_ARRAY, parser.nextToken().getVal());
        assertEquals("New York", parser.nextText());
        parser.skipChildren();
        assertEquals("Los Angeles", parser.nextText());
        parser.skipChildren();
        assertEquals("Chicago", parser.nextText());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken().getVal());
        parser.close();
    }

    @Test
    public void testBasicValueNullObject() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("{}");
        TreeTraversingParser parser = new TreeTraversingParser(node);
        assertEquals(JsonToken.END_OBJECT, parser.nextToken().getVal());
        parser.close();
    }
}