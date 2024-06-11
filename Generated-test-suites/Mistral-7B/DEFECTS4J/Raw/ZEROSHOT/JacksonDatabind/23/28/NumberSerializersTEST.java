package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Test class for NumberSerializers.
 */
public class NumberSerializersTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    /**
     * Test the serialization of integers.
     *
     * @throws JsonProcessingException if serialization fails.
     */
    @Test
    public void testSerializationOfIntegers() throws JsonProcessingException {
        Map<String, JsonSerializer<?>> allDeserializers = new JavaClassMap();
        NumberSerializers.addAll(allDeserializers);

        Integer intValue = 123;
        JsonNode jsonNode = objectMapper.writeValueAsTree(intValue);
        Assert.assertEquals(JsonNodeFactory.instance.numberNode(123), jsonNode);
    }

    /**
     * Test the serialization of longs.
     *
     * @throws JsonProcessingException if serialization fails.
     */
    @Test
    public void testSerializationOfLongs() throws JsonProcessingException {
        Map<String, JsonSerializer<?>> allDeserializers = new JavaClassMap();
        NumberSerializers.addAll(allDeserializers);

        Long longValue = 123L;
        JsonNode jsonNode = objectMapper.writeValueAsTree(longValue);
        Assert.assertEquals(JsonNodeFactory.instance.numberNode(123L), jsonNode);
    }

    /**
     * Test the serialization of shorts.
     *
     * @throws JsonProcessingException if serialization fails.
     */
    @Test
    public void testSerializationOfShorts() throws JsonProcessingException {
        Map<String, JsonSerializer<?>> allDeserializers = new JavaClassMap();
        NumberSerializers.addAll(allDeserializers);

        Short shortValue = 123;
        JsonNode jsonNode = objectMapper.writeValueAsTree(shortValue);
        Assert.assertEquals(JsonNodeFactory.instance.numberNode(123), jsonNode);
    }

    /**
     * Test the serialization of floats.
     *
     * @throws JsonProcessingException if serialization fails.
     */
    @Test
    public void testSerializationOfFloats() throws JsonProcessingException {
        Map<String, JsonSerializer<?>> allDeserializers = new JavaClassMap();
        NumberSerializers.addAll(allDeserializers);

        Float floatValue = 123.45f;
        JsonNode jsonNode = objectMapper.writeValueAsTree(floatValue);
        Assert.assertEquals(JsonNodeFactory.instance.numberNode(123.45f), jsonNode);
    }

    /**
     * Test the serialization of doubles.
     *
     * @throws JsonProcessingException if serialization fails.
     */
    @Test
    public void testSerializationOfDoubles() throws JsonProcessingException {
        Map<String, JsonSerializer<?>> allDeserializers = new JavaClassMap();
        NumberSerializers.addAll(allDeserializers);

        Double doubleValue = 123.45;
        JsonNode jsonNode = objectMapper.writeValueAsTree(doubleValue);
        Assert.assertEquals(JsonNodeFactory.instance.numberNode(123.45), jsonNode);
    }

    /**
     * Test the deserialization of integers.
     *
     * @throws IOException if deserialization fails.
     * @throws JsonMappingException if deserialization fails.
     */
    @Test
    public void testDeserializationOfIntegers() throws IOException, JsonMappingException {
        String jsonString = "123";
        Integer intValue = objectMapper.readValue(jsonString, Integer.class);
        Assert.assertEquals(123, intValue.intValue());
    }

    /**
     * Test the deserialization of longs.
     *
     * @throws IOException if deserialization fails.
     * @throws JsonMappingException if deserialization fails.
     */
    @Test
    public void testDeserializationOfLongs() throws IOException, JsonMappingException {
        String jsonString = "123L";
        Long longValue = objectMapper.readValue(jsonString, Long.class);
        Assert.assertEquals(123L, longValue.longValue());
    }

    /**
     * Test the deserialization of shorts.
     *
     * @throws IOException if deserialization fails.
     * @throws JsonMappingException if deserialization fails.
     */
    @Test
    public void testDeserializationOfShorts() throws IOException, JsonMappingException {
        String jsonString = "123";
        Short shortValue = objectMapper.readValue(jsonString, Short.class);
        Assert.assertEquals((short) 123, shortValue.shortValue());
    }

    /**
     * Test the deserialization of floats.
     *
     * @throws IOException if deserialization fails.
     * @throws JsonMappingException if deserialization fails.
     */
    @Test
    public void testDeserializationOfFloats() throws IOException, JsonMappingException {
        String jsonString = "123.45";
        Float floatValue = objectMapper.readValue(jsonString, Float.class);
        Assert.assertEquals(123.45f, floatValue.floatValue(), 0.001);
    }

    /**
     * Test the deserialization of doubles.
     *
     * @throws IOException if deserialization fails.
     * @throws JsonMappingException if deserialization fails.
     */
    @Test
    public void testDeserializationOfDoubles() throws IOException, JsonMappingException {
        String jsonString = "123.45";
        Double doubleValue = objectMapper.readValue(jsonString, Double.class);
        Assert.assertEquals(123.45, doubleValue.doubleValue(), 0.001);
    }
}