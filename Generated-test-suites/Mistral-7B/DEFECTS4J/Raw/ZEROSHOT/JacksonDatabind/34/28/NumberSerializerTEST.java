package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializationTest;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Test case for {@link NumberSerializer}.
 */
public class NumberSerializerTest {

    @Test
    public void testSerializeBigDecimal() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        NumberSerializer numberSerializer = NumberSerializer.instance;

        BigDecimal bigDecimal = new BigDecimal("123.456");
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator();
        numberSerializer.serialize(bigDecimal, jsonGenerator, null);
        String actualJson = jsonGenerator.getOutputContent();
        String expectedJson = "123.456";
        assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testSerializeBigInteger() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        NumberSerializer numberSerializer = NumberSerializer.instance;

        BigInteger bigInteger = BigInteger.valueOf(1234567890L);
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator();
        numberSerializer.serialize(bigInteger, jsonGenerator, null);
        String actualJson = jsonGenerator.getOutputContent();
        String expectedJson = "1234567890";
        assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testSerializeInteger() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        NumberSerializer numberSerializer = NumberSerializer.instance;

        Integer integer = 123;
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator();
        numberSerializer.serialize(integer, jsonGenerator, null);
        String actualJson = jsonGenerator.getOutputContent();
        String expectedJson = "123";
        assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testSerializeLong() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        NumberSerializer numberSerializer = NumberSerializer.instance;

        Long longValue = 1234567890L;
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator();
        numberSerializer.serialize(longValue, jsonGenerator, null);
        String actualJson = jsonGenerator.getOutputContent();
        String expectedJson = "1234567890";
        assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testSerializeDouble() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        NumberSerializer numberSerializer = NumberSerializer.instance;

        Double doubleValue = 123.456;
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator();
        numberSerializer.serialize(doubleValue, jsonGenerator, null);
        String actualJson = jsonGenerator.getOutputContent();
        String expectedJson = "123.456";
        assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testSerializeFloat() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        NumberSerializer numberSerializer = NumberSerializer.instance;

        Float floatValue = 123.456f;
        ObjectWriter objectWriter = mapper.writer().withFeature(SerializationFeature.WRITE_FLOAT_AS_INT);
        ObjectNode objectNode = objectWriter.writeValueAsTree(floatValue);
        String actualJson = objectNode.toString();
        String expectedJson = "123";
        assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testSerializeByte() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        NumberSerializer numberSerializer = NumberSerializer.instance;

        Byte byteValue = (byte) 123;
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator();
        numberSerializer.serialize(byteValue, jsonGenerator, null);
        String actualJson = jsonGenerator.getOutputContent();
        String expectedJson = "123";
        assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testSerializeShort() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        NumberSerializer numberSerializer = NumberSerializer.instance;

        Short shortValue = (short) 123;
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator();
        numberSerializer.serialize(shortValue, jsonGenerator, null);
        String actualJson = jsonGenerator.getOutputContent();
        String expectedJson = "123";
        assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testDeserializeBigDecimal() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        String json = "123.456";
        JsonParser jsonParser = mapper.getFactory().createParser(json);
        JsonDeserializer<Number> deserializer = NumberDeserializers.instance;

        Object readValue = deserializer.deserialize(jsonParser, Number.class, null);
        Number actualNumber = (Number) readValue;
        BigDecimal expectedNumber = new BigDecimal("123.456");
        assertEquals(expectedNumber, actualNumber);
    }

    @Test
    public void testDeserializeBigInteger() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        String json = "1234567890";
        JsonParser jsonParser = mapper.getFactory().createParser(json);
        JsonDeserializer<Number> deserializer = NumberDeserializers.instance;

        Object readValue = deserializer.deserialize(jsonParser, Number.class, null);
        Number actualNumber = (Number) readValue;
        BigInteger expectedNumber = BigInteger.valueOf(1234567890L);
        assertEquals(expectedNumber, actualNumber);
    }

    @Test
    public void testDeserializeInteger() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        String json = "123";
        JsonParser jsonParser = mapper.getFactory().createParser(json);
        JsonDeserializer<Number> deserializer = NumberDeserializers.instance;

        Object readValue = deserializer.deserialize(jsonParser, Number.class, null);
        Number actualNumber = (Number) readValue;
        Integer expectedNumber = 123;
        assertEquals(expectedNumber, actualNumber);
    }

    @Test
    public void testDeserializeLong() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        String json = "1234567890";
        JsonParser jsonParser = mapper.getFactory().createParser(json);
        JsonDeserializer<Number> deserializer = NumberDeserializers.instance;

        Object readValue = deserializer.deserialize(jsonParser, Number.class, null);
        Number actualNumber = (Number) readValue;
        Long expectedNumber = 1234567890L;
        assertEquals(expectedNumber, actualNumber);
    }

    @Test
    public void testDeserializeDouble() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        String json = "123.456";
        JsonParser jsonParser = mapper.getFactory().createParser(json);
        JsonDeserializer<Number> deserializer = NumberDeserializers.instance;

        Object readValue = deserializer.deserialize(jsonParser, Number.class, null);
        Number actualNumber = (Number) readValue;
        Double expectedNumber = 123.456;
        assertEquals(expectedNumber, actualNumber);
    }

    @Test
    public void testDeserializeFloat() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        String json = "123";
        JsonParser jsonParser = mapper.getFactory().createParser(json);
        JsonDeserializer<Number> deserializer = NumberDeserializers.instance;

        Object readValue = deserializer.deserialize(jsonParser, Number.class, null);
        Number actualNumber = (Number) readValue;
        Float expectedNumber = 123.0f;
        assertEquals(expectedNumber, actualNumber);
    }

    @Test
    public void testDeserializeByte() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        String json = "123";
        JsonParser jsonParser = mapper.getFactory().createParser(json);
        JsonDeserializer<Number> deserializer = NumberDeserializers.instance;

        Object readValue = deserializer.deserialize(jsonParser, Number.class, null);
        Number actualNumber = (Number) readValue;
        Byte expectedNumber = (byte) 123;
        assertEquals(expectedNumber, actualNumber);
    }

    @Test
    public void testDeserializeShort() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        String json = "123";
        JsonParser jsonParser = mapper.getFactory().createParser(json);
        JsonDeserializer<Number> deserializer = NumberDeserializers.instance;

        Object readValue = deserializer.deserialize(jsonParser, Number.class, null);
        Number actualNumber = (Number) readValue;
        Short expectedNumber = (short) 123;
        assertEquals(expectedNumber, actualNumber);
    }
}