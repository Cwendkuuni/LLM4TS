package com.yourpackage.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanProperty;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Test cases for NumberSerializer class.
 */
public class NumberSerializerTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
    }

    // Test case for constructor with Number argument
    @Test
    public void testNumberSerializerInstance() {
        NumberSerializer serializer = NumberSerializer.instance;
        assertNotNull(serializer);
    }

    // Test case for createContextual method with Integer property
    @Test
    public void testSerializeInteger() throws JsonProcessingException {
        JsonGenerator generator = mapper.getFactory().createGenerator(new java.io.File("output.json"));
        ObjectWriter writer = mapper.writerWithView(MyView.class);

        JsonSerializer<Number> serializer = mapper.findValueSerializer(Integer.class);
        Number number = 123;

        serializer.serialize(number, generator, mapper.getSerializerProvider());
        generator.close();

        JsonParser parser = mapper.getFactory().createParser(new java.io.File("output.json"));
        JsonNode node = mapper.readTree(parser);
        JsonNode expectedNode = JsonNodeFactory.instance.numberNode(123);

        assertEquals(expectedNode, node);
    }

    // Test case for serialize method with BigDecimal argument
    @Test
    public void testSerializeBigDecimal() throws JsonProcessingException {
        JsonGenerator generator = mapper.getFactory().createGenerator(new java.io.File("output.json"));
        ObjectWriter writer = mapper.writerWithView(MyView.class);

        JsonSerializer<Number> serializer = mapper.findValueSerializer(BigDecimal.class);
        BigDecimal bigDecimal = new BigDecimal("123.45");

        serializer.serialize(bigDecimal, generator, mapper.getSerializerProvider());
        generator.close();

        JsonParser parser = mapper.getFactory().createParser(new java.io.File("output.json"));
        JsonNode node = mapper.readTree(parser);
        JsonNode expectedNode = JsonNodeFactory.instance.numberNode("123.45");

        assertEquals(expectedNode, node);
    }

    // Test case for serialize method with BigInteger argument
    @Test
    public void testSerializeBigInteger() throws JsonProcessingException {
        JsonGenerator generator = mapper.getFactory().createGenerator(new java.io.File("output.json"));
        ObjectWriter writer = mapper.writerWithView(MyView.class);

        JsonSerializer<Number> serializer = mapper.findValueSerializer(BigInteger.class);
        BigInteger bigInteger = new BigInteger("12345678901234567890");

        serializer.serialize(bigInteger, generator, mapper.getSerializerProvider());
        generator.close();

        JsonParser parser = mapper.getFactory().createParser(new java.io.File("output.json"));
        JsonNode node = mapper.readTree(parser);
        JsonNode expectedNode = JsonNodeFactory.instance.numberNode("12345678901234567890");

        assertEquals(expectedNode, node);
    }

    // Test case for serialize method with Long argument
    @Test
    public void testSerializeLong() throws JsonProcessingException {
        JsonGenerator generator = mapper.getFactory().createGenerator(new java.io.File("output.json"));
        ObjectWriter writer = mapper.writerWithView(MyView.class);

        JsonSerializer<Number> serializer = mapper.findValueSerializer(Long.class);
        Long longValue = 123L;

        serializer.serialize(longValue, generator, mapper.getSerializerProvider());
        generator.close();

        JsonParser parser = mapper.getFactory().createParser(new java.io.File("output.json"));
        JsonNode node = mapper.readTree(parser);
        JsonNode expectedNode = JsonNodeFactory.instance.numberNode(123);

        assertEquals(expectedNode, node);
    }

    // Test case for serialize method with Double argument
    @Test
    public void testSerializeDouble() throws JsonProcessingException {
        JsonGenerator generator = mapper.getFactory().createGenerator(new java.io.File("output.json"));
        ObjectWriter writer = mapper.writerWithView(MyView.class);

        JsonSerializer<Number> serializer = mapper.findValueSerializer(Double.class);
        Double doubleValue = 123.45;

        serializer.serialize(doubleValue, generator, mapper.getSerializerProvider());
        generator.close();

        JsonParser parser = mapper.getFactory().createParser(new java.io.File("output.json"));
        JsonNode node = mapper.readTree(parser);
        JsonNode expectedNode = JsonNodeFactory.instance.numberNode("123.45");

        assertEquals(expectedNode, node);
    }

    // Test case for serialize method with Float argument
    @Test
    public void testSerializeFloat() throws JsonProcessingException {
        JsonGenerator generator = mapper.getFactory().createGenerator(new java.io.File("output.json"));
        ObjectWriter writer = mapper.writerWithView(MyView.class);

        JsonSerializer<Number> serializer = mapper.findValueSerializer(Float.class);
        Float floatValue = 123.45f;

        serializer.serialize(floatValue, generator, mapper.getSerializerProvider());
        generator.close();

        JsonParser parser = mapper.getFactory().createParser(new java.io.File("output.json"));
        JsonNode node = mapper.readTree(parser);
        JsonNode expectedNode = JsonNodeFactory.instance.numberNode("123.45");

        assertEquals(expectedNode, node);
    }

    // Test case for serialize method with String argument
    @Test
    public void testSerializeString() throws JsonProcessingException {
        JsonGenerator generator = mapper.getFactory().createGenerator(new java.io.File("output.json"));
        ObjectWriter writer = mapper.writerWithView(MyView.class);

        JsonSerializer<Number> serializer = mapper.findValueSerializer(Number.class);
        String stringValue = "123.45";

        serializer.serialize(stringValue, generator, mapper.getSerializerProvider());
        generator.close();

        JsonParser parser = mapper.getFactory().createParser(new java.io.File("output.json"));
        JsonNode node = mapper.readTree(parser);
        JsonNode expectedNode = JsonNodeFactory.instance.numberNode("123.45");

        assertEquals(expectedNode, node);
    }

    // Test case for getSchema method with Integer return type
    @Test
    public void testGetSchemaInteger() throws JsonProcessingException {
        JsonNode schema = new NumberSerializer().getSchema(mapper.getSerializerProvider(), Integer.class);
        JsonNode expectedSchema = JsonNodeFactory.instance.objectNode().put("type", "integer");

        assertEquals(expectedSchema, schema);
    }

    // Test case for getSchema method with BigDecimal return type
    @Test
    public void testGetSchemaBigDecimal() throws JsonProcessingException {
        JsonNode schema = new NumberSerializer().getSchema(mapper.getSerializerProvider(), BigDecimal.class);
        JsonNode expectedSchema = JsonNodeFactory.instance.objectNode().put("type", "number");

        assertEquals(expectedSchema, schema);
    }

    // Test case for serialize method with null value
    @Test(expected = JsonProcessingException.class)
    public void testSerializeNull() throws JsonProcessingException {
        JsonGenerator generator = mapper.getFactory().createGenerator(new java.io.File("output.json"));
        JsonSerializer<Number> serializer = mapper.findValueSerializer(Number.class);

        serializer.serialize(null, generator, mapper.getSerializerProvider());
    }
}