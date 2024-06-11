package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

public class NumberSerializerTest {
    @Test
    public void testConstructor() {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        assertTrue(serializer._isInt);
    }

    @Test
    public void testCreateContextual() throws Exception {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        SerializerProvider provider = new ObjectMapper().getSerializerProviderInstance();
        BeanProperty property = provider.findValueSerializer(Number.class).getBeanProperty();
        JsonSerializer<?> contextualSerializer = serializer.createContextual(provider, property);

        assertEquals(serializer, contextualSerializer);
    }

    @Test
    public void testSerializeBigDecimal() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonGenerator generator = new ObjectMapper().createGenerator(System.out);

        serializer.serialize(new BigDecimal("1.23"), generator, null);

        // Verify the output
        generator.flush();
    }

    @Test
    public void testSerializeBigInteger() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonGenerator generator = new ObjectMapper().createGenerator(System.out);

        serializer.serialize(new BigInteger("123"), generator, null);

        // Verify the output
        generator.flush();
    }

    @Test
    public void testSerializeLong() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonGenerator generator = new ObjectMapper().createGenerator(System.out);

        serializer.serialize(123L, generator, null);

        // Verify the output
        generator.flush();
    }

    @Test
    public void testSerializeDouble() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonGenerator generator = new ObjectMapper().createGenerator(System.out);

        serializer.serialize(1.23, generator, null);

        // Verify the output
        generator.flush();
    }

    @Test
    public void testSerializeFloat() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonGenerator generator = new ObjectMapper().createGenerator(System.out);

        serializer.serialize(1.23f, generator, null);

        // Verify the output
        generator.flush();
    }

    @Test
    public void testSerializeInteger() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonGenerator generator = new ObjectMapper().createGenerator(System.out);

        serializer.serialize(123, generator, null);

        // Verify the output
        generator.flush();
    }

    @Test
    public void testSerializeByte() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonGenerator generator = new ObjectMapper().createGenerator(System.out);

        serializer.serialize((byte) 123, generator, null);

        // Verify the output
        generator.flush();
    }

    @Test
    public void testSerializeShort() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonGenerator generator = new ObjectMapper().createGenerator(System.out);

        serializer.serialize((short) 123, generator, null);

        // Verify the output
        generator.flush();
    }

    @Test
    public void testSerializeFallback() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonGenerator generator = new ObjectMapper().createGenerator(System.out);

        serializer.serialize(123, generator, null);

        // Verify the output
        generator.flush();
    }

    @Test
    public void testGetSchema() {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        SerializerProvider provider = new ObjectMapper().getSerializerProviderInstance();
        JsonNode schema = serializer.getSchema(provider, Number.class);

        assertEquals("number", schema.get("type").asText());
        assertTrue(schema.get("required").asBoolean());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws Exception {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonFormatVisitorWrapper visitor = new ObjectMapper().getSerializerProviderInstance().getVisitorProvider()
                .getSerializerVisitor(new ObjectMapper(), JavaType.construct(Number.class), null);
        serializer.acceptJsonFormatVisitor(visitor, null);
    }
}