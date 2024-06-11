package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNumberNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Random;

import static com.fasterxml.jackson.databind.SerializationFeature.*;
import static org.junit.Assert.*;

/**
 * Tests for {@link NumberSerializer}.
 */
public class NumberSerializerTest {

    private ObjectMapper mapper;

    @Before
    public void setUp() {
        mapper = new ObjectMapper();
        mapper.configure(WRITE_NUMBERS_AS_STRINGS, false);
    }

    @Test
    public void testSerializeBigDecimal() throws JsonGenerationException, IOException {
        JsonGenerator generator = mapper.getFactory().createGenerator(new StringWriter(), null);
        NumberSerializer serializer = new NumberSerializer(BigDecimal.class);
        BigDecimal value = new BigDecimal("123.45");
        serializer.serialize(value, generator, new SerializerProvider());
        JsonNumberNode expected = JsonNumberNode.valueOf("123.45");
        JsonNode actual = generator.getOutputContent();
        assertEquals(expected, actual);
    }

    @Test
    public void testSerializeBigInteger() throws JsonGenerationException, IOException {
        JsonGenerator generator = mapper.getFactory().createGenerator(new StringWriter(), null);
        NumberSerializer serializer = new NumberSerializer(BigInteger.class);
        BigInteger value = new BigInteger("12345678901234567890");
        serializer.serialize(value, generator, new SerializerProvider());
        JsonNumberNode expected = JsonNumberNode.valueOf("12345678901234567890");
        JsonNode actual = generator.getOutputContent();
        assertEquals(expected, actual);
    }

    @Test
    public void testSerializeLong() throws JsonGenerationException, IOException {
        JsonGenerator generator = mapper.getFactory().createGenerator(new StringWriter(), null);
        NumberSerializer serializer = NumberSerializer.instance;
        long value = 1234567890123456789L;
        serializer.serialize(value, generator, new SerializerProvider());
        JsonNumberNode expected = JsonNumberNode.valueOf(value);
        JsonNode actual = generator.getOutputContent();
        assertEquals(expected, actual);
    }

    @Test
    public void testSerializeDouble() throws JsonGenerationException, IOException {
        JsonGenerator generator = mapper.getFactory().createGenerator(new StringWriter(), null);
        NumberSerializer serializer = NumberSerializer.instance;
        double value = 123.45;
        serializer.serialize(value, generator, new SerializerProvider());
        JsonNumberNode expected = JsonNumberNode.valueOf(value);
        JsonNode actual = generator.getOutputContent();
        assertEquals(expected, actual);
    }

    @Test
    public void testSerializeString() throws JsonGenerationException, IOException {
        JsonGenerator generator = mapper.getFactory().createGenerator(new StringWriter(), null);
        NumberSerializer serializer = NumberSerializer.instance;
        String value = "123.45";
        serializer.serialize(value, generator, new SerializerProvider());
        TextNode expected = JsonNodeFactory.instance.textNode("123.45");
        JsonNode actual = generator.getOutputContent();
        assertEquals(expected, actual);
    }

    @Test(expected = JsonProcessingException.class)
    public void testSerializeInvalidNumber() throws JsonGenerationException, IOException {
        JsonGenerator generator = mapper.getFactory().createGenerator(new StringWriter(), null);
        NumberSerializer serializer = NumberSerializer.instance;
        String value = "abcd";
        serializer.serialize(value, generator, new SerializerProvider());
    }

    @Test
    public void testDeserializeBigDecimal() throws JsonGenerationException, IOException {
        String json = "{\"n\":123.45}";
        ObjectReader reader = mapper.readValue(new StringReader(json), Number.class);
        JsonNode node = reader.getValueAsTree();
        JsonNumberNode numberNode = (JsonNumberNode) node.get("n");
        BigDecimal expected = new BigDecimal("123.45");
        Number deserialized = numberNode.numberValue();
        assertEquals(expected, deserialized);
    }

    @Test
    public void testDeserializeBigInteger() throws JsonGenerationException, IOException {
        String json = "{\"n\":12345678901234567890}";
        ObjectReader reader = mapper.readValue(new StringReader(json), Number.class);
        JsonNode node = reader.getValueAsTree();
        JsonNumberNode numberNode = (JsonNumberNode) node.get("n");
        BigInteger expected = new BigInteger("12345678901234567890");
        Number deserialized = numberNode.numberValue();
        assertEquals(expected, deserialized);
    }

    @Test
    public void testDeserializeLong() throws JsonGenerationException, IOException {
        String json = "{\"n\":1234567890123456789L}";
        ObjectReader reader = mapper.readValue(new StringReader(json), Number.class);
        JsonNode node = reader.getValueAsTree();
        JsonNumberNode numberNode = (JsonNumberNode) node.get("n");
        long expected = 1234567890123456789L;
        Number deserialized = numberNode.numberValue();
        assertEquals(expected, deserialized.longValue());
    }

    @Test
    public void testDeserializeDouble() throws JsonGenerationException, IOException {
        String json = "{\"n\":123.45}";
        ObjectReader reader = mapper.readValue(new StringReader(json), Number.class);
        JsonNode node = reader.getValueAsTree();
        JsonNumberNode numberNode = (JsonNumberNode) node.get("n");
        double expected = 123.45;
        Number deserialized = numberNode.numberValue();
        assertEquals(expected, deserialized.doubleValue(), 0.001);
    }

    @Test(expected = JsonProcessingException.class)
    public void testDeserializeInvalidNumber() throws JsonGenerationException, IOException {
        String json = "{\"n\":\"abcd\"}";
        ObjectReader reader = mapper.readValue(new StringReader(json), Number.class);
        reader.getValueAsTree();
    }
}