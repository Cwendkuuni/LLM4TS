package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerProviders;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;

public class NumberSerializersTest {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final JsonNodeFactory factory = mapper.getNodeFactory();

    @BeforeClass
    public static void setUp() {
        // Set up shared serializers map
        Map<String, JsonSerializer<?>> serializers = mapper.getSerializerProvider().getDefaultSerializerProvider()
                .getSerializerProvider().getSerializerMapper().findAndRegisterSerializers(NumberSerializers.class, null);
        NumberSerializers.addAll(serializers);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        mapper.close();
    }

    @Test
    public void testIntSerializer() throws IOException {
        int value = 2147483647;
        JsonSerializer<Integer> intSerializer = mapper.getSerializerProvider().findValueSerializer(Integer.class);

        JsonNode result = mapper.writeValueAsTree(value);
        assertNotNull(result);
        assertEquals("2147483647", result.toString());

        Integer deserialized = mapper.readValue(result.traverse(), Integer.class);
        assertEquals(value, deserialized.intValue());
    }

    @Test
    public void testLongSerializer() throws IOException {
        long value = 9223372036854775807L;
        JsonSerializer<Long> longSerializer = mapper.getSerializerProvider().findValueSerializer(Long.class);

        JsonNode result = mapper.writeValueAsTree(value);
        assertNotNull(result);
        assertEquals("9223372036854775807", result.toString());

        Long deserialized = mapper.readValue(result.traverse(), Long.class);
        assertEquals(value, deserialized.longValue());
    }

    @Test
    public void testFloatSerializer() throws IOException {
        float value = Float.POSITIVE_INFINITY;
        JsonSerializer<Float> floatSerializer = mapper.getSerializerProvider().findValueSerializer(Float.class);

        JsonNode result = mapper.writeValueAsTree(value);
        assertNotNull(result);
        assertEquals("inf", result.toString());

        Float deserialized = mapper.readValue(result.traverse(), Float.class);
        assertEquals(value, deserialized.floatValue());
    }

    @Test
    public void testDoubleSerializer() throws IOException {
        double value = Double.POSITIVE_INFINITY;
        JsonSerializer<Double> doubleSerializer = mapper.getSerializerProvider().findValueSerializer(Double.class);

        JsonNode result = mapper.writeValueAsTree(value);
        assertNotNull(result);
        assertEquals("inf", result.toString());

        Double deserialized = mapper.readValue(result.traverse(), Double.class);
        assertEquals(value, deserialized.doubleValue());
    }

    @Test
    public void testJsonFormatAnnotation() throws IOException {
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        int annotatedInt = 42;
        JsonSerializer<Integer> intSerializer = mapper.getSerializerProvider().findValueSerializer(Integer.class);

        JsonNode result = mapper.writeValueAsTree(annotatedInt);
        assertNotNull(result);
        assertEquals("42", result.toString());

        Integer deserialized = mapper.readValue(result.traverse(), Integer.class);
        assertEquals(annotatedInt, deserialized.intValue());
    }
}