package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.lang.reflect.Type;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

public class NumberSerializersTest {

    @Test
    public void testIntegerSerializer() throws Exception {
        JsonSerializer<Integer> serializer = NumberSerializers.findIntSerializer(null);
        assertNotNull(serializer);

        JsonNode node = mapper.readTree("{\"num\":1}");
        Integer value = mapper.treeToValue(node.path("num"), Integer.class);
        assertEquals(1, value.intValue());

        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        serializer.serialize(1, gen, null);
        assertEquals("1", gen.getOutputContent().toString());
    }

    @Test
    public void testIntLikeSerializer() throws Exception {
        JsonSerializer<Number> serializer = NumberSerializers.findIntLikeSerializer(null);
        assertNotNull(serializer);

        JsonNode node = mapper.readTree("{\"num\":1}");
        Number value = mapper.treeToValue(node.path("num"), Number.class);
        assertEquals(1, value.intValue());

        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        serializer.serialize(1, gen, null);
        assertEquals("1", gen.getOutputContent().toString());
    }

    @Test
    public void testShortSerializer() throws Exception {
        JsonSerializer<Short> serializer = NumberSerializers.findShortSerializer(null);
        assertNotNull(serializer);

        JsonNode node = mapper.readTree("{\"num\":1}");
        Short value = mapper.treeToValue(node.path("num"), Short.class);
        assertEquals(1, value.shortValue());

        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        serializer.serialize(1, gen, null);
        assertEquals("1", gen.getOutputContent().toString());
    }

    @Test
    public void testLongSerializer() throws Exception {
        JsonSerializer<Long> serializer = NumberSerializers.findLongSerializer(null);
        assertNotNull(serializer);

        JsonNode node = mapper.readTree("{\"num\":1}");
        Long value = mapper.treeToValue(node.path("num"), Long.class);
        assertEquals(1, value.longValue());

        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        serializer.serialize(1, gen, null);
        assertEquals("1", gen.getOutputContent().toString());
    }

    @Test
    public void testFloatSerializer() throws Exception {
        JsonSerializer<Float> serializer = NumberSerializers.findFloatSerializer(null);
        assertNotNull(serializer);

        JsonNode node = mapper.readTree("{\"num\":1.5f}");
        Float value = mapper.treeToValue(node.path("num"), Float.class);
        assertEquals(1.5f, value, 0.001f);

        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        serializer.serialize(1.5f, gen, null);
        assertEquals("1.5", gen.getOutputContent().toString());
    }

    @Test
    public void testDoubleSerializer() throws Exception {
        JsonSerializer<Double> serializer = NumberSerializers.findDoubleSerializer(null);
        assertNotNull(serializer);

        JsonNode node = mapper.readTree("{\"num\":1.5}");
        Double value = mapper.treeToValue(node.path("num"), Double.class);
        assertEquals(1.5, value, 0.001);

        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        serializer.serialize(1.5, gen, null);
        assertEquals("1.5", gen.getOutputContent().toString());
    }

    @Test
    public void testAddAll() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, JsonSerializer<?>> serializers = new LinkedHashMap<>();

        NumberSerializers.addAll(serializers);

        assertEquals(6, serializers.size());
        assertEquals(IntegerSerializer.class, serializers.get(Integer.class.getName()));
        assertEquals(IntegerSerializer.class, serializers.get(Integer.TYPE.getName()));
        assertEquals(LongSerializer.class, serializers.get(Long.class.getName()));
        assertEquals(LongSerializer.class, serializers.get(Long.TYPE.getName()));
        assertEquals(ShortSerializer.class, serializers.get(Short.class.getName()));
        assertEquals(ShortSerializer.class, serializers.get(Short.TYPE.getName()));
    }
}