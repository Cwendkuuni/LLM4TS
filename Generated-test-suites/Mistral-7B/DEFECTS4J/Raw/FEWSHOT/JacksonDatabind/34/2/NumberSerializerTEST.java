package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.node.BigIntegerNode;
import com.fasterxml.jackson.databind.node.BigDecimalNode;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

public class NumberSerializerTest {

    @Test
    public void testSerializeInt() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_NUMBERS_AS_STRINGS, false);
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        SerializerProvider provider = mapper.getSerializerProvider();

        Number number = 123;
        serializer.serialize(number, gen, provider);
        gen.flush();
        assertEquals("123", gen.getOutputBuffer().asString());
    }

    @Test
    public void testSerializeLong() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_NUMBERS_AS_STRINGS, false);
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        SerializerProvider provider = mapper.getSerializerProvider();

        Number number = 456L;
        serializer.serialize(number, gen, provider);
        gen.flush();
        assertEquals("456", gen.getOutputBuffer().asString());
    }

    @Test
    public void testSerializeFloat() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_NUMBERS_AS_STRINGS, false);
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        SerializerProvider provider = mapper.getSerializerProvider();

        Number number = 123.45f;
        serializer.serialize(number, gen, provider);
        gen.flush();
        assertEquals("123.45", gen.getOutputBuffer().asString());
    }

    @Test
    public void testSerializeDouble() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_NUMBERS_AS_STRINGS, false);
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        SerializerProvider provider = mapper.getSerializerProvider();

        Number number = 123.45;
        serializer.serialize(number, gen, provider);
        gen.flush();
        assertEquals("123.45", gen.getOutputBuffer().asString());
    }

    @Test
    public void testSerializeByte() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_NUMBERS_AS_STRINGS, false);
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        SerializerProvider provider = mapper.getSerializerProvider();

        Number number = (byte) 127;
        serializer.serialize(number, gen, provider);
        gen.flush();
        assertEquals("127", gen.getOutputBuffer().asString());
    }

    @Test
    public void testSerializeShort() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_NUMBERS_AS_STRINGS, false);
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        SerializerProvider provider = mapper.getSerializerProvider();

        Number number = (short) 32767;
        serializer.serialize(number, gen, provider);
        gen.flush();
        assertEquals("32767", gen.getOutputBuffer().asString());
    }

    @Test
    public void testSerializeBigInteger() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_NUMBERS_AS_STRINGS, false);
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        SerializerProvider provider = mapper.getSerializerProvider();

        Number number = BigInteger.valueOf(1234567890123456789L);
        serializer.serialize(number, gen, provider);
        gen.flush();
        assertEquals("1234567890123456789", gen.getOutputBuffer().asString());
    }

    @Test
    public void testSerializeBigDecimal() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_NUMBERS_AS_STRINGS, false);
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        SerializerProvider provider = mapper.getSerializerProvider();

        Number number = new BigDecimal("123.456");
        serializer.serialize(number, gen, provider);
        gen.flush();
        assertEquals("123.456", gen.getOutputBuffer().asString());
    }

    @Test
    public void testSerializeUnknownNumberType() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_NUMBERS_AS_STRINGS, false);
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        SerializerProvider provider = mapper.getSerializerProvider();

        Number number = new Object() {
        };
        serializer.serialize(number, gen, provider);
        gen.flush();
        assertEquals("null", gen.getOutputBuffer().asString());
    }

    @Test
    public void testGetSchema() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;

        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider provider = mapper.getSerializerProvider();
        TypeSerializer typeSerializer = provider.findValueSerializer(BigDecimal.class, null);

        JsonNode schema = serializer.getSchema(provider, BigDecimal.class);
        assertEquals("number", schema.asText());
        JsonNode additionalPropertiesSchema = schema.get("additionalProperties");
        assertNotNull(additionalPropertiesSchema);
        assertEquals("number", additionalPropertiesSchema.get("$ref").asText());

        JsonNode intSchema = serializer.getSchema(provider, Integer.class);
        assertEquals("integer", intSchema.asText());
    }

    @Test
    public void testAcceptJsonFormatVisitorForInteger() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;

        ObjectMapper mapper = new ObjectMapper();
        JsonFormatVisitorWrapper visitor = mapper.getSerializerProvider().findFormatVisitor(Object.class, null);

        serializer.acceptJsonFormatVisitor(visitor, BigInteger.TYPE);
        visitor.expectNumberFormat(BigInteger.TYPE);
        visitor.expectNumberFormat(JsonParser.NumberType.BIG_INTEGER);
        visitor.visitIntFormat(null, null, JsonParser.NumberType.BIG_INTEGER);
    }

    @Test
    public void testAcceptJsonFormatVisitorForBigDecimal() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;

        ObjectMapper mapper = new ObjectMapper();
        JsonFormatVisitorWrapper visitor = mapper.getSerializerProvider().findFormatVisitor(Object.class, null);

        serializer.acceptJsonFormatVisitor(visitor, BigDecimal.class);
        visitor.expectNumberFormat(BigDecimal.class);
        visitor.expectNumberFormat(JsonParser.NumberType.BIG_INTEGER);
        visitor.visitFloatFormat(null, null, JsonParser.NumberType.BIG_INTEGER);
    }

    @Test(expected = JsonMappingException.class)
    public void testAcceptJsonFormatVisitorForUnknownType() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;

        ObjectMapper mapper = new ObjectMapper();
        JsonFormatVisitorWrapper visitor = mapper.getSerializerProvider().findFormatVisitor(Object.class, null);

        serializer.acceptJsonFormatVisitor(visitor, String.class);
    }
}