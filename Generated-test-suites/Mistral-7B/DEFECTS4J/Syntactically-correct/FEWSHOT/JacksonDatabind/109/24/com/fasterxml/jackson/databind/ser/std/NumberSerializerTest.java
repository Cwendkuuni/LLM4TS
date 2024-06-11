package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import org.junit.Test;

@SuppressWarnings("unchecked")
public class NumberSerializerTest {

    @Test
    public void testNumberSerializerInstance() {
        assertEquals(NumberSerializer.class, NumberSerializer.instance.getClass());
    }

    @Test
    public void testSerializeInteger() throws IOException {
        JsonGenerator generator = createJsonGenerator();
        NumberSerializer serializer = new NumberSerializer(Integer.class);
        serializer.serialize(1, generator, new SerializerProvider());
        assertEquals("1", generator.getOutputContent().toString());
    }

    @Test
    public void testSerializeBigInteger() throws IOException {
        JsonGenerator generator = createJsonGenerator();
        NumberSerializer serializer = new NumberSerializer(BigInteger.class);
        serializer.serialize(new BigInteger("123456789012345678901234567890"), generator, new SerializerProvider());
        assertEquals("123456789012345678901234567890", generator.getOutputContent().toString());
    }

    @Test
    public void testSerializeBigDecimal() throws IOException {
        JsonGenerator generator = createJsonGenerator();
        NumberSerializer serializer = new NumberSerializer(BigDecimal.class);
        serializer.serialize(new BigDecimal("123.45"), generator, new SerializerProvider());
        assertEquals("123.45", generator.getOutputContent().toString());
    }

    @Test
    public void testSerializeLong() throws IOException {
        JsonGenerator generator = createJsonGenerator();
        Number serializer = new NumberSerializer(Long.class);
        serializer.serialize(1L, generator, new SerializerProvider());
        assertEquals("1", generator.getOutputContent().toString());
    }

    @Test
    public void testSerializeDouble() throws IOException {
        JsonGenerator generator = createJsonGenerator();
        NumberSerializer serializer = new NumberSerializer(Double.class);
        serializer.serialize(1.1, generator, new SerializerProvider());
        assertEquals("1.1", generator.getOutputContent().toString());
    }

    @Test
    public void testSerializeFloat() throws IOException {
        JsonGenerator generator = createJsonGenerator();
        NumberSerializer serializer = new NumberSerializer(Float.class);
        serializer.serialize(1.1f, generator, new SerializerProvider());
        assertEquals("1.1", generator.getOutputContent().toString());
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        ObjectMapper objectMapper = new ObjectMapper();
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonSerializer<Number> jsonSerializer = serializer.createContextual(objectMapper.getSerializerProvider(), null);
        assertEquals(serializer.getClass(), jsonSerializer.getClass());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonFormatVisitorWrapper visitor = objectMapper.schemaFactory().getSchemaBuilder().findSchemaNode("number")
                .acceptJsonFormatVisitor(objectMapper.getSerializerProvider(), Number.class);
        NumberSerializer serializer = new NumberSerializer(Number.class);
        serializer.acceptJsonFormatVisitor(visitor, Number.class);
        assertTrue(visitor.hasHandledNumberFormat());
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode schema = new NumberSerializer(Number.class).getSchema(objectMapper.getSerializerProvider(), Number.class);
        assertEquals("number", schema.asText());
    }

    private JsonGenerator createJsonGenerator() throws IOException {
        return new JsonFactory().createGenerator(new StringWriter());
    }
}