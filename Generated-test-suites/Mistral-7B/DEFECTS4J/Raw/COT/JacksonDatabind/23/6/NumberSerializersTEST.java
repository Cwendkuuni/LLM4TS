package com.example.junit; // replace with your test package

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test class for NumberSerializers.
 */
public class NumberSerializersTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testAddAll() {
        // Given
        Map<String, JsonSerializer<?>> allDeserializers = new java.util.HashMap<>();

        // When
        NumberSerializers.addAll(allDeserializers);

        // Then
        assertEquals(12, allDeserializers.size());
        assertEquals(Integer.class.getName(), allDeserializers.get(Integer.class.getName()).getClass().getName());
        assertEquals(Long.class.getName(), allDeserializers.get(Long.class.getName()).getClass().getName());
        // ... add other assertions for other classes
    }

    @Test
    public void testIntegerSerializer_serialize_intValue() throws Exception {
        // Given
        JsonSerializer<Object> integerSerializer = NumberSerializers.IntegerSerializer.instance;
        Integer value = 123;
        JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
        SerializerProvider provider = objectMapper.getSerializerProvider();

        // When
        integerSerializer.serialize(value, gen, provider);

        // Then
        String result = gen.getOutputBuffer().toString();
        assertEquals("123", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntegerSerializer_serialize_nullValue() throws Exception {
        // Given
        JsonSerializer<Object> integerSerializer = NumberSerializers.IntegerSerializer.instance;
        Integer value = null;
        JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
        SerializerProvider provider = objectMapper.getSerializerProvider();

        // When
        integerSerializer.serialize(value, gen, provider);

        // Then
        // An exception should be thrown
    }

    // Add other test methods for other serializers (ShortSerializer, LongSerializer, etc.)

}