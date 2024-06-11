package com.fasterxml.jackson.databind.ser.std;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

public class NumberSerializerTest {

    @Test
    public void testSerializeBigDecimal() throws IOException {
        NumberSerializer serializer = new NumberSerializer(BigDecimal.class);
        JsonGenerator generator = null; // replace with actual generator
        SerializerProvider provider = null; // replace with actual provider

        BigDecimal value = new BigDecimal("10.5");
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        serializer.serialize(value, generator, provider);
        Assert.assertEquals(value, node.get("test")); // replace with actual assertion
    }

    @Test
    public void testSerializeBigInteger() throws IOException {
        NumberSerializer serializer = new NumberSerializer(BigInteger.class);
        JsonGenerator generator = null; // replace with actual generator
        SerializerProvider provider = null; // replace with actual provider

        BigInteger value = new BigInteger("10");
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        serializer.serialize(value, generator, provider);
        Assert.assertEquals(value, node.get("test")); // replace with actual assertion
    }

    @Test
    public void testSerializeLong() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Long.class);
        JsonGenerator generator = null; // replace with actual generator
        SerializerProvider provider = null; // replace with actual provider

        Long value = 10L;
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        serializer.serialize(value, generator, provider);
        Assert.assertEquals(value, node.get("test")); // replace with actual assertion
    }

    @Test
    public void testSerializeDouble() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Double.class);
        JsonGenerator generator = null; // replace with actual generator
        SerializerProvider provider = null; // replace with actual provider

        Double value = 10.5;
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        serializer.serialize(value, generator, provider);
        Assert.assertEquals(value, node.get("test")); // replace with actual assertion
    }

    @Test
    public void testSerializeFloat() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Float.class);
        JsonGenerator generator = null; // replace with actual generator
        SerializerProvider provider = null; // replace with actual provider

        Float value = 10.5f;
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        serializer.serialize(value, generator, provider);
        Assert.assertEquals(value, node.get("test")); // replace with actual assertion
    }

    @Test
    public void testSerializeInteger() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Integer.class);
        JsonGenerator generator = null; // replace with actual generator
        SerializerProvider provider = null; // replace with actual provider

        Integer value = 10;
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        serializer.serialize(value, generator, provider);
        Assert.assertEquals(value, node.get("test")); // replace with actual assertion
    }

    @Test
    public void testSerializeByte() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Byte.class);
        JsonGenerator generator = null; // replace with actual generator
        SerializerProvider provider = null; // replace with actual provider

        Byte value = 10;
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        serializer.serialize(value, generator, provider);
        Assert.assertEquals(value, node.get("test")); // replace with actual assertion
    }

    @Test
    public void testSerializeShort() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Short.class);
        JsonGenerator generator = null; // replace with actual generator
        SerializerProvider provider = null; // replace with actual provider

        Short value = 10;
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        serializer.serialize(value, generator, provider);
        Assert.assertEquals(value, node.get("test")); // replace with actual assertion
    }

    @Test
    public void testSerializeDefault() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonGenerator generator = null; // replace with actual generator
        SerializerProvider provider = null; // replace with actual provider

        Number value = 10;
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        serializer.serialize(value, generator, provider);
        Assert.assertEquals(value.toString(), node.get("test").asText()); // replace with actual assertion
    }
}