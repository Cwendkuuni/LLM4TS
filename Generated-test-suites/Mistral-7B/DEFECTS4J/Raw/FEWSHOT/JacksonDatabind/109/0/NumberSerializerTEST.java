package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("unused")
public class NumberSerializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testSerializeInteger() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
        Number number = 123;
        serializer.serialize(number, gen, null);
        gen.flush();
        assertEquals("123", gen.getOutputContent().toString());
    }

    @Test
    public void testSerializeDouble() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
        Number number = 123.45;
        serializer.serialize(number, gen, null);
        gen.flush();
        assertEquals("123.45", gen.getOutputContent().toString());
    }

    @Test
    public void testSerializeFloat() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
        Number number = 123.45f;
        serializer.serialize(number, gen, null);
        gen.flush();
        assertEquals("123.45", gen.getOutputContent().toString());
    }

    @Test
    public void testSerializeBigInteger() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
        Number number = new BigInteger("123456789012345678901234567890");
        serializer.serialize(number, gen, null);
        gen.flush();
        assertEquals("123456789012345678901234567890", gen.getOutputContent().toString());
    }

    @Test
    public void testSerializeBigDecimal() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
        Number number = new BigDecimal("123.456789012345678901234567890");
        serializer.serialize(number, gen, null);
        gen.flush();
        assertEquals("123.456789012345678901234567890", gen.getOutputContent().toString());
    }

    @Test
    public void testSerializeLong() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
        Number number = 123L;
        serializer.serialize(number, gen, null);
        gen.flush();
        assertEquals("123", gen.getOutputContent().toString());
    }

    @Test
    public void testSerializeUnsupportedNumberType() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
        Number number = new Object();
        serializer.serialize(number, gen, null);
        gen.flush();
        assertEquals("\"[object Object]\"", gen.getOutputContent().toString());
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        JsonSerializer<Number> serializer = NumberSerializer.instance.createContextual(objectMapper.getSerializerProvider(), null);
        assertNotNull(serializer);
    }

    @Test
    public void testToStringSerializerForBigDecimal() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        JsonSerializer<Number> serializer = NumberSerializer.instance;
        serializer = mapper.getSerializerProvider().findValueSerializer(BigDecimal.class, null);
        assertNotNull(serializer);
        assertEquals(ToStringSerializer.instance, serializer);
    }

    @Test
    public void testGetSchema() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonNode schema = serializer.getSchema(objectMapper.getSerializerProvider(), Number.class);
        ObjectNode objectNode = (ObjectNode) schema;
        assertEquals("number", objectNode.get("type").asText());
        assertTrue(objectNode.get("type").has("type"));
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonFormatVisitorWrapper visitor = objectMapper.getSerializerProvider().getJsonFormatVisitor(null, Number.class);
        serializer.acceptJsonFormatVisitor(visitor, Number.class);
        assertTrue(visitor.hasEnumValueShape());
        assertTrue(visitor.hasStructuredMapSerializers());
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @JsonSerialize(using = NumberSerializer.class)
    static class TestNumber {
        private Number number;

        public Number getNumber() {
            return number;
        }

        public void setNumber(Number number) {
            this.number = number;
        }
    }

    @Test
    public void testSerializeContextual() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TestNumber testNumber = new TestNumber();
        testNumber.setNumber(new BigDecimal("123.45"));
        String json = mapper.writeValueAsString(testNumber);
        assertEquals("\"123.45\"", json);
    }
}