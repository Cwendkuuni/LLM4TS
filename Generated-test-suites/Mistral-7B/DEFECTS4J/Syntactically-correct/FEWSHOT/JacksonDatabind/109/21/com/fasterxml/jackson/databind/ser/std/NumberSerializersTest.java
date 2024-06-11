package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.*;

import org.junit.Test;

public class NumberSerializersTest {

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new LinkedHashMap<>();
        NumberSerializers.addAll(allDeserializers);

        assertEquals(11, allDeserializers.size());
        assertTrue(allDeserializers.containsKey(Integer.class.getName()));
        assertTrue(allDeserializers.containsKey(Integer.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Long.class.getName()));
        assertTrue(allDeserializers.containsKey(Long.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Byte.class.getName()));
        assertTrue(allDeserializers.containsKey(Byte.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Short.class.getName()));
        assertTrue(allDeserializers.containsKey(Short.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Double.class.getName()));
        assertTrue(allDeserializers.containsKey(Double.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Float.class.getName()));
        assertTrue(allDeserializers.containsKey(Float.TYPE.getName()));
    }

    @Test
    public void testShortSerializer() throws JsonProcessingException {
        ShortSerializer serializer = NumberSerializers.ShortSerializer.instance;
        JsonSerializer<Short> shortSerializer = new JsonSerializer<Short>() {
            @Override
            public void serialize(Short value, JsonGenerator gen, SerializerProvider provider)
                    throws IOException {
                gen.writeNumber(value.shortValue());
            }
        };

        assertEquals(shortSerializer, serializer);

        ObjectWriter writer = new ObjectMapper().writerWithDefaultPrettyPrinter();
        Short num = (short) 123;
        String json = writer.writeValueAsString(num);
        assertEquals("123", json);
    }

    @Test
    public void testIntegerSerializer() throws JsonProcessingException {
        IntegerSerializer serializer = NumberSerializers.IntegerSerializer.instance;
        JsonSerializer<Integer> intSerializer = new JsonSerializer<Integer>() {
            @Override
            public void serialize(Integer value, JsonGenerator gen, SerializerProvider provider)
                    throws IOException {
                gen.writeNumber(value.intValue());
            }
        };

        assertEquals(intSerializer, serializer);

        ObjectWriter writer = new ObjectMapper().writerWithDefaultPrettyPrinter();
        Integer num = 123;
        String json = writer.writeValueAsString(num);
        assertEquals("123", json);
    }

    @Test
    public void testIntLikeSerializer() throws JsonProcessingException {
        IntLikeSerializer serializer = NumberSerializers.IntLikeSerializer.instance;
        JsonSerializer<Number> intLikeSerializer = new JsonSerializer<Number>() {
            @Override
            public void serialize(Number value, JsonGenerator gen, SerializerProvider provider)
                    throws IOException {
                gen.writeNumber(value.intValue());
            }
        };

        assertEquals(intLikeSerializer, serializer);

        ObjectWriter writer = new ObjectMapper().writerWithDefaultPrettyPrinter();
        Number num = 123;
        String json = writer.writeValueAsString(num);
        assertEquals("123", json);
    }

    @Test
    public void testLongSerializer() throws JsonProcessingException {
        LongSerializer serializer = NumberSerializers.LongSerializer.instance;
        JsonSerializer<Long> longSerializer = new JsonSerializer<Long>() {
            @Override
            public void serialize(Long value, JsonGenerator gen, SerializerProvider provider)
                    throws IOException {
                gen.writeNumber(value.longValue());
            }
        };

        assertEquals(longSerializer, serializer);

        ObjectWriter writer = new ObjectMapper().writerWithDefaultPrettyPrinter();
        Long num = 123L;
        String json = writer.writeValueAsString(num);
        assertEquals("123", json);
    }

    @Test
    public void testFloatSerializer() throws JsonProcessingException {
        FloatSerializer serializer = NumberSerializers.FloatSerializer.instance;
        JsonSerializer<Float> floatSerializer = new JsonSerializer<Float>() {
            @Override
            public void serialize(Float value, JsonGenerator gen, SerializerProvider provider)
                    throws IOException {
                gen.writeNumber(value.floatValue());
            }
        };

        assertEquals(floatSerializer, serializer);

        ObjectWriter writer = new ObjectMapper().writerWithDefaultPrettyPrinter();
        Float num = 123.45f;
        String json = writer.writeValueAsString(num);
        assertEquals("123.45", json);
    }

    @Test
    public void testDoubleSerializer() throws JsonProcessingException {
        DoubleSerializer serializer = NumberSerializers.DoubleSerializer.instance;
        JsonSerializer<Double> doubleSerializer = new JsonSerializer<Double>() {
            @Override
            public void serialize(Double value, JsonGenerator gen, SerializerProvider provider)
                    throws IOException {
                gen.writeNumber(value.doubleValue());
            }
        };

        assertEquals(doubleSerializer, serializer);

        ObjectWriter writer = new ObjectMapper().writerWithDefaultPrettyPrinter();
        Double num = 123.45;
        String json = writer.writeValueAsString(num);
        assertEquals("123.45", json);
    }
}