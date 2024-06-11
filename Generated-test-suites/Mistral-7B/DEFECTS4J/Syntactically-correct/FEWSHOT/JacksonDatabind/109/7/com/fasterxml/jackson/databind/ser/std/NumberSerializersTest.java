package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.lang.reflect.Type;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

public class NumberSerializersTest {

    @Test
    public void testIntegerSerializer() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider serializers = mapper.getSerializerProvider();
        JsonSerializer<Integer> intSerializer = NumberSerializers.IntegerSerializer.instance;

        Integer value = 123;
        JsonNode result = mapper.readTree(mapper.writeValueAsString(value));

        assertEquals(value.intValue(), result.asInt());
        assertEquals("123", result.asText());

        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        intSerializer.serialize(value, gen, serializers);
        gen.flush();

        assertEquals("123", gen.getOutputBuffer().toString());
        gen.close();

        assertEquals(Integer.class, intSerializer.handledType());
    }

    @Test
    public void testIntLikeSerializer() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider serializers = mapper.getSerializerProvider();
        JsonSerializer<Number> intLikeSerializer = NumberSerializers.IntLikeSerializer.instance;

        Number value = 123;
        JsonNode result = mapper.readTree(mapper.writeValueAsString(value));

        assertEquals(value.intValue(), result.asInt());
        assertEquals("123", result.asText());

        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        intLikeSerializer.serialize(value, gen, serializers);
        gen.flush();

        assertEquals("123", gen.getOutputBuffer().toString());
        gen.close();

        assertEquals(Number.class, intLikeSerializer.handledType());
    }

    @Test
    public void testShortSerializer() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider serializers = mapper.getSerializerProvider();
        JsonSerializer<Short> shortSerializer = NumberSerializers.ShortSerializer.instance;

        Short value = Short.parseShort("123");
        JsonNode result = mapper.readTree(mapper.writeValueAsString(value));

        assertEquals(value.shortValue(), result.asInt());
        assertEquals("123", result.asText());

        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        shortSerializer.serialize(value, gen, serializers);
        gen.flush();

        assertEquals("123", gen.getOutputBuffer().toString());
        gen.close();

        assertEquals(Short.class, shortSerializer.handledType());
    }

    @Test
    public void testLongSerializer() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider serializers = mapper.getSerializerProvider();
        JsonSerializer<Long> longSerializer = NumberSerializers.LongSerializer.instance;

        Long value = Long.parseLong("12345678901234567890");
        JsonNode result = mapper.readTree(mapper.writeValueAsString(value));

        assertEquals(value.longValue(), result.asLong());
        assertEquals("12345678901234567890", result.asText());

        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        longSerializer.serialize(value, gen, serializers);
        gen.flush();

        assertEquals("12345678901234567890", gen.getOutputBuffer().toString());
        gen.close();

        assertEquals(Long.class, longSerializer.handledType());
    }

    @Test
    public void testFloatSerializer() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider serializers = mapper.getSerializerProvider();
        JsonSerializer<Float> floatSerializer = NumberSerializers.FloatSerializer.instance;

        Float value = Float.parseFloat("123.45");
        JsonNode result = mapper.readTree(mapper.writeValueAsString(value));

        assertEquals(value.floatValue(), result.asFloat(), 0.01f);
        assertEquals("123.45", result.asText());

        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        floatSerializer.serialize(value, gen, serializers);
        gen.flush();

        assertEquals("123.45", gen.getOutputBuffer().toString());
        gen.close();

        assertEquals(Float.class, floatSerializer.handledType());
    }

    @Test
    public void testDoubleSerializer() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider serializers = mapper.getSerializerProvider();
        JsonSerializer<Double> doubleSerializer = NumberSerializers.DoubleSerializer.instance;

        Double value = Double.parseDouble("123.456789");
        JsonNode result = mapper.readTree(mapper.writeValueAsString(value));

        assertEquals(value.doubleValue(), result.asDouble(), 0.0001);
        assertEquals("123.456789", result.asText());

        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        doubleSerializer.serialize(value, gen, serializers);
        gen.flush();

        assertEquals("123.456789", gen.getOutputBuffer().toString());
        gen.close();

        assertEquals(Double.class, doubleSerializer.handledType());
    }
}