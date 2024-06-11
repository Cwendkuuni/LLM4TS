package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.JsonSerializerProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.std.StdDelegatingSerializer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class StdDelegatingSerializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @After
    public void teardown() throws Exception {
        objectMapper = null;
    }

    @Test
    public void testSerializeObject() throws JsonMappingException, JsonGenerationException, IOException {
        Converter<Object, String> converter = new SimpleStringConverter();
        StdDelegatingSerializer delegatingSerializer = new StdDelegatingSerializer(converter);

        String input = "input";
        JsonSerializer<String> stringSerializer = objectMapper.findValueSerializer(String.class);
        JsonSerializer<Object> delegatedSerializer = objectMapper.findValueSerializer(Object.class);

        JsonSerializerProvider provider = objectMapper.getSerializerProvider();

        Object value = new Object() {
            @Override
            public String toString() {
                return input;
            }
        };

        JsonGenerator gen = objectMapper.getFactory().createGenerator();
        delegatingSerializer.serialize(value, gen, provider);
        gen.flush();

        String output = gen.getOutputStream().toString();
        assertEquals(input, output);

        // verify that the delegate serializer was used
        assertEquals(stringSerializer, delegatingSerializer.getDelegatee());
    }

    @Test
    public void testSerializeWithType() throws JsonMappingException, JsonGenerationException, IOException {
        Converter<Object, Integer> converter = new SimpleIntegerConverter();
        StdDelegatingSerializer delegatingSerializer = new StdDelegatingSerializer(converter, Integer.class);

        Integer input = 123;
        JsonSerializer<Integer> integerSerializer = objectMapper.findValueSerializer(Integer.class);

        JsonSerializerProvider provider = objectMapper.getSerializerProvider();
        TypeSerializer typeSerializer = provider.getTypeSerializer();

        JsonGenerator gen = objectMapper.getFactory().createGenerator();
        delegatingSerializer.serializeWithType(input, gen, provider, typeSerializer);
        gen.flush();

        String output = gen.getOutputStream().toString();
        assertEquals("123", output);

        // verify that the delegate serializer was used
        assertEquals(integerSerializer, delegatingSerializer.getDelegatee());
    }

    @Test
    public void testResolve() throws JsonMappingException {
        Converter<Object, Integer> converter = new SimpleIntegerConverter();
        StdDelegatingSerializer delegatingSerializer = new StdDelegatingSerializer(converter);

        JsonSerializerProvider provider = objectMapper.getSerializerProvider();

        delegatingSerializer.resolve(provider);

        JsonSerializer<Integer> integerSerializer = objectMapper.findValueSerializer(Integer.class);
        assertEquals(integerSerializer, delegatingSerializer.getDelegatee());
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        Converter<Object, Integer> converter = new SimpleIntegerConverter();
        StdDelegatingSerializer delegatingSerializer = new StdDelegatingSerializer(converter);

        JsonSerializerProvider provider = objectMapper.getSerializerProvider();
        BeanProperty property = new BeanProperty("test", Integer.class);

        JsonSerializer<Integer> integerSerializer = objectMapper.findValueSerializer(Integer.class);

        JsonSerializer<?> serializer = delegatingSerializer.createContextual(provider, property);

        assertEquals(integerSerializer, serializer);
    }

    static class SimpleStringConverter implements Converter<Object, String> {
        @Override
        public String convert(Object o) {
            if (o == null) {
                return null;
            }
            return o.toString();
        }

        @Override
        public Object convertAndSet(String value, Object t) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Object getEmptyValue(SerializerProvider provider) {
            return null;
        }
    }

    static class SimpleIntegerConverter implements Converter<Object, Integer> {
        @Override
        public Integer convert(Object o) {
            if (o == null) {
                return null;
            }
            return ((Number) o).intValue();
        }

        @Override
        public Object convertAndSet(Integer value, Object t) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Object getEmptyValue(SerializerProvider provider) {
            return null;
        }
    }
}