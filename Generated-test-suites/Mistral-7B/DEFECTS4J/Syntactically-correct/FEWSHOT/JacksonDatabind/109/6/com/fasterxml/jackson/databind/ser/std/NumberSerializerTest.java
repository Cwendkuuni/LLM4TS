package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.BigInteger;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

/**
 * Test cases for NumberSerializer class
 */
@JacksonStdImpl
@SuppressWarnings("serial")
public class NumberSerializerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testSerializeBigDecimal() throws IOException {
        JsonGenerator gen = mapper.getPolymorphicSerializer(NumberSerializer.class).getSerializerProvider().getGenerator();
        BigDecimal bd = new BigDecimal("123.45");
        NumberSerializer numberSerializer = (NumberSerializer) mapper.getPolymorphicSerializer(NumberSerializer.class);

        numberSerializer.serialize(bd, gen, mapper.getSerializerProvider());

        assertEquals("-123.45", gen.getOutputBuffer().toString());
    }

    @Test
    public void testSerializeBigInteger() throws IOException {
        JsonGenerator gen = mapper.getPolymorphicSerializer(NumberSerializer.class).getSerializerProvider().getGenerator();
        BigInteger bi = new BigInteger("12345678901234567890");
        NumberSerializer numberSerializer = (NumberSerializer) mapper.getPolymorphicSerializer(NumberSerializer.class);

        numberSerializer.serialize(bi, gen, mapper.getSerializerProvider());

        assertEquals("12345678901234567890", gen.getOutputBuffer().toString());
    }

    @Test
    public void testSerializeLong() throws IOException {
        JsonGenerator gen = mapper.getPolymorphicSerializer(NumberSerializer.class).getSerializerProvider().getGenerator();
        long l = 1234567890123456789L;
        NumberSerializer numberSerializer = (NumberSerializer) mapper.getPolymorphicSerializer(NumberSerializer.class);

        numberSerializer.serialize(l, gen, mapper.getSerializerProvider());

        assertEquals("1234567890123456789", gen.getOutputBuffer().toString());
    }

    @Test
    public void testSerializeDouble() throws IOException {
        JsonGenerator gen = mapper.getPolymorphicSerializer(NumberSerializer.class).getSerializerProvider().getGenerator();
        double d = 1234567890123456789.0;
        NumberSerializer numberSerializer = (NumberSerializer) mapper.getPolymorphicSerializer(NumberSerializer.class);

        numberSerializer.serialize(d, gen, mapper.getSerializerProvider());

        assertEquals("1.234567890123456789e+11", gen.getOutputBuffer().toString().replaceAll("\\s", ""));
    }

    @Test
    public void testSerializeFloat() throws IOException {
        JsonGenerator gen = mapper.getPolymorphicSerializer(NumberSerializer.class).getSerializerProvider().getGenerator();
        float f = 1234567890f;
        NumberSerializer numberSerializer = (NumberSerializer) mapper.getPolymorphicSerializer(NumberSerializer.class);

        numberSerializer.serialize(f, gen, mapper.getSerializerProvider());

        assertEquals("1.2345679e+08", gen.getOutputBuffer().toString().replaceAll("\\s", ""));
    }

    @Test
    public void testSerializeInteger() throws IOException {
        JsonGenerator gen = mapper.getPolymorphicSerializer(NumberSerializer.class).getSerializerProvider().getGenerator();
        int i = 123;
        NumberSerializer numberSerializer = (NumberSerializer) mapper.getPolymorphicSerializer(NumberSerializer.class);

        numberSerializer.serialize(i, gen, mapper.getSerializerProvider());

        assertEquals("123", gen.getOutputBuffer().toString());
    }

    @Test
    public void testSerializeByte() throws IOException {
        JsonGenerator gen = mapper.getPolymorphicSerializer(NumberSerializer.class).getSerializerProvider().getGenerator();
        byte b = 127;
        NumberSerializer numberSerializer = (NumberSerializer) mapper.getPolymorphicSerializer(NumberSerializer.class);

        numberSerializer.serialize(b, gen, mapper.getSerializerProvider());

        assertEquals("127", gen.getOutputBuffer().toString());
    }

    @Test
    public void testSerializeShort() throws IOException {
        JsonGenerator gen = mapper.getPolymorphicSerializer(NumberSerializer.class).getSerializerProvider().getGenerator();
        short s = 123;
        NumberSerializer numberSerializer = (NumberSerializer) mapper.getPolymorphicSerializer(NumberSerializer.class);

        numberSerializer.serialize(s, gen, mapper.getSerializerProvider());

        assertEquals("123", gen.getOutputBuffer().toString());
    }

    @Test
    public void testJsonFormatVisitorWrapper() throws IOException {
        JsonFormatVisitorWrapper visitor = mapper.getSerializationConfig().getFactory().createJsonFormatVisitor(JsonParser.Feature.FAIL_ON_EMPTY_BEANS, null);
        NumberSerializer numberSerializer = (NumberSerializer) mapper.getPolymorphicSerializer(NumberSerializer.class);

        numberSerializer.acceptJsonFormatVisitor(visitor, null);

        assertEquals("number", visitor.getShapeAsString());
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        SerializerProvider serializerProvider = mapper.getSerializerProvider();
        BeanProperty property = new BeanProperty("testProperty", Number.class, null);
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Value.Shape.STRING);

        JsonSerializer<?> jsonSerializer = numberSerializer.createContextual(serializerProvider, property);

        assertEquals(ToStringSerializer.instance, jsonSerializer);
    }

    @Test
    public void testSerializeWithJsonFormat() throws JsonMappingException, IOException {
        JsonGenerator gen = mapper.getFactory().createGenerator();

        ObjectNode node = mapper.createObjectNode();
        node.put("testProperty", new BigDecimal("123.45"));

        mapper.writeValue(gen, node);

        assertEquals("-123.45", gen.getOutputBuffer().toString());
    }
}