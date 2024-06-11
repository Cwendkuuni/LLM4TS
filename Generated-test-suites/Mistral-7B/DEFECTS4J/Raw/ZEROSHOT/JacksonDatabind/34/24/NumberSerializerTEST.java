package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser.NumberType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapperBuilder;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

/**
 * Test for NumberSerializer class.
 */
public class NumberSerializerTest {

    private ObjectMapper mapper;

    public NumberSerializerTest() {
        mapper = JsonMapperBuilder.jsonMapper()
                .enable(SerializationFeature.FAIL_ON_EMPTY_BEANS).build();
    }

    @Test
    public void testSerializeBigDecimal() throws JsonGenerationException {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);

        Number number = new BigDecimal("123.456");
        serializer.serialize(number, gen, null);
        gen.flush();

        String serialized = new String(gen.getOutputBuffer().toString());
        assertEquals("123.456", serialized);
    }

    @Test
    public void testSerializeBigInteger() throws JsonGenerationException {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);

        Number number = new BigInteger("789123456789123456789123456789123456789123456789123456789123456");
        serializer.serialize(number, gen, null);
        gen.flush();

        String serialized = new String(gen.getOutputBuffer().toString());
        assertEquals("789123456789123456789123456789123456789123456789123456789123456", serialized);
    }

    @Test
    public void testSerializeInteger() throws JsonGenerationException {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);

        Number number = 123;
        serializer.serialize(number, gen, null);
        gen.flush();

        String serialized = new String(gen.getOutputBuffer().toString());
        assertEquals("123", serialized);
    }

    @Test
    public void testSerializeLong() throws JsonGenerationException {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);

        Number number = 123456789123456789L;
        serializer.serialize(number, gen, null);
        gen.flush();

        String serialized = new String(gen.getOutputBuffer().toString());
        assertEquals("123456789123456789", serialized);
    }

    @Test
    public void testSerializeDouble() throws JsonGenerationException {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);

        Number number = 123.456;
        serializer.serialize(number, gen, null);
        gen.flush();

        String serialized = new String(gen.getOutputBuffer().toString());
        assertEquals("123.456", serialized);
    }

    @Test
    public void testSerializeFloat() throws JsonGenerationException {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);

        Number number = 123.456f;
        serializer.serialize(number, gen, null);
        gen.flush();

        String serialized = new String(gen.getOutputBuffer().toString());
        assertEquals("123.456", serialized);
    }

    @Test
    public void testSerializeByte() throws JsonGenerationException {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);

        Number number = (byte) 123;
        serializer.serialize(number, gen, null);
        gen.flush();

        String serialized = new String(gen.getOutputBuffer().toString());
        assertEquals("123", serialized);
    }

    @Test
    public void testSerializeShort() throws JsonGenerationException {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);

        Number number = (short) 123;
        serializer.serialize(number, gen, null);
        gen.flush();

        String serialized = new String(gen.getOutputBuffer().toString());
        assertEquals("123", serialized);
    }

    @Test
    public void testSerializeUnknownNumberType() throws JsonGenerationException {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);

        Number number = new Object() {
            public double doubleValue() {
                return 123.456;
            }

            public float floatValue() {
                return 123.456f;
            }

            public long longValue() {
                return 123L;
            }

            public int intValue() {
                return 123;
            }

            public byte byteValue() {
                return (byte) 123;
            }

            public short shortValue() {
                return (short) 123;
            }

            public BigDecimal bigDecimalValue() {
                return new BigDecimal("123.456");
            }

            public BigInteger bigIntegerValue() {
                return new BigInteger("123456789123456789");
            }
        };

        serializer.serialize(number, gen, null);
        gen.flush();

        String serialized = new String(gen.getOutputBuffer().toString());
        TextNode textNode = (TextNode) JsonNodeFactory.instance.textNode("123.456");
        assertEquals(textNode.asText(), serialized);
    }

    @Test
    public void testGetSchema() {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonNode schema = serializer.getSchema(mapper.getSerializerProvider(), Object.class);
        assertEquals("number", schema.asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonFormatVisitorWrapper visitor = null; // this should not be reached

        serializer.acceptJsonFormatVisitor(visitor, Object.class);
    }
}