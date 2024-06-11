package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import org.junit.Test;

public class NumberSerializersTest {

    @Test
    public void testShortSerializer() {
        Short value = Short.MIN_VALUE;
        JsonGenerator gen = createJsonGenerator();

        NumberSerializers.ShortSerializer serializer = NumberSerializers.ShortSerializer.instance;
        try {
            serializer.serialize(value, gen, new ObjectMapper().getSerializerProvider());
            String actual = gen.getOutputBuffer().toString();
            assertEquals("- Short.MIN_VALUE", actual);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testIntegerSerializer() {
        Integer value = Integer.MIN_VALUE;
        JsonGenerator gen = createJsonGenerator();

        NumberSerializers.IntegerSerializer serializer = NumberSerializers.IntegerSerializer.instance;
        try {
            serializer.serialize(value, gen, new ObjectMapper().getSerializerProvider());
            String actual = gen.getOutputBuffer().toString();
            assertEquals("- Integer.MIN_VALUE", actual);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testIntLikeSerializer() {
        Long value = Long.MIN_VALUE;
        JsonGenerator gen = createJsonGenerator();

        NumberSerializers.IntLikeSerializer serializer = NumberSerializers.IntLikeSerializer.instance;
        try {
            serializer.serialize(value, gen, new ObjectMapper().getSerializerProvider());
            String actual = gen.getOutputBuffer().toString();
            assertEquals("- Long.MIN_VALUE", actual);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLongSerializer() {
        Long value = Long.MIN_VALUE;
        JsonGenerator gen = createJsonGenerator();

        NumberSerializers.LongSerializer serializer = NumberSerializers.LongSerializer.instance;
        try {
            serializer.serialize(value, gen, new ObjectMapper().getSerializerProvider());
            String actual = gen.getOutputBuffer().toString();
            assertEquals("- Long.MIN_VALUE", actual);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFloatSerializer() {
        Float value = Float.MIN_VALUE;
        JsonGenerator gen = createJsonGenerator();

        NumberSerializers.FloatSerializer serializer = NumberSerializers.FloatSerializer.instance;
        try {
            serializer.serialize(value, gen, new ObjectMapper().getSerializerProvider());
            String actual = gen.getOutputBuffer().toString();
            assertEquals("-Infinity", actual);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDoubleSerializer() {
        Double value = Double.MIN_VALUE;
        JsonGenerator gen = createJsonGenerator();

        NumberSerializers.DoubleSerializer serializer = NumberSerializers.DoubleSerializer.instance;
        try {
            serializer.serialize(value, gen, new ObjectMapper().getSerializerProvider());
            String actual = gen.getOutputBuffer().toString();
            assertEquals("-Infinity", actual);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAddAllToMap() {
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);

        JsonSerializer<?> intSerializer = allDeserializers.get(Integer.class.getName());
        assertEquals(NumberSerializers.IntegerSerializer.class, intSerializer.getClass());

        JsonSerializer<?> longSerializer = allDeserializers.get(Long.class.getName());
        assertEquals(NumberSerializers.LongSerializer.class, longSerializer.getClass());

        JsonSerializer<?> shortSerializer = allDeserializers.get(Short.class.getName());
        assertEquals(NumberSerializers.ShortSerializer.class, shortSerializer.getClass());

        JsonSerializer<?> floatSerializer = allDeserializers.get(Float.class.getName());
        assertEquals(NumberSerializers.FloatSerializer.class, floatSerializer.getClass());

        JsonSerializer<?> doubleSerializer = allDeserializers.get(Double.class.getName());
        assertEquals(NumberSerializers.DoubleSerializer.class, doubleSerializer.getClass());

        JsonSerializer<?> intLikeSerializer = allDeserializers.get(Number.class.getName());
        assertEquals(NumberSerializers.IntLikeSerializer.class, intLikeSerializer.getClass());
    }

    private JsonGenerator createJsonGenerator() throws JsonProcessingException {
        StringWriter writer = new StringWriter();
        JsonFactory factory = new JsonFactory();
        return factory.createGenerator(writer);
    }
}