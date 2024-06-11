package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

import org.junit.Test;

@SuppressWarnings("unchecked")
public class NumberSerializerTest {

    private static final NumberSerializer INSTANCE = new NumberSerializer(Number.class);

    @Test
    public void testSerializeInteger() throws Exception {
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        Number num = 123;
        INSTANCE.serialize(num, gen, null);
        assertEquals("123", gen.getOutputText());
        gen.close();
    }

    @Test
    public void testSerializeLong() throws Exception {
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        Number num = 123L;
        INSTANCE.serialize(num, gen, null);
        assertEquals("123", gen.getOutputText());
        gen.close();
    }

    @Test
    public void testSerializeDouble() throws Exception {
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        Number num = 123.45;
        INSTANCE.serialize(num, gen, null);
        assertEquals("123.45", gen.getOutputText());
        gen.close();
    }

    @Test
    public void testSerializeFloat() throws Exception {
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        Number num = 123.45f;
        INSTANCE.serialize(num, gen, null);
        assertEquals("123.45", gen.getOutputText());
        gen.close();
    }

    @Test
    public void testSerializeByte() throws Exception {
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        Number num = (byte) 123;
        INSTANCE.serialize(num, gen, null);
        assertEquals("123", gen.getOutputText());
        gen.close();
    }

    @Test
    public void testSerializeShort() throws Exception {
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        Number num = (short) 123;
        INSTANCE.serialize(num, gen, null);
        assertEquals("123", gen.getOutputText());
        gen.close();
    }

    @Test
    public void testSerializeBigInteger() throws Exception {
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        Number num = new BigInteger("12345678901234567890");
        INSTANCE.serialize(num, gen, null);
        assertEquals("12345678901234567890", gen.getOutputText());
        gen.close();
    }

    @Test
    public void testSerializeBigDecimal() throws Exception {
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        Number num = new BigDecimal("123.456789");
        INSTANCE.serialize(num, gen, null);
        assertEquals("123.456789", gen.getOutputText());
        gen.close();
    }

    @Test
    public void testSerializeString() throws Exception {
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        Number num = new Integer("123");
        INSTANCE.serialize(num, gen, null);
        assertEquals("123", gen.getOutputText());
        gen.close();
    }

    @Test
    public void testGetSchema() throws Exception {
        SerializerProvider provider = new StandardSerializerProvider();
        JsonNode schema = INSTANCE.getSchema(provider, Number.class);
        assertEquals("number", schema.asText());
        JsonNode schemaForInt = INSTANCE.getSchema(provider, BigInteger.class);
        assertEquals("integer", schemaForInt.asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws Exception {
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(new StandardSerializerProvider());
        INSTANCE.acceptJsonFormatVisitor(visitor, Number.class);
        assertTrue(visitor.isAccepted(JsonParser.NumberType.NUMBER));
        assertFalse(visitor.isAccepted(JsonParser.NumberType.INT));
        INSTANCE.acceptJsonFormatVisitor(visitor, BigInteger.class);
        assertTrue(visitor.isAccepted(JsonParser.NumberType.INT));
    }
}