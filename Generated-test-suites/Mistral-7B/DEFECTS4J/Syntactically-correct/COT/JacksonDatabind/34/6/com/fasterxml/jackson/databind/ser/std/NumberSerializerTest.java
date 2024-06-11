package com.example.numberserializer; // replace with your package name

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

import org.junit.Test;

/**
 * Test class for NumberSerializer.
 */
public class NumberSerializerTest {

    @JacksonStdImpl
    static class TestNumberSerializer extends NumberSerializer {
        public TestNumberSerializer() {
            super(Number.class);
        }
    }

    @Test
    public void testSerializeInt() throws Exception {
        JsonGenerator gen = createJsonGenerator();
        NumberSerializer serializer = new TestNumberSerializer();
        int number = 123;
        serializer.serialize(number, gen, new SerializerProvider());
        JsonParser parser = createJsonParser(serializer.getSchema(new SerializerProvider(), Number.class));
        assertEquals(number, parser.getValueAsInt());
    }

    @Test
    public void testSerializeLong() throws Exception {
        JsonGenerator gen = createJsonGenerator();
        NumberSerializer serializer = new TestNumberSerializer();
        long number = 1234567890123L;
        serializer.serialize(number, gen, new SerializerProvider());
        JsonParser parser = createJsonParser(serializer.getSchema(new SerializerProvider(), Number.class));
        assertEquals(number, parser.getValueAsLong());
    }

    @Test
    public void testSerializeFloat() throws Exception {
        JsonGenerator gen = createJsonGenerator();
        NumberSerializer serializer = new TestNumberSerializer();
        float number = 123.45f;
        serializer.serialize(number, gen, new SerializerProvider());
        JsonParser parser = createJsonParser(serializer.getSchema(new SerializerProvider(), Number.class));
        assertEquals(number, parser.getValueAsFloat(), 0.001f);
    }

    @Test
    public void testSerializeDouble() throws Exception {
        JsonGenerator gen = createJsonGenerator();
        NumberSerializer serializer = new TestNumberSerializer();
        double number = 123.45;
        serializer.serialize(number, gen, new SerializerProvider());
        JsonParser parser = createJsonParser(serializer.getSchema(new SerializerProvider(), Number.class));
        assertEquals(number, parser.getValueAsDouble(), 0.001);
    }

    @Test
    public void testSerializeByte() throws Exception {
        JsonGenerator gen = createJsonGenerator();
        NumberSerializer serializer = new TestNumberSerializer();
        byte number = 127;
        serializer.serialize(number, gen, new SerializerProvider());
        JsonParser parser = createJsonParser(serializer.getSchema(new SerializerProvider(), Number.class));
        assertEquals(number, parser.getValueAsInt());
    }

    @Test
    public void testSerializeShort() throws Exception {
        JsonGenerator gen = createJsonGenerator();
        NumberSerializer serializer = new TestNumberSerializer();
        short number = 127;
        serializer.serialize(number, gen, new SerializerProvider());
        JsonParser parser = createJsonParser(serializer.getSchema(new SerializerProvider(), Number.class));
        assertEquals(number, parser.getValueAsInt());
    }

    @Test
    public void testSerializeBigDecimal() throws Exception {
        JsonGenerator gen = createJsonGenerator();
        NumberSerializer serializer = new TestNumberSerializer();
        BigDecimal number = new BigDecimal("123.45");
        serializer.serialize(number, gen, new SerializerProvider());
        JsonParser parser = createJsonParser(serializer.getSchema(new SerializerProvider(), Number.class));
        assertEquals(number.toString(), parser.getText());
    }

    @Test
    public void testSerializeBigInteger() throws Exception {
        JsonGenerator gen = createJsonGenerator();
        NumberSerializer serializer = new TestNumberSerializer();
        BigInteger number = new BigInteger("123456789012345678901234567890");
        serializer.serialize(number, gen, new SerializerProvider());
        JsonParser parser = createJsonParser(serializer.getSchema(new SerializerProvider(), Number.class));
        assertEquals(number.toString(), parser.getText());
    }

    @Test(expected = IOException.class)
    public void testSerializeString() throws Exception {
        JsonGenerator gen = createJsonGenerator();
        NumberSerializer serializer = new TestNumberSerializer();
        String number = "NotANumber";
        serializer.serialize(number, gen, new SerializerProvider());
    }

    @Test
    public void testSerializeNull() throws Exception {
        JsonGenerator gen = createJsonGenerator();
        NumberSerializer serializer = new TestNumberSerializer();
        serializer.serialize(null, gen, new SerializerProvider());
        JsonParser parser = createJsonParser(serializer.getSchema(new SerializerProvider(), Number.class));
        assertNull(parser.getValueAsInt());
        assertNull(parser.getValueAsLong());
        assertNull(parser.getValueAsFloat());
        assertNull(parser.getValueAsDouble());
        assertNull(parser.getValueAsBigDecimal());
        assertNull(parser.getValueAsBigInteger());
    }

    @Test
    public void testGetSchema() throws Exception {
        JsonSchema schema = new TestNumberSerializer().getSchema(new SerializerProvider(), Number.class);
        assertTrue(schema.isIntegralNumber());
        assertFalse(schema.isFloatingPointNumber());
    }

    // Additional test cases for edge cases and exception handling

    // ...

    private JsonGenerator createJsonGenerator() throws IOException {
        // ...
    }

    private JsonParser createJsonParser(JsonNode schema) throws IOException {
        // ...
    }
}