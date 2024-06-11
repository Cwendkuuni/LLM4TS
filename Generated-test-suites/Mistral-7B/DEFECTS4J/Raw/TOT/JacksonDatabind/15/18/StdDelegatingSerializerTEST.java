package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.ObjectMapper.DefaultSerializerProvider.Finder;

public class StdDelegatingSerializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testDelegateSerializationWithConverter() throws IOException, JsonProcessingException {
        Converter<String, Integer> converter = new StringToIntConverter();
        JsonSerializer<Object> delegateSerializer = objectMapper.findValueSerializer(Integer.class);

        StdDelegatingSerializer<Object> serializer = new StdDelegatingSerializer<>(converter);

        String input = "42";
        Object output = serializer.serialize(input, objectMapper.getJsonFactory(), objectMapper.getSerializerProvider());

        Assert.assertEquals(42, output);

        JsonNode schema = serializer.getSchema(objectMapper.getSerializerProvider(), String.class);
        Assert.assertNotNull(schema);
        Assert.assertEquals("number", schema.getClass().getSimpleName());

        // Testing with JSON serialization
        JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
        serializer.serialize(input, gen, objectMapper.getSerializerProvider());
        gen.flush();

        delegateSerializer.serialize(42, gen, objectMapper.getSerializerProvider());
        gen.flush();

        Assert.assertEquals(42, output);
    }

    @Test
    public void testDelegateSerializationWithContextualization() throws IOException, JsonProcessingException {
        Converter<String, Long> converter = new StringToLongConverter();
        JsonSerializer<Object> delegateSerializer = objectMapper.findValueSerializer(Long.class);

        Finder serializers = objectMapper.getSerializerProvider().findValueSerializers();
        JsonSerializer<Object> contextualSerializer = serializers.findValueSerializer(String.class, null);

        StdDelegatingSerializer<Object> serializer = new StdDelegatingSerializer<>(converter, null, contextualSerializer);

        String input = "42";
        Object output = serializer.serialize(input, objectMapper.getJsonFactory(), objectMapper.getSerializerProvider());

        Assert.assertEquals(42L, output);

        JsonNode schema = serializer.getSchema(objectMapper.getSerializerProvider(), String.class);
        Assert.assertNotNull(schema);
        Assert.assertEquals("number", schema.getClass().getSimpleName());

        // Testing with JSON serialization
        JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
        serializer.serialize(input, gen, objectMapper.getSerializerProvider());
        gen.flush();

        delegateSerializer.serialize(42L, gen, objectMapper.getSerializerProvider());
        gen.flush();

        Assert.assertEquals(42L, output);
    }

    @Test
    public void testDelegateSerializationWithFindValueSerializer() throws IOException, JsonProcessingException {
        Converter<String, Float> converter = new StringToFloatConverter();
        JsonSerializer<Object> delegateSerializer = objectMapper.findValueSerializer(Float.class);

        StdDelegatingSerializer<Object> serializer = new StdDelegatingSerializer<>(converter, null, null);

        String input = "3.14";
        Object output = serializer.serialize(input, objectMapper.getJsonFactory(), objectMapper.getSerializerProvider());

        Assert.assertEquals(3.14F, output);

        JsonNode schema = serializer.getSchema(objectMapper.getSerializerProvider(), String.class);
        Assert.assertNotNull(schema);
        Assert.assertEquals("number", schema.getClass().getSimpleName());

        // Testing with JSON serialization
        JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
        serializer.serialize(input, gen, objectMapper.getSerializerProvider());
        gen.flush();

        delegateSerializer.serialize(3.14F, gen, objectMapper.getSerializerProvider());
        gen.flush();

        Assert.assertEquals(3.14F, output);
    }

    @Test(expected = IllegalStateException.class)
    public void testDelegateSerializationWithInvalidType() throws IOException, JsonProcessingException {
        Converter<String, Byte[]> converter = new StringToByteArrayConverter();
        JsonSerializer<Object> delegateSerializer = objectMapper.findValueSerializer(byte[].class);

        StdDelegatingSerializer<Object> serializer = new StdDelegatingSerializer<>(converter);

        String input = "Hello World";
        Object output = serializer.serialize(input, objectMapper.getJsonFactory(), objectMapper.getSerializerProvider());

        Assert.fail("Expected IllegalStateException");
    }

    @Test
    public void testSerializeWithType() throws IOException, JsonProcessingException {
        Converter<Integer, String> converter = new IntToStringConverter();
        JsonSerializer<Object> delegateSerializer = objectMapper.findValueSerializer(String.class);

        StdDelegatingSerializer<Object> serializer = new StdDelegatingSerializer<>(converter, null, null);

        Integer input = 42;
        Object output = serializer.serializeWithType(input, objectMapper.getJsonFactory(), objectMapper.getSerializerProvider(), objectMapper.getTypeSerializer());

        Assert.assertEquals("42", output.toString());

        JsonNode schema = serializer.getSchema(objectMapper.getSerializerProvider(), Integer.class);
        Assert.assertNotNull(schema);
        Assert.assertEquals("string", schema.getClass().getSimpleName());

        // Testing with JSON serialization
        JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
        serializer.serializeWithType(input, gen, objectMapper.getSerializerProvider(), objectMapper.getTypeSerializer());
        gen.flush();

        delegateSerializer.serializeWithType(input.toString(), gen, objectMapper.getSerializerProvider(), objectMapper.getTypeSerializer());
        gen.flush();

        Assert.assertEquals("42", output.toString());
    }

    private static class StringToIntConverter implements Converter<String, Integer> {
        @Override
        public Integer convert(String value) {
            return Integer.parseInt(value);
        }
    }

    private static class StringToLongConverter implements Converter<String, Long> {
        @Override
        public Long convert(String value) {
            return Long.parseLong(value);
        }
    }

    private static class StringToFloatConverter implements Converter<String, Float> {
        @Override
        public Float convert(String value) {
            return Float.parseFloat(value);
        }
    }

    private static class IntToStringConverter implements Converter<Integer, String> {
        @Override
        public String convert(Integer value) {
            return value.toString();
        }
    }

    private static class StringToByteArrayConverter implements Converter<String, byte[]> {
        @Override
        public byte[] convert(String value) {
            return value.getBytes();
        }
    }
}