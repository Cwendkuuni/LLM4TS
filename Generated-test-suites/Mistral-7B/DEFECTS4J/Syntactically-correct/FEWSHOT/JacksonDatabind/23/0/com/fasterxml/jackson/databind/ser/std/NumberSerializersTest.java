package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

public class NumberSerializersTest {

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> serializers = new java.util.HashMap<>();
        NumberSerializers.addAll(serializers);
        assertEquals(11, serializers.size());
    }

    @Test
    public void testShortSerializer() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Short value = Short.MIN_VALUE;
        JsonSerializer<Short> serializer = NumberSerializers.findShortSerializer(mapper.getSerializerProvider());
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);

        serializer.serialize(value, gen, mapper.getSerializerProvider());
        gen.flush();
    }

    @Test
    public void testIntegerSerializer() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Integer value = Integer.MIN_VALUE;
        JsonSerializer<Integer> serializer = NumberSerializers.findIntegerSerializer(mapper.getSerializerProvider());
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);

        serializer.serialize(value, gen, mapper.getSerializerProvider());
        gen.flush();
    }

    @Test
    public void testIntLikeSerializer() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Number value = Byte.MIN_VALUE;
        JsonSerializer<Number> serializer = NumberSerializers.findIntLikeSerializer(mapper.getSerializerProvider());
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);

        serializer.serialize(value, gen, mapper.getSerializerProvider());
        gen.flush();
    }

    @Test
    public void testLongSerializer() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Long value = Long.MIN_VALUE;
        JsonSerializer<Long> serializer = NumberSerializers.LongSerializer.instance;
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);

        serializer.serialize(value, gen, mapper.getSerializerProvider());
        gen.flush();
    }

    @Test
    public void testFloatSerializer() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Float value = Float.MIN_VALUE;
        JsonSerializer<Float> serializer = NumberSerializers.FloatSerializer.instance;
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);

        serializer.serialize(value, gen, mapper.getSerializerProvider());
        gen.flush();
    }

    @Test
    public void testDoubleSerializer() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Double value = Double.MIN_VALUE;
        JsonSerializer<Double> serializer = NumberSerializers.DoubleSerializer.instance;
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);

        serializer.serialize(value, gen, mapper.getSerializerProvider());
        gen.flush();
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<?> serializer = NumberSerializers.ShortSerializer.instance.createContextual(mapper.getSerializerProvider(), null);
        assertNotNull(serializer);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<Short> serializer = NumberSerializers.ShortSerializer.instance;
        serializer.acceptJsonFormatVisitor(null, null);
    }

    @Test
    public void testGetSchema() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<Short> serializer = NumberSerializers.ShortSerializer.instance;
        Object schema = serializer.getSchema(mapper.getSerializerProvider(), Short.class);
        assertNotNull(schema);
    }
}